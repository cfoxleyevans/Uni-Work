package com.chrisfoxleyevans.SCC311.Auction.Client.StateManager;

import com.chrisfoxleyevans.SCC311.Auction.Client.SecurityManager.ClientSecurityManager;
import com.chrisfoxleyevans.SCC311.Auction.Server.Implementations.Auction;
import com.chrisfoxleyevans.SCC311.Auction.Server.Implementations.Bid;

import java.io.Serializable;
import java.security.Key;
import java.util.ArrayList;

/**
 * This class represents that state of the client
 *
 * @author Chris Foxley-Evans
 * @version 0.0.1
 */
public class ClientState implements Serializable {

    //instance vars
    public int clientID;
    public String username;
    public String password;
    public Key key;
    public ArrayList<Auction> auctions; //items that the user is auctioning
    public ArrayList<Bid> bids; //bids that the user has placed

<<<<<<< HEAD
    public ClientState(String username) {
=======
    //constructor
    public ClientState(String username, String password) {
>>>>>>> SCC311Security
        this.clientID = 0;
        this.username = username;
        this.password = password;
        this.key = ClientSecurityManager.generateKey(this.password);
        this.auctions = new ArrayList<Auction>();
        this.bids = new ArrayList<Bid>();
    }
}