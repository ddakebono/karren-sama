package org.frostbite.karren.InterConnect;

import org.frostbite.karren.Karren;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.ArrayList;

public class InterConnectListener extends Thread {
    Logger log;
    ArrayList<Extender> extenders = new ArrayList<Extender>();
    boolean killServer = false;

    public InterConnectListener(Logger log){
        this.log = log;
    }

    public void run(){
        log.info("Starting up the extender socket server!");
        try{
            ServerSocket server = new ServerSocket(Integer.parseInt(Karren.conf.getExtPort()), 1000, InetAddress.getByName(Karren.conf.getExtAddr()));
            while(!killServer){
                log.info("Waiting for extender on port " + Karren.conf.getExtPort());
                Extender newExtender = new Extender(null, null, server.accept(), log);
                log.info("Extender connected!");
                newExtender.startExtender();
                extenders.add(newExtender);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
