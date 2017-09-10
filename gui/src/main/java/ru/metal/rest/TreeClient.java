package ru.metal.rest;

import ru.metal.api.common.dto.TreeviewElement;
import ru.metal.api.common.request.DeleteTreeItemRequest;
import ru.metal.api.common.request.ObtainTreeItemRequest;
import ru.metal.api.common.request.UpdateTreeItemRequest;
import ru.metal.api.common.response.DeleteTreeItemResponse;
import ru.metal.dto.response.ObtainTreeItemResponse;
import ru.metal.api.common.response.UpdateTreeItemResponse;
import ru.metal.exceptions.ServerErrorException;

/**
 * Created by User on 15.08.2017.
 */
public interface TreeClient<T extends TreeviewElement> {

    ObtainTreeItemResponse<T> getItems(ObtainTreeItemRequest obtainTreeItemRequest) throws ServerErrorException;

    UpdateTreeItemResponse updateItems(UpdateTreeItemRequest<T> updateTreeItemRequest)throws ServerErrorException;

    UpdateTreeItemResponse deleteItem(DeleteTreeItemRequest<T> deleteTreeItemRequest) throws ServerErrorException;

}
