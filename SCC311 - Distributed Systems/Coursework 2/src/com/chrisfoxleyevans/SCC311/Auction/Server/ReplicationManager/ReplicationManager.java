package com.chrisfoxleyevans.SCC311.Auction.Server.ReplicationManager;

import com.chrisfoxleyevans.SCC311.Auction.Server.Implementations.Server;
import com.chrisfoxleyevans.SCC311.Auction.Server.StateManager.ServerState;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;

import javax.rmi.CORBA.Util;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * This class models the methods needed for the server to sync state
 *
 * @author Chris Foxley-Evans
 * @version 0.0.1
 */
public class ReplicationManager extends ReceiverAdapter{
    Server server;
    JChannel channel;


    public ReplicationManager(Server server){
        this.server = server;
        try {
            this.channel = new JChannel();
            this.channel.connect("AuctionServerReplication");
            channel.setReceiver(this);
            channel.setDiscardOwnMessages(true);
            channel.getState(null, 1000);
        }
        catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    @Override
    public void getState(OutputStream output) throws Exception {
        synchronized(server.state) {
            org.jgroups.util.Util.objectToStream(server.state, new DataOutputStream(output));

        }

    }

    @Override
    public void setState(InputStream input) throws Exception {
        ServerState s =(ServerState)org.jgroups.util.Util.objectFromStream(new DataInputStream(input));

        synchronized(server.state) {
            server.state = s;
        }
    }

    @Override
    public void receive(Message msg) {
        synchronized (server.state) {
            server.state = (ServerState) msg.getObject();
        }
    }

    public void send(Message msg) {
        try {
            channel.send(msg);
        }
        catch (Exception e) {

        }
    }

}

