package sr;

import java.util.ArrayList;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import sr.Extras.Roles;

/**
 *
 * @author Cross
 */
public class classChooser
{
    public SRKF plugin;
    
    public classChooser (SRKF plugin)
    {
        this.plugin = plugin;
    }
    
    public classChooser(Player player)
    {
        Inventory inv = Bukkit.createInventory(player, 9, "" + ChatColor.BOLD + "Choose your Role!");
        
        Iterator<String> it = Roles.roleNames.iterator();
        
        while (it.hasNext())
        {
            String roleName = it.next();
            Material itemType = Roles.roleItemMaterial.get(roleName);
            ItemStack stack = new ItemStack(itemType);
            ItemMeta meta = stack.getItemMeta();
            meta.setDisplayName(roleName);
            
            ArrayList<String> itemMetaArray = new ArrayList<String>();
            String desc = Roles.roleDescriptions.get(roleName);
            String desc2 = Roles.roleDescriptions.get(roleName + "_more");
            String desc3 = Roles.roleDescriptions.get(roleName + "_more2");
            itemMetaArray.add(ChatColor.DARK_PURPLE + desc);
            itemMetaArray.add(ChatColor.DARK_PURPLE + desc2);
            itemMetaArray.add(ChatColor.DARK_PURPLE + desc3);
            itemMetaArray.add("" + ChatColor.AQUA + "Click to choose the ");
            itemMetaArray.add("" + ChatColor.GREEN + roleName + ChatColor.DARK_AQUA + " role!");
            
            meta.setLore(itemMetaArray);
            stack.setItemMeta(meta);
            
            inv.addItem(stack);
        }
        
        ItemStack respecStack = new ItemStack(Material.DIAMOND);
        ItemMeta respecMeta = respecStack.getItemMeta();
        respecMeta.setDisplayName("Click to Respec your perks!");
        ArrayList<String> respecLore = new ArrayList<>();
        String desc = ChatColor.GRAY + "Cost: " + ChatColor.AQUA + Roles.respecCoinCost + " coin(s)";
        respecLore.add(desc);
        respecMeta.setLore(respecLore);
        respecStack.setItemMeta(respecMeta);
        
        inv.setItem(8, respecStack);
        
        player.openInventory(inv);
    }
}
