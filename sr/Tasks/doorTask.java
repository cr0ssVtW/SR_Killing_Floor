package sr.Tasks;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Zombie;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Openable;
import sr.Game;
import sr.SRKF;

/**
 *
 * @author Cross
 */
public class doorTask implements Runnable
{
    public SRKF plugin;
    private int taskID;
    public doorTask(SRKF plugin)
    {
        this.plugin = plugin;
        this.taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, 20, 20);
    }
    
    @Override
    public void run() 
    {
        HashSet<Location> hashcopy = new HashSet<>(Game.mapDoorLoc);
        Iterator<Location> it = hashcopy.iterator();
        
        while (it.hasNext())
        {
            Location loc = it.next();
            int doordurability = Game.mapDoorHealth.get(loc);
            
            if (doordurability < 0)
            {
                doordurability = 0;
            }
            
            if (!(Game.mapDoorBroken.contains(loc)))
            {
                if (doordurability > 0)
                {
                    boolean sent = true;
                    boolean hurt = true;
                    for (Entity e : loc.getWorld().getEntities())
                    {
                        if (e instanceof LivingEntity)
                        {
                            LivingEntity le = (LivingEntity) e;
                            
                            if (!(le instanceof Player))
                            {
                                if (le.getLocation().distance(loc) < 3 && le.getWorld().equals(loc.getWorld()))
                                {
                                    if(le instanceof Zombie || le instanceof IronGolem || le instanceof PigZombie || le instanceof Skeleton || le instanceof Creeper)
                                    {
                                        int damage = 1;
                                        if (le instanceof IronGolem)
                                        {
                                            damage = 15;
                                        }
                                        
                                        doordurability = doordurability - damage;
                                        //String message = ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "]" 
                                        //        + ChatColor.RED + " A nearby door was damaged! Remaining durability:" + ChatColor.GOLD + doordurability;

                                        if (doordurability <= 0)
                                        {
                                            if(Game.mapDoorBroken.contains(loc))
                                            {
                                                Game.mapDoorBroken.remove(loc);
                                            }
                                            Game.mapDoorBroken.add(loc);

                                            new doorFixTask(plugin, loc);
                                            
                                            sent = false;
                                            
                                            
                                            Block lower = loc.getWorld().getBlockAt(loc);
                                            Block upper = lower.getRelative(BlockFace.UP, 1);

                                            BlockState state = upper.getState();
                                            if (state.getData() instanceof Openable)
                                            {
                                                Openable om = (Openable) state.getData();
                                                om.setOpen(true);
                                                state.setData((MaterialData) om);
                                                state.update();
                                            }

                                            state = lower.getState();
                                            if (state.getData() instanceof Openable)
                                            {
                                                Openable om = (Openable) state.getData();
                                                om.setOpen(true);
                                                state.setData((MaterialData) om);
                                                state.update();
                                            }
                                        }

                                        if(Game.mapDoorHealth.containsKey(loc))
                                        {
                                            Game.mapDoorHealth.remove(loc);
                                        }
                                        Game.mapDoorHealth.put(loc, doordurability);

                                        hurt = false;
                                        
                                    }
                                }
                            }
                        }
                    }
                    
                    if (hurt == false)
                    {
                        hurt = true;
                        
                        World w2 = loc.getWorld();
                        w2.playSound(loc, Sound.ZOMBIE_WOOD, 1, 1);
                    }
                    
                    if (sent == false)
                    {
                        sent = true;
                        World w2 = loc.getWorld();
                        w2.playSound(loc, Sound.ZOMBIE_WOODBREAK, 1, 1);

                        for (Player p2 : Bukkit.getOnlinePlayers())
                        {
                            if (p2.getWorld().equals(w2))
                            {
                                if (p2.getLocation().distance(loc) < 20)
                                {
                                    p2.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "]" + ChatColor.RED + " A nearby door was destroyed!");
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
