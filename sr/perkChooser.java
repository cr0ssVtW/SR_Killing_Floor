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
import sr.Extras.PerkInfo;
import sr.Roles.Berserker;
import sr.Roles.Commando;
import sr.Roles.Engineer;
import sr.Roles.Medic;
import sr.Roles.Sharpshooter;

/**
 *
 * @author Cross
 */
public class perkChooser 
{
    public SRKF plugin;
    
    public perkChooser (SRKF plugin)
    {
        this.plugin = plugin;
    }
    
    public perkChooser(Player player)
    {
        int points = SRKF.player_dbManager.getPlayerStat(player.getName(), "UnspentPoints");
        String roleName = SRKF.player_dbManager.getRole(player.getName());
        Inventory inv = Bukkit.createInventory(player, 18, roleName + " perks. "
                + "Points: " + ChatColor.DARK_RED + points);
        
        if (roleName.equalsIgnoreCase("Berserker"))
        {
            Iterator<String> it = PerkInfo.BerserkerPerkNames.iterator();
            while (it.hasNext())
            {
                String perkName = it.next();
                int perkLevel = SRKF.player_dbManager.getRoleStat(player.getName(), roleName, perkName);
                ItemStack stack = PerkInfo.BerserkerPerkItemStack.get(perkName);
                ItemStack clone = stack.clone();
                ItemMeta meta = clone.getItemMeta();
                String disName = meta.getDisplayName();
                meta.setDisplayName(disName + " - Current Level: " + perkLevel);

                ArrayList<String> itemMetaArray = (ArrayList<String>) meta.getLore();
                if (perkName.equalsIgnoreCase("Endurance"))
                {
                    double bonus = Berserker.getEnduranceBonusAmount(player.getName());
                    double totalBonus =  bonus;
                    itemMetaArray.add("" + ChatColor.AQUA + "Current bonus is: ");
                    itemMetaArray.add(ChatColor.GREEN + "+" + totalBonus + " speed boost level.");
                }
                
                if (perkName.equalsIgnoreCase("Strength"))
                {
                    double bonus = Berserker.getStrengthBonusAmount(player.getName());
                    double totalBonus = bonus;
                    itemMetaArray.add("" + ChatColor.AQUA + "Current bonus is: ");
                    itemMetaArray.add(ChatColor.GREEN + "+" + totalBonus + " melee damage.");
                }
                
                if (perkName.equalsIgnoreCase("KevlarAdaptation"))
                {
                    double bonus = Berserker.getKevlarAdaptationBonusAmount(player.getName());
                    double totalBonus = bonus * 100;
                    itemMetaArray.add("" + ChatColor.AQUA + "Current bonus is: ");
                    itemMetaArray.add(ChatColor.GREEN + "" + totalBonus + "% damage taken.");
                }
                
                if (perkName.equalsIgnoreCase("Ferocity"))
                {
                    double bonus = Berserker.getFerocityBonusAmount(player.getName(), 20, 10);
                    int totalBonus = (int) (bonus * 100);
                    itemMetaArray.add("" + ChatColor.AQUA + "Current bonus is: ");
                    itemMetaArray.add(ChatColor.GREEN + "+" + totalBonus + "% damage from behind while at 50% health.");
                }
                

                meta.setLore(itemMetaArray);
                clone.setItemMeta(meta);

                inv.addItem(clone);
            }
            ItemStack filler = new ItemStack(Material.STAINED_GLASS_PANE);
            ItemMeta fillerMeta = filler.getItemMeta();
            fillerMeta.setDisplayName(" ");
            filler.setItemMeta(fillerMeta);
            inv.setItem(4, filler);
            inv.setItem(5, filler);
            inv.setItem(6, filler);
            inv.setItem(7, filler);
            inv.setItem(8, filler);
            
            Iterator<String> it2 = PerkInfo.BerserkerPerkNames.iterator();
            while (it2.hasNext())
            {
                String perkName = it2.next();
                Material upgradeMaterial = PerkInfo.upgradeMaterial;
                ItemStack stack = new ItemStack(upgradeMaterial);
                ItemMeta meta = stack.getItemMeta();
                meta.setDisplayName("Berserker - Click to level up: " + perkName);
                stack.setItemMeta(meta);
                inv.addItem(stack);
            }
            
            inv.setItem(13, filler);
            inv.setItem(14, filler);
            inv.setItem(15, filler);
            inv.setItem(16, filler);
            inv.setItem(17, filler);
            
        } // end Berserker
        
        if (roleName.equalsIgnoreCase("Commando"))
        {
            Iterator<String> it = PerkInfo.CommandoPerkNames.iterator();
            while (it.hasNext())
            {
                String perkName = it.next();
                int perkLevel = SRKF.player_dbManager.getRoleStat(player.getName(), roleName, perkName);
                ItemStack stack = PerkInfo.CommandoPerkItemStack.get(perkName);
                ItemStack clone = stack.clone();
                ItemMeta meta = clone.getItemMeta();
                String disName = meta.getDisplayName();
                meta.setDisplayName(disName + " - Current Level: " + perkLevel);

                ArrayList<String> itemMetaArray = (ArrayList<String>) meta.getLore();
                if (perkName.equalsIgnoreCase("RangerInstructor"))
                {
                    double bonus = Commando.getRangerInstructorBonusAmount(player.getName());
                    double totalBonus = bonus * 100;
                    itemMetaArray.add("" + ChatColor.AQUA + "Current bonus is: ");
                    itemMetaArray.add(ChatColor.GREEN + "+" + totalBonus + "% ranged damage.");
                }
                
                if (perkName.equalsIgnoreCase("Pain"))
                {
                    double bonus = Commando.getPainBonusAmount(player.getName());
                    double totalBonus = bonus;
                    itemMetaArray.add("" + ChatColor.AQUA + "Current bonus is: ");
                    itemMetaArray.add(ChatColor.GREEN + "+" + totalBonus + " damage returned.");
                }
                
                if (perkName.equalsIgnoreCase("Juggernaut"))
                {
                    double bonus = Commando.getJuggernautBonusAmount(player.getName());
                    double totalBonus = bonus;
                    itemMetaArray.add("" + ChatColor.AQUA + "Current bonus is: ");
                    itemMetaArray.add(ChatColor.GREEN + "+" + totalBonus + " maximum health.");
                }
                
                if (perkName.equalsIgnoreCase("Inspire"))
                {
                    double bonus = Commando.getInspireBonusDurationAmount(player.getName());
                    int totalBonus = (int) (bonus / 20);
                    itemMetaArray.add("" + ChatColor.AQUA + "Current bonus is: ");
                    itemMetaArray.add(ChatColor.GREEN + "+" + totalBonus + " second duration");
                    
                    double bonus2 = Commando.getInspireBonusFrequencyAmount(player.getName());
                    int totalBonus2 = (int) (bonus2 / 20);
                    itemMetaArray.add("" + ChatColor.AQUA + "Current bonus is: ");
                    itemMetaArray.add("" + ChatColor.GREEN + totalBonus2 + " second re-use time.");
                }
                

                meta.setLore(itemMetaArray);
                clone.setItemMeta(meta);

                inv.addItem(clone);
            }
            ItemStack filler = new ItemStack(Material.STAINED_GLASS_PANE);
            ItemMeta fillerMeta = filler.getItemMeta();
            fillerMeta.setDisplayName(" ");
            filler.setItemMeta(fillerMeta);
            inv.setItem(4, filler);
            inv.setItem(5, filler);
            inv.setItem(6, filler);
            inv.setItem(7, filler);
            inv.setItem(8, filler);
            
            Iterator<String> it2 = PerkInfo.CommandoPerkNames.iterator();
            while (it2.hasNext())
            {
                String perkName = it2.next();
                Material upgradeMaterial = PerkInfo.upgradeMaterial;
                ItemStack stack = new ItemStack(upgradeMaterial);
                ItemMeta meta = stack.getItemMeta();
                meta.setDisplayName("Commando - Click to level up: " + perkName);
                stack.setItemMeta(meta);
                inv.addItem(stack);
            }
            
            inv.setItem(13, filler);
            inv.setItem(14, filler);
            inv.setItem(15, filler);
            inv.setItem(16, filler);
            inv.setItem(17, filler);
            
        } // end Commando
        
        if (roleName.equalsIgnoreCase("Engineer"))
        {
            Iterator<String> it = PerkInfo.EngineerPerkNames.iterator();
            while (it.hasNext())
            {
                String perkName = it.next();
                int perkLevel = SRKF.player_dbManager.getRoleStat(player.getName(), roleName, perkName);
                ItemStack stack = PerkInfo.EngineerPerkItemStack.get(perkName);
                ItemStack clone = stack.clone();
                ItemMeta meta = clone.getItemMeta();
                String disName = meta.getDisplayName();
                meta.setDisplayName(disName + " - Current Level: " + perkLevel);

                ArrayList<String> itemMetaArray = (ArrayList<String>) meta.getLore();
                if (perkName.equalsIgnoreCase("BallisticTraining"))
                {
                    double bonus = Engineer.getBallisticTrainingBonusAmount(player.getName());
                    double totalBonus = bonus;
                    itemMetaArray.add("" + ChatColor.AQUA + "Current bonus is: ");
                    itemMetaArray.add(ChatColor.GREEN + "+" + totalBonus + " Ammo Clip amount.");
                }
                
                if (perkName.equalsIgnoreCase("Welding"))
                {
                    double bonus = Engineer.getWeldingBonusAmount(player.getName());
                    double totalBonus = bonus;
                    itemMetaArray.add("" + ChatColor.AQUA + "Current bonus is: ");
                    itemMetaArray.add(ChatColor.GREEN + "+" + totalBonus + " Welding power.");
                }
                
                if (perkName.equalsIgnoreCase("DemolitionMastery"))
                {
                    double bonus = Engineer.getDemoltionMasteryBonusAmount(player.getName());
                    double totalBonus = bonus;
                    itemMetaArray.add("" + ChatColor.AQUA + "Current bonus is: ");
                    itemMetaArray.add(ChatColor.GREEN + "+" + totalBonus + " Explosive damage.");
                }
                
                if (perkName.equalsIgnoreCase("HeavyArtillery"))
                {
                    double bonus = Engineer.getHeavyArtilleryBonusAmount(player.getName());
                    int totalBonus = (int) (bonus);
                    itemMetaArray.add("" + ChatColor.AQUA + "Current bonus is: ");
                    itemMetaArray.add(ChatColor.GREEN + "+" + totalBonus + " Artillery damage.");
                }
                

                meta.setLore(itemMetaArray);
                clone.setItemMeta(meta);

                inv.addItem(clone);
            }
            ItemStack filler = new ItemStack(Material.STAINED_GLASS_PANE);
            ItemMeta fillerMeta = filler.getItemMeta();
            fillerMeta.setDisplayName(" ");
            filler.setItemMeta(fillerMeta);
            inv.setItem(4, filler);
            inv.setItem(5, filler);
            inv.setItem(6, filler);
            inv.setItem(7, filler);
            inv.setItem(8, filler);
            
            Iterator<String> it2 = PerkInfo.EngineerPerkNames.iterator();
            while (it2.hasNext())
            {
                String perkName = it2.next();
                Material upgradeMaterial = PerkInfo.upgradeMaterial;
                ItemStack stack = new ItemStack(upgradeMaterial);
                ItemMeta meta = stack.getItemMeta();
                meta.setDisplayName("Engineer - Click to level up: " + perkName);
                stack.setItemMeta(meta);
                inv.addItem(stack);
            }
            
            inv.setItem(13, filler);
            inv.setItem(14, filler);
            inv.setItem(15, filler);
            inv.setItem(16, filler);
            inv.setItem(17, filler);
            
        } // end Engineer
        
        if (roleName.equalsIgnoreCase("Medic"))
        {
            Iterator<String> it = PerkInfo.MedicPerkNames.iterator();
            while (it.hasNext())
            {
                String perkName = it.next();
                int perkLevel = SRKF.player_dbManager.getRoleStat(player.getName(), roleName, perkName);
                ItemStack stack = PerkInfo.MedicPerkItemStack.get(perkName);
                ItemStack clone = stack.clone();
                ItemMeta meta = clone.getItemMeta();
                String disName = meta.getDisplayName();
                meta.setDisplayName(disName + " - Current Level: " + perkLevel);

                ArrayList<String> itemMetaArray = (ArrayList<String>) meta.getLore();
                if (perkName.equalsIgnoreCase("MedicalTraining"))
                {
                    double bonus = Medic.getHealBonusAmount(player.getName());
                    double totalBonus = bonus;
                    itemMetaArray.add("" + ChatColor.AQUA + "Current bonus is: ");
                    itemMetaArray.add(ChatColor.GREEN + "+" + totalBonus + " Med Kit heal amount.");
                }
                
                if (perkName.equalsIgnoreCase("ShotgunMastery"))
                {
                    double bonus = Medic.getShotgunBonusAmount(player.getName());
                    double totalBonus = bonus;
                    itemMetaArray.add("" + ChatColor.AQUA + "Current bonus is: ");
                    itemMetaArray.add(ChatColor.GREEN + "+" + totalBonus + " Shotgun damage.");
                }
                
                if (perkName.equalsIgnoreCase("Triage"))
                {
                    double bonus = Medic.getTriageBonusAmount(player.getName());
                    double totalBonus = bonus;
                    itemMetaArray.add("" + ChatColor.AQUA + "Current bonus is: ");
                    itemMetaArray.add(ChatColor.GREEN + "+" + totalBonus + " Health Amount on Revive.");
                }
                
                if (perkName.equalsIgnoreCase("CombatArmor"))
                {
                    double bonus = Medic.getCombatArmorBonusAmount(player.getName());
                    int totalBonus = (int) (bonus * 2);
                    itemMetaArray.add("" + ChatColor.AQUA + "Current bonus is: ");
                    itemMetaArray.add(ChatColor.GREEN + "+" + totalBonus + " Absorption Shield on self heal.");
                }
                

                meta.setLore(itemMetaArray);
                clone.setItemMeta(meta);

                inv.addItem(clone);
            }
            ItemStack filler = new ItemStack(Material.STAINED_GLASS_PANE);
            ItemMeta fillerMeta = filler.getItemMeta();
            fillerMeta.setDisplayName(" ");
            filler.setItemMeta(fillerMeta);
            inv.setItem(4, filler);
            inv.setItem(5, filler);
            inv.setItem(6, filler);
            inv.setItem(7, filler);
            inv.setItem(8, filler);
            
            Iterator<String> it2 = PerkInfo.MedicPerkNames.iterator();
            while (it2.hasNext())
            {
                String perkName = it2.next();
                Material upgradeMaterial = PerkInfo.upgradeMaterial;
                ItemStack stack = new ItemStack(upgradeMaterial);
                ItemMeta meta = stack.getItemMeta();
                meta.setDisplayName("Medic - Click to level up: " + perkName);
                stack.setItemMeta(meta);
                inv.addItem(stack);
            }
            inv.setItem(13, filler);
            inv.setItem(14, filler);
            inv.setItem(15, filler);
            inv.setItem(16, filler);
            inv.setItem(17, filler);
        } // end Medic
        
        if (roleName.equalsIgnoreCase("Sharpshooter"))
        {
            Iterator<String> it = PerkInfo.SharpshooterPerkNames.iterator();
            while (it.hasNext())
            {
                String perkName = it.next();
                int perkLevel = SRKF.player_dbManager.getRoleStat(player.getName(), roleName, perkName);
                ItemStack stack = PerkInfo.SharpshooterPerkItemStack.get(perkName);
                ItemStack clone = stack.clone();
                ItemMeta meta = clone.getItemMeta();
                String disName = meta.getDisplayName();
                meta.setDisplayName(disName + " - Current Level: " + perkLevel);

                ArrayList<String> itemMetaArray = (ArrayList<String>) meta.getLore();
                if (perkName.equalsIgnoreCase("MarksmanTraining"))
                {
                    double bonus = Sharpshooter.getMarksmanTrainingBonusAmount(player.getName());
                    double totalBonus = bonus * 100;
                    itemMetaArray.add("" + ChatColor.AQUA + "Current bonus is: ");
                    itemMetaArray.add(ChatColor.GREEN + "" + totalBonus + "% reload time.");
                }
                
                if (perkName.equalsIgnoreCase("CalledShot"))
                {
                    double bonus = Sharpshooter.getCalledShotBonus(player.getName());
                    int totalBonus = (int) (bonus * 100);
                    itemMetaArray.add("" + ChatColor.AQUA + "Current bonus is: ");
                    itemMetaArray.add(ChatColor.GREEN + "+" + totalBonus + "% Headshot damage.");
                }
                
                if (perkName.equalsIgnoreCase("Lucky"))
                {
                    double bonus = Sharpshooter.getLuckyBonusAmount(player.getName());
                    int totalBonus = (int) (bonus * 100);
                    itemMetaArray.add("" + ChatColor.AQUA + "Current bonus is: ");
                    itemMetaArray.add(ChatColor.GREEN + "+" + totalBonus + "% chance to find Ammo Clip.");
                }
                
                if (perkName.equalsIgnoreCase("Wound"))
                {
                    double bonus = Sharpshooter.getWoundBonusAmount(player.getName());
                    int totalBonus = (int) (bonus * 100);
                    itemMetaArray.add("" + ChatColor.AQUA + "Current bonus is: ");
                    itemMetaArray.add(ChatColor.GREEN + "+" + totalBonus + "% of a Targets health over 4 seconds.");
                }
                

                meta.setLore(itemMetaArray);
                clone.setItemMeta(meta);

                inv.addItem(clone);
            }
            ItemStack filler = new ItemStack(Material.STAINED_GLASS_PANE);
            ItemMeta fillerMeta = filler.getItemMeta();
            fillerMeta.setDisplayName(" ");
            filler.setItemMeta(fillerMeta);
            
            inv.setItem(4, filler);
            inv.setItem(5, filler);
            inv.setItem(6, filler);
            inv.setItem(7, filler);
            inv.setItem(8, filler);
            
            Iterator<String> it2 = PerkInfo.SharpshooterPerkNames.iterator();
            while (it2.hasNext())
            {
                String perkName = it2.next();
                Material upgradeMaterial = PerkInfo.upgradeMaterial;
                ItemStack stack = new ItemStack(upgradeMaterial);
                ItemMeta meta = stack.getItemMeta();
                meta.setDisplayName("Sharpshooter - Click to level up: " + perkName);
                stack.setItemMeta(meta);
                inv.addItem(stack);
            }
            
            inv.setItem(13, filler);
            inv.setItem(14, filler);
            inv.setItem(15, filler);
            inv.setItem(16, filler);
            inv.setItem(17, filler);
        } // end Medic
        
        player.openInventory(inv);
    }
    
    
}
