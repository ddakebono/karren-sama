karren-sama - Broken Branch
===========

Repo for the Karren-sama IRC bot as seen on the Redirect Gaming IRC

Uses PircBotX for the core IRC functions.

Usage:

    Upon compilation and start up the bot will generate it's configuration file @ conf/bot.prop then exit, set the config and restart it.
    With the Interaction system as it sits it will not create the interactions file, manually do so. (conf/Interactions.txt)
        Interaction template is Interaction:[Identifier]:[Trigger words Ex: hi,hello,hey]:[Response template Ex:Hello %name]:[Tags for interaction Ex:Name]:[Confidence setting(This governs how many triggers need to be found before a choice is made)]
        You can have as many interactions as you want.
    Import .SQL to database of your choice.
        I will be compiling a SQL script to build it's database once I get this branch good and stable, maybe Maven at the same time.
    After this point the bot should be operational.

This branch of the bot is hardly working, it will run, it will try to do things, then it will prolly have an error and die.

Working:

    Now playing - Sorta
    User interaction system(New) - Operational with some changes needed.
    SQL interaction - Plagued by numerous issues.
    User away/return tracking - Working as intended(I think)

Dependencies(2lazy4maven):

    PircBotX.jar
    Apache Common Lang
    Apache Common codec
    Google guava
    slf4j api and simple
    MySQL java connector library
    Apache HTTP Client
    Apache HTTP Core
    Apache Tika
