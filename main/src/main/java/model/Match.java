/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.List;


/**
 *
 * @author DELL
 */
public class Match {
    private String id;
    private int numberOfTicket;
    private List<Ticket> ticketList;

    public Match(String id, int numberOfTicket, List<Ticket> ticketList) {
        this.id = id;
        this.numberOfTicket = numberOfTicket;
        this.ticketList = ticketList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getNumberOfTicket() {
        return numberOfTicket;
    }

    public void setNumberOfTicket(int numberOfTicket) {
        this.numberOfTicket = numberOfTicket;
    }

    public List<Ticket> getTicketList() {
        return ticketList;
    }

    public void setTicketList(List<Ticket> ticketList) {
        this.ticketList = ticketList;
    }
    
    
}
