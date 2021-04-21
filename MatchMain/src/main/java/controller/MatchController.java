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
import static java.lang.Math.log;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.UUID;
import model.Match;
import model.Ticket;

@Slf4j
@Path("/matches")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MatchController {
    @Inject
    MatchRepostitory matchRepository;

    @POST()
    @Path("/add")
    public Uni<Match> addMatchApi(Match match){
        return Uni.createFrom().publisher(addMatch(match));
    }
    private Mono<Match> addMatch(Match match){
        match.setId(UUID.randomUUID().toString());
        return matchRepository.findMatchById(match.getId())
                .flatMap(match1 -> {
                    log.info("Found");
                    return Mono.error(new Throwable("Match existed"));
                })
                .switchIfEmpty(matchRepository.addMatch(match)).then(getMatchById(match.getId()));
    }
    @GET
    @Path("/get/{matchId}")
    public Uni<Match> getMatchByIdApi(@PathParam("matchId") String matchId){
        return Uni.createFrom().publisher(getMatchById(matchId));
    }
    private Mono<Match> getMatchById(String matchId){
        return matchRepository.findMatchById(matchId);
    }
    @POST()
    @Path("/ticket/changeStatus/{matchId}/{ticketId}")
    public Uni<Match> changeTicketStatusApi(@PathParam("matchId") String matchId, @PathParam("ticketId")String ticketId){
        return Uni.createFrom().publisher(changeTicketStatus(matchId, ticketId));
    }

    private Mono<Match> changeTicketStatus(String matchId, String ticketId){
        return matchRepository.findMatchById(matchId)
                .switchIfEmpty(Mono.error(new Throwable("Could not find match id " + matchId)))
                .flatMap(match -> matchRepository.changeTicketStatus(matchId, ticketId));
    }

    @GET
    @Path("/get/available/{matchId}")
    public Multi<Ticket> getAvailableTicketApi(@PathParam("matchId") String matchId){
        return Multi.createFrom().publisher(getAvailableTicket(matchId));
    }
    private Flux<Ticket> getAvailableTicket(String matchId){
        return matchRepository.findAvailableTicket(matchId);
    }

}
