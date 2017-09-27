package ru.metal.gui.controllers.catalogs.contragents;

import ru.metal.api.contragents.dto.ContragentDto;
import ru.metal.api.contragents.dto.ContragentGroupDto;
import ru.metal.api.contragents.request.ObtainContragentRequest;
import ru.metal.dto.ContragentFx;
import ru.metal.dto.ContragentGroupFx;
import ru.metal.gui.controllers.catalogs.CatalogSelector;
import ru.metal.gui.controllers.catalogs.GroupSelector;
import ru.metal.gui.controllers.catalogs.ItemSelector;
import ru.metal.gui.controllers.catalogs.SelectCriteria;
import ru.metal.rest.ContragentsClient;

/**
 * Created by User on 25.09.2017.
 */
public class ContragentSelector implements CatalogSelector<ContragentGroupFx, ContragentFx> {
    private final ContragentsClient contragentsClient = new ContragentsClient();
    private final ContragentItemSelector contragentItemSelector;
    private final ContragentGroupSelector contragentGroupSelector;

    public ContragentSelector(ContragentBaseCriteria criteria) {
        contragentItemSelector= new ContragentItemSelector(contragentsClient, criteria);
        contragentGroupSelector = new ContragentGroupSelector(contragentsClient, criteria);
    }


    @Override
    public ItemSelector<ContragentFx> getItemSelector() {
        return contragentItemSelector;
    }

    @Override
    public GroupSelector<ContragentGroupFx> getGroupSelector() {
        return contragentGroupSelector;
    }

}
