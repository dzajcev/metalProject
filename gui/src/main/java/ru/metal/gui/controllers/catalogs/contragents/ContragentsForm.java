package ru.metal.gui.controllers.catalogs.contragents;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import ru.metal.api.contragents.dto.ContragentType;
import ru.metal.api.users.request.ObtainDelegatingUsersRequest;
import ru.metal.api.users.response.ObtainDelegatingUsersResponse;
import ru.metal.dto.ContragentFx;
import ru.metal.dto.ContragentGroupFx;
import ru.metal.gui.StartPage;
import ru.metal.gui.controllers.catalogs.ItemSelector;
import ru.metal.gui.controls.tableviewcontrol.TableViewPane;
import ru.metal.gui.controls.treeviewcontrol.TreeViewPane;
import ru.metal.gui.windows.Window;
import ru.metal.rest.UserClient;
import ru.metal.security.ejb.security.DelegatingUser;
import ru.metal.security.ejb.security.SecurityUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by User on 08.08.2017.
 */
public class ContragentsForm extends AnchorPane {
    public static final String ID = "contragents";
    private TableViewPane<ContragentFx> tableViewPane;

    private final ContragentSelector selector;

    public ContragentsForm(boolean editable, ContragentType... contragentTypes) {
        ObtainDelegatingUsersRequest obtainDelegatingUsersRequest = new ObtainDelegatingUsersRequest();
        obtainDelegatingUsersRequest.setUserGuids(Collections.singletonList(SecurityUtils.getUserGUID()));
        UserClient userClient = new UserClient();
        ObtainDelegatingUsersResponse usersResponse = userClient.getDelegateUsers(obtainDelegatingUsersRequest);

        ContragentBaseCriteria contragentBaseCriteria = new ContragentBaseCriteria();
        contragentBaseCriteria.setContragentTypes(new ArrayList(Arrays.asList(contragentTypes)));
        contragentBaseCriteria.setUserGuids(getUserGuids(usersResponse.getDataList()));

        selector = new ContragentSelector(contragentBaseCriteria);
        ItemSelector<ContragentFx> itemSelector = selector.getItemSelector();

        tableViewPane = new TableViewPane(ContragentFx.class, itemSelector);

        setId(ID);
        SplitPane splitPane = new SplitPane();
        AnchorPane.setTopAnchor(splitPane, 0.0);
        AnchorPane.setBottomAnchor(splitPane, 0.0);
        AnchorPane.setLeftAnchor(splitPane, 0.0);
        AnchorPane.setRightAnchor(splitPane, 0.0);
        splitPane.setDividerPositions(0.3);
        getChildren().add(splitPane);

        TreeViewPane<ContragentGroupFx> treeView = new TreeViewPane(this.selector.getGroupSelector(), "Контрагенты", ContragentGroupFx.class);
        treeView.setEditable(editable);
        splitPane.getItems().add(treeView);

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
                            tableViewPane.addItem(window.getController().getContragent());
                            selector.getItemSelector().getAllItems().add(window.getController().getContragent());
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
                List<ContragentFx> items = selector.getItemSelector().getItems(newValue, tableViewPane.isOnlyActive());
                tableViewPane.setItems(items);
            }
        });
        tableViewPane.onlyActiveProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                List<ContragentFx> items = selector.getItemSelector().getItems(treeView.getSelectedGroups(), tableViewPane.isOnlyActive());
                tableViewPane.setItems(items);
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
        splitPane.getItems().add(tableViewPane);
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

    private List<String> getUserGuids(List<DelegatingUser> users) {
        List<String> result = new ArrayList<>();
        for (DelegatingUser user : users) {
            result.add(user.getUserGuid());
        }
        return result;
    }
}
