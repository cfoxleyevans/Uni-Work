package com.chrisfoxleyevans.SCC311.Auction.Server.Implementations;

import java.io.Serializable;
import java.util.Random;

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
    public Bid maxBid;

    /**
     * This is the class constructor, it takes the various bid info and returns a valid Auction object
     *
     * @param clientID        The client ID of the user that placed the auction
     * @param itemDescription The description of the item that is up for auction
     * @param reservePrice    The reserve price of the item that is up for auction
     */
    public Auction(int clientID, String itemDescription, double reservePrice, double startPrice) {
        this.auctionID = 0;
        this.clientID = clientID;
        this.itemDescription = itemDescription;
        this.reservePrice = reservePrice;
        this.maxBid = new Bid(0, "", startPrice);
    }
}
