/*
 * Copyright 2014 ripxfrostbite
 * Coded by Owen Bennett for the CRaZyPANTS Server Network, released under the MIT Licence, we take no responsibility if something breaks when you change code.
 * If something breaks on the bot and you didn't change anything please log it as an issue so I can fix it.
 */

package org.frostbite.karren.listencast;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

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
import org.frostbite.karren.*;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import sun.org.mozilla.javascript.tools.shell.Global;

public class ListenCast extends Thread{
	private static KarrenBot bot;
	private static boolean killListencast = false;
	public ListenCast(PircBotX bot) {
        if(bot instanceof KarrenBot)
		    ListenCast.bot = (KarrenBot)bot;
	}
	public void run(){
        IcyStreamMeta streamFile;
        try {
            streamFile = new IcyStreamMeta(new URL(bot.getBotConf().getConfigPayload("icecasthost")+":"+bot.getBotConf().getConfigPayload("icecastport")+"/"+bot.getBotConf().getConfigPayload("icecastmount")));
		    String npTemp = "offair";
		    while(!killListencast){
                streamFile.refreshMeta();
                npTemp = streamFile.getArtist() + " - " + streamFile.getTitle();
			    if(!npTemp.equalsIgnoreCase(GlobalVars.npSong)){
				    GlobalVars.npSong = npTemp;
				    onSongChange();
			    }
                try {
                    updateIcecastInfo();
                } catch (IOException | SQLException | ParserConfigurationException | SAXException e) {
                    e.printStackTrace();
                }
                try {
				    Thread.sleep(1000);
			    } catch (InterruptedException e) {
				    // TODO Auto-generated catch block
				    e.printStackTrace();
			    }
		    }
        } catch (IOException e) {
            killListencast = true;
            Logging.log("Bad stream host! Listencast thread died.", true);
            e.printStackTrace();
        }
	}
	private static void onSongChange(){
		String[] data = new String[0];
		try {
			MySQLConnector.sqlPush("radio", "song", data);
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
		try {
			GlobalVars.songChange = true;
			MySQLConnector.sqlPush("song", "", null);
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
		if(GlobalVars.loop){
			bot.sendIRC().message((String)(bot.getBotConf().getConfigPayload("channel")), "Now playing: \"" + GlobalVars.npSong + "\" On CRaZyRADIO ("+ GlobalVars.iceStreamTitle +"). Listeners: " + GlobalVars.iceListeners + "/" + GlobalVars.iceMaxListeners + ". This song was last played: " + GlobalVars.lpTime + ". Faves: " + GlobalVars.songFavCount + ". Plays: " + GlobalVars.songPlayedAmount);
            try {
                alertFaves();
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        }
	}
    private static void alertFaves() throws IOException, SQLException{
        ArrayList<String> returned;
        returned = MySQLConnector.sqlPush("fave", "", null);
        ImmutableSortedSet<User> chanUsers = GlobalVars.npChannel.getUsers();
        for(User user : chanUsers){
            if(returned.contains(user.getNick())){
                user.send().message(GlobalVars.npSong + " has started playing!");
            }
        }
    }
	private static void updateIcecastInfo() throws IOException, SQLException, ParserConfigurationException, IllegalStateException, SAXException{
		String[] dataToSql = new String[1];
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		CredentialsProvider clientCreds = new BasicCredentialsProvider();
		clientCreds.setCredentials(new AuthScope(new HttpHost((String)(bot.getBotConf().getConfigPayload("icecasthost")), (int)(bot.getBotConf().getConfigPayload("icecastport")))), new UsernamePasswordCredentials((String)(bot.getBotConf().getConfigPayload("icecastadminusername")), (String)(bot.getBotConf().getConfigPayload("icecastadminpass"))));
		CloseableHttpClient httpClient = HttpClients.custom().setDefaultCredentialsProvider(clientCreds).build();
		try{
			HttpGet httpGet = new HttpGet("http://" + (String)(bot.getBotConf().getConfigPayload("icecasthost")) + ":" + (int)(bot.getBotConf().getConfigPayload("icecastport")) + "/admin/stats.xml");
			CloseableHttpResponse result = httpClient.execute(httpGet);
			try{
				HttpEntity entity = result.getEntity();
				Document stats = dBuilder.parse(entity.getContent());
				NodeList sources = stats.getElementsByTagName("source");
				for(int i=0; i<sources.getLength(); i++){
					Node sourceData = sources.item(i);
					if(sourceData.getNodeType() == Node.ELEMENT_NODE){
						Element data = (Element)sourceData;
						if(data.getAttribute("mount").equalsIgnoreCase("/" + bot.getBotConf().getConfigPayload("icecastmount"))){
							dataToSql[0] = data.getElementsByTagName("server_description").item(0).getTextContent();
							GlobalVars.iceDJ = dataToSql[0];
							MySQLConnector.sqlPush("radio", "dj", dataToSql);
							dataToSql[0] = data.getElementsByTagName("listeners").item(0).getTextContent();
							GlobalVars.iceListeners = Integer.parseInt(dataToSql[0]);
							GlobalVars.iceMaxListeners = Integer.parseInt(data.getElementsByTagName("max_listeners").item(0).getTextContent());
							MySQLConnector.sqlPush("radio", "listen", dataToSql);
							dataToSql[0] = data.getElementsByTagName("server_name").item(0).getTextContent();
							GlobalVars.iceStreamTitle = dataToSql[0];
							MySQLConnector.sqlPush("radio", "title", dataToSql);
						}
					}
				}
			} catch(NullPointerException e) {
				dataToSql[0] = "Off-air";
				MySQLConnector.sqlPush("radio", "dj", dataToSql);
			} finally {
				result.close();
			}
		} finally {
			httpClient.close();
		}
		
	}
    public static void manualUpdate(String song){
        Logging.log("A Manual update of the now playing information has been triggered!", false);
        onSongChange();
        try {
            updateIcecastInfo();
        } catch (IOException | SQLException | SAXException | ParserConfigurationException e) {
            e.printStackTrace();
        }
    }
    public void kill(){
        killListencast = true;
    }
}
