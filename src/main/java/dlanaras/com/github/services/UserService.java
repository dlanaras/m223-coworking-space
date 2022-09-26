package dlanaras.com.github.services;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import dlanaras.com.github.exceptions.InvalidLoginException;
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
    public User createUser(User user) {
        entityManager.persist(user);
        return user;
    }

    @Transactional
    public User updateUser(User user) {
        return entityManager.merge(user);
    }

    public void deleteUser(Long id) {
        var entity = entityManager.find(User.class, id);
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
    
    public String register(User user) {
        createUser(user);

        //TODO: return real token
        return "This is a token";
    }
}
