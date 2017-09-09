package ru.metal.dto;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.metal.api.contragents.dto.EmployeeDto;
import ru.metal.dto.annotations.ValidatableField;
import ru.metal.dto.helper.DocumentHelper;
import ru.metal.dto.helper.EmployeeHelper;
import ru.metal.dto.helper.FxHelper;

/**
 * Created by User on 31.08.2017.
 */
public class EmployeeFx extends FxEntity<EmployeeDto>{

    @ValidatableField(nullable = false)
    private SimpleStringProperty firstName = new SimpleStringProperty();

    @ValidatableField(nullable = false)
    private SimpleStringProperty middleName = new SimpleStringProperty();

    @ValidatableField(nullable = false)
    private SimpleStringProperty secondName = new SimpleStringProperty();

    @ValidatableField(nullable = false)
    private SimpleStringProperty position = new SimpleStringProperty();

    private ObservableList<DocumentFx> documents = FXCollections.observableArrayList();

    private BooleanProperty active=new SimpleBooleanProperty(true);

    public String getFirstName() {
        return firstName.get();
    }

    public SimpleStringProperty firstNameProperty() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getMiddleName() {
        return middleName.get();
    }

    public SimpleStringProperty middleNameProperty() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName.set(middleName);
    }

    public String getSecondName() {
        return secondName.get();
    }

    public SimpleStringProperty secondNameProperty() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName.set(secondName);
    }

    public String getPosition() {
        return position.get();
    }

    public SimpleStringProperty positionProperty() {
        return position;
    }

    public void setPosition(String position) {
        this.position.set(position);
    }

    public ObservableList<DocumentFx> getDocuments() {
        return documents;
    }

    public void setDocuments(ObservableList<DocumentFx> documents) {
        this.documents = documents;
    }

    public boolean isActive() {
        return active.get();
    }

    public BooleanProperty activeProperty() {
        return active;
    }

    public void setActive(boolean active) {
        this.active.set(active);
    }

    @Override
    public EmployeeDto getEntity() {
        EmployeeDto employeeDto=new EmployeeDto();
        employeeDto.setActive(isActive());
        employeeDto.setGuid(getGuid());
        employeeDto.setLastEditingDate(getLastEditingDate());
        employeeDto.setTransportGuid(getTransportGuid());
        employeeDto.setDocuments(DocumentHelper.getInstance().getDtoCollection(getDocuments()));
        employeeDto.setPosition(getPosition());
        employeeDto.setFirstName(getFirstName());
        employeeDto.setMiddleName(getMiddleName());
        employeeDto.setSecondName(getSecondName());
        return employeeDto;
    }

    @Override
    public FxHelper<EmployeeFx, EmployeeDto> getHelper() {
        return EmployeeHelper.getInstance();
    }
}
