package ru.metal.impl.facade;


import ru.lanit.hcs.convert.mapper.Mapper;
import ru.metal.api.common.request.ObtainTreeItemRequest;
import ru.metal.api.nomenclature.dto.OkeiDto;
import ru.metal.api.nomenclature.request.ObtainOkeiRequest;
import ru.metal.api.nomenclature.request.UpdateGoodsRequest;
import ru.metal.api.common.request.UpdateTreeItemRequest;
import ru.metal.api.nomenclature.response.*;
import ru.metal.api.nomenclature.NomenclatureFacade;
import ru.metal.api.nomenclature.dto.GoodDto;
import ru.metal.api.nomenclature.dto.GroupDto;
import ru.metal.api.nomenclature.request.ObtainGoodRequest;
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
public class NomenclatureFacadeImpl implements NomenclatureFacade {

    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private Mapper mapper;

    public ObtainGroupReponse getGroups(ObtainTreeItemRequest obtainTreeItemRequest) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Group> cq = cb.createQuery(Group.class);
        Root<Group> root = cq.from(Group.class);
        if (obtainTreeItemRequest.getActive()) {
            cq.where(cb.equal(root.get(Group_.active), true));
        }
        TypedQuery<Group> q = entityManager.createQuery(cq);
        List<Group> results = q.getResultList();
        ObtainGroupReponse obtainGroupReponse = new ObtainGroupReponse();
        obtainGroupReponse.setDataList(mapper.mapCollections(results, GroupDto.class));
        return obtainGroupReponse;
    }

    @Override
    public UpdateGroupResponse updateGroups(UpdateTreeItemRequest<GroupDto> request) {
        UpdateGroupResponse response = new UpdateGroupResponse();
        List<Group> data = mapper.mapCollections(request.getDataList(), Group.class);

        for (Group group : data) {
            entityManager.merge(group);
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
            Join<Good, Group> groups = root.join(Good_.group);
            Predicate predicate = groups.get(Group_.guid).in(obtainGoodRequest.getGroupGuids());
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
        List<Good> data = mapper.mapCollections(request.getDataList(), Good.class);
        for (Good good : data) {

            entityManager.merge(good);

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
