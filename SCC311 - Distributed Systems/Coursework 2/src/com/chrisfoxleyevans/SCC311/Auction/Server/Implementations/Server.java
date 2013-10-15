package com.chrisfoxleyevans.SCC311.Auction.Server.Implementations;

import com.chrisfoxleyevans.SCC311.Auction.Server.Interfaces.IServer;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Random;

public class Server implements IServer {
    private int auctionID;
    public ArrayList<Auction> activeAuctions;

    public Server() {
        auctionID = 0;
        this.activeAuctions = new ArrayList<Auction>();
    }

    @Override
    public int registerAuction(String description, double reservePrice) throws RemoteException {
        int id = auctionID++;
        activeAuctions.add(new Auction(id, description, reservePrice));
        return id;
    }

    @Override
    public Boolean registerBid(long auctionID, String username, double bidValue) throws RemoteException {
        for (Auction i : activeAuctions) {
            if(i.auctionID == auctionID) {
                if (bidValue > i.maxBid.bidValue) {
                    i.maxBid = new Bid(username, bidValue);
                    return true;
                }
                return false;
            }
        }
        throw new RemoteException("No auction with this id found");
    }

    @Override
    public ArrayList<Auction> getActiveAuctions() throws RemoteException {
       return activeAuctions;
    }

}

