Karren-sama Discord Bot
==========================

Description
-----------

This is my Discord bot I build using the Discord4J library, it's somewhat tied into a database I haven't released yet so some features may not be usable, the main one is the Icecast listener, it will work without it but favorites, plays, and other information will be lost.

If you're interested there is also a branch on here that contains a version using Javacord as the library, both branches are close to the same feature wise, but the Javacord one will not be worked on unless Discord4J dies or otherwise stops working.


Features
--------

There isn't too many and functionality may change suddenly but here we go.<br/>
* Json based Interaction system.<br/>
* Simple voice channel support.<br/>

Compiling
---------

This bot was developed using Java 8, so make sure you have the latest Java 8 SDK installed.
I have a handy maven file included that will resolve all the dependencies and get you a usable jar.

Dependencies
------------

To use this bot you will need the Java 8 runtime.
If you wish to have Youtube playback in the voice channels you also need Youtube-dl and ffmpeg.


Usage
-----

To run the bot simply open a command prompt or terminal in it's directory and enter java -jar Karren-sama.jar.<br/>
It will populate it's directory with a conf folder that contains all the interactions and it's config file, bot.prop.<br/>
It will also add a log folder, which will contain songs.log and bot.log.<br/>

Once the bot has run once it will give you an empty config file something like this.

> sqluser=changeme<br/>
> allowSQLReadWrite=true<br/>
> enableInteractionSystem=true<br/>
> sqlpass=changeme<br/>
> streamAnnounceChannel=0<br/>
> sqlport=3306<br/>
> sqldb=changeme<br/>
> BotAccountToken=changeme<br/>
> icecastAdminUsername=changeme<br/>
> icecastMount=changeme.ogg<br/>
> sqlhost=0.0.0.0<br/>
> ExtenderListenAddress=127.0.0.1<br/>
> enableListencastSystem=true<br/>
> ExtenderListenPort=8281<br/>
> icecastPort=8000<br/>
> GuildID=0<br/>
> commandPrefix=.<br/>
> connectToDiscord=true<br/>
> icecastHost=0.0.0.0<br/>
> icecastAdminPass=changeme<br/>
> karrenVersion=2.1-DISCORD<br/>

Change it to fit your needs, also I would recommend turning off SQL as I haven't released my tables that power it.<br/>
Now playing will work properly without an SQL, you just lose play counts, faves and last played data.<br/>
GuildID is the ID of your general channel on your discord.<br/>
StreamAnnounceChannel is the channel you want to get spammed by now playing information.<br/>
BotAccountToken is the bot you will receive once you create a bot account on Discord.<br/>

Interactions
------------

I'm quite happy with the way they work now, I'll be writing a good creation guide into a wiki page soon.<br/>
Please note that Tags and the way they work may be changed at any time, I just kinda think of a useful one and add it.<br/>


Licence
-------

All of my code is licenced under the MIT licence<br/>
<a href="http://opensource.org/licenses/MIT">http://opensource.org/licenses/MIT</a>
