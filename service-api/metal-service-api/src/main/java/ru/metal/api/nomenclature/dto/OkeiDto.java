package ru.metal.api.nomenclature.dto;

import ru.metal.security.ejb.dto.AbstractDto;

/**
 * Created by User on 09.08.2017.
 */
public class OkeiDto extends AbstractDto {

    private String name;

    private Boolean isActive;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OkeiDto)) return false;

        OkeiDto okeiDto = (OkeiDto) o;

        return getGuid() != null ? getGuid().equals(okeiDto.getGuid()) : okeiDto.getGuid() == null;
    }

    @Override
    public int hashCode() {
        return getGuid() != null ? getGuid().hashCode() : 0;
    }
}
