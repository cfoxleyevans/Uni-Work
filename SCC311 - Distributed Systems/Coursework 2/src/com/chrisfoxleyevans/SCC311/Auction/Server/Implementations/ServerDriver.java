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

    //main method
    public static void main(String args[]) {
        try {
            Registry registry = LocateRegistry.createRegistry(1099);

            Server server = new Server();
            IServer stub = (IServer) UnicastRemoteObject.exportObject(server, 1099);

            registry.bind("AuctionServer", stub);

            System.out.println("Server is running. Bound to the name \"AuctionServer\" on port 1099");
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
}
