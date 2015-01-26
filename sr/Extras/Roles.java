package sr.Extras;

import java.util.HashMap;
import java.util.HashSet;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import sr.SRKF;

/**
 *
 * @author Cross
 */
public class Roles 
{
    public SRKF plugin;
    
    public Roles (SRKF plugin)
    {
        this.plugin = plugin;
    }
    
    public static double respecCoinCost = 5;
    
    public static HashSet<String> roleNames = new HashSet<>();
    
    static
    {
        roleNames.add("Medic");
        roleNames.add("Commando");
        roleNames.add("Engineer");
        roleNames.add("Berserker");
        roleNames.add("Sharpshooter");
    }
    
    public static HashMap<String, Material> roleItemMaterial = new HashMap<>();
    
    static
    {
        roleItemMaterial.put("Medic", Material.POTION);
        roleItemMaterial.put("Commando", Material.IRON_CHESTPLATE);
        roleItemMaterial.put("Engineer", Material.REDSTONE);
        roleItemMaterial.put("Berserker", Material.DIAMOND_SWORD);
        roleItemMaterial.put("Sharpshooter", Material.BOW);
    }
    
    public static HashMap<String, String> roleDescriptions = new HashMap<>();
    
    static
    {
        roleDescriptions.put("Medic", "Self-Sufficient and battle tested,");
        roleDescriptions.put("Medic_more", "the Medic can stand on their own");
        roleDescriptions.put("Medic_more2", "as well as aid their allies.");
        
        roleDescriptions.put("Commando", "A natural leader, the Commando can ");
        roleDescriptions.put("Commando_more", "give as good as they take. Always");
        roleDescriptions.put("Commando_more2", "in the front lines calling the shots.");
        
        roleDescriptions.put("Engineer", "Smart, sophisticated and prone to ");
        roleDescriptions.put("Engineer_more", "blowing things up, the Engineer");
        roleDescriptions.put("Engineer_more2", "has many tools to help the team.");
        
        roleDescriptions.put("Berserker", "Someone once said that Berserkers were.");
        roleDescriptions.put("Berserker_more", "fierce, fearless and ferocious.");
        roleDescriptions.put("Berserker_more2", "I think they're just crazy.");
        
        roleDescriptions.put("Sharpshooter", "The Sharpshooter can shoot a flea off");
        roleDescriptions.put("Sharpshooter_more", "a dogs fur from a mile away. They");
        roleDescriptions.put("Sharpshooter_more2", "could also miss... if they wanted to.");
    }
    
    public static HashMap<String, String> roleDetailedInfo = new HashMap<>();
    
    static
    {
        roleDetailedInfo.put("Medic", ChatColor.DARK_GRAY + "************ " + ChatColor.GREEN + "Medic Information" + ChatColor.DARK_GRAY + " ************\n\n"
                + ChatColor.GOLD + "The " + ChatColor.YELLOW + "Medic" + ChatColor.GOLD + " gets increased bonuses to " + ChatColor.YELLOW + "Shotguns and Healing" 
                + ChatColor.GOLD +" capabilities. "
                + "\n" + ChatColor.RED + "Restricted to " + ChatColor.DARK_AQUA + "Bows/Crossbows, Pistols " + ChatColor.RED + "and " + ChatColor.DARK_AQUA +"Shotguns" + ChatColor.RED +" only.\n\n"
                + ChatColor.LIGHT_PURPLE + "- Medical Training: " + ChatColor.GOLD + "Increase healing by " + ChatColor.YELLOW + "+1" + ChatColor.GOLD +" for every point of Medical Kits spent.\n"
                + ChatColor.LIGHT_PURPLE + "- Shotgun Mastery: " + ChatColor.GOLD + "Increase damage by " + ChatColor.YELLOW + "+1" + ChatColor.GOLD +" when using Shotguns for every point of Shotgun Mastery spent.\n"
                + ChatColor.LIGHT_PURPLE + "- Triage: " + ChatColor.GOLD + "When reviving a player who is downed, bring them back with " + ChatColor.YELLOW + "+1" + ChatColor.GOLD +" heart for every point of Triage spent (Temporary Bonus Health over max can be obtained).\n"
                + ChatColor.LIGHT_PURPLE + "- Combat Armor: " + ChatColor.GOLD + "The " + ChatColor.YELLOW + "Medic" + ChatColor.GOLD + " gains temporary absorption after healing themselves, roughly " 
                + ChatColor.YELLOW + "+2 hearts" + ChatColor.GOLD + " per point of Combat Armor.");
        
        roleDetailedInfo.put("Commando", ChatColor.DARK_GRAY + "************ " + ChatColor.GREEN + "Commando Information" + ChatColor.DARK_GRAY + " ************\n\n"
                + ChatColor.GOLD + "The " + ChatColor.YELLOW + "Commando" + ChatColor.GOLD + " gets increased bonuses to " + ChatColor.YELLOW + "Damage Reduction"
                + ChatColor.GOLD + " and all " + ChatColor.YELLOW + "Ranged Weaponry. " + ChatColor.GOLD
                + "\n" + ChatColor.RED + "No restrictions other than cannot use exclusive Berserker Melee weaponry or Artillery.\n\n"
                + ChatColor.LIGHT_PURPLE + "- Ranger Instructor: " + ChatColor.GOLD + "Increase Ranged damage for self and nearby allies by " + ChatColor.YELLOW +"+3%" + ChatColor.GOLD +" point of Ranger Training spent.\n"
                + ChatColor.LIGHT_PURPLE + "- Pain: " + ChatColor.GOLD + "For every hit taken, reflect " + ChatColor.YELLOW + "+1" + ChatColor.GOLD + " per point of Pain spent.\n"
                + ChatColor.LIGHT_PURPLE + "- Juggernaut: " + ChatColor.GOLD + "Permanently increase your health by " + ChatColor.YELLOW + "1" + ChatColor.GOLD + " per point of Juggernaut spent.\n"
                + ChatColor.LIGHT_PURPLE + "- Inspire: " + ChatColor.GOLD + "The " + ChatColor.YELLOW + "Commando" + ChatColor.GOLD + " occasionally shouts some words of inspiration, increasing either the movement speed, damage or protection of all nearby allies temporarily.\n"
                + ChatColor.GOLD + " Duration of the bonus increased by " + ChatColor.YELLOW + "1 second" + ChatColor.GOLD + " per point of Inspire spent.\n"
                + ChatColor.GOLD + " Frequency of the shout increased by " + ChatColor.YELLOW + "1 second" + ChatColor.GOLD + " per point of Inspire spent.");
        
        roleDetailedInfo.put("Engineer", ChatColor.DARK_GRAY + "************ " + ChatColor.GREEN + "Engineer Information" + ChatColor.DARK_GRAY + " ************\n\n"
                + ChatColor.GOLD + "The " + ChatColor.YELLOW + "Engineer" + ChatColor.GOLD + " gets increased bonuses to " + ChatColor.YELLOW + "Explosives"
                + ChatColor.GOLD + " and " + ChatColor.YELLOW + "Increased Carrying Capacity. " + ChatColor.GOLD 
                + "\n" + ChatColor.RED + "Restricted to " + ChatColor.DARK_AQUA + "Secondary Melee Weapons, Rifles, Explosives" + ChatColor.RED + " and " + ChatColor.DARK_AQUA + "Artillery " + ChatColor.RED + "only.\n\n"
                + ChatColor.LIGHT_PURPLE + "- Ballistic Training: " + ChatColor.GOLD + "Increases your max clip amount by " + ChatColor.YELLOW +"1 per 2" + ChatColor.GOLD +" points of Ballistic Training spent.\n"
                + ChatColor.LIGHT_PURPLE + "- Welding: " + ChatColor.GOLD + "Increases your welding amount by " + ChatColor.YELLOW + "+1" + ChatColor.GOLD + " per point spent.\n"
                + ChatColor.LIGHT_PURPLE + "- Demolition Mastery: " + ChatColor.GOLD + "Increases all damage dealt from explosives by " + ChatColor.YELLOW + "+1" + ChatColor.GOLD + " per point spent.\n"
                + ChatColor.LIGHT_PURPLE + "- Heavy Artillery: " + ChatColor.GOLD + "The " + ChatColor.YELLOW + "Engineer" + ChatColor.GOLD + " can unlock the use of the Bazooka at 2 points and the L.A.W. (Light Anti-Tank Weapon) at 8 points spent.\n"
                + ChatColor.GOLD + "Also passively increases damage with the Bazooka and L.A.W. by +3 damager per point of Heavy Artillery spent.");
        
        roleDetailedInfo.put("Berserker", ChatColor.DARK_GRAY + "************ " + ChatColor.GREEN + "Berserker Information" + ChatColor.DARK_GRAY + " ************\n\n"
                + ChatColor.GOLD + "The " + ChatColor.YELLOW + "Berserker" + ChatColor.GOLD + " gets increased bonuses to " + ChatColor.YELLOW + "Melee Combat"
                + ChatColor.GOLD + " and " + ChatColor.YELLOW + "Movement Speed/Hit & Run tactics." + ChatColor.GOLD 
                + "\n" + ChatColor.RED + "Restricted to " + ChatColor.DARK_AQUA + "Primary Melee Weapons" + ChatColor.RED + " and " + ChatColor.DARK_AQUA + "Pistols" + ChatColor.RED + " only.\n\n"
                + ChatColor.LIGHT_PURPLE + "- Endurance: " + ChatColor.GOLD + "Increases your passive movement speed by " + ChatColor.YELLOW +"0.33%" + ChatColor.GOLD +" boost level per point spent.\n"
                + ChatColor.LIGHT_PURPLE + "- Strength: " + ChatColor.GOLD + "Increases your damage dealt with Melee Weapons by " + ChatColor.YELLOW + "+1" + ChatColor.GOLD + " per point spent. At 4 points, unlocks exclusive Berserker weapons.\n"
                + ChatColor.LIGHT_PURPLE + "- Kevlar Adaptation: " + ChatColor.GOLD + "Reduces all damage taken by " + ChatColor.YELLOW + "5%" + ChatColor.GOLD + " per point of Kevlar Adaptation spent.\n"
                + ChatColor.LIGHT_PURPLE + "- Ferocity: " + ChatColor.GOLD + "The " + ChatColor.YELLOW + "Berserker" + ChatColor.GOLD + " deals 150% damage with Melee to a target from behind.\n"
                + ChatColor.GOLD + "Additionally, for every 1 heart of health you are missing, deal " + ChatColor.YELLOW + "+5% additional multiplier" + ChatColor.GOLD + " per point spent in Ferocity.");
        
        roleDetailedInfo.put("Sharpshooter", ChatColor.DARK_GRAY + "************ " + ChatColor.GREEN + "Sharpshooter Information" + ChatColor.DARK_GRAY + " ************\n\n"
                + ChatColor.GOLD + "The " + ChatColor.YELLOW + "Sharpshooter" + ChatColor.GOLD + " gets increased bonuses to " + ChatColor.YELLOW + "Rifles, Bows/Crossbows"
                + ChatColor.GOLD + " and " + ChatColor.YELLOW + "Pistols." + ChatColor.GOLD
                + "\n" + ChatColor.RED + "Restricted to " + ChatColor.DARK_AQUA + "Ranged Weaponry" + ChatColor.RED + " and " + ChatColor.DARK_AQUA + "Secondary Melee Weapons" + ChatColor.RED + " only.\n\n"
                + ChatColor.LIGHT_PURPLE + "- Marksman Training: " + ChatColor.GOLD + "Increase Reload Speed by " + ChatColor.YELLOW +"5%" + ChatColor.GOLD +" per point of Marksman Training spent.\n"
                + ChatColor.LIGHT_PURPLE + "- Called Shot: " + ChatColor.GOLD + "Increases headshot damage by " + ChatColor.YELLOW + "8% " + ChatColor.GOLD + " per every point of Called Shot spent.\n"
                + ChatColor.LIGHT_PURPLE + "- Lucky: " + ChatColor.GOLD + "You never quite seem to run out of ammo, increasing chance to find 1 clip of ammo by " + ChatColor.YELLOW + "1%" + ChatColor.GOLD + " per point of Lucky spent.\n"
                + ChatColor.LIGHT_PURPLE + "- Wound: " + ChatColor.GOLD + "The " + ChatColor.YELLOW + "Sharpshooter" + ChatColor.GOLD + " will occasionally bleed big priority targets, dealing 5% "
                + ChatColor.YELLOW + "+1%" + ChatColor.GOLD + " (per point) of the targets maximum health every second over 5 seconds.");
    }
    
}
