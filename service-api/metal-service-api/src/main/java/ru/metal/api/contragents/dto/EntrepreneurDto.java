package ru.metal.api.contragents.dto;

import ru.metal.security.ejb.dto.AbstractDto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 31.08.2017.
 */
public class EntrepreneurDto extends AbstractDto {
    private List<DocumentDto> documents;

    public List<DocumentDto> getDocuments() {
        if (documents == null) {
            documents = new ArrayList<>();
        }

        return documents;
    }

    public void setDocuments(List<DocumentDto> documents) {
        this.documents = documents;
    }
}
