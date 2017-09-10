package ru.metal.impl.domain.persistent.catalog;

import ru.metal.impl.domain.persistent.BaseEntity;

import java.util.List;

/**
 * Created by User on 10.09.2017.
 */
public interface Catalog<T extends BaseEntity> {

    List<T> getItems();

    String getGuid();

    String getGroupGuid();

}
