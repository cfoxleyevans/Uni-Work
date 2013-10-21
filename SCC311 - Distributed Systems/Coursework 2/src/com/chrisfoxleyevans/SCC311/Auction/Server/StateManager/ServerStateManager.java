package com.chrisfoxleyevans.SCC311.Auction.Server.StateManager;

import com.chrisfoxleyevans.SCC311.Auction.Server.Implementations.Auction;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * This class deal with the persistence file for the server
 *
 * @author Chris Foxley-Evans
 * @version 0.0.1
 */
public class ServerStateManager {

    /**
     * Saves the passed in state object to a file for later use
     *
     * @param state The array list that holds all of the current auctions that the server is hosting
     * @return Returns true if the file is saved to the disk false if not
     */
    public static boolean saveState(ServerState state) {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("serverState"));
            objectOutputStream.writeObject(state);
            return true;
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            return false;
        }
    }

    /**
     * Attempts to load the server state from the file
     *
     * @return Returns the ArrayList containing the auctions if the file exists or null if not
     */
    public static ServerState loadState() {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("ServerState"));
            return (ServerState) objectInputStream.readObject();
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            return null;
        }
    }
}
