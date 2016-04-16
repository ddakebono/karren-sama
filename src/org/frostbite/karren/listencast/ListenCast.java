/*
 * Copyright (c) 2016 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.listencast;

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
import org.frostbite.karren.Database.Models.tables.records.FavoritesRecord;
import org.frostbite.karren.Karren;
import org.jooq.Result;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.HTTP429Exception;
import sx.blah.discord.util.MissingPermissionsException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

public class ListenCast extends Thread{
	private IDiscordClient bot;
    private boolean nowPlaying = true;
    private String iceDJ;
    private String lastDJ = "";
    private String lastStreamTitle = "";
    private String iceStreamTitle;
    private int iceListeners = 0;
    private String iceMaxListeners;
    private String artist;
    private String title;
    private Song currentSong;
    private boolean doUpdate;
	private boolean killListencast;
	public ListenCast(IDiscordClient bot) {
        nowPlaying = Boolean.parseBoolean(Karren.conf.getListencastAnnounce());
        this.bot = bot;
    }

	public void run(){
        Song songTemp;
        killListencast = false;
        while(!killListencast) {
            doUpdate = true;
            try {
                updateIcecastInfo();
                if(title!=null && artist !=null) {
                    songTemp = new Song(artist + " - " + title);
                } else if(title!=null){
                    songTemp = new Song(title);
                } else {
                    songTemp = new Song("Off-air");
                }
            } catch (IOException e) {
                songTemp = new Song("Off-air");
                iceDJ = "";
                Karren.log.info("Stream seems to have shutdown.");
                doUpdate = false;
            } catch (StringIndexOutOfBoundsException | SQLException | ParserConfigurationException | SAXException e1) {
                songTemp = new Song("Error encountered when parsing song info!");
                Karren.log.error("Bad info in song data, couldn't parse song title!");
                doUpdate = false;
            }

            if(iceDJ != null && iceDJ.equalsIgnoreCase("")){
                songTemp = new Song("Off-air");
                doUpdate = false;
            }
            if (currentSong == null || !songTemp.getSongName().equalsIgnoreCase(currentSong.getSongName())) {
                Song lastSong = currentSong;
                currentSong = songTemp;
                if(lastSong != null) {
                    lastSong.songEnded();
                    Karren.log.debug("The last song played for " + lastSong.getSongDuration());
                }
                onSongChange();
            }
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Karren.log.info("Listencast Thread Terminated!");
	}
	private void onSongChange(){
        try {
            if(doUpdate)
                currentSong.getFieldsFromSQL();
            if(!lastDJ.equalsIgnoreCase(iceDJ) || !lastStreamTitle.equalsIgnoreCase(iceStreamTitle)){
                Karren.bot.getSql().updateDJActivity(iceDJ, iceStreamTitle);
                lastDJ=iceDJ;
                lastStreamTitle=iceStreamTitle;
                if(iceDJ!=null&&iceDJ.length()>0)
                    bot.getChannelByID(Karren.conf.getStreamAnnounceChannel()).changeTopic("Stream is LIVE. Current DJ is " + lastDJ + " and the current show is " + lastStreamTitle);
                else
                    bot.getChannelByID(Karren.conf.getStreamAnnounceChannel()).changeTopic("Stream is off air.");

            }
        } catch (DiscordException | MissingPermissionsException | HTTP429Exception e) {
            e.printStackTrace();
        }
        try {
            writeToSongLog(currentSong);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Karren.log.debug("Song \"" + currentSong.getSongName() + "\" duration lock: " + Boolean.toString(currentSong.isDurationLocked()));
        if(nowPlaying && Boolean.parseBoolean(Karren.conf.getConnectToDiscord()) && Boolean.parseBoolean(Karren.conf.getEnableListencast())){
            try {
                bot.getChannelByID(Karren.conf.getStreamAnnounceChannel()).sendMessage(getNowPlayingStr());
            } catch (MissingPermissionsException | HTTP429Exception | DiscordException e) {
                e.printStackTrace();
            }
            try {
                alertFaves();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
	}
    private void alertFaves() throws SQLException {
        Result<FavoritesRecord> returned = Karren.bot.getSql().getUserFaves(currentSong.getSongID());
        for(FavoritesRecord user : returned){
            try {
                Karren.bot.getClient().getOrCreatePMChannel(Karren.bot.getClient().getUserByID(user.getUserid().toString())).sendMessage(currentSong.getSongName() + " has started playing!");
            } catch (DiscordException | HTTP429Exception | MissingPermissionsException e) {
                e.printStackTrace();
            }
        }
}
    private String getNowPlayingStr(){
        if(!currentSong.getSongName().equalsIgnoreCase("off-air"))
            return "```Now Playing on " + iceStreamTitle + ":\n\"" + currentSong.getSongName() + "\"\nListeners: " + iceListeners + "/" + iceMaxListeners + " | Last Played: " + currentSong.getLastPlayed() + " | Faves: " + currentSong.getFavCount() + " | Plays " + currentSong.getPlayCount() + "```";
        else
            return "No Source connected, the stream is offline.";
    }
	private void updateIcecastInfo() throws IOException, SQLException, ParserConfigurationException, IllegalStateException, SAXException {
        boolean onair = false;
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		CredentialsProvider clientCreds = new BasicCredentialsProvider();
		clientCreds.setCredentials(new AuthScope(new HttpHost(Karren.conf.getIcecastHost(), Integer.parseInt(Karren.conf.getIcecastPort()))), new UsernamePasswordCredentials(Karren.conf.getIcecastAdminUsername(), Karren.conf.getIcecastAdminPass()));
        try (CloseableHttpClient httpClient = HttpClients.custom().setDefaultCredentialsProvider(clientCreds).build()) {
            HttpGet httpGet = new HttpGet("http://" + Karren.conf.getIcecastHost() + ":" + Karren.conf.getIcecastPort() + "/admin/stats.xml");
            try (CloseableHttpResponse result = httpClient.execute(httpGet)) {
                HttpEntity entity = result.getEntity();
                Document stats = dBuilder.parse(entity.getContent());
                NodeList sources = stats.getElementsByTagName("source");
                for (int i = 0; i < sources.getLength(); i++) {
                    Node sourceData = sources.item(i);
                    if (sourceData.getNodeType() == Node.ELEMENT_NODE) {
                        Element data = (Element) sourceData;
                        if (data.getAttribute("mount").equalsIgnoreCase("/" + Karren.conf.getIcecastMount()) && data.getElementsByTagName("server_url").item(0) !=null) {
                            onair = true;
                            iceDJ = data.getElementsByTagName("server_description").item(0).getTextContent();
                            iceListeners = Integer.parseInt(data.getElementsByTagName("listeners").item(0).getTextContent());
                            iceMaxListeners = data.getElementsByTagName("max_listeners").item(0).getTextContent();
                            iceStreamTitle = data.getElementsByTagName("server_name").item(0).getTextContent();
                            if(data.getElementsByTagName("title").item(0)!=null)
                                title = data.getElementsByTagName("title").item(0).getTextContent();
                            else
                                onair = false;
                            NodeList artistElement = data.getElementsByTagName("artist");
                            if(artistElement.item(0)!=null)
                                artist = artistElement.item(0).getTextContent();
                            else
                                artist = null;
                        }
                    }
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
                Karren.log.debug("NPE Occurred at XML processing. Raw Title Line " + title + " Raw Artist Line " + artist);
            }
        }
		if(!onair){
            iceDJ = "";
        }
	}
    public void kill(){
        killListencast = true;
    }
    public String getIceDJ(){
        if(iceDJ.length()>0)
            return iceDJ;
        return "Nobody";
    }
    public Song getSong(){return currentSong;}
    public String getIceStreamTitle(){return iceStreamTitle;}
    public String getIceMaxListeners(){return iceMaxListeners;}
    public int getIceListeners(){return iceListeners;}
    private void writeToSongLog(Song curSong) throws IOException {
        BufferedWriter songLog = new BufferedWriter(new FileWriter("logs/songs.log", true));
        if(curSong.getSongID() != 0) {
            songLog.append(curSong.getSongName()).append(":").append(String.valueOf(curSong.getSongID())).append("\n");
            songLog.close();
        }
    }
    public boolean enableNP(){
        nowPlaying = !nowPlaying;
        return nowPlaying;
    }
    public long getSongCurTime(){
        long result;
        Date date = new Date();
        result = date.getTime() - currentSong.getSongStartTime();
        return result;
    }
    public String getMinSecFormattedString(long time){
        String result;
        long seconds = time/1000;
        long minutes = 0;
        if(seconds/60>=1) {
            minutes = seconds / 60;
            seconds = seconds - (minutes*60);
        }
        if(time!=0 && seconds<10) {
            result = minutes + ":" + "0" + seconds;
        } else {
            result = minutes + ":" + seconds;
        }
        return result;
    }
}
