package sr.Tasks;

import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import sr.Extras.Guns;
import sr.Game;
import sr.Roles.Sharpshooter;
import sr.SRKF;

/**
 *
 * @author Cross
 */
public class sharpshooterLuckyTask implements Runnable
{
    private SRKF plugin;
    private Player player;
    private int taskID;
    
    public sharpshooterLuckyTask(SRKF plugin, Player player)
    {
        this.player = player;
        this.plugin = plugin;
        
        this.taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, 1200, 600);
    }

    @Override
    public void run() 
    {
        if (!(player.isOnline()))
        {
            Bukkit.getScheduler().cancelTask(taskID);
            //SRKF.LOG.info("[SRKF] - Cancelled sharpshooterLuckyTask ID: " + taskID);
        }
        
        double coinflip = Math.random();
        
        ItemStack ammo1 = null;
        ItemStack ammo2 = null;
        
        String ammo1Type = "null";
        String ammo2Type = "null";
        if (coinflip > 0.5)
        {
            double luckyRoll = Math.random();
            
            double luckyVal = 0.25 + Sharpshooter.getLuckyBonusAmount(player.getName());
            
            if (luckyRoll <= luckyVal)
            {
                // check items in that players inventory for guns
                for (ItemStack invStack : player.getInventory().getContents())
                {
                    if (invStack != null)
                    {
                        if (invStack.hasItemMeta())
                        {
                            if (invStack.getItemMeta().hasLore())
                            {
                                List<String> lore = invStack.getItemMeta().getLore();
                                String rawType = lore.get(0);
                                String type = ChatColor.stripColor(rawType);
                                if (Guns.miscItemStacks.containsKey(type))
                                {
                                    // get type Ammo
                                    if (ammo1 == null)
                                    {
                                        ammo1 = Guns.miscItemStacks.get(type).clone();
                                        ammo1Type = type;
                                    }
                                    else
                                    {
                                        ammo2 = Guns.miscItemStacks.get(type).clone();
                                        ammo2Type = type;
                                    }
                                }
                            }
                        }
                    }
                }
                
                
                if (ammo1 != null)
                {
                    double lastRoll = Math.random();
                    if (lastRoll > 0.05)
                    {
                        player.getInventory().addItem(ammo1);
                        player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.YELLOW + "LuckY!" + ChatColor.GRAY + " You found a clip for your " + ammo1Type + "!");
                    }
                }

                if (ammo2 != null)
                {
                    double lastRoll = Math.random();
                    if (lastRoll > 0.05)
                    {
                        player.getInventory().addItem(ammo2);
                        player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.YELLOW + "LuckY!" + ChatColor.GRAY + " You found a clip for your " + ammo2Type + "!");
                    }
                }
            }
        }
    }
    
}
