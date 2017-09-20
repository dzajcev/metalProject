package ru.metal.dto.helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.metal.api.order.dto.OrderHeaderDto;
import ru.metal.dto.OrderHeaderFx;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by User on 01.09.2017.
 */
public class OrderHeaderHelper implements FxHelper<OrderHeaderFx,OrderHeaderDto> {
    private static volatile OrderHeaderHelper instance;

    public static OrderHeaderHelper getInstance() {
        OrderHeaderHelper localInstance = instance;
        if (localInstance == null) {
            synchronized (OrderHeaderHelper.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new OrderHeaderHelper();
                }
            }
        }
        return localInstance;
    }

    private OrderHeaderHelper(){}


    @Override
    public OrderHeaderFx getFxEntity(OrderHeaderDto dto) {
        if (dto == null) {
            return null;
        }
        OrderHeaderFx orderHeader = new OrderHeaderFx();
        orderHeader.setGuid(dto.getGuid());
        orderHeader.setLastEditingDate(dto.getLastEditingDate());
        orderHeader.setTransportGuid(dto.getTransportGuid());
        orderHeader.setUserGuid(dto.getUserGuid());
        orderHeader.setSource(ContragentHelper.getInstance().getFxEntity(dto.getSource()));
        orderHeader.setRecipient(ContragentHelper.getInstance().getFxEntity(dto.getRecipient()));
        orderHeader.setNumber(dto.getNumber());
        orderHeader.setDateDocument(dto.getDateOrder());
        orderHeader.setCreateDate(dto.getCreateDate());
        orderHeader.setComment(dto.getComment());
        orderHeader.setActive(dto.isActive());
        orderHeader.setOrderBody(OrderBodyHelper.getInstance().getFxCollection(dto.getBody()));
        return orderHeader;
    }

    @Override
    public ObservableList<OrderHeaderFx> getFxCollection(List<OrderHeaderDto> collection) {
        ObservableList<OrderHeaderFx> result = FXCollections.observableArrayList();
        for (OrderHeaderDto dto : collection) {
            result.add(getFxEntity(dto));
        }
        return result;
    }

    @Override
    public List<OrderHeaderDto> getDtoCollection(Collection<OrderHeaderFx> collection) {
        List<OrderHeaderDto> result = new ArrayList<>();
        for (OrderHeaderFx dto : collection) {
            result.add(dto.getEntity());
        }
        return result;
    }
}
