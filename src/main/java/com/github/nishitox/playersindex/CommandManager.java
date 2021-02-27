package com.github.nishitox.playersindex;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandManager implements CommandExecutor, TabCompleter {
    String msgHeader = "§7[PlayersIndex] ";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 0)
            return false;

        if(args[0].equalsIgnoreCase("set")) {
            //no selector
            if(args.length == 1){
                PlayersIndex.plugin.setPlayersIndex();
                sender.sendMessage(msgHeader + "The index has been set!");
                return true;
            }

            //with selector
            if(args.length == 2){
                PlayersIndex.plugin.setPlayersIndex(
                        PlayersIndex.plugin.entityToPlayer(
                                Bukkit.selectEntities(sender, args[1])), false);
                sender.sendMessage(msgHeader + "The index has been set!");
                return true;
            }

            //group by team
            if(args.length == 3){
                if(args[2].equalsIgnoreCase("groupByTeam")){
                    PlayersIndex.plugin.setPlayersIndex(
                            PlayersIndex.plugin.entityToPlayer(
                                    Bukkit.selectEntities(sender, args[1])), true);
                    sender.sendMessage(msgHeader + "The index has been set!");
                    return true;
                }
            }
        }

        //clear index
        if(args[0].equalsIgnoreCase("clear")){
            PlayersIndex.plugin.clearIndex();
            sender.sendMessage(msgHeader + "Cleared the index.");
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> result = new ArrayList<>();
        String[] cmds = {
                "set", "clear"
        };
        String[] selector = {
                "@a", "@e", "@p", "@r", "@s"
        };

        if(args.length == 1) {
            Collections.addAll(result, cmds);
        }

        if(args[0].equalsIgnoreCase("set")) {
            if(args.length == 2) {
                Collections.addAll(result, selector);
            }

            if(args.length == 3) {
                result.add("groupByTeam");
            }
        }
        return result;
    }
}