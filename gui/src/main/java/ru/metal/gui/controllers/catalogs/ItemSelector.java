package ru.metal.gui.controllers.catalogs;

import ru.common.api.dto.TableElement;
import ru.common.api.dto.UpdateResult;
import ru.common.api.request.ObtainAbstractRequest;
import ru.common.api.response.ObtainAbstractResponse;
import ru.common.api.response.UpdateAbstractResponse;

import java.util.List;

/**
 * Created by User on 25.09.2017.
 */
public interface ItemSelector<I extends TableElement> {

    List<I> getItems(List<String> groups, boolean active);

    List<I> getAllItems();

    UpdateAbstractResponse<? extends UpdateResult> updateItems(List<I> items);

}
