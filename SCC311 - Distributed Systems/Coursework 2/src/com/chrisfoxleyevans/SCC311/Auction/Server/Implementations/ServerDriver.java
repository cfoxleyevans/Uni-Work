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
            Registry registry = LocateRegistry.createRegistry(2000);
            Server server = new Server("AuctionServerZeroState.state");
            IServer stub = (IServer) UnicastRemoteObject.exportObject(server, 2000);

            registry.bind("AuctionServerZero", stub);
            System.out.println("Server Zero is running. Bound to the name \"AuctionServerZero\" on port 2000");


//            Registry registry = LocateRegistry.createRegistry(2001);
//            Server server = new Server("AuctionServerOneState.state");
//            IServer stub = (IServer) UnicastRemoteObject.exportObject(server, 2001);
//
//            registry.bind("AuctionServerOne", stub);
//            System.out.println("Server One is running. Bound to the name \"AuctionServerOne\" on port 2001");

        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
}
