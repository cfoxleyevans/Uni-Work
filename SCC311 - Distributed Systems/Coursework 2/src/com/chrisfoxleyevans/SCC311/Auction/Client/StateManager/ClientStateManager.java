package com.chrisfoxleyevans.SCC311.Auction.Client.StateManager;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * This class deal with the persistence file for the client
 *
 * @author Chris Foxley-Evans
 * @version 0.0.1
 */
public class ClientStateManager {

    public static boolean saveState(ClientState state) {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(state.username));
            objectOutputStream.writeObject(state);
            return true;
        } catch (Exception e) {
            System.out.println("ERROR: Unable to write the state file");
            return false;
        }
    }

    public static ClientState loadState(String username) {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(username));
            return (ClientState) objectInputStream.readObject();
        } catch (Exception e) {
            System.out.println("INFO: No state found for that user creating new state file");
            return null;
        }
    }
}
