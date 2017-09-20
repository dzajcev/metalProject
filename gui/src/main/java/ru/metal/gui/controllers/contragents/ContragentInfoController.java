package ru.metal.gui.controllers.contragents;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.StringConverter;
import ru.common.api.response.AbstractResponse;
import ru.metal.api.contragents.dto.ContragentType;
import ru.metal.api.contragents.dto.DocumentType;
import ru.metal.api.contragents.dto.PersonType;
import ru.metal.api.contragents.request.UpdateContragentRequest;
import ru.metal.api.contragents.response.UpdateContragentResponse;
import ru.metal.dto.ContragentFx;
import ru.metal.dto.DocumentFx;
import ru.metal.dto.EmployeeFx;
import ru.metal.dto.EntrepreneurFx;
import ru.metal.gui.StartPage;
import ru.metal.gui.controllers.AbstractController;
import ru.metal.gui.controls.tableviewcontrol.TableViewChangeProperty;
import ru.metal.gui.controls.tableviewcontrol.ValidatedTableView;
import ru.metal.gui.windows.SaveButton;
import ru.metal.gui.windows.Window;
import ru.metal.rest.ContragentsClient;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Created by User on 17.08.2017.
 */
public class ContragentInfoController extends AbstractController {
    private ContragentFx contragentFx;

    @FXML
    private SaveButton save;

    @FXML
    TextField name;

    @FXML
    private CheckBox active;

    @FXML
    private Label group;

    @FXML
    private ComboBox<ContragentFx> shipper;

    @FXML
    private Button selectShipper;

    @FXML
    private Button fillByInn;

    @FXML
    private TextField alias;

    @FXML
    private TextField inn;

    @FXML
    private TextField kpp;

    @FXML
    private TextArea address;

    @FXML
    private TextField account;

    @FXML
    private TextField bik;

    @FXML
    private TextField bank;

    @FXML
    private TextField korrAccount;

    @FXML
    private TextArea comment;

    @FXML
    private TextField phone;

    @FXML
    private TextField email;

    @FXML
    private TextField ogrn;

    @FXML
    private TextField okpo;

    @FXML
    private TextField okved;

    @FXML
    private Label ogrnLabel;

    @FXML
    private RadioButton ul;

    @FXML
    private RadioButton ip;

    @FXML
    private TextField sSerie;
    @FXML
    private TextField sNumber;
    @FXML
    private TextField sVidan;
    @FXML
    private DatePicker sDate;

    @FXML
    private TextField pSerie;
    @FXML
    private TextField pNumber;
    @FXML
    private TextField pVidan;
    @FXML
    private DatePicker pDate;

    @FXML
    private CheckBox buyer;

    @FXML
    private CheckBox source;

    @FXML
    private CheckBox driver;

    @FXML
    private ComboBox<EmployeeFx> director;
    @FXML
    private ComboBox<EmployeeFx> accountant;

    @FXML
    private ValidatedTableView<EmployeeFx> employees;
    private ObservableList<EmployeeFx> employeesSource = FXCollections.observableArrayList();
    private FilteredList<EmployeeFx> employeeFilteredList = new FilteredList<>(employeesSource);
    private TableViewChangeProperty<EmployeeFx> employeeDtoTableViewChangeProperty;

    @FXML
    private ValidatedTableView<DocumentFx> documents;
    private ObservableList<DocumentFx> docSource = FXCollections.observableArrayList();
    private FilteredList<DocumentFx> docFilteredList = new FilteredList<>(docSource);
    private TableViewChangeProperty<DocumentFx> documentTableViewChangeProperty;

    @FXML
    private AnchorPane flAttribute;

    @FXML
    private CheckBox empActive;

    @FXML
    private CheckBox docActive;

    private ToggleGroup toggleGroup = new ToggleGroup();

    private ContragentsClient contragentsClient;


    @FXML
    private void initialize() {
        contragentsClient = new ContragentsClient();
        name.setPromptText("Не более 60 символов");
        alias.setPromptText("Не более 60 символов");
        inn.setPromptText("10 или 12 цифр");
        address.setPromptText("Не более 150 символов");
        comment.setPromptText("Не более 150 символов");
        account.setPromptText("20 цифр");
        bik.setPromptText("9 цифр");
        bank.setPromptText("Не более 100 символов");
        korrAccount.setPromptText("20 цифр");
        email.setPromptText("example@example.ex");
        kpp.setPromptText("9 цифр");
        ogrn.setPromptText("13 или 15 цифр");
        okpo.setPromptText("8 или 10 цифр");
        okved.setPromptText("2, 4 или 6 цифр");

        name.setOnKeyTyped(clearHandler);

        inn.setOnKeyTyped(clearHandler);
        address.setOnKeyTyped(clearHandler);
        account.setOnKeyTyped(clearHandler);
        bik.setOnKeyTyped(clearHandler);
        bank.setOnKeyTyped(clearHandler);
        korrAccount.setOnKeyTyped(clearHandler);
        kpp.setOnKeyTyped(clearHandler);
        sSerie.setOnKeyTyped(clearHandler);
        sNumber.setOnKeyTyped(clearHandler);
        sVidan.setOnKeyTyped(clearHandler);
        sDate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ((DatePicker) event.getSource()).setStyle(null);
            }
        });
        pSerie.setOnKeyTyped(clearHandler);
        pNumber.setOnKeyTyped(clearHandler);
        pVidan.setOnKeyTyped(clearHandler);
        pDate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ((DatePicker) event.getSource()).setStyle(null);
            }
        });
        toggleGroup.getToggles().addAll(ul, ip);
        toggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if (newValue == ul) {
                    kpp.getParent().setVisible(true);
                    flAttribute.setVisible(false);
                    ogrnLabel.setText("ОГРН");
                } else if (newValue == ip) {
                    kpp.getParent().setVisible(false);
                    flAttribute.setVisible(true);
                    ogrnLabel.setText("ОГРНИП");
                }
            }
        });
        toggleGroup.selectToggle(ul);

        StringConverter<EmployeeFx> stringConverter = new StringConverter<EmployeeFx>() {
            @Override
            public String toString(EmployeeFx object) {
                StringBuilder stringBuilder = new StringBuilder(object.getSecondName()).append(" ");
                if (object.getFirstName() != null) {
                    stringBuilder.append(object.getFirstName().substring(0, 1) + ".").append(" ");
                }
                if (object.getMiddleName() != null) {
                    stringBuilder.append(object.getMiddleName().substring(0, 1) + ".");
                }
                return stringBuilder.toString();
            }

            @Override
            public EmployeeFx fromString(String string) {
                return null;
            }
        };
        director.setConverter(stringConverter);
        accountant.setConverter(stringConverter);

        initEmployees();
        empActive.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                employeeFilteredList.setPredicate(new Predicate<EmployeeFx>() {
                    @Override
                    public boolean test(EmployeeFx employeeFx) {
                        return newValue ? employeeFx.isActive() : true;
                    }
                });
            }
        });
        docActive.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                docFilteredList.setPredicate(new Predicate<DocumentFx>() {
                    @Override
                    public boolean test(DocumentFx documentFx) {
                        return newValue ? documentFx.isActive() : true;
                    }
                });
            }
        });
        empActive.setSelected(true);
        docActive.setSelected(true);
        ObservableList<ContragentFx> shipperSource = FXCollections.observableArrayList();
        ContragentFx itself = new ContragentFx();
        itself.setGuid("-1");
        itself.setName("Он же");

        shipperSource.add(itself);
        shipper.setItems(shipperSource);
        shipper.setConverter(new StringConverter<ContragentFx>() {
            @Override
            public String toString(ContragentFx object) {
                return object.getName();
            }

            @Override
            public ContragentFx fromString(String string) {
                return null;
            }
        });
        selectShipper.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                Window<ShipperController, Node> window = StartPage.openContent("/fxml/Shipper.fxml", getPrimaryStage());
                ShipperController controller = window.getController();
                ContragentFx clone = contragentFx.clone();
                clone.setName(name.getText());
                clone.setInn(inn.getText());
                clone.setAddress(address.getText());
                clone.setAccount(account.getText());
                clone.setBik(bik.getText());
                clone.setBank(bank.getText());
                clone.setKorrAccount(korrAccount.getText());
                clone.setPhone(phone.getText());
                clone.setOkpo(okpo.getText());
                clone.setKpp(kpp.getText());
                controller.initShipper(clone);
                controller.savedObjectProperty().addListener(new ChangeListener<ContragentFx>() {
                    @Override
                    public void changed(ObservableValue<? extends ContragentFx> observable, ContragentFx oldValue, ContragentFx newValue) {
                        if (newValue != null) {
                            if (shipper.getItems().size() == 1) {
                                shipper.getItems().add(newValue);
                            }
                            shipper.getSelectionModel().select(newValue);
                            shipper.setValue(newValue);
                            contragentFx.setShipper(newValue);
                        }
                    }
                });
                window.setModal(true);
                window.setTitle("Грузополучатель");
                window.setMaximizable(false);
                window.setClosable(true);
                StartPage.addWindow(window);
            }
        });
    }

    private void initDocuments() {
        Callback<TableColumn<DocumentFx, String>, TableCell<DocumentFx, String>> textCellFactory = documents.getTextCellFactory(String.class);
        documentTableViewChangeProperty = new TableViewChangeProperty<>(documents, docSource);
        this.documents.setEditable(true);
        TableColumn<DocumentFx, DocumentType> documentType = new TableColumn("Тип");
        documentType.setMinWidth(100);
        documentType.setCellValueFactory(
                new PropertyValueFactory<DocumentFx, DocumentType>("documentType"));

        documentType.setCellFactory(documents.getComboBoxCell(DocumentType.class, DocumentType.values()));


        TableColumn<DocumentFx, String> serie = new TableColumn("Серия*");
        serie.setMinWidth(50);
        serie.setCellValueFactory(
                new PropertyValueFactory<DocumentFx, String>("serie"));

        serie.setCellFactory(textCellFactory);

        TableColumn number = new TableColumn("Номер*");
        number.setMinWidth(50);
        number.setCellValueFactory(
                new PropertyValueFactory<DocumentFx, String>("number"));
        number.setCellFactory(textCellFactory);

        TableColumn vidan = new TableColumn("Кем выдан*");
        vidan.setMinWidth(100);
        vidan.setCellValueFactory(
                new PropertyValueFactory<DocumentFx, String>("vidan"));
        vidan.setCellFactory(textCellFactory);

        TableColumn date = new TableColumn("Когда выдан*");
        date.setMinWidth(100);
        date.setCellValueFactory(
                new PropertyValueFactory<DocumentFx, String>("date"));

        date.setCellFactory(documents.getDateCellFactory());

        TableColumn active = new TableColumn("Активен");
        active.setMinWidth(100);
        active.setCellValueFactory(
                new PropertyValueFactory<DocumentFx, Boolean>("active"));
        active.setCellFactory(CheckBoxTableCell.forTableColumn(active));

        documents.getColumns().addAll(documentType, serie, number, vidan, date, active);


        ContextMenu cm = new ContextMenu();
        MenuItem add = new MenuItem("Добавить");
        add.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DocumentFx documentFx = new DocumentFx();
                if (employees.getSelectionModel().getSelectedItem() == null) {
                    return;
                }
                employees.getSelectionModel().getSelectedItem().getDocuments().addAll(documentFx);
                documents.setToValidate(false);
                docSource.add(documentFx);
                documents.getSelectionModel().select(documentFx);

                documents.edit(docSource.indexOf(documentFx), documents.getColumns().get(1));
            }
        });
        MenuItem del = new MenuItem("Удалить");
        del.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DocumentFx selectedItem = documents.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    docSource.remove(selectedItem);
                    employees.getSelectionModel().getSelectedItem().getDocuments().remove(selectedItem);

                }
            }
        });
        cm.setOnShown(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                DocumentFx selectedItem = documents.getSelectionModel().getSelectedItem();
                if (selectedItem != null && selectedItem.getGuid() == null) {
                    del.setVisible(true);
                } else {
                    del.setVisible(false);
                }
            }
        });
        cm.getItems().addAll(add, del);
        documents.setContextMenu(cm);
        SortedList<DocumentFx> sortedList = new SortedList<DocumentFx>(docFilteredList);
        sortedList.comparatorProperty().bind(this.documents.comparatorProperty());
        //todo: ебучий случай, схуяли листенер не видит пустой список?
        DocumentFx pzdc = new DocumentFx();
        docSource.add(pzdc);
        this.documents.setItems(sortedList);
        docSource.remove(pzdc);

    }

    private void initEmployees() {
        Callback<TableColumn<EmployeeFx, String>, TableCell<EmployeeFx, String>> textCellFactory = employees.getTextCellFactory(String.class);
        employeeDtoTableViewChangeProperty = new TableViewChangeProperty<>(employees, employeesSource);
        employees.setEditable(true);

        TableColumn<EmployeeFx, String> secondName = new TableColumn("Фамилия*");
        secondName.setMinWidth(100);
        secondName.setCellValueFactory(
                new PropertyValueFactory<EmployeeFx, String>("secondName"));
        secondName.setCellFactory(textCellFactory);

        TableColumn<EmployeeFx, String> firstName = new TableColumn("Имя*");
        firstName.setMinWidth(100);
        firstName.setCellValueFactory(
                new PropertyValueFactory<EmployeeFx, String>("firstName"));
        firstName.setCellFactory(textCellFactory);

        TableColumn<EmployeeFx, String> middleName = new TableColumn("Отчество");
        middleName.setMinWidth(100);
        middleName.setCellValueFactory(
                new PropertyValueFactory<EmployeeFx, String>("middleName"));
        middleName.setCellFactory(textCellFactory);

        TableColumn<EmployeeFx, String> position = new TableColumn("Должность*");
        position.setMinWidth(100);
        position.setCellValueFactory(
                new PropertyValueFactory<EmployeeFx, String>("position"));
        position.setCellFactory(textCellFactory);

        TableColumn active = new TableColumn("Активен");
        active.setMinWidth(100);
        active.setCellValueFactory(
                new PropertyValueFactory<EmployeeFx, Boolean>("active"));
        active.setCellFactory(CheckBoxTableCell.forTableColumn(active));

        employees.getColumns().addAll(secondName, firstName, middleName, position, active);
        ContextMenu cm = new ContextMenu();
        MenuItem add = new MenuItem("Добавить");
        add.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                EmployeeFx employee = new EmployeeFx();
                employees.setToValidate(false);
                employeesSource.addAll(employee);
                employees.getSelectionModel().select(employee);
                employees.edit(employeesSource.indexOf(employee), employees.getColumns().get(0));
            }
        });
        MenuItem del = new MenuItem("Удалить");
        del.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                EmployeeFx selectedItem = employees.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {

                    employeesSource.remove(selectedItem);
                }
            }
        });
        cm.setOnShown(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                EmployeeFx selectedItem = employees.getSelectionModel().getSelectedItem();
                if (selectedItem != null && selectedItem.getGuid() == null) {
                    del.setVisible(true);
                } else {
                    del.setVisible(false);
                }
            }
        });
        cm.getItems().addAll(add, del);
        employees.setContextMenu(cm);
        employees.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        employees.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<EmployeeFx>() {
            @Override
            public void changed(ObservableValue<? extends EmployeeFx> observable, EmployeeFx oldValue, EmployeeFx newValue) {
                if (newValue != null) {
                    docSource.clear();
                    ObservableList<DocumentFx> docs = newValue.getDocuments();
                    docSource.addAll(docs);
                }
            }
        });
    }

    @FXML
    protected void saveAction(ActionEvent event) {
        saveResult(true);
    }

    private BooleanProperty saved = new SimpleBooleanProperty(false);

    @Override
    protected UpdateContragentResponse save() {
        UpdateContragentRequest request = new UpdateContragentRequest();

        contragentFx.setActive(active.isSelected());
        contragentFx.setName(name.getText());
        contragentFx.setAccount(account.getText());
        contragentFx.setAddress(address.getText());
        contragentFx.setAlias(alias.getText());
        contragentFx.setBank(bank.getText());
        contragentFx.setBik(bik.getText());
        contragentFx.setComment(comment.getText());
        contragentFx.setComment(comment.getText());
        contragentFx.getContragentTypes().clear();
        if (buyer.isSelected()) {
            contragentFx.getContragentTypes().add(ContragentType.BUYER);
        }
        if (source.isSelected()) {
            contragentFx.getContragentTypes().add(ContragentType.SOURCE);
        }
        if (driver.isSelected()) {
            contragentFx.getContragentTypes().add(ContragentType.DRIVER);
        }
        contragentFx.setInn(inn.getText());
        contragentFx.setKorrAccount(korrAccount.getText());
        contragentFx.setShipper(shipper.getSelectionModel().getSelectedItem() == null || shipper.getSelectionModel().getSelectedItem().getGuid().equals("-1")
                ? null : shipper.getSelectionModel().getSelectedItem());
        if (toggleGroup.getSelectedToggle() == ul) {
            contragentFx.setPersonType(PersonType.UL);
            contragentFx.setKpp(kpp.getText());
            contragentFx.setEntrepreneur(null);
        } else if (toggleGroup.getSelectedToggle() == ip) {
            contragentFx.setPersonType(PersonType.IP);
            contragentFx.setKpp(null);
            EntrepreneurFx entrepreneur = contragentFx.getEntrepreneur();
            if (entrepreneur == null) {
                entrepreneur = new EntrepreneurFx();
            }
            DocumentFx certificate = null;
            DocumentFx passport = null;
            for (DocumentFx document : entrepreneur.getDocuments()) {
                if (document.getDocumentType() == DocumentType.PASSPORT) {
                    passport = document;
                }
                if (document.getDocumentType() == DocumentType.CERTIFICATE) {
                    certificate = document;
                }
            }
            if (certificate == null) {
                certificate = new DocumentFx();
                certificate.setDocumentType(DocumentType.CERTIFICATE);
                entrepreneur.getDocuments().add(certificate);
            }
            certificate.setSerie(sSerie.getText());
            certificate.setNumber(sNumber.getText());
            certificate.setVidan(sVidan.getText());
            LocalDate localDateSertificate = sDate.getValue();
            if (localDateSertificate != null) {
                Instant instantCertificate = Instant.from(localDateSertificate.atStartOfDay(ZoneId.systemDefault()));
                Date dateCertificate = Date.from(instantCertificate);
                certificate.setDate(dateCertificate);
            }
            if (passport == null) {
                passport = new DocumentFx();
                passport.setDocumentType(DocumentType.PASSPORT);
                entrepreneur.getDocuments().add(passport);
            }
            passport.setSerie(pSerie.getText());
            passport.setNumber(pNumber.getText());
            passport.setVidan(pVidan.getText());
            LocalDate localDatePassport = pDate.getValue();
            if (localDatePassport != null) {
                Instant instantPassport = Instant.from(localDatePassport.atStartOfDay(ZoneId.systemDefault()));
                Date datePassport = Date.from(instantPassport);
                passport.setDate(datePassport);
            }
            contragentFx.setEntrepreneur(entrepreneur);
        }

        contragentFx.setPhone(phone.getText());
        contragentFx.setEmail(email.getText());
        contragentFx.setOgrn(ogrn.getText());
        contragentFx.setOkpo(okpo.getText());
        contragentFx.setOkved(okved.getText());
        if (director.getValue() != null) {
            contragentFx.setDirector(director.getValue());
        }
        if (accountant.getValue() != null) {
            contragentFx.setAccountant(accountant.getValue());
        }

        contragentFx.getEmployees().clear();
        for (EmployeeFx employee : employees.getItems()) {
            contragentFx.getEmployees().add(employee);
        }
        request.getDataList().add(contragentFx.getEntity());
        boolean error = contragentFx.hasError();
        if (error) {
            employees.setToValidate(true);
            documents.setToValidate(true);
            setError(name, "name", contragentFx);
            setError(inn, "inn", contragentFx);
            setError(address, "address", contragentFx);
            setError(account, "account", contragentFx);
            setError(bik, "bik", contragentFx);
            setError(bank, "bank", contragentFx);
            setError(korrAccount, "korrAccount", contragentFx);

            if (toggleGroup.getSelectedToggle() == ul) {
                setError(kpp, "kpp", contragentFx);
            } else if (toggleGroup.getSelectedToggle() == ip) {
                ObservableList<DocumentFx> documents = contragentFx.getEntrepreneur().getDocuments();
                for (DocumentFx documentFx : documents) {
                    if (documentFx.getDocumentType() == DocumentType.CERTIFICATE) {
                        setError(sSerie, "serie", contragentFx, documentFx.getTransportGuid());
                        setError(sNumber, "number", contragentFx, documentFx.getTransportGuid());
                        setError(sVidan, "vidan", contragentFx, documentFx.getTransportGuid());
                        setError(sDate, "date", contragentFx, documentFx.getTransportGuid());
                    } else if (documentFx.getDocumentType() == DocumentType.PASSPORT) {
                        setError(pSerie, "serie", contragentFx, documentFx.getTransportGuid());
                        setError(pNumber, "number", contragentFx, documentFx.getTransportGuid());
                        setError(pVidan, "vidan", contragentFx, documentFx.getTransportGuid());
                        setError(pDate, "date", contragentFx, documentFx.getTransportGuid());
                    }
                }
                if (contragentFx.getContragentTypes().isEmpty()) {
                    buyer.setStyle("-fx-background-color: lightcoral");
                    source.setStyle("-fx-background-color: lightcoral");
                    driver.setStyle("-fx-background-color: lightcoral");
                }
            }
            employees.checkError();
            documents.checkError();
            return null;
        }

        UpdateContragentResponse updateContragentResponse = contragentsClient.updateContragents(request);

        saved.setValue(true);
        setCloseRequest(true);
        return updateContragentResponse;
    }


    public boolean isSaved() {
        return saved.get();
    }

    public BooleanProperty savedProperty() {
        return saved;
    }

    public void setSaved(boolean saved) {
        this.saved.set(saved);
    }

    public void initContragent(ContragentFx contragent) {
        setContragent(contragent);
        registerControlsProperties(save, name.textProperty(), active.selectedProperty(), alias.textProperty(), inn.textProperty(), kpp.textProperty(),
                address.textProperty(), account.textProperty(), bik.textProperty(), bank.textProperty(), korrAccount.textProperty(), comment.textProperty(),
                phone.textProperty(), email.textProperty(), okpo.textProperty(), okved.textProperty(), ogrn.textProperty(), shipper.getSelectionModel().selectedItemProperty(),
                sSerie.textProperty(), sNumber.textProperty(), sVidan.textProperty(), pSerie.textProperty(), pNumber.textProperty(), pVidan.textProperty(),
                sDate.valueProperty(), pDate.valueProperty(), ul.selectedProperty(), ip.selectedProperty(), director.valueProperty(), accountant.valueProperty(),
                employeeDtoTableViewChangeProperty, documentTableViewChangeProperty);
        employeeDtoTableViewChangeProperty.actionProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    employeeFilteredList.setPredicate(new Predicate<EmployeeFx>() {
                        @Override
                        public boolean test(EmployeeFx employeeFx) {
                            return empActive.isSelected() ? employeeFx.isActive() : true;
                        }
                    });
                }
            }
        });
    }

    public void setContragent(ContragentFx contragent) {
        this.contragentFx = contragent;
        name.setText(contragent.getName());
        active.setSelected(contragent.getActive());
        group.setText(contragent.getGroup().getName());
        alias.setText(contragent.getAlias());
        inn.setText(contragent.getInn());
        driver.setSelected(contragent.getContragentTypes().contains(ContragentType.DRIVER));
        source.setSelected(contragent.getContragentTypes().contains(ContragentType.SOURCE));
        buyer.setSelected(contragent.getContragentTypes().contains(ContragentType.BUYER));
        if (contragent.getShipper() == null) {
            shipper.getSelectionModel().select(0);
            selectShipper.setText("Выбрать");
        } else {
            selectShipper.setText("Изменить");
            shipper.getItems().add(contragentFx.getShipper());
            shipper.getSelectionModel().select(contragentFx.getShipper());
        }
        if (contragent.getPersonType() == PersonType.IP) {
            toggleGroup.selectToggle(ip);
            if (contragent.getEntrepreneur() != null) {
                DocumentFx certificate = null;
                DocumentFx passport = null;
                for (DocumentFx document : contragent.getEntrepreneur().getDocuments()) {
                    if (document.getDocumentType() == DocumentType.CERTIFICATE) {
                        certificate = document;
                    }
                    if (document.getDocumentType() == DocumentType.PASSPORT) {
                        passport = document;
                    }
                }
                if (certificate != null) {
                    sSerie.setText(certificate.getSerie());
                    sNumber.setText(certificate.getNumber());
                    sVidan.setText(certificate.getVidan());
                    if (certificate.getDate() != null) {
                        sDate.setValue(certificate.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                    }
                }
                if (passport != null) {
                    pSerie.setText(passport.getSerie());
                    pNumber.setText(passport.getNumber());
                    pVidan.setText(passport.getVidan());
                    if (passport.getDate() != null) {
                        pDate.setValue(passport.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                    }
                }
            }
        } else if (contragent.getPersonType() == PersonType.UL) {
            kpp.setText(contragent.getKpp());
            toggleGroup.selectToggle(ul);
        }
        address.setText(contragent.getAddress());
        account.setText(contragent.getAccount());
        bik.setText(contragent.getBik());
        bank.setText(contragent.getBank());
        korrAccount.setText(contragent.getKorrAccount());
        comment.setText(contragent.getComment());
        phone.setText(contragent.getPhone());
        email.setText(contragent.getEmail());
        okpo.setText(contragent.getOkpo());
        ogrn.setText(contragent.getOgrn());
        okved.setText(contragent.getOkved());

        employeesSource.addAll(contragentFx.getEmployees());
        director.setItems(employeeFilteredList);
        accountant.setItems(employeeFilteredList);
        Map<String, EmployeeFx> employeeFxMap = new HashMap<>();
        for (EmployeeFx fx : contragentFx.getEmployees()) {
            employeeFxMap.put(fx.getGuid(), fx);
        }
        if (contragentFx.getDirector() != null) {
            EmployeeFx employeeFx = employeeFxMap.get(contragentFx.getDirector().getGuid());
            director.getSelectionModel().select(employeeFx);
        }
        if (contragentFx.getAccountant() != null) {
            accountant.getSelectionModel().select(employeeFxMap.get(contragentFx.getAccountant().getGuid()));
        }

        SortedList<EmployeeFx> sortedList = new SortedList<>(employeeFilteredList);
        sortedList.comparatorProperty().bind(this.employees.comparatorProperty());
        this.employees.setItems(sortedList);
        initDocuments();

    }
}
