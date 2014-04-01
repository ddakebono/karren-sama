/*
 * Copyright 2014 ripxfrostbite
 * Coded by Owen Bennett for the CRaZyPANTS Server Network, released under the MIT Licence, we take no responsibility if something breaks when you change code.
 * If something breaks on the bot and you didn't change anything please log it as an issue so I can fix it.
 */

package org.frostbite.karren.listencast;

import com.google.common.collect.ImmutableSortedSet;
import org.frostbite.karren.BotConfiguration;
import org.frostbite.karren.KarrenBot;
import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
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
    private int iceMaxListeners = 0;
    private Song currentSong;
    private Song songTemp;
	private boolean killListencast = false;
	public ListenCast(PircBotX bot, BotConfiguration botConf) {
        if(bot instanceof KarrenBot)
		    this.bot = (KarrenBot)bot;
        icecastHost = (String)botConf.getConfigPayload("icecasthost");
        icecastPort = (String)botConf.getConfigPayload("icecastport");
        icecastMount = (String)botConf.getConfigPayload("icecastmount");
	}
	public void run(){
        IcyStreamMeta streamFile;
        try {
            streamFile = new IcyStreamMeta(new URL("http://"+icecastHost+":"+icecastPort+"/"+icecastMount));
            while(!killListencast){
                try {
                    streamFile.refreshMeta();
                    songTemp = new Song(streamFile.getArtist() + " - " + streamFile.getTitle());
                }catch(IOException e){
                    songTemp = new Song("Off-air");
                }
                if (currentSong == null || !songTemp.getSongName().equalsIgnoreCase(currentSong.getSongName())) {
                    currentSong = songTemp;
                    onSongChange();
                }
                //try {
                //   updateIcecastInfo();
                // } catch (IOException | SQLException | ParserConfigurationException | SAXException e) {
                //   e.printStackTrace();
                //}
                try {
		        Thread.sleep(1000);
			    } catch (InterruptedException e) {
				    // TODO Auto-generated catch block
				    e.printStackTrace();
			    }
		    }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
	}
	private void onSongChange(){
        try {
            bot.getSql().updateRadioPage(currentSong);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(nowPlaying){
			bot.sendIRC().message((String)(bot.getBotConf().getConfigPayload("channel")), getNowPlayingStr());
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
        return "Now playing: \"" + currentSong.getSongName() + "\" On CRaZyRADIO ("+ iceStreamTitle +"). Listeners: " + iceListeners + "/" + iceMaxListeners + ". This song was last played: " + currentSong.getLastPlayed() + ". Faves: " + currentSong.getFavCount() + ". Plays: " + currentSong.getPlayCount();
    }
	/*private void updateIcecastInfo() throws IOException, SQLException, ParserConfigurationException, IllegalStateException, SAXException{
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
							iceDJ = dataToSql[0];
							MySQLConnector.sqlPush("radio", "dj", dataToSql);
							dataToSql[0] = data.getElementsByTagName("listeners").item(0).getTextContent();
							iceListeners = Integer.parseInt(dataToSql[0]);
							iceMaxListeners = Integer.parseInt(data.getElementsByTagName("max_listeners").item(0).getTextContent());
							MySQLConnector.sqlPush("radio", "listen", dataToSql);
							dataToSql[0] = data.getElementsByTagName("server_name").item(0).getTextContent();
							iceStreamTitle = dataToSql[0];
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
		
	}*/
    public void kill(){
        killListencast = true;
    }
    public String getIceDJ(){return iceDJ;}
    public Song getSong(){return currentSong;}
    public String getIceStreamTitle(){return iceStreamTitle;}
    public int getIceListeners(){return iceListeners;}
    public boolean enableNP(Channel channel){
        announceChannel = channel;
        if(!nowPlaying)
            nowPlaying = true;
        else
            nowPlaying = false;
        return nowPlaying;
    }
}
