package com.chrisfoxleyevans.SCC311.Auction.Server.Interfaces;

import com.chrisfoxleyevans.SCC311.Auction.Server.Implementations.Auction;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * This interface provides the definition for the methods that the Server class will implement
 *
 * @author Chris Foxley-Evans
 * @version 0.0.1
 */
public interface IServer extends Remote {

    /**
     * This method is invoked by the client to get a list of the current active auctions
     *
     * @return Return and ArrayList of Auctions
     * @throws RemoteException Throws a Remote Exception if there is an issue completing the method
     */
    public ArrayList<Auction> getActiveAuctions() throws RemoteException;

    /**
     * This method is invoked by the client to register an auction
     *
     * @param clientID     The ID of the client that is registering the auction
     * @param description  The description of the item that the client is registering
     * @param reservePrice the reserve price of the item that the client is registering
     * @return Returns and integer with the Items auction ID
     * @throws RemoteException Throws a Remote Exception if there is an issue completing the method
     */
    public int registerAuction(int clientID, String username, String description, double reservePrice) throws RemoteException;

    /**
     * This method is invoked by the client to register a bid on an item
     *
     * @param clientID  The ID of the client that is registering the bid
     * @param auctionID The ID of the auction that the client is bidding on
     * @param username  The username of the client
     * @param bidValue  The value of the bid that the client is registering
     * @return Returns true if the bid was registered successfully
     * @throws RemoteException Throws a Remote Exception if there is an issue completing the method
     */
    public Boolean registerBid(int auctionID, int clientID, String username, double bidValue) throws RemoteException;

    public String closeAuction() throws RemoteException;
}

