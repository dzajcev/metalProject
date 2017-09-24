package ru.metal.gui.controllers.documents;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.Robot;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.util.Callback;
import ru.common.api.request.ObtainTreeItemRequest;
import ru.common.api.response.AbstractResponse;
import ru.metal.api.contragents.dto.ContragentType;
import ru.metal.api.contragents.request.ObtainContragentRequest;
import ru.metal.api.contragents.response.ObtainContragentResponse;
import ru.metal.api.documents.DocumentStatus;
import ru.metal.api.users.request.ObtainDelegatingUsersRequest;
import ru.metal.api.users.response.ObtainDelegatingUsersResponse;
import ru.metal.dto.ContragentFx;
import ru.metal.dto.ContragentGroupFx;
import ru.metal.dto.DocumentHeader;
import ru.metal.dto.helper.ContragentHelper;
import ru.metal.dto.response.ObtainTreeItemResponse;
import ru.metal.gui.StartPage;
import ru.metal.gui.controllers.AbstractController;
import ru.metal.gui.controls.MultiSelectTree;
import ru.metal.gui.windows.LabelButton;
import ru.metal.gui.windows.Window;
import ru.metal.rest.ContragentsClient;
import ru.metal.rest.UserClient;
import ru.metal.security.ejb.UserContextHolder;
import ru.metal.security.ejb.security.DelegatingUser;
import ru.metal.security.ejb.security.SecurityUtils;

import java.util.*;

/**
 * Created by User on 20.09.2017.
 */
public class DocumentJournalController<T extends DocumentHeader, E extends DocumentStatus> extends AbstractController {

    private DocumentSelector<T, E> selector;

    @FXML
    private AnchorPane root;

    @FXML
    private DatePicker from;

    @FXML
    private DatePicker to;

    @FXML
    private TextField source;

    @FXML
    private TextField recipient;

    @FXML
    private TextField employees;

    @FXML
    private Label employeesLabel;

    @FXML
    private TableView<T> body;

    @FXML
    private TextField status;

    @FXML
    private LabelButton chooseStatus;

    @FXML
    private LabelButton chooseSource;

    @FXML
    private LabelButton chooseRecipient;

    @FXML
    private LabelButton chooseEmployee;

    @FXML
    private LabelButton refresh;

    @FXML
    private Label totalSum;

    private List<ContragentFx> sources = FXCollections.observableArrayList();
    private ObservableList<ContragentFx> recipients = FXCollections.observableArrayList();
    private ObservableList<E> statuses = FXCollections.observableArrayList();

    private ObservableList<DelegatingUser> allUsers = FXCollections.observableArrayList();
    private ObservableList<DelegatingUser> selectedUsers = FXCollections.observableArrayList();

    private ObservableList<ContragentFx> allSource = FXCollections.observableArrayList();
    private ObservableList<ContragentGroupFx> allSourceGroups = FXCollections.observableArrayList();

    private ObservableList<ContragentFx> allRecipient = FXCollections.observableArrayList();
    private ObservableList<ContragentGroupFx> allRecipientGroups = FXCollections.observableArrayList();

    private ContragentsClient contragentsClient = new ContragentsClient();

    @Override
    protected AbstractResponse save() {
        return null;
    }

    @FXML
    private void initialize() {
        UserClient userClient = new UserClient();
        ObtainDelegatingUsersRequest obtainDelegatingUsersRequest = new ObtainDelegatingUsersRequest();
        obtainDelegatingUsersRequest.setUserGuids(Collections.singletonList(SecurityUtils.getUserGUID()));
        ObtainDelegatingUsersResponse usersResponse = userClient.getDelegateUsers(obtainDelegatingUsersRequest);
        allUsers.addAll(usersResponse.getDataList());

        DelegatingUser delegatingUser = new DelegatingUser();
        delegatingUser.setUserGuid(UserContextHolder.getPermissionContextDataThreadLocal().getUser().getGuid());
        delegatingUser.setUserName(UserContextHolder.getPermissionContextDataThreadLocal().getUser().getShortName());
        selectedUsers.add(delegatingUser);
        employees.setText(delegatingUser.getUserName());


        ObtainContragentRequest obtainContragentRequest = new ObtainContragentRequest();
        obtainContragentRequest.setActive(true);
        obtainContragentRequest.getUserGuids().addAll(getUserGuids(selectedUsers));
        obtainContragentRequest.getContragentTypes().add(ContragentType.SOURCE);
        ObtainContragentResponse contragents = contragentsClient.getContragents(obtainContragentRequest);
        allSource.addAll(ContragentHelper.getInstance().getFxCollection(contragents.getDataList()));
        allSourceGroups = FXCollections.observableArrayList(getGroups(allSource));

        obtainContragentRequest.setActive(true);
        obtainContragentRequest.getUserGuids().addAll(getUserGuids(selectedUsers));
        obtainContragentRequest.getContragentTypes().add(ContragentType.BUYER);
        contragents = contragentsClient.getContragents(obtainContragentRequest);
        allRecipient.addAll(ContragentHelper.getInstance().getFxCollection(contragents.getDataList()));
        allRecipientGroups = FXCollections.observableArrayList(getGroups(allRecipient));

        chooseStatus.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/icons/open.png"))));
        chooseSource.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/icons/open.png"))));
        chooseRecipient.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/icons/open.png"))));
        chooseEmployee.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/icons/open.png"))));
        refresh.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/icons/refresh.png"))));
    }

    public void setDocumentSelector(DocumentSelector<T, E> selector) {
        root.setId(selector.getId());
        setButtonActions(selector);


        ObtainDocumentsRequest request = new ObtainDocumentsRequest();
        request.setRecipients(recipients);
        request.setSources(sources);

        request.getStatuses().addAll(statuses);

        ObservableList<T> documents = selector.getDocuments(request);
    }

    private List<ContragentGroupFx> getGroups(List<ContragentFx> contargents) {
        ObtainTreeItemRequest obtainTreeItemRequest = new ObtainTreeItemRequest();
        for (ContragentFx contragentFx : contargents) {
            obtainTreeItemRequest.getGroupGuids().add(contragentFx.getGroup().getGuid());
        }
        ObtainTreeItemResponse<ContragentGroupFx> groupItems = contragentsClient.getGroupFullItems(obtainTreeItemRequest);
        return groupItems.getDataList();
    }

    private void setButtonActions(DocumentSelector<T, E> selector) {
        chooseStatus.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, null);
                DialogPane dialogPane = new DialogPane();
                MultiSelectListView<E> selectListView = new MultiSelectListView<E>(selector.getDocumentStatusValues(), statuses);
                dialogPane.setContent(selectListView);
                alert.setTitle("Выберите статус(ы)");
                alert.setDialogPane(dialogPane);
                alert.setHeaderText(null);
                alert.initOwner(StartPage.primaryStage);
                alert.setContentText(null);
                ButtonType buttonTypeOne = new ButtonType("Ок");
                ButtonType buttonTypeCancel = new ButtonType("Отмена", ButtonBar.ButtonData.CANCEL_CLOSE);
                alert.getButtonTypes().addAll(buttonTypeOne, buttonTypeCancel);

                Optional<ButtonType> buttonType = alert.showAndWait();
                if (buttonType.get() == buttonTypeOne) {
                    statuses.clear();
                    statuses.addAll(selectListView.getSelectionModel().getSelectedItems());
                    StringBuilder stringBuilder = new StringBuilder();
                    if (!statuses.isEmpty()) {
                        for (E status : statuses) {
                            stringBuilder.append(status.getTitle()).append(", ");
                        }
                    }
                    stringBuilder.deleteCharAt(stringBuilder.lastIndexOf(","));
                    status.setText(stringBuilder.toString());
                }
            }
        });
        chooseEmployee.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, null);
                DialogPane dialogPane = new DialogPane();
                ListView<DelegatingUser> listView = new ListView<>();
                listView.setCellFactory(new Callback<ListView<DelegatingUser>, ListCell<DelegatingUser>>() {

                    @Override
                    public ListCell<DelegatingUser> call(ListView<DelegatingUser> p) {

                        ListCell<DelegatingUser> cell = new ListCell<DelegatingUser>() {

                            @Override
                            protected void updateItem(DelegatingUser t, boolean bln) {
                                super.updateItem(t, bln);
                                if (t != null) {
                                    setText(t.getUserName());
                                } else {
                                    setText(null);
                                }
                            }

                        };
                        return cell;
                    }
                });
                listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
                listView.getItems().sort(new Comparator<DelegatingUser>() {
                    @Override
                    public int compare(DelegatingUser o1, DelegatingUser o2) {
                        return o1.getUserName().compareTo(o2.getUserName());
                    }
                });
                listView.setItems(allUsers);
                if (selectedUsers.size() > 0) {
                    int[] idx = new int[selectedUsers.size() - 1];
                    for (int i = 1; i < selectedUsers.size(); i++) {
                        idx[i - 1] = listView.getItems().indexOf(selectedUsers.get(i));
                    }
                    listView.getSelectionModel().selectIndices(listView.getItems().indexOf(selectedUsers.get(0)), idx);
                }
                dialogPane.setContent(listView);
                alert.setTitle("Выберите сотрудников");
                alert.setDialogPane(dialogPane);
                alert.setHeaderText(null);
                alert.initOwner(StartPage.primaryStage);
                alert.setContentText(null);
                ButtonType buttonTypeOne = new ButtonType("Ок");
                ButtonType buttonTypeCancel = new ButtonType("Отмена", ButtonBar.ButtonData.CANCEL_CLOSE);
                alert.getButtonTypes().addAll(buttonTypeOne, buttonTypeCancel);

                Optional<ButtonType> buttonType = alert.showAndWait();
                if (buttonType.get() == buttonTypeOne) {
                    selectedUsers.clear();
                    selectedUsers.addAll(listView.getSelectionModel().getSelectedItems());
                    StringBuilder stringBuilder = new StringBuilder();
                    if (!selectedUsers.isEmpty()) {
                        for (DelegatingUser status : selectedUsers) {
                            stringBuilder.append(status.getUserName()).append(", ");
                        }
                    }
                    stringBuilder.deleteCharAt(stringBuilder.lastIndexOf(","));
                    employees.setText(stringBuilder.toString());
                }
            }
        });
        chooseSource.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                selectContragents(ContragentType.SOURCE);
            }
        });
        chooseRecipient.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                selectContragents(ContragentType.BUYER);
            }
        });
    }

    private void selectContragents(ContragentType contragentType){
        List<ContragentFx> contragents=null;
        List<ContragentGroupFx> groups=null;
        List<ContragentFx> selected=null;
        String nameTree=null;
        String title=null;
        TextField textField=null;
        if (contragentType==ContragentType.SOURCE){
            contragents=allSource;
            groups=allSourceGroups;
            selected=sources;
            nameTree="Источник(и)";
            textField=source;
            title="Выберите источник(и)";

        }else if (contragentType==ContragentType.BUYER){
            contragents=allRecipient;
            groups=allRecipientGroups;
            selected=recipients;
            nameTree="Получатель(ли)";
            textField=recipient;
            title="Выберите получателя(ей)";
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION, null);
        DialogPane dialogPane = new DialogPane();
        Robot robot =
                Application.GetApplication().createRobot();
        int x = robot.getMouseX();
        int y = robot.getMouseY();

        MultiSelectTree<ContragentGroupFx, ContragentFx> tree = new MultiSelectTree<>(groups, contragents, nameTree);
        alert.setX(x-tree.getPrefWidth()/2);
        alert.setY(y-tree.getPrefHeight()/4);
        tree.setSelected(selected, true);
        dialogPane.setContent(tree);
        alert.setTitle(title);
        alert.setDialogPane(dialogPane);
        alert.setHeaderText(null);
        alert.initOwner(StartPage.primaryStage);
        alert.initModality(Modality.WINDOW_MODAL);
        alert.setContentText(null);
        ButtonType buttonTypeOne = new ButtonType("Ок");
        ButtonType buttonTypeCancel = new ButtonType("Отмена", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().addAll(buttonTypeOne, buttonTypeCancel);

        Optional<ButtonType> buttonType = alert.showAndWait();
        if (buttonType.get() == buttonTypeOne) {
            List<ContragentFx> selectedItems = tree.getSelectedItems();
            selected.clear();
            selected.addAll(selectedItems);
            StringBuilder stringBuilder = new StringBuilder();
            if (!selected.isEmpty()) {
                for (ContragentFx status : selected) {
                    stringBuilder.append(status.getName()).append(", ");
                }
            }
            stringBuilder.deleteCharAt(stringBuilder.lastIndexOf(","));
            textField.setText(stringBuilder.toString());
        }
    }
    private List<String> getUserGuids(List<DelegatingUser> users) {
        List<String> result = new ArrayList<>();
        for (DelegatingUser user : users) {
            result.add(user.getUserGuid());
        }
        return result;
    }
}
