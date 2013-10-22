package com.chrisfoxleyevans.SCC311.Auction.Client.StateManager;

import com.chrisfoxleyevans.SCC311.Auction.Server.Implementations.Bid;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class represents that state of the client at a given time this can then be written to file
 * when changes are made.
 *
 * @author Chris Foxley-Evans
 * @version 0.0.1
 */
public class ClientState implements Serializable{

    public int clientID;
    public String username;
    public ArrayList<Bid> currentBids;

    /**
     * This is the class constructor, it returns a new valid state object
     */
    public ClientState(String username) {
        this.clientID = 0;
        this.username = username;
        this.currentBids = new ArrayList<Bid>();
    }
}
