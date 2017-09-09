package ru.metal.api.nomenclature.dto;

import ru.metal.api.common.dto.UpdateResult;

/**
 * Created by User on 16.08.2017.
 */
public class UpdateGoodResult extends UpdateResult {
    private String guid;

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }
}
