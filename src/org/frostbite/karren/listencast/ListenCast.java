package org.frostbite.karren.listencast;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;

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
		for(;;){
			try {
				InputStream input = new URL("http://" + GlobalVars.icecastHost + ":" + GlobalVars.icecastPort + "/" + GlobalVars.icecastMount).openStream();
				ContentHandler handler = new DefaultHandler();
				Metadata metadata = new Metadata();
				Parser parser = new AutoDetectParser();
				ParseContext parseCont = new ParseContext();
				parser.parse(input, handler, metadata, parseCont);
				input.close();
				if(!GlobalVars.npSong.equalsIgnoreCase(metadata.get("TITLE"))){
					GlobalVars.npSong = metadata.get("TITLE");
					Logging.song(GlobalVars.npSong);
					onSongChange();
				}
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TikaException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
		}
	}
	public static void onSongChange(){
		String[] data = new String[1];
		data[0] = GlobalVars.npSong;
		try {
			MySQLConnector.sqlPush("radio", "song", data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(GlobalVars.loop){
			bot.sendIRC().message(GlobalVars.channel, "Current DJ: " + GlobalVars.iceDJ + " Now playing: \"" + GlobalVars.npSong + "\" On CRaZyRADIO("+ GlobalVars.iceStreamTitle +"). Listeners: " + GlobalVars.iceListeners + "/" + GlobalVars.iceMaxListeners);
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
							GlobalVars.iceStreamTitle = dataToSql[0];
							MySQLConnector.sqlPush("radio", "dj", dataToSql);
							dataToSql[0] = data.getElementsByTagName("listeners").item(0).getTextContent();
							GlobalVars.iceListeners = Integer.parseInt(dataToSql[0]);
							GlobalVars.iceMaxListeners = Integer.parseInt(data.getElementsByTagName("max_listeners").item(0).getTextContent());
							MySQLConnector.sqlPush("radio", "listen", dataToSql);
							dataToSql[0] = data.getElementsByTagName("server_name").item(0).getTextContent();
							GlobalVars.iceDJ = dataToSql[0];
							MySQLConnector.sqlPush("radio", "title", dataToSql);
						}
					}
				}
			} finally {
				result.close();
			}
		} finally {
			httpClient.close();
		}
		
	}
}
