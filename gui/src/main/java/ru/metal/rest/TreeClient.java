package ru.metal.rest;

import ru.common.api.dto.TreeviewElement;
import ru.common.api.request.DeleteTreeItemRequest;
import ru.common.api.request.ObtainTreeItemRequest;
import ru.common.api.request.UpdateTreeItemRequest;
import ru.common.api.response.UpdateTreeItemResponse;
import ru.metal.dto.response.ObtainTreeItemResponse;

/**
 * Created by User on 15.08.2017.
 */
public interface TreeClient<T extends TreeviewElement> {

    ObtainTreeItemResponse<T> getItems(ObtainTreeItemRequest obtainTreeItemRequest);

    UpdateTreeItemResponse updateItems(UpdateTreeItemRequest<T> updateTreeItemRequest);

    UpdateTreeItemResponse deleteItem(DeleteTreeItemRequest<T> deleteTreeItemRequest);

}
