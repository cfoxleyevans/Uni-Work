package com.chrisfoxleyevans.SCC311.Auction.Server.Implementations;

import com.chrisfoxleyevans.SCC311.Auction.Server.Interfaces.IServer;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Random;

/**
 * This class serves to implement to contract defined by IServer
 *
 * @author Chris Foxley-Evans
 * @version 0.0.1
 */
public class Server implements IServer{

    //instance vars
    private Random idGenerator;
    public ArrayList<Auction> activeAuctions;

    //constructor
    public Server() {
        this.idGenerator = new Random(105332434);
        this.activeAuctions = new ArrayList<Auction>(50);
    }

    //public methods
    @Override
    public long registerAuction(Auction info) throws RemoteException {
        activeAuctions.add(info);
        info.auctionID = idGenerator.nextInt();
        return info.auctionID;
    }

    @Override
    public ArrayList<Auction> getActiveAuctions() throws RemoteException {
        return activeAuctions;
    }

    @Override
    public Boolean registerBid(long auctionID, String username, double bidValue) throws RemoteException {
        for(Auction i : activeAuctions) {
            if(i.auctionID == auctionID) {
                if(bidValue > i.reservePrice && bidValue > i.maxBid.bidValue) {
                    i.updateCurrentMaxBid(new Bid(username, bidValue));
                    return true;
                }
            }
        }
        throw new RemoteException("No auction with this id");
    }
}
