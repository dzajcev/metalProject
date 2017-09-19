package ru.metal.dto.helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.metal.api.auth.dto.RegistrationData;
import ru.metal.api.auth.dto.User;
import ru.metal.dto.RegistrationRequestDataFx;
import ru.metal.dto.UserFx;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by User on 01.09.2017.
 */
public class UserHelper implements FxHelper<UserFx,User> {
    private static volatile UserHelper instance;


    public static UserHelper getInstance() {
        UserHelper localInstance = instance;
        if (localInstance == null) {
            synchronized (UserHelper.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new UserHelper();
                }
            }
        }
        return localInstance;
    }
    private UserHelper(){}

    @Override
    public UserFx getFxEntity(User dto){
        if (dto==null){
            return null;
        }
        UserFx dataFx=new UserFx();
        dataFx.setGuid(dto.getGuid());
        dataFx.setLastEditingDate(dto.getLastEditingDate());
        dataFx.setTransportGuid(dto.getTransportGuid());

        dataFx.setEmail(dto.getEmail());
        dataFx.setFirstName(dto.getFirstName());
        dataFx.setMiddleName(dto.getMiddleName());
        dataFx.setLogin(dto.getLogin());
        dataFx.setSecondName(dto.getSecondName());
        dataFx.setToken(dto.getToken());
        dataFx.setActive(dto.isActive());
        dataFx.setRoles(FXCollections.observableArrayList(dto.getRoles()));
        dataFx.setPrivileges(FXCollections.observableArrayList(dto.getPrivileges()));
        return dataFx;
    }
    @Override
    public ObservableList<UserFx> getFxCollection(List<User> collection){
        ObservableList<UserFx> result= FXCollections.observableArrayList();
        for (User dto:collection){
            result.add(getFxEntity(dto));
        }
        return result;
    }
    @Override
    public List<User> getDtoCollection(Collection<UserFx> collection){
        List<User> result=new ArrayList<>();
        for (UserFx dto:collection){
            result.add(dto.getEntity());
        }
        return result;
    }

}
