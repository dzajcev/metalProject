package ru.metal.gui.controllers.catalogs;

import ru.common.api.dto.TreeviewElement;
import ru.common.api.dto.UpdateResult;
import ru.common.api.request.DeleteTreeItemRequest;
import ru.common.api.request.UpdateTreeItemRequest;
import ru.common.api.response.UpdateTreeItemResponse;

import java.util.List;

/**
 * Created by User on 25.09.2017.
 */
public interface GroupSelector<G extends TreeviewElement> {

    List<G> getGroups(boolean active);

    UpdateTreeItemResponse<? extends UpdateResult> updateGroups(List<G> list);

    UpdateTreeItemResponse deleteGroup(List<String> request);
}
