package com.chrisfoxleyevans.SCC311.Auction.Server.Interfaces;

import com.chrisfoxleyevans.SCC311.Auction.Server.Implementations.Auction;
import com.chrisfoxleyevans.SCC311.Auction.Server.Implementations.Bid;

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
     *                         or if the server is currently hosting no auctions
     */
    public ArrayList<Auction> getActiveAuctions() throws RemoteException;

    /**
     * This method is invoked by the client to register an auction
     *
     * @param clientID     The ID of the client that is registering the auction
     * @param description  The description of the item that the client is registering
     * @param reservePrice The reserve price of the item that the client is registering
     * @param startPrice   The starting price of the item that the client is registering
     * @return Returns an integer with the registered items AuctionID
     * @throws RemoteException Throws a Remote Exception if there is an issue completing the method
     *                         or if the server is unable to register the bid
     */
    public int registerAuction(int clientID, String description, double reservePrice, double startPrice) throws RemoteException;

    /**
     * This method is invoked by the client to register a bid on an item
     *
     * @param clientID  The ID of the client that is registering the bid
     * @param auctionID The ID of the auction that the client is bidding on
     * @param username  The username of the client
     * @param bidValue  The value of the bid that the client is registering
     * @return Returns true if the bid was registered successfully
     * @throws RemoteException Throws a Remote Exception if there is an issue completing the method
     *                         or if the bid is unable to be registerd
     */
    public Boolean registerBid(int auctionID, int clientID, String username, double bidValue) throws RemoteException;

    /**
     * This method is invoked by the client to close an auction
     *
     * @param auctionID The of the auction that the client wishes to close
     * @param clientID  The ID of the client that wishes to close the auction
     * @return Returns a bid object that contains the details of the winning bid
     * @throws RemoteException Throws a remote exception if there is an issue completing the method
     *                         or if the high bid is below the items reserve
     */
    public Bid closeAuction(int auctionID, int clientID) throws RemoteException;
}

