package ru.metal.impl.domain.persistent.contragents;

import ru.metal.api.contragents.dto.ContragentType;
import ru.metal.api.contragents.dto.PersonType;
import ru.metal.impl.domain.persistent.BaseEntity;

import javax.persistence.*;
import java.util.List;

/**
 * Created by User on 29.08.2017.
 */
@Entity
@Table(name = "CONTRAGENTS")
public class Contragent extends BaseEntity {
    @Column(name = "NAME", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GROUP_GUID",nullable = false)
    private ContragentGroup group;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Employee> employees;

    @ManyToOne
    @JoinColumn(name = "SHIPPER")
    private Contragent shipper;

    @Column(name = "ALIAS")
    private String alias;

    @Column(name = "INN", nullable = false)
    private String inn;

    @Column(name = "KPP")
    private String kpp;

    @Column(name = "ADDRESS", nullable = false)
    private String address;

    @Column(name = "ACCOUNT", nullable = false)
    private String account;

    @Column(name = "BIK", nullable = false)
    private String bik;

    @Column(name = "BANK", nullable = false)
    private String bank;

    @Column(name = "KORR_ACCOUNT", nullable = false)
    private String korrAccount;

    @Column(name = "COMMENT")
    private String comment;

    @Column(name = "ACTIVE")
    private boolean active;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "OGRN")
    private String ogrn;

    @Column(name = "OKPO")
    private String okpo;

    @Column(name = "OKVED")
    private String okved;

    @ManyToOne(cascade = CascadeType.ALL)
    private Employee director;

    @ManyToOne(cascade = CascadeType.ALL)
    private Employee accountant;

    @Column(name = "PERSON_TYPE")
    @Enumerated(EnumType.STRING)
    private PersonType personType;

    @ElementCollection(targetClass = ContragentType.class, fetch = FetchType.EAGER)
    @CollectionTable(name="CONTRAGENT_TYPES")
    @Column(name = "CONTRAGENT_TYPE", nullable = false)
    @Enumerated(EnumType.STRING)
    private List<ContragentType> contragentTypes;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Document> documents;

    public void setGroup(ContragentGroup group) {
        this.group = group;
    }

    public ContragentGroup getGroup() {
        return group;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }

    public String getKpp() {
        return kpp;
    }

    public void setKpp(String kpp) {
        this.kpp = kpp;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getBik() {
        return bik;
    }

    public void setBik(String bik) {
        this.bik = bik;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getKorrAccount() {
        return korrAccount;
    }

    public void setKorrAccount(String korrAccount) {
        this.korrAccount = korrAccount;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOgrn() {
        return ogrn;
    }

    public void setOgrn(String ogrn) {
        this.ogrn = ogrn;
    }

    public String getOkpo() {
        return okpo;
    }

    public void setOkpo(String okpo) {
        this.okpo = okpo;
    }

    public String getOkved() {
        return okved;
    }

    public void setOkved(String okved) {
        this.okved = okved;
    }

    public Contragent getShipper() {
        return shipper;
    }

    public void setShipper(Contragent shipper) {
        this.shipper = shipper;
    }

    public PersonType getPersonType() {
        return personType;
    }

    public void setPersonType(PersonType personType) {
        this.personType = personType;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public Employee getDirector() {
        return director;
    }

    public void setDirector(Employee director) {
        this.director = director;
    }

    public Employee getAccountant() {
        return accountant;
    }

    public void setAccountant(Employee accountant) {
        this.accountant = accountant;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public List<ContragentType> getContragentTypes() {
        return contragentTypes;
    }

    public void setContragentTypes(List<ContragentType> contragentTypes) {
        this.contragentTypes = contragentTypes;
    }
}
