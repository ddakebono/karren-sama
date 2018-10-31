/*
 * Copyright (c) 2018 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren;

import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.security.cert.X509Certificate;
import java.util.*;

public class KarrenUtil {

    public static final String[] heroList = {"Reinhardt", "Tracer", "Zenyatta", "Junkrat", "Mccree", "Winston", "Orisa", "Hanzo", "Pharah", "Roadhog", "Zarya", "Torbjorn", "Mercy", "Soldier76", "Ana", "Widowmaker", "Genji", "Reaper", "Mei", "Bastion", "Symmetra", "Dva", "Sombra", "Lucio"};
    public static final TrustManager[] trustAllCertificates = new TrustManager[]{
            new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null; // Not relevant.
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    // Do nothing. Just allow them all.
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    // Do nothing. Just allow them all.
                }
            }
    };

    public static <T> T instantiate(final String className, final Class<T> type){
        try{
            return type.cast(Class.forName(className).newInstance());
        } catch(InstantiationException
                | IllegalAccessException
                | ClassNotFoundException e){
            throw new IllegalStateException(e);
        }
    }

    @SuppressWarnings("ConstantConditions")
    public static File[] getFilesInFolders(File directory){
        ArrayList<File> files = new ArrayList<>();
        if(directory.isDirectory()){
            for(File file : directory.listFiles()){
                if(file.isDirectory())
                    Collections.addAll(files, getFilesInFolders(file));
                else
                    files.add(file);
            }
            return files.toArray(new File[0]);
        } else {
            return files.toArray(new File[0]);
        }
    }

    public static String getVRCSafetySystemRank(List<String> tags){
        LinkedHashMap<String, String> permMap = new LinkedHashMap<>();
        permMap.put("system_avatar_access", "User");
        permMap.put("system_trust_known", "User");
        permMap.put("system_trust_trusted", "Known User");
        permMap.put("system_trust_veteran", "Trusted User");
        String permission = "Visitor/New User (No Tags)";
        int permLevel = -1;
        for(String tag : tags){
            String level = permMap.getOrDefault(tag, null);
            if(level!=null){
                int index = new ArrayList<>(permMap.values()).indexOf(level);
                if(index>permLevel){
                    permission = level;
                    permLevel = index;
                }
            }
        }
        return permission;
    }

    public static String getNormalizedName(String jsonName){
        switch(jsonName){
            case "sound_barriers_provided":
                return "Sound Barriers Provided";
            case "players_resurrected":
                return "Players Revived";
            case "damage_done_most_in_game":
                return "Highest Damage Done in Game";
            case "games_played":
                return "Games Played";
            case "deaths":
                return "Deaths";
            case "medals_gold":
                return "Gold Medals Earned";
            case "weapon_accuracy":
                return "Weapon Accuracy";
            case "healing_done":
                return "Total Healing";
            case "healing_done_most_in_game":
                return "Highest Healing Done in a Game";
            case "damage_done":
                return "Total Damage";
            case "eliminations":
                return "Total Eliminations";
            case "earthshatter_kills":
                return "Earthshatter Kills";
            case "nano_boosts_applied":
                return "Nano Boosts Used";
            case "pulse_bomb_kills":
                return "Pulse Bomb Kills";
            case "blizzard_kills":
                return "Blizzard Kills";
            case "transcendence_healing_best":
                return "Best Transcendence Healing";
            case "rip_tire_kills":
                return "Rip-Tire Kills";
            case "deadeye_kills":
                return "Kills with Deadeye";
            case "melee_kills":
                return "Melee Kills";
            case "damage_amplified":
                return "Damage Amplified";
            case "tank_kills":
                return "Tank Kills";
            case "players_teleported":
                return "Players Teleported";
            case "shields_provided":
                return "Shields Provided";
            case "dragonstrike_kills":
                return "Dragonstrike Kills";
            case "barrage_kills":
                return "Rocket Barrage Kills";
            case "self_destruct_kills":
                return "Self Destruct Kills";
            case "lifetime_graviton_surge_kills":
                return "Total Graviton Surge Kills";
            case "recon_assists_most_in_game":
                return "Highest Infra-Sight Assists in a Game";
            case "enemies_empd":
                return "Total Players EMP'd";
            case "turret_kills":
                return "Turret Kills";
            case "tactical_visor_kills":
                return "Tactical Visor Kills";
            case "death_blossom_kills":
                return "Death Blossom Kills";
            case "dragonblade_kills":
                return "Dragonblade Kills";
            case "whole_hog_kills":
                return "Whole Hog Kills";
            default:
                return null;
        }
    }

    /*public static boolean hasRole(IUser user, IGuild guild, String roleName) {
        return guild == null || roleName == null || user.getRolesForGuild(guild).stream().anyMatch(x -> x.getName().equals(roleName));
    }*/

    public static String calcAway(long leaveDate){
        String backTime;
        long diffTime;
        long seconds;
        long minutes = 0;
        long hours = 0;
        long days = 0;
        Date date = new Date();
        diffTime = date.getTime()-leaveDate;
        seconds = diffTime/1000;
        if(seconds>=60){
            minutes = seconds/60;
            seconds = seconds-(minutes*60);
        }
        if(minutes>=60){
            hours = minutes/60;
            minutes = minutes-(hours*60);
        }
        if(hours>=24){
            days = hours/24;
            hours = hours-(days*24);
        }
        backTime = days + " Days, " + hours + " Hours, " + minutes + " Minutes, and " + seconds + " Seconds";
        return backTime;
    }

    public static String calcTimeDiff(long diff1, long diff2){
        String backTime;
        long diffTime;
        long seconds;
        long minutes = 0;
        long hours = 0;
        long days = 0;
        Date date = new Date();
        diffTime = diff1 - diff2;
        seconds = diffTime/1000;
        if(seconds>=60){
            minutes = seconds/60;
            seconds = seconds-(minutes*60);
        }
        if(minutes>=60){
            hours = minutes/60;
            minutes = minutes-(hours*60);
        }
        if(hours>=24){
            days = hours/24;
            hours = hours-(days*24);
        }
        backTime = days + " Days, " + hours + " Hr, " + minutes + " Min, " + seconds + " Sec";
        return backTime;
    }

    public static String getMinSecFormattedString(long time){
        String result;
        long seconds = time/1000;
        long minutes = 0;
        if(seconds/60>=1) {
            minutes = seconds / 60;
            seconds = seconds - (minutes*60);
        }
        if(time!=0 && seconds<10) {
            result = minutes + ":" + "0" + seconds;
        } else {
            result = minutes + ":" + seconds;
        }
        return result;
    }
}
