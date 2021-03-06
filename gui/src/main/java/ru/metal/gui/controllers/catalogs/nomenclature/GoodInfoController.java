package ru.metal.gui.controllers.catalogs.nomenclature;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import ru.metal.api.nomenclature.request.ObtainOkeiRequest;
import ru.metal.api.nomenclature.request.UpdateGoodsRequest;
import ru.metal.api.nomenclature.response.ObtainOkeiResponse;
import ru.metal.api.nomenclature.response.UpdateGoodsResponse;
import ru.metal.dto.GoodFx;
import ru.metal.dto.OkeiFx;
import ru.metal.dto.helper.OkeiHelper;
import ru.metal.gui.controllers.AbstractController;
import ru.metal.gui.windows.SaveButton;
import ru.metal.rest.NomenclatureClient;

/**
 * Created by User on 17.08.2017.
 */
public class GoodInfoController extends AbstractController<UpdateGoodsResponse> {
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

        ObtainOkeiResponse okeiResponse = nomenclatureClient.getOkei(new ObtainOkeiRequest());
        ObservableList<OkeiFx> fxCollection = OkeiHelper.getInstance().getFxCollection(okeiResponse.getDataList());
        okei.setItems(fxCollection);

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
    protected UpdateGoodsResponse save() {

        goodFx.setOkei(okei.getValue());
        goodFx.setActive(active.isSelected());
        goodFx.setName(name.getText());
        if (nds.getText() != null && !nds.getText().isEmpty()) {
            try {
                goodFx.setNds(Integer.parseInt(nds.getText()));
            } catch (NumberFormatException e) {
                goodFx.setNds(null);
            }
        }

        boolean error = goodFx.hasError();
        if (error) {
            setError(name, "name", goodFx);
            setError(okei, "okei", goodFx);
            setError(nds, "nds", goodFx);
            return null;
        }
        UpdateGoodsRequest updateGoodsRequest = new UpdateGoodsRequest();

        updateGoodsRequest.getDataList().add(goodFx.getEntity());

        UpdateGoodsResponse updateGoodsResponse = nomenclatureClient.updateGoods(updateGoodsRequest);
        goodFx.setGuid(updateGoodsResponse.getImportResults().get(0).getGuid());
        saved.setValue(true);
        setCloseRequest(true);
        return updateGoodsResponse;
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
        if (goodFx.getNds() != null) {
            nds.setText(Integer.toString(goodFx.getNds()));
        }

        registerControlsProperties(save, name.textProperty(), active.selectedProperty(),
                okei.getSelectionModel().selectedItemProperty(), nds.textProperty());
    }

    public GoodFx getGood(){
        return goodFx;
    }
}
