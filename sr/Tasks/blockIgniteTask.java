package sr.Tasks;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import sr.SRKF;

/**
 *
 * @author Cross
 */
public class blockIgniteTask implements Runnable
{
    public SRKF plugin;
    private int taskID;
    private int x;
    private Block b;
    
    public blockIgniteTask(SRKF plugin, Block b)
    {
        this.plugin = plugin;
        this.b = b;
        this.taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, 200, 10);
        this.x = 0;
    }

    @Override
    public void run() 
    {
        x++;
        
        if (x > 5)
        {
            Bukkit.getScheduler().cancelTask(taskID);
            Chunk c = b.getChunk();
            if (!(c.isLoaded()))
            {
                c.load();
            }
            
            if (b.getType() == Material.FIRE)
            {
                b.setType(Material.AIR);
            }
        }
    }
}
