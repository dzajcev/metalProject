package ru.metal.impl.facade;


import ru.lanit.hcs.convert.mapper.Mapper;
import ru.metal.api.common.request.ObtainTreeItemRequest;
import ru.metal.api.common.request.UpdateTreeItemRequest;
import ru.metal.api.contragents.ContragentsFacade;
import ru.metal.api.contragents.dto.*;
import ru.metal.api.contragents.request.ObtainContragentRequest;
import ru.metal.api.contragents.request.UpdateContragentRequest;
import ru.metal.api.contragents.request.UpdateEmployeeRequest;
import ru.metal.api.contragents.response.*;
import ru.metal.impl.domain.persistent.contragents.*;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.*;

/**
 * Created by d.zaitsev on 02.08.2017.
 */
@Remote
@Stateless(name = "contragentsFacade")
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class ContragentsFacadeImpl implements ContragentsFacade {

    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private Mapper mapper;

    public ObtainContragentGroupReponse getGroups(ObtainContragentRequest obtainTreeItemRequest) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<ContragentGroup> cq = cb.createQuery(ContragentGroup.class);
        Root<ContragentGroup> root = cq.from(ContragentGroup.class);
        List<Predicate> predicates = new ArrayList<>();
        if (obtainTreeItemRequest.getActive()) {
            predicates.add(cb.equal(root.get(ContragentGroup_.active), true));
        }
        if (!obtainTreeItemRequest.getPersonTypes().isEmpty()){
            Join<ContragentGroup, Contragent> contragents = root.join(ContragentGroup_.contragents);
            Predicate criteria = contragents.get(Contragent_.personType).in(obtainTreeItemRequest.getPersonTypes());
            predicates.add(criteria);
        }
        if (!obtainTreeItemRequest.getContragentTypes().isEmpty()){
            Join<ContragentGroup, Contragent> contragents = root.join(ContragentGroup_.contragents);
            final ListJoin<Contragent, ContragentType> status = contragents.joinList("contragentTypes");
            final Predicate predicate = status.in(obtainTreeItemRequest.getContragentTypes());
            predicates.add(predicate);
        }
        cq.where(cb.and(predicates.toArray(new Predicate[0]))).distinct(true);
        TypedQuery<ContragentGroup> q = entityManager.createQuery(cq);
        List<ContragentGroup> results = q.getResultList();
        ObtainContragentGroupReponse obtainContragentGroupReponse = new ObtainContragentGroupReponse();
        obtainContragentGroupReponse.setDataList(mapper.mapCollections(results, ContragentGroupDto.class));
        return obtainContragentGroupReponse;
    }

    @Override
    public UpdateContragentGroupResponse updateGroups(UpdateTreeItemRequest<ContragentGroupDto> request) {
        UpdateContragentGroupResponse response = new UpdateContragentGroupResponse();
        List<ContragentGroup> data = mapper.mapCollections(request.getDataList(), ContragentGroup.class);

        for (ContragentGroup group : data) {
            entityManager.merge(group);
        }

        return response;
    }

    @Override
    public ObtainContragentResponse getContragents(ObtainContragentRequest request) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<Contragent> cq = cb.createQuery(Contragent.class);
        Root<Contragent> root = cq.from(Contragent.class);
        List<Predicate> predicates = new ArrayList<>();
        if (request.getActive()) {
            Predicate predicate = cb.equal(root.get(Contragent_.active), true);
            predicates.add(predicate);
        }
        if (!request.getContragentTypes().isEmpty()) {
            final ListJoin<Contragent, ContragentType> status = root.joinList("contragentTypes");
            final Predicate predicate = status.in(request.getContragentTypes());
            predicates.add(predicate);
        }
        if (!request.getPersonTypes().isEmpty()) {
            Predicate criteria = root.get(Contragent_.personType).in(request.getPersonTypes());
            predicates.add(criteria);
        }
        if (!request.getGroupGuids().isEmpty()) {
            Join<Contragent, ContragentGroup> groups = root.join(Contragent_.group);
            Predicate predicate = groups.get(ContragentGroup_.guid).in(request.getGroupGuids());
            predicates.add(predicate);
        }
        cq.where(cb.and(predicates.toArray(new Predicate[0]))).distinct(true);

        TypedQuery<Contragent> q = entityManager.createQuery(cq);
        List<Contragent> results = q.getResultList();
        ObtainContragentResponse obtainContragentResponse = new ObtainContragentResponse();
        List<ContragentDto> contragentList = mapper.mapCollections(results, ContragentDto.class);
        for (ContragentDto dto:contragentList){
            if (dto.getEntrepreneur().getDocuments().isEmpty()){
                dto.setEntrepreneur(null);
            }
        }
        obtainContragentResponse.setDataList(contragentList);

        return obtainContragentResponse;
    }

    @Override
    public UpdateContragentResponse updateContragents(UpdateContragentRequest request) {
        UpdateContragentResponse response = new UpdateContragentResponse();

        for (ContragentDto contragentDto : request.getDataList()) {
            Map<String, EmployeeDto> employees = new HashMap<>();
            for (EmployeeDto employeeDto : contragentDto.getEmployees()) {
                employees.put(employeeDto.getTransportGuid(), employeeDto);
            }
            if (contragentDto.getAccountant() != null) {
                contragentDto.setAccountant(employees.get(contragentDto.getAccountant().getTransportGuid()));
            }
            if (contragentDto.getDirector() != null) {
                contragentDto.setDirector(employees.get(contragentDto.getDirector().getTransportGuid()));
            }
            Contragent contragent = mapper.map(contragentDto, Contragent.class);
            Contragent merge = entityManager.merge(contragent);
            UpdateContragentResult updateContragentResult = new UpdateContragentResult();
            updateContragentResult.setGuid(merge.getGuid());
            updateContragentResult.setTransportGuid(contragentDto.getTransportGuid());
            response.getImportResults().add(updateContragentResult);
        }

        return response;
    }

    @Override
    public UpdateEmployeeResponse updateEmployee(UpdateEmployeeRequest request) {
        UpdateEmployeeResponse response = new UpdateEmployeeResponse();
        for (EmployeeDto employee : request.getDataList()) {
            Employee merge = entityManager.merge(mapper.map(employee, Employee.class));
            UpdateEmployeeResult updateEmployeeResult = new UpdateEmployeeResult();
            updateEmployeeResult.setTransportGuid(employee.getTransportGuid());
            updateEmployeeResult.setGuid(merge.getGuid());
            response.getImportResults().add(updateEmployeeResult);
        }
        return response;
    }

    private Employee mergeEmployee(Employee employee) {
        return entityManager.merge(employee);
    }
}
