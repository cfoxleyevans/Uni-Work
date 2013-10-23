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
    public String itemDescription;
    public double reservePrice;
    public double startPrice;
    public Bid maxBid;

    /**
     * This is the class constructor, it takes the various bid info and returns a valid Auction object
     *
     * @param clientID        The client ID of the user that placed the auction
     * @param auctionID       The ID of the auction
     * @param itemDescription The description of the item that is up for auction
     * @param reservePrice    The reserve price of the item that is up for auction
     */
    public Auction(int auctionID, int clientID, String itemDescription, double reservePrice, double startPrice) {
        this.auctionID = auctionID;
        this.clientID = clientID;
        this.itemDescription = itemDescription;
        this.reservePrice = reservePrice;
        this.maxBid = new Bid(0, "", startPrice);
    }
}
