package com.chrisfoxleyevans.SCC311.Auction.Server.ReplicationManager;

import com.chrisfoxleyevans.SCC311.Auction.Server.Implementations.Server;
import com.chrisfoxleyevans.SCC311.Auction.Server.StateManager.ServerState;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;

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
        }
        catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    @Override
    public void setState(InputStream input) throws Exception {
        super.setState(input);
    }

    @Override
    public void getState(OutputStream output) throws Exception {
        super.getState(output);
    }

    public void send(Message msg) {
        try {
            channel.send(msg);
        }
        catch (Exception e) {

        }
    }

    @Override
    public void receive(Message msg) {
        synchronized (server.state) {
            server.state = (ServerState) msg.getObject();
        }
    }

}

