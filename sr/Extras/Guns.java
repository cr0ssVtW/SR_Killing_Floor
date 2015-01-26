package sr.Extras;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import sr.SRKF;

/**
 *
 * @author Cross
 */
public class Guns implements Listener
{
    public SRKF plugin;
    
    public Guns (SRKF plugin)
    {
        this.plugin = plugin;
    }
    public static final double multi = 3;
    public static int maxMedKits = 5;
    public static int maxGrenades = 3;
    
    public static HashSet<String> DemoCheck = new HashSet<>();
    public static HashMap<String, ItemStack> DemoList = new HashMap<>();
    public static HashMap<String, Integer> DemoCost = new HashMap<>();
    public static HashMap<String, String> DemoType = new HashMap<>();
    public static HashMap<String, Integer> DemoAmmo = new HashMap<>();
    public static HashMap<String, Double> DemoDamage = new HashMap<>();
    public static HashMap<String, Integer> DemoReload = new HashMap<>();
    
    public static HashMap<String, ItemStack> MeleeList = new HashMap<>();
    public static HashSet<String> MeleeCheck = new HashSet<>();
    public static HashMap<String, Double> MeleeDamage = new HashMap<>();
    public static HashMap<String, Double> MeleeRange = new HashMap<>();
    public static HashMap<String, Integer> MeleeCost = new HashMap<>();
    public static HashMap<String, Integer> MeleeDelay = new HashMap<>();
    
    
    public static HashMap<String, ItemStack> GunList = new HashMap<>();
    public static HashSet<String> GunCheck = new HashSet<>();
    public static HashMap<String, Double> GunDamage = new HashMap<>();
    public static HashMap<String, Long> GunDelay = new HashMap<>();
    public static HashMap<String, Integer> GunAmmo = new HashMap<>();
    public static HashMap<String, Integer> GunReload = new HashMap<>();
    public static HashMap<String, String> GunType = new HashMap<>();
    public static HashMap<String, Integer> GunCost = new HashMap<>();

    
    // Ammo STuff
    public static HashMap<String, Material> ammoTypes = new HashMap<>();
    static
    {
        ammoTypes.put("Pistol", Material.SNOW_BALL);
        ammoTypes.put("Rifle", Material.BLAZE_POWDER);
        ammoTypes.put("SMG", Material.COCOA);
        ammoTypes.put("Shotgun", Material.INK_SACK);
        ammoTypes.put("Bow", Material.INK_SACK);
        ammoTypes.put("Artillery", Material.STICK);
    }
    
    public static HashMap<Material, String> ammoTypeByMaterial = new HashMap<>();
    static
    {
        ammoTypeByMaterial.put(Material.SNOW_BALL, "Pistol");
        ammoTypeByMaterial.put(Material.BLAZE_POWDER, "Rifle");
        ammoTypeByMaterial.put(Material.COCOA, "SMG");
        ammoTypeByMaterial.put(Material.INK_SACK, "Shotgun");
        ammoTypeByMaterial.put(Material.INK_SACK, "Bow");
        ammoTypeByMaterial.put(Material.STICK, "Artillery");
    }
    
    public static HashMap<String, Integer> ammoCounts = new HashMap<>();
    static
    {
        ammoCounts.put("Pistol", 4);
        ammoCounts.put("Rifle", 3);
        ammoCounts.put("SMG", 3);
        ammoCounts.put("Shotgun", 12);
        ammoCounts.put("Bow", 15);
        ammoCounts.put("Artillery", 2);
    }
    
    public static HashMap<Material, Integer> ammoMaterialCounts = new HashMap<>();
    static
    {
        Material Pistol = Material.SNOW_BALL;
        Material Rifle = Material.BLAZE_POWDER;
        Material SMG = Material.COCOA;
        Material Shotgun = Material.INK_SACK;
        Material Bow = Material.INK_SACK;
        Material Artillery = Material.STICK;
        ammoMaterialCounts.put(Pistol, 4);
        ammoMaterialCounts.put(Rifle, 3);
        ammoMaterialCounts.put(SMG, 3);
        ammoMaterialCounts.put(Shotgun, 15);
        ammoMaterialCounts.put(Bow, 15);
        ammoMaterialCounts.put(Artillery, 2);
    }
    
    public static HashMap<String, Integer> miscItemCosts = new HashMap<>();
    static
    {
        miscItemCosts.put("Medical Kit", 100);
        miscItemCosts.put("Ammo", 125);
    }
    
    public static HashMap<String, ItemStack> miscItemStacks = new HashMap<>();
    static
    {
        ItemStack ammo_pistol = new ItemStack(ammoTypes.get("Pistol"));
        ItemMeta pistolMeta = ammo_pistol.getItemMeta();
        pistolMeta.setDisplayName(ChatColor.GOLD + "Pistol Ammo Clip");
        ArrayList<String> pistolAmmoLore = new ArrayList<>();
        String pistolAmmoType = ChatColor.GRAY + "Misc";
        String pistolAmmoName = "Ammo";
        pistolAmmoLore.add(pistolAmmoType);
        pistolAmmoLore.add(pistolAmmoName);
        pistolMeta.setLore(pistolAmmoLore);
        ammo_pistol.setItemMeta(pistolMeta);
        //
        ItemStack ammo_rifle = new ItemStack(ammoTypes.get("Rifle"));
        ItemMeta rifleMeta = ammo_rifle.getItemMeta();
        rifleMeta.setDisplayName(ChatColor.GOLD + "Rifle Ammo Clip");
        rifleMeta.setLore(pistolAmmoLore);
        ammo_rifle.setItemMeta(rifleMeta);
        //
        ItemStack ammo_smg = new ItemStack(ammoTypes.get("SMG"));
        ItemMeta smgMeta = ammo_smg.getItemMeta();
        smgMeta.setDisplayName(ChatColor.GOLD + "SMG Ammo Clip");
        smgMeta.setLore(pistolAmmoLore);
        ammo_smg.setItemMeta(smgMeta);
        //
        ItemStack ammo_shotgun = new ItemStack(ammoTypes.get("Shotgun"), 1, (short) 5);
        ItemMeta shotgunMeta = ammo_shotgun.getItemMeta();
        shotgunMeta.setDisplayName(ChatColor.GOLD + "Shotgun Ammo Clip");
        shotgunMeta.setLore(pistolAmmoLore);
        ammo_shotgun.setItemMeta(shotgunMeta);
        //
        ItemStack ammo_bow = new ItemStack(ammoTypes.get("Bow"), 1, (short) 6);
        ItemMeta bowMeta = ammo_bow.getItemMeta();
        bowMeta.setDisplayName(ChatColor.GOLD + "Crossbow Bolt Clip");
        bowMeta.setLore(pistolAmmoLore);
        ammo_bow.setItemMeta(bowMeta);
        //
        ItemStack ammo_artillery = new ItemStack(ammoTypes.get("Artillery"));
        ItemMeta artilleryMeta = ammo_artillery.getItemMeta();
        artilleryMeta.setDisplayName(ChatColor.GOLD + "Artillery Ammo Clip");
        artilleryMeta.setLore(pistolAmmoLore);
        ammo_artillery.setItemMeta(artilleryMeta);
        //
        
        miscItemStacks.put("Pistol", ammo_pistol);
        miscItemStacks.put("Rifle", ammo_rifle);
        miscItemStacks.put("SMG", ammo_smg);
        miscItemStacks.put("Shotgun", ammo_shotgun);
        miscItemStacks.put("Bow", ammo_bow);
        miscItemStacks.put("Artillery", ammo_artillery);
        //
        //
        
        ItemStack medKit = new ItemStack(Material.PAPER);
        ItemMeta medKitMeta = medKit.getItemMeta();
        medKitMeta.setDisplayName(ChatColor.GOLD + "Medical Kit");
        ArrayList<String> medLore = new ArrayList<>();
        String type = ChatColor.GRAY + "Misc";
        String name = "Medical Kit";
        String lore1 = ChatColor.LIGHT_PURPLE + "Right click to heal yourself or a target.";
        String lore2 = ChatColor.RED + "You must stay very close to heal others.";
        String lore3 = ChatColor.GRAY + "Max Kits: " + ChatColor.GREEN + maxMedKits;
        medLore.add(type);
        medLore.add(name);
        medLore.add(lore1);
        medLore.add(lore2);
        medLore.add(lore3);
        medKitMeta.setLore(medLore);
        medKit.setItemMeta(medKitMeta);
        
        miscItemStacks.put("Medical Kit", medKit);
        
        //
        ItemStack welder = new ItemStack(Material.BLAZE_ROD);
        ItemMeta welderMeta = welder.getItemMeta();
        welderMeta.setDisplayName(ChatColor.RED + "Welder");
        ArrayList<String> welderLore = new ArrayList<>();
        String welderType = ChatColor.GRAY + "Misc";
        String welderName = "Welder";
        String welderLore1 = ChatColor.LIGHT_PURPLE + "Used on doors to weld them closed!";
        String welderLore2 = ChatColor.LIGHT_PURPLE + "While sneaking, it will unweld doors.";
        String welderLore3 = ChatColor.RED + "You must stay close to the doors to weld.";
        welderLore.add(welderType);
        welderLore.add(welderName);
        welderLore.add(welderLore1);
        welderLore.add(welderLore2);
        welderLore.add(welderLore3);
        welderMeta.setLore(welderLore);
        welder.setItemMeta(welderMeta);
        
        miscItemStacks.put("Welder", welder);
        //
        
        /*
         * Kevlar
         */
        Color dye = Color.fromRGB(25,40,65);
        //
        ItemStack kevlarVest = new ItemStack(Material.LEATHER_CHESTPLATE);
        ItemMeta vestMeta2 = kevlarVest.getItemMeta();
        LeatherArmorMeta vestMeta = (LeatherArmorMeta) vestMeta2;
        vestMeta.setColor(dye);
        vestMeta.setDisplayName(ChatColor.GOLD + "Kevlar Vest");
        ArrayList<String> vestLore = new ArrayList<>();
        String vestType = ChatColor.GRAY + "Misc";
        String vestName = "Kevlar Vest";
        String vestLore1 = ChatColor.DARK_PURPLE + "Offers typical protection against bites,";
        String vestLore2 = ChatColor.DARK_PURPLE + "scratches and bullets.";
        vestLore.add(vestType);
        vestLore.add(vestName);
        vestLore.add(vestLore1);
        vestLore.add(vestLore2);
        vestMeta.setLore(vestLore);
        kevlarVest.setItemMeta(vestMeta);
        miscItemStacks.put("Kevlar Vest", kevlarVest);
        
        //
        ItemStack kevlarPants = new ItemStack(Material.LEATHER_LEGGINGS);
        ItemMeta pantsMeta2 = kevlarPants.getItemMeta();
        LeatherArmorMeta pantsMeta = (LeatherArmorMeta) pantsMeta2;
        pantsMeta.setColor(dye);
        pantsMeta.setDisplayName(ChatColor.GOLD + "Kevlar Pants");
        ArrayList<String> pantsLore = new ArrayList<>();
        String pantsType = ChatColor.GRAY + "Misc";
        String pantsName = "Kevlar Pants";
        String pantsLore1 = ChatColor.DARK_PURPLE + "Protects your naughty bits from";
        String pantsLore2 = ChatColor.DARK_PURPLE + "hordes of angry monsters.";
        pantsLore.add(pantsType);
        pantsLore.add(pantsName);
        pantsLore.add(pantsLore1);
        pantsLore.add(pantsLore2);
        pantsMeta.setLore(pantsLore);
        kevlarPants.setItemMeta(pantsMeta);
        
        miscItemStacks.put("Kevlar Pants", kevlarPants);
        
        //
        ItemStack kevlarBoots = new ItemStack(Material.LEATHER_BOOTS);
        ItemMeta bootsMeta2 = kevlarBoots.getItemMeta();
        LeatherArmorMeta bootsMeta = (LeatherArmorMeta) bootsMeta2;
        bootsMeta.setColor(dye);
        bootsMeta.setDisplayName(ChatColor.GOLD + "Kevlar Boots");
        ArrayList<String> bootsLore = new ArrayList<>();
        String bootsType = ChatColor.GRAY + "Misc";
        String bootsName = "Kevlar Boots";
        String bootsLore1 = ChatColor.DARK_PURPLE + "Take care of your feet,";
        String bootsLore2 = ChatColor.DARK_PURPLE + "and they'll take care of you...";
        String bootsLore3 = ChatColor.DARK_PURPLE + "by kicking zombies in the face.";
        bootsLore.add(bootsType);
        bootsLore.add(bootsName);
        bootsLore.add(bootsLore1);
        bootsLore.add(bootsLore2);
        bootsLore.add(bootsLore3);
        bootsMeta.setLore(bootsLore);
        kevlarBoots.setItemMeta(bootsMeta);
        
        miscItemStacks.put("Kevlar Boots", kevlarBoots);
        
        
    }
    
    
    // Desert Eagle
    /*
    public static final String desertEagle_gunName = "Desert Eagle";
    public static final String desertEagle_gunType = "Pistol";
    public static final double desertEagle_dmg = 15;
    public static final long desertEagle_shootDelay = 700;
    public static final int desertEagle_ammo = 7;
    public static final int desertEagle_reload = 2;
    public static final String desertEagle_name = ChatColor.LIGHT_PURPLE + desertEagle_gunName + " " + ChatColor.GRAY + "(" + desertEagle_ammo + "/" + desertEagle_ammo + ")";
    public static final String desertEagle_name_empty = ChatColor.LIGHT_PURPLE + desertEagle_gunName + " " + ChatColor.GRAY + "(" + "0" + "/" + desertEagle_ammo + ")";
    
    // Double Barrel Shotgun
    public static final String doubleBarrel_gunName = "Double Barreled";
    public static final String doubleBarrel_gunType = "Shotgun";
    public static final double doubleBarrel_dmg = 22;
    public static final long doubleBarrel_shootDelay = 300;
    public static final int doubleBarrel_ammo = 2;
    public static final int doubleBarrel_reload = 4;
    public static final String doubleBarrel_name = ChatColor.LIGHT_PURPLE + doubleBarrel_gunName + " " + ChatColor.GRAY + "(" + doubleBarrel_ammo + "/" + doubleBarrel_ammo + ")";
    public static final String doubleBarrel_name_empty = ChatColor.LIGHT_PURPLE + doubleBarrel_gunName + " " + ChatColor.GRAY + "(" + "0" + "/" + doubleBarrel_ammo + ")";
    
    // M4A1
    public static final String m4_gunName = "M4A1";
    public static final String m4_gunType = "Rifle";
    public static final double m4_dmg = 10;
    public static final long m4_shootDelay = 0;
    public static final int m4_ammo = 30;
    public static final int m4_reload = 3;
    public static final String m4_name = ChatColor.LIGHT_PURPLE + m4_gunName + " " + ChatColor.GRAY + "(" + m4_ammo + "/" + m4_ammo + ")";
    public static final String m4_empty = ChatColor.LIGHT_PURPLE + m4_gunName + " " + ChatColor.GRAY + "(" + "0" + "/" + m4_ammo + ")";
    */
    
    
    
    
    /*
     * Start Gun List
     */
            /*
    static
    {
        
        // Desert Eagle
        ItemStack pistol_desertEagle = new ItemStack(Material.IRON_AXE);
        ItemMeta desertEagle_meta = pistol_desertEagle.getItemMeta();
        ArrayList<String> deagle_lore = new ArrayList<String>();
        if (desertEagle_meta.hasLore())
        {
            deagle_lore = (ArrayList<String>) desertEagle_meta.getLore();
        }
        desertEagle_meta.setDisplayName(desertEagle_name);
        String deagle_Type = ChatColor.GRAY + desertEagle_gunType;
        String deagle_dmg = ChatColor.GRAY + "Damage: " + ChatColor.GOLD + "|||||||" + ChatColor.DARK_GRAY + "|||";
        String deagle_reload = ChatColor.GRAY + "Reload: " + ChatColor.GOLD + "|||||||" + ChatColor.DARK_GRAY + "|||";
        String deagle_clip = ChatColor.GRAY + "Clip: " + ChatColor.GOLD + "|||||" + ChatColor.DARK_GRAY + "|||||";
        String deagle_rateoffire = ChatColor.GRAY + "RoF: " + ChatColor.GOLD + "|||||||" + ChatColor.DARK_GRAY + "|||";
        deagle_lore.add(deagle_Type);
        deagle_lore.add(deagle_dmg);
        deagle_lore.add(deagle_clip);
        deagle_lore.add(deagle_reload);
        deagle_lore.add(deagle_rateoffire);
        desertEagle_meta.setLore(deagle_lore);
        pistol_desertEagle.setItemMeta(desertEagle_meta);
        
        
        // Double Barreled
        ItemStack shotgun_DoubleBarreled = new ItemStack(Material.IRON_PICKAXE);
        ItemMeta doubleBarreled_meta = shotgun_DoubleBarreled.getItemMeta();
        ArrayList<String> doubleBarreled_lore = new ArrayList<String>();
        if (doubleBarreled_meta.hasLore())
        {
            doubleBarreled_lore = (ArrayList<String>) doubleBarreled_meta.getLore();
        }
        doubleBarreled_meta.setDisplayName(doubleBarrel_name);
        String db_Type = ChatColor.GRAY + doubleBarrel_gunType;
        String db_Dmg = ChatColor.GRAY + "Damage: " + ChatColor.GOLD + "|||||||||" + ChatColor.DARK_GRAY + "|";
        String db_Reload = ChatColor.GRAY + "Reload: " + ChatColor.GOLD + "||||" + ChatColor.DARK_GRAY + "||||||";
        String db_Clip = ChatColor.GRAY + "Clip: " + ChatColor.GOLD + "|" + ChatColor.DARK_GRAY + "|||||||||";
        String db_RateOfFire = ChatColor.GRAY + "RoF: " + ChatColor.GOLD + "|||||||||" + ChatColor.DARK_GRAY + "|";
        deagle_lore.add(db_Type);
        deagle_lore.add(db_Dmg);
        deagle_lore.add(db_Reload);
        deagle_lore.add(db_Clip);
        deagle_lore.add(db_RateOfFire);
        doubleBarreled_meta.setLore(doubleBarreled_lore);
        shotgun_DoubleBarreled.setItemMeta(doubleBarreled_meta);
        
        
        // M4A1
        ItemStack rifle_M4A1 = new ItemStack(Material.IRON_HOE);
        ItemMeta M4A1_meta = rifle_M4A1.getItemMeta();
        ArrayList<String> M4A1_lore = new ArrayList<String>();
        if (M4A1_meta.hasLore())
        {
            M4A1_lore = (ArrayList<String>) M4A1_meta.getLore();
        }
        M4A1_meta.setDisplayName(m4_name);
        String m4_Type = ChatColor.GRAY + m4_gunType;
        String m4_Dmg = ChatColor.GRAY + "Damage: " + ChatColor.GOLD + "|||||" + ChatColor.DARK_GRAY + "|||||";
        String m4_Reload = ChatColor.GRAY + "Reload: " + ChatColor.GOLD + "|||||||" + ChatColor.DARK_GRAY + "|||";
        String m4_Clip = ChatColor.GRAY + "Clip: " + ChatColor.GOLD + "|||||||||" + ChatColor.DARK_GRAY + "|";
        String m4_RateOfFire = ChatColor.GRAY + "RoF: " + ChatColor.GOLD + "||||||||||" + ChatColor.DARK_GRAY + "";
        deagle_lore.add(m4_Type);
        deagle_lore.add(m4_Dmg);
        deagle_lore.add(m4_Reload);
        deagle_lore.add(m4_Clip);
        deagle_lore.add(m4_RateOfFire);
        M4A1_meta.setLore(M4A1_lore);
        rifle_M4A1.setItemMeta(M4A1_meta);
       

        
        GunList.add(pistol_desertEagle);
        GunList.add(shotgun_DoubleBarreled);
        GunList.add(rifle_M4A1);
        
    }
    */
    
    public static void craftDemo(String demoName, String demoType, double demoDamage, int demoPrimary, int demoAmmo, int demoCost, Material demoMaterial, int demoReload)
    {
        if (DemoCheck.contains(demoName))
        {
            DemoCheck.remove(demoName);
        }
        DemoCheck.add(demoName);
        
        if (DemoCost.containsKey(demoName))
        {
            DemoCost.remove(demoName);
        }
        DemoCost.put(demoName, demoCost);
        if (DemoDamage.containsKey(demoName))
        {
            DemoDamage.remove(demoName);
        }
        DemoDamage.put(demoName, demoDamage);
        if (DemoType.containsKey(demoName))
        {
            DemoType.remove(demoName);
        }
        DemoType.put(demoName, demoType);
        
        if (DemoAmmo.containsKey(demoName))
        {
            DemoAmmo.remove(demoName);
        }
            if (!(demoName.equalsIgnoreCase("Grenade")))
            {
                DemoAmmo.put(demoName, demoAmmo);
            }
        
        if (DemoReload.containsKey(demoName))
        {
            DemoReload.remove(demoName);
        }
            if (!(demoName.equalsIgnoreCase("Grenade")))
            {
                DemoReload.put(demoName, demoReload);
            }
        
        
        /*
         * create item stack
         */
        Material type = demoMaterial;
        
        
        String isPrimary = ChatColor.GOLD + "Primary Weapon";
        int prime = demoPrimary;
       
        if (prime == 0)
        {
            isPrimary = ChatColor.GOLD + "Secondary Weapon";
        }
        
        String demoStack_name = "null";
        
        if (demoName.equalsIgnoreCase("Grenade"))
        {
            demoStack_name = ChatColor.LIGHT_PURPLE + demoName;
        }
        else
        {
            demoStack_name = ChatColor.LIGHT_PURPLE + demoName + " " + ChatColor.GRAY + "(" + demoAmmo + "/" + demoAmmo + ")";
        }
        
        ItemStack demoStack = new ItemStack(type);

        
        ItemMeta demoStack_meta = demoStack.getItemMeta();
        ArrayList<String> demoStack_lore = new ArrayList<>();
        if (demoStack_meta.hasLore())
        {
            demoStack_lore = (ArrayList<String>) demoStack_meta.getLore();
        }

        demoStack_meta.setDisplayName(demoStack_name);
        String demo_Type = ChatColor.GRAY + demoType;
        String demo_Name = demoName;
        String demo_Dmg = ChatColor.GRAY + "Damage: " + doGunDamage(demoDamage) + ChatColor.GRAY + " (" + ChatColor.GREEN + demoDamage + ChatColor.GRAY + ")";
        String demo_MaxClip = ChatColor.GRAY + "Max Clips: ";
        String demo_Primary = isPrimary;
        if (demoName.equalsIgnoreCase("Grenade"))
        {
            demo_MaxClip = ChatColor.GRAY + "Max Grenades: " + ChatColor.GREEN + maxGrenades;
        }
        else
        {
            demo_MaxClip = ChatColor.GRAY + "Max Clips: " + ChatColor.GREEN + ammoCounts.get(demoType);
        }

        demoStack_lore.add(demo_Type);
        demoStack_lore.add(demo_Name);
        demoStack_lore.add(demo_Dmg);
        demoStack_lore.add(demo_MaxClip);
        if (!(demoName.equalsIgnoreCase("Grenade")))
        {
            demoStack_lore.add(demo_Primary);
        }
        demoStack_meta.setLore(demoStack_lore);
        demoStack.setItemMeta(demoStack_meta);

        DemoList.put(demoName, demoStack);
        SRKF.LOG.info("[SRKF] - Loaded " + demoName + " into memory.");
    }
    
    
    public static void craftMelee(String meleeName, String meleeType, double meleeDamage, int meleeDelay, double meleeRange, int meleePrimary, int meleeCost, Material meleeMaterial)
    {
        if (MeleeCheck.contains(meleeName))
        {
            MeleeCheck.remove(meleeName);
        }
        MeleeCheck.add(meleeName);
        
        if (MeleeDamage.containsKey(meleeName))
        {
            MeleeDamage.remove(meleeName);
        }
        MeleeDamage.put(meleeName, meleeDamage);
        
        if (MeleeRange.containsKey(meleeName))
        {
            MeleeRange.remove(meleeName);
        }
        MeleeRange.put(meleeName, meleeRange);
        
        if (MeleeCost.containsKey(meleeName))
        {
            MeleeCost.remove(meleeName);
        }
        MeleeCost.put(meleeName, meleeCost);
        
        if (MeleeDelay.containsKey(meleeName))
        {
            MeleeDelay.remove(meleeName);
        }
        MeleeDelay.put(meleeName, meleeDelay);
        
        Material type = meleeMaterial;
        
        String isPrimary = ChatColor.GOLD + "Primary Weapon";
        int prime = meleePrimary;
        //SRKF.LOG.info("prime is: " + prime);
        
        if (prime == 0)
        {
            isPrimary = ChatColor.GOLD + "Secondary Weapon";
        }
        
        String meleeStack_name = ChatColor.LIGHT_PURPLE + meleeName;
        ItemStack meleeStack = new ItemStack(type);
        ItemMeta meleeStack_meta = meleeStack.getItemMeta();
        ArrayList<String> meleeStack_lore = new ArrayList<>();
        if (meleeStack_meta.hasLore())
        {
            meleeStack_lore = (ArrayList<String>) meleeStack_meta.getLore();
        }

        meleeStack_meta.setDisplayName(meleeStack_name);
        String melee_Type = ChatColor.GRAY + meleeType;
        String melee_Name = meleeName;
        String melee_Dmg = ChatColor.GRAY + "Damage: " + doGunDamage(meleeDamage) + ChatColor.GRAY + " (" + ChatColor.GREEN + meleeDamage + ChatColor.GRAY + ")";
        String melee_Delay = ChatColor.GRAY + "Delay: " + doMeleeDelay(meleeDelay) + ChatColor.GRAY + " (" + ChatColor.GREEN + meleeDelay + ChatColor.GRAY + ")";
        
        String melee_Primary = isPrimary;

        meleeStack_lore.add(melee_Type);
        meleeStack_lore.add(melee_Name);
        meleeStack_lore.add(melee_Dmg);
        meleeStack_lore.add(melee_Delay);
        meleeStack_lore.add(melee_Primary);
        meleeStack_meta.setLore(meleeStack_lore);
        meleeStack.setItemMeta(meleeStack_meta);

        MeleeList.put(meleeName, meleeStack);
        SRKF.LOG.info("[SRKF] - Loaded " + meleeName + " into memory.");
        
    }
    
    public static void craftGun(String gunName, String gunType, double gunDamage, long gunDelay, int gunAmmo, int gunReload, int gunPrimary, int gunCost, Material gunMaterial)
    {
        /*
         * submit all data to hashes
         */
        if (GunCheck.contains(gunName))
        {
            GunCheck.remove(gunName);
        }
        GunCheck.add(gunName);
        
        if (GunDamage.containsKey(gunName))
        {
            GunDamage.remove(gunName);
        }
        GunDamage.put(gunName, gunDamage);
        
        if (GunDelay.containsKey(gunName))
        {
            GunDelay.remove(gunName);
        }
        GunDelay.put(gunName, gunDelay);
        
        if (GunAmmo.containsKey(gunName))
        {
            GunAmmo.remove(gunName);
        }
        GunAmmo.put(gunName, gunAmmo);
        
        if (GunReload.containsKey(gunName))
        {
            GunReload.remove(gunName);
        }
        GunReload.put(gunName, gunReload);
        
        if (GunType.containsKey(gunName))
        {
            GunType.remove(gunName);
        }
        GunType.put(gunName, gunType);
        
        if (GunCost.containsKey(gunName))
        {
            GunCost.remove(gunName);
        }
        GunCost.put(gunName, gunCost);
        
        /*
         * create item stack
         */
        Material type = gunMaterial;
        
        
        String isPrimary = ChatColor.GOLD + "Primary Weapon";
        int prime = gunPrimary;
       
        if (prime == 0)
        {
            isPrimary = ChatColor.GOLD + "Secondary Weapon";
        }
        
        
        String gunStack_name = ChatColor.LIGHT_PURPLE + gunName + " " + ChatColor.GRAY + "(" + gunAmmo + "/" + gunAmmo + ")";
        ItemStack gunStack = new ItemStack(type);
        ItemMeta gunStack_meta = gunStack.getItemMeta();
        ArrayList<String> gunStack_lore = new ArrayList<>();
        if (gunStack_meta.hasLore())
        {
            gunStack_lore = (ArrayList<String>) gunStack_meta.getLore();
        }

        gunStack_meta.setDisplayName(gunStack_name);
        String gun_Type = ChatColor.GRAY + gunType;
        String gun_Name = gunName;
        String gun_Dmg = ChatColor.GRAY + "Damage: " + doGunDamage(gunDamage) + ChatColor.GRAY + " (" + ChatColor.GREEN + gunDamage + ChatColor.GRAY + ")";
        String gun_Reload = ChatColor.GRAY + "Reload: " + doGunReload(gunReload) + ChatColor.GRAY + " (" + ChatColor.GREEN + gunReload + ChatColor.GRAY + ")";
        String gun_Clip = ChatColor.GRAY + "Clip: " + doGunAmmo(gunAmmo) + ChatColor.GRAY + " (" + ChatColor.GREEN + gunAmmo + ChatColor.GRAY + ")";
        String gun_MaxClip = ChatColor.GRAY + "Max Clips: " + ChatColor.GREEN + ammoCounts.get(gunType);
        String gun_RateOfFire = ChatColor.GRAY + "RoF: " + doGunRoF(gunDelay) + ChatColor.GRAY + " (" + ChatColor.GREEN + gunDelay + ChatColor.GRAY + ")";
        String gun_Primary = isPrimary;

        gunStack_lore.add(gun_Type);
        gunStack_lore.add(gun_Name);
        gunStack_lore.add(gun_Dmg);
        gunStack_lore.add(gun_Reload);
        gunStack_lore.add(gun_Clip);
        gunStack_lore.add(gun_MaxClip);
        gunStack_lore.add(gun_RateOfFire);
        gunStack_lore.add(gun_Primary);
        gunStack_meta.setLore(gunStack_lore);
        gunStack.setItemMeta(gunStack_meta);

        GunList.put(gunName, gunStack);
        SRKF.LOG.info("[SRKF] - Loaded " + gunName + " into memory.");
    }
    
   
    public static String doGunDamage(double dmg)
    {
        String damageString = ChatColor.GOLD + "";
        
        double rounded = Math.ceil(((dmg * 100) / 1000) * 2);
        int dmgInt = (int) rounded;
        
        for (int i = 0; i <= dmgInt - 1; i++)
        {
            if (i < 15)
            {
                damageString = damageString + "*";
            }
        }
        
        int addGray = 10 - dmgInt;
        String totalString = ChatColor.GRAY + "";
        for (int i = 0; i <= addGray - 1; i++)
        {
            totalString = totalString + "*";
        }
        
        String finalString = (damageString + totalString);
        
        return finalString;
    }
    
    public static String doGunReload(int reload)
    {
        String reloadString = ChatColor.GOLD + "";
        
        int amount = (10 - reload);
        
        if (amount == 0)
        {
            amount = 1;
        }
        
        for (int i = 0; i <= amount - 1; i++)
        {
            reloadString = reloadString + "*";
        }
        
        int addGray = 10 - amount;
        
        String totalString = ChatColor.GRAY + "";
        for (int i = 0; i <= addGray - 1; i++)
        {
            totalString = totalString + "*";
        }
        
        String finalString = (reloadString + totalString);
        
        return finalString;
    }
    
    public static String doGunAmmo(int ammo)
    {
        String ammoString = ChatColor.GOLD + "";
        
        double rounded = Math.floor(((double)ammo / 50) * 10);
        int roundedInt = (int) rounded;
        
        for (int i = 0; i <= roundedInt - 1; i++)
        {
            ammoString = ammoString + "*";
        }
        int addGray = 10 - roundedInt;
        
        String totalString = ChatColor.GRAY + "";
        for (int i = 0; i <= addGray - 1; i++)
        {
            totalString = totalString + "*";
        }
        
        String finalString = (ammoString + totalString);
        
        return finalString;
    }
    
    public static String doMeleeDelay(int delay)
    {
        String rateString = ChatColor.GOLD + "";
        
        int swap = 10 - (delay * 2);
        
        if (swap == 0)
        {
            swap = 1;
        }
        
        for (int i = 0; i <= swap - 1; i++)
        {
            rateString = rateString + "*";
        }
        
        int addGray = 10 - swap;
        
        String totalString = ChatColor.GRAY + "";
        for (int i = 0; i <= addGray - 1; i++)
        {
            totalString = totalString + "*";
        }
        
        String finalString = (rateString + totalString);
        
        return finalString;
    }
    
    public static String doGunRoF(long rate)
    {
        String rateString = ChatColor.GOLD + "";
        
        double dbSwap = Math.round(((((double) rate / 2000) * 100) / 10));
        int roundedInt = (int) dbSwap;
        
        int swap = 10 - roundedInt;
        
        if (swap == 0)
        {
            swap = 1;
        }
        
        for (int i = 0; i <= swap - 1; i++)
        {
            rateString = rateString + "*";
        }
        
        int addGray = 10 - swap;
        
        String totalString = ChatColor.GRAY + "";
        for (int i = 0; i <= addGray - 1; i++)
        {
            totalString = totalString + "*";
        }
        
        String finalString = (rateString + totalString);
        
        return finalString;
    }
    
    
    public static String getDefaultPrimary (String roleName)
    {
        String primary = "WWII Grease Gun";
        
        if (roleName.equalsIgnoreCase("Commando"))
        {
            primary = "Bullpup";
        }
        
        if (roleName.equalsIgnoreCase("Medic"))
        {
            primary = "Double Barreled";
        }
        
        if (roleName.equalsIgnoreCase("Engineer"))
        {
            primary = "WWII Grease Gun";
        }
        
        if (roleName.equalsIgnoreCase("Sharpshooter"))
        {
            primary = "Lee Enfield";
        }
        
        if (roleName.equalsIgnoreCase("Berserker"))
        {
            primary = "Machete";
        }
        
        return primary;
    }
    
    public static String getDefaultSecondary (String roleName)
    {
        String secondary = "9mm";
        
        if (roleName.equalsIgnoreCase("Commando"))
        {
            secondary = "Combat Knife";
        }
        
        if (roleName.equalsIgnoreCase("Medic"))
        {
            secondary = "9mm";
        }
        
        if (roleName.equalsIgnoreCase("Engineer"))
        {
            secondary = "9mm";
        }
        
        if (roleName.equalsIgnoreCase("Sharpshooter"))
        {
            secondary = "9mm";
        }
        
        if (roleName.equalsIgnoreCase("Berserker"))
        {
            secondary = "9mm";
        }
        
        return secondary;
    }
    
    
   
}
