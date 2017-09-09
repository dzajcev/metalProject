package ru.metal.gui.controllers.nomenclature;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;
import ru.metal.api.nomenclature.dto.GoodDto;
import ru.metal.api.nomenclature.dto.OkeiDto;
import ru.metal.api.nomenclature.request.ObtainOkeiRequest;
import ru.metal.api.nomenclature.request.UpdateGoodsRequest;
import ru.metal.api.nomenclature.response.ObtainOkeiResponse;
import ru.metal.dto.GoodFx;
import ru.metal.dto.OkeiFx;
import ru.metal.dto.helper.GoodHelper;
import ru.metal.dto.helper.OkeiHelper;
import ru.metal.exceptions.ServerErrorException;
import ru.metal.gui.controllers.AbstractController;
import ru.metal.gui.windows.SaveButton;
import ru.metal.rest.NomenclatureClient;

import java.util.List;

/**
 * Created by User on 17.08.2017.
 */
public class GoodInfoController extends AbstractController {
    private GoodFx goodFx;

    @FXML
    private SaveButton save;

    @FXML
    TextField name;

    @FXML
    private CheckBox active;
    @FXML
    private TextField nds;

    @FXML
    private Label group;

    @FXML
    private ComboBox<OkeiFx> okei;
    NomenclatureClient nomenclatureClient;

    @FXML
    private void initialize() {
        nomenclatureClient = new NomenclatureClient();
        okei.setConverter(new StringConverter<OkeiFx>() {
            @Override
            public String toString(OkeiFx object) {
                return object.getName();
            }

            @Override
            public OkeiFx fromString(String string) {
                return null;
            }
        });
        try {
            ObtainOkeiResponse okeiResponse = nomenclatureClient.getOkei(new ObtainOkeiRequest());
            ObservableList<OkeiFx> fxCollection = OkeiHelper.getInstance().getFxCollection(okeiResponse.getDataList());
            okei.setItems(fxCollection);
        } catch (ServerErrorException e) {
        }
        okei.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<OkeiFx>() {
            @Override
            public void changed(ObservableValue<? extends OkeiFx> observable, OkeiFx oldValue, OkeiFx newValue) {
                if (newValue != null) {
                    goodFx.setOkei(newValue);
                    okei.setStyle(null);
                }
            }
        });
        name.setOnKeyTyped(clearHandler);
        nds.setOnKeyTyped(clearHandler);
    }

    @FXML
    protected void saveAction(ActionEvent event) {
        saveResult(true);
    }

    private BooleanProperty saved = new SimpleBooleanProperty(false);

    @Override
    protected boolean save() {

        goodFx.setOkei(okei.getValue());
        goodFx.setActive(active.isSelected());
        goodFx.setName(name.getText());
        if (nds.getText()!=null && !nds.getText().isEmpty()) {
            try {
                goodFx.setNds(Integer.parseInt(nds.getText()));
            }catch (NumberFormatException e){
                goodFx.setNds(null);
            }
        }

        boolean error = goodFx.hasError();
        if (error){
            setError(name, "name", goodFx);
            setError(okei, "okei", goodFx);
            setError(nds, "nds", goodFx);
            return false;
        }
        UpdateGoodsRequest updateGoodsRequest = new UpdateGoodsRequest();

        updateGoodsRequest.getDataList().add(goodFx.getEntity());
        try {
            nomenclatureClient.updateGoods(updateGoodsRequest);
        } catch (ServerErrorException e) {
            e.printStackTrace();
        }
        saved.setValue(true);
        setCloseRequest(true);
        return true;
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

    public void setGood(GoodFx good) {
        this.goodFx = good;
        name.setText(good.getName());
        active.setSelected(good.getActive());
        group.setText(good.getGroup().getName());
        okei.getSelectionModel().select(goodFx.getOkei());
        if (goodFx.getNds()!=null) {
            nds.setText(Integer.toString(goodFx.getNds()));
        }

        registerControlsProperties(save, name.textProperty(), active.selectedProperty(),
                okei.getSelectionModel().selectedItemProperty(), nds.textProperty());
    }
}
