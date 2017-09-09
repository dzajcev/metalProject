package ru.metal.impl.domain.persistent.contragents;

import ru.metal.impl.domain.persistent.BaseEntity;

import javax.persistence.*;
import java.util.List;

/**
 * Created by User on 31.08.2017.
 */
@Entity
@Table(name = "EMPLOYEES")
public class Employee extends BaseEntity {

    @Column(name = "FIRST_NAME",nullable = false)
    private String firstName;

    @Column(name = "MIDDLE_NAME")
    private String middleName;

    @Column(name = "SECOND_NAME",nullable = false)
    private String secondName;

    @Column(name = "POSITION",nullable = false)
    private String position;

    @Column(name = "active")
    private Boolean active;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Document> documents;

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
        this.middleName = middleName;
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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

}
