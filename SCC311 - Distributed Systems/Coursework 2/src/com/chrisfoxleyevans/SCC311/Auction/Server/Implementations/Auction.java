package com.chrisfoxleyevans.SCC311.Auction.Server.Implementations;

import java.io.Serializable;

/**
 * This class provides the server internal representation of an auction
 *
 * @author Chris Foxley-Evans
 * @version 0.0.1
 */
public class Auction implements Serializable {

    public int auctionID;
    public int clientID;
    public String username;
    public boolean active;
    public String itemDescription;
    public double reservePrice;
    public Bid maxBid;

    /**
     * This is the class constructor, it takes the various bid info and returns a valid Auction object
     *
     * @param clientID        The client ID of the user that placed the auction
     * @param auctionID       The ID of the auction
     * @param itemDescription The description of the item that is up for auction
     * @param reservePrice    The reserve price of the item that is up for auction
     */
    public Auction(int auctionID, int clientID, String username, String itemDescription, double reservePrice) {
        this.auctionID = auctionID;
        this.clientID = clientID;
        this.username = username;
        this.active = true;
        this.itemDescription = itemDescription;
        this.reservePrice = reservePrice;
        this.maxBid = new Bid(0, "", 0);
    }
}
