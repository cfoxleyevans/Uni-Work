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
    public double bidValue;

<<<<<<< HEAD
    public Bid(int clientID, String username, double bidValue) {
=======
    //constructor
    public Bid(int auctionID, int clientID, double bidValue) {
        this.auctionID = auctionID;
>>>>>>> SCC311Security
        this.clientID = clientID;
        this.bidValue = bidValue;
    }
}
