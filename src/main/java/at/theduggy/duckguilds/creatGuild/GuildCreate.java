  /*DuckGuilds: a plugin for creating/managing guilds
    Copyright (C) 2021 Georg Kollegger (or TheDuggy/CoderTheDuggy)

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.*/

package at.theduggy.duckguilds.creatGuild;

import at.theduggy.duckguilds.Main;
import at.theduggy.duckguilds.config.GuildConfig;
import at.theduggy.duckguilds.logging.AutoLogger;
import at.theduggy.duckguilds.other.Utils;
import at.theduggy.duckguilds.storage.Storage;
import org.apache.log4j.Level;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

  public class GuildCreate {

    public static void createGuild(Player player, ChatColor color, String name, String tag, ChatColor tagColor) throws IOException, ParseException {
            if (name.length() <= 20) {
                if (!Utils.isPlayerInGuild(player)) {
                    if (!Utils.guildExists(name)) {
                        if (Utils.isStringReadyToUse(name)) {
                            if (Utils.isReadyForCreate(tag)) {
                                if (tag.length() <= 4) {
                                    if (GuildConfig.getMaxGuildSize()>0){
                                        Bukkit.getLogger().warning(String.valueOf(GuildConfig.getMaxGuildSize()));
                                        if (Main.getGuildCache().size()> GuildConfig.getMaxGuildSize()){
                                            addPlayerToTeamAndCreateFiles(player,color,name,tag,tagColor);
                                            AutoLogger.logMessage( player.getUniqueId() + "(" + player.getName() + ") created the guild " + name + "!%n Tag: " + tag + "%n Color: " + color + "%n tagColor: " + tag, Level.INFO);;
                                            player.sendMessage(Main.getPlayerCache().get(player.getUniqueId()).toString());
                                        }else {
                                            player.sendMessage(Main.prefix + ChatColor.RED + "The servers max guild-level was reached, which is " + ChatColor.YELLOW + GuildConfig.getMaxGuildSize() + ChatColor.RED + " and the amount of guilds on this server is " + ChatColor.YELLOW + Main.getGuildCache().size() + ChatColor.RED + " !" + " You can't create guilds till a minimum of 1 is deleted!");
                                        }
                                    }else {
                                        addPlayerToTeamAndCreateFiles(player,color,name,tag,tagColor);
                                    }
                                } else {
                                    player.sendMessage(Main.prefix + ChatColor.RED + "The tag is " + ChatColor.YELLOW + tag.length() + ChatColor.RED + " characters long, but can only be 4 characters long!");
                                }
                            } else {
                                player.sendMessage(Main.prefix + "The tag contains forbidden symbols!");
                            }
                        } else {
                            player.sendMessage(Main.prefix + ChatColor.RED + "Tha guild-name " + ChatColor.YELLOW + name + ChatColor.RED + " is invalid, because it contains other symbols than alphabetic characters or digits!");
                        }
                    } else {
                        Bukkit.getLogger().info(Main.getGuildCache().toString());
                        player.sendMessage(Main.prefix + ChatColor.RED + "Guild already exists!");
                    }
                }else {
                    player.sendMessage(Main.playerAlreadyInGuild);
                }
        }else {
                player.sendMessage(Main.prefix + ChatColor.RED + "The name of a guild can't be longer that 50 characters!");
            }
    }

    public static void addPlayerToTeamAndCreateFiles(Player player, ChatColor color, String name, String tag, ChatColor tagColor) throws IOException, ParseException {
        Team guild;
        try {
             guild = Main.getScoreboard().registerNewTeam(name);
        }catch (IllegalArgumentException e){
            guild = Main.getScoreboard().getTeam(name);
        }
        ArrayList<UUID> players = new ArrayList<>();
        players.add(player.getUniqueId());
        ArrayList<String> playersForFile = new ArrayList<>();
        playersForFile.add(player.getUniqueId().toString());
        guild.setSuffix(ChatColor.GRAY + "[" + tagColor + tag + ChatColor.GRAY + "]");
        guild.setColor(color);
        guild.setDisplayName(name);
        HashMap<String, Object> guildData = new HashMap<>();
        guildData.put("color", Utils.getChatColorCode(color));
        guildData.put("tag", tag);
        guildData.put("tagColor", Utils.getChatColorCode(tagColor));
        guildData.put("players", players);
        guildData.put("head", player.getUniqueId());
        System.out.println(name);
        Storage.createGuildField(guildData,name);
        Main.getGuildCache().put(name, guildData);
        reCachePlayer(name, player);
        guild.addEntry(player.getName());
        player.setDisplayName(color + player.getName() + ChatColor.GRAY + "[" + tagColor + tag + ChatColor.GRAY + "]" + ChatColor.WHITE);
        for (Player playerFromServer : Bukkit.getOnlinePlayers()) {
            playerFromServer.setScoreboard(Main.getScoreboard());
        }
        player.sendMessage(Main.prefix + ChatColor.GREEN + "Your guild with the name " + ChatColor.UNDERLINE + "" + ChatColor.GOLD + name + ChatColor.GREEN + " has been created!");
        player.sendMessage(Main.getPlayerCache().get(player.getUniqueId()).toString());
    }

    public static void reCachePlayer(String name, Player player){
        UUID uuidFromPlayer = player.getUniqueId();
        HashMap<String,Object> tempCachedPlayerData = new HashMap<>();
        tempCachedPlayerData.put("name", Main.getPlayerCache().get(uuidFromPlayer).get("name"));
        tempCachedPlayerData.put("guild",name);
        tempCachedPlayerData.put("online", Main.getPlayerCache().get(uuidFromPlayer).get("online"));
        Main.getPlayerCache().remove(uuidFromPlayer);
        Main.getPlayerCache().put(uuidFromPlayer, tempCachedPlayerData);
    }
}
