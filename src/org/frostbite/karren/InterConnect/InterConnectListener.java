/*
 * Copyright (c) 2016 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.InterConnect;

import org.frostbite.karren.Karren;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.ArrayList;

public class InterConnectListener extends Thread {
    Logger log;
    ArrayList<Extender> extenders = new ArrayList<>();
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
