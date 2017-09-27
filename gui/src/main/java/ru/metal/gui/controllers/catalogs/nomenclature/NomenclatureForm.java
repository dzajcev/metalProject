package ru.metal.gui.controllers.catalogs.nomenclature;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import ru.metal.api.nomenclature.request.ObtainGoodRequest;
import ru.metal.api.nomenclature.response.ObtainGoodResponse;
import ru.metal.dto.GoodFx;
import ru.metal.dto.GroupFx;
import ru.metal.dto.helper.GoodHelper;
import ru.metal.gui.StartPage;
import ru.metal.gui.controls.tableviewcontrol.TableViewPane;
import ru.metal.gui.controls.treeviewcontrol.TreeViewPane;
import ru.metal.gui.windows.Window;
import ru.metal.rest.NomenclatureClient;

import java.util.List;

/**
 * Created by User on 08.08.2017.
 */
public class NomenclatureForm extends AnchorPane {

    private NomenclatureClient nomenclatureClient;

    private final TableViewPane<GoodFx> tableViewPane;
    private final NomenclatureSelector selector;

    public static final String ID = "nomenclature";

    public NomenclatureForm(boolean editable) {
        selector=new NomenclatureSelector();
        nomenclatureClient = new NomenclatureClient();
        tableViewPane = new TableViewPane<>(GoodFx.class,selector.getItemSelector());

        setId(ID);
        SplitPane splitPane = new SplitPane();
        AnchorPane.setTopAnchor(splitPane, 0.0);
        AnchorPane.setBottomAnchor(splitPane, 0.0);
        AnchorPane.setLeftAnchor(splitPane, 0.0);
        AnchorPane.setRightAnchor(splitPane, 0.0);
        splitPane.setDividerPositions(0.3);
        getChildren().add(splitPane);

        TreeViewPane<GroupFx> treeView =new TreeViewPane(selector.getGroupSelector(), "Номенклатура", GroupFx.class);
        treeView.setEditable(editable);
        splitPane.getItems().add(treeView);


        tableViewPane.openProperty().addListener(new ChangeListener<GoodFx>() {
            @Override
            public void changed(ObservableValue<? extends GoodFx> observable, GoodFx oldValue, GoodFx newValue) {
                if (newValue == null) {
                    return;
                }
                if (newValue.getGroup() == null) {
                    newValue.setGroup(treeView.getSelectedItem());
                }
                Window<GoodInfoController, Node> window = StartPage.openContent("/fxml/GoodInfo.fxml", (Stage) getScene().getWindow());
                window.getController().setGood(newValue);
                window.getController().savedProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        if (newValue) {
                            tableViewPane.addItem(window.getController().getGood());
                            selector.getItemSelector().getAllItems().add(window.getController().getGood());
                        }
                    }
                });
                window.setTitle("Карточка товара");
                window.setClosable(true);
                window.setMaximizable(false);
                window.setModal(true);
                StartPage.addWindow(window);
            }
        });
        treeView.selectedItemProperty().addListener(new ChangeListener<GroupFx>() {
            @Override
            public void changed(ObservableValue<? extends GroupFx> observable, GroupFx oldValue, GroupFx newValue) {
                boolean root = newValue.getGuid().equals("-1");
                tableViewPane.setEditable(!root);
            }
        });
        treeView.selectedGroupsProperty().addListener(new ChangeListener<List<String>>() {
            @Override
            public void changed(ObservableValue<? extends List<String>> observable, List<String> oldValue, List<String> newValue) {
                List<GoodFx> items = selector.getItemSelector().getItems(newValue, tableViewPane.isOnlyActive());
                tableViewPane.setItems(items);
            }
        });
        tableViewPane.onlyActiveProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                List<GoodFx> items = selector.getItemSelector().getItems(treeView.getSelectedGroups(), tableViewPane.isOnlyActive());
                tableViewPane.setItems(items);

            }
        });
        treeView.selectRoot();
        TableColumn name = new TableColumn("Название");
        name.setMinWidth(100);
        name.setCellValueFactory(
                new PropertyValueFactory<GoodFx, String>("name"));

        TableColumn group = new TableColumn("Группа");
        group.setMinWidth(100);
        group.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<GoodFx, String>, ObservableValue>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<GoodFx, String> p) {
                return new ReadOnlyObjectWrapper<>(p.getValue().getGroup().getName());
            }
        });

        TableColumn okeiColumn = new TableColumn("Ед. изм.");
        okeiColumn.setMinWidth(100);
        okeiColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<GoodFx, String>, ObservableValue>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<GoodFx, String> p) {
                if (p.getValue().getOkei() != null) {
                    return new ReadOnlyObjectWrapper<>(p.getValue().getOkei().getName());
                } else {
                    return new ReadOnlyObjectWrapper<>("");
                }
            }
        });

        tableViewPane.addColumns(name, okeiColumn, group);
        splitPane.getItems().add(tableViewPane);
    }
    public void setObtainMode(boolean obtainMode) {
        tableViewPane.setObtainMode(obtainMode);
    }

    public boolean isObtainMode() {
        return tableViewPane.isObtainMode();
    }

    public ObjectProperty<GoodFx> obtainItemProperty() {
        return tableViewPane.obtainItemProperty();
    }
}
