package ru.metal.api.contragents.dto;

import ru.metal.security.ejb.dto.AbstractDto;

import java.util.Date;

/**
 * Created by User on 31.08.2017.
 */
public class DocumentDto extends AbstractDto {

    private DocumentType documentType;

    private String serie;

    private String number;

    private String vidan;

    private Date date;

    private boolean active;

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

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
