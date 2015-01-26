package sr.Extras;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftVillager;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import sr.Game;
import sr.Listeners.PL;
import sr.SRKF;

/**
 *
 * @author Cross
 */
public class mobRemover implements Runnable
{
    public SRKF plugin;
    
    public int taskID;
    public mobRemover(SRKF plugin)
    {
        this.plugin = plugin;
        this.taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, 20, 40);
    }

    @Override
    public void run() 
    {
        /*
        HashSet<LivingEntity> copy = new HashSet<>(Game.leID);
        Iterator<LivingEntity> leIt = copy.iterator();
        while (leIt.hasNext())
        {
            LivingEntity l = leIt.next();
            
            UUID lUID = l.getUniqueId();
            
            HashMap<UUID, Integer> copyID = new HashMap<>(Game.mobID);
            for (UUID uid : copyID.keySet())
            {
                if (uid == lUID)
                {
                    if (l.isDead())
                    {
                        int ID = Game.mobID.get(uid);
                        int kills = 0;
                        if (PL.mapKills.containsKey(ID))
                        {
                            kills = PL.mapKills.get(ID);
                            PL.mapKills.remove(ID);
                        }
                        PL.mapKills.put(ID, kills);
                        
                        Game.leID.remove(l);
                        Game.mobID.remove(uid);
                        
                        l.remove();
                        Game.updateMobCount(ID);
                    }
                }
            }
        }
        */
        
        /*
         * Fire cleanup
         */
        
        for (World w : Bukkit.getWorlds())
        {
            for (Entity e : Bukkit.getWorld(w.getName()).getEntities())
            {
                if (e instanceof LivingEntity)
                {
                    LivingEntity le = (LivingEntity) e;
                    
                    if (!(le instanceof Player))
                    {
                        if (le instanceof Blaze) // do blaze teleport
                        {
                            for (Player target : Bukkit.getOnlinePlayers())
                            {
                                if (target.getWorld().equals(le.getWorld()))
                                {
                                    if (!(PL.isSpectating.contains(target.getName())))
                                    {
                                        if (target.getLocation().distance(le.getLocation()) > 10)
                                        {
                                            double random = Math.random();
                                            if (random > 0.5)
                                            {
                                                Location pLoc = target.getLocation();
                                                pLoc.setY(pLoc.getY() + 2);
                                                pLoc.setX(pLoc.getX() + 1);
                                                pLoc.setZ(pLoc.getZ() - 1);

                                                Block b = pLoc.getBlock();

                                                if (b.getType() == Material.AIR)
                                                {
                                                    Location pLoc2 = pLoc.clone();
                                                    pLoc2.setY(pLoc2.getY() + 1);
                                                    Block b2 = pLoc2.getBlock();
                                                    if (b2.getType() == Material.AIR)
                                                    {
                                                        le.teleport(pLoc2);
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        
                        UUID uid = le.getUniqueId();
                        if (Game.mobID.containsKey(uid))
                        {
                            int gameID = Game.mobID.get(uid);
                            if (Game.isRoundChanging.containsKey(gameID))
                            {
                                if (le instanceof CraftVillager || le instanceof Villager)
                                {
                                    return;
                                }
                                
                                //Bukkit.broadcastMessage("Map round is changing. Removed entity: " + le.getType().toString());
                                SRKF.LOG.info("[SRKF] Map round is changing. Removed entity: " + le.getType() + " from game ID: " + gameID);
                                le.remove();
                                Game.mobID.remove(uid);
                            }
                        }
                        /*
                        else
                        {
                            //Bukkit.broadcastMessage("Removed bad entity: " + le.getType().toString());
                            SRKF.LOG.info("[SRKF] Removed bad entity: " + le.getType() + " from world: " + le.getWorld().getName());
                            le.remove();
                        }
                        */
                    }
                }
            }
        }
    }
    
}
