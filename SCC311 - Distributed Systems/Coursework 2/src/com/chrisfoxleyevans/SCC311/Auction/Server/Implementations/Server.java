package com.chrisfoxleyevans.SCC311.Auction.Server.Implementations;

import com.chrisfoxleyevans.SCC311.Auction.Server.Interfaces.IServer;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class Server implements IServer {
    private int auctionID;
    public ArrayList<Auction> activeAuctions;

    public Server() {
        auctionID = 0;
        this.activeAuctions = new ArrayList<Auction>();
    }

    @Override
    public int registerAuction(int clientID, String description, double reservePrice) throws RemoteException {
        int id = auctionID++;
        activeAuctions.add(new Auction(clientID, id, description, reservePrice));

        //Display info on the server console
        System.out.println("AUCTION REGISTERED -  ClientID: " + clientID + " AuctionID: " + id + " Description: " + description + " Reserve: " + reservePrice);
        return id;
    }

    @Override
    public Boolean registerBid(int clientID, int auctionID, String username, double bidValue) throws RemoteException {
        Boolean response = false;
        for (Auction i : activeAuctions) {
            if (i.auctionID == auctionID) {
                if (i.maxBid.bidValue > bidValue) {
                    response = false;
                } else {
                    i.maxBid = new Bid(clientID, username, bidValue);
                    //display info on the server console
                    System.out.println("BID ACCEPTED - ClientID: " + clientID + " AuctionID: " + i.auctionID + " BidValue: " + bidValue);
                    response = true;
                }
                return response;
            }
        }
        throw new RemoteException("No auction with the ID: " + auctionID);
    }

    @Override
    public ArrayList<Auction> getActiveAuctions() throws RemoteException {
        return activeAuctions;
    }

}

