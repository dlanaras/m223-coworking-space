package dlanaras.com.github.services;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import dlanaras.com.github.models.Booking;
import dlanaras.com.github.models.User;

@ApplicationScoped
public class BookingService {
    @Inject
    private EntityManager entityManager;

    public List<Booking> findAll() {
        var query = entityManager.createQuery("FROM Booking", Booking.class);
        return query.getResultList();
    }

    public List<Booking> findAll(int userId) {
        var query = entityManager.createQuery("SELECT b FROM Booking b JOIN User u ON b.user=u WHERE u.id= :userId");
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    @Transactional
    public Booking createBooking(Booking booking) {
        entityManager.persist(booking);
        return booking;
    }

    @Transactional
    public Booking updateBooking(Booking booking) {
        return entityManager.merge(booking);
    }

    public void deleteBooking(Long id) {
        var entity = entityManager.find(Booking.class, id);
        entityManager.remove(entity);
    }
}
