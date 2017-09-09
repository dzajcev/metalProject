package ru.metal.impl.facade;


import ru.lanit.hcs.convert.mapper.Mapper;
import ru.metal.api.common.dto.Error;
import ru.metal.api.organizationinfo.ErrorCodeEnum;
import ru.metal.api.organizationinfo.OrganizationInfoFacade;
import ru.metal.api.organizationinfo.dto.OrganizationInfoDto;
import ru.metal.api.organizationinfo.request.UpdateOrganizationRequest;
import ru.metal.api.organizationinfo.response.ObtainOrganizationInfoResponse;
import ru.metal.api.organizationinfo.response.UpdateOrganizationResponse;
import ru.metal.impl.domain.persistent.organizationinfo.OrganizationInfo;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

/**
 * Created by d.zaitsev on 02.08.2017.
 */
@Remote
@Stateless(name = "organizationInfoFacade")
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class OrganizationInfoFacadeImpl implements OrganizationInfoFacade {

    @PersistenceContext
    private EntityManager entityManager;


    @Inject
    private Mapper mapper;


    public ObtainOrganizationInfoResponse getOrganizationInfo() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<OrganizationInfo> cq = cb.createQuery(OrganizationInfo.class);
        cq.from(OrganizationInfo.class);

        TypedQuery<OrganizationInfo> q = entityManager.createQuery(cq);
        OrganizationInfo result;
        ObtainOrganizationInfoResponse obtainOrganizationInfoResponse=new ObtainOrganizationInfoResponse();
        try {
            result = q.getSingleResult();
            obtainOrganizationInfoResponse.getDataList().add(mapper.map(result, OrganizationInfoDto.class));
        }catch (NoResultException e){
            Error error=new Error(ErrorCodeEnum.ORG001);
            obtainOrganizationInfoResponse.getErrors().add(error);
        }


        return obtainOrganizationInfoResponse;
    }

    @Override
    public UpdateOrganizationResponse updateOrganizationInfo(UpdateOrganizationRequest updateOrganizationRequest) {
        UpdateOrganizationResponse updateOrganizationResponse=new UpdateOrganizationResponse();
        ObtainOrganizationInfoResponse organizationInfoResponse = getOrganizationInfo();
        if (updateOrganizationRequest.getDataList().isEmpty()) {
            return updateOrganizationResponse;
        }
        OrganizationInfo updateOrganizationInfo = mapper.map(updateOrganizationRequest.getDataList().get(0), OrganizationInfo.class);
        if (!organizationInfoResponse.getErrors().isEmpty()){
            entityManager.persist(updateOrganizationInfo);
        }else{
            OrganizationInfoDto organizationInfo = organizationInfoResponse.getDataList().get(0);
            updateOrganizationInfo.setGuid(organizationInfo.getGuid());
            if (organizationInfo.getBankRequisites()!=null){
                updateOrganizationInfo.getBankRequisites().setGuid(organizationInfo.getBankRequisites().getGuid());
            }
            if (organizationInfo.getAdress()!=null){
                updateOrganizationInfo.getAdress().setGuid(organizationInfo.getAdress().getGuid());
            }
            entityManager.merge(updateOrganizationInfo);
        }

        return updateOrganizationResponse;
    }

}
