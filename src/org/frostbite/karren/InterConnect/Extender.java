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

import org.slf4j.Logger;

import java.io.*;
import java.net.Socket;

public class Extender {

    Service[] services;
    System system;

    Socket client;
    Logger log;
    PrintWriter pw;
    BufferedReader br;

    public Extender(Service[] services, System system, Socket client, Logger log) throws IOException {
        this.services = services;
        this.system = system;
        this.client = client;
        this.log = log;
        this.pw = new PrintWriter(new OutputStreamWriter(client.getOutputStream(), "UTF-8"), true);
        this.br = new BufferedReader(new InputStreamReader(client.getInputStream(), "UTF-8"));
    }

    public void startExtender(){
        pw.println("\rTESTING CONNECTION!");
        try {
            client.close();
            pw.close();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("An extender has disconnected");
    }

}
