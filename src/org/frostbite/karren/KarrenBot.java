package org.frostbite.karren;

import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;

/**
 * Created by frostbite on 17/03/14.
 */
public class KarrenBot extends PircBotX {
    private String test = "test";
    public KarrenBot(Configuration<PircBotX> config){
        super(config);
    }
    public String getTestString(){return test;}
}