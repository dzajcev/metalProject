package ru.metal.api.contragents.dto;

import ru.metal.api.common.dto.AbstractDto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 31.08.2017.
 */
public class EmployeeDto extends AbstractDto {
    private String firstName;

    private String middleName;

    private String secondName;

    private String position;

    private List<DocumentDto> documents;

    private boolean active = true;

    public List<DocumentDto> getDocuments() {
        if (documents==null){
            documents=new ArrayList<>();
        }
        return documents;
    }

    public void setDocuments(List<DocumentDto> documents) {
        this.documents = documents;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName=middleName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}
