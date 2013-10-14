package com.chrisfoxleyevans.SCC311.Auction.Client.Interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This interface serves to establish the contract that the clients will offer to server
 *
 * @author Chris Foxley-Evans
 * @version 0.0.1
 */
public interface IClient extends Remote {
    /**
     * This method will cause the client to display the list of actve auctions on the server
     *
     * @throws RemoteException
     */
    public void displayAuctions() throws RemoteException;
}
