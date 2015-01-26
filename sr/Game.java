package sr;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.DyeColor;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Openable;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import sr.CustomEntities.CustomEntityIronGolem;
import sr.Extras.gameMobChecker;
import sr.Listeners.PL;
import sr.Roles.Berserker;
import sr.Tasks.gameMessageTask;

/**
 *
 * @author Cross
 */
public class Game
{
    public SRKF plugin;
    int ID;
    
    public Game(SRKF plugin, int ID)
    {
        this.ID = ID;
        this.plugin = plugin;
        startNewGame(this.ID);
    }
    
    public static int downedTime = 25;
    
    public static HashSet<UUID> hasWound = new HashSet<>();
    
    public static HashMap<Integer, Integer> gameMobCheckerHash = new HashMap<>();
    
    public static HashMap<UUID, Double> mobHealth = new HashMap<>();
    
    public static HashSet<String> autoReload = new HashSet<>();
    
    public static HashMap<Integer, Integer> mapSkipAmount = new HashMap<>();
    public static HashMap<String, Integer> gameRoundSkip = new HashMap<>();
    public static HashSet<Integer> mapIDBeingSkipped = new HashSet<>();
    
    public static HashMap<Integer, String> gameIDWorldName = new HashMap<>();
    
    public static HashMap<Integer, Integer> timeoutCheck = new HashMap<>();
    
    public static HashMap<Integer, String> mapNameByID = new HashMap<>();
    
    public static HashMap<String, Integer> playerMoney = new HashMap<>();
    
    public static int roundChangeTime = 180;
    
    public static HashMap<String, String> playerMeleeWeapon = new HashMap<>();
    
    public static HashMap<String, String> playerClass = new HashMap<>();
    
    public static HashMap<UUID, Integer> mobID = new HashMap<>();
    public static HashSet<LivingEntity> leID = new HashSet<>();
    public static HashSet<CustomEntityIronGolem> igID = new HashSet<>();
    
    public static HashSet<Chunk> chunks = new HashSet<>();
    
    public static HashMap<Integer, Integer> isRoundChanging = new HashMap<>();
    
    public static HashMap<Integer, Integer> mapIDSpawned = new HashMap<>();
    public static HashMap<Integer, Integer> mapIDSpawnedLimit = new HashMap<>();
    
    public static HashMap<String, Location> mobSpawns = new HashMap<>();
    
    public static HashSet<Integer> gameWarmup = new HashSet<>();
    public static HashSet<Integer> gameStarted = new HashSet<>();
  //  public static HashMap<Integer, List<Location>> spawnLocations = new HashMap<>();
    public static HashMap<String, Integer> playersMaps = new HashMap<>();
    
    public static HashMap<Integer, Integer> mapRound = new HashMap<>();
    
    public static HashSet<Location> mapDoorLoc = new HashSet<>();
    public static HashMap<Location, Integer> mapDoorHealth = new HashMap<>();
    public static HashSet<Location> mapDoorBroken = new HashSet<>();
    
    public static HashMap<String, Double> playerCoins = new HashMap<>();
    
    /*
     * 
    public static HashMap<String, Location[]> mapIDShopLocations = new HashMap<>();
    public static HashMap<String, Material> mapIDShopDoorwayMaterial = new HashMap<>();
    public static HashMap<String, Integer> mapIDShopCount = new HashMap<>();
    */
    
    public static HashMap<String, Location[]> GameidShopnumberShoplocations = new HashMap<>();
    public static HashMap<Integer, ArrayList<ItemStack>> GameidShopitemnames = new HashMap<>();
    
    public static HashMap<Integer, ArrayList<ItemStack>> rifleShopItemNames = new HashMap<>();
    public static HashMap<Integer, ArrayList<ItemStack>> pistolShopItemNames = new HashMap<>();
    public static HashMap<Integer, ArrayList<ItemStack>> smgShopItemNames = new HashMap<>();
    public static HashMap<Integer, ArrayList<ItemStack>> shotgunShopItemNames = new HashMap<>();
    public static HashMap<Integer, ArrayList<ItemStack>> meleeShopItemNames = new HashMap<>();
    public static HashMap<Integer, ArrayList<ItemStack>> demoShopItemNames = new HashMap<>();
    
    public static HashMap<String, Material> GameidShopDoorwayMaterial = new HashMap<>();
    
    
    private void startNewGame(int ID)
    {
        new newGame(ID);
        loadMapName(ID);
    }
    
    private void loadMapName(int ID)
    {
        String mapName = SRKF.dbManager.checkMapName(ID);
        if (mapNameByID.containsKey(ID))
        {
            mapNameByID.remove(ID);
        }
        mapNameByID.put(ID, mapName);
    }
    private class newGame implements Runnable
    {
        int x;
        int ID;
        int taskID;
        public newGame(int ID)
        {
            this.x = 0;
            this.ID = ID;
            this.taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, 0, 20);
        }

        @Override
        public void run() 
        {
            x++;
            // countdown
            if (x == 1)
            {
                for (String name : playersMaps.keySet())
                {
                    int id = playersMaps.get(name);
                    if (id == this.ID)
                    {
                        Player player = Bukkit.getPlayer(name);
                        player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.GOLD + "Game starting in 60 seconds.");
                    } 
                }
                
                gameWarmup.add(this.ID);
                
                String mapName = SRKF.dbManager.getMapName(ID);
                String worldName = SRKF.dbManager.getMapWorld(ID);
                
                String mapID = mapName + "_" + ID;
                
                
                
                for (Entity e : Bukkit.getWorld(worldName).getEntities())
                {
                    if (e instanceof LivingEntity)
                    {
                        LivingEntity le = (LivingEntity) e;
                        if (!(le instanceof Player))
                        {
                            le.remove();
                        }
                    }
                    else
                    {
                        e.remove();
                    }
                }
                
                if (!(mobSpawns.containsKey(mapID)))
                {
                    SRKF.dbManager.getMobSpawns(ID, mapName, worldName);
                    SRKF.LOG.log(Level.WARNING, "[SRKF] - Mob Spawns for: " + mapID + " not loaded into memory.");
                    SRKF.LOG.log(Level.INFO, "[SRKF] - Loading mob spawns for Map: " + mapName + " on Server ID: " + ID);
                }

                new gameMobChecker(plugin, ID);
                
                SRKF.LOG.log(Level.INFO, "[SRKF] - Warmup Started for ID: {0}", ID);
                
                new gameMessageTask(plugin, ID);
            }
            
            if (x < 5)
            {
                String mapName = SRKF.dbManager.getMapName(ID);
                String worldName = SRKF.dbManager.getMapWorld(ID);
                
                String mapID = mapName + "_" + ID;
                
                for (Entity e : Bukkit.getWorld(worldName).getEntities())
                {
                    if (e instanceof LivingEntity)
                    {
                        LivingEntity le = (LivingEntity) e;
                        if (!(le instanceof Player))
                        {
                            le.remove();
                        }
                    }
                    else
                    {
                        e.remove();
                    }
                }
            }
            
            
            if (x == 30)
            {
                for (String name : playersMaps.keySet())
                {
                    int id = playersMaps.get(name);
                    if (id == this.ID)
                    {
                        Player player = Bukkit.getPlayer(name);
                        player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.GOLD + "Game starting in 30 seconds.");
                    } 
                }
                
                String mapName = SRKF.dbManager.getMapName(ID);
                String worldName = SRKF.dbManager.getMapWorld(ID);
                
                String mapID = mapName + "_" + ID;
                
                for (Entity e : Bukkit.getWorld(worldName).getEntities())
                {
                    if (e instanceof LivingEntity)
                    {
                        LivingEntity le = (LivingEntity) e;
                        if (!(le instanceof Player))
                        {
                            le.remove();
                        }
                    }
                    else
                    {
                        e.remove();
                    }
                }
            }

            if (x == 50)
            {
                for (String name : playersMaps.keySet())
                {
                    int id = playersMaps.get(name);
                    if (id == this.ID)
                    {
                        Player player = Bukkit.getPlayer(name);
                        player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.GOLD + "Game starting in 10 seconds.");
                    }
                }
                
                String mapName = SRKF.dbManager.getMapName(ID);
                String worldName = SRKF.dbManager.getMapWorld(ID);
                
                String mapID = mapName + "_" + ID;
                
                for (Entity e : Bukkit.getWorld(worldName).getEntities())
                {
                    if (e instanceof LivingEntity)
                    {
                        LivingEntity le = (LivingEntity) e;
                        if (!(le instanceof Player))
                        {
                            le.remove();
                        }
                    }
                    else
                    {
                        e.remove();
                    }
                }
            }

            if (x == 60)
            {
                for (String name : playersMaps.keySet())
                {
                    int id = playersMaps.get(name);
                    if (id == this.ID)
                    {
                        Player player = Bukkit.getPlayer(name);
                        player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.GOLD + "Game starting!");
                    } 
                }

                if (gameWarmup.contains(this.ID))
                {
                    gameWarmup.remove(this.ID);
                }
                /*
                for (String name : playersMaps.keySet())
                {
                    int id = playersMaps.get(name);
                    if (id == this.ID)
                    {
                        Player player = Bukkit.getPlayer(name);
                        
                        if (InventoryListener.playerPrimary.containsKey(player.getName()))
                        {
                            ItemStack primary = InventoryListener.playerPrimary.get(player.getName()).clone();
                            player.getInventory().addItem(primary);
                        }
                        
                        if (InventoryListener.playerSecondary.containsKey(player.getName()))
                        {
                            ItemStack secondary = InventoryListener.playerSecondary.get(player.getName()).clone();
                            player.getInventory().addItem(secondary);
                        }
                        
                        if (InventoryListener.playerMedKits.containsKey(player.getName()))
                        {
                            ItemStack medkits = InventoryListener.playerMedKits.get(player.getName()).clone();
                            player.getInventory().addItem(medkits);
                        }
                        
                        if (InventoryListener.playerGrenade.containsKey(player.getName()))
                        {
                            ItemStack grenade = InventoryListener.playerGrenade.get(player.getName()).clone();
                            player.getInventory().addItem(grenade);
                        }
                        
                        if (InventoryListener.playerSpecial.containsKey(player.getName()))
                        {
                            ItemStack special = InventoryListener.playerSpecial.get(player.getName()).clone();
                            player.getInventory().addItem(special);
                        }
                        
                        if (InventoryListener.playerPrimaryAmmo.containsKey(player.getName()))
                        {
                            ItemStack priAmmo = InventoryListener.playerPrimaryAmmo.get(player.getName()).clone();
                            player.getInventory().addItem(priAmmo);
                        }
                        
                        if (InventoryListener.playerSecondaryAmmo.containsKey(player.getName()))
                        {
                            ItemStack secAmmo = InventoryListener.playerSecondaryAmmo.get(player.getName()).clone();
                            player.getInventory().addItem(secAmmo);
                        }
                        
                        //
                        
                        ItemStack kevlarVest = Guns.miscItemStacks.get("Kevlar Vest").clone();
                        ItemStack kevlarPants = Guns.miscItemStacks.get("Kevlar Pants").clone();
                        ItemStack kevlarBoots = Guns.miscItemStacks.get("Kevlar Boots").clone();
                        
                        player.getInventory().setChestplate(kevlarVest);
                        player.getInventory().setLeggings(kevlarPants);
                        player.getInventory().setBoots(kevlarBoots);
                    } 
                }
                */
                
                int status = 1; // started
                SRKF.dbManager.setMapStatus(this.ID, status);
                Location min = SRKF.dbManager.getMapMinLocation(this.ID);
                Location max = SRKF.dbManager.getMapMaxLocation(this.ID);
                int round = 0;
                
                new roundChanger(plugin, this.ID, round);
                
                new spawnMobs(plugin, this.ID, min, max);
                
                Bukkit.getScheduler().cancelTask(taskID);
                
                for (String name : playersMaps.keySet())
                {
                    int id = playersMaps.get(name);
                    if (id == this.ID)
                    {
                        Player player = Bukkit.getPlayer(name);
                        String role = SRKF.player_dbManager.getRole(player.getName());
                        if (Game.playerClass.containsKey(player.getName()))
                        {
                            Game.playerClass.remove(player.getName());
                        }
                        Game.playerClass.put(player.getName(), role);
                        
                        if (role.equalsIgnoreCase("Berserker"))
                        {
                            if (player.hasPotionEffect(PotionEffectType.SPEED))
                            {
                                player.removePotionEffect(PotionEffectType.SPEED);
                                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,0,0,true));
                            }
                            
                            int level = Berserker.getEnduranceBonusAmount(player.getName());
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 70000, level), true);
                        }
                    }
                }
                
                SRKF.LOG.log(Level.INFO, "[SRKF] - Game Started for ID: " + this.ID + " at round " + round);
            }
        }
    }
    
    public static void updateMobCount(int gameID)
    {
        if (gameStarted.contains(gameID))
        {
            int spawnAmount = 0;
            if (mapIDSpawned.containsKey(gameID))
            {
                spawnAmount = mapIDSpawned.get(gameID);
            }
            
            int killAmount = 0;
            if (PL.mapKills.containsKey(gameID))
            {
                killAmount = PL.mapKills.get(gameID);
            }

            int newAmount = spawnAmount - killAmount;
            
            //Bukkit.broadcastMessage("Mob Count Update. newAmount: " + newAmount);
            for (String name : playersMaps.keySet())
            {
                int id = playersMaps.get(name);
                if (id == gameID)
                {
                    Player inGame = Bukkit.getPlayer(name);
                    inGame.setLevel(newAmount);
                } 
            }
        }
    }
    
    public static void setDoorPositions(Location loc)
    {
        Location l = loc;
        /*
        loc.getBlock().setType(Material.AIR);
        loc.setY(loc.getY() + 1);
        loc.getBlock().setType(Material.AIR);
        loc.setY(loc.getY() - 1);
        */

        Block lowerdoor = l.getBlock();
        Block upperdoor = lowerdoor.getRelative(BlockFace.UP, 1);

        /*
        Byte data1 = (0x8);
        Byte data2 = (0x4);
        
        //upperdoor.setTypeIdAndData(64, data1, true);
        //lowerdoor.setTypeIdAndData(64, data2, true);

        //loc.getWorld().getBlockAt(l).setTypeId(64);
        
        
        upperdoor.setData(data1, true);
        lowerdoor.setData(data2, true);
        */
        
        BlockState state = upperdoor.getState();
        if (state.getData() instanceof Openable)
        {
            Openable om = (Openable) state.getData();
            om.setOpen(true);
            state.setData((MaterialData) om);
            state.update();
        }

        state = lowerdoor.getState();
        if (state.getData() instanceof Openable)
        {
            Openable om = (Openable) state.getData();
            om.setOpen(true);
            state.setData((MaterialData) om);
            state.update();
        }
    }
    
    public static void shopFirework(Location loc)
    {
        loc.setY(loc.getY() + 35);
        Firework fw = (Firework)loc.getWorld().spawn(loc, Firework.class);
        DyeColor fwColor = DyeColor.WHITE;
        FireworkEffect.Type fwEffectType = FireworkEffect.Type.BALL_LARGE;
        FireworkMeta fwMeta = fw.getFireworkMeta();
        FireworkEffect fwEffect = FireworkEffect.builder().trail(true).flicker(false).withColor(fwColor.getFireworkColor()).with(fwEffectType).build();
        fwMeta.clearEffects();
        fwMeta.addEffect(fwEffect);
        try
        {
            Field f = fwMeta.getClass().getDeclaredField("power");
            f.setAccessible(true);
            try
            {
                f.set(fwMeta, Integer.valueOf(-2));
            } catch (IllegalAccessException e)
            {
                e.printStackTrace();
            }
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException e)
        {
            e.printStackTrace();
        }

        fw.setFireworkMeta(fwMeta);
    }
}
