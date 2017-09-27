package ru.metal.gui.controllers.catalogs;

import ru.common.api.dto.TableElement;
import ru.common.api.dto.TreeviewElement;

/**
 * Created by User on 25.09.2017.
 */
public interface CatalogSelector<G extends TreeviewElement, I extends TableElement<G>> {
    ItemSelector<I> getItemSelector();

    GroupSelector<G> getGroupSelector();
}
