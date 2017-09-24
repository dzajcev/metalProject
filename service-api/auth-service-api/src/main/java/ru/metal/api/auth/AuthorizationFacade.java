package ru.metal.api.auth;

import ru.metal.api.auth.dto.KeyPair;
import ru.metal.api.auth.dto.SessionDto;
import ru.metal.api.auth.request.*;
import ru.metal.api.auth.response.*;

/**
 * Created by User on 11.09.2017.
 */
public interface AuthorizationFacade {


    ObtainUserResponse obtainUsers(ObtainUserRequest obtainUserRequest);

    UpdateUserResponse updateUser(UpdateUserRequest updateUserRequest);

    AuthorizationResponse authorization(AuthorizationRequest authorizationRequest);

    KeyPair getKeyPair(String userGuid);

    SessionDto getSession(String sessionGuid);

    ChangePasswordResponse changePassword(ChangePasswordRequest request);

}
