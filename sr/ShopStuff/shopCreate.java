package sr.ShopStuff;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import sr.Extras.Guns;
import sr.Game;
import sr.SRKF;

/**
 *
 * @author Cross
 */
public class shopCreate 
{
    public SRKF plugin;
 
 
    public shopCreate(SRKF plugin)
    {
      this.plugin = plugin;

    }
   
    public shopCreate(SRKF plugin, int gameid) 
    {
        this.plugin = plugin;
        int i = 1;
        boolean done = false;
        String mapName = SRKF.dbManager.getMapName(gameid);
        String shopSpawn = "Shop_" + mapName + "_" + gameid;
        String passThrough = "";
        while(done == false)
        {
            if(!(Game.GameidShopnumberShoplocations.containsKey(shopSpawn + "_" + i)))
            {
                i = i - 1;
                done = true;
                
            }
            else
            {
               i = i + 1;
            }
        }
        
        int Min = 1;
        int Max = i;
        //Bukkit.broadcastMessage("max " + Max);

        double random = Min + (int)(Math.random() * ((Max - Min) + 1));

        int shopid = (int) random;
        //Bukkit.broadcastMessage("shopid " + shopid);

        Location[] locs = Game.GameidShopnumberShoplocations.get(shopSpawn + "_" + shopid);
        passThrough = shopSpawn + "_" + shopid;

        Location loc0 = locs[0];
        Location loc1 = locs[1];
        Location loc2 = locs[2];
        Location loc3 = locs[3];

        //Set door to air

        World w = loc1.getWorld();
        Chunk c = loc1.getChunk();
        if (!(c.isLoaded()))
        {
            c.load();
        }
        int minx = Math.min(loc1.getBlockX(), loc2.getBlockX()),
        miny = Math.min(loc1.getBlockY(), loc2.getBlockY()),
        minz = Math.min(loc1.getBlockZ(), loc2.getBlockZ()),
        maxx = Math.max(loc1.getBlockX(), loc2.getBlockX()),
        maxy = Math.max(loc1.getBlockY(), loc2.getBlockY()),
        maxz = Math.max(loc1.getBlockZ(), loc2.getBlockZ());
        for (int x = minx; x<=maxx;x++)
        {
            for (int y = miny; y<=maxy;y++)
            {
                for (int z = minz; z<=maxz;z++)
                {
                    Block b = w.getBlockAt(x, y, z);
                    b.setType(Material.AIR);
                }
            }
        }

        //Populate random items for shop

        //The shop items for the shop inventory
        int size1 = Guns.GunList.size();
        int size2 = Guns.MeleeList.size();
        int total = ((size1 + size2) + 2) - 1;
        
        ArrayList<ItemStack> mainList = new ArrayList();
        ArrayList<ItemStack> rifleList = new ArrayList();
        ArrayList<ItemStack> pistolList = new ArrayList();
        ArrayList<ItemStack> shotgunList = new ArrayList();
        ArrayList<ItemStack> smgList = new ArrayList();
        ArrayList<ItemStack> meleeList = new ArrayList();
        ArrayList<ItemStack> demoList = new ArrayList();
        
        /*
         * Create menu options
         */
        
        ItemStack rifleStack = new ItemStack(Material.IRON_HOE);
        ItemMeta rifleMeta = rifleStack.getItemMeta();
        rifleMeta.setDisplayName("Rifles");
        ArrayList<String> rifleLore = new ArrayList<>();
        String rifleLore1 = ChatColor.LIGHT_PURPLE + "Click to see all Rifle Weapons";
        rifleLore.add(rifleLore1);
        rifleMeta.setLore(rifleLore);
        rifleStack.setItemMeta(rifleMeta);
        
        ItemStack pistolStack = new ItemStack(Material.STONE_PICKAXE);
        ItemMeta pistolMeta = pistolStack.getItemMeta();
        pistolMeta.setDisplayName("Pistols");
        ArrayList<String> pistolLore = new ArrayList<>();
        String pistolLore1 = ChatColor.LIGHT_PURPLE + "Click to see all Pistol Weapons";
        pistolLore.add(pistolLore1);
        pistolMeta.setLore(pistolLore);
        pistolStack.setItemMeta(pistolMeta);
        
        ItemStack smgStack = new ItemStack(Material.STONE_SPADE);
        ItemMeta smgMeta = smgStack.getItemMeta();
        smgMeta.setDisplayName("SMGs");
        ArrayList<String> smgLore = new ArrayList<>();
        String smgLore1 = ChatColor.LIGHT_PURPLE + "Click to see all SMG Weapons";
        smgLore.add(smgLore1);
        smgMeta.setLore(smgLore);
        smgStack.setItemMeta(smgMeta);
        
        ItemStack shotgunStack = new ItemStack(Material.IRON_PICKAXE);
        ItemMeta shotgunMeta = shotgunStack.getItemMeta();
        shotgunMeta.setDisplayName("Shotguns");
        ArrayList<String> shotgunLore = new ArrayList<>();
        String shotgunLore1 = ChatColor.LIGHT_PURPLE + "Click to see all Shotgun Weapons";
        shotgunLore.add(shotgunLore1);
        shotgunMeta.setLore(shotgunLore);
        shotgunStack.setItemMeta(shotgunMeta);
        
        ItemStack meleeStack = new ItemStack(Material.GOLD_SWORD);
        ItemMeta meleeMeta = meleeStack.getItemMeta();
        meleeMeta.setDisplayName("Melee");
        ArrayList<String> meleeLore = new ArrayList<>();
        String meleeLore1 = ChatColor.LIGHT_PURPLE + "Click to see all Melee Weapons";
        meleeLore.add(meleeLore1);
        meleeMeta.setLore(meleeLore);
        meleeStack.setItemMeta(meleeMeta);
        
        ItemStack demoStack = new ItemStack(Material.STONE_HOE);
        ItemMeta demoMeta = demoStack.getItemMeta();
        demoMeta.setDisplayName("Demolitions");
        ArrayList<String> demoLore = new ArrayList<>();
        String demoLore1 = ChatColor.LIGHT_PURPLE + "Click to see all Demolitions";
        demoLore.add(demoLore1);
        demoMeta.setLore(demoLore);
        demoStack.setItemMeta(demoMeta);
        
        mainList.add(rifleStack);
        mainList.add(pistolStack);
        mainList.add(smgStack);
        mainList.add(shotgunStack);
        mainList.add(meleeStack);
        mainList.add(demoStack);
        
        /*
         * 
         */
        ItemStack filler = new ItemStack(Material.STAINED_GLASS_PANE);
        ItemMeta fillerMeta = filler.getItemMeta();
        fillerMeta.setDisplayName(" ");
        filler.setItemMeta(fillerMeta);
            
        // med kit
        ItemStack medKit = Guns.miscItemStacks.get("Medical Kit").clone();
        ItemMeta papermeta = medKit.getItemMeta();
        int cost = Guns.miscItemCosts.get("Medical Kit");
        String medKitDisp = papermeta.getDisplayName();
        papermeta.setDisplayName(medKitDisp + ChatColor.GOLD + " - Costs: " + ChatColor.GREEN + cost);
        medKit.setItemMeta(papermeta);

        // ammo refill
        ItemStack ammo = new ItemStack(Material.ARROW, 1);
        ItemMeta ammometa = ammo.getItemMeta();
        int cost2 = Guns.miscItemCosts.get("Ammo");
        ammometa.setDisplayName(ChatColor.GOLD + "Refill Ammo" + ChatColor.GOLD + " - Costs: " + ChatColor.GREEN + cost2);
        ArrayList<String> lore = new ArrayList<>();
        String line1 = ChatColor.GREEN + "Click to refill your Ammo";
        String line2 = "Ammo";
        String line3 = "Notice - Max ammo is as follows:";
        String line4 = ChatColor.GOLD + "Pistol: 4 Clips";
        String line5 = ChatColor.GOLD + "Rifle: 3 Clips";
        String line6 = ChatColor.GOLD + "SMG: 3 Clips";
        String line7 = ChatColor.GOLD + "Shotgun: 12 'Clips'";
        String line8 = ChatColor.GOLD + "Crossbow: 15 'Clips'";
        lore.add(line1);
        lore.add(line2);
        lore.add(line3);
        lore.add(line4);
        lore.add(line5);
        lore.add(line6);
        lore.add(line7);
        lore.add(line8);
        ammometa.setLore(lore);
        ammo.setItemMeta(ammometa);
        
        // add all guns / melee
        Iterator<String> gunIt = Guns.GunList.keySet().iterator();
        while (gunIt.hasNext())
        {
            String gunName = gunIt.next();
            ItemStack stack = Guns.GunList.get(gunName).clone();
            ItemMeta meta = stack.getItemMeta();
            String disp = meta.getDisplayName();
            int gunCost = Guns.GunCost.get(gunName);
            String newName = disp + ChatColor.GOLD + " - Costs: " + ChatColor.GREEN + gunCost;
            meta.setDisplayName(newName);
            stack.setItemMeta(meta);
            
            List<String> loreList = meta.getLore();
            String type = loreList.get(0);
            
            if (type.contains("Rifle"))
            {
                rifleList.add(stack);
            }
            
            if (type.contains("Pistol"))
            {
                pistolList.add(stack);
            }
            
            if (type.contains("SMG"))
            {
                smgList.add(stack);
            }
            
            if (type.contains("Shotgun"))
            {
                shotgunList.add(stack);
            }
            
            if (type.contains("Bow"))
            {
                rifleList.add(stack);
            }
        }
        
        Iterator<String> meleeIt = Guns.MeleeList.keySet().iterator();
        while (meleeIt.hasNext())
        {
            String meleeName = meleeIt.next();
            ItemStack stack = Guns.MeleeList.get(meleeName).clone();
            ItemMeta meta = stack.getItemMeta();
            String disp = meta.getDisplayName();
            int meleeCost = Guns.MeleeCost.get(meleeName);
            String newName = disp + ChatColor.GOLD + " - Costs: " + ChatColor.GREEN + meleeCost;
            meta.setDisplayName(newName);
            stack.setItemMeta(meta);
            
            meleeList.add(stack);
        }
        
        
        Iterator<String> demoIt = Guns.DemoList.keySet().iterator();
        while (demoIt.hasNext())
        {
            String demoName = demoIt.next();
            ItemStack stack = Guns.DemoList.get(demoName).clone();
            ItemMeta meta = stack.getItemMeta();
            String disp = meta.getDisplayName();
            int demoCost = Guns.DemoCost.get(demoName);
            String newName = disp + ChatColor.GOLD + " - Costs: " + ChatColor.GREEN + demoCost;
            meta.setDisplayName(newName);
            stack.setItemMeta(meta);
            
            demoList.add(stack);
        }
        
        // add spacer
        mainList.add(filler);
        // add  ammo
        mainList.add(ammo);
        // add med kit
        mainList.add(medKit);
        
        // add to shop
        if(Game.GameidShopitemnames.containsKey(gameid))
        {
            Game.GameidShopitemnames.remove(gameid);
        }
        Game.GameidShopitemnames.put(gameid, mainList);
        
        if (Game.meleeShopItemNames.containsKey(gameid))
        {
            Game.meleeShopItemNames.remove(gameid);
        }
        Game.meleeShopItemNames.put(gameid, meleeList);
        
        if (Game.pistolShopItemNames.containsKey(gameid))
        {
            Game.pistolShopItemNames.remove(gameid);
        }
        Game.pistolShopItemNames.put(gameid, pistolList);
        
        if (Game.rifleShopItemNames.containsKey(gameid))
        {
            Game.rifleShopItemNames.remove(gameid);
        }
        Game.rifleShopItemNames.put(gameid, rifleList);
        
        if (Game.smgShopItemNames.containsKey(gameid))
        {
            Game.smgShopItemNames.remove(gameid);
        }
        Game.smgShopItemNames.put(gameid, smgList);
        
        if (Game.shotgunShopItemNames.containsKey(gameid))
        {
            Game.shotgunShopItemNames.remove(gameid);
        }
        Game.shotgunShopItemNames.put(gameid, shotgunList);
        
        if (Game.demoShopItemNames.containsKey(gameid))
        {
            Game.demoShopItemNames.remove(gameid);
        }
        Game.demoShopItemNames.put(gameid, demoList);
        
        
        /*
        //Get 1 random melee weapon
        HashSet<String> hashcopy = new HashSet<String>(Guns.MeleeCheck);
          Iterator<String> iterator = hashcopy.iterator();
          int ii = 0;
          while (iterator.hasNext())
          {
              ii = ii + 1;

              String tempstring = iterator.next();
          }

        

        int Min2 = 1;
        int Max2 = ii;

        int itemsi = 1;
        while(itemsi <= 1)
        {
            double random2 = Min2 + (int)(Math.random() * ((Max2 - Min2) + 1));

            HashSet<String> gunhashcopy = new HashSet<String>(Guns.MeleeCheck);
            Iterator<String> iterator2 = gunhashcopy.iterator();
            int iii = 1;
            while (iterator2.hasNext())
            {
                String itemname = iterator2.next();
                if(random2 == iii)
                {
                   //Bukkit.broadcastMessage("shop item number " + (itemsi - 1) + " meleeweapon: " + itemname );

                    ItemStack is = Guns.MeleeList.get(itemname).clone();
                    ItemMeta im = is.getItemMeta();

                    int cost = Guns.MeleeCost.get(itemname);
                    String displayname = im.getDisplayName();
                    im.setDisplayName(displayname + ChatColor.GOLD + " - Costs: " + ChatColor.GREEN + cost);
                    is.setItemMeta(im);
                    shopitems[itemsi - 1] = is;

                }  


                iii = iii + 1;
            }

            itemsi = itemsi + 1;
        }

        //Get 2 random guns
        HashSet<String> hashcopy2 = new HashSet<String>(Guns.GunCheck);
        Iterator<String> iteratorgun = hashcopy2.iterator();
        int ii2 = 0;
        while (iteratorgun.hasNext())
        {
            ii2 = ii2 + 1;
            String tempstring2 = iteratorgun.next();
        }



        int Min3 = 1;
        int Max3 = ii2;

        //Gun 1
        double random2 = Min3 + (int)(Math.random() * ((Max3 - Min3) + 1));
        String nametoremove = "";
        HashSet<String> hashcopygun = new HashSet<String>(Guns.GunCheck);
        Iterator<String> iterator3 = hashcopygun.iterator();
        int iii = 1;
        while (iterator3.hasNext())
        {
            String itemname = iterator3.next();
            if(random2 == iii)
            {
                //Bukkit.broadcastMessage("shop item number " + (itemsi - 1) + " gunname: " + itemname );

                ItemStack is = Guns.GunList.get(itemname).clone();
                ItemMeta im = is.getItemMeta();

                int cost = Guns.GunCost.get(itemname);
                String displayname = im.getDisplayName();

                im.setDisplayName(displayname + ChatColor.GOLD + " - Costs: " + ChatColor.GREEN + cost);
                is.setItemMeta(im);
                shopitems[1] = is;

                nametoremove = itemname;
            }

            iii = iii + 1;
        }


        //Gun 2

        Max3 = Max3 - 1;
        double random3 = Min3 + (int)(Math.random() * ((Max3 - Min3) + 1));

        HashSet<String> hashcopygun2 = new HashSet<String>(Guns.GunCheck);
        hashcopygun2.remove(nametoremove);
        Iterator<String> iterator4 = hashcopygun2.iterator();
        iii = 1;
        while (iterator4.hasNext())
        {
            String itemname = iterator4.next();
            if(random3 == iii)
            {
                //Bukkit.broadcastMessage("shop item number " + (itemsi - 1) + " gunname2: " + itemname );

                ItemStack is = Guns.GunList.get(itemname).clone();
                ItemMeta im = is.getItemMeta();

                int cost = Guns.GunCost.get(itemname);
                String displayname = im.getDisplayName();
                im.setDisplayName(displayname + ChatColor.GOLD + " - Costs: " + ChatColor.GREEN + cost);
                is.setItemMeta(im);
                shopitems[2] = is;

            }

            iii = iii + 1;
        }
        */
        
        

        //Spawn Villager
        Villager newEnt = (Villager) loc3.getWorld().spawnEntity(loc3, EntityType.VILLAGER);
        LivingEntity le = (LivingEntity) newEnt;
        //Villager v = (Villager)loc3.getWorld().spawn(loc3, Villager.class);
        
        UUID uid = newEnt.getUniqueId();
        
        if (Game.mobID.containsKey(uid))
        {
            Game.mobID.remove(uid);
        }
        
        Game.mobID.put(uid, gameid);
        newEnt.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,12000, 10));
        newEnt.setCustomName(ChatColor.AQUA + "Black Market Dealer");

        new shopDespawn(plugin, loc0, loc1, loc2, newEnt, gameid, passThrough);
        
        /*
         * Set Cash to Level
         */
        
        
        for (String name : Game.playersMaps.keySet())
        {
            int id = Game.playersMaps.get(name);
            if (id == gameid)
            {
                int cash = 0;
                Player inGame = Bukkit.getPlayer(name);
                if (Game.playerMoney.containsKey(inGame.getName()))
                {
                    cash = Game.playerMoney.get(inGame.getName());
                }
                inGame.setLevel(cash);
                inGame.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.GOLD + "Round changed. Your cash is: " + ChatColor.GREEN + "$" + cash);
            } 
        }
    }
}
