package com.chrisfoxleyevans.SCC311.Auction.Server.Interfaces;

import com.chrisfoxleyevans.SCC311.Auction.Server.Implementations.Auction;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IServer extends Remote {

    public int registerAuction(String description, double reservePrice) throws RemoteException;

    public Boolean registerBid(long auctionID, String username, double bidValue) throws RemoteException;

    public ArrayList<Auction> getActiveAuctions() throws RemoteException;
}

