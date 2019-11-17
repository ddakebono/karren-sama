Karren-sama Discord Bot
==========================

Description
-----------

A random discord bot that I've put together over quite some time, currently V5 uses the JDA library and lavaplayer for audio stuff.
The feature of this bot kinda sorta appear when I feel like it, things may change at any time without warning.

Features
--------

There isn't too many and functionality may change suddenly but here we go.<br/>
* Json based Interaction system.<br/>
* Advanced meme playback in voice channels<br/>
* Reminders
* Rolling
* VRChat API interaction
* Other things I've forgotten lol

Compiling
---------

This bot was developed using Java 8, so make sure you have the latest Java 8 SDK installed.
I have a handy maven file included that will resolve all the dependencies and get you a usable jar.

Dependencies
------------

To use this bot you will need the Java 8 runtime, the rest of the dependencies will be handled by Maven.
You will also need to have an SQL server to use the bot, it's only been tested against a MariaDB server, but you should just need to change the connector used as long as the DB server is compatable with JOOQ.
You can find the SQL file in the conf folder, the SQL is prolly outdated, sorry about that.


Usage
-----

Check out our wiki for instructions on adding the bot to your discord!

Documentation will be up sometime on how to run your own instance of the bot.


Licence
-------

All of my code is licenced under the MIT licence<br/>
<a href="http://opensource.org/licenses/MIT">http://opensource.org/licenses/MIT</a>
