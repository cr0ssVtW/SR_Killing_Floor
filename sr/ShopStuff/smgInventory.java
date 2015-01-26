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
public class smgInventory 
{
    public SRKF plugin;
    
    public smgInventory(SRKF plugin)
    {
        this.plugin = plugin;
    }
   
    public smgInventory(Player player, Integer gameid) 
    {
        int cash = 0;
        if (Game.playerMoney.containsKey(player.getName()))
        {
            cash = Game.playerMoney.get(player.getName());
        }
        
        Inventory inv = Bukkit.createInventory(player, 18, "SMG Weaps - Cash: " + ChatColor.DARK_GREEN + "$" + cash);
        
        if (Game.smgShopItemNames.containsKey(gameid))
        {
            ArrayList<ItemStack> list = Game.smgShopItemNames.get(gameid);
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
            SRKF.LOG.severe("[SRKF] Shop failed to open inventory. smgShopItemNames does not contain ID.");
        }
        
        ItemStack back = new ItemStack(Material.EMERALD);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.setDisplayName("Main Shop Inventory");
        back.setItemMeta(backMeta);
        
        inv.setItem(17, back);
           
    }
}
