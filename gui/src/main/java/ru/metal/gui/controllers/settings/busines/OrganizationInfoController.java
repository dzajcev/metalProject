package ru.metal.gui.controllers.settings.busines;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import ru.common.api.dto.AdressDto;
import ru.common.api.dto.BankRequisitesDto;
import ru.metal.api.organizationinfo.dto.OrganizationInfoDto;
import ru.metal.api.organizationinfo.request.UpdateOrganizationRequest;
import ru.metal.gui.controllers.AbstractController;
import ru.metal.gui.windows.SaveButton;
import ru.metal.rest.OrganizationClient;

import java.util.List;

/**
 * Created by User on 08.08.2017.
 */
public class OrganizationInfoController extends AbstractController {
    @FXML
    private TextField organizationName;
    @FXML
    private TextField director;
    @FXML
    private TextField accountant;
    @FXML
    private TextField city;
    @FXML
    private TextField street;
    @FXML
    private TextField house;
    @FXML
    private TextField building;
    @FXML
    private TextField office;
    @FXML
    private TextField phone;
    @FXML
    private TextField bankName;
    @FXML
    private TextField inn;
    @FXML
    private TextField kpp;
    @FXML
    private TextField bik;
    @FXML
    private TextField recipientName;
    @FXML
    private TextField account1;
    @FXML
    private TextField account2;

    @FXML
    private SaveButton save;

    private OrganizationClient organizationClient;

    private OrganizationInfoDto organizationInfo;

    @FXML
    private void initialize() {
        organizationClient = new OrganizationClient();

        List<OrganizationInfoDto> dataList = organizationClient.getOrganizationInfo().getDataList();
        if (!dataList.isEmpty()) {
            organizationInfo = dataList.get(0);
        }

        if (organizationInfo != null) {
            organizationName.setText(organizationInfo.getOrganizationName());
            director.setText(organizationInfo.getDirector());
            accountant.setText(organizationInfo.getAccountant());
            if (organizationInfo.getAdress() != null) {
                AdressDto adress = organizationInfo.getAdress();
                city.setText(adress.getCity());
                street.setText(adress.getStreet());
                house.setText(adress.getHouseNumber());
                building.setText(adress.getBuilding());
                office.setText(adress.getApartment());
                phone.setText(adress.getPhoneNumber());
            }
            if (organizationInfo.getBankRequisites() != null) {
                BankRequisitesDto bankRequisites = organizationInfo.getBankRequisites();
                bankName.setText(bankRequisites.getBankName());
                inn.setText(bankRequisites.getInn());
                kpp.setText(bankRequisites.getKpp());
                bik.setText(bankRequisites.getBik());
                recipientName.setText(bankRequisites.getRecipientName());
                account1.setText(bankRequisites.getAccount1());
                account2.setText(bankRequisites.getAccount2());
            }
        }
        registerControlsProperties(save, organizationName.textProperty(), director.textProperty(), accountant.textProperty(), city.textProperty(),
                street.textProperty(), house.textProperty(), building.textProperty(), office.textProperty(), phone.textProperty(), bankName.textProperty(),
                inn.textProperty(), kpp.textProperty(), bik.textProperty(), recipientName.textProperty(), account1.textProperty(), account2.textProperty());

    }

    @FXML
    protected void saveAction(ActionEvent event) {
        save();
    }

    @Override
    protected boolean save() {
        if (organizationInfo == null) {
            organizationInfo = new OrganizationInfoDto();
        }
        organizationInfo.setOrganizationName(organizationName.getText());
        organizationInfo.setDirector(director.getText());
        organizationInfo.setAccountant(accountant.getText());

        AdressDto adressDto;
        if (organizationInfo.getAdress() == null) {
            adressDto = new AdressDto();
        } else {
            adressDto = organizationInfo.getAdress();
        }
        adressDto.setCity(city.getText());
        adressDto.setStreet(street.getText());
        adressDto.setHouseNumber(house.getText());
        adressDto.setBuilding(building.getText());
        adressDto.setApartment(office.getText());
        adressDto.setPhoneNumber(phone.getText());
        organizationInfo.setAdress(adressDto);

        BankRequisitesDto bankRequisitesDto;
        if (organizationInfo.getBankRequisites() == null) {
            bankRequisitesDto = new BankRequisitesDto();
        } else {
            bankRequisitesDto = organizationInfo.getBankRequisites();
        }
        bankRequisitesDto.setAccount1(account1.getText());
        bankRequisitesDto.setAccount2(account2.getText());
        bankRequisitesDto.setBankName(bankName.getText());
        bankRequisitesDto.setBik(bik.getText());
        bankRequisitesDto.setInn(inn.getText());
        bankRequisitesDto.setKpp(kpp.getText());
        bankRequisitesDto.setRecipientName(recipientName.getText());

        UpdateOrganizationRequest updateOrganizationRequest = new UpdateOrganizationRequest();
        updateOrganizationRequest.getDataList().add(organizationInfo);

        organizationClient.updateOrganizationInfo(updateOrganizationRequest);

        return true;
    }
}
