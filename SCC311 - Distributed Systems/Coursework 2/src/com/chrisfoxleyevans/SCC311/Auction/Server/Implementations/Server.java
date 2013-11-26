package com.chrisfoxleyevans.SCC311.Auction.Server.Implementations;

import com.chrisfoxleyevans.SCC311.Auction.Server.Interfaces.IServer;
import com.chrisfoxleyevans.SCC311.Auction.Server.SecurityManager.ClientSecurityDetails;
import com.chrisfoxleyevans.SCC311.Auction.Server.SecurityManager.ServerSecurityManager;
import com.chrisfoxleyevans.SCC311.Auction.Server.StateManager.ServerState;
import com.chrisfoxleyevans.SCC311.Auction.Server.StateManager.ServerStateManager;

import javax.crypto.SealedObject;
import java.rmi.RemoteException;
import java.security.Key;

/**
 * This class provides the implementation of the IServer interface
 *
 * @author Chris Foxley-Evans
 * @version 0.0.1
 */
public class Server implements IServer {

    //instance vars
    public ServerState state;

<<<<<<< HEAD
=======
    //constructor
>>>>>>> SCC311Security
    public Server() {
        ServerState state = ServerStateManager.loadState();
        if (state == null) {
            this.state = new ServerState();
            ServerStateManager.saveState(this.state);
        } else {
            this.state = state;
        }
    }

    //public methods
    public void registerClient(int id, Key key) {
        boolean idFound = false;
        for (ClientSecurityDetails i : state.clientSecurityDetailses) {
            if (i.clientID == id) {
                idFound = true;
                break;
            }
        }
        if (!idFound)
            state.clientSecurityDetailses.add(new ClientSecurityDetails(id, key));
    }

    @Override
    public synchronized SealedObject getActiveAuctions(int clientID) throws RemoteException {
        Key key = findClientKey(clientID);
        if (key != null) {
            return ServerSecurityManager.encryptActiveAuctions(state.auctions, key);
        } else {
            throw new RemoteException("Problem encrypting the response");
        }
    }

    @Override
    public synchronized SealedObject registerAuction(int clientID, SealedObject encryptedAuction) throws RemoteException {
        //find this clients key
        Key key = findClientKey(clientID);
        if (key != null) {
            Auction auction = ServerSecurityManager.decryptAuction(encryptedAuction, key);
            auction.auctionID = state.auctionID++;
            state.auctions.add(auction);
            System.out.println("AUCTION REGISTERED -" +
                    " ClientID: " + clientID +
                    " AuctionID: " + auction.auctionID +
                    " Description: " + auction.itemDescription + " Reserve price: " + auction.reservePrice +
                    " Start price: " + auction.maxBid.bidValue);
            ServerStateManager.saveState(state);
            return ServerSecurityManager.encryptAuction(auction, key);
        } else {
            throw new RemoteException("Unable to register auction - problem decrypting the information");
        }
    }

    @Override
    public synchronized SealedObject registerBid(int clientID, SealedObject encryptedBid) throws RemoteException {
        /*
         * Look up the clients key
         * As long as the key can be found
         * Find the auction object, make sure the new bid is higher, register the bid
         * Save the state
         * Return the encrypted bid object
         */
        Key key = findClientKey(clientID);
        if (key != null) {
            Bid bid = ServerSecurityManager.decryptBid(encryptedBid, key);
            if (bid != null) {
                for (Auction i : state.auctions) {
                    if (i.auctionID == bid.auctionID) {
                        if (placeBid(i, bid)) {
                            return ServerSecurityManager.encryptBid(bid, key);
                        }
                    }
                }
            }
        }
        throw new RemoteException();
    }

    @Override
    public synchronized SealedObject closeAuction(int clientID, SealedObject encryptedAuction) throws RemoteException {
        /*
         * Look up the clients key
         * As long as the key can be found
         * Find the auction object, remove it from the DS and return the encrypted max bid
         */
        Key key = findClientKey(clientID);
        if (key != null) {
            Auction auction = ServerSecurityManager.decryptAuction(encryptedAuction, key);
            if (auction != null) {
                for (Auction i : state.auctions) {
                    if (i.auctionID == auction.auctionID) {
                        state.auctions.remove(i);
                        ServerStateManager.saveState(state);
                        return ServerSecurityManager.encryptBid(i.maxBid, key);
                    }
                }
            }
        }
        throw new RemoteException();
    }

<<<<<<< HEAD
    private void placeBid(Auction auction, Bid bid) {
        auction.maxBid = bid;
        System.out.println("BID ACCEPTED - ClientID: " + bid.clientID + " AuctionID: " + auction.auctionID + " BidValue: " + bid.bidValue);
=======
    //private methods
    private synchronized boolean placeBid(Auction auction, Bid bid) {
        if (bid.bidValue >= auction.maxBid.bidValue) {
            auction.maxBid = bid;
            return true;
        }
        return false;
    }

    private synchronized Key findClientKey(int clientID) {
        for (ClientSecurityDetails i : state.clientSecurityDetailses) {
            if (i.clientID == clientID) {
                return i.key;
            }
        }
        return null;
>>>>>>> SCC311Security
    }
}

