package com.chrisfoxleyevans.SCC311.Auction.Server.StateManager;

import com.chrisfoxleyevans.SCC311.Auction.Server.Implementations.Auction;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class represents that state of the server
 *
 * @author Chris Foxley-Evans
 * @version 0.0.1
 */
public class ServerState implements Serializable {

    public ArrayList<Auction> auctions;
    public int auctionID;

    public ServerState() {
        this.auctions = new ArrayList<Auction>();
        this.auctionID = 0;
    }
}
