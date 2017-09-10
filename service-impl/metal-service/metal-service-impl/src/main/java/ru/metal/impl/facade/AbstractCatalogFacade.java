package ru.metal.impl.facade;

import ru.metal.api.contragents.dto.ContragentGroupDto;
import ru.metal.api.contragents.request.ObtainContragentGroupRequest;
import ru.metal.api.contragents.response.ObtainContragentGroupReponse;
import ru.metal.impl.domain.persistent.catalog.Catalog;
import ru.metal.impl.domain.persistent.contragents.ContragentGroup;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by User on 09.09.2017.
 */
public abstract class AbstractCatalogFacade<T extends Catalog> {

    protected boolean notDel(List<T> groups, T current, Set<String> toDel) {
        boolean result = false;
        if (!current.getItems().isEmpty()) {
            result = true;
        }
        List<T> childs = getChilds(groups, current);
        if (childs.isEmpty()) {
            result = !current.getItems().isEmpty() && true;
        } else {

            for (T group : childs) {
                boolean b = notDel(groups, group, toDel);
                result = result | b;
            }
        }
        if (result) {
            toDel.add(current.getGuid());
        }
        return result;
    }

    protected List<T> getChilds(List<T> groups, T current) {
        List<T> result = new ArrayList<>();
        for (T group : groups) {
            if (group.getGroupGuid() != null && group.getGroupGuid().equals(current.getGuid())) {
                result.add(group);
            }
        }
        return result;
    }

    protected List<T> getRoots(List<T> groups) {
        List<T> result = new ArrayList<>();
        for (T mayBeRoot : groups) {
            boolean isRoot = true;
            for (T group : groups) {
                if (group.getGuid().equals(mayBeRoot.getGroupGuid())) {
                    isRoot = false;
                    break;
                }
            }
            if (isRoot) {
                result.add(mayBeRoot);
            }
        }
        return result;
    }


    protected List<T> getDeletedGroups(List<T> source) {

        List<T> roots = getRoots(source);
        Set<String> notDel = new HashSet<>();
        for (T root : roots) {
            notDel(source, root, notDel);
        }
        List<T> result = new ArrayList<>();
        for (T group : source) {
            if (!notDel.contains(group.getGuid())) {
                result.add(group);
            }
        }
        return result;
    }
}
