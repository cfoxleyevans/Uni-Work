package com.chrisfoxleyevans.SCC311.Auction.Server.StateManager;

import com.chrisfoxleyevans.SCC311.Auction.Server.Implementations.Auction;
import com.chrisfoxleyevans.SCC311.Auction.Server.SecurityManager.ClientSecurityDetails;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class represents that state of the server
 *
 * @author Chris Foxley-Evans
 * @version 0.0.1
 */
public class ServerState implements Serializable {

    //instance vars
    public ArrayList<Auction> auctions;
    public ArrayList<ClientSecurityDetails> /* my precious */ clientSecurityDetailses;
    public int auctionID;

<<<<<<< HEAD
=======
    //constructor
>>>>>>> SCC311Security
    public ServerState() {
        this.auctions = new ArrayList<Auction>();
        this.clientSecurityDetailses = new ArrayList<ClientSecurityDetails>();
        this.auctionID = 0;
    }
}
