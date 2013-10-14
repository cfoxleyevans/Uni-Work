package com.chrisfoxleyevans.SCC311.Auction.Server.Implementations;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class provides the servers internal representation of an auction
 *
 * @author Chris Foxley-Evans
 * @version 0.0.1
 */
public class Auction implements Serializable {

    //instance vars
    public long  auctionID;
    public String itemDescription;
    public double reservePrice;
    public ArrayList<Bid> bidHistroy;
    public Bid maxBid;

    //constructor
    public Auction(long id, String itemDescription, double reservePrice) {
        this.auctionID = id;
        this.itemDescription = itemDescription;
        this.reservePrice =reservePrice;
        this.maxBid = null;
    }

    //public methods
    public void updateCurrentMaxBid(Bid newBid) {
        bidHistroy.add(newBid);
        maxBid = newBid;
    }
}
