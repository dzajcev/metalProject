package ru.metal.auth.impl.facade;

import ru.common.api.dto.Error;
import ru.common.api.dto.Pair;
import ru.metal.api.auth.AuthorizationFacade;
import ru.metal.api.auth.ErrorCodeEnum;
import ru.metal.api.auth.dto.KeyPair;
import ru.metal.api.auth.dto.SessionDto;
import ru.metal.api.auth.dto.User;
import ru.metal.api.auth.dto.UserUpdateResult;
import ru.metal.api.auth.exceptions.FieldValidationException;
import ru.metal.api.auth.request.AuthorizationRequest;
import ru.metal.api.auth.request.ChangePasswordRequest;
import ru.metal.api.auth.request.ObtainUserRequest;
import ru.metal.api.auth.request.UpdateUserRequest;
import ru.metal.api.auth.response.AuthorizationResponse;
import ru.metal.api.auth.response.ChangePasswordResponse;
import ru.metal.api.auth.response.ObtainUserResponse;
import ru.metal.api.auth.response.UpdateUserResponse;
import ru.metal.auth.impl.domain.persistent.Session;
import ru.metal.auth.impl.domain.persistent.Session_;
import ru.metal.auth.impl.domain.persistent.UserData;
import ru.metal.auth.impl.domain.persistent.UserData_;
import ru.metal.convert.mapper.Mapper;
import ru.metal.crypto.service.KeyGenerator;
import ru.metal.email.Email;
import ru.metal.email.EmailSender;
import ru.metal.security.ejb.PermissionContextData;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.*;
import javax.persistence.criteria.*;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by User on 11.09.2017.
 */
@Remote
@Stateless(name = "authorizationFacade")
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class AuthorizationFacadeImpl implements AuthorizationFacade {
    //1 час=3600 секунд*1000 миллисекунд
    private final long sessionLifeTime = 3600 * 1000;
    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private Mapper mapper;

    @Inject
    private Validator validator;

    @Inject
    private KeyGenerator keyGenerator;

    @Inject
    private EmailSender emailSender;

    @Override
    public ObtainUserResponse obtainUser(ObtainUserRequest obtainUserRequest) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<UserData> cq = cb.createQuery(UserData.class);
        Root<UserData> root = cq.from(UserData.class);
        List<Predicate> predicates = new ArrayList<>();
        if (obtainUserRequest.getGuid() != null) {
            cq.where(cb.equal(root.get(UserData_.guid), obtainUserRequest.getGuid()));
        } else {
            if (obtainUserRequest.getEmail() != null) {
                predicates.add(cb.equal(root.get(UserData_.email), obtainUserRequest.getEmail()));
            }
            if (obtainUserRequest.getLogin() != null) {
                predicates.add(cb.equal(root.get(UserData_.login), obtainUserRequest.getLogin()));
            }
            if (obtainUserRequest.isActive()) {
                predicates.add(cb.equal(root.get(UserData_.active), obtainUserRequest.isActive()));
            }
            if (!predicates.isEmpty()) {
                cq.where(cb.or(predicates.toArray(new Predicate[0]))).distinct(true);
            }
        }
        TypedQuery<UserData> q = entityManager.createQuery(cq);
        List<UserData> results = q.getResultList();
        List<User> users = mapper.mapCollections(results, User.class);
        ObtainUserResponse obtainUserResponse = new ObtainUserResponse();
        obtainUserResponse.setDataList(users);
        return obtainUserResponse;
    }

    private void deactivateSessions(String userGuid) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Session> cq = cb.createQuery(Session.class);
        Root<Session> root = cq.from(Session.class);
        Join<Session, UserData> join = root.join(Session_.user);
        cq.where(cb.and(cb.notEqual(root.get(Session_.closed), true),
                cb.equal(join.get(UserData_.guid), userGuid)));
        TypedQuery<Session> q = entityManager.createQuery(cq);
        List<Session> results = q.getResultList();
        if (!results.isEmpty()) {
            for (Session s : results) {
                s.setClosed(true);
                entityManager.merge(s);
            }
        }
    }

    @Override
    public SessionDto getSession(String sessionGuid) {
        Session session = entityManager.find(Session.class, sessionGuid);
        if (session == null) {
            return null;
        } else {
            if (new Date().getTime() > session.getLastAction().getTime() + sessionLifeTime) {
                session.setClosed(true);
            } else {
                session.setLastAction(new Date());
            }
            Session merge = entityManager.merge(session);
            SessionDto map = mapper.map(merge, SessionDto.class);
            return map;
        }
    }

    @Override
    public ChangePasswordResponse changePassword(ChangePasswordRequest request) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<UserData> cq = cb.createQuery(UserData.class);
        Root<UserData> root = cq.from(UserData.class);
        cq.where(cb.equal(root.get(UserData_.token), request.getToken()));
        TypedQuery<UserData> q = entityManager.createQuery(cq);
        ChangePasswordResponse changePasswordResponse = new ChangePasswordResponse();
        try {
            UserData userData = q.getSingleResult();
            if (!userData.getToChangePassword()) {
                changePasswordResponse.getErrors().add(new Error(ErrorCodeEnum.AUTH002));
            } else {
                userData.setToken(request.getNewToken());
                userData.setToChangePassword(false);
            }
        } catch (NoResultException e) {
            changePasswordResponse.getErrors().add(new Error(ErrorCodeEnum.AUTH001));
        }
        return changePasswordResponse;
    }

    @Override
    public UpdateUserResponse updateUser(UpdateUserRequest updateUserRequest) {
        UpdateUserResponse updateUserResponse = new UpdateUserResponse();
        UserData map = mapper.map(updateUserRequest.getUser(), UserData.class);

        map.setToChangePassword(updateUserRequest.isToChangePassword());

        ObtainUserRequest obtainUserRequest=new ObtainUserRequest();
        obtainUserRequest.setLogin(map.getLogin());
        obtainUserRequest.setEmail(map.getEmail());
        ObtainUserResponse obtainUserResponse = obtainUser(obtainUserRequest);
        if (!obtainUserResponse.getDataList().isEmpty()) {
            boolean loginExist = false;
            boolean emailExist = false;
            for (User user : obtainUserResponse.getDataList()) {
                if (!user.getGuid().equals(map.getGuid())) {
                    if (user.getEmail().equals(map.getEmail())) {
                        emailExist = true;
                    }
                    if (user.getLogin().equals(map.getLogin())) {
                        loginExist = true;
                    }
                }
            }
            if (emailExist) {
                Error error = new Error(ErrorCodeEnum.REGISTRATION004);
                updateUserResponse.getErrors().add(error);
            }
            if (loginExist) {
                Error error = new Error(ErrorCodeEnum.REGISTRATION003);
                updateUserResponse.getErrors().add(error);
            }
            return updateUserResponse;
        }


        if (updateUserRequest.isNeedGenerateKeys()) {
            Pair<PublicKey, PrivateKey> publicKeyPrivateKeyPair = regenerateKeys(map);
            map.setPublicUserKey(publicKeyPrivateKeyPair.getLeft().getEncoded());
            map.setPrivateServerKey(publicKeyPrivateKeyPair.getRight().getEncoded());
        } else {
            UserData userData = entityManager.find(UserData.class, map.getGuid());
            map.setPublicUserKey(userData.getPublicUserKey());
            map.setPrivateServerKey(userData.getPrivateServerKey());
            entityManager.detach(userData);
        }
        UserData merge = entityManager.merge(map);
        UserUpdateResult userUpdateResult = new UserUpdateResult();
        userUpdateResult.setGuid(merge.getGuid());
        userUpdateResult.setTransportGuid(updateUserRequest.getUser().getTransportGuid());
        updateUserResponse.getImportResults().add(userUpdateResult);
        return updateUserResponse;
    }

    private Pair<PublicKey, PrivateKey> regenerateKeys(UserData userData) {
        Pair<PublicKey, PrivateKey> userKeys = keyGenerator.generate();
        Pair<PublicKey, PrivateKey> serverKeys = keyGenerator.generate();
        userData.setPrivateServerKey(serverKeys.getRight().getEncoded());
        userData.setPublicUserKey(userKeys.getLeft().getEncoded());

        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(
                serverKeys.getLeft().getEncoded());

        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(
                userKeys.getRight().getEncoded());

        Email email = new Email();
        email.getRecipients().add(userData.getEmail());
        email.getAttachments().put("public.key", publicKeySpec.getEncoded());
        email.getAttachments().put("private.key", privateKeySpec.getEncoded());
        email.setTheme("Ключи доступа");
        email.setMessage("Сохраните новые ключи. Он будет использоваться для доступа к программе");
        emailSender.send(email);

        Pair<PublicKey, PrivateKey> result = new Pair<>(userKeys.getLeft(), serverKeys.getRight());
        return result;
    }

    private String createSession(User user) {
        deactivateSessions(user.getGuid());
        Session session = new Session();
        session.setClosed(false);
        session.setStartSession(new Date());
        session.setLastAction(new Date());
        session.setUser(mapper.map(user, UserData.class));
        return entityManager.merge(session).getGuid();
    }

    @Override
    public AuthorizationResponse authorization(AuthorizationRequest authorizationRequest) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<UserData> cq = cb.createQuery(UserData.class);
        Root<UserData> root = cq.from(UserData.class);
        cq.where(cb.and(cb.equal(root.get(UserData_.active), true)),
                cb.equal(root.get(UserData_.token), authorizationRequest.getToken())).distinct(true);
        TypedQuery<UserData> q = entityManager.createQuery(cq);
        AuthorizationResponse authorizationResponse = new AuthorizationResponse();
        try {
            UserData result = q.getSingleResult();
            PermissionContextData permissionContextData = mapper.map(result, PermissionContextData.class);
            permissionContextData.setSessionGuid(createSession(mapper.map(result, User.class)));
            permissionContextData.setToken(authorizationRequest.getToken());
            authorizationResponse.setPermissionContextData(permissionContextData);
        } catch (NoResultException e) {
        }
        return authorizationResponse;
    }


    @Override
    public KeyPair getKeyPair(String userGuid) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        Root<UserData> root = cq.from(UserData.class);
        cq.where(cb.equal(root.get(UserData_.guid), userGuid));
        cq.multiselect(root.get(UserData_.privateServerKey), root.get(UserData_.publicUserKey));
        Tuple tupleResult = entityManager.createQuery(cq).getSingleResult();
        KeyPair keyPair = new KeyPair();
        if (tupleResult.get(0) != null) {
            keyPair.setPrivateKey((byte[]) tupleResult.get(0));
        }
        if (tupleResult.get(1) != null) {
            keyPair.setPublicKey((byte[]) tupleResult.get(1));
        }

        return keyPair;
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
