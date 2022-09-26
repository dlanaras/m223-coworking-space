package dlanaras.com.github.services;

import java.util.Arrays;
import java.util.Comparator;
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

    public List<Booking> findAll(Long userId) {
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

    public Float getLatestBookingPrice(Long userId) {
        List<Booking> userBookings = this.findAll(userId);

        Comparator<Booking> dateComparator = Comparator.comparing(Booking::getStartDate); // What the hell is this java, just use LINQ
        Arrays.sort((userBookings.stream().toArray(Booking[] ::new)), dateComparator);

        Booking newestBooking = userBookings.toArray(Booking[] ::new)[0];

        return newestBooking.getDayPrice();
    }

    public Booking getBooking(Long bookingId) {
        return entityManager.find(Booking.class, bookingId);
    }
}
