package ru.metal.api.contragents.dto;

import ru.common.api.dto.AbstractDto;
import ru.common.api.dto.TableElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 29.08.2017.
 */
public class ContragentDto extends AbstractDto implements Cloneable, TableElement<ContragentGroupDto> {

    private String name;

    private ContragentGroupDto group;

    private String alias;

    private String inn;

    private String kpp;

    private String address;

    private String account;

    private String bik;

    private String bank;

    private String korrAccount;

    private String comment;

    private boolean active = true;

    private String phone;

    private String email;

    private String ogrn;

    private String okpo;

    private String okved;

    private ContragentDto shipper;

    private PersonType personType;

    private List<ContragentType> contragentTypes;

    private EntrepreneurDto entrepreneur;

    private EmployeeDto director;

    private EmployeeDto accountant;

    private List<EmployeeDto> employees;


    public void setGroup(ContragentGroupDto group) {
        this.group = group;
    }

    public ContragentGroupDto getGroup() {
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

    public ContragentDto getShipper() {
        return shipper;
    }

    public void setShipper(ContragentDto shipper) {
        this.shipper = shipper;
    }

    public PersonType getPersonType() {
        return personType;
    }

    public void setPersonType(PersonType personType) {
        this.personType = personType;
    }

    public EntrepreneurDto getEntrepreneur() {
        return entrepreneur;
    }

    public void setEntrepreneur(EntrepreneurDto entrepreneur) {
        this.entrepreneur = entrepreneur;
    }

    public EmployeeDto getDirector() {
        return director;
    }

    public void setDirector(EmployeeDto director) {
        this.director = director;
    }

    public EmployeeDto getAccountant() {
        return accountant;
    }

    public void setAccountant(EmployeeDto accountant) {
        this.accountant = accountant;
    }

    public List<EmployeeDto> getEmployees() {
        if (employees == null) {
            employees = new ArrayList<>();
        }
        return employees;
    }

    public List<ContragentType> getContragentTypes() {
        return contragentTypes;
    }

    public void setContragentTypes(List<ContragentType> contragentTypes) {
        this.contragentTypes = contragentTypes;
    }

    public void setEmployees(List<EmployeeDto> employees) {
        this.employees = employees;
    }

    public ContragentDto clone() {
        try {
            return (ContragentDto) super.clone();
        } catch (CloneNotSupportedException e) {
            return new ContragentDto();
        }
    }
}
