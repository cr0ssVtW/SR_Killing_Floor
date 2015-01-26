package sr.Extras;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import sr.SRKF;

/**
 *
 * @author Cross
 */
public class PerkInfo 
{
    public SRKF plugin;
    
    public PerkInfo(SRKF plugin)
    {
        this.plugin = plugin;
    }
    
    /*
     * Begin Berserker
     */
    public static int defaultPoints = 3;
    public static int MaxPerkLevel = 10;
    
    public static Material upgradeMaterial = Material.FIREWORK;
    
    public static HashMap<String, Material> BerserkerPerkItemMaterial = new HashMap<>();
    static
    {
        BerserkerPerkItemMaterial.put("Endurance", Material.GOLD_BOOTS);
        BerserkerPerkItemMaterial.put("Strength", Material.IRON_AXE);
        BerserkerPerkItemMaterial.put("KevlarAdaptation", Material.IRON_CHESTPLATE);
        BerserkerPerkItemMaterial.put("Ferocity", Material.DIAMOND_AXE);
    }
    public static HashSet<String> BerserkerPerkNames = new HashSet<>();
    static
    {
        BerserkerPerkNames.add("Endurance");
        BerserkerPerkNames.add("Strength");
        BerserkerPerkNames.add("KevlarAdaptation");
        BerserkerPerkNames.add("Ferocity");
    }
    
    public static HashMap<String, String> BerserkerPerkDescriptions = new HashMap<>();
    static
    {
        BerserkerPerkDescriptions.put("Endurance", ChatColor.GOLD + "Increases your passive movement speed");
        BerserkerPerkDescriptions.put("Endurance2", ChatColor.GOLD + "by " + ChatColor.YELLOW +"+1 boost" + ChatColor.GOLD +" per 3 points.");
        BerserkerPerkDescriptions.put("Strength", ChatColor.GOLD + "Increases your damage dealt with Melee");
        BerserkerPerkDescriptions.put("Strength2", ChatColor.GOLD + "Weapons by " + ChatColor.YELLOW + "+1" + ChatColor.GOLD + " per point.");
        BerserkerPerkDescriptions.put("KevlarAdaptation", ChatColor.GOLD + "Reduces all damage taken by");
        BerserkerPerkDescriptions.put("KevlarAdaptation2", ChatColor.YELLOW + "+5%" + ChatColor.GOLD + " per point.");
        BerserkerPerkDescriptions.put("Ferocity", ChatColor.GOLD + "The " + ChatColor.YELLOW + "Berserker" + ChatColor.GOLD + " deals 150% damage with");
        BerserkerPerkDescriptions.put("Ferocity2", ChatColor.GOLD + "Melee to a target from behind. Additionally, for ");
        BerserkerPerkDescriptions.put("Ferocity3", ChatColor.GOLD + "every 1 heart of health you are missing, deal");
        BerserkerPerkDescriptions.put("Ferocity4", ChatColor.YELLOW + "+5% additional multiplier" + ChatColor.GOLD + " per point.");
    }
    
    public static HashMap<String, ItemStack> BerserkerPerkItemStack = new HashMap<>();
    static
    {
        craftBerserkerPerkStack("Endurance");
        craftBerserkerPerkStack("Strength");
        craftBerserkerPerkStack("KevlarAdaptation");
        craftBerserkerPerkStack("Ferocity");
    }
    /*
     * end Berserker
     * 
     * Begin Commando
     */
    
    public static HashMap<String, Material> CommandoPerkItemMaterial = new HashMap<>();
    static
    {
        CommandoPerkItemMaterial.put("RangerInstructor", Material.ARROW);
        CommandoPerkItemMaterial.put("Pain", Material.REDSTONE);
        CommandoPerkItemMaterial.put("Juggernaut", Material.GOLDEN_APPLE);
        CommandoPerkItemMaterial.put("Inspire", Material.NOTE_BLOCK);
    }
    
    public static HashSet<String> CommandoPerkNames = new HashSet<>();
    static
    {
        CommandoPerkNames.add("RangerInstructor");
        CommandoPerkNames.add("Pain");
        CommandoPerkNames.add("Juggernaut");
        CommandoPerkNames.add("Inspire");
    }
    
    public static HashMap<String, String> CommandoPerkDescriptions = new HashMap<>();
    static
    {
        CommandoPerkDescriptions.put("RangerInstructor", ChatColor.GOLD + "Increases Ranged Weapon damage of you and ");
        CommandoPerkDescriptions.put("RangerInstructor2", ChatColor.GOLD + "allies by " + ChatColor.YELLOW +"+3%" + ChatColor.GOLD +" per point.");
        CommandoPerkDescriptions.put("Pain", ChatColor.GOLD + "Every time you take damage");
        CommandoPerkDescriptions.put("Pain2", ChatColor.GOLD + "reflect " + ChatColor.YELLOW + "+1" + ChatColor.GOLD + " damage per point.");
        CommandoPerkDescriptions.put("Juggernaut", ChatColor.GOLD + "Increases base health by ");
        CommandoPerkDescriptions.put("Juggernaut2", ChatColor.YELLOW + "+1" + ChatColor.GOLD + " per point.");
        CommandoPerkDescriptions.put("Inspire", ChatColor.GOLD + "The " + ChatColor.YELLOW + "Commando" + ChatColor.GOLD + " occasionally shouts");
        CommandoPerkDescriptions.put("Inspire2", ChatColor.GOLD + "some words of inspiration, increasing either");
        CommandoPerkDescriptions.put("Inspire3", ChatColor.GOLD + "the movement speed, damage or protection");
        CommandoPerkDescriptions.put("Inspire4", ChatColor.GOLD + "of all nearby allies.");
        CommandoPerkDescriptions.put("Inspire5", ChatColor.GOLD + "Duration of the bonus increased by");
        CommandoPerkDescriptions.put("Inspire6", ChatColor.YELLOW + "1 second" + ChatColor.GOLD + " per point.");
        CommandoPerkDescriptions.put("Inspire7", ChatColor.GOLD + "Frequency of the shout increased by");
        CommandoPerkDescriptions.put("Inspire8", ChatColor.YELLOW + "1 second" + ChatColor.GOLD + " per point.");
    }
    
    public static HashMap<String, ItemStack> CommandoPerkItemStack = new HashMap<>();
    static
    {
        craftCommandoPerkStack("RangerInstructor");
        craftCommandoPerkStack("Pain");
        craftCommandoPerkStack("Juggernaut");
        craftCommandoPerkStack("Inspire");
    }
    /*
     * End Commando
     * 
     * Begin Engineer
     */
    
    public static HashMap<String, Material> EngineerPerkItemMaterial = new HashMap<>();
    static
    {
        EngineerPerkItemMaterial.put("BallisticTraining", Material.ARROW);
        EngineerPerkItemMaterial.put("Welding", Material.BLAZE_ROD);
        EngineerPerkItemMaterial.put("DemolitionMastery", Material.TNT);
        EngineerPerkItemMaterial.put("HeavyArtillery", Material.HOPPER);
    }
    
    public static HashSet<String> EngineerPerkNames = new HashSet<>();
    static
    {
        EngineerPerkNames.add("BallisticTraining");
        EngineerPerkNames.add("Welding");
        EngineerPerkNames.add("DemolitionMastery");
        EngineerPerkNames.add("HeavyArtillery");
    }
    
    public static HashMap<String, String> EngineerPerkDescriptions = new HashMap<>();
    static
    {
        EngineerPerkDescriptions.put("BallisticTraining", ChatColor.GOLD + "Increases your maximum clips by");
        EngineerPerkDescriptions.put("BallisticTraining2", ChatColor.YELLOW +"+1" + ChatColor.GOLD +" per 2 points.");
        EngineerPerkDescriptions.put("Welding", ChatColor.GOLD + "Increases your welding amount by");
        EngineerPerkDescriptions.put("Welding2", ChatColor.YELLOW + "+1" + ChatColor.GOLD + " per point spent.");
        EngineerPerkDescriptions.put("DemolitionMastery", ChatColor.GOLD + "Increases all damage dealt from explosives");
        EngineerPerkDescriptions.put("DemolitionMastery2", ChatColor.GOLD + "by " + ChatColor.YELLOW + "+1" + ChatColor.GOLD + " per point spent.");
        EngineerPerkDescriptions.put("HeavyArtillery", ChatColor.GOLD + "The " + ChatColor.YELLOW + "Engineer" + ChatColor.GOLD + " can unlock the use");
        EngineerPerkDescriptions.put("HeavyArtillery2", ChatColor.GOLD + "of the Bazooka at 2 points, and the");
        EngineerPerkDescriptions.put("HeavyArtillery3", ChatColor.GOLD + "L.A.W. (Light Anti-armour Weapon) at 8 points.");
        EngineerPerkDescriptions.put("HeavyArtillery4", ChatColor.GOLD + "Also passively increases damage with the Bazooka");
        EngineerPerkDescriptions.put("HeavyArtillery5", ChatColor.GOLD + "and L.A.W. by " + ChatColor.YELLOW + "+3" + ChatColor.GOLD + " per point.");
    }
    
    public static HashMap<String, ItemStack> EngineerPerkItemStack = new HashMap<>();
    static
    {
        craftEngineerPerkStack("BallisticTraining");
        craftEngineerPerkStack("Welding");
        craftEngineerPerkStack("DemolitionMastery");
        craftEngineerPerkStack("HeavyArtillery");
    }
    
    /*
     * End Engineer
     * 
     * Begin Medic
     */
    
    public static HashMap<String, Material> MedicPerkItemMaterial = new HashMap<>();
    static
    {
        MedicPerkItemMaterial.put("MedicalTraining", Material.PAPER);
        MedicPerkItemMaterial.put("ShotgunMastery", Material.IRON_AXE);
        MedicPerkItemMaterial.put("Triage", Material.SHEARS);
        MedicPerkItemMaterial.put("CombatArmor", Material.GOLD_CHESTPLATE);
    }
   
    public static HashSet<String> MedicPerkNames = new HashSet<>();
    static
    {
        MedicPerkNames.add("MedicalTraining");
        MedicPerkNames.add("ShotgunMastery");
        MedicPerkNames.add("Triage");
        MedicPerkNames.add("CombatArmor");
    }
    
    public static HashMap<String, String> MedicPerkDescriptions = new HashMap<>();
    static
    {
        MedicPerkDescriptions.put("MedicalTraining", ChatColor.GOLD + "Increases Med Kit healing");
        MedicPerkDescriptions.put("MedicalTraining2", ChatColor.GOLD + "by " + ChatColor.YELLOW + "+1" + ChatColor.GOLD +" per point.");
        MedicPerkDescriptions.put("ShotgunMastery", ChatColor.GOLD + "Increases your Shotgun damage");
        MedicPerkDescriptions.put("ShotgunMastery2", ChatColor.GOLD + "by " + ChatColor.YELLOW + "+1" + ChatColor.GOLD +" per point.");
        MedicPerkDescriptions.put("Triage", ChatColor.GOLD + "When reviving a player who is downed, bring");
        MedicPerkDescriptions.put("Triage2", ChatColor.GOLD + "them back with " + ChatColor.YELLOW + "+1" + ChatColor.GOLD +" heart per point.");
        MedicPerkDescriptions.put("Triage3", ChatColor.GOLD + "(Temporary Bonus Health over max can be obtained)");
        MedicPerkDescriptions.put("CombatArmor", ChatColor.GOLD + "The " + ChatColor.YELLOW + "Medic" + ChatColor.GOLD + " gains temporary absorption");
        MedicPerkDescriptions.put("CombatArmor2", ChatColor.GOLD + "after healing themselves, roughly equal to");
        MedicPerkDescriptions.put("CombatArmor3", ChatColor.YELLOW + "+2 hearts" + ChatColor.GOLD + " per point of Combat Armor.");
    }
    
    public static HashMap<String, ItemStack> MedicPerkItemStack = new HashMap<>();
    static
    {
        craftMedicPerkStack("MedicalTraining");
        craftMedicPerkStack("ShotgunMastery");
        craftMedicPerkStack("Triage");
        craftMedicPerkStack("CombatArmor");
    }
    
    /*
     * End Medic
     * 
     * Begin Sharpshooter
     */
    
    public static HashMap<String, Material> SharpshooterPerkItemMaterial = new HashMap<>();
    static
    {
        SharpshooterPerkItemMaterial.put("MarksmanTraining", Material.BOW);
        SharpshooterPerkItemMaterial.put("CalledShot", Material.EYE_OF_ENDER);
        SharpshooterPerkItemMaterial.put("Lucky", Material.ARROW);
        SharpshooterPerkItemMaterial.put("Wound", Material.REDSTONE);
    }
    
    public static HashSet<String> SharpshooterPerkNames = new HashSet<>();
    static
    {
        SharpshooterPerkNames.add("MarksmanTraining");
        SharpshooterPerkNames.add("CalledShot");
        SharpshooterPerkNames.add("Lucky");
        SharpshooterPerkNames.add("Wound");
    }
    
    public static HashMap<String, String> SharpshooterPerkDescriptions = new HashMap<>();
    static
    {
        SharpshooterPerkDescriptions.put("MarksmanTraining", ChatColor.GOLD + "Increases your Reload Speed");
        SharpshooterPerkDescriptions.put("MarksmanTraining2", ChatColor.GOLD + "by " + ChatColor.YELLOW +"+5%" + ChatColor.GOLD +" per point.");
        SharpshooterPerkDescriptions.put("CalledShot", ChatColor.GOLD + "Increases your headshot damage");
        SharpshooterPerkDescriptions.put("CalledShot2", ChatColor.GOLD + "by " + ChatColor.YELLOW + "+8%" + ChatColor.GOLD + " per point.");
        SharpshooterPerkDescriptions.put("Lucky", ChatColor.GOLD + "Increases chance to find extra ammo");
        SharpshooterPerkDescriptions.put("Lucky2", ChatColor.GOLD + "clip by " + ChatColor.YELLOW + "+1%" + ChatColor.GOLD + " per point.");
        SharpshooterPerkDescriptions.put("Wound", ChatColor.GOLD + "The " + ChatColor.YELLOW + "Sharpshooter" + ChatColor.GOLD + " will occasionally");
        SharpshooterPerkDescriptions.put("Wound2", ChatColor.GOLD + "bleed big priority targets, dealing 5%,");
        SharpshooterPerkDescriptions.put("Wound3", ChatColor.YELLOW + "+1%" + ChatColor.GOLD + " per point, of the targets");
        SharpshooterPerkDescriptions.put("Wound4", ChatColor.GOLD + "maximum health every second over 5 seconds.");
    }
    
    public static HashMap<String, ItemStack> SharpshooterPerkItemStack = new HashMap<>();
    static
    {
        craftSharpshooterPerkStack("MarksmanTraining");
        craftSharpshooterPerkStack("CalledShot");
        craftSharpshooterPerkStack("Lucky");
        craftSharpshooterPerkStack("Wound");
    }
    
    /*
     * End Sharpshooter
     */
    
    private static void craftBerserkerPerkStack(String perkName)
    {
        Material perkNameMat = BerserkerPerkItemMaterial.get(perkName);
        ItemStack perkNameStack = new ItemStack(perkNameMat);
        ItemMeta perkNameStackMeta = perkNameStack.getItemMeta();
        perkNameStackMeta.setDisplayName(perkName);
        ArrayList<String> perkNameLore = new ArrayList<>();
        String perkNameLore1 = BerserkerPerkDescriptions.get(perkName);
        perkNameLore.add(perkNameLore1);
        
        String perkNameLore2 = BerserkerPerkDescriptions.get(perkName+"2");
            perkNameLore.add(perkNameLore2);
        
        if (perkName.equalsIgnoreCase("Ferocity"))
        {
            String perkNameLore3 = BerserkerPerkDescriptions.get(perkName+"3");
            perkNameLore.add(perkNameLore3);
            String perkNameLore4 = BerserkerPerkDescriptions.get(perkName+"4");
            perkNameLore.add(perkNameLore4);
        }
        
        perkNameStackMeta.setLore(perkNameLore);
        perkNameStack.setItemMeta(perkNameStackMeta);
        
        BerserkerPerkItemStack.put(perkName, perkNameStack);
    }
    
    private static void craftCommandoPerkStack(String perkName)
    {
        Material perkNameMat = CommandoPerkItemMaterial.get(perkName);
        ItemStack perkNameStack = new ItemStack(perkNameMat);
        ItemMeta perkNameStackMeta = perkNameStack.getItemMeta();
        perkNameStackMeta.setDisplayName(perkName);
        ArrayList<String> perkNameLore = new ArrayList<>();
        String perkNameLore1 = CommandoPerkDescriptions.get(perkName);
        perkNameLore.add(perkNameLore1);
        
        String perkNameLore2 = CommandoPerkDescriptions.get(perkName+"2");
        perkNameLore.add(perkNameLore2);
            
        if (perkName.equalsIgnoreCase("Inspire"))
        {
            String perkNameLore3 = CommandoPerkDescriptions.get(perkName+"3");
            perkNameLore.add(perkNameLore3);
            String perkNameLore4 = CommandoPerkDescriptions.get(perkName+"4");
            perkNameLore.add(perkNameLore4);
            String perkNameLore5 = CommandoPerkDescriptions.get(perkName+"5");
            perkNameLore.add(perkNameLore5);
            String perkNameLore6 = CommandoPerkDescriptions.get(perkName+"6");
            perkNameLore.add(perkNameLore6);
            String perkNameLore7 = CommandoPerkDescriptions.get(perkName+"7");
            perkNameLore.add(perkNameLore7);
            String perkNameLore8 = CommandoPerkDescriptions.get(perkName+"8");
            perkNameLore.add(perkNameLore8);
        }
        perkNameStackMeta.setLore(perkNameLore);
        perkNameStack.setItemMeta(perkNameStackMeta);
        
        CommandoPerkItemStack.put(perkName, perkNameStack);
    }
    
    private static void craftEngineerPerkStack(String perkName)
    {
        Material perkNameMat = EngineerPerkItemMaterial.get(perkName);
        ItemStack perkNameStack = new ItemStack(perkNameMat);
        ItemMeta perkNameStackMeta = perkNameStack.getItemMeta();
        perkNameStackMeta.setDisplayName(perkName);
        ArrayList<String> perkNameLore = new ArrayList<>();
        String perkNameLore1 = EngineerPerkDescriptions.get(perkName);
        perkNameLore.add(perkNameLore1);
        
        String perkNameLore2 = EngineerPerkDescriptions.get(perkName+"2");
        perkNameLore.add(perkNameLore2);
            
        if (perkName.equalsIgnoreCase("HeavyArtillery"))
        {
            String perkNameLore3 = EngineerPerkDescriptions.get(perkName+"3");
            perkNameLore.add(perkNameLore3);
            String perkNameLore4 = EngineerPerkDescriptions.get(perkName+"4");
            perkNameLore.add(perkNameLore4);
            String perkNameLore5 = EngineerPerkDescriptions.get(perkName+"5");
            perkNameLore.add(perkNameLore5);
        }
        perkNameStackMeta.setLore(perkNameLore);
        perkNameStack.setItemMeta(perkNameStackMeta);
        
        EngineerPerkItemStack.put(perkName, perkNameStack);
    }
    
    private static void craftMedicPerkStack(String perkName)
    {
        Material perkNameMat = MedicPerkItemMaterial.get(perkName);
        ItemStack perkNameStack = new ItemStack(perkNameMat);
        ItemMeta perkNameStackMeta = perkNameStack.getItemMeta();
        perkNameStackMeta.setDisplayName(perkName);
        ArrayList<String> perkNameLore = new ArrayList<>();
        String perkNameLore1 = MedicPerkDescriptions.get(perkName);
        perkNameLore.add(perkNameLore1);
        
        String perkNameLore2 = MedicPerkDescriptions.get(perkName+"2");
        perkNameLore.add(perkNameLore2);
            
        if (perkName.equalsIgnoreCase("Triage"))
        {
            String perkNameLore3 = MedicPerkDescriptions.get(perkName+"3");
            perkNameLore.add(perkNameLore3);
        }
        if (perkName.equalsIgnoreCase("CombatArmor"))
        {
            String perkNameLore3 = MedicPerkDescriptions.get(perkName+"3");
            perkNameLore.add(perkNameLore3);
        }
        
        perkNameStackMeta.setLore(perkNameLore);
        perkNameStack.setItemMeta(perkNameStackMeta);
        
        MedicPerkItemStack.put(perkName, perkNameStack);
    }
    
    private static void craftSharpshooterPerkStack(String perkName)
    {
        Material perkNameMat = SharpshooterPerkItemMaterial.get(perkName);
        ItemStack perkNameStack = new ItemStack(perkNameMat);
        ItemMeta perkNameStackMeta = perkNameStack.getItemMeta();
        perkNameStackMeta.setDisplayName(perkName);
        ArrayList<String> perkNameLore = new ArrayList<>();
        String perkNameLore1 = SharpshooterPerkDescriptions.get(perkName);
        perkNameLore.add(perkNameLore1);
        String perkNameLore2 = SharpshooterPerkDescriptions.get(perkName+"2");
        perkNameLore.add(perkNameLore2);
        if (perkName.equalsIgnoreCase("Wound"))
        {
            String perkNameLore3 = SharpshooterPerkDescriptions.get(perkName+"3");
            perkNameLore.add(perkNameLore3);
            String perkNameLore4 = SharpshooterPerkDescriptions.get(perkName+"4");
            perkNameLore.add(perkNameLore4);
        }
        perkNameStackMeta.setLore(perkNameLore);
        perkNameStack.setItemMeta(perkNameStackMeta);
        
        SharpshooterPerkItemStack.put(perkName, perkNameStack);
    }
    
}
