package com.chrisfoxleyevans.SCC311.Auction.Server.StateManager;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * This class deal with the persistence file for the server
 *
 * @author Chris Foxley-Evans
 * @version 0.0.1
 */
public class ServerStateManager {



    //public methods
    public static boolean saveState(ServerState state, int portNumber) {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("serverState" + portNumber));
            objectOutputStream.writeObject(state);
            return true;
        } catch (Exception e) {
            System.out.println("ERROR: Unable to write the state file");
            return false;
        }
    }

    public static ServerState loadState(int portNumber) {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("ServerState" + portNumber));
            return (ServerState) objectInputStream.readObject();
        } catch (Exception e) {
            System.out.println("INFO: No state found creating new state file");
            return null;
        }
    }
}
