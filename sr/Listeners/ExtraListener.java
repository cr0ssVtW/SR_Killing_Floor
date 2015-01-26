package sr.Listeners;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;
import sr.SRKF;
import sr.Tasks.blockIgniteTask;

/**
 *
 * @author Cross
 */
public class ExtraListener implements Listener
{
    public SRKF plugin;
    
    public ExtraListener(SRKF plugin)
    {
        this.plugin = plugin;
    }
    
    
    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent event)
    {
        Block b = event.getBlock();
        
        new blockIgniteTask(plugin, b);
    }
}
