package com.chrisfoxleyevans.SCC311.Auction.Server.Implementations;

import com.chrisfoxleyevans.SCC311.Auction.Server.Interfaces.IServer;
import com.chrisfoxleyevans.SCC311.Auction.Server.StateManager.ServerState;
import com.chrisfoxleyevans.SCC311.Auction.Server.StateManager.ServerStateManager;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * This class provides the implementation of the IServer interface
 *
 * @author Chris Foxley-Evans
 * @version 0.0.1
 */
public class Server implements IServer {

    public ServerState state;

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

    @Override
    public ArrayList<Auction> getActiveAuctions() throws RemoteException {
        if (state.auctions.size() <= 0) {
            throw  new RemoteException("There are no active auctions");
        }
        return state.auctions;
    }

    @Override
    public int registerAuction(int clientID, String description, double reservePrice, double startPrice) throws RemoteException {
        int id = state.auctionID++;

        state.auctions.add(new Auction(id, clientID, description, reservePrice, startPrice));

        System.out.println("AUCTION REGISTERED -  ClientID: " + clientID + " AuctionID: " + id + " Description: " + description +
                " Reserve price: " + reservePrice + " Start price: " + startPrice);

        ServerStateManager.saveState(state);
        return id;
    }

    @Override
    public Boolean registerBid(int clientID, int auctionID, String username, double bidValue) throws RemoteException {
        //iterate over all of auctions
        for (Auction i : state.auctions) {
            //if the auction id's match
            if (i.auctionID == auctionID) {
                //make sure that the bidder is not the client that listed the item
                if (i.clientID != clientID) {
                    //make sure that the bid is greater than the current bid
                    if (i.maxBid.bidValue < bidValue) {
                        //place the bid
                        placeBid(i, new Bid(clientID, username, bidValue));
                        //update the state
                        ServerStateManager.saveState(state);
                        return true;
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
        //if the auction cant be found
        throw new RemoteException("No auction with this ID exists");
    }

    @Override
    public Bid closeAuction(int auctionID, int clientID) throws RemoteException {
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

    private void placeBid(Auction auction, Bid bid) {
        auction.maxBid = bid;
        System.out.println("BID ACCEPTED - ClientID: " + bid.clientID + " AuctionID: " + auction.auctionID + " BidValue: " + bid.bidValue);
    }
}

