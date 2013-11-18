package com.chrisfoxleyevans.SCC311.Auction.Client;

import com.chrisfoxleyevans.SCC311.Auction.Client.StateManager.ClientState;
import com.chrisfoxleyevans.SCC311.Auction.Client.StateManager.ClientStateManager;
import com.chrisfoxleyevans.SCC311.Auction.Server.Implementations.Auction;
import com.chrisfoxleyevans.SCC311.Auction.Server.Implementations.Bid;
import com.chrisfoxleyevans.SCC311.Auction.Server.Interfaces.IServer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Random;

public class ClientSeller {
    //instance vars
    public ClientState state;
    public IServer server;

    //constructor
    public ClientSeller(String hostname) {
        //read in the users email
        System.out.print("Please enter you username: ");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String username = "";
        try {
            username = bufferedReader.readLine();
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }

        //attempt to read the saved client state
        ClientState state = ClientStateManager.loadState(username);
        if (state == null) {
            this.state = new ClientState(username);
            Random random = new Random();
            this.state.clientID = random.nextInt(Integer.MAX_VALUE) + 1;
            ClientStateManager.saveState(this.state);
        } else {
            this.state = state;
        }

        //attempt to connect to the server
        try {
            Registry registry = LocateRegistry.getRegistry(hostname);
            this.server = (IServer) registry.lookup("AuctionServer");
        } catch (Exception e) {
            System.out.println("ERROR: Unable to connect to the server");
            closeApplication("Failed to connect to the server unable to continue");
        }
    }

    //public methods
    public void displayAuctions() {
        try {
            ArrayList<Auction> auctions = server.getActiveAuctions();
            for (Auction i : auctions) {
                System.out.println("AuctionID: " + i.auctionID + " Description: " + i.itemDescription
                        + " Current Price: "  + i.maxBid.bidValue);
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

            //grab the bid amount
            System.out.print("Please enter the bid amount: ");
            double bidValue = Double.parseDouble(bufferedReader.readLine());

            //send the bid to the server
            Boolean serverResponse = server.registerBid(state.clientID, auctionID, state.username, bidValue);
            if (serverResponse != null) {
                System.out.println("The bid was accepted by the server");
            }
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getCause());

        }
    }

    public void registerAuction() {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            //grab the auction description
            System.out.print("Please enter a description of the item: ");
            String description = bufferedReader.readLine();

            //grab the reserve amount
            System.out.print("Please enter the reserve price: ");
            double reserveValue = Double.parseDouble(bufferedReader.readLine());

            //grab the start price
            System.out.print("Please enter the start price: ");
            double startPrice = Double.parseDouble(bufferedReader.readLine());


            int serverResponse = server.registerAuction(state.clientID, description, reserveValue, startPrice);
            System.out.println("The item was listed with the ID: " + serverResponse);
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    public void closeAuction() {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            //grab the auction description
            System.out.print("Please enter the ID of the auction you wish to close: ");
            int auctionID = Integer.parseInt(bufferedReader.readLine());

            Bid winningBid = server.closeAuction(auctionID, state.clientID);
            if (winningBid != null) {
                System.out.println("The item was won by: " + winningBid.username + " with a bid of " + winningBid.bidValue);
            }

        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    public void displayMenu() {
        System.out.println("******************************************");
        System.out.println("* SCC311 Auction System - Client Program *");
        System.out.println("******************************************");
        System.out.println("* List an item for an active auction:  1 *");
        System.out.println("* Close an active auction:             2 *");
        System.out.println("******************************************");

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.print("Enter selection: ");
            int choice = Integer.parseInt(bufferedReader.readLine());
            switch (choice) {
                case 1:
                    registerAuction();
                    break;
                case 2:
                    closeAuction();
                    break;
            }
        } catch (Exception e) {
            System.out.println("ERROR: Problem with the option that you entered");
        }
    }

    private void closeApplication(String message) {
        System.out.println("FATAL ERROR: " + message);
        System.exit(-1);
    }
}
