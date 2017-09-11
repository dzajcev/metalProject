package ru.metal.gui.controls.tableviewcontrol.customcells;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;
import ru.common.api.dto.AbstractDto;

import java.text.*;
import java.util.Date;
import java.util.Locale;

/**
 * Created by User on 02.09.2017.
 */
public class TextFieldEditingCell<T extends AbstractDto, E> extends AbstractCell<T, E> {

    private <T> Number getNumber(String value, String pattern) throws ParseException {
        DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.getDefault());
        DecimalFormat formatter = new DecimalFormat(pattern, dfs);
        return formatter.parse(value);

    }

    private E fromString(String value) {
        String pattern = (String) getTableColumn().getProperties().get(CellFormatKeys.NUMERIC_CELL_PATTERN);
        E result;
        try {
            if (clazz == String.class) {
                result = (E) value;
            } else if (clazz == Double.class) {
                if (pattern != null) {
                    value = getNumber(value, pattern).toString();
                }
                result = (E) (Double) Double.parseDouble(value);
            } else if (clazz == Integer.class) {
                if (pattern != null) {
                    value = getNumber(value, pattern).toString();
                }
                result = (E) (Integer) Integer.parseInt(value);
            } else if (clazz == Long.class) {
                if (pattern != null) {
                    value = getNumber(value, pattern).toString();
                }
                result = (E) (Long) Long.parseLong(value);
            } else if (clazz == Short.class) {
                if (pattern != null) {
                    value = getNumber(value, pattern).toString();
                }
                result = (E) (Short) Short.parseShort(value);
            } else if (clazz == Date.class) {
                DateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                result = (E) format.format(value);
            } else {
                result = null;
            }
        } catch (NumberFormatException | ParseException e) {
            result = getItem();
        }
        return result;
    }

    private String toString(E value) {
        String result;
        if (clazz == String.class) {
            result = value.toString();
        } else if (clazz == Double.class) {
            result = Double.toString((Double) value);
        } else if (clazz == Integer.class) {
            result = Integer.toString((Integer) value);
        } else if (clazz == Long.class) {
            result = Long.toString((Long) value);
        } else if (clazz == Short.class) {
            result = Short.toString((Short) value);
        } else if (clazz == Date.class) {
            DateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
            result = format.format((Date) value);
        } else {
            result = null;
        }
        if (value instanceof Number) {
            String pattern = (String) getTableColumn().getProperties().get(CellFormatKeys.NUMERIC_CELL_PATTERN);
            if (pattern != null) {
                DecimalFormat formatter = new DecimalFormat(pattern);
                result = formatter.format(value);
            }
        }
        return result;
    }

    private TextField textField;

    public TextFieldEditingCell(Class<E> clazz) {
        super(clazz);
    }

    @Override
    public void commitEdit(E newValue) {
        super.commitEdit(newValue);

    }

    @Override
    public void startEdit() {
        if (!isEmpty()) {
            super.startEdit();
            createTextField();
            setText(null);
            setGraphic(textField);
            textField.selectAll();
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
            setText(item != null ? item.toString() : null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                if (textField != null) {
                    textField.setText(getStringValue());
                }
                setText(null);
                setGraphic(textField);
            } else {
                if (getStringValue() != null && getStringValue().length() == 0) {
                    setItem(null);
                }
                setText(getStringValue());
                setGraphic(null);
                if (getItem() == null) {
                    setIsNull(true);
                    setItem(getEmptyValue());
                } else {
                    setIsNull(false);
                }
            }
        }

    }

    private void createTextField() {
        textField = new TextField(getStringValue());
        textField.requestFocus();
        textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
        textField.setOnAction((e) -> commitEdit(fromString(textField.getText())));
        textField.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            TextFieldEditingCell.this.setStyle(null);
            if (!newValue) {
                commitEdit(fromString(textField.getText()));
            }
        });
    }

    private String getStringValue() {
        return getItem() != null ? toString(getItem()) : null;
    }
}
