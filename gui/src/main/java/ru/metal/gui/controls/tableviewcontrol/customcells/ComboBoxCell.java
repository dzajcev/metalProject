package ru.metal.gui.controls.tableviewcontrol.customcells;

import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.util.StringConverter;
import ru.common.api.dto.AbstractDto;
import ru.common.api.dto.ComboBoxElement;

import java.util.List;

/**
 * Created by User on 02.09.2017.
 */
public class ComboBoxCell<T extends AbstractDto, E extends ComboBoxElement> extends AbstractCell<T, E> {


    private List<E> comboBoxData;
    private ComboBox<E> comboBox;

    public E getEmpty() {
        return comboBoxData.get(0);
    }

    public ComboBoxCell(List<E> data, Class<E> clazz) {
        super(clazz);
        this.comboBoxData = data;
    }

    @Override
    public void startEdit() {
        if (!isEmpty()) {
            super.startEdit();
            createComboBox();
            setText(null);
            setGraphic(comboBox);
        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();

        setText(getStringValue());
        setGraphic(null);
    }

    @Override
    public void updateItem(E item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                if (comboBox != null) {
                    comboBox.setValue(getValue());
                }
                setText(getStringValue());
                setGraphic(comboBox);
            } else {
                setText(getStringValue());
                setGraphic(null);
                if (item == null) {
                    setIsNull(true);
                    setItem(getEmpty());
                } else {
                    setIsNull(false);
                }

            }
        }
    }

    private void createComboBox() {
        comboBox = new ComboBox<>(FXCollections.observableArrayList(comboBoxData));
        comboBoxConverter(comboBox);
        comboBox.valueProperty().set(getValue());
        comboBox.setConverter(new StringConverter<E>() {
            @Override
            public String toString(E object) {
                return object.getName();
            }

            @Override
            public E fromString(String string) {
                return null;
            }
        });
        comboBox.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
        comboBox.setOnAction((e) -> {
            ComboBoxCell.this.setStyle(null);
            commitEdit(comboBox.getSelectionModel().getSelectedItem());
        });
//            comboBox.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
//                if (!newValue) {
//                    commitEdit(comboBox.getSelectionModel().getSelectedItem());
//                }
//            });
    }


    public void comboBoxConverter(ComboBox<E> comboBox) {
        comboBox.setCellFactory((c) -> {
            return new ListCell<E>() {
                @Override
                protected void updateItem(E item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(item.getName());
                    }
                }
            };
        });
    }


    public E getValue() {
        return getItem();
    }

    public String getStringValue() {
        if (getItem() == null) {
            return "";
        } else {
            return getItem().getName();
        }
    }
}

