package ru.metal.impl.facade;


import ru.common.api.dto.Error;
import ru.metal.api.documents.order.ErrorCodeEnum;
import ru.metal.api.documents.order.OrderFacade;
import ru.metal.api.documents.order.dto.*;
import ru.metal.api.documents.order.request.DropOrderRequest;
import ru.metal.api.documents.order.request.ObtainOrderRequest;
import ru.metal.api.documents.order.request.UpdateOrderRequest;
import ru.metal.api.documents.order.response.ObtainOrderResponse;
import ru.metal.api.documents.order.response.UpdateOrderResponse;
import ru.metal.convert.mapper.Mapper;
import ru.metal.impl.domain.persistent.contragents.Contragent;
import ru.metal.impl.domain.persistent.contragents.Contragent_;
import ru.metal.impl.domain.persistent.order.*;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by d.zaitsev on 02.08.2017.
 */
@Remote
@Stateless(name = "orderFacade")
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class OrderFacadeImpl implements OrderFacade {

    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private Mapper mapper;

    @Override
    public ObtainOrderResponse getOrders(ObtainOrderRequest request) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<OrderHeader> cq = cb.createQuery(OrderHeader.class);
        Root<OrderHeader> root = cq.from(OrderHeader.class);
        List<Predicate> predicates = new ArrayList<>();
        if (!request.getGuids().isEmpty()) {
            Predicate guidPredicate;
            if (request.getGuids().size() == 1) {
                guidPredicate = cb.equal(root.get(OrderHeader_.guid), request.getGuids().get(0));
            } else {
                guidPredicate = root.get(OrderHeader_.guid).in(request.getGuids());
            }
            predicates.add(guidPredicate);
        }

        if (request.getStartDate() != null) {
            Predicate predicate = cb.greaterThanOrEqualTo(root.get(OrderHeader_.dateOrder), request.getStartDate());
            predicates.add(predicate);
        }
        if (request.getEndDate() != null) {
            Predicate predicate = cb.lessThanOrEqualTo(root.get(OrderHeader_.dateOrder), request.getEndDate());
            predicates.add(predicate);
        }
        if (!request.getNumbers().isEmpty()) {
            Predicate numberPredicate;
            if (request.getNumbers().size() == 1) {
                numberPredicate = cb.equal(root.get(OrderHeader_.number), request.getNumbers().get(0));
            } else {
                numberPredicate = root.get(OrderHeader_.number).in(request.getNumbers());
            }
            predicates.add(numberPredicate);
        }
        if (!request.getSources().isEmpty()) {
            Predicate sourcePredicate;
            Join<OrderHeader, Contragent> source = root.join(OrderHeader_.source);
            if (request.getSources().size() == 1) {
                sourcePredicate = cb.equal(source.get(Contragent_.guid), request.getSources().get(0));
            } else {
                sourcePredicate = source.get(Contragent_.guid).in(request.getSources());
            }
            predicates.add(sourcePredicate);
        }
        if (!request.getRecipients().isEmpty()) {
            Predicate recipientPredicate;
            Join<OrderHeader, Contragent> recipient = root.join(OrderHeader_.recipient);
            if (request.getRecipients().size() == 1) {
                recipientPredicate = cb.equal(recipient.get(Contragent_.guid), request.getRecipients().get(0));
            } else {
                recipientPredicate = recipient.get(Contragent_.guid).in(request.getRecipients());
            }
            predicates.add(recipientPredicate);
        }

        if (!request.getUserGuids().isEmpty()) {
            Predicate userPredicate = root.get(OrderHeader_.userGuid).in(request.getUserGuids());
            predicates.add(userPredicate);
        }

        if (!request.getStatuses().isEmpty()) {
            final Predicate predicate =root.get(OrderHeader_.status).in(request.getStatuses());
            predicates.add(predicate);
        }
        cq.where(cb.and(predicates.toArray(new Predicate[0]))).distinct(true);

        TypedQuery<OrderHeader> q = entityManager.createQuery(cq);
        List<OrderHeader> results = q.getResultList();
        ObtainOrderResponse response = new ObtainOrderResponse();
        response.setDataList(mapper.mapCollections(results, OrderHeaderDto.class));
        return response;
    }

    private void incrementOrderNumber(OrderNumber number) {
        number.setOrderNumber(number.getOrderNumber() + 1);
        entityManager.merge(number);

    }

    private OrderNumber getOrderNumber(String suffix) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<OrderNumber> cq = cb.createQuery(OrderNumber.class);

        Root<OrderNumber> root = cq.from(OrderNumber.class);
        if (suffix != null) {
            cq.where(cb.equal(root.get(OrderNumber_.suffix), suffix));
        } else {
            cq.where(cb.isNull(root.get(OrderNumber_.suffix)));
        }
        TypedQuery<OrderNumber> q = entityManager.createQuery(cq).setLockMode(LockModeType.PESSIMISTIC_WRITE);
        OrderNumber orderNumber = q.getSingleResult();
        System.out.println(orderNumber.getNumber() + Thread.currentThread().getName());
        return orderNumber;
    }

    @Override
    public UpdateOrderResponse updateOrders(UpdateOrderRequest updateOrderRequest) {
        UpdateOrderResponse response = new UpdateOrderResponse();
        for (OrderHeaderDto dto : updateOrderRequest.getDataList()) {
            OrderHeader orderHeader = mapper.map(dto, OrderHeader.class);
            for (OrderBody body : orderHeader.getBody()) {
                body.setOrder(orderHeader);
            }

            //todo: может и введу разделение по суффиксам
            if (orderHeader.getNumber() == null) {
                OrderNumber orderNumber = getOrderNumber(null);
                orderHeader.setNumber(orderNumber.getNumber());
                incrementOrderNumber(orderNumber);
            }
            OrderHeader merge = entityManager.merge(orderHeader);

            UpdateOrderResult updateOrderResult = new UpdateOrderResult();
            updateOrderResult.setGuid(merge.getGuid());
            updateOrderResult.setTransportGuid(dto.getTransportGuid());
            updateOrderResult.setOrderNumber(merge.getNumber());
            for (OrderBodyDto bodyDto : dto.getBody()) {
                UpdateBodyResult updateBodyResult = new UpdateBodyResult();
                for (OrderBody body : merge.getBody()) {
                    if (body.getGood().getGuid().equals(bodyDto.getGood().getGuid())) {
                        updateBodyResult.setTransportGuid(bodyDto.getTransportGuid());
                        updateBodyResult.setGuid(body.getGuid());
                        updateOrderResult.getBodyResults().add(updateBodyResult);
                        break;
                    }
                }
            }
            response.getImportResults().add(updateOrderResult);
        }
        return response;
    }

    @Override
    public UpdateOrderResponse dropOrders(DropOrderRequest dropOrderRequest) {
        UpdateOrderResponse response=new UpdateOrderResponse();
        for (String guid:dropOrderRequest.getDataList()){
            OrderHeader orderHeader = entityManager.find(OrderHeader.class, guid);
            UpdateOrderResult updateOrderResult = new UpdateOrderResult();
            updateOrderResult.setGuid(guid);
            if (orderHeader==null){
                Error error = new Error(ErrorCodeEnum.ORDER002, guid);
                updateOrderResult.getErrors().add(error);
            }
            if (orderHeader.getStatus()!= OrderStatus.DRAFT){
                Error error = new Error(ErrorCodeEnum.ORDER001, guid);
                updateOrderResult.getErrors().add(error);
            }
            if (updateOrderResult.getErrors().isEmpty()){
                entityManager.remove(orderHeader);
            }
            response.getImportResults().add(updateOrderResult);
        }
        return response;
    }
}
