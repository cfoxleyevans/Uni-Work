package com.chrisfoxleyevans.SCC311.Auction.Client.Implementations;

import com.chrisfoxleyevans.SCC311.Auction.Client.Interfaces.IClient;
import com.chrisfoxleyevans.SCC311.Auction.Server.Implementations.Auction;
import com.chrisfoxleyevans.SCC311.Auction.Server.Interfaces.IServer;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

/**
 * This server to implement to contract defined by IClient
 */
public class Client implements IClient {

    public IServer server;


    public Client(String hostname) {
        connectToServer(hostname);
    }

    @Override
    public void displayAuctions() throws RemoteException {
        ArrayList<Auction> auctions = server.getActiveAuctions();
        System.out.println("AuctionID, Item Description, Current High Bid, Current High Bid User.");
        if (auctions.size() < 1) {
            System.out.println("There are no auctions.....");
        } else {
            for (Auction i : auctions) {
                System.out.println(i.auctionID + " * " + i.itemDescription + " * " + i.maxBid.bidValue);
            }
        }
    }

    public void registerAuction(String desciription, double reservePrice) {
        try {
            server.registerAuction("This is a nice hat", 10D);
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }


    /**
     * This function is called as part of the constructor
     *
     * @param hostname The hostname of the server
     */
    private void connectToServer(String hostname) {
        try {
            Registry registry = LocateRegistry.getRegistry(hostname, 1099);
            server = (IServer) registry.lookup("AuctionServer");
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
}
