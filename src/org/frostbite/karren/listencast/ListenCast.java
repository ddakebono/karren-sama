/*
 * Copyright 2014 ripxfrostbite
 * Coded by Owen Bennett for the CRaZyPANTS Server Network, released under the MIT Licence, we take no responsibility if something breaks when you change code.
 * If something breaks on the bot and you didn't change anything please log it as an issue so I can fix it.
 */

package org.frostbite.karren.listencast;

import com.google.common.collect.ImmutableSortedSet;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.frostbite.karren.BotConfiguration;
import org.frostbite.karren.KarrenBot;
import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.slf4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class ListenCast extends Thread{
	private KarrenBot bot;
    private String icecastHost;
    private String icecastPort;
    private String icecastMount;
    private boolean nowPlaying;
    private String iceDJ = "Testing";
    private String iceStreamTitle = "Testing";
    private int iceListeners = 0;
    private Channel announceChannel;
    private String iceMaxListeners;
    private String artist;
    private String title;
    private Song currentSong;
    private boolean doUpdate;
	private boolean killListencast = false;
    private Logger log;
	public ListenCast(PircBotX bot, BotConfiguration botConf, Logger log) {
        if(bot instanceof KarrenBot)
		    this.bot = (KarrenBot)bot;
        icecastHost = botConf.getIcecastHost();
        icecastPort = botConf.getIcecastPort();
        icecastMount = botConf.getIcecastMount();
        this.log = log;
	}
	public void run(){
        Song songTemp;
        while(!killListencast && bot.getBotConf().getEnableListencast().equalsIgnoreCase("true")) {
            doUpdate = true;
            try {
                updateIcecastInfo();
                songTemp = new Song(artist + " - " + title);
            } catch (IOException e) {
                songTemp = new Song("Off-air");
                log.info("Stream seems to have shutdown.");
                doUpdate = false;
            } catch (StringIndexOutOfBoundsException | SQLException | ParserConfigurationException | SAXException e1) {
                songTemp = new Song("Error encountered when parsing song info!");
                log.error("Bad info in song data, couldn't parse song title!");
                doUpdate = false;
            }
            if(iceDJ.equalsIgnoreCase("offline")){
                songTemp = new Song("Off-air");
                doUpdate = false;
            }
            if (currentSong == null || !songTemp.getSongName().equalsIgnoreCase(currentSong.getSongName())) {
                currentSong = songTemp;
                onSongChange();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
	}
	private void onSongChange(){
        try {
            if(doUpdate)
                bot.getSql().updateRadioDatabase(currentSong);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(nowPlaying){
			bot.sendIRC().message(bot.getBotConf().getChannel(), getNowPlayingStr());
            try {
                alertFaves();
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        }
	}
    private void alertFaves() throws IOException, SQLException{
        ArrayList<Object> returned;
        returned = bot.getSql().getUserFaves(currentSong);
        ImmutableSortedSet<User> chanUsers = announceChannel.getUsers();
        for(User user : chanUsers){
            if(returned.contains(user.getNick())){
                user.send().message(currentSong.getSongName() + " has started playing!");
            }
        }
    }
    public String getNowPlayingStr(){
        if(!currentSong.getSongName().equalsIgnoreCase("off-air"))
            return "Now playing: \"" + currentSong.getSongName() + "\" On CRaZyRADIO ("+ getIceStreamTitle() +"). Listeners: " + getIceListeners() + "/" + getIceMaxListeners() + ". This song was last played: " + currentSong.getLastPlayed() + ". Faves: " + currentSong.getFavCount() + ". Plays: " + currentSong.getPlayCount();
        else
            return "Source disconnected, the stream is now offline.";
    }
	private void updateIcecastInfo() throws IOException, SQLException, ParserConfigurationException, IllegalStateException, SAXException {
        boolean onair = false;
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		CredentialsProvider clientCreds = new BasicCredentialsProvider();
		clientCreds.setCredentials(new AuthScope(new HttpHost(icecastHost, Integer.parseInt(icecastPort))), new UsernamePasswordCredentials(bot.getBotConf().getIcecastAdminUsername(), bot.getBotConf().getIcecastAdminPass()));
        try (CloseableHttpClient httpClient = HttpClients.custom().setDefaultCredentialsProvider(clientCreds).build()) {
            HttpGet httpGet = new HttpGet("http://" + icecastHost + ":" + icecastPort + "/admin/stats.xml");
            try (CloseableHttpResponse result = httpClient.execute(httpGet)) {
                HttpEntity entity = result.getEntity();
                Document stats = dBuilder.parse(entity.getContent());
                NodeList sources = stats.getElementsByTagName("source");
                for (int i = 0; i < sources.getLength(); i++) {
                    Node sourceData = sources.item(i);
                    if (sourceData.getNodeType() == Node.ELEMENT_NODE) {
                        Element data = (Element) sourceData;
                        if (data.getAttribute("mount").equalsIgnoreCase("/" + icecastMount)) {
                            onair = true;
                            iceDJ = data.getElementsByTagName("server_description").item(0).getTextContent();
                            iceListeners = Integer.parseInt(data.getElementsByTagName("listeners").item(0).getTextContent());
                            iceMaxListeners = data.getElementsByTagName("max_listeners").item(0).getTextContent();
                            iceStreamTitle = data.getElementsByTagName("server_name").item(0).getTextContent();
                            artist = data.getElementsByTagName("artist").item(0).getTextContent();
                            title = data.getElementsByTagName("title").item(0).getTextContent();
                        }
                    }
                }
            } catch (NullPointerException e) {
                iceDJ = "Off-air";
                iceStreamTitle = "Offline";
            }
        }
		if(!onair){
            iceDJ = "offline";
        }
	}
    public void kill(){
        killListencast = true;
    }
    public String getIceDJ(){return iceDJ;}
    public Song getSong(){return currentSong;}
    public String getIceStreamTitle(){return iceStreamTitle;}
    public String getIceMaxListeners(){return iceMaxListeners;}
    public int getIceListeners(){return iceListeners;}
    public boolean enableNP(Channel channel){
        announceChannel = channel;
        nowPlaying = !nowPlaying;
        return nowPlaying;
    }
}
