package ru.metal.dto;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.common.api.dto.TableElement;
import ru.metal.api.contragents.dto.ContragentDto;
import ru.metal.api.contragents.dto.ContragentType;
import ru.metal.api.contragents.dto.PersonType;
import ru.metal.dto.annotations.PredicateField;
import ru.metal.dto.annotations.ValidatableCollection;
import ru.metal.dto.annotations.ValidatableField;
import ru.metal.dto.helper.ContragentHelper;
import ru.metal.dto.helper.EmployeeHelper;
import ru.metal.dto.helper.FxHelper;

import java.util.ArrayList;
import java.util.function.Predicate;

/**
 * Created by User on 29.08.2017.
 */
public class ContragentFx extends FxEntity<ContragentDto> implements TableElement<ContragentGroupFx>, Cloneable {

    @PredicateField
    private Predicate<ContragentFx> kppPredicate = new Predicate<ContragentFx>() {
        @Override
        public boolean test(ContragentFx contragentFx) {
            return contragentFx.getPersonType() == PersonType.UL;
        }
    };

    @PredicateField
    private Predicate<ContragentFx> flPredicate = new Predicate<ContragentFx>() {
        @Override
        public boolean test(ContragentFx contragentFx) {
            return contragentFx.getPersonType() != PersonType.UL;
        }
    };
    @ValidatableField(nullable = false, regexp = Formats.NAME_FORMAT)
    private StringProperty name = new SimpleStringProperty();

    @ValidatableField(nullable = false)
    private ObjectProperty<ContragentGroupFx> group = new SimpleObjectProperty<>();

    @ValidatableField(regexp = Formats.NAME_FORMAT)
    private StringProperty alias = new SimpleStringProperty();

    @ValidatableField(nullable = false, regexp = Formats.INN_FORMAT)
    private StringProperty inn = new SimpleStringProperty();

    @ValidatableField(nullable = false, predicateName = "kppPredicate", regexp = Formats.KPP_FORMAT)
    private StringProperty kpp = new SimpleStringProperty();

    @ValidatableField(nullable = false, regexp = Formats.ADDRESS_FORMAT)
    private StringProperty address = new SimpleStringProperty();

    @ValidatableField(nullable = false, regexp = Formats.ACCOUNT_FORMAT)
    private StringProperty account = new SimpleStringProperty();

    @ValidatableField(nullable = false, regexp = Formats.BIK_FORMAT)
    private StringProperty bik = new SimpleStringProperty();

    @ValidatableField(nullable = false, regexp = Formats.BANK_FORMAT)
    private StringProperty bank = new SimpleStringProperty();

    @ValidatableField(nullable = false, regexp = Formats.ACCOUNT_FORMAT)
    private StringProperty korrAccount = new SimpleStringProperty();

    @ValidatableField(regexp = Formats.COMMENT_FORMAT)
    private StringProperty comment = new SimpleStringProperty();

    private BooleanProperty active = new SimpleBooleanProperty(true);

    private StringProperty phone = new SimpleStringProperty();

    @ValidatableField(regexp = Formats.EMAIL_FORMAT)
    private StringProperty email = new SimpleStringProperty();

    @ValidatableField(regexp = Formats.OGRN_FORMAT)
    private StringProperty ogrn = new SimpleStringProperty();

    @ValidatableField(regexp = Formats.OKPO_FORMAT)
    private StringProperty okpo = new SimpleStringProperty();

    @ValidatableField(regexp = Formats.OKVED_FORMAT)
    private StringProperty okved = new SimpleStringProperty();

    private ObjectProperty<ContragentFx> shipper = new SimpleObjectProperty<>();

    @ValidatableField(nullable = false)
    private ObjectProperty<PersonType> personType = new SimpleObjectProperty<>();

    @ValidatableCollection(minSize = 1)
    private ObservableList<ContragentType> contragentTypes = FXCollections.observableArrayList();

    @ValidatableField(nullable = false, predicateName = "flPredicate")
    private ObjectProperty<EntrepreneurFx> entrepreneur = new SimpleObjectProperty<>();

    private ObjectProperty<EmployeeFx> director = new SimpleObjectProperty<>();

    private ObjectProperty<EmployeeFx> accountant = new SimpleObjectProperty<>();

    private ObservableList<EmployeeFx> employees = FXCollections.observableArrayList();

    @Override
    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public ContragentGroupFx getGroup() {
        return group.get();
    }

    public ObjectProperty<ContragentGroupFx> groupProperty() {
        return group;
    }

    public void setGroup(ContragentGroupFx group) {
        this.group.set(group);
    }

    public String getAlias() {
        return alias.get();
    }

    public StringProperty aliasProperty() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias.set(alias);
    }

    public String getInn() {
        return inn.get();
    }

    public StringProperty innProperty() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn.set(inn);
    }

    public String getKpp() {
        return kpp.get();
    }

    public StringProperty kppProperty() {
        return kpp;
    }

    public void setKpp(String kpp) {
        this.kpp.set(kpp);
    }

    public String getAddress() {
        return address.get();
    }

    public StringProperty addressProperty() {
        return address;
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public String getAccount() {
        return account.get();
    }

    public StringProperty accountProperty() {
        return account;
    }

    public void setAccount(String account) {
        this.account.set(account);
    }

    public String getBik() {
        return bik.get();
    }

    public StringProperty bikProperty() {
        return bik;
    }

    public void setBik(String bik) {
        this.bik.set(bik);
    }

    public String getBank() {
        return bank.get();
    }

    public StringProperty bankProperty() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank.set(bank);
    }

    public String getKorrAccount() {
        return korrAccount.get();
    }

    public StringProperty korrAccountProperty() {
        return korrAccount;
    }

    public void setKorrAccount(String korrAccount) {
        this.korrAccount.set(korrAccount);
    }

    public String getComment() {
        return comment.get();
    }

    public StringProperty commentProperty() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment.set(comment);
    }

    @Override
    public boolean getActive() {
        return active.get();
    }

    public BooleanProperty activeProperty() {
        return active;
    }

    public void setActive(boolean active) {
        this.active.set(active);
    }

    public String getPhone() {
        return phone.get();
    }

    public StringProperty phoneProperty() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }

    public String getEmail() {
        return email.get();
    }

    public StringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getOgrn() {
        return ogrn.get();
    }

    public StringProperty ogrnProperty() {
        return ogrn;
    }

    public void setOgrn(String ogrn) {
        this.ogrn.set(ogrn);
    }

    public String getOkpo() {
        return okpo.get();
    }

    public StringProperty okpoProperty() {
        return okpo;
    }

    public void setOkpo(String okpo) {
        this.okpo.set(okpo);
    }

    public String getOkved() {
        return okved.get();
    }

    public StringProperty okvedProperty() {
        return okved;
    }

    public void setOkved(String okved) {
        this.okved.set(okved);
    }

    public ContragentFx getShipper() {
        return shipper.get();
    }

    public ObjectProperty<ContragentFx> shipperProperty() {
        return shipper;
    }

    public void setShipper(ContragentFx shipper) {
        this.shipper.set(shipper);
    }

    public PersonType getPersonType() {
        return personType.get();
    }

    public ObjectProperty<PersonType> personTypeProperty() {
        return personType;
    }

    public void setPersonType(PersonType personType) {
        this.personType.set(personType);
    }

    public EntrepreneurFx getEntrepreneur() {
        return entrepreneur.get();
    }

    public ObjectProperty<EntrepreneurFx> entrepreneurProperty() {
        return entrepreneur;
    }

    public void setEntrepreneur(EntrepreneurFx entrepreneur) {
        this.entrepreneur.set(entrepreneur);
    }

    public EmployeeFx getDirector() {
        return director.get();
    }

    public ObjectProperty<EmployeeFx> directorProperty() {
        return director;
    }

    public void setDirector(EmployeeFx director) {
        this.director.set(director);
    }

    public EmployeeFx getAccountant() {
        return accountant.get();
    }

    public ObjectProperty<EmployeeFx> accountantProperty() {
        return accountant;
    }

    public void setAccountant(EmployeeFx accountant) {
        this.accountant.set(accountant);
    }

    public ObservableList<EmployeeFx> getEmployees() {
        return employees;
    }

    public void setEmployees(ObservableList<EmployeeFx> employees) {
        this.employees = employees;
    }

    public ContragentFx clone() {
        try {
            return (ContragentFx) super.clone();
        } catch (CloneNotSupportedException e) {
            return new ContragentFx();
        }
    }

    public ObservableList<ContragentType> getContragentTypes() {
        return contragentTypes;
    }

    public void setContragentTypes(ObservableList<ContragentType> contragentTypes) {
        this.contragentTypes = contragentTypes;
    }

    @Override
    public ContragentDto getEntity() {
        ContragentDto contragent = new ContragentDto();
        contragent.setActive(getActive());
        contragent.setGuid(getGuid());
        contragent.setLastEditingDate(getLastEditingDate());
        contragent.setTransportGuid(getTransportGuid());
        contragent.setAccount(getAccount());
        if (getAccountant() != null) {
            contragent.setAccountant(getAccountant().getEntity());
        }
        if (getDirector() != null) {
            contragent.setDirector(getDirector().getEntity());
        }
        contragent.setEmployees(EmployeeHelper.getInstance().getDtoCollection(getEmployees()));

        contragent.setAddress(getAddress());
        contragent.setAlias(getAlias());
        contragent.setBank(getBank());
        contragent.setBik(getBik());
        contragent.setComment(getComment());
        contragent.setPersonType(getPersonType());
        contragent.setContragentTypes(new ArrayList<>(getContragentTypes()));
        contragent.setEmail(getEmail());
        if (getEntrepreneur() != null) {
            contragent.setEntrepreneur(getEntrepreneur().getEntity());
        }
        if (getGroup() != null) {
            contragent.setGroup(getGroup().getEntity());
        }
        contragent.setInn(getInn());
        contragent.setKorrAccount(getKorrAccount());
        contragent.setKpp(getKpp());
        contragent.setName(getName());
        contragent.setOgrn(getOgrn());
        contragent.setOkpo(getOkpo());
        contragent.setOkved(getOkved());
        contragent.setPhone(getPhone());
        if (getShipper() != null) {
            contragent.setShipper(getShipper().getEntity());
        }
        return contragent;
    }

    @Override
    public FxHelper<ContragentFx, ContragentDto> getHelper() {
        return ContragentHelper.getInstance();
    }
}
