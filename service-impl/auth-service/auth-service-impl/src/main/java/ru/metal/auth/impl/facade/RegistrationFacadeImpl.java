package ru.metal.auth.impl.facade;

import ru.common.api.dto.Error;
import ru.common.api.dto.Pair;
import ru.metal.api.auth.AuthorizationFacade;
import ru.metal.api.auth.ErrorCodeEnum;
import ru.metal.api.auth.RegistrationFacade;
import ru.metal.api.auth.dto.RegistrationData;
import ru.metal.api.auth.dto.User;
import ru.metal.api.auth.exceptions.FieldValidationException;
import ru.metal.api.auth.request.AcceptRegistrationRequest;
import ru.metal.api.auth.request.ObtainUserRequest;
import ru.metal.api.auth.request.RegistrationRequest;
import ru.metal.api.auth.response.AcceptRegistrationResponse;
import ru.metal.api.auth.response.ObtainRegistrationRequestsResponse;
import ru.metal.api.auth.response.ObtainUserResponse;
import ru.metal.api.auth.response.RegistrationResponse;
import ru.metal.auth.impl.domain.persistent.RegistrationRequestData;
import ru.metal.auth.impl.domain.persistent.RegistrationRequestData_;
import ru.metal.auth.impl.domain.persistent.UserData;
import ru.metal.auth.impl.domain.persistent.UserData_;
import ru.metal.convert.mapper.Mapper;
import ru.metal.security.ejb.dto.Privilege;
import ru.metal.security.ejb.dto.Role;
import ru.metal.email.Email;
import ru.metal.email.EmailSender;

import javax.ejb.*;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
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
    private EmailSender emailSender;

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

        boolean firstRun = firstRun();
        RegistrationRequestData registrationRequestData = mapper.map(registrationRequest.getRegistrationData(), RegistrationRequestData.class);
        registrationRequestData.setAccepted(false);
        RegistrationRequestData merge = entityManager.merge(registrationRequestData);
        AcceptRegistrationRequest request = new AcceptRegistrationRequest();
        request.setRegistrationRequestGuid(merge.getGuid());
        request.getRoles().add(Role.ADMIN);


        if (firstRun) {
            AcceptRegistrationResponse acceptRegistrationResponse = acceptRegistration(request, true);
            response.setPublicServerKey(acceptRegistrationResponse.getPublicServerKey());
        }
        return response;
    }

    private Pair<UserData, PublicKey> createUser(RegistrationRequestData registrationRequestData, List<Role> roles, List<Privilege> privileges) {
        UserData userData = new UserData();
        userData.setActive(true);
        userData.setEmail(registrationRequestData.getEmail());
        userData.setFirstName(registrationRequestData.getFirstName());
        userData.setSecondName(registrationRequestData.getSecondName());
        userData.setLogin(registrationRequestData.getLogin());
        userData.setMiddleName(registrationRequestData.getMiddleName());
        userData.setToken(registrationRequestData.getToken());
        userData.setPublicUserKey(registrationRequestData.getPublicUserKey());
        userData.setPrivileges(privileges);
        userData.setRoles(roles);
        PublicKey publicKey;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            userData.setPrivateServerKey(privateKey.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        UserData merge = entityManager.merge(userData);

        Pair<UserData, PublicKey> pair = new Pair<>(merge, publicKey);
        return pair;
    }

    @Override
    public AcceptRegistrationResponse acceptRegistration(AcceptRegistrationRequest acceptRegistrationRequest, boolean fistRun) {
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

        Pair<UserData, PublicKey> user = createUser(registrationRequestData, acceptRegistrationRequest.getRoles(),acceptRegistrationRequest.getPrivileges());
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(
                user.getRight().getEncoded());
        registrationRequestData.setAccepted(true);
        entityManager.merge(registrationRequestData);
        if (fistRun) {
            acceptRegistrationResponse.setPublicServerKey(x509EncodedKeySpec.getEncoded());
        } else {
            byte[] encoded = x509EncodedKeySpec.getEncoded();
            Email email = new Email();
            email.getRecipients().add(user.getLeft().getEmail());
            email.getAttachments().put("public.key", encoded);
            email.setTheme("Ключ доступа");
            email.setMessage("Сохраните полученный ключ. Он будет использоваться для доступа к программе");
            emailSender.send(email);
        }

        return acceptRegistrationResponse;
    }

    @Override
    public ObtainRegistrationRequestsResponse getRegistrationRequests() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<RegistrationRequestData> cq = cb.createQuery(RegistrationRequestData.class);
        Root<RegistrationRequestData> root = cq.from(RegistrationRequestData.class);
        cq.where(cb.equal(root.get(RegistrationRequestData_.accepted), false));

        TypedQuery<RegistrationRequestData> q = entityManager.createQuery(cq);
        List<RegistrationRequestData> results = q.getResultList();
        List<RegistrationData> registrationRequests = mapper.mapCollections(results, RegistrationData.class);
        ObtainRegistrationRequestsResponse obtainRegistrationRequestsResponse = new ObtainRegistrationRequestsResponse();
        obtainRegistrationRequestsResponse.setDataList(registrationRequests);
        return obtainRegistrationRequestsResponse;
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

    private boolean firstRun() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        cq.select(cb.count(cq.from(UserData.class)));
        return entityManager.createQuery(cq).getSingleResult() == 0;
    }
}
