Karren-sama Discord Bot
==========================

Description
-----------

This is my Discord bot I build using the Discord4J library.

If you're interested there is also a branch on here that contains a version using Javacord as the library, the Javacord branch has been stale for a while now and probably won't work at all.


Features
--------

There isn't too many and functionality may change suddenly but here we go.<br/>
* Json based Interaction system.<br/>
* Advanced meme playback in voice channels<br/>
* Reminders
* Rolling

Compiling
---------

This bot was developed using Java 8, so make sure you have the latest Java 8 SDK installed.
I have a handy maven file included that will resolve all the dependencies and get you a usable jar.

Dependencies
------------

To use this bot you will need the Java 8 runtime, the rest of the dependencies will be handled by Maven.
You will also need to have an SQL server to use the bot, it's only been tested against a MariaDB server, but you should just need to change the connector used as long as the DB server is compatable with JOOQ.
You can find the SQL file in the conf folder


Usage
-----

Check out our wiki for instructions on adding the bot to your discord!

Documentation will be up sometime on how to run your own instance of the bot.


Licence
-------

All of my code is licenced under the MIT licence<br/>
<a href="http://opensource.org/licenses/MIT">http://opensource.org/licenses/MIT</a>
