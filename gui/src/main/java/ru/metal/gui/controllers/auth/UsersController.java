package ru.metal.gui.controllers.auth;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import ru.common.api.response.AbstractResponse;
import ru.metal.api.auth.request.ObtainUserRequest;
import ru.metal.api.auth.response.ObtainRegistrationRequestsResponse;
import ru.metal.api.auth.response.ObtainUserResponse;
import ru.metal.dto.GoodFx;
import ru.metal.dto.RegistrationRequestDataFx;
import ru.metal.dto.UserFx;
import ru.metal.dto.helper.RegistrationRequestDataHelper;
import ru.metal.dto.helper.UserHelper;
import ru.metal.gui.StartPage;
import ru.metal.gui.controllers.AbstractController;
import ru.metal.gui.windows.Window;
import ru.metal.rest.AdminClient;


/**
 * Created by User on 18.09.2017.
 */
public class UsersController extends AbstractController {
    public static final String ID = "users";

    @FXML
    private TableView<UserFx> usersTable;

    @FXML
    private CheckBox selected;

    private ObservableList<UserFx> fxCollection = FXCollections.observableArrayList();

    private AdminClient adminClient = new AdminClient();

    @FXML
    private void initialize() {

        TableColumn login = new TableColumn("Логин");
        login.setMinWidth(100);
        login.setCellValueFactory(
                new PropertyValueFactory<UserFx, String>("login"));

        TableColumn firstName = new TableColumn("Имя");
        firstName.setMinWidth(100);
        firstName.setCellValueFactory(
                new PropertyValueFactory<UserFx, String>("firstName"));

        TableColumn middleName = new TableColumn("Отчество");
        middleName.setMinWidth(100);
        middleName.setCellValueFactory(
                new PropertyValueFactory<UserFx, String>("middleName"));

        TableColumn secondName = new TableColumn("Фамилия");
        secondName.setMinWidth(100);
        secondName.setCellValueFactory(
                new PropertyValueFactory<UserFx, String>("secondName"));

        TableColumn email = new TableColumn("Email");
        email.setMinWidth(100);
        email.setCellValueFactory(
                new PropertyValueFactory<UserFx, String>("email"));

        usersTable.getColumns().addAll(login, firstName, middleName, secondName, email);

        ObtainUserRequest obtainUserRequest = new ObtainUserRequest();
        obtainUserRequest.setActive(true);

        ObtainUserResponse obtainUserResponse = adminClient.getUsers(obtainUserRequest);
        fxCollection.clear();
        fxCollection.addAll(UserHelper.getInstance().getFxCollection(obtainUserResponse.getDataList()));

        selected.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                ObtainUserRequest obtainUserRequest = new ObtainUserRequest();
                if (newValue) {
                    obtainUserRequest.setActive(true);
                } else {
                    obtainUserRequest.setActive(false);
                }
                ObtainUserResponse obtainUserResponse = adminClient.getUsers(obtainUserRequest);
                fxCollection.clear();
                fxCollection.addAll(UserHelper.getInstance().getFxCollection(obtainUserResponse.getDataList()));
            }
        });
        SortedList<UserFx> sortedList = new SortedList(new FilteredList(fxCollection));
        sortedList.comparatorProperty().bind(usersTable.comparatorProperty());
        usersTable.setItems(sortedList);

        usersTable.setRowFactory(tv -> {

            TableRow<UserFx> row = new TableRow<UserFx>() {
                @Override
                public void updateItem(UserFx item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null) {
                        setStyle(null);
                    } else if (!item.isActive()) {
                        setStyle("-fx-font-style: oblique;");
                    } else {
                        setStyle("");
                    }
                }
            };

            row.setOnMouseClicked(event -> {

                if (event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY && (!row.isEmpty())) {
                    UserFx rowData = row.getItem();
                    Window<UserInfoController, Node> window = StartPage.openContent("/fxml/UserInfo.fxml", getPrimaryStage());
                    window.getController().setUser(rowData);
                    window.getController().savedProperty().addListener(new ChangeListener<Boolean>() {
                        @Override
                        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                            if (newValue) {
                                if (!rowData.isActive()) {
                                    if (!selected.isSelected()) {
                                        row.setStyle("-fx-font-style: oblique;");
                                    } else {
                                        fxCollection.remove(rowData);
                                    }
                                } else {
                                    row.setStyle("");
                                }

                            }
                        }
                    });
                    window.setTitle("Сведения о пользователе");
                    window.setClosable(true);
                    window.setMaximizable(false);
                    window.setModal(true);
                    StartPage.addWindow(window);
                }
            });
            return row;
        });
    }

    @Override
    protected AbstractResponse save() {
        return null;
    }
}
