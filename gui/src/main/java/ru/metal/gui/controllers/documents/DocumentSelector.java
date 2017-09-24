package ru.metal.gui.controllers.documents;

/**
 * Created by User on 20.09.2017.
 */

import javafx.collections.ObservableList;
import ru.common.api.dto.ComboBoxElement;
import ru.metal.api.documents.DocumentStatus;
import ru.metal.dto.ContragentFx;
import ru.metal.dto.DocumentHeader;

import java.util.List;

public interface DocumentSelector<T extends DocumentHeader, E extends DocumentStatus> {

    void dropDocument(T document);

    T createEmptyDocument();

    ObservableList<T> getDocuments(ObtainDocumentsRequest obtainDocumentsRequest);

    String getId();

    E[] getDocumentStatusValues();
}
