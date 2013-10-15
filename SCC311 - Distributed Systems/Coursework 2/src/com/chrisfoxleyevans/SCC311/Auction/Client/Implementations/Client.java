package com.chrisfoxleyevans.SCC311.Auction.Client.Implementations;

import com.chrisfoxleyevans.SCC311.Auction.Server.Implementations.Auction;
import com.chrisfoxleyevans.SCC311.Auction.Server.Interfaces.IServer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

public class Client {
    //instance vars
    public IServer server;

    //constructor
    public Client(String hostname) {
        connectToServer(hostname);
    }

    //public methods
    public void displayAuctions() {
        try {
            ArrayList<Auction> auctions = server.getActiveAuctions();
            if (auctions.size() > 0) {
                for (Auction i : auctions) {
                    System.out.println(i.auctionID + " " + i.itemDescription);
                }
            }
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }

    }

    public void registerBid() {
        //TODO code to read in the info needed to register a bid and then give the info to the server
    }

    public void registerAuction() {
        //TODO code to read in the info needed to register an auction and then give the info to the server

    }

    public void closeAuction() {
        //TODO code to read in the info needed to close the auction and then give the info to the user
    }

    public void displayMenu() {
        System.out.println("* SCC311 Auction System - Client Program");
        System.out.println("* View current active auctions: 1");
        System.out.println("* Place a bid on an active auction: 2");
        System.out.println("* List an item for an active auction: 3");
        System.out.println("* Close an active auction: 4");

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.print("Enter selection: ");
            int choice = Integer.parseInt(bufferedReader.readLine());
            switch(choice) {
                case 1:
                    displayAuctions();
                    break;
                case 2:
                    registerBid();
                    break;
                case 3:
                    registerAuction();
                    break;
                case 4:
                    closeAuction();
                    break;
            }
        }
        catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }


    }


    //private methods
    private void connectToServer(String hostname) {
        try {
            Registry registry = LocateRegistry.getRegistry(hostname, 1099);
            server = (IServer) registry.lookup("AuctionServer");
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
}
