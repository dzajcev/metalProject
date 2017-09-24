package ru.metal.dto.helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.metal.api.documents.order.dto.OrderBodyDto;
import ru.metal.dto.OrderBodyFx;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by User on 01.09.2017.
 */
public class OrderBodyHelper implements FxHelper<OrderBodyFx,OrderBodyDto> {
    private static volatile OrderBodyHelper instance;

    public static OrderBodyHelper getInstance() {
        OrderBodyHelper localInstance = instance;
        if (localInstance == null) {
            synchronized (OrderBodyHelper.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new OrderBodyHelper();
                }
            }
        }
        return localInstance;
    }

    private OrderBodyHelper(){}


    @Override
    public OrderBodyFx getFxEntity(OrderBodyDto dto) {
        if (dto == null) {
            return null;
        }
        OrderBodyFx orderBody = new OrderBodyFx();
        orderBody.setGuid(dto.getGuid());
        orderBody.setLastEditingDate(dto.getLastEditingDate());
        orderBody.setTransportGuid(dto.getTransportGuid());
        orderBody.setCount(dto.getCount());
        orderBody.setGood(GoodHelper.getInstance().getFxEntity(dto.getGood()));
        orderBody.setPrice(dto.getPrice());
        return orderBody;
    }

    @Override
    public ObservableList<OrderBodyFx> getFxCollection(List<OrderBodyDto> collection) {
        ObservableList<OrderBodyFx> result = FXCollections.observableArrayList();
        for (OrderBodyDto dto : collection) {
            result.add(getFxEntity(dto));
        }
        return result;
    }

    @Override
    public List<OrderBodyDto> getDtoCollection(Collection<OrderBodyFx> collection) {
        List<OrderBodyDto> result = new ArrayList<>();
        for (OrderBodyFx dto : collection) {
            result.add(dto.getEntity());
        }
        return result;
    }
}
