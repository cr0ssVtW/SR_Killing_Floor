package sr.Tasks;

import java.util.HashSet;
import java.util.Iterator;
import java.util.UUID;
import net.minecraft.server.v1_7_R1.EntityCreature;
import net.minecraft.server.v1_7_R1.Navigation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_7_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftCreature;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftVillager;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import sr.CustomEntities.CustomEntityIronGolem;
import sr.Game;
import sr.Listeners.PL;
import sr.SRKF;

/**
 *
 * @author Cross
 */
public class mobTargettingTask implements Runnable
{
    public SRKF plugin;
    
    int taskID;
    public mobTargettingTask(SRKF plugin)
    {
        this.plugin = plugin;
        this.taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, 200L, 200L);
    }
    
    @Override
    public void run() 
    {
        boolean playeronline = false;
        boolean ecthere = true;
        boolean ecIGthere = true;
        int i = 0;
        for (Player p : Bukkit.getOnlinePlayers())
        {
            i = i + 1;
        }
        
        if(i>0)
        {
            playeronline = true;
        }
        
        if (Game.igID.isEmpty())
        {
            ecIGthere = false;
        }

        if(Game.leID.isEmpty())
        {
            ecthere = false;
        }
        
        if (ecIGthere == true && playeronline == true)
        {
            HashSet<CustomEntityIronGolem> hashcopy = new HashSet<>(Game.igID);
            Iterator<CustomEntityIronGolem> it = hashcopy.iterator();
            while (it.hasNext())
            {
                CustomEntityIronGolem ig = it.next();
                if (ig.isAlive())
                {
                    EntityCreature ec = ig;
                    
                    CraftWorld w3 = ig.world.getWorld();
                    int x = (int) ig.locX;
                    int y = (int) ig.locY;
                    int z = (int) ig.locZ;
                    Block b = w3.getBlockAt(x, y, z);
                    Location igLoc = b.getLocation();
                    
                    Player closestplayer = null;
                    for (Player p : Bukkit.getOnlinePlayers())
                    {
                        if (!(PL.isSpectating.contains(p.getName())))
                        {
                            Location pLoc = p.getLocation();
                            World pWorld = pLoc.getWorld();
                            net.minecraft.server.v1_7_R1.World w2 = ((CraftWorld) pWorld).getHandle();

                            if(w2.equals(ig.world))
                            {
                                //Bukkit.broadcastMessage("w2 equals ig.world");
                                if(closestplayer != null)
                                {
                                    if (!(PL.isDowned.contains(p.getName())))
                                    {
                                        double closetplayerdis = closestplayer.getLocation().distance(igLoc);
                                        double pdis = p.getLocation().distance(igLoc);

                                        if(pdis < closetplayerdis)
                                        {
                                            closestplayer = p;
                                        }
                                    }

                                }
                                else
                                {
                                    if (!(PL.isDowned.contains(p.getName())))
                                    {
                                        closestplayer = p;
                                    }
                                }
                            }
                        }
                        

                    }
                    if(closestplayer != null && closestplayer.getLocation().distance(igLoc) > 10)
                    {
                        //Bukkit.broadcastMessage("dis to player " + closestplayer.getLocation().distance(le.getLocation()));
                        new mobMoveTaskIG(plugin, ec, closestplayer, ig);
                        //Bukkit.broadcastMessage("Fired off new mobMoveTaskIG");
                    }
                }
            }
        }

        if(ecthere == true && playeronline == true)
        {
            HashSet<LivingEntity> hashcopy = new HashSet<>(Game.leID);
            
            Iterator<LivingEntity> iterator = hashcopy.iterator();
            while (iterator.hasNext())
            {
                LivingEntity le = iterator.next();
                
                if(!(le.isDead()))
                {
                    EntityCreature ec = ((CraftCreature)le).getHandle();
                    Player closestplayer = null;
                    for (Player p : Bukkit.getOnlinePlayers())
                    {
                        if (!(PL.isSpectating.contains(p.getName())))
                        {
                            if(p.getWorld().equals(le.getWorld()))
                            {
                                if(closestplayer != null)
                                {
                                    double closetplayerdis = closestplayer.getLocation().distance(le.getLocation());
                                    double pdis = p.getLocation().distance(le.getLocation());

                                    if(pdis < closetplayerdis)
                                    {
                                        closestplayer = p;
                                    }
                                }
                                else
                                {
                                    closestplayer = p;
                                }
                            }
                        }

                    }
                    
                    if(closestplayer != null && closestplayer.getLocation().distance(le.getLocation()) > 10)
                    {
                        //Bukkit.broadcastMessage("dis to player " + closestplayer.getLocation().distance(le.getLocation()));
                        new mobMoveTask(plugin, ec, closestplayer, le);
                    }
                }
            }
        }
    }
}
