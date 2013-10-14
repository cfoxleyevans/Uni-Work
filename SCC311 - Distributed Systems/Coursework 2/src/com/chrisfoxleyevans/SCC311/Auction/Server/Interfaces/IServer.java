package com.chrisfoxleyevans.SCC311.Auction.Server.Interfaces;

import com.chrisfoxleyevans.SCC311.Auction.Server.Implementations.Auction;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * This interface serves to establish the contract that the server will offer to clients
 *
 * @author Chris Foxley-Evans
 * @version 0.0.1
 */
public interface IServer extends Remote {
    /**
     * This function will allow the client to register a new auction with the server
     *
     * @return The ID of the registered auction
     * @throws RemoteException
     */
    public long registerAuction(Auction info) throws RemoteException;

    /**
     * This function will allow the client to get a list of the active auctions
     *
     * @return ArrayList containing the active auctions
     * @throws RemoteException
     */
    public ArrayList<Auction> getActiveAuctions() throws RemoteException;

    /**
     * This function will allow a client to register a bid on one of the active auctions
     *
     * @param auctionID The id of the item that the client wants to bid on
     * @param username  The email of the client
     * @param bidValue  The value that the client has bid for the item
     * @return True if the server has accepted the bid
     * @throws RemoteException
     */
    public Boolean registerBid(long auctionID, String username, double bidValue) throws RemoteException;
}

