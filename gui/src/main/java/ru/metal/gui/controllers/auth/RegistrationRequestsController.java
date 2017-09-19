package ru.metal.gui.controllers.auth;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.*;
import javafx.stage.Stage;
import ru.common.api.dto.TableElement;
import ru.metal.api.auth.response.ObtainRegistrationRequestsResponse;
import ru.metal.api.nomenclature.request.ObtainGoodRequest;
import ru.metal.dto.FxEntity;
import ru.metal.dto.GoodFx;
import ru.metal.dto.RegistrationRequestDataFx;
import ru.metal.dto.helper.RegistrationRequestDataHelper;
import ru.metal.gui.StartPage;
import ru.metal.gui.controllers.AbstractController;
import ru.metal.gui.controllers.nomenclature.GoodInfoController;
import ru.metal.gui.controls.tableviewcontrol.TableViewPane;
import ru.metal.gui.windows.Window;
import ru.metal.rest.AdminClient;
import ru.metal.security.ejb.dto.AbstractDto;

import java.util.Iterator;


/**
 * Created by User on 18.09.2017.
 */
public class RegistrationRequestsController extends AbstractController {
    public static final String ID = "registrationRequests";

    @FXML
    private TableView requests;

    @FXML
    private void initialize() {

        TableColumn login = new TableColumn("Логин");
        login.setMinWidth(100);
        login.setCellValueFactory(
                new PropertyValueFactory<GoodFx, String>("login"));

        TableColumn firstName = new TableColumn("Имя");
        firstName.setMinWidth(100);
        firstName.setCellValueFactory(
                new PropertyValueFactory<GoodFx, String>("firstName"));

        TableColumn middleName = new TableColumn("Отчество");
        middleName.setMinWidth(100);
        middleName.setCellValueFactory(
                new PropertyValueFactory<GoodFx, String>("middleName"));

        TableColumn secondName = new TableColumn("Фамилия");
        secondName.setMinWidth(100);
        secondName.setCellValueFactory(
                new PropertyValueFactory<GoodFx, String>("secondName"));

        TableColumn email = new TableColumn("Email");
        email.setMinWidth(100);
        email.setCellValueFactory(
                new PropertyValueFactory<GoodFx, String>("email"));

        requests.getColumns().addAll(firstName,middleName,secondName,email);
        AdminClient adminClient = new AdminClient();
        ObtainRegistrationRequestsResponse registrationRequests = adminClient.getRegistrationRequests();
        ObservableList<RegistrationRequestDataFx> fxCollection = RegistrationRequestDataHelper.getInstance().getFxCollection(registrationRequests.getDataList());

        SortedList<RegistrationRequestDataFx> sortedList = new SortedList(new FilteredList(fxCollection));
        sortedList.comparatorProperty().bind(requests.comparatorProperty());
        requests.setItems(sortedList);

        requests.setRowFactory(tv -> {

            TableRow<RegistrationRequestDataFx> row = new TableRow<RegistrationRequestDataFx>();

            row.setOnMouseClicked(event -> {

                if (event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY && (!row.isEmpty())) {
                    RegistrationRequestDataFx rowData = row.getItem();
                    Window<UserInfoController, Node> window = StartPage.openContent("/fxml/UserInfo.fxml", getPrimaryStage());
                    window.getController().setRegistrationRequest(rowData);

                    window.setTitle("Сведения о запросе на регистрацию");
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
    protected boolean save() {
        return false;
    }
}
