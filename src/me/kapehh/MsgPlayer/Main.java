package me.kapehh.MsgPlayer;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

/**
 * Created by Karen on 17.01.2015.
 */
public class Main extends JavaPlugin {

    private String setColorChar(String msg) {
        return msg.replace('&', ChatColor.COLOR_CHAR).replace("" + ChatColor.COLOR_CHAR + ChatColor.COLOR_CHAR, "&");
    }

    private String sendMessagePlayers(World world, String[] args) {
        List<Player> playerList = world.getPlayers();
        String msg = setColorChar(StringUtils.join(args, ' ', 1, args.length));
        for (Player p : playerList) {
            p.sendMessage(msg);
        }
        return msg;
    }

    @Override
    public void onEnable() {
        getCommand("msgplayer").setExecutor(new CommandExecutor() {
            @Override
            public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
                if (!sender.isOp()) {
                    sender.sendMessage("You need OP!");
                    return true;
                }

                if (args.length < 2) {
                    return false;
                }

                String playerName = args[0];
                Player[] players = Bukkit.getOnlinePlayers();
                World worldMessage = null;
                for (Player p : players) {
                    if (p.getName().equals(playerName)) {
                        worldMessage = p.getWorld();
                    }
                }

                if (worldMessage == null) {
                    sender.sendMessage(ChatColor.RED + "Player '" + playerName + "' not found.");
                    return true;
                }

                getLogger().info("To " + worldMessage.getName() + ": " + sendMessagePlayers(worldMessage, args));
                return true;
            }
        });
        getCommand("msgworld").setExecutor(new CommandExecutor() {
            @Override
            public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
                if (!sender.isOp()) {
                    sender.sendMessage("You need OP!");
                    return true;
                }

                if (args.length < 2) {
                    return false;
                }

                String worldName = args[0];
                World worldMessage = Bukkit.getWorld(worldName);

                if (worldMessage == null) {
                    sender.sendMessage(ChatColor.RED + "World '" + worldName + "' not found.");
                    return true;
                }

                getLogger().info("To " + worldMessage.getName() + ": " + sendMessagePlayers(worldMessage, args));
                return true;
            }
        });
    }

    @Override
    public void onDisable() {

    }
}
