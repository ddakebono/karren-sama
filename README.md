karren-sama - Master Branch
===========

Repo for the Karren-sama IRC bot as seen on the Redirect Gaming IRC

Hey look at that, I got a POM to work, we maven now.

Uses PircBotX for the core IRC functions.

Usage:

    Upon compilation and start up the bot will generate it's configuration file @ conf/bot.prop then exit, set the config and restart it.
    With the Interaction system as it sits it will not create the interactions file, manually do so. (conf/Interactions.txt)
        Interaction template is Interaction:[Identifier]:[Trigger words Ex: hi,hello,hey]:[Response template Ex:Hello %name]:[Tags for interaction Ex:Name]:[Confidence setting(This governs how many triggers need to be found before a choice is made)]
        You can have as many interactions as you want.
    Import .SQL to database of your choice.
        I will be compiling a SQL script to build it's database once I get this branch good and stable, maybe Maven at the same time.
    After this point the bot should be operational.

Working:

    Now playing - WORKING!
    User interaction system(New) - Appears to be fully working.
    SQL interaction - Stable at the current point.
    User away/return tracking - WORKING!

Latest Changes:
    
    Added the ability for the bot to tell how long a song is and give feedback on it.
    New logging system, actually outputs to a file now.
    Songs played are now logged.
    We maven now.
    
