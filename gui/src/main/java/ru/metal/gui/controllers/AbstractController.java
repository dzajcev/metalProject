package ru.metal.gui.controllers;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import ru.metal.dto.FxEntity;
import ru.metal.gui.windows.ActionInterface;

import java.util.*;

/**
 * Created by User on 08.08.2017.
 */
public abstract class AbstractController {
    private Stage primaryStage;
    private BooleanProperty closeRequest = new SimpleBooleanProperty();
    private BooleanProperty toClose = new SimpleBooleanProperty();
    private List<ReadOnlyProperty> properties = new ArrayList<>();
    private BooleanProperty changed = new SimpleBooleanProperty();
    Map<Integer, Object> objects = new HashMap<>();
    Map<Integer, Boolean> changedValues = new HashMap<>();

    private ActionInterface save;

    protected EventHandler<KeyEvent> clearHandler = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {
            ((TextInputControl) event.getSource()).setStyle(null);
        }
    };

    public AbstractController() {
        closeRequest.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    if (isChanged()) {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Сохранить");
                        alert.setHeaderText("Есть не сохраненные данные. \nЕсли продолжить, изменения будут поретяны");
                        alert.setContentText("Выберите действие");
                        alert.initOwner(primaryStage);
                        ButtonType buttonTypeOne = new ButtonType("Сохранить");
                        ButtonType buttonTypeTwo = new ButtonType("Не сохранять");
                        ButtonType buttonTypeCancel = new ButtonType("Отмена", ButtonBar.ButtonData.CANCEL_CLOSE);

                        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeCancel);

                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.get() == buttonTypeOne) {
                            save();
                            setToClose(true);
                        } else if (result.get() == buttonTypeTwo) {
                            setToClose(true);
                        }
                    } else {
                        setToClose(true);
                    }
                }
            }
        });
    }

    ChangeListener changeListener = new ChangeListener() {
        @Override
        public void changed(ObservableValue observable, Object oldValue, Object newValue) {
            ReadOnlyProperty objectProperty = (ReadOnlyProperty) observable;
            Object bean = objectProperty.getBean();
            int hashCode = bean.hashCode();
            Object o = objects.get(bean.hashCode());
            if ((newValue == null && o != null) || !newValue.equals(o)) {
                changed.setValue(true);
                changedValues.put(hashCode, true);
            } else {
                changedValues.put(hashCode, false);
                boolean isChanged = false;
                for (Boolean value : changedValues.values()) {
                    if (value) {
                        isChanged = value;
                    }
                }
                if (!isChanged) {
                    changed.setValue(false);
                }
            }
        }
    };

    public boolean isChanged() {
        return changed.get();
    }

    public BooleanProperty changedProperty() {
        return changed;
    }

    public void setChanged(boolean changed) {
        this.changed.set(changed);
    }

    public void registerControlsProperties(ActionInterface saveButton, ReadOnlyProperty... properties) {
        this.save = saveButton;
        if (save != null) {
            save.addCustomEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    refreshControlsProperties();
                }
            });
        }
        for (ReadOnlyProperty property : properties) {
            this.properties.add(property);
            Object bean = property.getBean();
            objects.put(bean.hashCode(), property.getValue());
            changedValues.put(bean.hashCode(), false);
            property.addListener(changeListener);
        }
    }

    private void refreshControlsProperties() {
        for (ReadOnlyProperty property : properties) {
            Object bean = property.getBean();
            objects.put(bean.hashCode(), property.getValue());
            changedValues.put(bean.hashCode(), false);
            changed.setValue(false);
        }
    }

    public boolean getCloseRequest() {
        return closeRequest.get();
    }

    public BooleanProperty closeRequestProperty() {
        return closeRequest;
    }

    public void setCloseRequest(boolean closeRequest) {
        this.closeRequest.set(closeRequest);
    }

    public boolean isToClose() {
        return toClose.get();
    }

    public BooleanProperty toCloseProperty() {
        return toClose;
    }

    public void setToClose(boolean toClose) {
        this.toClose.set(toClose);
    }

    protected abstract boolean save();

    protected boolean saveResult(boolean withMessage){
        if(!save()){
            if (withMessage) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setHeaderText("Произошла ошибка при сохранении");
                alert.setContentText("Проверьте правильность заполнения полей");
                alert.initOwner(getPrimaryStage());
                alert.show();
            }
            return false;
        }else{
            if (withMessage) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Успешно");
                alert.setHeaderText(null);
                alert.setContentText("Сохранение выполнено.");
                alert.initOwner(getPrimaryStage());
                alert.show();
            }
            return true;
        }
    }
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    protected void setError(Control control, String fieldName, FxEntity entity, String transportGuid) {
        if (entity.hasError(fieldName, transportGuid)) {
            control.setStyle("-fx-background-color: lightcoral");
        }
    }

    protected void setError(Control control, String fieldName, FxEntity entity) {
        if (entity.hasError(fieldName)) {
            control.setStyle("-fx-background-color: lightcoral");
        }
    }
}
