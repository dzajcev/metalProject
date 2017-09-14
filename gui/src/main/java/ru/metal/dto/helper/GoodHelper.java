package ru.metal.dto.helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.metal.api.nomenclature.dto.GoodDto;
import ru.metal.dto.GoodFx;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by User on 01.09.2017.
 */
public class GoodHelper implements FxHelper<GoodFx,GoodDto> {
    private static volatile GoodHelper instance;

    public static GoodHelper getInstance() {
        GoodHelper localInstance = instance;
        if (localInstance == null) {
            synchronized (GoodHelper.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new GoodHelper();
                }
            }
        }
        return localInstance;
    }
    private GoodHelper(){}

    @Override
    public GoodFx getFxEntity(GoodDto dto){
        if (dto==null){
            return null;
        }
        GoodFx goodFx=new GoodFx();
        goodFx.setActive(dto.getActive());
        goodFx.setGuid(dto.getGuid());
        goodFx.setLastEditingDate(dto.getLastEditingDate());
        goodFx.setTransportGuid(dto.getTransportGuid());

        goodFx.setActive(dto.getActive());
        goodFx.setGroup(GoodGroupHelper.getInstance().getFxEntity(dto.getGroup()));
        goodFx.setName(dto.getName());
        goodFx.setNds(dto.getNds());
        goodFx.setOkei(OkeiHelper.getInstance().getFxEntity(dto.getOkei()));
        return goodFx;
    }
    @Override
    public ObservableList<GoodFx> getFxCollection(List<GoodDto> collection){
        ObservableList<GoodFx> result= FXCollections.observableArrayList();
        for (GoodDto dto:collection){
            result.add(getFxEntity(dto));
        }
        return result;
    }
    @Override
    public List<GoodDto> getDtoCollection(Collection<GoodFx> collection){
        List<GoodDto> result=new ArrayList<>();
        for (GoodFx dto:collection){
            result.add(dto.getEntity());
        }
        return result;
    }

}
