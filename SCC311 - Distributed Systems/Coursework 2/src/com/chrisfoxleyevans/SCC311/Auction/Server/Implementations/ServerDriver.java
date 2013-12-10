package com.chrisfoxleyevans.SCC311.Auction.Server.Implementations;

import com.chrisfoxleyevans.SCC311.Auction.Server.Interfaces.IServer;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * The class server as an entry point to the server app
 *
 * @author Chris Foxley-Evans
 * @version 0.0.1
 */
public class ServerDriver {
    public static int portNumber = 2000;

    //main method
    public static void main(String args[]) {
        try {
            Registry registry = LocateRegistry.createRegistry(portNumber);
            Server server = new Server("AuctionServer" + portNumber + ".state");
            IServer stub = (IServer) UnicastRemoteObject.exportObject(server, portNumber);

            registry.bind("AuctionServer", stub);
            System.out.println("Server is running. Bound to the name \"AuctionServer\" on port " + portNumber);

        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
}
