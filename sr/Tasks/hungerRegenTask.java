package sr.Tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import sr.SRKF;

/**
 *
 * @author Cross
 */
public class hungerRegenTask implements Runnable
{
    private SRKF plugin;
    private int taskID;
    public hungerRegenTask(SRKF plugin)
    {
        this.plugin = plugin;
        this.taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, 0, 100);
    }

    @Override
    public void run() 
    {
        for (Player p : Bukkit.getOnlinePlayers())
        {
            int hunger = p.getFoodLevel();
            int increase = hunger + 4;
            if (increase >= 20)
            {
                increase = 20;
            }
            
            p.setFoodLevel(increase);
        }
    }
    
}
