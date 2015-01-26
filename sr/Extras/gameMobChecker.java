package sr.Extras;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import sr.Game;
import sr.Listeners.PL;
import sr.SRKF;

/**
 *
 * @author Cross
 */
public class gameMobChecker implements Runnable
{
    public SRKF plugin;
    
    public int delay;
    public int taskID;
    public int gameID;
    public gameMobChecker(SRKF plugin, int gameID)
    {
        this.gameID = gameID;
        this.plugin = plugin;
        this.delay = 100;
        this.taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, 20, this.delay);
        
        if (Game.gameMobCheckerHash.containsKey(gameID))
        {
            Game.gameMobCheckerHash.remove(gameID);
        }
        Game.gameMobCheckerHash.put(gameID, taskID);
    }

    @Override
    public void run() 
    {
        if (!(Game.isRoundChanging.containsKey(gameID)))
        {
            int mobsSpawned = 0;
            if (Game.mapIDSpawned.containsKey(gameID))
            {
                mobsSpawned = Game.mapIDSpawned.get(gameID);
            }
            
            int mobsKilled = 0;
            if (PL.mapKills.containsKey(gameID))
            {
                mobsKilled = PL.mapKills.get(gameID);
            }
            
            String worldName = Game.gameIDWorldName.get(gameID);
            World world = Bukkit.getWorld(worldName);
            int mobsAlive = 0;
            
            for (Entity e : world.getEntities())
            {
                if (e instanceof LivingEntity)
                {
                    LivingEntity le = (LivingEntity) e;
                    if (!(le instanceof Player))
                    {
                        mobsAlive = mobsAlive + 1;
                    }
                }
            }
            
            int diff = (mobsSpawned - mobsKilled) - mobsAlive;
            
            if (diff > 0)
            {
                // update with diff
            
                if (PL.mapKills.containsKey(gameID))
                {
                    PL.mapKills.remove(gameID);
                }
                int updated = mobsKilled + diff;
                PL.mapKills.put(gameID, updated);

                Game.updateMobCount(gameID);
            }
            
        }
    }
}
