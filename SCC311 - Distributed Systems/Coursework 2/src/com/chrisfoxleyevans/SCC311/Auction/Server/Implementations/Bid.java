package com.chrisfoxleyevans.SCC311.Auction.Server.Implementations;

import java.io.Serializable;

/**
 * This class provides the servers internal representation of a bid
 *
 * @author Chris Foxley-Evans
 * @version 0.0.1
 */
public class Bid implements Serializable {

    //instance vars
    public int auctionID;
    public int clientID;
    public String clientName;
    public double bidValue;

    //constructor
    public Bid(int auctionID, int clientID, String clientName, double bidValue) {
        this.auctionID = auctionID;
        this.clientID = clientID;
        this.clientName = clientName;
        this.bidValue = bidValue;
    }
}
