package ru.metal.gui.controls.tableviewcontrol;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import ru.metal.security.ejb.dto.AbstractDto;
import ru.metal.dto.FxEntity;
import ru.metal.dto.annotations.ValidatableField;
import ru.metal.gui.controls.tableviewcontrol.customcells.AbstractCell;
import ru.metal.gui.controls.tableviewcontrol.customcells.ComboBoxCell;
import ru.metal.gui.controls.tableviewcontrol.customcells.DatePickerCell;
import ru.metal.gui.controls.tableviewcontrol.customcells.TextFieldEditingCell;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by User on 02.09.2017.
 */
public class ValidatedTableView<S extends FxEntity> extends TableView<S> {

    private boolean toValidate = false;
    private Map<String, Map<ValidateField, AbstractCell<S, ?>>> cells = new HashMap<>();

    private ListChangeListener listListener = new ListChangeListener<S>() {
        @Override
        public void onChanged(Change<? extends S> c) {
            while (c.next()) {
                if (c.wasRemoved()) {
                    for (S s : c.getRemoved()) {
                        Map<ValidateField, AbstractCell<S, ?>> abstractCells = cells.get(s.getTransportGuid());
                        if (abstractCells != null) {
                            for (AbstractCell<S, ?> cell : abstractCells.values()) {
                                cell.setStyle(null);
                            }
                            cells.remove(s.getTransportGuid());
                        }
                    }
                }
            }
        }
    };

    public ValidatedTableView() {
        super();
        this.getItems().addListener(listListener);
        this.itemsProperty().addListener(new ChangeListener<ObservableList<S>>() {
            @Override
            public void changed(ObservableValue<? extends ObservableList<S>> observable, ObservableList<S> oldValue, ObservableList<S> newValue) {
                if (newValue != null) {
                    newValue.addListener(listListener);
                }
            }
        });
    }

    private Callback<TableColumn<S, Date>, TableCell<S, Date>> datePickerCell
            = param -> {
        AbstractCell<S, Date> dateCell = new DatePickerCell<>();

        dateCell.itemProperty().addListener(new ChangeListener<Date>() {
            @Override
            public void changed(ObservableValue<? extends Date> observable, Date oldValue, Date newValue) {
                dateCell.setStyle(null);
                SimpleObjectProperty<AbstractCell> cell = (SimpleObjectProperty) observable;
                AbstractCell<S, Date> bean = (AbstractCell) cell.getBean();
                TableColumn<S, Date> tableColumn = bean.getTableColumn();
                PropertyValueFactory<S, Date> cellValueFactory = (PropertyValueFactory<S, Date>) tableColumn.getCellValueFactory();
                TableRow<S> tableRow = bean.getTableRow();
                S item = tableRow.getItem();

                if (item != null) {
                    Class<? extends AbstractDto> aClass = item.getClass();
                    String property = cellValueFactory.getProperty();
                    Field field = getAnnotatedField(aClass, ru.metal.dto.annotations.ValidatableField.class, property);
                    if (field != null) {
                        if (toValidate) {
                            validField(field, bean.getItem(), bean);
                        }
                        int colIdx = tableColumn.getTableView().getColumns().indexOf(bean.getTableColumn());
                        if (!cells.containsKey(item.getTransportGuid())) {
                            cells.put(item.getTransportGuid(), new HashMap<>());
                        }
                        cells.get(item.getTransportGuid()).put(new ValidateField(colIdx, field), bean);
                        if (newValue != null && dateCell.isNull() && newValue.equals(DatePickerCell.EMPTY_DATE)) {
                            bean.setItem(null);
                        }
                    }
                }
            }
        });
        return dateCell;
    };

    protected Field getAnnotatedField(final Class clazz, final Class<? extends Annotation> annotationClass, String fieldName) {
        List<Field> allFields = getAllDeclaredFields(clazz);

        for (Field field : allFields) {
            if (field.isAnnotationPresent(annotationClass) && field.getName().equals(fieldName)) {
                return field;
            }
        }
        return null;
    }

    protected List<Field> getAllDeclaredFields(Class<?> type) {
        return getAllDeclaredFields(new LinkedList<Field>(), type);
    }

    protected List<Field> getAllDeclaredFields(List<Field> fields, Class<?> type) {
        for (final Field f : type.getDeclaredFields()) {

            boolean exists = false;
            for (final Field ff : fields) {
                if (f.getName().equals(ff.getName())) {
                    exists = true;
                    break;
                }
            }
            if (!exists) fields.add(f);
        }

        if (type.getSuperclass() != null) {
            fields = getAllDeclaredFields(fields, type.getSuperclass());
        }

        return fields;
    }

    public <E> Callback<TableColumn<S, E>, TableCell<S, E>> getTextCellFactory(Class<E> clazz) {
        return new Callback<TableColumn<S, E>, TableCell<S, E>>() {
            @Override
            public TableCell<S, E> call(TableColumn<S, E> param) {

                AbstractCell<S, E> textFieldEditingCell = new TextFieldEditingCell(clazz);
                textFieldEditingCell.itemProperty().addListener((ObservableValue<? extends E> observable, E oldValue, E newValue) -> {
                    textFieldEditingCell.setStyle(null);
                    SimpleObjectProperty<AbstractCell> cell = (SimpleObjectProperty) observable;
                    AbstractCell<S, E> bean = (AbstractCell) cell.getBean();
                    TableColumn<S, E> tableColumn = bean.getTableColumn();

                    TableRow<S> tableRow = bean.getTableRow();
                    S item = tableRow.getItem();

                    if (item != null) {
                        Class<? extends AbstractDto> aClass = item.getClass();
                        if (!(tableColumn.getCellValueFactory() instanceof PropertyValueFactory)) {
                            return;
                        }
                        PropertyValueFactory<S, E> cellValueFactory = (PropertyValueFactory<S, E>) tableColumn.getCellValueFactory();
                        String property = cellValueFactory.getProperty();
                        Field field = getAnnotatedField(aClass, ru.metal.dto.annotations.ValidatableField.class, property);
                        if (field != null) {
                            if (toValidate) {
                                validField(field, bean.getItem(), bean);
                            }
                            int colIdx = tableColumn.getTableView().getColumns().indexOf(bean.getTableColumn());
                            if (!cells.containsKey(item.getTransportGuid())) {
                                cells.put(item.getTransportGuid(), new HashMap<>());
                            }
                            cells.get(item.getTransportGuid()).put(new ValidateField(colIdx, field), bean);
                            if (newValue == null || textFieldEditingCell.isNull() && newValue.equals(textFieldEditingCell.getEmptyValue())) {
                                bean.setItem(null);
                            }
                        }
                    }
                });
                return textFieldEditingCell;
            }
        };
    }

    public Callback<TableColumn<S, Date>, TableCell<S, Date>> getDateCellFactory() {
        return datePickerCell;
    }

    public <E> Callback<TableColumn<S, E>, TableCell<S, E>> getComboBoxCell(Class<E> clazz, E... data) {
        return getComboBoxCell(FXCollections.observableArrayList(data), clazz);
    }

    public <E> Callback<TableColumn<S, E>, TableCell<S, E>> getComboBoxCell(Collection<E> data, Class<E> clazz) {
        return new Callback<TableColumn<S, E>, TableCell<S, E>>() {
            @Override
            public TableCell<S, E> call(TableColumn<S, E> param) {
                ComboBoxCell abstractCell = new ComboBoxCell(FXCollections.observableArrayList(data), clazz);
                abstractCell.itemProperty().addListener((observable, oldValue, newValue) -> {
                    SimpleObjectProperty<ComboBoxCell> cell = (SimpleObjectProperty) observable;
                    ComboBoxCell bean = (ComboBoxCell) cell.getBean();
                    TableColumn tableColumn = bean.getTableColumn();
                    PropertyValueFactory cellValueFactory = (PropertyValueFactory) tableColumn.getCellValueFactory();
                    TableRow<S> tableRow = bean.getTableRow();
                    S item = tableRow.getItem();

                    if (item != null) {
                        Class<? extends AbstractDto> aClass = item.getClass();
                        String property = cellValueFactory.getProperty();
                        Field field = getAnnotatedField(aClass, ru.metal.dto.annotations.ValidatableField.class, property);
                        if (field != null) {
                            if (toValidate) {
                                validField(field, bean.getItem(), bean);
                            }
                            int colIdx = tableColumn.getTableView().getColumns().indexOf(bean.getTableColumn());
                            if (!cells.containsKey(item.getTransportGuid())) {
                                cells.put(item.getTransportGuid(), new HashMap<>());
                            }
                            cells.get(item.getTransportGuid()).put(new ValidateField(colIdx, field), bean);
                            if (abstractCell.isNull() && newValue != null && newValue.equals(abstractCell.getEmpty())) {
                                bean.setItem(null);
                            }
                        }
                    }
                });
                return abstractCell;
            }
        };
    }

    private boolean validField(Field field, Object value, AbstractCell<S, ?> cell) {
        boolean result = false;
        ValidatableField annotation = field.getAnnotation(ValidatableField.class);
        if (annotation != null) {
            if (!annotation.nullable() && value == null) {
                cell.setError();
                result = true;
            }
            String regexp = annotation.regexp();
            if (!result && !regexp.isEmpty()) {
                boolean regexpNotValid;
                if (!(value instanceof Date)) {
                    regexpNotValid = !value.toString().matches(regexp);
                } else {
                    regexpNotValid = false;
                }
                if (regexpNotValid) {
                    cell.setError();
                    result = true;
                }
            }
        }
        return result;
    }

    public boolean checkError() {
        boolean result = false;
        for (Map.Entry<String, Map<ValidateField, AbstractCell<S, ?>>> entry : cells.entrySet()) {
            for (Map.Entry<ValidateField, AbstractCell<S, ?>> subEntry : entry.getValue().entrySet()) {
                result = result | validField(subEntry.getKey().getField(), subEntry.getValue().getItem(), subEntry.getValue());
            }
        }
        return result;
    }

    private static class ValidateField {
        private int colIdx;

        private Field field;

        public ValidateField(int colIdx, Field field) {
            this.colIdx = colIdx;
            this.field = field;
        }

        public int getColIdx() {
            return colIdx;
        }

        public void setColIdx(int colIdx) {
            this.colIdx = colIdx;
        }

        public Field getField() {
            return field;
        }

        public void setField(Field field) {
            this.field = field;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ValidatedTableView.ValidateField)) return false;

            ValidateField that = (ValidateField) o;

            return getColIdx() == that.getColIdx();
        }

        @Override
        public int hashCode() {
            return getColIdx();
        }
    }

    public boolean isToValidate() {
        return toValidate;
    }

    public void setToValidate(boolean toValidate) {
        this.toValidate = toValidate;
    }
}

