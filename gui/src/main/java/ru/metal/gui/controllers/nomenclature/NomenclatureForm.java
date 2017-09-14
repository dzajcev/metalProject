package ru.metal.gui.controllers.nomenclature;

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
import ru.common.api.request.ObtainTreeItemRequest;
import ru.metal.api.nomenclature.request.ObtainGoodRequest;
import ru.metal.api.nomenclature.request.UpdateGoodsRequest;
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

    private TableViewPane<GoodFx> tableViewPane = new TableViewPane<>(GoodFx.class);

    public static final String ID = "nomenclature";

    public NomenclatureForm(boolean editable) {
        nomenclatureClient = new NomenclatureClient();
        setId(ID);
        SplitPane splitPane = new SplitPane();
        AnchorPane.setTopAnchor(splitPane, 0.0);
        AnchorPane.setBottomAnchor(splitPane, 0.0);
        AnchorPane.setLeftAnchor(splitPane, 0.0);
        AnchorPane.setRightAnchor(splitPane, 0.0);
        splitPane.setDividerPositions(0.3);
        getChildren().add(splitPane);

        TreeViewPane<GroupFx> treeView = new TreeViewPane(nomenclatureClient, "Номенклатура", GroupFx.class);
        treeView.setRequest(new ObtainTreeItemRequest());
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
                            ObtainGoodRequest obtainGoodRequest = new ObtainGoodRequest();
                            obtainGoodRequest.setGroupGuids(treeView.getSelectedGroups());
                            obtainGoodRequest.setActive(tableViewPane.isOnlyActive());
                            tableViewPane.setItems(obtainGoods(obtainGoodRequest));
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
                boolean hasChilden = treeView.hasChildren(newValue);
                tableViewPane.setEditable(!root && !hasChilden);
            }
        });
        treeView.selectedGroupsProperty().addListener(new ChangeListener<List<String>>() {
            @Override
            public void changed(ObservableValue<? extends List<String>> observable, List<String> oldValue, List<String> newValue) {
                ObtainGoodRequest obtainGoodRequest = new ObtainGoodRequest();
                obtainGoodRequest.setGroupGuids(newValue);
                obtainGoodRequest.setActive(tableViewPane.isOnlyActive());
                tableViewPane.setItems(obtainGoods(obtainGoodRequest));
            }
        });
        tableViewPane.onlyActiveProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                ObtainGoodRequest request = new ObtainGoodRequest();
                request.setGroupGuids(treeView.getSelectedGroups());
                request.setActive(newValue);
                tableViewPane.setItems(obtainGoods(request));

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
        tableViewPane.toSaveProperty().addListener(new ChangeListener<GoodFx>() {
            @Override
            public void changed(ObservableValue<? extends GoodFx> observable, GoodFx oldValue, GoodFx newValue) {
                if (newValue != null) {
                    UpdateGoodsRequest updateGoodsRequest = new UpdateGoodsRequest();
                    updateGoodsRequest.getDataList().add(newValue.getEntity());

                    nomenclatureClient.updateGoods(updateGoodsRequest);

                }
            }
        });
        splitPane.getItems().add(tableViewPane);
    }

    private ObservableList<GoodFx> obtainGoods(ObtainGoodRequest obtainGoodRequest) {
        ObtainGoodResponse response = nomenclatureClient.getGoods(obtainGoodRequest);
        return GoodHelper.getInstance().getFxCollection(response.getDataList());
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
