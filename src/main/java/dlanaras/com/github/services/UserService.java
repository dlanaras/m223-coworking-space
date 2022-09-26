package dlanaras.com.github.services;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.TransactionRequiredException;
import javax.transaction.Transactional;

import dlanaras.com.github.exceptions.InvalidLoginException;
import dlanaras.com.github.exceptions.NullValueException;
import dlanaras.com.github.models.User;
import dlanaras.com.github.models.dto.Login;

@ApplicationScoped
public class UserService {
    @Inject
    private EntityManager entityManager;

    public List<User> findAll() {
        var query = entityManager.createQuery("FROM Entry", User.class);
        return query.getResultList();
    }

    @Transactional
    public User createUser(User user) throws EntityExistsException, Exception {
        try {
            entityManager.persist(user);
        } catch (EntityExistsException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }

        return user;
    }

    @Transactional
    public User updateUser(User user) throws IllegalArgumentException, TransactionRequiredException {
        try {
            return entityManager.merge(user);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (TransactionRequiredException e) {
            throw e;
        }
    }

    public void deleteUser(Long id) throws NullValueException {
        var entity = entityManager.find(User.class, id);
        if (entity == null) {
            throw new NullValueException("No user with id: " + id + " was found");
        }
        entityManager.remove(entity);
    }

    public String login(Login login) throws InvalidLoginException {
        var entity = entityManager.find(User.class, login.getEmail());
        if (entity == null) {
            throw new InvalidLoginException("Email doesn't exist");
        } else if (entity.getPassword().equals(login.getPassword())) {
            throw new InvalidLoginException("Wrong Password");
        }

        //TODO: return real token
        return "THIS IS A TOKEN";
    }
    
    public String register(User user) throws EntityExistsException, Exception {
        try {
            createUser(user);
        } catch (EntityExistsException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }

        //TODO: return real token
        return "This is a token";
    }
}
