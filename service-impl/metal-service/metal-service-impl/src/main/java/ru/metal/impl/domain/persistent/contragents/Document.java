package ru.metal.impl.domain.persistent.contragents;

import ru.metal.api.contragents.dto.DocumentType;
import ru.metal.impl.domain.persistent.BaseEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by User on 31.08.2017.
 */
@Entity
@Table(name = "DOCUMENTS")
public class Document extends BaseEntity {

    @Column(name = "DOCUMENT_TYPE")
    @Enumerated(EnumType.STRING)
    private DocumentType documentType;

    @Column(name = "SERIE")
    private String serie;

    @Column(name = "NUMBER")
    private String number;

    @Column(name = "VIDAN")
    private String vidan;

    @Column(name = "DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Column(name = "active")
    private Boolean active;

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getVidan() {
        return vidan;
    }

    public void setVidan(String vidan) {
        this.vidan = vidan;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
