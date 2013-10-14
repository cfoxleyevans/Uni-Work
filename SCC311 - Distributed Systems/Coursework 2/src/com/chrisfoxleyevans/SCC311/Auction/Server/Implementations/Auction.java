package com.chrisfoxleyevans.SCC311.Auction.Server.Implementations;

/**
 * This class provides the servers internal representation of an auction
 *
 * @author Chris Foxley-Evans
 * @version 0.0.1
 */
public class Auction {

    //instance vars
    public long  auctionID;
    public String itemDescription;
    public double reservePrice;
    public Bid maxBid;

    //constructor
    public Auction(String itemDescription, double reservePrice) {
        this.auctionID = 0;
        this.itemDescription = itemDescription;
        this.reservePrice =reservePrice;
        this.maxBid = null;
    }

    //public methods
    public void updateCurrentMaxBid(Bid newBid) {
        maxBid = newBid;
    }
}
