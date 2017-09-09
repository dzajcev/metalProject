package ru.metal.api.organizationinfo.dto;

import ru.metal.api.common.dto.AbstractDto;
import ru.metal.api.common.dto.AdressDto;
import ru.metal.api.common.dto.BankRequisitesDto;

/**
 * Created by d.zaitsev on 02.08.2017.
 */
public class OrganizationInfoDto extends AbstractDto {
    private String organizationName;

    private String director;

    private String accountant;

    private AdressDto adress;

    private BankRequisitesDto bankRequisites;

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getAccountant() {
        return accountant;
    }

    public void setAccountant(String accountant) {
        this.accountant = accountant;
    }

    public AdressDto getAdress() {
        return adress;
    }

    public void setAdress(AdressDto adress) {
        this.adress = adress;
    }

    public BankRequisitesDto getBankRequisites() {
        return bankRequisites;
    }

    public void setBankRequisites(BankRequisitesDto bankRequisites) {
        this.bankRequisites = bankRequisites;
    }
}
