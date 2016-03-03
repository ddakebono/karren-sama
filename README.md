Karren-sama Discord Bot
==========================

Description
-----------

This is my Discord bot I build using the Discord4J library, it's very much tied in with a site I run so It's prolly not to useful for everybody.<br/>
There is still some very messy files as I've just started bringing it over to discord, as it used to be an IRC bot.

Features
--------

There isn't too many and functionality may change suddenly but here we go.<br/>
* Listening in on multiple servers, just invite it.<br/>
* Icecast now playing integration<br/>
* Interaction half assed scripting thing, it's bad, I'll be making it better soon.<br/>
* Pointless commands that just give dumb responses.<br/>

Usage
-----

Don't bother

But if you really want to it does work, somewhat.

Once the bot has run once it will give you an empty config file something like this.

> sqluser=changeme<br/>
> allowSQLReadWrite=true<br/>
> enableInteractionSystem=true<br/>
> sqlpass=changeme<br/>
> streamAnnounceChannel=0<br/>
> sqlport=3306<br/>
> sqldb=changeme<br/>
> nickservPass=changeme<br/>
> EmailAddressPassword=hackme<br/>
> icecastAdminUsername=changeme<br/>
> icecastMount=changeme.ogg<br/>
> sqlhost=0.0.0.0<br/>
> ExtenderListenAddress=127.0.0.1<br/>
> enableListencastSystem=true<br/>
> ExtenderListenPort=8281<br/>
> icecastPort=8000<br/>
> EmailAddress=changethis@emailaddress.bad<br/>
> GuildID=0<br/>
> commandPrefix=.<br/>
> connectToDiscord=true<br/>
> icecastHost=0.0.0.0<br/>
> icecastAdminPass=changeme<br/>
> karrenVersion=2.0-DISCORDTesting<br/>

Change it to fit your needs, also I would recommend turning off SQL as I haven't released my tables that power it.<br/>
Now playing will work properly without an SQL, you just lose play counts, faves and last played data.<br/>
GuildID is the ID of your general channel on your discord.<br/>
StreamAnnounceChannel is the channel you want to get spammed by now playing information.<br/>
EmailAddress is the one the bot will use to sign in.<br/>

Interactions
------------

Don't bother with this until I redo it.



Licence
-------

All of my code is licenced under the MIT licence<br/>
<a href="http://opensource.org/licenses/MIT">http://opensource.org/licenses/MIT</a>
