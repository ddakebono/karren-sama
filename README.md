Karren-sama Discord Bot
==========================

Description
-----------

This is my Discord bot I build using the Discord4J library, it's very much tied in with a site I run so It's prolly not to useful for everybody.
There is still some very messy files as I've just started bringing it over to discord, as it used to be an IRC bot.

Features
--------

There isn't too many and functionality may change suddenly but here we go.
* Listening in on multiple servers, just invite it.
* Icecast now playing integration
* Interaction half assed scripting thing, it's bad, I'll be making it better soon.
* Pointless commands that just give dumb responses.

Usage
-----

Don't bother

But if you really want to it does work, somewhat.

Once the bot has run once it will give you an empty config file something like this.

>sqluser=changeme
>allowSQLReadWrite=true
>enableInteractionSystem=true
>sqlpass=changeme
>streamAnnounceChannel=0
>sqlport=3306
>sqldb=changeme
>nickservPass=changeme
>EmailAddressPassword=hackme
>icecastAdminUsername=changeme
>icecastMount=changeme.ogg
>sqlhost=0.0.0.0
>ExtenderListenAddress=127.0.0.1
>enableListencastSystem=true
>ExtenderListenPort=8281
>icecastPort=8000
>EmailAddress=changethis@emailaddress.bad
>GuildID=0
>commandPrefix=.
>connectToDiscord=true
>icecastHost=0.0.0.0
>icecastAdminPass=changeme
>karrenVersion=2.0-DISCORDTesting

Change it to fit your needs, also I would recommend turning off SQL as I haven't released my tables that power it.
Now playing will work properly without an SQL, you just lose play counts, faves and last played data.
GuildID is the ID of your general channel on your discord.
StreamAnnounceChannel is the channel you want to get spammed by now playing information.
EmailAddress is the one the bot will use to sign in.

Interactions
------------

Don't bother with this until I redo it.



Licence
-------

All of my code is licenced under the MIT licence
<a href="http://opensource.org/licenses/MIT">http://opensource.org/licenses/MIT</a>

