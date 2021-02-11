package com.github.nishitox.playersindex;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.*;

import java.util.*;

public final class PlayersIndex extends JavaPlugin {
    private final Server server = getServer();
    private final Scoreboard scoreboard = server.getScoreboardManager().getMainScoreboard();
    private final String msgHeader = "§7PlayersIndex: ";
    private Objective objective;

    @Override
    public void onEnable() {
        // Plugin startup logic
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        objective = scoreboard.getObjective("playerIndex");
        List<Player> players = new ArrayList<Player>();

        if(args.length > 0){
            if(args[0].equals("set")){
                if(args.length > 1){
                    //selector
                    players = entityToPlayer(Bukkit.selectEntities(sender, args[1]));

                    //group by team
                    if(args.length > 2 && args[2].equals("groupbyteam")){
                        Set<Team> teams = scoreboard.getTeams();
                        for(Team team: teams){
                            List<Player> teamPlayers = getTeamPlayers(players, team);
                            setIndex(teamPlayers);
                        }
                    }else{
                        setIndex(players);
                    }
                }else{
                    //not selector
                    players = (List<Player>) server.getOnlinePlayers();
                    setIndex(players);
                }
                objective.setDisplaySlot(DisplaySlot.PLAYER_LIST);
                sender.sendMessage(msgHeader + "The index has been set!");
                return true;

            }else if(args[0].equals("clear")){
                clearIndex();
                sender.sendMessage(msgHeader + "Cleared the index.");
                return true;
            }
        }
        return false;
    }

    //set index
    public void setIndex(List<Player> players){
        if(objective == null){
            objective = scoreboard.registerNewObjective("playerIndex", "dummy", "");
        }
        for(int i = 0; i< players.size(); i++){
            Score score = objective.getScore(players.get(i).getName());
            score.setScore(i+1);
        }
    }

    //clear index
    public void clearIndex(){
        if(objective != null){
            objective.unregister();
        }
    }

    //entityToPlayer
    public List<Player> entityToPlayer(List<Entity> entities){
        List<Player> players = new ArrayList<Player>();
        for(Entity entity: entities){
            if(entity.getType() == EntityType.PLAYER){
                UUID uuid = entity.getUniqueId();
                players.add(server.getPlayer(uuid));
            }
        }
        return players;
    }

    public List<Player> getTeamPlayers(List<Player> players, Team team){
        Set<String> entries = team.getEntries();
        List<Player> teamPlayers = new ArrayList<Player>();

        for(String entry: entries){
            for(Player player: players){
                if(entry.equals(player.getName())){
                    teamPlayers.add(player);
                }
            }
        }
        return teamPlayers;
    }
}