package com.chrisfoxleyevans.SCC311.Auction.Server.Implementations;

import com.chrisfoxleyevans.SCC311.Auction.Server.Interfaces.IServer;
import com.chrisfoxleyevans.SCC311.Auction.Server.SecurityManager.ClientSecurityDetails;
import com.chrisfoxleyevans.SCC311.Auction.Server.SecurityManager.ServerSecurityManager;
import com.chrisfoxleyevans.SCC311.Auction.Server.StateManager.ServerState;
import com.chrisfoxleyevans.SCC311.Auction.Server.StateManager.ServerStateManager;

import javax.crypto.SealedObject;
import java.rmi.RemoteException;
import java.security.Key;
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

    public void registerClient(int id, Key key) {
        boolean idFound = false;
        for (ClientSecurityDetails i : state.clientSecurityDetailses) {
            if (i.clientID == id) {
                idFound = true;
            }
        }
        if (!idFound)
            state.clientSecurityDetailses.add(new ClientSecurityDetails(id, key));
    }

    @Override
    public ArrayList<Auction> getActiveAuctions() throws RemoteException {
        if (state.auctions.size() <= 0) {
            throw new RemoteException("There are no active auctions");
        }
        return state.auctions;
    }

    @Override
    public synchronized SealedObject registerAuction(int clientID, SealedObject encryptedAuction) throws RemoteException {
        //find this clients key
        Key key = findClientKey(clientID);
        if (key != null) {
            Auction auction = ServerSecurityManager.decryptAuction(encryptedAuction, key);
            auction.auctionID = state.auctionID++;
            state.auctions.add(auction);
            System.out.println("AUCTION REGISTERED -  ClientID: " + clientID + " AuctionID: " + auction.auctionID +
                    " Description: " + auction.itemDescription + " Reserve price: " + auction.reservePrice +
                    " Start price: " + auction.maxBid.bidValue);
            ServerStateManager.saveState(state);
            return ServerSecurityManager.encryptAuction(auction, key);
        } else {
            throw new RemoteException("Unable to register auction");
        }
    }

    @Override
    public synchronized SealedObject registerBid(int clientID, SealedObject encryptedBid) throws RemoteException {
        //find the clients key
        Key key = findClientKey(clientID);
        if (key != null) {
            Bid bid = ServerSecurityManager.decryptBid(encryptedBid, key);
            if (bid != null) {
                //iterate over all of auctions
                for (Auction i : state.auctions) {
                    //if the auction id's match
                    if (i.auctionID == bid.auctionID) {
                        //make sure that the bidder is not the client that listed the item
                        if (i.clientID != clientID) {
                            //make sure that the bid is greater than the current bid
                            if (i.maxBid.bidValue < bid.bidValue) {
                                //place the bid
                                placeBid(i, bid, clientID);
                                //update the state
                                ServerStateManager.saveState(state);
                                return ServerSecurityManager.encryptBid(bid, key);
                            } else {
                                //if the bid is not high enough
                                throw new RemoteException("The bid value is less than the current bid");
                            }
                        } else {
                            //if the client is the auction owner
                            throw new RemoteException("You cannot bid on your own auction");
                        }
                    }
                }
            }


        }
        //if the auction cant be found
        throw new RemoteException("No auction with this ID exists");
    }

    @Override
    public synchronized Bid closeAuction(int auctionID, int clientID) throws RemoteException {
        //iterate over all of the auctions
        for (Auction i : state.auctions) {
            //if the auction id's match
            if (i.auctionID == auctionID) {
                //make sure that the closer owns the auction
                if (i.clientID == clientID) {
                    //make sure that the reserve has been met
                    if (i.maxBid.bidValue > i.reservePrice) {
                        //remove the auction from the server
                        state.auctions.remove(i);
                        //save the state object
                        ServerStateManager.saveState(state);
                        //return the winning bid
                        return i.maxBid;
                    } else {
                        //remove the auction from the server
                        state.auctions.remove(i);
                        //save the state object
                        ServerStateManager.saveState(state);
                        //if the item failed to meet the reserve
                        throw new RemoteException("The item failed to meet its reserve price");
                    }
                } else {
                    //if the client attempts to close an auction that they do not own
                    throw new RemoteException("You cannot close an auction that you do not own");
                }
            }
        }
        //if the auction cannot be found
        throw new RemoteException("No auction with this ID exists");
    }

    /**
     * This function simply wraps the act of placing a bid
     *
     * @param auction The auction that is being edited
     * @param bid     The bid that is being placed
     */
    private synchronized void placeBid(Auction auction, Bid bid, int clientID) {
        auction.maxBid = bid;
        System.out.println("BID ACCEPTED - ClientID: " + clientID + " AuctionID: " + auction.auctionID + " BidValue: " + bid.bidValue);
    }

    private synchronized Key findClientKey(int clientID) {
        for (ClientSecurityDetails i : state.clientSecurityDetailses) {
            if (i.clientID == clientID) {
                return i.key;
            }
        }
        return null;
    }
}

