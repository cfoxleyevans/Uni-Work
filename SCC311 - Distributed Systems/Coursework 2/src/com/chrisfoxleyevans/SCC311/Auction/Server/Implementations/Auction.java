package com.chrisfoxleyevans.SCC311.Auction.Server.Implementations;

import java.io.Serializable;

public class Auction implements Serializable {

    //instance vars
    public int clientID;
    public int  auctionID;
    public String itemDescription;
    public double reservePrice;
    public Bid maxBid;

    //constructor
    public Auction(int clientID, int auctionID, String itemDescription, double reservePrice) {
        this.clientID = clientID;
        this.auctionID = auctionID;
        this.itemDescription = itemDescription;
        this.reservePrice = reservePrice;
        this.maxBid = null;
    }
}
