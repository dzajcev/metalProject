package ru.metal.dto.helper;

import javafx.collections.ObservableList;
import ru.metal.security.ejb.dto.AbstractDto;
import ru.metal.dto.FxEntity;

import java.util.Collection;
import java.util.List;

/**
 * Created by User on 04.09.2017.
 */
public interface FxHelper<FX extends FxEntity<DTO>, DTO extends AbstractDto> {
    FX getFxEntity(DTO dto);

    ObservableList<FX> getFxCollection(List<DTO> collection);

    List<DTO> getDtoCollection(Collection<FX> collection);
}
