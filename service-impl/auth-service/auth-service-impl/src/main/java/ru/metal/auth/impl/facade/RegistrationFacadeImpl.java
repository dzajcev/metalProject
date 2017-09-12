package ru.metal.auth.impl.facade;

import ru.common.api.dto.Error;
import ru.lanit.hcs.convert.mapper.Mapper;
import ru.metal.api.auth.AuthorizationFacade;
import ru.metal.api.auth.ErrorCodeEnum;
import ru.metal.api.auth.RegistrationFacade;
import ru.metal.api.auth.dto.User;
import ru.metal.api.auth.exceptions.FieldValidationException;
import ru.metal.api.auth.request.AcceptRegistrationRequest;
import ru.metal.api.auth.request.ObtainUserRequest;
import ru.metal.api.auth.request.RegistrationRequest;
import ru.metal.api.auth.response.AcceptRegistrationResponse;
import ru.metal.api.auth.response.ObtainUserResponse;
import ru.metal.api.auth.response.RegistrationResponse;
import ru.metal.auth.impl.domain.persistent.Position;
import ru.metal.auth.impl.domain.persistent.RegistrationRequestData;
import ru.metal.auth.impl.domain.persistent.UserData;
import ru.metal.crypto.ejb.dto.Privilege;
import ru.metal.crypto.ejb.dto.Role;

import javax.ejb.*;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by User on 11.09.2017.
 */
@Remote
@Stateless(name = "registrationFacade")
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class RegistrationFacadeImpl implements RegistrationFacade {
    @PersistenceContext
    private EntityManager entityManager;

    @EJB
    private AuthorizationFacade authorizationFacade;

    @Inject
    private Mapper mapper;

    @Inject
    private Validator validator;

    @Override
    public RegistrationResponse createRegistration(RegistrationRequest registrationRequest) {
        RegistrationResponse response = new RegistrationResponse();

        if (registrationRequest.getRegistrationData() == null) {
            Error error = new Error(ErrorCodeEnum.REGISTRATION001);
            response.getErrors().add(error);
            return response;
        }
        try {
            validateRequest(registrationRequest.getRegistrationData());
        } catch (FieldValidationException e) {
            Error error = new Error(ErrorCodeEnum.REGISTRATION002, Arrays.toString(e.getFieldNames().toArray()));
            response.getErrors().add(error);
            return response;
        }

        ObtainUserRequest obtainUserRequest = new ObtainUserRequest();
        obtainUserRequest.setEmail(registrationRequest.getRegistrationData().getEmail());
        obtainUserRequest.setLogin(registrationRequest.getRegistrationData().getLogin());
        ObtainUserResponse obtainUserResponse = authorizationFacade.obtainUser(obtainUserRequest);
        if (!obtainUserResponse.getDataList().isEmpty()) {
            boolean loginExist = false;
            boolean emailExist = false;
            for (User user : obtainUserResponse.getDataList()) {
                if (user.getEmail().equals(registrationRequest.getRegistrationData().getEmail())) {
                    emailExist = true;
                }
                if (user.getLogin().equals(registrationRequest.getRegistrationData().getLogin())) {
                    loginExist = true;
                }
            }
            if (emailExist) {
                Error error = new Error(ErrorCodeEnum.REGISTRATION004);
                response.getErrors().add(error);
            }
            if (loginExist) {
                Error error = new Error(ErrorCodeEnum.REGISTRATION003);
                response.getErrors().add(error);
            }
            return response;
        }

        RegistrationRequestData registrationRequestData = mapper.map(registrationRequest.getRegistrationData(), RegistrationRequestData.class);
        registrationRequestData.setAccepted(false);
        RegistrationRequestData merge = entityManager.merge(registrationRequestData);
        AcceptRegistrationRequest request=new AcceptRegistrationRequest();
        request.setRegistrationRequestGuid(merge.getGuid());
        AcceptRegistrationResponse acceptRegistrationResponse = acceptRegistration(request);

        return response;
    }

    @Override
    public AcceptRegistrationResponse acceptRegistration(AcceptRegistrationRequest acceptRegistrationRequest) {
        AcceptRegistrationResponse acceptRegistrationResponse = new AcceptRegistrationResponse();
        try {
            validateRequest(acceptRegistrationRequest);
        } catch (FieldValidationException e) {
            Error error = new Error(ErrorCodeEnum.REGISTRATION006, Arrays.toString(e.getFieldNames().toArray()));
            acceptRegistrationResponse.getErrors().add(error);
            return acceptRegistrationResponse;
        }
        RegistrationRequestData registrationRequestData = entityManager.find(RegistrationRequestData.class, acceptRegistrationRequest.getRegistrationRequestGuid());

        if (registrationRequestData == null) {
            Error error = new Error(ErrorCodeEnum.REGISTRATION005, acceptRegistrationRequest.getRegistrationRequestGuid());
            acceptRegistrationResponse.getErrors().add(error);
            return acceptRegistrationResponse;
        }

        UserData userData = new UserData();
        userData.setEmail(registrationRequestData.getEmail());
        userData.setFirstName(registrationRequestData.getFirstName());
        userData.setSecondName(registrationRequestData.getSecondName());
        userData.setLogin(registrationRequestData.getLogin());
        userData.setMiddleName(registrationRequestData.getMiddleName());
        Position pos = new Position();
        pos.setGuid("f6084674-a0cb-4e72-8d1f-2142675e344e");
        userData.setPosition(pos);
        userData.getPrivileges().add(Privilege.WRITE_ORDERS);
        userData.getRoles().add(Role.ADMIN);
        userData.setToken(registrationRequestData.getToken());
        userData.setPublicUserKey(registrationRequestData.getPublicUserKey());
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            userData.setPrivateServerKey(privateKey.getEncoded());
            //todo:send to user public key
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(
                    publicKey.getEncoded());
            FileOutputStream fos = new FileOutputStream("D:\\public.key");
            fos.write(x509EncodedKeySpec.getEncoded());
            fos.close();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        entityManager.persist(userData);

        return null;
    }


    private <R> void validateRequest(R request) throws FieldValidationException {
        Set<ConstraintViolation<R>> errors = validator.validate(request);
        if (errors != null && !errors.isEmpty()) {
            List<String> fieldNames = new ArrayList<>(errors.size());
            for (ConstraintViolation constraintViolation : errors) {
                fieldNames.add(constraintViolation.getPropertyPath().toString());
            }
            throw new FieldValidationException(fieldNames);
        }
    }
}
