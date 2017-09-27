package ru.metal.dto.helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.metal.api.nomenclature.dto.GroupDto;
import ru.metal.dto.GroupFx;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by User on 01.09.2017.
 */
public class GoodGroupHelper implements FxHelper<GroupFx,GroupDto> {
    private static volatile GoodGroupHelper instance;

    public static GoodGroupHelper getInstance() {
        GoodGroupHelper localInstance = instance;
        if (localInstance == null) {
            synchronized (GoodGroupHelper.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new GoodGroupHelper();
                }
            }
        }
        return localInstance;
    }
    private GoodGroupHelper(){}

    @Override
    public GroupFx getFxEntity(GroupDto dto){
        if (dto==null){
            return null;
        }
        GroupFx groupFx=new GroupFx();
        groupFx.setActive(dto.getActive());
        groupFx.setGuid(dto.getGuid());
        groupFx.setLastEditingDate(dto.getLastEditingDate());
        groupFx.setTransportGuid(dto.getTransportGuid());

        groupFx.setGroupGuid(dto.getGroupGuid());
        groupFx.setName(dto.getName());
        groupFx.setUserGuid(dto.getUserGuid());
        return groupFx;
    }
    @Override
    public ObservableList<GroupFx> getFxCollection(List<GroupDto> collection){
        ObservableList<GroupFx> result= FXCollections.observableArrayList();
        for (GroupDto dto:collection){
            result.add(getFxEntity(dto));
        }
        return result;
    }
    @Override
    public List<GroupDto> getDtoCollection(Collection<GroupFx> collection){
        List<GroupDto> result=new ArrayList<>();
        for (GroupFx dto:collection){
            result.add(dto.getEntity());
        }
        return result;
    }

}
