package com.chrisfoxleyevans.SCC311.Auction.Server.Implementations;

import java.io.Serializable;

/**
 * This class provides the servers internal representation of a bid
 *
 * @author Chris Foxley-Evans
 * @version 0.0.1
 */
public class Bid implements Serializable {

    public int clientID;
    public String username;
    public double bidValue;

    public Bid(int clientID, String username, double bidValue) {
        this.clientID = clientID;
        this.username = username;
        this.bidValue = bidValue;
    }
}
