package sr.Tasks;

import java.util.HashSet;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Openable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import sr.Game;
import sr.Listeners.PL;
import sr.Roles.Engineer;
import sr.SRKF;

/**
 *
 * @author Cross
 */
public class weldingTask implements Runnable
{
    public SRKF plugin;
    
    private int taskID;
    public weldingTask (SRKF plugin)
    {
        this.plugin = plugin;
        this.taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, 20, 20);
    }

    @Override
    public void run() 
    {
        HashSet<Location> hashcopy = new HashSet<Location>(Game.mapDoorLoc);
        Iterator<Location> iterator = hashcopy.iterator();
        while (iterator.hasNext())
        {
            Location loc = iterator.next();
            World w2 = loc.getWorld();  
            for (Player p2 : Bukkit.getOnlinePlayers())
            {
                if (p2.getWorld().equals(w2))
                {
                    if(p2.getLocation().distance(loc) <= 3)
                    {
                        if(p2.getItemInHand() != null)
                        {
                            ItemStack inHand = p2.getItemInHand();
                            if(inHand.hasItemMeta())
                            {
                                ItemMeta meta = inHand.getItemMeta();
                                if (meta.hasDisplayName())
                                {
                                    String disp = meta.getDisplayName();
                                    if (disp.contains("Welder"))
                                    {
                                        if(!(Game.mapDoorBroken.contains(loc)))
                                        {
                                           int doordurability = Game.mapDoorHealth.get(loc);

                                           String roleName = "Medic";
                                           
                                           int weldAmount = 3;
                                           
                                           if (Game.playerClass.containsKey(p2.getName()))
                                           {
                                               roleName = Game.playerClass.get(p2.getName());
                                           }
                                           
                                           if (roleName.equalsIgnoreCase("Engineer"))
                                           {
                                               int bonus = Engineer.getWeldingBonusAmount(p2.getName());
                                               weldAmount = weldAmount + bonus;
                                           }
                                           
                                           boolean changedura = false;
                                           boolean openorclose = false;
                                           boolean open = true;
                                           boolean didweld = false;
                                           boolean didunweld = false;
                                           String message = "";
                                           if(p2.isSneaking())
                                           {
                                               if(doordurability > 0)
                                               {
                                                   changedura = true;
                                                   didunweld = true;

                                                   doordurability = doordurability - weldAmount;
                                                   if(doordurability <= 0)
                                                   {
                                                       doordurability = 0;
                                                       openorclose = true;
                                                       open = true;
                                                   }

                                               }
                                               /*
                                               else
                                               {
                                                   message = ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "]" + ChatColor.RED + " This door is open.  You cannot unweld it further!";
                                               }
                                               */

                                           }
                                           else
                                           {
                                               if(doordurability < 100)
                                               {
                                                   changedura = true;
                                                   didweld = true;

                                                   if(doordurability <= 0)
                                                   {
                                                       doordurability = 0;
                                                       openorclose = true;
                                                       open = false;
                                                   }
                                                   doordurability = doordurability + weldAmount;
                                                   if(doordurability >= 100)
                                                   {
                                                       doordurability = 100;

                                                   }
                                               }
                                               /*
                                               else
                                               {
                                                   message = ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "]" + ChatColor.RED + "This door has already been welded the max amount!";
                                               }
                                               */


                                           }

                                           if(changedura == true)
                                           {
                                               if(Game.mapDoorHealth.containsKey(loc))
                                               {
                                                   Game.mapDoorHealth.remove(loc);
                                               }

                                               Game.mapDoorHealth.put(loc, doordurability);
                                               
                                               loc.getWorld().playSound(loc, Sound.FIZZ, 1, 4);
                                           }
                                           if(openorclose == true)
                                           {
                                               Block lower = loc.getWorld().getBlockAt(loc);
                                               Block upper = lower.getRelative(BlockFace.UP, 1);

                                               BlockState state = upper.getState();
                                               if (state.getData() instanceof Openable)
                                               {
                                                   Openable om = (Openable) state.getData();
                                                   om.setOpen(open);
                                                   state.setData((MaterialData) om);
                                                   state.update();
                                               }

                                               state = lower.getState();
                                               if (state.getData() instanceof Openable)
                                               {
                                                   Openable om = (Openable) state.getData();
                                                   om.setOpen(open);
                                                   state.setData((MaterialData) om);
                                                   state.update();
                                               }
                                           }
                                           /*
                                           if(didweld == true)
                                           {
                                               message = ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "]" + ChatColor.AQUA + " You weld, increasing the door's durability to: " + ChatColor.GOLD + doordurability;

                                           }
                                           if(didunweld == true)
                                           {
                                               message = ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "]" + ChatColor.AQUA + " You unweld, decreasing the door's durability to: " + ChatColor.GOLD + doordurability;

                                           }
                                           if(openorclose == true)
                                           {
                                               if(open == true)
                                               {
                                                   message = ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "]" + ChatColor.AQUA + " You successfully open the door!";

                                               }
                                               if(open == false)
                                               {
                                                   message = ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "]" + ChatColor.AQUA + " You successfully weld the door shut!";

                                               }
                                           }
                                           */


                                           //p2.sendMessage(message);
                                           
                                           if (!(PL.weldingSB.contains(p2.getName())))
                                           {
                                               int cash = 0;
                                               if (Game.playerMoney.containsKey(p2.getName()))
                                               {
                                                   cash = Game.playerMoney.get(p2.getName());
                                               }
                                               
                                               ScoreboardManager manager = Bukkit.getScoreboardManager();
                                                Scoreboard sb2 = manager.getNewScoreboard();

                                                Objective objective = sb2.registerNewObjective("kf", "dummy");
                                                objective.setDisplaySlot(DisplaySlot.SIDEBAR);
                                                objective.setDisplayName(ChatColor.GRAY + "Killing Floor");

                                                Score score = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.GOLD + " Cash: " + ChatColor.GREEN + "$"));
                                                score.setScore(cash);

                                                if (PL.weldingSB.contains(p2.getName()))
                                                {
                                                    PL.weldingSB.remove(p2.getName());
                                                }

                                                objective = sb2.getObjective(DisplaySlot.SIDEBAR);
                                                int doordura = 0;
                                                if (Game.mapDoorHealth.containsKey(loc))
                                                {
                                                    doordura = Game.mapDoorHealth.get(loc);
                                                }
                                                Score score2 = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.YELLOW + " Door: " + ChatColor.GOLD + ""));
                                                score2.setScore(doordura);
                                                
                                                PL.weldingSB.add(p2.getName());
                                                p2.setScoreboard(sb2);
                                                
                                                new scoreboardTask(plugin, p2, loc);
                                           }

                                         }
                                         else
                                         {
                                             String message = ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "]" + ChatColor.RED + " This door has been broken. It will take a round to fix!";

                                             p2.sendMessage(message);
                                         }
                                    }
                                }
                                

                            }
                        }
                    }
                }
            }
        }
    }
}
