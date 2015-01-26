package sr.Tasks;

import net.minecraft.server.v1_7_R1.EntityCreature;
import net.minecraft.server.v1_7_R1.Navigation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_7_R1.CraftWorld;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import sr.CustomEntities.CustomEntityIronGolem;
import sr.SRKF;

/**
 *
 * @author Cross
 */
public class mobMoveTaskIG implements Runnable
{
    public SRKF plugin;
    EntityCreature ec;
    CustomEntityIronGolem le;
    Player player;
    int i;
    int taskID;
               
    public mobMoveTaskIG(SRKF plugin, EntityCreature ec, Player player, CustomEntityIronGolem le) 
    {
        this.plugin = plugin;
        this.ec = ec;
        this.le = le;
        this.player = player;
        i = 0;
        this.taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, 5L, 5L);
    }
     
    @Override
    public void run() 
    {

        i = i + 1;

        if(i > 40)
        {
            Bukkit.getScheduler().cancelTask(taskID);
        }
        else
        {
            if (!(le.isAlive()))
            {
                //Bukkit.broadcastMessage("le not alive task failed");
                Bukkit.getScheduler().cancelTask(taskID);
            }

            if(le.isAlive() && player.isOnline())
            {
                //Bukkit.broadcastMessage("le is alive and player online");
                
                CraftWorld w3 = le.world.getWorld();
                int x = (int) le.locX;
                int y = (int) le.locY;
                int z = (int) le.locZ;
                Block block = w3.getBlockAt(x, y, z);
                Location loc = block.getLocation();
                
                Location permloc = loc;
                Location mloc = loc;
                Location ploc = player.getLocation();

                if (mloc.getWorld().equals(ploc.getWorld()))
                {
                    if(mloc.distance(ploc) < 64)
                    {
                        Navigation nav = ec.getNavigation();
                        nav.a(ploc.getX(), ploc.getY(), ploc.getZ());
                    }
                    else
                    {
                        double mlocx = mloc.getX();
                        double mlocy = mloc.getY();
                        double mlocz = mloc.getZ();

                        double plocx = ploc.getX();
                        double plocy = ploc.getY();
                        double plocz = ploc.getZ();

                        double newx = (mlocx + plocx)/2;
                        double newy = (mlocy + plocy)/2;
                        double newz = (mlocz + plocz)/2;

                        Block block2 = w3.getBlockAt((int) newx, (int) newy, (int) newz);
                        Location newloc = block2.getLocation();

                        boolean havenewloc = false;
                        while(havenewloc == false)
                        {
                            if(newloc.distance(mloc) < 15)
                            {
                                havenewloc = true;
                            }
                            else
                            {
                                newx = (newx + mlocx)/2;
                                newy = (newy + mlocy)/2;
                                newz = (newz + mlocz)/2;

                                Block block3 = w3.getBlockAt((int) newx, (int) newy, (int) newz);
                                newloc = block3.getLocation();
                            }
                        }

                        Navigation nav = ec.getNavigation();
                        nav.a(newloc.getX(), newloc.getY(), newloc.getZ(), 1.2f);
                    }
                }
                
            }
            else
            {
                //Bukkit.broadcastMessage("le not alive task failed2");
                Bukkit.getScheduler().cancelTask(taskID);
            }
        }
    }
}
