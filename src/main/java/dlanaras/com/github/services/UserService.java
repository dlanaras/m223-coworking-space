package dlanaras.com.github.services;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.TransactionRequiredException;
import javax.transaction.Transactional;

import dlanaras.com.github.exceptions.InvalidLoginException;
import dlanaras.com.github.exceptions.InvalidValueException;
import dlanaras.com.github.exceptions.NullValueException;
import dlanaras.com.github.models.User;
import dlanaras.com.github.models.dto.Login;

@ApplicationScoped
public class UserService {
    @Inject
    private EntityManager entityManager;

    @Inject
    private TokenService tokenService;

    public List<User> findAll() {
        var query = entityManager.createQuery("FROM User", User.class);
        return query.getResultList();
    }

    @Transactional
    public User createUser(User user) throws EntityExistsException, InvalidValueException, Exception {
        entityManager.persist(user);
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

    @Transactional
    public void deleteUser(Long id) throws NullValueException {
        var entity = entityManager.find(User.class, id);
        if (entity == null) {
            throw new NullValueException("No user with id: " + id + " was found");
        }
        entityManager.remove(entity);
    }

    public String login(Login login) throws InvalidLoginException {

        var query = entityManager.createQuery("SELECT u FROM User u Where u.email = :email");
        query.setParameter("email", login.getEmail());
        User entity = (User) query.getSingleResult();

        if (entity == null) {
            throw new InvalidLoginException("Email doesn't exist");
        } else if (entity.getPassword() == login.getPassword()) {
            throw new InvalidLoginException("Wrong Password: " + entity.getPassword() + " vs " + login.getPassword());
        }
        // We do a little french
        String leTokenSécurisé = tokenService.createToken(entity);
        return leTokenSécurisé;
    }

    public String register(User user) throws EntityExistsException, InvalidValueException, Exception {

            List<User> users = this.findAll();

            if (users.isEmpty()) {
                user.setAdmin(true);
            } else {
                user.setAdmin(false);
            }

            createUser(user);

        String leTokenSécurisé = tokenService.createToken(user);
        return leTokenSécurisé;
    }
}
