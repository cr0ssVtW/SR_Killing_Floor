package sr.Tasks;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import sr.Game;
import sr.SRKF;

/**
 *
 * @author Cross
 */
public class doorFixTask implements Runnable
{
    private int taskID;
    private Location loc;
    private SRKF plugin;

    public doorFixTask(SRKF plugin, Location loc)
    {
        this.loc = loc;
        this.plugin = plugin;
        this.taskID = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, this, 1800);
    }
    @Override
    public void run() 
    {
        if (Game.mapDoorBroken.contains(loc))
        {
            Game.mapDoorBroken.remove(loc);
        }
    }
}
