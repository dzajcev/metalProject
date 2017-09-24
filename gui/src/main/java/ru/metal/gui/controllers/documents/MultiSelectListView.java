package ru.metal.gui.controllers.documents;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import ru.metal.api.documents.DocumentStatus;
import ru.metal.security.ejb.dto.Role;

import java.util.List;

/**
 * Created by User on 23.09.2017.
 */
public class MultiSelectListView<T extends DocumentStatus> extends ListView<T> {


    public MultiSelectListView(T[] values, List<T> selectedValues) {
        setItems(FXCollections.observableArrayList(values));
        getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        if (selectedValues.size()>0) {
            int[] idx = new int[selectedValues.size() - 1];
            for (int i = 1; i < selectedValues.size(); i++) {
                idx[i - 1] = getItems().indexOf(selectedValues.get(i));
            }
            getSelectionModel().selectIndices(getItems().indexOf(selectedValues.get(0)), idx);
        }
        requestFocus();
        Callback<ListView<T>, ListCell<T>> roleCallback = new Callback<ListView<T>, ListCell<T>>() {

            @Override
            public ListCell<T> call(ListView<T> p) {

                ListCell<T> cell = new ListCell<T>() {

                    @Override
                    protected void updateItem(T t, boolean bln) {
                        super.updateItem(t, bln);
                        if (t != null) {
                            setText(t.getTitle());
                        } else {
                            setText(null);
                        }
                    }

                };
                return cell;
            }
        };
        setCellFactory(roleCallback);

    }
}
