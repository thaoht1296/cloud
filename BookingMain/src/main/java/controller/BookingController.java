/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

/**
 *
 * @author DELL
 */

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.UUID;
import model.Booking;
import model.Match;

@Path("/bookings")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookingController {
    @Inject
    BookingRepository bookingRepository;

    @POST()
    @Path("/add")
    public Uni<Void> addBookingApi(Booking Booking){
        return Uni.createFrom().publisher(addBooking(Booking));
    }
    private Mono<Void> addBooking(Booking booking){
        return bookingRepository.addBooking(booking)
                    .flatMap(booking1 -> setStatusBookedTicket(booking1));
    }

    private Mono<Void> setStatusBookedTicket(Booking booking){
        WebClient client = WebClient.create("http://localhost:8092");
        return client.post()
                .uri("/matches/ticket/changeStatus/" + booking.getMatchId() + "/" + booking.getTicketId())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body(Mono.just(booking), Booking.class)
                .retrieve()
                .bodyToMono(Match.class)
                .then();
    }
}
