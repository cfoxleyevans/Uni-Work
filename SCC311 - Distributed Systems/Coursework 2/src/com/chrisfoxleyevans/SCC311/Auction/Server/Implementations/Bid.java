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
    public String username;
    public double bidValue;

    //constructor
    public Bid(int auctionID, String username, double bidValue) {
        this.auctionID = auctionID;
        this.username = username;
        this.bidValue = bidValue;
    }
}
