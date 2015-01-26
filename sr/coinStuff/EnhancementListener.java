package sr.coinStuff;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import sr.SRKF;




public class EnhancementListener implements Listener
{

  public SRKF plugin;

  public EnhancementListener(SRKF plugin)
  {
    this.plugin = plugin;
  }
  
  //Enhancement Hashmaps
public static HashMap<String, String> HelmPrefix = new HashMap<String, String>();
//public static HashMap<String, String> HelmSuffix = new HashMap<String, String>();
public static HashMap<String, String> HelmPrefixBonusText = new HashMap<String, String>();
//public static HashMap<String, String> HelmSuffixNegativeText = new HashMap<String, String>();

public static HashMap<String, String> ChestPrefix = new HashMap<String, String>();
//public static HashMap<String, String> ChestSuffix = new HashMap<String, String>();
public static HashMap<String, String> ChestPrefixBonusText = new HashMap<String, String>();
//public static HashMap<String, String> ChestSuffixNegativeText = new HashMap<String, String>();

public static HashMap<String, String> LegsPrefix = new HashMap<String, String>();
//public static HashMap<String, String> LegsSuffix = new HashMap<String, String>();
public static HashMap<String, String> LegsPrefixBonusText = new HashMap<String, String>();
//public static HashMap<String, String> LegsSuffixNegativeText = new HashMap<String, String>();

public static HashMap<String, String> BootsPrefix = new HashMap<String, String>();
//public static HashMap<String, String> BootsSuffix = new HashMap<String, String>();
public static HashMap<String, String> BootsPrefixBonusText = new HashMap<String, String>();
//public static HashMap<String, String> BootsSuffixNegativeText = new HashMap<String, String>();

public static HashMap<String, String> MeleeWeaponPrefix = new HashMap<String, String>();
//public static HashMap<String, String> MeleeWeaponSuffix = new HashMap<String, String>();
public static HashMap<String, String> MeleeWeaponPrefixBonusText = new HashMap<String, String>();
//public static HashMap<String, String> MeleeWeaponSuffixNegativeText = new HashMap<String, String>();

public static HashMap<String, String> RangedWeaponPrefix = new HashMap<String, String>();
//public static HashMap<String, String> RangedWeaponSuffix = new HashMap<String, String>();
public static HashMap<String, String> RangedWeaponPrefixBonusText = new HashMap<String, String>();
//public static HashMap<String, String> RangedWeaponSuffixNegativeText = new HashMap<String, String>();



public static ItemStack getChestEnhancements(ItemStack is, Player p)
    {
        String pname = p.getName().toLowerCase();
        ItemStack returnstack = is;
        
        String itemtype = "ChestPlate";
        int dd = ChestPrefix.size();
        if(ChestPrefix.containsKey(pname))
        {
            String prefix = "";
            //String suffix = "";
            String bonus = "";
            //String negative = "";
            
            prefix = ChestPrefix.get(pname);
            //suffix = ChestSuffix.get(pname);
            bonus = ChestPrefixBonusText.get(pname);
            //negative = ChestSuffixNegativeText.get(pname);
            
            ItemMeta meta = returnstack.getItemMeta();
            
            List<String> lorelist = new ArrayList<String>();
            
            if(meta.hasLore())
            {
                lorelist = meta.getLore();
            }
            
            if(!(meta.hasDisplayName()))
            {
                String displayitem = "Chestplate";
                
                String displayname = ChatColor.GOLD + prefix + " " + displayitem;
                
                meta.setDisplayName(displayname);
                
            }
            
            lorelist.add(" ");
            lorelist.add(ChatColor.GOLD + "Enhancement: " + prefix + " " + itemtype);
            lorelist.add(ChatColor.AQUA + "---------------");
            lorelist.add(bonus);
            
            meta.setLore(lorelist);
            returnstack.setItemMeta(meta);
            
            returnstack.addUnsafeEnchantment(Enchantment.DURABILITY, 100);
        }

        
       
        return returnstack;
    }

public static ItemStack getHelmEnhancements(ItemStack is, Player p)
    {
        String pname = p.getName().toLowerCase();
        ItemStack returnstack = is;
        
        String itemtype = "Helmet";
        
        
        if(HelmPrefix.containsKey(pname))
        {
            String prefix = "";
            //String suffix = "";
            String bonus = "";
            //String negative = "";
            
            prefix = HelmPrefix.get(pname);
            //suffix = HelmSuffix.get(pname);
            bonus = HelmPrefixBonusText.get(pname);
            //negative = HelmSuffixNegativeText.get(pname);
            
            ItemMeta meta = returnstack.getItemMeta();
            
            List<String> lorelist = new ArrayList<String>();
            
            if(meta.hasLore())
            {
                lorelist = meta.getLore();
            }
            lorelist.add(" ");
            lorelist.add(ChatColor.GOLD + "Enhancements: " + prefix + " " + itemtype);
            lorelist.add(ChatColor.AQUA + "---------------");
            lorelist.add(bonus);
            //lorelist.add(negative);
            
            if(!(meta.hasDisplayName()))
            {
                String displayitem = "Helmet";
                
                String displayname = ChatColor.GOLD + prefix + " " + displayitem;
                
                meta.setDisplayName(displayname);
                
            }
            
            meta.setLore(lorelist);
            returnstack.setItemMeta(meta);
            
            returnstack.addUnsafeEnchantment(Enchantment.DURABILITY, 100);
        }

        
       
        return returnstack;
    }
    
    
    
    public static ItemStack getLegEnhancements(ItemStack is, Player p)
    {
        String pname = p.getName().toLowerCase();
        ItemStack returnstack = is;
        
        String itemtype = "Leggings";
        
       
        if(LegsPrefix.containsKey(pname))
        {
            String prefix = "";
            //String suffix = "";
            String bonus = "";
            //String negative = "";
            
            prefix = LegsPrefix.get(pname);
            //suffix = LegsSuffix.get(pname);
            bonus = LegsPrefixBonusText.get(pname);
            //negative = LegsSuffixNegativeText.get(pname);
            
            ItemMeta meta = returnstack.getItemMeta();
            
            List<String> lorelist = new ArrayList<String>();
            
            if(meta.hasLore())
            {
                lorelist = meta.getLore();
            }
            lorelist.add(" ");
            lorelist.add(ChatColor.GOLD + "Enhancements: " + prefix + " " + itemtype);
            lorelist.add(ChatColor.AQUA + "---------------");
            lorelist.add(bonus);
            //lorelist.add(negative);
            
            if(!(meta.hasDisplayName()))
            {
                String displayitem = "Leggings";
                
                String displayname = ChatColor.GOLD + prefix + " " + displayitem;
                
                meta.setDisplayName(displayname);
                
            }
            
            meta.setLore(lorelist);
            returnstack.setItemMeta(meta);
            
            returnstack.addUnsafeEnchantment(Enchantment.DURABILITY, 100);
        }

        
       
        return returnstack;
    }
    
    public static ItemStack getBootEnhancements(ItemStack is, Player p)
    {
        String pname = p.getName().toLowerCase();
        ItemStack returnstack = is;
        
        String itemtype = "Boots";

        if(BootsPrefix.containsKey(pname))
        {
            String prefix = "";
           // String suffix = "";
            String bonus = "";
            //String negative = "";
            
            prefix = BootsPrefix.get(pname);
           // suffix = BootsSuffix.get(pname);
            bonus = BootsPrefixBonusText.get(pname);
           // negative = BootsSuffixNegativeText.get(pname);
            
            ItemMeta meta = returnstack.getItemMeta();
            
            List<String> lorelist = new ArrayList<String>();
            
            if(meta.hasLore())
            {
                lorelist = meta.getLore();
            }
            
            if(!(meta.hasDisplayName()))
            {
                String displayitem = "Boots";
                
                String displayname = ChatColor.GOLD + prefix + " " + displayitem;
                
                meta.setDisplayName(displayname);
                
            }
            lorelist.add(" ");
            lorelist.add(ChatColor.GOLD + "Enhancements: " + prefix + " " + itemtype);
            lorelist.add(ChatColor.AQUA + "---------------");
            lorelist.add(bonus);
           // lorelist.add(negative);
            
            meta.setLore(lorelist);
            returnstack.setItemMeta(meta);
            
            returnstack.addUnsafeEnchantment(Enchantment.DURABILITY, 100);
        }

        
       
        return returnstack;
    }
    public static ItemStack getMeleeWeaponEnhancements(ItemStack is, Player p)
    {
        String pname = p.getName().toLowerCase();
        ItemStack returnstack = is;
        
        String itemtype = "Sword";
        if(MeleeWeaponPrefix.containsKey(pname))
        {
            String prefix = "";
            //String suffix = "";
            String bonus = "";
           // String negative = "";
            
            prefix = MeleeWeaponPrefix.get(pname);
            //suffix = MeleeWeaponSuffix.get(pname);
            bonus = MeleeWeaponPrefixBonusText.get(pname);
           // negative = MeleeWeaponSuffixNegativeText.get(pname);
           
            ItemMeta meta = returnstack.getItemMeta();
            
            List<String> lorelist = new ArrayList<String>();
            
            if(meta.hasLore())
            {
                lorelist = meta.getLore();
            }
            
            if(!(meta.hasDisplayName()))
            {
                String displayitem = "Sword";
                
                if(returnstack.getType().equals(Material.IRON_AXE) || returnstack.getType().equals(Material.GOLD_AXE) || returnstack.getType().equals(Material.DIAMOND_AXE))
                {
                    displayitem = "Axe";
                }
                String displayname = ChatColor.GOLD + prefix + " " + displayitem;
                
                meta.setDisplayName(displayname);
                
                itemtype = displayitem;
                
            }
            lorelist.add(" ");
            lorelist.add(ChatColor.GOLD + "Enhancements: " + prefix + " " + itemtype);
            lorelist.add(ChatColor.AQUA + "---------------");
            lorelist.add(bonus);
            //lorelist.add(negative);
            
            meta.setLore(lorelist);
            returnstack.setItemMeta(meta);
            
            returnstack.addUnsafeEnchantment(Enchantment.DURABILITY, 100);
        }

        
       
        return returnstack;
    }
    
    public static ItemStack getRangedWeaponEnhancements(ItemStack is, Player p)
    {
        String pname = p.getName().toLowerCase();
        ItemStack returnstack = is;
        
        String itemtype = "Gun";
        
        if(RangedWeaponPrefix.containsKey(pname))
        {
            String prefix = "";
           // String suffix = "";
            String bonus = "";
           // String negative = "";
            
            prefix = RangedWeaponPrefix.get(pname);
           // suffix = RangedWeaponSuffix.get(pname);
            bonus = RangedWeaponPrefixBonusText.get(pname);
           // negative = RangedWeaponSuffixNegativeText.get(pname);
            
            ItemMeta meta = returnstack.getItemMeta();
            
            List<String> lorelist = new ArrayList<String>();
            
            if(meta.hasLore())
            {
                lorelist = meta.getLore();
            }
            
            if(!(meta.hasDisplayName()))
            {
                String displayitem = "Gun";
                
                if(returnstack.getType().equals(Material.BOW))
                {
                    displayitem = "Bow";
                }
                String displayname = ChatColor.GOLD + prefix + " " + displayitem;
                
                meta.setDisplayName(displayname);
                
                itemtype = displayitem;
                
            }
            
            lorelist.add(" "); 
            lorelist.add(ChatColor.GOLD + "Enhancements: " + prefix + " " + itemtype);
            lorelist.add(ChatColor.AQUA + "---------------");
            lorelist.add(bonus);
           // lorelist.add(negative);
            
            meta.setLore(lorelist);
            returnstack.setItemMeta(meta);
            
            returnstack.addUnsafeEnchantment(Enchantment.DURABILITY, 100);
        }

        
       
        return returnstack;
    }

@EventHandler
public void PlayerQuit(PlayerQuitEvent event) 
{
    Player p = event.getPlayer();
    String pname = p.getName().toLowerCase();
    
    if(HelmPrefix.containsKey(pname))
    {
        HelmPrefix.remove(pname);
    }
   // if(HelmSuffix.containsKey(pname))
   // {
   //     HelmSuffix.remove(pname);
   // }
    if(HelmPrefixBonusText.containsKey(pname))
    {
        HelmPrefixBonusText.remove(pname);
    }
  //  if(HelmSuffixNegativeText.containsKey(pname))
  //  {
  //     HelmSuffixNegativeText.remove(pname);
   // }
    
    if(ChestPrefix.containsKey(pname))
    {
        ChestPrefix.remove(pname);
    }
  //  if(ChestSuffix.containsKey(pname))
   // {
   //     ChestSuffix.remove(pname);
  //  }
    if(ChestPrefixBonusText.containsKey(pname))
    {
        ChestPrefixBonusText.remove(pname);
    }
  //  if(ChestSuffixNegativeText.containsKey(pname))
  //  {
  //      ChestSuffixNegativeText.remove(pname);
  //  }
    
    if(LegsPrefix.containsKey(pname))
    {
        LegsPrefix.remove(pname);
    }
  //  if(LegsSuffix.containsKey(pname))
  //  {
  //      LegsSuffix.remove(pname);
  //  }
    if(LegsPrefixBonusText.containsKey(pname))
    {
        LegsPrefixBonusText.remove(pname);
    }
  //  if(LegsSuffixNegativeText.containsKey(pname))
  //  {
  //      LegsSuffixNegativeText.remove(pname);
  //  }
    
    if(BootsPrefix.containsKey(pname))
    {
        BootsPrefix.remove(pname);
    }
 //   if(BootsSuffix.containsKey(pname))
 //   {
 //       BootsSuffix.remove(pname);
 //   }
    if(BootsPrefixBonusText.containsKey(pname))
    {
        BootsPrefixBonusText.remove(pname);
    }
 //   if(BootsSuffixNegativeText.containsKey(pname))
  //  {
 //       BootsSuffixNegativeText.remove(pname);
 //   }
    
    if(MeleeWeaponPrefix.containsKey(pname))
    {
        MeleeWeaponPrefix.remove(pname);
    }
 //   if(MeleeWeaponSuffix.containsKey(pname))
 //   {
 //       MeleeWeaponSuffix.remove(pname);
 //   }
    if(MeleeWeaponPrefixBonusText.containsKey(pname))
    {
        MeleeWeaponPrefixBonusText.remove(pname);
    }
 //   if(MeleeWeaponSuffixNegativeText.containsKey(pname))
 //   {
 //       MeleeWeaponSuffixNegativeText.remove(pname);
 //   }
    
    if(RangedWeaponPrefix.containsKey(pname))
    {
        RangedWeaponPrefix.remove(pname);
    }
 //   if(RangedWeaponSuffix.containsKey(pname))
 //   {
 //       RangedWeaponSuffix.remove(pname);
 //   }
    if(RangedWeaponPrefixBonusText.containsKey(pname))
    {
        RangedWeaponPrefixBonusText.remove(pname);
    }
 //   if(RangedWeaponSuffixNegativeText.containsKey(pname))
 //   {
 //       RangedWeaponSuffixNegativeText.remove(pname);
 //   }
    
    
}


@EventHandler
public void PlayerJoin(PlayerJoinEvent event) 
{
    Player p = event.getPlayer();
    String pname = p.getName().toLowerCase();

            Player player = event.getPlayer();
            
            //enhancementsdatabasemanager db = new enhancementsdatabasemanager(plugin);
            if (SRKF.coindb.connected)
            {
                  
                  String currentchest = SRKF.coindb.getChestplate(player.getName().toLowerCase());
                  if(currentchest != null)
                  {
                      String info = SRKF.coindb.getItemInformation(currentchest);
                      String[] parts = currentchest.split("\\s+");
                      if(!(parts[0].toLowerCase().equals("none")))
                      {
                          ChestPrefix.put(pname, parts[0]);
                          ChestPrefixBonusText.put(pname, ChatColor.AQUA + "Bonus: " + ChatColor.GOLD + info);
                      }
                      
                  }
                  
                  String currentlegs = SRKF.coindb.getLeggings(player.getName().toLowerCase());
                  
                  if(currentlegs != null)
                  {
                      String info = SRKF.coindb.getItemInformation(currentlegs);
                      String[] parts = currentlegs.split("\\s+");
                      if(!(parts[0].toLowerCase().equals("none")))
                      {
                         LegsPrefix.put(pname, parts[0]);
                         LegsPrefixBonusText.put(pname, ChatColor.AQUA + "Bonus: " + ChatColor.GOLD + info); 
                      }
                      
                  }
                  
                  String currentboots = SRKF.coindb.getBoots(player.getName().toLowerCase());
                  
                  if(currentboots != null)
                  {
                      String info = SRKF.coindb.getItemInformation(currentboots);
                      String[] parts = currentboots.split("\\s+");
                      if(!(parts[0].toLowerCase().equals("none")))
                      {
                          BootsPrefix.put(pname, parts[0]);
                          BootsPrefixBonusText.put(pname, ChatColor.AQUA + "Bonus: " + ChatColor.GOLD + info);
                      }
                      
                  }
                  
                  String currentmweap = SRKF.coindb.getMeleeweapon(player.getName().toLowerCase());
                  
                  if(currentmweap != null)
                  {
                      String info = SRKF.coindb.getItemInformation(currentmweap);
                      String[] parts = currentmweap.split("\\s+");
                      if(!(parts[0].toLowerCase().equals("none")))
                      {
                         MeleeWeaponPrefix.put(pname, parts[0]);
                         MeleeWeaponPrefixBonusText.put(pname, ChatColor.AQUA + "Bonus: " + ChatColor.GOLD + info); 
                      }
                      
                  }
                  
                  String currentrweap = SRKF.coindb.getRangedweapon(player.getName().toLowerCase());
                  
                  if(currentrweap != null)
                  {
                      String info = SRKF.coindb.getItemInformation(currentrweap);
                      String[] parts = currentrweap.split("\\s+");
                      if(!(parts[0].toLowerCase().equals("none")))
                      {
                          RangedWeaponPrefix.put(pname, parts[0]);
                          RangedWeaponPrefixBonusText.put(pname, ChatColor.AQUA + "Bonus: " + ChatColor.GOLD + info);
                      }
                      
                  }
                  

            } 

}

    
  
}