package dlanaras.com.github.controllers;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.persistence.TransactionRequiredException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.eclipse.microprofile.jwt.JsonWebToken;

import dlanaras.com.github.exceptions.NullValueException;
import dlanaras.com.github.models.Booking;
import dlanaras.com.github.services.BookingService;

@Path("/bookings")
public class BookingController {
    @Inject
    BookingService bookingService;

    @Inject
    JsonWebToken jwt;

    @GET
    @RolesAllowed({ "User", "Admin" })
    @Produces(MediaType.APPLICATION_JSON)
    public List<Booking> index() {
        return bookingService.findAll();
    }

    @POST
    @RolesAllowed({ "Admin" })
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
    @RolesAllowed({ "Admin" })
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
    @RolesAllowed({ "Admin" })
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
    @RolesAllowed({ "Admin" })
    @GET
    public Response changeBookingStatus(Long id, Boolean accept) {
        Booking booking = bookingService.getBooking(id);
        if (booking == null)
            return Response.status(Response.Status.BAD_REQUEST).build();

        booking.setIsAccepted(accept);
        bookingService.updateBooking(booking);
        return Response.ok().build();
    }

    @Path("/price/{id}")
    @RolesAllowed({ "User", "Admin" })
    @GET
    public Response getNewestBookingPrice(Long id, @Context SecurityContext ctx) {
        try {

            if (ctx.isUserInRole("User")) {
                id = Long.parseLong(ctx.getUserPrincipal().getName());
            }

            return Response.ok(bookingService.getLatestBookingPrice(id)).build();
        } catch (NullValueException e) {
            System.out.println(e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @Path("/cancel/{id}")
    @RolesAllowed({ "User", "Admin" })
    @GET
    public Response cancelBooking(Long id, @Context SecurityContext ctx) {
        Long userId = null;

        if (ctx.isUserInRole("User")) {
            userId = Long.parseLong(ctx.getUserPrincipal().getName());
        }

        Booking booking = bookingService.getBooking(id);

        if (userId == null ? booking == null : booking == null || (booking.getUser()).getId() != userId)
            return Response.status(Response.Status.BAD_REQUEST).build();

        booking.setIsAccepted(false);

        try {
            bookingService.updateBooking(booking);
            return Response.ok().build();
        } catch (TransactionRequiredException e) {
            System.out.println(e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (IllegalArgumentException e) {
            System.out.println(e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
}
