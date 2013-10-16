package com.chrisfoxleyevans.SCC311.Auction.Client.Implementations;

import com.chrisfoxleyevans.SCC311.Auction.Server.Implementations.Auction;
import com.chrisfoxleyevans.SCC311.Auction.Server.Interfaces.IServer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Random;

public class Client {
    //instance vars
    public int clientID;
    public IServer server;

    //constructor
    public Client(String hostname) {
        this.clientID = new Random().nextInt(Integer.MAX_VALUE) + 1;
        try {
            Registry registry = LocateRegistry.getRegistry(hostname);
            this.server = (IServer) registry.lookup("AuctionServer");

        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            closeApplication("Failed to connect to the server unable to continue");
        }
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
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            //grab the auction ID
            System.out.print("Please enter the auctionID: ");
            int auctionID = Integer.parseInt(bufferedReader.readLine());

            //grab the email
            System.out.print("Please enter your email: ");
            String username = bufferedReader.readLine();

            //grab the bid amount
            System.out.print("Please enter the bid amount: ");
            double bidValue = Double.parseDouble(bufferedReader.readLine());

            //send the bid to the server
            Boolean serverResponse = server.registerBid(clientID, auctionID, username, bidValue);
            if (serverResponse) {
                System.out.println("The bid was accepted by the server");
            } else {
                System.out.println("The bid was rejected by the server");
            }
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());

        }
    }

    public void registerAuction() {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            //grab the auction description
            System.out.print("Please enter a description of the item: ");
            String description = bufferedReader.readLine();

            //grab the reserve amount
            System.out.print("Please enter the reserve amount: ");
            double reserveValue = Double.parseDouble(bufferedReader.readLine());

            int serverResponse = server.registerAuction(clientID, description, reserveValue);
            System.out.print("The item was listed with the ID: " + serverResponse);
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    public void closeAuction() {

    }

    public void displayMenu() {
        System.out.println();
        System.out.println();
        System.out.println("******************************************");
        System.out.println("* SCC311 Auction System - Client Program *");
        System.out.println("******************************************");
        System.out.println("* View current active auctions:        1 *");
        System.out.println("* Place a bid on an active auction:    2 *");
        System.out.println("* List an item for an active auction:  3 *");
        System.out.println("* Close an active auction:             4 *");
        System.out.println("******************************************");
        System.out.println();
        System.out.println();


        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.print("Enter selection: ");
            int choice = Integer.parseInt(bufferedReader.readLine());
            switch (choice) {
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
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private void closeApplication(String message) {
        System.out.println("FATAL ERROR: " + message);
        System.exit(-1);
    }
}
