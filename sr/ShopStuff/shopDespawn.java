package sr.ShopStuff;

import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import sr.Game;
import sr.SRKF;

/**
 *
 * @author Cross
 */
public class shopDespawn implements Runnable
{
    public SRKF plugin;
    
    Location loc0;
    Location loc1;
    Location loc2;
    Villager v;
    int gameid;
    Player player;
    int i;
    int taskId;
    String materialType;
    int despawnTime = (Game.roundChangeTime / 2);
    public shopDespawn(SRKF plugin, Location loc0, Location loc1, Location loc2, Villager v, int gameid, String matString) 
    {
        this.plugin = plugin;
        this.loc0 = loc0;
        this.loc1 = loc1;
        this.loc2 = loc2;
        this.v = v;
        this.gameid = gameid;
        i = 0;
        taskId = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, this, 0, 20);
        this.materialType = matString;
    }
     
    public void run() 
    {
        i = i + 1;
        
        if (i % 3 == 0)
        {
            Location vLoc = v.getLocation();
            Game.shopFirework(vLoc);
        }
        
        if (Game.mapIDBeingSkipped.contains(gameid) || i >= despawnTime)
        {
            if (Game.mapIDBeingSkipped.contains(gameid))
            {
                Game.mapIDBeingSkipped.remove(gameid);
            }
            
            //Set back to stone or whatever you wanna do
            Material mat = Game.GameidShopDoorwayMaterial.get(materialType);

            World w = loc1.getWorld();
            Chunk c = loc1.getChunk();

            if (!(c.isLoaded()))
            {
                c.load();
            }

            int minx = Math.min(loc1.getBlockX(), loc2.getBlockX()),
            miny = Math.min(loc1.getBlockY(), loc2.getBlockY()),
            minz = Math.min(loc1.getBlockZ(), loc2.getBlockZ()),
            maxx = Math.max(loc1.getBlockX(), loc2.getBlockX()),
            maxy = Math.max(loc1.getBlockY(), loc2.getBlockY()),
            maxz = Math.max(loc1.getBlockZ(), loc2.getBlockZ());

            for (int x = minx; x<=maxx;x++)
            {
                for (int y = miny; y<=maxy;y++)
                {
                    for (int z = minz; z<=maxz;z++)
                    {
                        Block b = w.getBlockAt(x, y, z);
                        b.setType(mat);
                    }
                }
            }

            //Teleport players out.  Use wg check here.  This is just a quick little thing you can replace.
            World w2 = loc1.getWorld();  
            for (Player p2 : Bukkit.getOnlinePlayers())
            {
                if (p2.getWorld().equals(w2))
                {
                    if(p2.getLocation().distance(loc2) < 16)
                    {
                        p2.teleport(loc0);
                    }
                }
            }

            //Remove villager
            UUID uid = v.getUniqueId();

            if (Game.mobID.containsKey(uid))
            {
                Game.mobID.remove(uid);
            }

            v.remove();

            if(Game.GameidShopitemnames.containsKey(gameid))
            {
                Game.GameidShopitemnames.remove(gameid);
            }

            Bukkit.getServer().getScheduler().cancelTask(taskId);
        }
        
    }
}

