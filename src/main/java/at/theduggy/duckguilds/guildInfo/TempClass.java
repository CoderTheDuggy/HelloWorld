package at.theduggy.duckguilds.guildInfo;

import at.theduggy.duckguilds.Main;
import at.theduggy.duckguilds.other.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class TempClass {

    public void temp() throws IOException, ParseException {
        ArrayList<String> keys = new ArrayList<>(Main.getGuildCache().keySet());
        StringBuilder msg = new StringBuilder();
        msg.append(Utils.centerText(ChatColor.GRAY + "        [" + ChatColor.YELLOW + "Guild-System" + ChatColor.GRAY  + "]"+ ChatColor.WHITE + "\n"));
        msg.append(Utils.centerText(ChatColor.BLUE + "" + ChatColor.BOLD + ""  + ChatColor.MAGIC+ "wa" + ChatColor.YELLOW + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE +"Guilds" + ChatColor.BLUE + "" + ChatColor.BOLD + ""  + ChatColor.MAGIC + "wa")).append("\n  \n");
        Player player = Bukkit.getPlayer("");
        String s="";
        HashMap<String,Object> guildInfos = Main.getGuildCache().get(s);
        Bukkit.broadcastMessage(guildInfos.toString());
        ChatColor color = Utils.translateFromStringToChatColor((String) guildInfos.get("color"));
        String line = Utils.centerText(  color  + "" + ChatColor.BOLD+ s + ":");
        String optionalPlayerRole = "";
        if (Utils.getPlayerGuild(player).equals(s)) {
            optionalPlayerRole = Utils.centerText(ChatColor.RED + "" + ChatColor.BOLD + "YOU");
        }
        String players = Utils.centerText(ChatColor.GREEN + "Player-count: " + ChatColor.YELLOW + Utils.getGuildSize(s));
        ChatColor tagColor = Utils.translateFromStringToChatColor((String) guildInfos.get("tagColor"));
        String tag = Utils.centerText(ChatColor.GREEN + "Tag: " + tagColor + guildInfos.get("tag"));
        Path guildPlayerFolder = Paths.get(Main.guildRootFolder + "/playerData");
        Path headPlayerGuildFolder = Paths.get(guildPlayerFolder + "/" + guildInfos.get("head"));
        Path playerNameData = Paths.get(headPlayerGuildFolder + "/data.json");
        String name;
        if (Files.exists(playerNameData)){
            name = (String) Main.getPlayerCache().get(guildInfos.get("head")).get("name");//TODO Change all UUID-objects to strings
        }else{
            name = ChatColor.RED + "NOT FOUND";
        }
        String head = Utils.centerText(ChatColor.GREEN + "Head: " + ChatColor.YELLOW + name);

        msg.append(line).append("\n");
        if (!optionalPlayerRole.equals("")){
            msg.append(optionalPlayerRole).append("\n");
        }
        msg.append(players).append("\n");
        msg.append(tag).append("\n");
        msg.append(head).append("\n\n");
    }
}
