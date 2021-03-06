package sr.ShopStuff;

import java.util.ArrayList;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import sr.Game;
import sr.SRKF;

/**
 *
 * @author Cross
 */
public class demoInventory 
{
    public SRKF plugin;
    
    public demoInventory(SRKF plugin)
    {
        this.plugin = plugin;
    }
   
    public demoInventory(Player player, Integer gameid) 
    {
        int cash = 0;
        if (Game.playerMoney.containsKey(player.getName()))
        {
            cash = Game.playerMoney.get(player.getName());
        }
        
        Inventory inv = Bukkit.createInventory(player, 18, "Demo Weaps - Cash: " + ChatColor.DARK_GREEN + "$" + cash);
        
        if (Game.demoShopItemNames.containsKey(gameid))
        {
            ArrayList<ItemStack> list = Game.demoShopItemNames.get(gameid);
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
            SRKF.LOG.severe("[SRKF] Shop failed to open inventory. demoInventory does not contain ID.");
        }
        
        ItemStack back = new ItemStack(Material.EMERALD);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.setDisplayName("Main Shop Inventory");
        back.setItemMeta(backMeta);
        
        inv.setItem(17, back);
           
    }
}
