package ru.metal.gui.controllers.catalogs.nomenclature;

import ru.metal.dto.GoodFx;
import ru.metal.dto.GroupFx;
import ru.metal.gui.controllers.catalogs.CatalogSelector;
import ru.metal.gui.controllers.catalogs.GroupSelector;
import ru.metal.gui.controllers.catalogs.ItemSelector;
import ru.metal.rest.NomenclatureClient;

/**
 * Created by User on 25.09.2017.
 */
public class NomenclatureSelector implements CatalogSelector<GroupFx, GoodFx> {
    private final NomenclatureClient nomenclatureClient = new NomenclatureClient();

    private final NomenclatureItemSelector nomenclatureItemSelector;
    private final NomenclatureGroupSelector nomenclatureGroupSelector;

    public NomenclatureSelector() {
        nomenclatureItemSelector = new NomenclatureItemSelector(nomenclatureClient);
        nomenclatureGroupSelector = new NomenclatureGroupSelector(nomenclatureClient);
    }


    @Override
    public ItemSelector<GoodFx> getItemSelector() {
        return nomenclatureItemSelector;
    }

    @Override
    public GroupSelector<GroupFx> getGroupSelector() {
        return nomenclatureGroupSelector;
    }

}
