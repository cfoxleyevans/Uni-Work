package com.chrisfoxleyevans.SCC311.Auction.Server.Interfaces;

import com.chrisfoxleyevans.SCC311.Auction.Server.Implementations.Bid;

import javax.crypto.SealedObject;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.Key;

/**
 * This interface provides the definition for the methods that the Server class will implement
 *
 * @author Chris Foxley-Evans
 * @version 0.0.1
 */
public interface IServer extends Remote {

    //public methods
    public void registerClient(int id, Key key) throws RemoteException;

    public SealedObject getActiveAuctions(int clientID) throws RemoteException;

    public SealedObject registerAuction(int clientID, SealedObject auction) throws RemoteException;

    public SealedObject registerBid(int clientID, SealedObject bid) throws RemoteException;

    public Bid closeAuction(int auctionID, int clientID) throws RemoteException;
}

