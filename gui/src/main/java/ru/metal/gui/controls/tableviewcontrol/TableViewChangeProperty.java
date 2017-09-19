package ru.metal.gui.controls.tableviewcontrol;

import javafx.beans.InvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import ru.metal.security.ejb.dto.AbstractDto;

import java.util.*;

/**
 * Created by User on 31.08.2017.
 */
public class TableViewChangeProperty<T extends AbstractDto> extends BooleanPropertyBase {
    List<ChangeListener> changeListeners = new ArrayList<>();
    List<InvalidationListener> invalidationListeners = new ArrayList<>();

    private BooleanProperty action = new SimpleBooleanProperty(false);
    private boolean hasChangeListener = false;
    private TableView<T> tableView;
    private int rowCount;
    private Map<String, Map<Integer, Object>> values = new HashMap<>();

    public TableViewChangeProperty(TableView<T> tTableView, ObservableList<T> items) {
        this.tableView = tTableView;
        set(false);
        rowCount = tableView.getItems().size();
        items.addListener(new ListChangeListener<T>() {
            @Override
            public void onChanged(Change<? extends T> c) {

                while (c.next()) {
                    if (c.wasAdded()) {
                        for (T t : c.getAddedSubList()) {
                            if (t.getGuid() == null) {
                                if (hasChangeListener) {
                                    set(true);
                                }
                            }
                            values.put(t.getGuid(), new HashMap<>());
                            final int[] colArr = new int[]{0};
                            for (TableColumn<T, ?> column : tableView.getColumns()) {
                                ObservableValue<?> cellObservableValue = column.getCellObservableValue(t);
                                values.get(t.getGuid()).put(colArr[0], cellObservableValue.getValue());
                                cellObservableValue.addListener(new ChangeListener<Object>() {
                                    private String guid = t.getGuid();
                                    private Integer columnIx = colArr[0];

                                    @Override
                                    public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
                                        setAction(true);
                                        Map<Integer, Object> cols = values.get(guid);
                                        Object o = cols.get(columnIx);
                                        boolean equals;
                                        if (o == null && newValue != null) {
                                            equals = false;
                                        } else {
                                            equals = o.equals(newValue);
                                        }
                                        if (!equals) {
                                            if (hasChangeListener) {
                                                set(true);
                                            }
                                        } else {
                                            if (hasChangeListener) {
                                                set(false);
                                            }
                                        }
                                        //  reset();
                                    }
                                });
                                colArr[0] = colArr[0] + 1;
                            }
                        }
                    }
                    if (c.getList().size() != rowCount) {
                        if (hasChangeListener) {
                            set(true);
                        }
                    } else {
                        if (hasChangeListener) {
                            set(false);
                        }
                    }
                    //    reset();
                }
                fireValueChangedEvent();
            }
        });
//        tableView.refresh();

    }

    private void reset() {
        for (ChangeListener listener : changeListeners) {
            TableViewChangeProperty.super.removeListener(listener);
        }
        set(false);
        for (ChangeListener listener : changeListeners) {
            TableViewChangeProperty.super.addListener(listener);
        }
    }

    @Override
    public Object getBean() {
        return tableView;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void addListener(ChangeListener<? super Boolean> listener) {
        super.addListener(listener);
        changeListeners.add(listener);
        hasChangeListener = true;
    }

    @Override
    public void removeListener(ChangeListener<? super Boolean> listener) {
        super.removeListener(listener);
        Iterator<ChangeListener> iterator = changeListeners.iterator();
        while (iterator.hasNext()) {
            ChangeListener next = iterator.next();
            if (next == listener) {
                iterator.remove();
                break;
            }
        }
        if (changeListeners.isEmpty()) {
            hasChangeListener = false;
        }
    }

    public boolean isAction() {
        return action.get();
    }

    public BooleanProperty actionProperty() {
        return action;
    }

    private void setAction(boolean action) {
        this.action.set(action);
        this.action.set(!this.action.get());
    }
}
