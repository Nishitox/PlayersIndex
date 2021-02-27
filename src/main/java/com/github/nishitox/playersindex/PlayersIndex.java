package com.github.nishitox.playersindex;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.*;

import java.util.*;

public final class PlayersIndex extends JavaPlugin{
    static PlayersIndex plugin;
    private final Scoreboard scoreboard =
            Bukkit.getScoreboardManager().getMainScoreboard();
    private Objective objective;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        getCommand("playersindex").setExecutor(new CommandManager());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    // set players index
    void setPlayersIndex() {
        setPlayersIndex(
                (List<Player>) Bukkit.getOnlinePlayers(), false);
    }
    void setPlayersIndex(List<Player> players, Boolean groupByTeam) {
        objective = scoreboard.getObjective("playerIndex");
        if(objective == null) {
            objective = scoreboard.registerNewObjective("playerIndex", "dummy", "");
        }
        if(!groupByTeam) {
            setIndex(players);
        }else {
            for(Team team: scoreboard.getTeams()) {
                setIndex(getTeamPlayers(team, players));
            }
        }
        objective.setDisplaySlot(DisplaySlot.PLAYER_LIST);
    }

    // set index
    void setIndex(List<Player> players) {
        for(int i = 0; i< players.size(); i++) {
            Score score = objective.getScore(players.get(i).getName());
            score.setScore(i+1);
        }
    }

    // clear index
    void clearIndex() {
        if(objective != null) {
            objective.unregister();
        }
    }

    List<Player> entityToPlayer(List<Entity> entities) {
        List<Player> players = new ArrayList<Player>();
        for(Entity entity: entities) {
            if(entity.getType() == EntityType.PLAYER) {
                players.add(Bukkit.getPlayer(entity.getUniqueId()));
            }
        }
        return players;
    }

    List<Player> getTeamPlayers(Team team, List<Player> players) {
        List<Player> teamPlayers = new ArrayList<Player>();

        for(String entry: team.getEntries()) {
            for(Player player: players) {
                if(entry.equals(player.getName())) {
                    teamPlayers.add(player);
                }
            }
        }
        return teamPlayers;
    }
}