package sr.Tasks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import sr.Game;
import sr.SRKF;

/**
 *
 * @author Cross
 */
public class gameMessageTask implements Runnable
{
    public SRKF plugin;
    private int ID;
    private int taskID;
    private int x;

    
    public gameMessageTask(SRKF plugin, int gameID)
    {
        this.plugin = plugin;
        this.ID = gameID;
        this.taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, 200, 20);
        this.x = 0;
        
        
    }

    @Override
    public void run() 
    {
        x++;
        
        if (!(Game.playersMaps.containsValue(ID)))
        {
            Bukkit.getScheduler().cancelTask(taskID); // cancel task as map is clear of players
        }
        
        if (x == 30)
        {
            x = 0;
            
            String msg1 = ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + " Objectives] " + ChatColor.LIGHT_PURPLE + "Stay alive, slay monsters, earn cash.";
            String msg2 = ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + " Tip] " + ChatColor.LIGHT_PURPLE + "Utilize welders & doors to barricade yourself in!";
            String msg3 = ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + " Tip] " + ChatColor.LIGHT_PURPLE + "If playing with friends, share the cash drops! You'll need better gear to survive the later rounds.";
            String msg4 = ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + " Tip] " + ChatColor.LIGHT_PURPLE + "Turn your Server Textures on for a sweet Gun texture pack! (Video Options) Thanks to Hexas92!";
            String msg5 = ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + " Tip] " + ChatColor.LIGHT_PURPLE + "You can toggle Auto Reload on/off using: /autoreload - Reminder: Sneak + Right click to manual reload!";
            String roundMsg = ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + " Objectives] " + ChatColor.LIGHT_PURPLE + "During a round change, find the Black Market Dealer who has some supplies for you.";
            String roundMsg2 = ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + " Tip] " + ChatColor.LIGHT_PURPLE + "During a round change, type /skip to vote on skipping the round change timer!";
            
            
            
            double random = Math.random();
            
             if (Game.isRoundChanging.containsKey(ID))
            {
                if (random >= 0.50)
                {
                    for (String name : Game.playersMaps.keySet())
                    {
                        int id = Game.playersMaps.get(name);
                        if (id == ID)
                        {
                            OfflinePlayer op = Bukkit.getOfflinePlayer(name);
                            if (op.isOnline())
                            {
                                Player player = Bukkit.getPlayer(name);
                                player.sendMessage(roundMsg);
                            }
                        }
                    }
                }
                else
                {
                    for (String name : Game.playersMaps.keySet())
                    {
                        int id = Game.playersMaps.get(name);
                        if (id == ID)
                        {
                            OfflinePlayer op = Bukkit.getOfflinePlayer(name);
                            if (op.isOnline())
                            {
                                Player player = Bukkit.getPlayer(name);
                                player.sendMessage(roundMsg2);
                            }
                        }
                    }
                }
            }
            else
            {
                if (random >= 0 && random < 0.20)
                {
                    for (String name : Game.playersMaps.keySet())
                    {
                        int id = Game.playersMaps.get(name);
                        if (id == ID)
                        {
                            OfflinePlayer op = Bukkit.getOfflinePlayer(name);
                            if (op.isOnline())
                            {
                                Player player = Bukkit.getPlayer(name);
                                player.sendMessage(msg1);
                            }
                        }
                    }
                }
                if (random >= 0.20 && random < 0.40)
                {
                    for (String name : Game.playersMaps.keySet())
                    {
                        int id = Game.playersMaps.get(name);
                        if (id == ID)
                        {
                            OfflinePlayer op = Bukkit.getOfflinePlayer(name);
                            if (op.isOnline())
                            {
                                Player player = Bukkit.getPlayer(name);
                                player.sendMessage(msg2);
                            }
                        }
                    }
                }
                if (random >= 0.40 && random < 0.60)
                {
                    for (String name : Game.playersMaps.keySet())
                    {
                        int id = Game.playersMaps.get(name);
                        if (id == ID)
                        {
                            OfflinePlayer op = Bukkit.getOfflinePlayer(name);
                            if (op.isOnline())
                            {
                                Player player = Bukkit.getPlayer(name);
                                player.sendMessage(msg3);
                            }

                        }
                    }
                }
                if (random >= 0.60 && random < 0.80)
                {
                    for (String name : Game.playersMaps.keySet())
                    {
                        int id = Game.playersMaps.get(name);
                        if (id == ID)
                        {
                            OfflinePlayer op = Bukkit.getOfflinePlayer(name);
                            if (op.isOnline())
                            {
                                Player player = Bukkit.getPlayer(name);
                                player.sendMessage(msg4);
                            }
                        }
                    }
                }

                if (random >= 0.80)
                {
                    for (String name : Game.playersMaps.keySet())
                    {
                        int id = Game.playersMaps.get(name);
                        if (id == ID)
                        {
                            OfflinePlayer op = Bukkit.getOfflinePlayer(name);
                            if (op.isOnline())
                            {
                                Player player = Bukkit.getPlayer(name);
                                player.sendMessage(msg5);
                            }
                        }
                    }
                }
            }
             
            
            
           
            
            
        }
    }
    
}
