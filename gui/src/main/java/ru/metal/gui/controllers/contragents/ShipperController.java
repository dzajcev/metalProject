package ru.metal.gui.controllers.contragents;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import ru.metal.api.contragents.dto.*;
import ru.metal.api.contragents.request.UpdateContragentRequest;
import ru.metal.api.contragents.response.UpdateContragentResponse;
import ru.metal.dto.ContragentFx;
import ru.metal.exceptions.ServerErrorException;
import ru.metal.gui.controllers.AbstractController;
import ru.metal.gui.windows.SaveButton;
import ru.metal.rest.ContragentsClient;

/**
 * Created by User on 17.08.2017.
 */
public class ShipperController extends AbstractController {

    private ContragentFx contragent;
    private ContragentFx shipper;
    @FXML
    private SaveButton save;
    @FXML
    private Button cancel;
    @FXML
    private Button copyReq;

    @FXML
    TextField name;

    @FXML
    private TextField inn;

    @FXML
    private TextField kpp;

    @FXML
    private TextField address;

    @FXML
    private TextField account;

    @FXML
    private TextField bik;

    @FXML
    private TextField bank;

    @FXML
    private TextField korrAccount;

    @FXML
    private TextField phone;

    @FXML
    private TextField okpo;

    private ContragentsClient contragentsClient;

    private ObjectProperty<ContragentFx> savedObject = new SimpleObjectProperty<>();


    @FXML
    private void initialize() {
        contragentsClient = new ContragentsClient();
        final EventHandler<KeyEvent> clearHandler = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                ((TextInputControl) event.getSource()).setStyle(null);
            }
        };
        kpp.setOnKeyTyped(clearHandler);
        name.setOnKeyTyped(clearHandler);
        inn.setOnKeyTyped(clearHandler);
        address.setOnKeyTyped(clearHandler);
        account.setOnKeyTyped(clearHandler);
        bik.setOnKeyTyped(clearHandler);
        bank.setOnKeyTyped(clearHandler);
        korrAccount.setOnKeyTyped(clearHandler);
        kpp.setOnKeyTyped(clearHandler);

        cancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                setCloseRequest(true);
            }
        });

        copyReq.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                fillForm(contragent);
            }
        });
    }

    private void fillForm(ContragentFx contragent) {
        name.setText(contragent.getName());
        inn.setText(contragent.getInn());
        kpp.setText(contragent.getKpp());
        address.setText(contragent.getAddress());
        account.setText(contragent.getAccount());
        bik.setText(contragent.getBik());
        bank.setText(contragent.getBank());
        korrAccount.setText(contragent.getKorrAccount());
        phone.setText(contragent.getPhone());
        okpo.setText(contragent.getOkpo());
    }

    @FXML
    protected void saveAction(ActionEvent event) {
        saveResult(true);
    }


    @Override
    protected boolean save() {

        UpdateContragentRequest request = new UpdateContragentRequest();
        shipper.setGroup(this.contragent.getGroup());
        shipper.getContragentTypes().add(ContragentType.SHIPPER);
        shipper.setPersonType(this.contragent.getPersonType());
        shipper.setName(name.getText());
        shipper.setAccount(account.getText());
        shipper.setAddress(address.getText());
        shipper.setBank(bank.getText());
        shipper.setBik(bik.getText());
        shipper.setInn(inn.getText());
        shipper.setKorrAccount(korrAccount.getText());
        shipper.setKpp(kpp.getText());


        shipper.setPhone(phone.getText());
        shipper.setOkpo(okpo.getText());

        boolean error = shipper.hasError();

        if (error) {
            setError(name, "name", shipper);
            setError(inn, "inn", shipper);
            setError(address, "address", shipper);
            setError(account, "account", shipper);
            setError(bik, "bik", shipper);
            setError(bank, "bank", shipper);
            setError(korrAccount, "korrAccount", shipper);
            setError(kpp, "kpp", shipper);
            setError(name, "name", shipper);
            return false;

        }
        request.getDataList().add(shipper.getEntity());
        try {
            UpdateContragentResponse updateContragentResponse = contragentsClient.updateContragents(request);
            UpdateContragentResult updateContragentResult = updateContragentResponse.getImportResults().get(0);
            shipper.setGuid(updateContragentResult.getGuid());
        } catch (ServerErrorException e) {
            return false;
        }
        setSavedObject(shipper);
        setSavedObject(null);
        setCloseRequest(true);
        return true;
    }

    public ContragentFx getSavedObject() {
        return savedObject.get();
    }

    public ObjectProperty<ContragentFx> savedObjectProperty() {
        return savedObject;
    }

    public void setSavedObject(ContragentFx savedObject) {
        this.savedObject.set(savedObject);
    }

    public void initShipper(ContragentFx contragent) {
        this.contragent = contragent;
        if (contragent.getShipper() != null) {
            fillForm(contragent.getShipper());
            shipper = contragent.getShipper();
        } else {
            shipper = new ContragentFx();
        }
        registerControlsProperties(save, name.textProperty(), inn.textProperty(), kpp.textProperty(), address.textProperty(), account.textProperty(),
                bik.textProperty(), bank.textProperty(), korrAccount.textProperty(), phone.textProperty(), okpo.textProperty());

    }


}
