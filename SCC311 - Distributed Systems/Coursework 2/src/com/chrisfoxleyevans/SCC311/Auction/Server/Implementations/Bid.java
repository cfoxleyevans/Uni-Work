package com.chrisfoxleyevans.SCC311.Auction.Server.Implementations;

import java.io.Serializable;

/**
 * This class provides the servers internal representation of a bid
 *
 * @author Chris Foxley-Evans
 * @version 0.0.1
 */
public class Bid implements Serializable {

    public int auctionID;
    public String username;
    public double bidValue;

    /**
     * This is the class constructor, it takes the various bid info and returns a valid Bid object
     *
     * @param auctionID The client ID of the bidder
     * @param username The username of the bidder
     * @param bidValue The value of the bid
     */
    public Bid(int auctionID, String username, double bidValue) {
        this.auctionID = auctionID;
        this.username = username;
        this.bidValue = bidValue;
    }
}
