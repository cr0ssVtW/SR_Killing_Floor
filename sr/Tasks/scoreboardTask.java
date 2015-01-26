package sr.Tasks;

import net.minecraft.server.v1_7_R1.EntityCreature;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import sr.Game;
import sr.Listeners.PL;
import sr.SRKF;

/**
 *
 * @author Cross
 */
public class scoreboardTask implements Runnable
{
    EntityCreature ec;
    LivingEntity le;
    Player player; 
    Location doorloc;
    int i;
    int taskID;
    SRKF plugin;
    public scoreboardTask(SRKF plugin, Player player, Location doorloc)
    {
        this.plugin = plugin;
        this.doorloc = doorloc;
        this.player = player;
        this.taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, 0, 20);
    }

    @Override
    public void run() 
    {
        i = i + 1;
        
        if (!(player.isOnline()))
        {
            i = 5;
        }
        
        if (i > 4)
        {
            ScoreboardManager manager = Bukkit.getScoreboardManager();
            Scoreboard sb = manager.getNewScoreboard();
            int cash = 0;
            if (Game.playerMoney.containsKey(player.getName()))
            {
                cash = Game.playerMoney.get(player.getName());
            }
            
            Objective objective = sb.registerNewObjective("kf", "dummy");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
            objective.setDisplayName(ChatColor.GRAY + "Killing Floor");
            
            Score score = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.GOLD + " Cash: " + ChatColor.GREEN + "$"));
            score.setScore(cash);
            player.setScoreboard(sb);
            
            if (PL.weldingSB.contains(player.getName()))
            {
                PL.weldingSB.remove(player.getName());
            }
            
            Bukkit.getScheduler().cancelTask(taskID);
        }
        else
        {
            Scoreboard sb = player.getScoreboard();
            Objective objective;
            objective = sb.getObjective(DisplaySlot.SIDEBAR);
            int doordura = 0;
            if (Game.mapDoorHealth.containsKey(doorloc))
            {
                doordura = Game.mapDoorHealth.get(doorloc);
            }
            Score score2 = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.YELLOW + " Door: " + ChatColor.GOLD + ""));
            score2.setScore(doordura);
        }
    }
    
}
