package sr.ShopStuff;

import java.util.ArrayList;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import sr.Game;
import sr.SRKF;

/**
 *
 * @author Cross
 */
public class shopInventory 
{
    public SRKF plugin;
    
    public shopInventory(SRKF plugin)
    {
        this.plugin = plugin;
    }
   
    public shopInventory(Player player, Integer gameid) 
    {
        int cash = 0;
        if (Game.playerMoney.containsKey(player.getName()))
        {
            cash = Game.playerMoney.get(player.getName());
        }
        
        Inventory inv = Bukkit.createInventory(player, 18, "Click to buy! Cash: " + ChatColor.DARK_GREEN + "$" + cash);
        
        if (Game.GameidShopitemnames.containsKey(gameid))
        {
            ArrayList<ItemStack> list = Game.GameidShopitemnames.get(gameid);
            Iterator<ItemStack> it = list.iterator();
            
            while (it.hasNext())
            {
                ItemStack stack = it.next();
                inv.addItem(stack.clone());
            }

            player.openInventory(inv);  
        }
        else
        {
            SRKF.LOG.severe("[SRKF] Shop failed to open inventory. GameidShopitemnames does not contain ID.");
        }
           
    }
}
