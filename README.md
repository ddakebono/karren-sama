Karren-sama Discord Bot
==========================

Description
-----------

This is my Discord bot I build using the Discord4J library, it's somewhat tied into a database I haven't released yet so some features may not be usable.

If you're interested there is also a branch on here that contains a version using Javacord as the library, both branches are close to the same feature wise, but the Javacord one will not be worked on unless Discord4J dies or otherwise stops working.


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
You will also need to have an SQL server to use the bot, it's only been tested against a MariaDB server, but you should just need to change the connector used as long as the DB server is compatable with JOOQ


Usage
-----

Check out our wiki for instructions on adding the bot to your discord!


Licence
-------

All of my code is licenced under the MIT licence<br/>
<a href="http://opensource.org/licenses/MIT">http://opensource.org/licenses/MIT</a>
