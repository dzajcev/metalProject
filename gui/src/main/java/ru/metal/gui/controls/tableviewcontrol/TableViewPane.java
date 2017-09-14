package ru.metal.gui.controls.tableviewcontrol;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import ru.common.api.dto.TableElement;
import ru.metal.crypto.ejb.dto.AbstractDto;
import ru.metal.dto.FxEntity;
import ru.metal.gui.windows.LabelButton;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by User on 16.08.2017.
 */
public class TableViewPane<T extends TableElement> extends VBox {
    public static final DataFormat tableRowDataFormat = new DataFormat("tableRow");

    private boolean obtainMode = false;
    private ObservableList<T> source;
    private TableView<T> tableView;
    private BooleanProperty onlyActive = new SimpleBooleanProperty(true);
    private ObjectProperty<T> toSave = new SimpleObjectProperty();
    private ObjectProperty<T> open = new SimpleObjectProperty();
    private BooleanProperty editable = new SimpleBooleanProperty(false);

    private ObjectProperty<T> obtainItem = new SimpleObjectProperty<T>();

    public TableViewPane(Class<T> clazz) {
        setAlignment(Pos.TOP_CENTER);
        setPadding(new Insets(2));
        HBox buttonBlock = new HBox();
        buttonBlock.setPadding(new Insets(2));
        buttonBlock.setPrefWidth(200);

        buttonBlock.setAlignment(Pos.CENTER_RIGHT);
        getChildren().addAll(buttonBlock);

        CheckBox activeBox = new CheckBox("Активные");
        activeBox.setSelected(true);
        Tooltip tooltipActiveBox = new Tooltip("Только активные");
        activeBox.setTooltip(tooltipActiveBox);
        activeBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                onlyActive.setValue(newValue);
            }
        });

        LabelButton add = new LabelButton(null, new Image(getClass().getResourceAsStream("/icons/add.png")));
        add.setDisable(true);
        add.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    open.setValue(clazz.newInstance());
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }
        });
        LabelButton edit = new LabelButton(null, new Image(getClass().getResourceAsStream("/icons/edit.png")));
        edit.setDisable(true);
        edit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (tableView.getSelectionModel().getSelectedItem() != null) {
                    open.setValue(tableView.getSelectionModel().getSelectedItem());
                    open.set(null);
                }
            }
        });
        buttonBlock.getChildren().addAll(activeBox, add, edit);
        TextField findField = new TextField();
        findField.setOnKeyReleased(new EventHandler<KeyEvent>() {
            int i = 0;
            List<T> items = new ArrayList<T>();

            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().equals(KeyCode.ENTER)) {
                    if (i < items.size() - 1) {
                        i++;
                    }
                    if (!items.isEmpty()) {
                        tableView.getSelectionModel().select(items.get(i));
                    }
                } else {
                    items = findItems(findField.getText());
                    if (!items.isEmpty()) {
                        i = 0;
                        tableView.getSelectionModel().select(items.get(i));
                    }
                }
                int selectedIndex = tableView.getSelectionModel().getSelectedIndex();
                tableView.scrollTo(selectedIndex);
            }
        });
        this.getChildren().add(findField);

        tableView = new TableView<T>();
        tableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<T>() {
            @Override
            public void changed(ObservableValue<? extends T> observable, T oldValue, T newValue) {
                edit.setDisable(newValue == null);
            }
        });
        tableView.setRowFactory(tv -> {

            TableRow<T> row = new TableRow<T>() {
                @Override
                public void updateItem(T item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null) {
                        setStyle(null);
                    } else if (!item.getActive()) {
                        setStyle("-fx-font-style: oblique;");
                    } else {
                        setStyle("");
                    }
                }
            };

            row.setOnMouseClicked(event -> {

                if (event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY && (!row.isEmpty())) {
                    T rowData = row.getItem();
                    if (obtainMode) {
                        setObtainItem(rowData);
                        setObtainItem(null);

                    } else {
                        setOpen(rowData);
                        setOpen(null);
                    }
                }
            });
            row.setOnDragDetected(event -> {
                if (!row.isEmpty()) {
                    T item = row.getItem();
                    if (item instanceof FxEntity) {
                        FxEntity fxEntity = (FxEntity) item;
                        AbstractDto entity = fxEntity.getEntity();
                        Dragboard db = row.startDragAndDrop(TransferMode.MOVE);
                        db.setDragView(row.snapshot(null, null));
                        ClipboardContent cc = new ClipboardContent();
                        cc.put(tableRowDataFormat, entity);
                        db.setContent(cc);
                    }
                    event.consume();
                }
            });
            row.setOnDragDone(new EventHandler<DragEvent>() {
                @Override
                public void handle(DragEvent event) {
                    TableElement contentTableRow = (TableElement) event.getDragboard().getContent(TableViewPane.tableRowDataFormat);
                    Iterator<T> iterator = source.iterator();
                    while (iterator.hasNext()) {
                        T next = iterator.next();
                        if (next.getGuid().equals(contentTableRow.getGuid())) {
                            FxEntity entity = (FxEntity) next;
                            FxEntity fxEntity = entity.getHelper().getFxEntity((AbstractDto) contentTableRow);
                            TableElement tableElement = (TableElement) fxEntity;
                            next.setGroup(tableElement.getGroup());
                            toSave.setValue(next);
                            iterator.remove();
                            break;
                        }
                    }
                }
            });
            return row;
        });
        VBox.setVgrow(tableView, Priority.ALWAYS);
        getChildren().add(tableView);
        editable.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                add.setDisable(!newValue);
            }
        });

    }


    public T getToSave() {
        return toSave.get();
    }

    public ObjectProperty<T> toSaveProperty() {
        return toSave;
    }

    public void setToSave(T toSave) {
        this.toSave.set(toSave);
    }

    private List<T> findItems(String text) {
        List<T> result = new ArrayList<T>();
        for (T t : tableView.getItems()) {
            if (t.getName().toLowerCase().contains(text.toLowerCase())) {
                result.add(t);
            }
        }
        return result;
    }

    public <S> void addColumns(TableColumn<T, S>... columns) {
        tableView.getColumns().addAll(columns);
    }

    public boolean isEditable() {
        return editable.get();
    }

    public BooleanProperty editableProperty() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable.set(editable);
    }

    public void setItems(List<T> items) {
        source = FXCollections.observableArrayList(items);

        SortedList<T> sortedList = new SortedList<T>(new FilteredList(source));
        sortedList.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedList);
    }

    public boolean isOnlyActive() {
        return onlyActive.get();
    }

    public BooleanProperty onlyActiveProperty() {
        return onlyActive;
    }

    public void setOnlyActive(boolean onlyActive) {
        this.onlyActive.set(onlyActive);
    }

    public T getOpen() {
        return open.get();
    }

    public ObjectProperty<T> openProperty() {
        return open;
    }

    public void setOpen(T open) {
        this.open.set(open);
    }

    public boolean isObtainMode() {
        return obtainMode;
    }

    public void setObtainMode(boolean obtainMode) {
        this.obtainMode = obtainMode;
    }

    public T getObtainItem() {
        return obtainItem.get();
    }

    public ObjectProperty<T> obtainItemProperty() {
        return obtainItem;
    }

    public void setObtainItem(T obtainItem) {
        this.obtainItem.set(obtainItem);
    }
}
