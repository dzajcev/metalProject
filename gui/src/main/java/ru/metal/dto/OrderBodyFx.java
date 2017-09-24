package ru.metal.dto;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import ru.metal.api.documents.order.dto.OrderBodyDto;
import ru.metal.dto.annotations.ValidatableField;
import ru.metal.dto.helper.FxHelper;
import ru.metal.dto.helper.OrderBodyHelper;

/**
 * Created by User on 30.08.2017.
 */
public class OrderBodyFx extends FxEntity<OrderBodyDto> {

    @ValidatableField(nullable = false)
    private ObjectProperty<GoodFx> good=new SimpleObjectProperty<>();

    @ValidatableField(nullable = false, regexp = Formats.POSITIVE_DECIMAL)
    private DoubleProperty price=new SimpleDoubleProperty();

    @ValidatableField(nullable = false, regexp = Formats.POSITIVE_DECIMAL)
    private DoubleProperty count=new SimpleDoubleProperty();

    public GoodFx getGood() {
        return good.get();
    }

    public ObjectProperty<GoodFx> goodProperty() {
        return good;
    }

    public void setGood(GoodFx good) {
        this.good.set(good);
    }

    public double getPrice() {
        return price.get();
    }

    public DoubleProperty priceProperty() {
        return price;
    }

    public void setPrice(double price) {
        this.price.set(price);
    }

    public double getCount() {
        return count.get();
    }

    public DoubleProperty countProperty() {
        return count;
    }

    public void setCount(double count) {
        this.count.set(count);
    }

    @Override
    public OrderBodyDto getEntity() {
        OrderBodyDto dto=new OrderBodyDto();
        dto.setCount(getCount());
        if (getGood()!=null) {
            dto.setGood(getGood().getEntity());
        }
        dto.setPrice(getPrice());
        dto.setGuid(getGuid());
        dto.setLastEditingDate(getLastEditingDate());
        dto.setTransportGuid(getTransportGuid());
        return dto;
    }

    @Override
    public FxHelper<OrderBodyFx, OrderBodyDto> getHelper() {
        return OrderBodyHelper.getInstance();
    }
}
