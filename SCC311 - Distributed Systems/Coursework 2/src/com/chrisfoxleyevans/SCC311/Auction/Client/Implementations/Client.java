package com.chrisfoxleyevans.SCC311.Auction.Client.Implementations;

import com.chrisfoxleyevans.SCC311.Auction.Client.SecurityManager.ClientSecurityManager;
import com.chrisfoxleyevans.SCC311.Auction.Client.StateManager.ClientState;
import com.chrisfoxleyevans.SCC311.Auction.Client.StateManager.ClientStateManager;
import com.chrisfoxleyevans.SCC311.Auction.Server.Implementations.Auction;
import com.chrisfoxleyevans.SCC311.Auction.Server.Implementations.Bid;
import com.chrisfoxleyevans.SCC311.Auction.Server.Interfaces.IServer;

import javax.crypto.SealedObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Random;

/**
<<<<<<< HEAD:SCC311 - Distributed Systems/Coursework 2/src/com/chrisfoxleyevans/SCC311/Auction/Client/Client.java
 * This class holds the min code for the client side of the application
=======
 * This class implements all of the functionality that the client will need
>>>>>>> SCC311Security:SCC311 - Distributed Systems/Coursework 2/src/com/chrisfoxleyevans/SCC311/Auction/Client/Implementations/Client.java
 *
 * @author Chris Foxley-Evans
 * @version 0.0.1
 */
public class Client {
    //instance vars
    public ClientState state;
    public IServer server;

    //constructor
    public Client(String hostname) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String username = "";
        String password = "";
        //read in the users username and password
        try {
            System.out.print("Please enter you username: ");
            username = bufferedReader.readLine();

            System.out.print("Please enter a password: ");
            password = bufferedReader.readLine();
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }

        //attempt to read the saved client state
        ClientState state = ClientStateManager.loadState(username);
        if (state == null) {
            this.state = new ClientState(username, password);
            Random random = new Random();
            this.state.clientID = random.nextInt(Integer.MAX_VALUE) + 1;
            ClientStateManager.saveState(this.state);
        } else {
            if (state.key.hashCode() == ClientSecurityManager.generateKey(password).hashCode()) {
                this.state = state;
            } else {
                System.out.println("Sorry that is not the correct password for this user");
                closeApplication("Password authentication failed");
            }
        }

        //attempt to connect to the server
        try {
            Registry registry = LocateRegistry.getRegistry(hostname);
            this.server = (IServer) registry.lookup("AuctionServer");
            this.server.registerClient(this.state.clientID, this.state.key);
        } catch (Exception e) {
            System.out.println("ERROR: Unable to connect to the server");
            closeApplication("Failed to connect to the server unable to continue.");
        }
    }

    //public methods
    public void displayAuctions() {
        try {
            ArrayList<Auction> auctions = ClientSecurityManager.decryptAuctions(server.getActiveAuctions(state.clientID), state.key);

            if (auctions.size() > 0) {
                for (Auction i : auctions) {
                    System.out.println("AuctionID: " + i.auctionID + " Description: " + i.itemDescription
                            + " Current Price: " + i.maxBid.bidValue);
                }
            } else {
                System.out.println("The server has no active auctions.");
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

            if(!ownAuction(auctionID)) {
                Bid response = ClientSecurityManager.decryptBid(
                        server.registerBid(state.clientID,
                        ClientSecurityManager.encryptBid(new Bid(auctionID, state.clientID, bidValue), state.key)),state.key);
                if (response != null) {
                    System.out.println("Bid has been placed.");
                }
            }
            else {
                System.out.println("ERROR: Unable to register the bid.");
            }
        } catch (Exception e) {
            System.out.println("ERROR: Unable to register the bid.");
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

            //seal the object and send it to the server
            SealedObject serverResponse = server.registerAuction(state.clientID, ClientSecurityManager.encryptAuction(
                    new Auction(state.clientID, description, reserveValue, startPrice), state.key));

            //decrypt the response
            Auction response = ClientSecurityManager.decryptAuction(serverResponse, state.key);

            //print the response
            System.out.println("The item was listed with the ID: " + response.auctionID + ".");

            //add the auction to the state
            state.auctions.add(ClientSecurityManager.decryptAuction(serverResponse, state.key));

            //save the state
            ClientStateManager.saveState(state);
        } catch (Exception e) {
            System.out.println("ERROR: Unable to register the auction.");
        }
    }

    public void closeAuction() {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            //grab the auction ID
            System.out.print("Please enter the ID of the auction you wish to close: ");
            int auctionID = Integer.parseInt(bufferedReader.readLine());

            //make sure that we own this auction
            for (Auction i : state.auctions) {
                if (i.auctionID == auctionID) {


                    SealedObject encryptedAuction = ClientSecurityManager.encryptAuction(i, state.key);
                    SealedObject sealedResponse = server.closeAuction(state.clientID, encryptedAuction);
                    Bid winningBid = ClientSecurityManager.decryptBid(sealedResponse, state.key);

                    if (winningBid.bidValue < i.reservePrice) {
                        System.out.println("The auction failed to meet the reserve");
                    }
                    else {
                        System.out.println("The auction was won by: " + winningBid.clientID + " with a bid of: " + winningBid.bidValue);
                    }

                    state.auctions.remove(i);
                    ClientStateManager.saveState(state);
                    break;
                }
            }

        } catch (Exception e) {
            System.out.println("ERROR: Unable to close the auction.");
            System.out.println(e.getMessage());
        }
    }

    public void displayMenu() {
        System.out.println("******************************************");
        System.out.println("* SCC311 Auction System - Client Program *");
        System.out.println("******************************************");
        System.out.println("* View current active auctions:        1 *");
        System.out.println("* Place a bid on an active auction:    2 *");
        System.out.println("* List an item for an active auction:  3 *");
        System.out.println("* Close an active auction:             4 *");
        System.out.println("******************************************");

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
            System.out.println("ERROR: Problem with the option that you entered");
        }
    }

    //private methods
    private boolean ownAuction(int auctionID) {
        for (Auction i : state.auctions) {
            if (i.auctionID == auctionID) {
                return true;
            }
        }
        return false;
    }

    private void closeApplication(String message) {
        System.out.println("FATAL ERROR: " + message);
        System.exit(-1);
    }
}