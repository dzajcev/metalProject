package ru.metal.auth.impl.facade;

import ru.lanit.hcs.convert.mapper.Mapper;
import ru.metal.api.auth.AuthorizationFacade;
import ru.metal.api.auth.dto.KeyPair;
import ru.metal.api.auth.dto.SessionDto;
import ru.metal.api.auth.dto.User;
import ru.metal.api.auth.exceptions.FieldValidationException;
import ru.metal.api.auth.request.AdminExistRequest;
import ru.metal.api.auth.request.AuthorizationRequest;
import ru.metal.api.auth.request.ObtainUserRequest;
import ru.metal.api.auth.request.UpdateUserRequest;
import ru.metal.api.auth.response.AdminExistResponse;
import ru.metal.api.auth.response.AuthorizationResponse;
import ru.metal.api.auth.response.ObtainUserResponse;
import ru.metal.api.auth.response.UpdateUserResponse;
import ru.metal.auth.impl.domain.persistent.Session;
import ru.metal.auth.impl.domain.persistent.Session_;
import ru.metal.auth.impl.domain.persistent.UserData;
import ru.metal.auth.impl.domain.persistent.UserData_;
import ru.metal.crypto.ejb.PermissionContextData;
import ru.metal.crypto.ejb.UserContextHolder;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.*;
import javax.persistence.criteria.*;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
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
    private final long sessionLifeTime = 3600*1000 ;
    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private Mapper mapper;

    @Inject
    private Validator validator;


    @Override
    public AdminExistResponse adminExist(AdminExistRequest adminExistRequest) {
        return null;
    }

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
            cq.where(cb.or(predicates.toArray(new Predicate[0]))).distinct(true);
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
            return mapper.map(entityManager.merge(session), SessionDto.class);
        }
    }

    @Override
    public UpdateUserResponse updateUser(UpdateUserRequest updateUserRequest) {
        return null;
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
        cq.where(cb.equal(root.get(UserData_.token), authorizationRequest.getToken())).distinct(true);
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
