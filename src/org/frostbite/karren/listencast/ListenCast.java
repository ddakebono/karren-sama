package org.frostbite.karren.listencast;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.frostbite.karren.GlobalVars;
import org.frostbite.karren.Logging;
import org.frostbite.karren.MySQLConnector;
import org.pircbotx.PircBotX;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ListenCast extends Thread{
	public static PircBotX bot;
	public ListenCast(PircBotX bot) {
		this.bot = bot;
	}
	public void run(){
		String[] data = new String[0];
		String npTemp = "offair";
		while(true){
			try {
				ArrayList<String> resultSet = MySQLConnector.sqlPush("radio", "getSong", data);
				npTemp = resultSet.get(0);
			} catch (IOException | SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if(!npTemp.equalsIgnoreCase(GlobalVars.npSong)){
				GlobalVars.npSong = npTemp;
				onSongChange();
			}
			try {
				updateIcecastInfo();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public static void onSongChange(){
		String[] data = new String[0];
		try {
			MySQLConnector.sqlPush("radio", "song", data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			SongDatabase.onSongChange(GlobalVars.npSong);
		} catch (IOException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(GlobalVars.loop){
			bot.sendIRC().message(GlobalVars.channel, "Now playing: \"" + GlobalVars.npSong + "\" On CRaZyRADIO ("+ GlobalVars.iceStreamTitle +"). Listeners: " + GlobalVars.iceListeners + "/" + GlobalVars.iceMaxListeners);
		}
	}
	public static void updateIcecastInfo() throws IOException, SQLException, ParserConfigurationException, IllegalStateException, SAXException{
		String[] dataToSql = new String[1];
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		CredentialsProvider clientCreds = new BasicCredentialsProvider();
		clientCreds.setCredentials(new AuthScope(GlobalVars.icecastHost, Integer.parseInt(GlobalVars.icecastPort)), new UsernamePasswordCredentials(GlobalVars.icecastAdminUsername, GlobalVars.icecastAdminPass));
		CloseableHttpClient httpClient = HttpClients.custom().setDefaultCredentialsProvider(clientCreds).build();
		try{
			HttpGet httpGet = new HttpGet("http://" + GlobalVars.icecastHost + ":" + GlobalVars.icecastPort + "/admin/stats.xml");
			CloseableHttpResponse result = httpClient.execute(httpGet);
			try{
				HttpEntity entity = result.getEntity();
				Document stats = dBuilder.parse(entity.getContent());
				NodeList sources = stats.getElementsByTagName("source");
				for(int i=0; i<sources.getLength(); i++){
					Node sourceData = sources.item(i);
					if(sourceData.getNodeType() == Node.ELEMENT_NODE){
						Element data = (Element)sourceData;
						if(data.getAttribute("mount").equalsIgnoreCase("/" + GlobalVars.icecastMount)){
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
}
