package ru.metal.gui.controllers.documents;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.Robot;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import ru.common.api.dto.CellFormats;
import ru.common.api.dto.TableElement;
import ru.common.api.request.ObtainTreeItemRequest;
import ru.common.api.response.AbstractResponse;
import ru.metal.api.contragents.dto.ContragentType;
import ru.metal.api.contragents.request.ObtainContragentRequest;
import ru.metal.api.contragents.response.ObtainContragentResponse;
import ru.metal.api.documents.DocumentStatus;
import ru.metal.api.users.request.ObtainDelegatingUsersRequest;
import ru.metal.api.users.response.ObtainDelegatingUsersResponse;
import ru.metal.dto.*;
import ru.metal.dto.helper.ContragentHelper;
import ru.metal.dto.response.ObtainTreeItemResponse;
import ru.metal.gui.StartPage;
import ru.metal.gui.controllers.AbstractController;
import ru.metal.gui.controls.MultiSelectTree;
import ru.metal.gui.controls.tableviewcontrol.TableViewPane;
import ru.metal.gui.windows.LabelButton;
import ru.metal.rest.ContragentsClient;
import ru.metal.rest.UserClient;
import ru.metal.security.ejb.UserContextHolder;
import ru.metal.security.ejb.dto.AbstractDto;
import ru.metal.security.ejb.security.DelegatingUser;
import ru.metal.security.ejb.security.SecurityUtils;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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
    private TableView<T> body;

    @FXML
    private TextField documentStatus;

    @FXML
    private LabelButton chooseStatus;

    @FXML
    private LabelButton chooseSource;

    @FXML
    private LabelButton chooseRecipient;

    @FXML
    private LabelButton chooseEmployee;

    @FXML
    private LabelButton resetStatus;

    @FXML
    private LabelButton resetSource;

    @FXML
    private LabelButton resetRecipient;

    @FXML
    private LabelButton resetEmployee;

    @FXML
    private LabelButton refreshButton;

    @FXML
    private Label totalSum;

    private final ObservableList<T> documents=FXCollections.observableArrayList();

    private  final DecimalFormat formatter = new DecimalFormat(CellFormats.TWO_DECIMAL_PLACES);

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
        obtainContragentRequest.setContragentTypes(Collections.singletonList(ContragentType.SOURCE));
        ObtainContragentResponse contragents = contragentsClient.getContragents(obtainContragentRequest);
        allSource.addAll(ContragentHelper.getInstance().getFxCollection(contragents.getDataList()));
        allSourceGroups = FXCollections.observableArrayList(getGroups(allSource));

        obtainContragentRequest = new ObtainContragentRequest();
        obtainContragentRequest.setActive(true);
        obtainContragentRequest.getUserGuids().addAll(getUserGuids(selectedUsers));
        obtainContragentRequest.setContragentTypes(Collections.singletonList(ContragentType.BUYER));
        contragents = contragentsClient.getContragents(obtainContragentRequest);
        allRecipient.addAll(ContragentHelper.getInstance().getFxCollection(contragents.getDataList()));
        allRecipientGroups = FXCollections.observableArrayList(getGroups(allRecipient));

        chooseStatus.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/icons/open.png"))));
        chooseSource.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/icons/open.png"))));
        chooseRecipient.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/icons/open.png"))));
        chooseEmployee.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/icons/open.png"))));

        resetStatus.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/icons/reset.png"))));
        resetSource.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/icons/reset.png"))));
        resetRecipient.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/icons/reset.png"))));
        resetEmployee.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/icons/reset.png"))));

        refreshButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/icons/refresh.png"))));

        LocalDate now = LocalDate.now();

        from.setValue(LocalDate.of(now.getYear(), now.getMonth(), now.getDayOfMonth() - 3));
        to.setValue(LocalDate.of(now.getYear(), now.getMonth(), now.getDayOfMonth() + 3));

        TableColumn<T, String> number = new TableColumn("Номер");
        number.setMinWidth(100);
        number.setCellValueFactory(
                new PropertyValueFactory("number"));

        TableColumn<T, String> dateDocument = new TableColumn("Дата документа");
        dateDocument.setMinWidth(100);
        dateDocument.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<T, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<T, String> param) {
                DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
                return new ReadOnlyObjectWrapper<>(df.format(param.getValue().getDateDocument()));
            }
        });

        TableColumn<T, String> dateCreate = new TableColumn("Дата создания");
        dateCreate.setMinWidth(100);
        dateCreate.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<T, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<T, String> param) {
                DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm");
                return new ReadOnlyObjectWrapper<>(df.format(param.getValue().getDateCreate()));
            }
        });

        TableColumn<T, String> sourceColumn = new TableColumn("Источник");
        sourceColumn.setMinWidth(100);
        sourceColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<T, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<T, String> param) {
                return new ReadOnlyObjectWrapper<>(param.getValue().getSource().getName());
            }
        });
        TableColumn<T, String> recipientColumn = new TableColumn("Получатель");
        recipientColumn.setMinWidth(100);
        recipientColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<T, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<T, String> param) {
                return new ReadOnlyObjectWrapper<>(param.getValue().getRecipient().getName());
            }
        });

        TableColumn<T, String> countRows = new TableColumn("Количество строк");
        countRows.setMinWidth(100);
        countRows.setCellValueFactory(
                new PropertyValueFactory("rowCount"));

        TableColumn<T, String> sum = new TableColumn("Сумма");
        sum.setMinWidth(100);
        sum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<T, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<T, String> param) {
                return new ReadOnlyObjectWrapper<>(formatter.format(param.getValue().getSum()));
            }
        });

        TableColumn<T, E> statusColumn = new TableColumn("Статус");
        statusColumn.setMinWidth(100);
        statusColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<T, E>, ObservableValue<E>>() {
            @Override
            public ObservableValue<E> call(TableColumn.CellDataFeatures<T, E> param) {
                return new ReadOnlyObjectWrapper(param.getValue().getDocumentStatus().getTitle());
            }
        });
        TableColumn<T, String> user = new TableColumn("Пользователь");
        user.setMinWidth(100);

        user.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<T, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<T, String> param) {
                for (DelegatingUser delegatingUser : allUsers) {
                    if (delegatingUser.getUserGuid().equals(param.getValue().getUserGuid())) {
                        return new ReadOnlyObjectWrapper<>(delegatingUser.getUserName());
                    }
                }
                return null;
            }
        });
        body.getColumns().addAll(number, dateDocument, sourceColumn, recipientColumn, statusColumn, countRows, sum, user, dateCreate);

        body.setRowFactory(tv -> {

            TableRow<T> row = new TableRow<T>();

            row.setOnMouseClicked(event -> {

                if (event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY && (!row.isEmpty())) {
                    T rowData = row.getItem();
                    selector.openDocument(rowData.getGuid());
                }
            });
            return row;
        });

        from.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                refresh();
            }
        });
        to.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                refresh();
            }
        });
        createMenu();
    }

    private void createMenu(){
        ContextMenu cm = new ContextMenu();
        MenuItem del = new MenuItem("Удалить");
        del.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                T selectedItem = body.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    documents.remove(selectedItem);
                    selector.dropDocument(selectedItem);
                }
            }
        });
        cm.setOnShown(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                T selectedItem = body.getSelectionModel().getSelectedItem();
                del.setVisible(selector.isDroppable(selectedItem));
            }
        });
        cm.getItems().add(del);
        body.setContextMenu(cm);
    }
    @FXML
    private void resetStatusList(){
        statuses.clear();
        documentStatus.clear();
        refresh();
    }
    @FXML
    private void resetSourceList(){
        sources.clear();
        source.clear();
        refresh();
    }
    @FXML
    private void resetRecipientList(){
        recipients.clear();
        recipient.clear();
        refresh();
    }
    @FXML
    private void resetEmployeeList(){
        selectedUsers.clear();
        employees.clear();
        DelegatingUser delegatingUser=new DelegatingUser();
        delegatingUser.setUserName(UserContextHolder.getPermissionContextDataThreadLocal().getUser().getShortName());
        delegatingUser.setUserGuid(UserContextHolder.getPermissionContextDataThreadLocal().getUser().getGuid());
        selectedUsers.add(delegatingUser);
        employees.setText(delegatingUser.getUserName());
        refresh();
    }
    @FXML
    private void refresh() {
        ObtainDocumentsRequest request = new ObtainDocumentsRequest();
        request.setRecipients(recipients);
        request.setSources(sources);
        request.setUserGuids(getUserGuids(selectedUsers));
        request.getStatuses().addAll(statuses);

        request.setEnd(Date.from(Instant.from(to.getValue().atStartOfDay(ZoneId.systemDefault()))));
        request.setStart(Date.from(Instant.from(from.getValue().atStartOfDay(ZoneId.systemDefault()))));
        documents.clear();
        documents.addAll(selector.getDocuments(request));
        documents.sort(new Comparator<T>() {
            @Override
            public int compare(T o1, T o2) {
                return o1.getDateDocument().compareTo(o2.getDateDocument());
            }
        });
        FilteredList<T> filteredList = new FilteredList<T>(documents);
        SortedList<T> sortedList = new SortedList<T>(filteredList);
        sortedList.comparatorProperty().bind(body.comparatorProperty());
        body.setItems(sortedList);
        double summa=0;
        for (T t:documents){
            summa+=t.getSum();
        }
        totalSum.setText(formatter.format(summa));
    }

    public void setDocumentSelector(DocumentSelector<T, E> selector) {
        this.selector = selector;
        root.setId(selector.getId());
        setButtonActions(selector);
        refresh();
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
                    documentStatus.setText(stringBuilder.toString());
                    refresh();
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
                    refresh();
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

    private void selectContragents(ContragentType contragentType) {
        List<ContragentFx> contragents = null;
        List<ContragentGroupFx> groups = null;
        List<ContragentFx> selected = null;
        String nameTree = null;
        String title = null;
        TextField textField = null;
        if (contragentType == ContragentType.SOURCE) {
            contragents = allSource;
            groups = allSourceGroups;
            selected = sources;
            nameTree = "Источник(и)";
            textField = source;
            title = "Выберите источник(и)";

        } else if (contragentType == ContragentType.BUYER) {
            contragents = allRecipient;
            groups = allRecipientGroups;
            selected = recipients;
            nameTree = "Получатель(ли)";
            textField = recipient;
            title = "Выберите получателя(ей)";
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION, null);
        DialogPane dialogPane = new DialogPane();
        Robot robot =
                Application.GetApplication().createRobot();
        int x = robot.getMouseX();
        int y = robot.getMouseY();

        MultiSelectTree<ContragentGroupFx, ContragentFx> tree = new MultiSelectTree<>(groups, contragents, nameTree, true);
        alert.setX(x - tree.getPrefWidth() / 2);
        alert.setY(y - tree.getPrefHeight() / 4);
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
            refresh();
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
