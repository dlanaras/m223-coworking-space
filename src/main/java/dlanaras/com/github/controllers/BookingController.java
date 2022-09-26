package dlanaras.com.github.controllers;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.TransactionRequiredException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import dlanaras.com.github.exceptions.NullValueException;
import dlanaras.com.github.models.Booking;
import dlanaras.com.github.services.BookingService;

@Path("/bookings")
public class BookingController {
    @Inject
    BookingService bookingService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Booking> index() {
        return bookingService.findAll();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Booking booking) {
        try {
            Booking createdBooking = bookingService.createBooking(booking);
            return Response.ok(createdBooking).build();
        } catch (Exception e) {
            System.out.println(e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateBooking(Booking booking) {
        try {
            return Response.ok(bookingService.updateBooking(booking)).build();
        } catch (IllegalArgumentException e) {
            System.out.println(e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (TransactionRequiredException e) {
            System.out.println(e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @Path("/{id}")
    @DELETE
    public Response deleteBooking(Long id) {
        try {
            bookingService.deleteBooking(id);
            return Response.noContent().build();
        } catch (NullValueException e) {
            System.out.println(e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @Path("/review/{id}/{accept}")
    @GET
    public Response changeBookingStatus(Long id, Boolean accept) {
        Booking booking = bookingService.getBooking(id);
        booking.setIsAccepted(accept);
        bookingService.updateBooking(booking);
        return Response.ok().build();
    }

    @Path("/price/{id}")
    @GET
    public Response getNewestBookingPrice(Long id) {
        return Response.ok(bookingService.getLatestBookingPrice(id)).build();
    }

    @Path("/cancel/{id}")
    @GET
    public Response cancelBooking(Long id) {
        //TODO: get user id through claim if not admin
        Booking booking = bookingService.getBooking(id);
        booking.setIsAccepted(false);
        bookingService.updateBooking(booking);
        return Response.ok().build();
    }
}
