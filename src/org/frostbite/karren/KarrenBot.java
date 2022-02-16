/*
 * Copyright (c) 2022 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeHttpContextFilter;
import com.sedmelluq.discord.lavaplayer.track.playback.NonAllocatingAudioFrameBuffer;
import io.github.vrchatapi.ApiClient;
import io.github.vrchatapi.ApiException;
import io.github.vrchatapi.api.AuthenticationApi;
import io.github.vrchatapi.api.SystemApi;
import io.github.vrchatapi.api.UsersApi;
import io.github.vrchatapi.api.WorldsApi;
import io.github.vrchatapi.auth.HttpBasicAuth;
import io.github.vrchatapi.model.CurrentUser;
import io.github.vrchatapi.util.SimpleOkHttpCookieJar;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import okhttp3.*;
import org.apache.http.client.config.RequestConfig;
import org.frostbite.karren.AudioPlayer.GuildMusicManager;
import org.frostbite.karren.Database.MySQLInterface;
import org.frostbite.karren.listeners.*;
import org.jetbrains.annotations.Nullable;
import org.knowm.yank.Yank;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.auth.login.LoginException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static org.frostbite.karren.Karren.conf;

public class KarrenBot {
    public JDABuilder clientBuilder;
    public JDA client;
    public MySQLInterface sql = new MySQLInterface();
    public boolean extrasReady = false;
    public Map<Long, GuildMusicManager> gms;
    public HashMap<Long, Boolean> departedUsers = new HashMap<>();
    public GuildManager ic;
    public boolean isKill = false;
    public YouTube yt;
    public AudioPlayerManager pm;
    public AutoInteraction ar = new AutoInteraction();
    public ChannelMonitor cm = new ChannelMonitor();
    private ApiClient defaultClient;
    private UsersApi usersApi;
    private SystemApi systemApi;
    private WorldsApi worldsApi;
    private CurrentUser vrcUser;
    private AuthenticationApi authApi;

    public KarrenBot(JDABuilder clientBuilder) {
        this.clientBuilder = clientBuilder;
    }

    public void initDiscord() {
        Karren.log.info("Starting up Lavaplayer...");
        gms = new HashMap<>();
        pm = new DefaultAudioPlayerManager();
        pm.getConfiguration().setFrameBufferFactory(NonAllocatingAudioFrameBuffer::new);
        pm.setHttpRequestConfigurator(requestConfig -> RequestConfig.copy(requestConfig).setSocketTimeout(10000).setConnectTimeout(10000).build());
        AudioSourceManagers.registerRemoteSources(pm);
        AudioSourceManagers.registerLocalSource(pm);
        //Continue connecting to discord
        if (Karren.conf.getConnectToDiscord()) {
            clientBuilder.addEventListeners(new ConnectCommand());
            clientBuilder.addEventListeners(new KillCommand());
            clientBuilder.addEventListeners(new ReconnectListener());
            clientBuilder.addEventListeners(new ShutdownListener());
            clientBuilder.addEventListeners(new ResumeListener());
            clientBuilder.addEventListeners(new StatCommand());
            clientBuilder.addEventListeners(new VoiceLeaveListener());
            if (Karren.conf.getEnableInteractions())
                clientBuilder.addEventListeners(new HelpListener());
            clientBuilder.addEventListeners(new InteractionCommand());
            clientBuilder.addEventListeners(new GuildCreateListener());
            initExtras();
            try {
                client = clientBuilder.build();
            } catch (LoginException e) {
                e.printStackTrace();
            }
        } else {
            Karren.log.info("Running in test mode, not connected to Discord.");
            initExtras();
            //Init interaction processor

        }
    }

    public void initExtras() {
        if (!extrasReady) {
            ic = new GuildManager();
            ic.loadDefaultInteractions();

            //Setup youtube
            yt = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), request -> {
            }).setApplicationName("Karren-sama").build();

            YoutubeHttpContextFilter.setPAPISID(conf.getYoutubePAPISID());
            YoutubeHttpContextFilter.setPSID(conf.getYoutube3PSID());

            //Log into VRCAPI and get auth token
            if (!KarrenUtil.stringIsNullEmptyWhitespaceCheck(conf.getVrcUsername()) && !KarrenUtil.stringIsNullEmptyWhitespaceCheck(conf.getVrcPassword()) && !KarrenUtil.stringIsNullEmptyWhitespaceCheck(conf.getVrcBasePath()) && conf.isVrcEnable()) {

                CookieJar cookieJar = new SimpleOkHttpCookieJar();
                OkHttpClient.Builder client = new OkHttpClient.Builder()
                        .connectTimeout(60, TimeUnit.SECONDS)
                        .writeTimeout(60, TimeUnit.SECONDS)
                        .readTimeout(60, TimeUnit.SECONDS)
                        .cookieJar(cookieJar);

                if (!KarrenUtil.stringIsNullEmptyWhitespaceCheck(conf.getProxyServer())) {
                    Authenticator proxyAuthenticator = new Authenticator() {
                        @Override
                        public Request authenticate(@Nullable Route route, okhttp3.Response response) {
                            String credential = Credentials.basic(conf.getProxyUsername(), conf.getProxyPassword());
                            return response.request().newBuilder()
                                    .header("Proxy-Authorization", credential)
                                    .build();
                        }
                    };

                    client.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(conf.getProxyServer(), conf.getProxyPort()))).proxyAuthenticator(proxyAuthenticator);
                }

                TrustManager[] trustAllCerts = new TrustManager[]{
                        new X509TrustManager() {
                            @Override
                            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                            }

                            @Override
                            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                            }

                            @Override
                            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                return new java.security.cert.X509Certificate[]{};
                            }
                        }
                };

                SSLContext sslContext = null;

                try {
                    sslContext = SSLContext.getInstance("SSL");
                    sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

                    client.sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCerts[0]);
                    client.hostnameVerifier((hostname, session) -> true);
                } catch (NoSuchAlgorithmException | KeyManagementException e) {
                    e.printStackTrace();
                }

                OkHttpClient clientFinal = client.build();

                defaultClient = new ApiClient(clientFinal);
                defaultClient.setBasePath(conf.getVrcBasePath());

                systemApi = new SystemApi(defaultClient);
                authApi = new AuthenticationApi(defaultClient);

                HttpBasicAuth authHeader = (HttpBasicAuth) defaultClient.getAuthentication("authHeader");
                authHeader.setUsername(conf.getVrcUsername());
                authHeader.setPassword(conf.getVrcPassword());

                try {
                    vrcUser = authApi.getCurrentUser();
                    usersApi = new UsersApi(defaultClient);
                    worldsApi = new WorldsApi(defaultClient);
                    Karren.log.info("Successfully logged into the VRCAPI as " + vrcUser.getDisplayName() + "!");
                } catch (ApiException e) {
                    Karren.log.error("Exception during VRCAPI Login! Status Code: " + e.getCode());
                    Karren.log.error("Reason: " + e.getResponseBody());
                    Karren.log.error("Response Headers: " + e.getResponseHeaders());
                    e.printStackTrace();
                }
            }
        }

        extrasReady = true;
    }

    public void killBot(String killer) {
        Karren.bot.isKill = true;
        //Unhook and shutdown interaction system
        Yank.releaseAllConnectionPools();
        //Interactions reset to default state and unregistered
        client.shutdown();
        Karren.bot = null;
        System.gc();
        Karren.log.info("Bot has been killed by " + killer);
        System.exit(0);
    }

    public void onConnectStartup() {
        //Initialize database connection pool
        Karren.log.info("Initializing Yank database pool");
        Properties dbSettings = new Properties();

        dbSettings.setProperty("jdbcUrl", "jdbc:mysql://" + conf.getSqlhost() + ":" + conf.getSqlport() + "/" + conf.getSqldb() + "?useUnicode=true&characterEncoding=UTF-8");
        dbSettings.setProperty("username", conf.getSqluser());
        dbSettings.setProperty("password", conf.getSqlpass());

        if (Karren.conf.getAllowSQLRW())
            Yank.setupDefaultConnectionPool(dbSettings);

        //Launch threads
        Karren.bot.ar.start();
        Karren.bot.cm.start();

        if (!Karren.conf.isTestMode()) {
            if (Karren.conf.getStatusOverride().isEmpty())
                client.getPresence().setActivity(Activity.playing("KarrenSama Ver." + Karren.botVersion));
            else
                client.getPresence().setActivity(Activity.playing(Karren.conf.getStatusOverride()));
        } else {
            client.getPresence().setActivity(Activity.playing("TEST MODE - " + Karren.botVersion));
        }
    }

    public void refreshCurrentUser() {
        try {
            vrcUser = authApi.getCurrentUser();
        } catch (ApiException e) {
            Karren.log.error("Exception during VRCAPI Login! Status Code: " + e.getCode());
            Karren.log.error("Reason: " + e.getResponseBody());
            Karren.log.error("Response Headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }

    /*
    GETTERS
     */

    public boolean isKill() {
        return isKill;
    }

    public MySQLInterface getSql() {
        return sql;
    }

    public GuildManager getGuildManager() {
        return ic;
    }

    public JDA getClient() {
        return client;
    }

    public boolean isExtrasReady() {
        return extrasReady;
    }

    public GuildMusicManager getGuildMusicManager(Guild guild) {
        return gms.get(guild.getIdLong());
    }

    public void createGuildMusicManager(Guild guild) {
        if (!gms.containsKey(guild.getIdLong())) {
            gms.put(guild.getIdLong(), new GuildMusicManager(pm, guild));
            guild.getAudioManager().setSendingHandler(getGuildMusicManager(guild).getAudioProvider());
        }
    }

    public AudioPlayerManager getPm() {
        return pm;
    }

    public AutoInteraction getAr() {
        return ar;
    }

    public ChannelMonitor getCm() {
        return cm;
    }

    public UsersApi getUsersApi() {
        return usersApi;
    }

    public SystemApi getSystemApi() {
        return systemApi;
    }

    public WorldsApi getWorldsApi() {
        return worldsApi;
    }

    public CurrentUser getVrcUser() {
        return vrcUser;
    }

    public AuthenticationApi getAuthApi() {
        return authApi;
    }
}
