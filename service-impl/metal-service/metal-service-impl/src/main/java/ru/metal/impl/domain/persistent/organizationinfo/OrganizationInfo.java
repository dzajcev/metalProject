package ru.metal.impl.domain.persistent.organizationinfo;

import ru.metal.impl.domain.persistent.Adress;
import ru.metal.impl.domain.persistent.BankRequisites;
import ru.metal.impl.domain.persistent.BaseEntity;

import javax.persistence.*;

/**
 * Created by d.zaitsev on 02.08.2017.
 */
@Entity
@Table(name = "organization_info")
public class OrganizationInfo extends BaseEntity {

    @Column(name = "ORGANIZATION_NAME")
    private String organizationName;

    @Column(name = "DIRECTOR")
    private String director;

    @Column(name = "ACCOUNTANT")
    private String accountant;

    @OneToOne(fetch=FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name="ADDRESS_GUID")
    private Adress adress;

    @OneToOne(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="BANK_REQ_GUID")
    private BankRequisites bankRequisites;


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

    public Adress getAdress() {
        return adress;
    }

    public void setAdress(Adress adress) {
        this.adress = adress;
    }

    public BankRequisites getBankRequisites() {
        return bankRequisites;
    }

    public void setBankRequisites(BankRequisites bankRequisites) {
        this.bankRequisites = bankRequisites;
    }
}
