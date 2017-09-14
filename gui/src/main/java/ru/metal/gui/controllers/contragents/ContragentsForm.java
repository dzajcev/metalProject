package ru.metal.gui.controllers.contragents;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import ru.metal.api.contragents.request.ObtainContragentGroupRequest;
import ru.metal.api.contragents.request.ObtainContragentRequest;
import ru.metal.api.contragents.request.UpdateContragentRequest;
import ru.metal.api.contragents.response.ObtainContragentResponse;
import ru.metal.dto.ContragentFx;
import ru.metal.dto.ContragentGroupFx;
import ru.metal.dto.helper.ContragentHelper;
import ru.metal.gui.StartPage;
import ru.metal.gui.controls.tableviewcontrol.TableViewPane;
import ru.metal.gui.controls.treeviewcontrol.TreeViewPane;
import ru.metal.gui.windows.Window;
import ru.metal.rest.ContragentsClient;

import java.util.List;

/**
 * Created by User on 08.08.2017.
 */
public class ContragentsForm extends AnchorPane {
    public static final String ID = "contragents";
    private ContragentsClient contragentsClient;
    TableViewPane<ContragentFx> tableViewPane = new TableViewPane<>(ContragentFx.class);

    private ObtainContragentRequest obtainContragentRequest;
    private ObtainContragentGroupRequest groupRequest;
    public ContragentsForm(boolean editable, ObtainContragentRequest obtainContragentRequest) {
        contragentsClient = new ContragentsClient();
        setRequest(obtainContragentRequest);

        setId(ID);
        SplitPane splitPane = new SplitPane();
        AnchorPane.setTopAnchor(splitPane, 0.0);
        AnchorPane.setBottomAnchor(splitPane, 0.0);
        AnchorPane.setLeftAnchor(splitPane, 0.0);
        AnchorPane.setRightAnchor(splitPane, 0.0);
        splitPane.setDividerPositions(0.3);
        getChildren().add(splitPane);

        TreeViewPane<ContragentGroupFx> treeView = new TreeViewPane(contragentsClient, "Контрагенты", ContragentGroupFx.class);
        treeView.setEditable(editable);
        splitPane.getItems().add(treeView);
        treeView.setRequest(groupRequest);

        tableViewPane.openProperty().addListener(new ChangeListener<ContragentFx>() {
            @Override
            public void changed(ObservableValue<? extends ContragentFx> observable, ContragentFx oldValue, ContragentFx newValue) {
                if (newValue == null) {
                    return;
                }
                if (newValue.getGroup() == null) {
                    newValue.setGroup(treeView.getSelectedItem());
                }
                Window<ContragentInfoController, ContragentsForm> window = StartPage.openContent("/fxml/ContragentInfo.fxml", (Stage) getScene().getWindow());
                window.getController().initContragent(newValue);
                window.getController().savedProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        if (newValue) {
                            ObtainContragentRequest request = new ObtainContragentRequest();
                            request.getContragentTypes().addAll(obtainContragentRequest.getContragentTypes());
                            request.getPersonTypes().addAll(obtainContragentRequest.getPersonTypes());
                            request.setGroupGuids(treeView.getSelectedGroups());
                            request.setActive(tableViewPane.isOnlyActive());
                            tableViewPane.setItems(obtainContragents(request));
                        }
                    }
                });
                window.setTitle("Карточка контрагента");
                window.setClosable(true);
                window.setMaximizable(false);
                window.setModal(true);
                StartPage.addWindow(window);
            }
        });

        treeView.selectedItemProperty().addListener(new ChangeListener<ContragentGroupFx>() {
            @Override
            public void changed(ObservableValue<? extends ContragentGroupFx> observable, ContragentGroupFx oldValue, ContragentGroupFx newValue) {
                boolean root = newValue.getGuid().equals("-1");

                tableViewPane.setEditable(!root);
            }
        });
        treeView.selectRoot();
        treeView.selectedGroupsProperty().addListener(new ChangeListener<List<String>>() {
            @Override
            public void changed(ObservableValue<? extends List<String>> observable, List<String> oldValue, List<String> newValue) {
                ObtainContragentRequest request = new ObtainContragentRequest();
                request.getContragentTypes().addAll(obtainContragentRequest.getContragentTypes());
                request.getPersonTypes().addAll(obtainContragentRequest.getPersonTypes());
                request.setGroupGuids(newValue);
                request.setActive(tableViewPane.isOnlyActive());
                tableViewPane.setItems(obtainContragents(request));
            }
        });
        tableViewPane.onlyActiveProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                ObtainContragentRequest request = new ObtainContragentRequest();
                request.setGroupGuids(treeView.getSelectedGroups());
                request.getContragentTypes().addAll(obtainContragentRequest.getContragentTypes());
                request.getPersonTypes().addAll(obtainContragentRequest.getPersonTypes());
                request.setActive(newValue);

                tableViewPane.setItems(obtainContragents(request));

            }
        });

        TableColumn name = new TableColumn("Название");
        name.setMinWidth(100);
        name.setCellValueFactory(
                new PropertyValueFactory<ContragentFx, String>("name"));

        TableColumn address = new TableColumn("Адрес");
        address.setMinWidth(100);
        address.setCellValueFactory(
                new PropertyValueFactory<ContragentFx, String>("address"));

        TableColumn phone = new TableColumn("Телефон");
        phone.setMinWidth(100);
        phone.setCellValueFactory(
                new PropertyValueFactory<ContragentFx, String>("phone"));

        TableColumn inn = new TableColumn("ИНН");
        inn.setMinWidth(100);
        inn.setCellValueFactory(
                new PropertyValueFactory<ContragentFx, String>("inn"));

        TableColumn kpp = new TableColumn("КПП");
        kpp.setMinWidth(100);
        kpp.setCellValueFactory(
                new PropertyValueFactory<ContragentFx, String>("kpp"));

        TableColumn group = new TableColumn("Группа");
        group.setMinWidth(100);
        group.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ContragentFx, String>, ObservableValue>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ContragentFx, String> p) {
                return new ReadOnlyObjectWrapper<>(p.getValue().getGroup().getName());
            }
        });


        tableViewPane.addColumns(name, address, phone, inn, kpp, group);
        tableViewPane.toSaveProperty().addListener(new ChangeListener<ContragentFx>() {
            @Override
            public void changed(ObservableValue<? extends ContragentFx> observable, ContragentFx oldValue, ContragentFx newValue) {
                if (newValue != null) {
                    UpdateContragentRequest request = new UpdateContragentRequest();
                    request.getDataList().add(newValue.getEntity());

                        contragentsClient.updateContragents(request);
                }
            }
        });
        splitPane.getItems().add(tableViewPane);
    }


    public void setRequest(ObtainContragentRequest request) {
        this.obtainContragentRequest=request;
        groupRequest = new ObtainContragentGroupRequest();

        groupRequest.setContragentTypes(request.getContragentTypes());
        groupRequest.setPersonTypes(request.getPersonTypes());
        tableViewPane.setItems(obtainContragents(request));
    }

    private ObservableList<ContragentFx> obtainContragents(ObtainContragentRequest obtainContragentRequest) {
            ObtainContragentResponse response = contragentsClient.getContragents(obtainContragentRequest);
            return ContragentHelper.getInstance().getFxCollection(response.getDataList());

    }

    public void setObtainMode(boolean obtainMode) {
        tableViewPane.setObtainMode(obtainMode);
    }

    public boolean isObtainMode() {
        return tableViewPane.isObtainMode();
    }

    public ObjectProperty<ContragentFx> obtainItemProperty() {
        return tableViewPane.obtainItemProperty();
    }
}
