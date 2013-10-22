package com.chrisfoxleyevans.SCC311.Auction.Server.Implementations;

import com.chrisfoxleyevans.SCC311.Auction.Server.Interfaces.IServer;
import com.chrisfoxleyevans.SCC311.Auction.Server.StateManager.ServerState;
import com.chrisfoxleyevans.SCC311.Auction.Server.StateManager.ServerStateManager;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * This class provides the implementation of the IServer interface
 *
 * @author Chris Foxley-Evans
 * @version 0.0.1
 */
public class Server implements IServer {

    public ServerState state;

    /**
     * This is the class constructor, it will attempt to restore state from the file
     * if this is not possible then it will start with an empty data structure
     */
    public Server() {
        //attempt to read the saved server state
        ServerState state = ServerStateManager.loadState();
        if (state == null) {
            this.state = new ServerState();
            ServerStateManager.saveState(this.state);
        } else {
            this.state = state;
        }
    }

    @Override
    public ArrayList<Auction> getActiveAuctions() throws RemoteException {
        return state.auctions;
    }

    @Override
    public int registerAuction(int clientID, String username, String description, double reservePrice) throws RemoteException {
        int id = state.auctionID++;
        state.auctions.add(new Auction(id, clientID, username, description, reservePrice));

        //Display info on the server console
        System.out.println("AUCTION REGISTERED -  ClientID: " + clientID + " AuctionID: " + id + " Description: " + description + " Reserve: " + reservePrice);

        ServerStateManager.saveState(state);
        return id;
    }

    @Override
    public Boolean registerBid(int clientID, int auctionID, String username, double bidValue) throws RemoteException {
        Boolean response = false;
        for (Auction i : state.auctions) {
            if (i.auctionID == auctionID) {
                if (i.clientID != clientID) {
                    if (i.maxBid.bidValue < bidValue) {
                        //place the bid
                        placeBid(i, new Bid(clientID, username, bidValue));
                        response = true;
                        ServerStateManager.saveState(state);
                    }
                }
            }
        }
        return response;
    }

    @Override
    public String closeAuction() throws RemoteException {
        return null;
    }

    private void placeBid(Auction auction, Bid bid) {
        auction.maxBid = bid;
        System.out.println("BID ACCEPTED - ClientID: " + bid.clientID + " AuctionID: " + auction.auctionID + " BidValue: " + bid.bidValue);
    }
}

