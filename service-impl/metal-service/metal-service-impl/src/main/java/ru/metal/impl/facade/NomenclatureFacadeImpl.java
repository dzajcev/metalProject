package ru.metal.impl.facade;


import ru.common.api.dto.Error;
import ru.common.api.request.DeleteTreeItemRequest;
import ru.common.api.request.ObtainTreeItemRequest;
import ru.common.api.request.UpdateTreeItemRequest;
import ru.metal.api.nomenclature.ErrorCodeEnum;
import ru.metal.api.nomenclature.NomenclatureFacade;
import ru.metal.api.nomenclature.dto.*;
import ru.metal.api.nomenclature.request.ObtainGoodRequest;
import ru.metal.api.nomenclature.request.ObtainOkeiRequest;
import ru.metal.api.nomenclature.request.UpdateGoodsRequest;
import ru.metal.api.nomenclature.response.*;
import ru.metal.convert.mapper.Mapper;
import ru.metal.impl.domain.persistent.nomenclature.*;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by d.zaitsev on 02.08.2017.
 */
@Remote
@Stateless(name = "nomenclatureFacade")
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class NomenclatureFacadeImpl extends AbstractCatalogFacade<GoodGroup> implements NomenclatureFacade {

    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private Mapper mapper;

    private List<GoodGroup> getEntityGroups(ObtainTreeItemRequest obtainTreeItemRequest) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<GoodGroup> cq = cb.createQuery(GoodGroup.class);
        Root<GoodGroup> root = cq.from(GoodGroup.class);
        List<Predicate> predicates = new ArrayList<>();
        if (obtainTreeItemRequest.getActive()) {
            predicates.add(cb.equal(root.get(GoodGroup_.active), true));
        }
        if (!obtainTreeItemRequest.getGroupGuids().isEmpty()) {
            predicates.add(root.get(GoodGroup_.guid).in(obtainTreeItemRequest.getGroupGuids()));
        }

        cq.where(cb.and(predicates.toArray(new Predicate[0]))).distinct(true);
        TypedQuery<GoodGroup> q = entityManager.createQuery(cq);
        List<GoodGroup> results = q.getResultList();
        return results;
    }


    public ObtainGroupReponse getGroups(ObtainTreeItemRequest obtainTreeItemRequest) {
        ObtainGroupReponse obtainGroupReponse = new ObtainGroupReponse();
        obtainGroupReponse.setDataList(mapper.mapCollections(getEntityGroups(obtainTreeItemRequest), GroupDto.class));
        return obtainGroupReponse;
    }

    @Override
    public UpdateGoodGroupResponse updateGroups(UpdateTreeItemRequest<GroupDto> request) {
        UpdateGoodGroupResponse response = new UpdateGoodGroupResponse();
        List<GoodGroup> data = mapper.mapCollections(request.getDataList(), GoodGroup.class);

        for (GoodGroup group : data) {
            if (!group.getActive()) {
                ObtainGoodRequest obtainGoodRequest = new ObtainGoodRequest();
                obtainGoodRequest.getGroupGuids().add(group.getGuid());
                obtainGoodRequest.setActive(true);
                ObtainGoodResponse goods = getGoods(obtainGoodRequest);

                if (!goods.getDataList().isEmpty()) {
                    UpdateGoodsRequest goodRequest = new UpdateGoodsRequest();
                    for (GoodDto goodDto : goods.getDataList()) {
                        goodDto.setActive(false);
                        goodRequest.getDataList().add(goodDto);
                    }
                    updateGoods(goodRequest);
                }
            }
            entityManager.merge(group);
        }

        return response;
    }

    @Override
    public UpdateGoodGroupResponse deleteGroups(DeleteTreeItemRequest request) {
        ObtainTreeItemRequest obtainGoodGroupRequest = new ObtainTreeItemRequest();
        obtainGoodGroupRequest.setGroupGuids(request.getDataList());
        List<GoodGroup> groups = getEntityGroups(obtainGoodGroupRequest);
        List<GoodGroup> deleted = getDeletedGroups(groups);
        UpdateGoodGroupResponse response = new UpdateGoodGroupResponse();

        if (!deleted.isEmpty()) {
            for (GoodGroup group : deleted) {
                UpdateGoodGroupResult updateGoodGroupResult = new UpdateGoodGroupResult();
                updateGoodGroupResult.setGuid(group.getGuid());
                response.getImportResults().add(updateGoodGroupResult);
                entityManager.remove(group);
            }
        }

        for (GoodGroup group : groups) {
            if (!deleted.contains(group.getGuid()) && !group.getItems().isEmpty()) {
                Error error = new Error(ErrorCodeEnum.NOMENCLATURE001, group.getName());
                UpdateGoodGroupResult updateGroupResult = new UpdateGoodGroupResult();
                updateGroupResult.setGuid(group.getGuid());
                updateGroupResult.getErrors().add(error);
                response.getImportResults().add(updateGroupResult);
            }
        }

        return response;
    }

    @Override
    public ObtainGoodResponse getGoods(ObtainGoodRequest obtainGoodRequest) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<Good> cq = cb.createQuery(Good.class);
        Root<Good> root = cq.from(Good.class);
        List<Predicate> predicates = new ArrayList<>();
        if (obtainGoodRequest.getActive()) {
            Predicate predicate = cb.equal(root.get(Good_.active), true);
            predicates.add(predicate);
        }
        if (!obtainGoodRequest.getGroupGuids().isEmpty()) {
            Join<Good, GoodGroup> groups = root.join(Good_.group);
            Predicate predicate = groups.get(GoodGroup_.guid).in(obtainGoodRequest.getGroupGuids());
            predicates.add(predicate);
        }
        cq.where(predicates.toArray(new Predicate[0])).distinct(true);

        TypedQuery<Good> q = entityManager.createQuery(cq);
        List<Good> results = q.getResultList();
        ObtainGoodResponse getGroupReponse = new ObtainGoodResponse();
        getGroupReponse.setDataList(mapper.mapCollections(results, GoodDto.class));
        return getGroupReponse;
    }

    @Override
    public UpdateGoodsResponse updateGoods(UpdateGoodsRequest request) {
        UpdateGoodsResponse response = new UpdateGoodsResponse();
        for (GoodDto good : request.getDataList()) {
            Good merge = entityManager.merge(mapper.map(good,Good.class));
            UpdateGoodResult updateGoodResult=new UpdateGoodResult();
            updateGoodResult.setGuid(merge.getGuid());
            updateGoodResult.setTransportGuid(good.getTransportGuid());
            response.getImportResults().add(updateGoodResult);
        }
        return response;
    }

    public ObtainOkeiResponse getOkei(ObtainOkeiRequest obtainOkeiRequest) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<Okei> cq = cb.createQuery(Okei.class);
        Root<Okei> root = cq.from(Okei.class);
        TypedQuery<Okei> q = entityManager.createQuery(cq);
        List<Okei> results = q.getResultList();
        ObtainOkeiResponse obtainOkeiReponse = new ObtainOkeiResponse();
        obtainOkeiReponse.setDataList(mapper.mapCollections(results, OkeiDto.class));
        return obtainOkeiReponse;
    }
}
