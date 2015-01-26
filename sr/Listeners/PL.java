package sr.Listeners;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.net.Socket;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Effect;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftVillager;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.LargeFireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Witch;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.util.Vector;
import sr.Extras.EXP;
import sr.Extras.Guns;
import sr.Game;
import sr.Roles.Berserker;
import sr.Roles.Commando;
import sr.Roles.Medic;
import sr.Roles.Sharpshooter;
import sr.SRKF;
import sr.Tasks.commandoInspireTask;
import sr.Tasks.sharpshooterLuckyTask;
import sr.Tasks.woundTask;
import sr.Utilities.AngleUtils;
import sr.coinStuff.EnhancementListener;
import sr.coinStuff.rankEL;



/**
 *
 * @author Cross
 */
public class PL implements Listener
{
    public SRKF plugin;
    
    public PL (SRKF plugin)
    {
        this.plugin = plugin;
    }
    
    public static HashSet<String> weldingSB = new HashSet<>();
    
    public static HashMap<String, Integer> playerShots = new HashMap<>();
    public static HashMap<String, Integer> playerHits = new HashMap<>();
    
    public static HashSet<String> isDowned = new HashSet<>();
    
    public static HashMap<Integer, Integer> mapKills = new HashMap<>();
    public static HashMap<String, Integer> playerKills = new HashMap<>();
    
    public static HashMap<String, Integer> isBandaging = new HashMap<>();
    
    public static HashMap<String, Integer> incomingPlayer = new HashMap<>();
    public static HashMap<String, Integer> incomingSpectator = new HashMap<>();
    public static HashSet<String> isSpectating = new HashSet<>();
    
    private Socket socket;
    private int ProxySocketPort;
    private String proxyIP;
    
    public static HashMap<UUID, String> grenadeHit = new HashMap<>();
    public static HashMap<UUID, String> rocketHit = new HashMap<>();

    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent event)
    {
        if (isSpectating.contains(event.getPlayer().getName()))
        {
            event.setCancelled(true);
            
            String msg = event.getMessage();
            for (Player p : Bukkit.getOnlinePlayers())
            {
                if (p.getWorld().equals(event.getPlayer().getWorld()))
                {
                    p.sendMessage(ChatColor.AQUA + event.getPlayer().getName() + ": " + ChatColor.DARK_AQUA + msg);
                }
            }
        }
    }
    
    @EventHandler
    public void onMove(PlayerMoveEvent event)
    {
        Player player = event.getPlayer();
        if (!(SRKF.isLobby))
        {
            if (isSpectating.contains(player.getName()))
            {
                if (player.getInventory().getHelmet() != null)
                {
                    player.getInventory().setHelmet(null);
                }
                if (!(player.hasPotionEffect(PotionEffectType.INVISIBILITY)))
                {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 144000, 2), true);
                }
            }
        }
    }
    
    @EventHandler
    public void onDeath(PlayerDeathEvent event)
    {
        Player player = event.getEntity();
        
        // code in a spectator thing for later? for now just send to lobby on death
        int round = 0;
        int kills = 0;
        int exp = 0;
        double coins = 0;
        
        if (playerKills.containsKey(player.getName()))
        {
            kills = playerKills.get(player.getName());
        }
        
        int ID = 0;
        if (Game.playersMaps.containsKey(player.getName()))
        {
            ID = Game.playersMaps.get(player.getName());
        }
        
        if (Game.mapRound.containsKey(ID))
        {
            round = Game.mapRound.get(ID);
        }
        
        if (EXP.playerEXP.containsKey(player.getName()))
        {
            exp = EXP.playerEXP.get(player.getName());
        }
        
        if (Game.playerCoins.containsKey(player.getName()))
        {
            coins = Game.playerCoins.get(player.getName());
        }
        double rankmultiplier = 1;
        String rankname = rankEL.playernamerank.get(player.getName().toLowerCase());

        if(rankname.toLowerCase().contains("vip"))
        {
            rankmultiplier = 1.25;
        }
        if(rankname.toLowerCase().contains("elite"))
        {
            rankmultiplier = 1.5;
        }
        if(rankname.toLowerCase().contains("hero"))
        {
            rankmultiplier = 1.75;
        }
        if(rankname.toLowerCase().contains("champion"))
        {
            rankmultiplier = 2;
        }

        coins = coins * rankmultiplier;
        
        double baseChance = 0;
        
        boolean getsChest = false;
        
        if (round >= 5)
        {
            baseChance = 0.05;
            double bonusChance = (round / 100) - 0.05;
            double totalChance = baseChance + bonusChance;
            
            double chestRand = Math.random();
            
            if (totalChance <= chestRand)
            {
                getsChest = true;
            }
        }
        
        SRKF.player_dbManager.updatePlayerStats(player.getName(), true, round, getsChest);

        cleanUpHashes(player.getName());
        
        player.sendMessage(ChatColor.DARK_GRAY + "[SR"+ ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "You died on round " + ChatColor.GREEN + round + ChatColor.RED + " after killing " + ChatColor.GREEN + kills + ChatColor.RED + " monsters!");
        player.sendMessage(ChatColor.DARK_GRAY + "[SR"+ ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.AQUA + "You gained " + exp + " experience that game!");
        player.sendMessage(ChatColor.DARK_GRAY + "[SR"+ ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.AQUA + "You earned " + coins + " coins that game!");
        if (getsChest)
        {
            player.sendMessage(ChatColor.DARK_GRAY + "[SR"+ ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.GREEN + "You earned a chest that game!");
        }

        toLobby(player);
    }
    
    @EventHandler
    public void onQuit(PlayerQuitEvent event)
    {
        Player player = event.getPlayer();
        event.setQuitMessage("");
        
        if (isSpectating.contains(player.getName()))
        {
            isSpectating.remove(player.getName());
        }
        
        if (Game.playersMaps.containsKey(player.getName()))
        {
            int ID = Game.playersMaps.get(player.getName());
            SRKF.dbManager.setMapPlayers(ID, "leave");
            
            int round = 0;
            if (Game.mapRound.containsKey(ID))
            {
                round = Game.mapRound.get(ID);
                
            }
            /*
            * 
            * Update player statistics in database for PL.playerKills
            */
            
            double baseChance = 0;
        
            boolean getsChest = false;

            if (round >= 5)
            {
                baseChance = 0.05;
                double bonusChance = (round / 100) - 0.05;
                double totalChance = baseChance + bonusChance;

                double chestRand = Math.random();

                if (totalChance <= chestRand)
                {
                    getsChest = true;
                }
            }
            
            SRKF.player_dbManager.updatePlayerStats(player.getName(), true, round, getsChest);
        }
        
        cleanUpHashes(player.getName());
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        event.setJoinMessage("");
        
        /*
        if (incomingPlayer.containsKey(player.getName()))
        {
            incomingPlayer.remove(player.getName());
        }
        int tempID = 2;
        
        incomingPlayer.put(player.getName(), tempID);
        
        */
        
        
        if (SRKF.isLobby)
        {
            SRKF.player_dbManager.createNewPlayer(player.getName());
            int points = SRKF.player_dbManager.getPlayerStat(player.getName(), "UnspentPoints");
            if (points > 0)
            {
                player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.AQUA + "You have unspent skill points!");
                player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.AQUA + "To upgrade your perks, type: " + ChatColor.GOLD + "/perks");
            }
            
        }
        
        if (!(SRKF.isLobby))
        {
            if (player.isOp())
            {
                player.setAllowFlight(true);
                player.setFlying(false);
            }
            else
            {
                player.setAllowFlight(false);
                player.setFlying(false);
            }
            
            
            
            if (incomingSpectator.containsKey(player.getName()))
            {
                int ID = incomingSpectator.get(player.getName());
                String worldName = SRKF.dbManager.getMapWorld(ID);
                String mapName = SRKF.dbManager.getMapName(ID);
                Location loc = SRKF.dbManager.getSpawnRandom(mapName, worldName);
                loc.setY(loc.getY() + 10);
                
                incomingSpectator.remove(player.getName());
                player.setAllowFlight(true);
                player.setFlying(true);
                player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 144000, 2), true);
                
                if (isSpectating.contains(player.getName()))
                {
                    isSpectating.remove(player.getName());
                }
                isSpectating.add(player.getName());
                SRKF.LOG.info("[SRKF] - Spectator " + player.getName() + " teleported to spawn location.");
                player.teleport(loc);
                
                player.sendMessage(ChatColor.DARK_GRAY + "[SR"+ ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.GOLD + "Spectating Map ID " + ID + ".");
                
                player.setHealth(player.getMaxHealth());
                player.getInventory().setChestplate(null);
                player.getInventory().setLeggings(null);
                player.getInventory().setBoots(null);
                player.getInventory().clear();
                player.setFoodLevel(20);
                player.setLevel(0);
                player.setExp(0);
            }
            
            Iterator<String> spectatorIt = isSpectating.iterator();
            while (spectatorIt.hasNext())
            {
                String name = spectatorIt.next();
                Player spec = Bukkit.getPlayer(name);
                if (spec.isOnline())
                {
                    player.hidePlayer(spec);
                }
            }
            
            if (incomingPlayer.containsKey(player.getName()))
            {
                int ID = incomingPlayer.get(player.getName());

                SRKF.LOG.log(Level.INFO, "[SRKF] {0} joining for ID: {1}", new Object[]{player.getName(), ID});
                
                boolean isReady = SRKF.dbManager.checkServerReady(ID);

                if (isReady)
                {
                    String worldName = SRKF.dbManager.getMapWorld(ID);
                    String mapName = SRKF.dbManager.getMapName(ID);
                    Location loc = SRKF.dbManager.getSpawnRandom(mapName, worldName);
                    
                    for (Player p : Bukkit.getServer().getOnlinePlayers())
                    {
                        if ((!p.equals(player)) && (!(p.canSee(player))))
                        {
                            p.showPlayer(player);
                        }
                    }
                    
                    incomingPlayer.remove(player.getName());
                    Game.playersMaps.put(player.getName(), ID);
                    SRKF.LOG.info("[SRKF] - Player " + player.getName() + " teleported to spawn location.");
                    player.teleport(loc);
                    player.sendMessage(ChatColor.DARK_GRAY + "[SR"+ ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.GOLD + "Teleport to Map ID " + ID + ". In Warmup.");
                    SRKF.LOG.info("[SRKF] - Player " + player.getName() + " is actually in world " + loc.getWorld().getName());
                    
                    String role = SRKF.player_dbManager.getRole(player.getName());
                    if (Game.playerClass.containsKey(player.getName()))
                    {
                        Game.playerClass.remove(player.getName());
                    }
                    Game.playerClass.put(player.getName(), role);
                    
                    if (player.hasPotionEffect(PotionEffectType.SPEED))
                    {
                        player.removePotionEffect(PotionEffectType.SPEED);
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,0,0), true);
                    }
                    
                    if (player.hasPotionEffect(PotionEffectType.INVISIBILITY))
                    {
                        player.removePotionEffect(PotionEffectType.INVISIBILITY);
                        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,0,0), true);
                    }
                    
                    if (player.hasPotionEffect(PotionEffectType.SLOW))
                    {
                        player.removePotionEffect(PotionEffectType.SLOW);
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,0,0), true);
                    }
                    
                    if (player.hasPotionEffect(PotionEffectType.SLOW_DIGGING))
                    {
                        player.removePotionEffect(PotionEffectType.SLOW_DIGGING);
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING,0,0), true);
                    }
                    
                    if (player.hasPotionEffect(PotionEffectType.WITHER))
                    {
                        player.removePotionEffect(PotionEffectType.WITHER);
                        player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER,0,0), true);
                    }
                    
                    if (role.equalsIgnoreCase("Berserker"))
                    {
                        int level = Berserker.getEnduranceBonusAmount(player.getName());
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 70000, level), true);
                    }
                    else
                    {
                        if (player.hasPotionEffect(PotionEffectType.SPEED))
                        {
                            player.removePotionEffect(PotionEffectType.SPEED);
                        }
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 0, 0), true);
                    }
                    
                    double baseHealth = 20;
                    double bonusHealth = 0;
                    if (role.equalsIgnoreCase("Commando"))
                    {
                        int durationBonus = Commando.getInspireBonusDurationAmount(player.getName());
                        int reuseBonus = Commando.getInspireBonusFrequencyAmount(player.getName());
                        new commandoInspireTask(plugin, player, durationBonus, reuseBonus);
                        
                        bonusHealth = Commando.getJuggernautBonusAmount(player.getName());
                    }
                    
                    if (role.equalsIgnoreCase("Sharpshooter"))
                    {
                        new sharpshooterLuckyTask(plugin, player);
                    }
                    
                    if (!(Game.gameWarmup.contains(ID)))
                    {
                        new Game(plugin, ID);
                        SRKF.dbManager.loadMapDoors(ID, mapName, worldName);
                        SRKF.dbManager.loadShops(ID, mapName, worldName);
                        Game.gameIDWorldName.put(ID, worldName);
                        SRKF.dbManager.setMapPlayers(ID, 0);
                    }
                    
                    SRKF.dbManager.setMapPlayers(ID, "join");

                    double totalHealth = baseHealth + bonusHealth;
                    
                    player.setHealth(player.getMaxHealth());
                    player.setMaxHealth(totalHealth);
                    player.damage(1);
                    player.getInventory().setChestplate(null);
                    player.getInventory().setLeggings(null);
                    player.getInventory().setBoots(null);
                    player.getInventory().clear();
                    player.setFoodLevel(20);
                    player.setLevel(0);
                    player.setExp(0);
                    
                    /*
                     * Scoreboard
                     */
                    
                    ScoreboardManager manager = Bukkit.getScoreboardManager();
                    Scoreboard sb = manager.getNewScoreboard();
                    int cash = 0;
                    if (Game.playerMoney.containsKey(player.getName()))
                    {
                        cash = Game.playerMoney.get(player.getName());
                    }

                    Objective objective = sb.registerNewObjective("kf", "dummy");
                    objective.setDisplaySlot(DisplaySlot.SIDEBAR);
                    objective.setDisplayName(ChatColor.GRAY + "Killing Floor");

                    Score score = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.GOLD + " Cash: " + ChatColor.GREEN + "$"));
                    score.setScore(cash);
                    player.setScoreboard(sb);
                    
                    /*
                     * 
                     */

                    if (player.getHealth() != player.getMaxHealth())
                    {
                        player.setHealth(player.getMaxHealth());
                    }
                    
                  //  SRKF.LOG.info("role is: " + role);
                    InventoryListener.setupPrimaryWeapon(player.getName(), role);
                    //InventoryListener.setupPrimaryAmmo(player.getName());
                    
                    InventoryListener.setupSecondaryWeapon(player.getName(), role);
                    //InventoryListener.setupSecondaryAmmo(player.getName());
                    
                    InventoryListener.setupMedKits(player.getName());
                    InventoryListener.setupGrenade(player.getName());
                    InventoryListener.setupSpecialItem(player.getName());

                    SRKF.player_dbManager.loadPlayerRoleStats(player.getName());

                    
                    
                    loadPlayerInv(player);
                }
                else
                {
                    incomingPlayer.remove(player.getName());
                    
                    player.sendMessage(ChatColor.DARK_GRAY + "[SR"+ ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "Map is full.");
                    SRKF.LOG.log(Level.INFO, "[SRKF] - Player {0} sent back to lobby due to Map ID: {1} is full.", new Object[]{player.getName(), ID});

                    toLobby(player);
                }

            }
        }
        
    }
    
    @EventHandler(priority=EventPriority.HIGHEST)
    public void onEntityDamage(EntityDamageEvent event)
    {
        Entity ent = event.getEntity();
        
        if (ent instanceof LivingEntity)
        {
            LivingEntity le = (LivingEntity) ent;
            
            if (le instanceof CraftVillager || le instanceof Villager)
            {
                event.setDamage(0);
                event.setCancelled(true);
            }
            
            UUID uid = le.getUniqueId();
            
            if (rocketHit.containsKey(uid))
            {
                double mobHP = le.getHealth();
                double dmg = event.getDamage();
                if ((mobHP - dmg) <= 0)
                {
                    String pName = rocketHit.get(uid);
                    OfflinePlayer op = Bukkit.getOfflinePlayer(pName);
                    if (op.isOnline())
                    {
                        Player grenadier = (Player) op;
                        int killCount = 0;

                        if (PL.playerKills.containsKey(grenadier.getName()))
                        {
                            killCount = PL.playerKills.get(grenadier.getName());
                            PL.playerKills.remove(grenadier.getName());
                        }

                        killCount++;
                        PL.playerKills.put(grenadier.getName(), killCount);
                    }
                }
                
                rocketHit.remove(uid);
            }
            
            if (grenadeHit.containsKey(uid))
            {
                double mobHP = le.getHealth();
                double dmg = event.getDamage();
                if ((mobHP - dmg) <= 0)
                {
                    String pName = grenadeHit.get(uid);
                    OfflinePlayer op = Bukkit.getOfflinePlayer(pName);
                    if (op.isOnline())
                    {
                        Player grenadier = (Player) op;
                        int killCount = 0;

                        if (PL.playerKills.containsKey(grenadier.getName()))
                        {
                            killCount = PL.playerKills.get(grenadier.getName());
                            PL.playerKills.remove(grenadier.getName());
                        }

                        killCount++;
                        PL.playerKills.put(grenadier.getName(), killCount);
                    }
                }
                
                grenadeHit.remove(uid);
            }
            
            if (Game.mobHealth.containsKey(uid))
            {
                if ((le.getHealth() - event.getDamage()) > 0)
                {
                    double hashHealth = Game.mobHealth.get(uid);
                    int inc = (int) Math.ceil(hashHealth / 10);
                    double currentHealth = le.getHealth();
                    int diff = (int) Math.ceil(currentHealth / inc);
                    String healthString = ChatColor.DARK_RED + "";
                    String greyString = ChatColor.GRAY + "";
                    for (int i = 0; i < diff; i++)
                    {
                        healthString = healthString + "♥";
                    }
                    int grayDiff = (10 - diff);
                    for (int i = 0; i < grayDiff; i++)
                    {
                        greyString = greyString + "♥";
                    }
                    le.setCustomName(healthString + greyString);
                    le.setCustomNameVisible(true);
                }
            }
            
            if (!(le instanceof Player))
            {
                //int noDmg = le.getNoDamageTicks();
                //int maxNoDmg = le.getMaximumNoDamageTicks();
               // Bukkit.broadcastMessage("noDmg is: " + noDmg);
               // Bukkit.broadcastMessage("maxNoDmg is: " + maxNoDmg);
                le.setNoDamageTicks(0);
                
                if (event.getCause() == DamageCause.FIRE || event.getCause() == DamageCause.FIRE_TICK)
                {
                    event.setCancelled(true);
                    event.setDamage(0);
                }
                
                if (event.getCause() == DamageCause.VOID)
                {
                    double LOL = 400;
                    le.damage(LOL);
                }
            }
            
            if (le instanceof Player)
            {
                Player player = (Player) ent;

                if (event.getCause() == DamageCause.VOID)
                {
                    // send home
                    player.sendMessage(ChatColor.RED + "Fell into the void... ");
                    toLobby(player);
                }                
                
                if (isSpectating.contains(player.getName()))
                {
                    event.setDamage(0);
                    event.setCancelled(true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 144000, 2), true);
                    return;
                }
                
                if (player.getInventory().getChestplate() != null)
                {
                    ItemStack chest = player.getInventory().getChestplate();
                    chest.setDurability((short) 0);
                }
                if (player.getInventory().getLeggings() != null)
                {
                    ItemStack legs = player.getInventory().getLeggings();
                    legs.setDurability((short) 0);
                }
                if (player.getInventory().getBoots() != null)
                {
                    ItemStack boots = player.getInventory().getBoots();
                    boots.setDurability((short) 0);
                }
                
                if (event.getCause() == DamageCause.FALL)
                {
                    event.setCancelled(true);
                    event.setDamage(0);
                }

                
                if (Game.playerClass.containsKey(player.getName()))
                {
                    String role = Game.playerClass.get(player.getName());
                    if (role.equalsIgnoreCase("Berserker"))
                    {
                        double reduction = Berserker.getKevlarAdaptationBonusAmount(player.getName());
                        double dmg = event.getDamage();
                        double newDmg = dmg * reduction;
                        
                        if (newDmg < 1)
                        {
                            newDmg = 1;
                        }
                        
                        event.setDamage(newDmg);
                    }
                }
                
                if (isDowned.contains(player.getName()))
                {
                    event.setCancelled(true);
                    event.setDamage(0);

                    if (player.getFireTicks() > 0)
                    {
                        player.setFireTicks(0);
                    }

                    if (player.hasPotionEffect(PotionEffectType.WITHER))
                    {
                        player.removePotionEffect(PotionEffectType.WITHER);
                        player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER,0,0));
                    }

                    if (player.hasPotionEffect(PotionEffectType.POISON))
                    {
                        player.removePotionEffect(PotionEffectType.POISON);
                        player.addPotionEffect(new PotionEffect(PotionEffectType.POISON,0,0));
                    }
                }
                
                double dmg = event.getDamage();
                double death = player.getHealth() - dmg;
                
                if (death <= 0)
                {
                    if (isDowned.contains(player.getName()))
                    {
                        event.setCancelled(true);
                        event.setDamage(0);
                        return;
                    }
                    else
                    {
                        event.setCancelled(true);
                        event.setDamage(0);  
                        
                        isDowned.add(player.getName());
                        player.setHealth(1);
                        new downedEvent(player);
                        int ID = Game.playersMaps.get(player.getName());
                        for (String name : Game.playersMaps.keySet())
                        {
                            int id = Game.playersMaps.get(name);
                            if (id == ID)
                            {
                                Player inGame = Bukkit.getPlayer(name);
                                if (inGame != player)
                                {
                                    inGame.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.GOLD + player.getName()
                                        + ChatColor.RED + ChatColor.BOLD + " is DOWNED! Provide them medical attention!");
                                }
                            } 
                        }
                        player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " 
                                + ChatColor.RED + ChatColor.BOLD + "You are DOWNED! You need immediate medical attention.");
                    }
                }
            }
        }
        
        
    }
    
    @EventHandler
    public void onRespawn (PlayerRespawnEvent event)
    {
        toLobby(event.getPlayer());
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerAnimation (PlayerAnimationEvent event)
    {
        Player player = event.getPlayer();
        
        if (!(player.hasPotionEffect(PotionEffectType.SLOW_DIGGING)))
        {
            if (player.getItemInHand() != null)
            {
                ItemStack inHand = player.getItemInHand();

                if (inHand.hasItemMeta())
                {
                    ItemMeta meta = inHand.getItemMeta();
                    if (meta.hasDisplayName())
                    {
                        if (meta.hasLore())
                        {
                            List<String> lore = meta.getLore();
                            String meleeName = lore.get(1);

                            if (Guns.MeleeCheck.contains(meleeName))
                            {
                                int delay = Guns.MeleeDelay.get(meleeName);

                                int slowTime = 7 + (10 * delay);
                                int slowLevel = 10 * delay;

                                if (delay == 0)
                                {
                                    slowTime = 0;
                                    slowLevel = 5;
                                }

                                player.setFoodLevel(20);
                                player.setSaturation(10);
                                player.setExhaustion(0);
                                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING,slowLevel,slowTime), true);
                                player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION,2,0), true);
                            }
                        }
                    }
                }
                
            }
        }
        
    }
    
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event)
    {
        Entity ent = event.getEntity();
        Entity dmger = event.getDamager();

        if (SRKF.isLobby)
        {
            return;
        }
        /*
        if (!(ent instanceof Player))
        {
            if ((!(dmger instanceof Player)) || (!(dmger instanceof Projectile)))
            {
                event.setCancelled(true);
                event.setDamage(0);
                return;
            }
        }
        */
        if (dmger instanceof LivingEntity)
        {
            LivingEntity le = (LivingEntity) dmger;
            if (le.hasPotionEffect(PotionEffectType.INVISIBILITY))
            {
                le.removePotionEffect(PotionEffectType.INVISIBILITY);
                le.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,0,0));
            }
            
            if (le instanceof Zombie)
            {
                if (Game.mobID.containsKey(le.getUniqueId()))
                {
                    Zombie ig = (Zombie) le;
                    
                    if (ig.hasPotionEffect(PotionEffectType.SLOW_DIGGING) && (!(ig.hasPotionEffect(PotionEffectType.SATURATION)) ))
                    {
                        event.setCancelled(true);
                    }
                    else
                    {
                        if (ent instanceof LivingEntity)
                        {
                            LivingEntity damaged = (LivingEntity) ent;
                            damaged.setNoDamageTicks(0);
                            ig.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING,100,100), true);
                            ig.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION,1,0), true);
                            
                            if (ent instanceof Player)
                            {
                                int gameID = Game.mobID.get(le.getUniqueId());
                                int round = Game.mapRound.get(gameID);

                                double dmg = Math.floor((event.getDamage() + ((round * 0.65) / 2) * 0.65));
                                //SRKF.LOG.info("dmg is: " + dmg);
                                event.setDamage(dmg);
                            }
                        }
                    }
                    
                    
                    
                }
            }
            
            if (le instanceof IronGolem)
            {
                IronGolem ig = (IronGolem) le;
                
                if (ig.hasPotionEffect(PotionEffectType.SLOW_DIGGING) && (!(ig.hasPotionEffect(PotionEffectType.SATURATION)) ))
                {
                    event.setCancelled(true);
                }
                else
                {
                    if (ent instanceof LivingEntity)
                    {
                        LivingEntity damaged = (LivingEntity) ent;
                        damaged.setNoDamageTicks(0);
                        ig.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING,100,100), true);
                        ig.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION,1,0), true);
                    }
                    
                    if (ent instanceof Player)
                    {
                        event.setDamage(Math.floor(event.getDamage() * 0.65));
                    }
                }
            }
            
            if (le instanceof Blaze)
            {
                if (ent instanceof Player)
                {
                    event.setDamage(Math.floor(event.getDamage() * 0.65));
                }
            }
            
            if (le instanceof Creeper)
            {
                if (!(ent instanceof Player))
                {
                    event.setCancelled(true);
                    event.setDamage(0);
                }
                else
                {
                    //SRKF.LOG.info("creeper did: " + rounded);
                    event.setCancelled(true);
                    event.setDamage(0);
                    
                    Player target = (Player) ent;
                    int round = 1;
                    if (Game.mobID.containsKey(le.getUniqueId()))
                    {
                        round = Game.mobID.get(le.getUniqueId());
                    }
                    
                    int duration = (round * 20) / 2;
                    target.addPotionEffect(new PotionEffect(PotionEffectType.POISON,duration,1), true);
                    target.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION,duration,1), true);
                }
            }
            if (!(le instanceof Player))
            {
                if (!(ent instanceof Player))
                {
                    event.setDamage(0);
                    event.setCancelled(true);
                }
                
                if (ent instanceof Player)
                {
                    double dmg = event.getDamage();

                    if (le instanceof Skeleton)
                    {
                        event.setDamage(dmg + (dmg / 2));
                        Player target = (Player) ent;
                        target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 1), true);
                    }

                    Player player = (Player) ent;
                    String role = "null";
                    if (Game.playerClass.containsKey(player.getName()))
                    {
                        role = Game.playerClass.get(player.getName());
                    }
                    
                    if (role.equalsIgnoreCase("Commando"))
                    {
                        double reflect = Commando.getPainBonusAmount(player.getName());
                        le.damage(reflect, player);
                    }
                }
            }
            
        }
        
        if (dmger instanceof Player)
        {
            if (ent instanceof Player)
            {

                event.setCancelled(true);
                event.setDamage(0);
                return;
                
            }
            Player damager = (Player) dmger;
            
            if (isSpectating.contains(damager.getName()))
            {
                event.setDamage(0);
                event.setCancelled(true);
                damager.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 144000, 2), true);
                return;
            }
            
            /*
            if (isReloading.contains(damager.getName()))
            {
                event.setCancelled(true);
                event.setDamage(0);
                damager.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "You are reloading and cannot do anything else!");
                return;
            }
            */
            
            ItemStack inHand = damager.getItemInHand();
            if (inHand != null)
            {
                inHand.setDurability((short) 0);
                
                if (inHand.hasItemMeta())
                {
                    ItemMeta meta = inHand.getItemMeta();

                    if (meta.hasDisplayName())
                    {
                        if (meta.hasLore())
                        {
                            List<String> lore = meta.getLore();
                            String meleeName = lore.get(1);

                            if (Guns.MeleeCheck.contains(meleeName))
                            {
                                double range = Guns.MeleeRange.get(meleeName);

                                if (ent.getLocation().distance(damager.getLocation()) <= range)
                                {
                                    int delay = Guns.MeleeDelay.get(meleeName);

                                    /*
                                    int hungerCost = 1 * delay;
                                    int removeHunger = damager.getFoodLevel() - hungerCost;
                                    if (removeHunger <= 0)
                                    {
                                        damager.setFoodLevel(0);
                                    }
                                    else
                                    {
                                        damager.setFoodLevel(removeHunger);
                                    }
                                    */
                                    
                                    if (damager.hasPotionEffect(PotionEffectType.SLOW_DIGGING) && (!(damager.hasPotionEffect(PotionEffectType.SATURATION)) ))
                                    {
                                        event.setCancelled(true);
                                    }
                                    
                                    

                                    double dmg = Guns.MeleeDamage.get(meleeName);
                                    /*
                                     * add bonus for Berserker
                                     */
                                    boolean isBehind = false;
                                    double ferocityBonus = 1;
                                    double strengthBonus = 0;
                                    String role = "Null";
                                    if (Game.playerClass.containsKey(damager.getName()))
                                    {
                                        role = Game.playerClass.get(damager.getName());
                                    }

                                    if (role.equalsIgnoreCase("Berserker"))
                                    {
                                        strengthBonus = Berserker.getStrengthBonusAmount(damager.getName());
                                        double angle = AngleUtils.getAngle(ent.getLocation().getDirection(), damager.getLocation().getDirection());

                                        if (angle < AngleUtils.maxAngle)
                                        {
                                            isBehind = true;
                                            ferocityBonus = Berserker.getFerocityBonusAmount(damager.getName(), damager.getMaxHealth(), damager.getHealth());
                                        }
                                    }

                                    if (isBehind)
                                    {
                                        dmg = dmg * ferocityBonus;
                                    }


                                    /*
                                     * Assign damage
                                     */
                                    double totalDamage = (dmg + strengthBonus);

                                    event.setDamage(totalDamage);
                                }
                                else
                                {
                                    event.setCancelled(true);
                                    event.setDamage(0);
                                    //damager.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "You swing and miss. Get closer!");
                                }
                            }
                            else
                            {
                                event.setCancelled(true);
                                event.setDamage(0);
                            }
                        }
                        else
                        {
                            event.setCancelled(true);
                            event.setDamage(0);
                        }
                    }
                    else
                    {
                        event.setCancelled(true);
                        event.setDamage(0);
                    }
                }
                else
                {
                    event.setCancelled(true);
                    event.setDamage(0);
                }
            }
            
        }
        
        /*
         * 
         * Start Gun Projectiles
         * 
         */
        
        if (dmger instanceof Projectile)
        {
            Projectile proj = (Projectile) dmger;
            
            if (proj.getShooter() instanceof Skeleton)
            {
                if (ent instanceof Player)
                {
                    Player target = (Player) ent;
                    target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 1), true);
                }
            }
            
            if (proj.getShooter() instanceof Player)
            {
                Player shooter = (Player) proj.getShooter();
                
                if (proj instanceof LargeFireball)
                {
                    event.setCancelled(true);
                    event.setDamage(0);
                    return;
                }
                
                if (ent instanceof Player)
                {
                    Player damaged = (Player) ent;
                    
                    if (isSpectating.contains(damaged.getName()))
                    {
                        Vector velocity = proj.getVelocity();
                        double x = velocity.getX();
                        double y = velocity.getY();
                        double z = velocity.getZ();
                        
                        double locx = 0;
                        double locz = 0;
                        double locy = 0;
                        if (x > 0 && x >= 1)
                        {
                            locx = 1;
                        }
                        
                        if (x < 0 && x <= -1)
                        {
                            locx = -1;
                        }
                        
                        if(z > 0 && z >= 1)
                        {
                            locz = 1;
                        }
                        if(z < 0 && z <= -1)
                        {
                            locz = -1;
                        }

                        if(y > 0 && y >= .5)
                        {
                            locy = .5;
                        }
                        if(y < 0 && y <= -.5)
                        {
                            locy = -.5;
                        }
                        if(y > 0 && y >= 1)
                        {
                            locy = 1;
                        }
                        if(y < 0 && y <= -1)
                        {
                            locy = -1;
                        }
                        if(y > 0 && y >= 1.5)
                        {
                            locy = 1.5;
                        }
                        if(y < 0 && y <= -1.5)
                        {
                            locy = -1.5;
                        }
                        if(y > 0 && y >= 2)
                        {
                            locy = 2;
                        }
                        if(y < 0 && y <= -2)
                        {
                            locy = -2;
                        }
                        
                        Location projSpawnLoc = new Location(damaged.getWorld(), proj.getLocation().getX() + locx, proj.getLocation().getY() + locy, proj.getLocation().getZ() + locz);
                        
                        EntityType type = proj.getType();
                        if (type == EntityType.ARROW)
                        {
                            Arrow a = (Arrow) shooter.getWorld().spawn(projSpawnLoc, Arrow.class);
                            a.setShooter(shooter);
                            a.setVelocity(velocity);
                            a.setBounce(false);
                            event.setCancelled(true);
                            proj.remove();
                        }
                        
                        if (type == EntityType.EGG)
                        {
                            Egg a = (Egg) shooter.getWorld().spawn(projSpawnLoc, Egg.class);
                            a.setShooter(shooter);
                            a.setVelocity(velocity);
                            a.setBounce(false);
                            event.setCancelled(true);
                            proj.remove();
                        }
                        
                        if (type == EntityType.SNOWBALL)
                        {
                            Snowball a = (Snowball) shooter.getWorld().spawn(projSpawnLoc, Snowball.class);
                            a.setShooter(shooter);
                            a.setVelocity(velocity);
                            a.setBounce(false);
                            event.setCancelled(true);
                            proj.remove();
                        }
                        
                        if (type == EntityType.FIREBALL)
                        {
                            LargeFireball a = (LargeFireball) shooter.getWorld().spawn(projSpawnLoc, LargeFireball.class);
                            a.setShooter(shooter);
                            a.setVelocity(velocity);
                            a.setBounce(false);
                            event.setCancelled(true);
                            proj.remove();
                        }
                        
                        
                    }
                    else
                    {
                        event.setCancelled(true);
                        event.setDamage(0);
                        return;
                    }
                }
                
                
                boolean canHeadShot = false;
                boolean canHeadShotTall = false;
                if (ent instanceof Creeper || ent instanceof Zombie || ent instanceof Skeleton || ent instanceof PigZombie || ent instanceof Villager || ent instanceof Witch)
                {
                    canHeadShot = true;
                }
                
                if (ent instanceof IronGolem)
                {
                    LivingEntity ig = (LivingEntity) ent;
                    if (!(Game.hasWound.contains(ig.getUniqueId())))
                    {
                        canHeadShotTall = true;
                    
                        double random = Math.random();

                        String role = "null";
                        if (Game.playerClass.containsKey(shooter.getName()))
                        {
                            role = Game.playerClass.get(shooter.getName());
                        }

                        if (role.equalsIgnoreCase("Sharpshooter"))
                        {
                            if (random <= 0.10)
                            {

                                new woundTask(plugin, shooter, ig);
                            }
                        }
                    }
                    
                }
                
                if (ent instanceof Blaze)
                {
                    LivingEntity blaze = (LivingEntity) ent;
                    if (!(Game.hasWound.contains(blaze.getUniqueId())))
                    {
                        double random = Math.random();
                    
                        String role = "null";
                        if (Game.playerClass.containsKey(shooter.getName()))
                        {
                            role = Game.playerClass.get(shooter.getName());
                        }

                        if (role.equalsIgnoreCase("Sharpshooter"))
                        {
                            if (random <= 0.10)
                            {
                                new woundTask(plugin, shooter, blaze);
                            }
                        }
                    }
                    
                }
                
                ItemStack inHand = shooter.getItemInHand();
                if (inHand.hasItemMeta())
                {
                    ItemMeta meta = inHand.getItemMeta();
                    
                    if (meta.hasDisplayName())
                    {
                        double headshot_multi = 1.35;
                        double gunDmg = 0;
                        if (meta.hasLore())
                        {
                            List<String> lore = meta.getLore();
                            Iterator<String> it = lore.iterator();
                            
                            String gunName = "null";
                            while (it.hasNext())
                            {
                                String st = it.next();
                                if (Guns.GunCheck.contains(st))
                                {
                                    gunName = st;
                                }
                            }
                            
                            if (!(gunName.equalsIgnoreCase("null")))
                            {
                                // update hits
                                int hits = 0;
                                if (playerHits.containsKey(shooter.getName()))
                                {
                                    hits = playerHits.get(shooter.getName());
                                    playerHits.remove(shooter.getName());
                                }
                                hits++;
                                playerHits.put(shooter.getName(), hits);
                                
                                /*
                                 * Get bonus damage stuff
                                 */
                                String type = Guns.GunType.get(gunName);
                                String role = Game.playerClass.get(shooter.getName());
                                double bonusdmg = 0;
                                double headshotbonus = 0;
                                if (role.equalsIgnoreCase("medic"))
                                {
                                    if (type.equalsIgnoreCase("Shotgun"))
                                    {
                                        bonusdmg = Medic.getShotgunBonusAmount(shooter.getName());
                                    }
                                }
                                // do commando bonus damage check
                                double whichHigher = 0;
                                for (Player commandos : Bukkit.getOnlinePlayers())
                                {
                                    if (commandos.getWorld().equals(shooter.getWorld()))
                                    {
                                        double commandoBonus = 0;
                                        if (Commando.Stats_RangerInstructor.containsKey(commandos.getName()))
                                        {
                                            commandoBonus = Commando.Stats_RangerInstructor.get(commandos.getName());
                                        }
                                        if (commandoBonus >= whichHigher)
                                        {
                                            whichHigher = commandoBonus;
                                            String bestCommando = commandos.getName();
                                            bonusdmg = Commando.getRangerInstructorBonusAmount(bestCommando);
                                        }
                                    }
                                }
                                
                                if (role.equalsIgnoreCase("Sharpshooter"))
                                {
                                    //bonusdmg = Sharpshooter.getMarksmanTrainingBonusAmount(shooter.getName());
                                    headshotbonus = Sharpshooter.getCalledShotBonus(shooter.getName());
                                }
                                
                                /*
                                 * apply damage
                                 */
                                
                                gunDmg = Guns.GunDamage.get(gunName);
                                double totalDmg = (gunDmg * bonusdmg);
                                
                                if ((canHeadShot) && (proj.getLocation().getY() - ent.getLocation().getY() > 1.70))
                                {
                                    event.setDamage(totalDmg * (headshot_multi + headshotbonus));
                                    if (ent instanceof LivingEntity)
                                    {
                                        LivingEntity le = (LivingEntity) ent;
                                        Location loc = le.getLocation();
                                        loc.setY(loc.getY() + 1);
                                        le.getWorld().playEffect(loc, Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
                                    }
                                    //shooter.playSound(shooter.getLocation(), Sound.ANVIL_LAND, 1, 2);
                                }
                                else if ((canHeadShotTall) && (proj.getLocation().getY() - ent.getLocation().getY() > 2.65))
                                {
                                    event.setDamage(totalDmg * (headshot_multi + headshotbonus));
                                    if (ent instanceof LivingEntity)
                                    {
                                        LivingEntity le = (LivingEntity) ent;
                                        Location loc = le.getLocation();
                                        loc.setY(loc.getY() + 1);
                                        le.getWorld().playEffect(loc, Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
                                    }
                                    //shooter.playSound(shooter.getLocation(), Sound.ANVIL_LAND, 1, 2);
                                }
                                else
                                {
                                    event.setDamage(totalDmg);
                                }
                            }
                        }
                    }
                }
            }
            
        }
        
        /*
         * 
         * End Gun Projectiles
         * 
         */
        
        

        
        
    }
    
    private long getCurrentTimeLong()
    {
        Calendar calendar = new GregorianCalendar();

        long time = calendar.getTimeInMillis();
        
        return time;
    }
    
    public static HashMap<String, Long> shootDelay = new HashMap<>();
    public static HashSet<String> isReloading = new HashSet<>();
    
    @EventHandler
    public void onInteract(PlayerInteractEvent event)
    {
        Player player = event.getPlayer();
        
        if (isSpectating.contains(player.getName()))
        {
            event.setCancelled(true);
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 144000, 2), true);
            return;
        }
        
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
        {
            //event.setCancelled(true);
            
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
            {
                if (!SRKF.isLobby)
                {
                    if (event.getClickedBlock().getType() == Material.CHEST || event.getClickedBlock().getType() == Material.ENDER_CHEST 
                            || event.getClickedBlock().getType() == Material.TRAPPED_CHEST)
                    {
                        event.setCancelled(true);
                        return;
                    }
                    
                    if (event.getClickedBlock().getType() == Material.WOODEN_DOOR)
                    {
                        if (player.isOp())
                        {
                            return;
                        }
                        
                        event.setCancelled(true);
                        String message = ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "]" + ChatColor.RED + " You must weld doors to close them and unweld doors to open them!";
                        event.getPlayer().sendMessage(message); 
                        return;
                    }
                }
            }
            
            if (SRKF.isLobby)
            {
                Block b = event.getClickedBlock();
                
                if (player.getItemInHand().getType() == Material.WOOD_AXE)
                {
                    event.setCancelled(true);
                    return;
                }
                
                // open starting inventory in Lobby
               
                
                /*
                if (state instanceof Sign)
                {
                    Sign sign = (Sign) state;

                    String line1 = sign.getLine(0);
                    
                    if (line1.equalsIgnoreCase("Inventory"))
                    {
                        Inventory i = player.getServer().createInventory(null, 8, "Starting Inventory for " + player.getName());
                        
                    }
                }
                
                */
                
                // do join server Signs
                if (SignListener.signLocs.containsValue(b))
                {
                    event.setCancelled(true);
                    
                    int ID = 0;
                    
                    
                    BlockState state = b.getState();
                    if (state instanceof Sign)
                    {
                        Sign sign = (Sign) state;

                        String line1 = sign.getLine(0);
                        Pattern intsOnly = Pattern.compile("\\d+");
                        Matcher makeMatch = intsOnly.matcher(line1);

                        makeMatch.find();
                        String inputInt = makeMatch.group();

                        ID = Integer.parseInt(inputInt);
                    }

                   // Bukkit.broadcastMessage("ID is: " + ID);

                    boolean ready = SRKF.dbManager.checkServerReady(ID);

                    if (ready)
                    {
                        String serverName = SRKF.dbManager.getProxyServerName(ID);

                        String msgType = "joinmap";
                        String playerName = player.getName();
                        boolean received = false;

                        String serveraddress = SRKF.dbManager.checkMapSocketAddress(ID);
                        int serversocketport = SRKF.dbManager.checkMapSocketPort(ID);

                        String serverSocketPort = "" + serversocketport;

                        String mapID = ""+ID;

                        try
                        {
                            this.proxyIP = SRKF.config.getString("ProxyIP");
                            this.ProxySocketPort = SRKF.config.getInt("ProxySocketPort");

                            this.socket = new Socket(this.proxyIP, this.ProxySocketPort);
                            ObjectOutputStream oos = new ObjectOutputStream(this.socket.getOutputStream());

                            oos.writeObject(new String[]
                            {
                               msgType, playerName, serverName, serveraddress, serverSocketPort, mapID
                            });


                            ObjectInputStream ois = new ObjectInputStream(this.socket.getInputStream());
                            if (((String)ois.readObject()).equalsIgnoreCase("true"))
                            {
                                received = true;
                                SRKF.LOG.log(Level.INFO, "[SRKF] - Fired off Socket Object: " + msgType + " and received reply for" + this.proxyIP + " on port " + this.ProxySocketPort);
                            }

                            ois.close();
                            oos.close();
                            this.socket.close();


                        } catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                        if (received)
                        {
                            player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.GOLD + "Joining Map Shortly...");
                        }
                        else
                        {
                            player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "Something bad happened. Check stacktrace.");
                        }

                    }
                    else
                    {
                        String rank = "member";
                        if (rankEL.playernamerank.containsKey(player.getName().toLowerCase()))
                        {
                            rank = rankEL.playernamerank.get(player.getName().toLowerCase());
                        }
                        
                        if (rank.equalsIgnoreCase("champion") || rank.equalsIgnoreCase("mod"))
                        {
                            String serverName = SRKF.dbManager.getProxyServerName(ID);

                            String msgType = "spectatemap";
                            String playerName = player.getName();
                            boolean received = false;

                            String serveraddress = SRKF.dbManager.checkMapSocketAddress(ID);
                            int serversocketport = SRKF.dbManager.checkMapSocketPort(ID);

                            String serverSocketPort = "" + serversocketport;

                            String mapID = ""+ID;

                            try
                            {
                                this.proxyIP = SRKF.config.getString("ProxyIP");
                                this.ProxySocketPort = SRKF.config.getInt("ProxySocketPort");

                                this.socket = new Socket(this.proxyIP, this.ProxySocketPort);
                                ObjectOutputStream oos = new ObjectOutputStream(this.socket.getOutputStream());

                                oos.writeObject(new String[]
                                {
                                   msgType, playerName, serverName, serveraddress, serverSocketPort, mapID
                                });


                                ObjectInputStream ois = new ObjectInputStream(this.socket.getInputStream());
                                if (((String)ois.readObject()).equalsIgnoreCase("true"))
                                {
                                    received = true;
                                    SRKF.LOG.log(Level.INFO, "[SRKF] - Fired off Socket Object: " + msgType + " and received reply for" + this.proxyIP + " on port " + this.ProxySocketPort);
                                }

                                ois.close();
                                oos.close();
                                this.socket.close();


                            } catch (Exception e)
                            {
                                e.printStackTrace();
                            }

                            if (received)
                            {
                                player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.GOLD + "Spectating Map Shortly...");
                            }
                            else
                            {
                                player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "Something bad happened. Check stacktrace.");
                            }
                        }
                        else
                        {
                            player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "Server is not ready. Try again soon.");
                            return;
                        }
                        
                    }
                }
            }
            
            if (!(SRKF.isLobby))
            {
               // event.setCancelled(true);
                
                /*
                if (isDowned.contains(player.getName()))
                {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + 
                            ChatColor.RED + "You are " + ChatColor.BOLD + "DOWNED" + ChatColor.RESET + ChatColor.RED + " and cannot interact. Get immediate medical help!");
                    return;
                }
                */
                
                if (isReloading.contains(player.getName()))
                {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + 
                            ChatColor.RED + "You are reloading and cannot do anything else!");
                    return;
                }
                
                ItemStack inHand = player.getItemInHand();
                if (inHand.hasItemMeta())
                {
                    ItemMeta meta = inHand.getItemMeta();
                    if (meta.hasDisplayName())
                    {
                        String dName = meta.getDisplayName();
                        
                        if (dName.contains("Grenade"))
                        {
                            event.setCancelled(true);
                            int grenadeCount = inHand.getAmount();
                                
                            if (grenadeCount > 1)
                            {
                                inHand.setAmount(grenadeCount - 1);
                            }
                            else
                            {
                                player.getInventory().removeItem(inHand);
                            }

                            //Item grenade = player.getWorld().dropItemNaturally(player.getLocation(), new ItemStack(Material.FIREBALL));
                            //grenade.setVelocity(player.getLocation().getDirection().add(player.getVelocity().toBlockVector()));
                            //new throwGrenade(player, grenade);
                            Snowball s = (Snowball) player.launchProjectile(Snowball.class);
                            s.setShooter(player);
                            s.setVelocity(player.getLocation().getDirection().multiply(1.5F));
                            s.setMetadata("Grenade", new FixedMetadataValue(plugin, player.getName()));
                            return;
                        }
                        
                        if (dName.contains("Ammo"))
                        {
                            event.setCancelled(true);
                            return;
                        }
                        
                        if (dName.contains("Medical Kit"))
                        {
                            if (isDowned.contains(player.getName()))
                            {
                                return;
                            }
                            
                            // allow cancel if stuck
                            if (isBandaging.containsKey(player.getName()))
                            {
                                if (player.isSneaking())
                                {
                                    int ID = isBandaging.get(player.getName());
                                    boolean isRunning = Bukkit.getScheduler().isCurrentlyRunning(ID);
                                    if (isRunning)
                                    {
                                        Bukkit.getScheduler().cancelTask(ID);
                                    }
                                    player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "You cancel your healing.");
                                    isBandaging.remove(player.getName());
                                    return;
                                }
                                else
                                {
                                    return;
                                }
                            }
                            
                            // do med kit stuff
                            assert player != null;
                            Entity target = null;
                            Player target2 = null;

                            double targetDistanceSquared = 0;
                            final double radiusSquared = 1;
                            final Vector l = player.getEyeLocation().toVector(), n = player.getLocation().getDirection().normalize();
                            final double cos45 = Math.cos(Math.PI / 4);

                            for (final LivingEntity other : player.getWorld().getEntitiesByClass(LivingEntity.class))
                            {
                                if (other == player)
                                    continue;
                                if (target == null || targetDistanceSquared > other.getLocation().distanceSquared(player.getLocation()))
                                {
                                    final Vector t = other.getLocation().add(0, 1, 0).toVector().subtract(l);
                                    if (n.clone().crossProduct(t).lengthSquared() < radiusSquared && t.normalize().dot(n) >= cos45)
                                    {
                                        target = other;
                                        targetDistanceSquared = target.getLocation().distanceSquared(player.getLocation());
                                    }
                                }
                            }

                            if (target != null)
                            {
                                if (!(target instanceof Player))
                                {
                                    if (player.getHealth() >= player.getMaxHealth())
                                    {
                                        return;
                                    }
                                    
                                    int medCount = inHand.getAmount();
                                
                                    if (medCount > 1)
                                    {
                                        inHand.setAmount(medCount - 1);
                                    }
                                    else
                                    {
                                        player.getInventory().removeItem(inHand);
                                    }

                                    new ApplyAid(player, player);
                                    
                                    player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " +
                                            ChatColor.GREEN + "You begin to apply first aid to your wounds...");
                                    
                                    return;
                                }

                                target2 = (Player) target;
                                
                                if (target2.getLocation().distance(player.getLocation()) > 5)
                                {
                                    player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " +
                                            ChatColor.RED + "Your target is too far away to apply first aid.");
                                    return;
                                }
                                
                                if (target2.getHealth() >= target2.getMaxHealth())
                                {
                                    return;
                                }
                                
                                int medCount = inHand.getAmount();
                                
                                if (medCount > 1)
                                {
                                    inHand.setAmount(medCount - 1);
                                }
                                else
                                {
                                    player.getInventory().removeItem(inHand);
                                }

                                new ApplyAid(player, target2);
                                player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " +
                                        ChatColor.GREEN + "You begin to apply a first aid to " + ChatColor.GOLD + target2.getName() + "'s" + ChatColor.GREEN + " wounds...");
                            }
                            else
                            {
                                if (player.getHealth() >= player.getMaxHealth())
                                {
                                    return;
                                }
                                
                                int medCount = inHand.getAmount();
                                
                                if (medCount > 1)
                                {
                                    inHand.setAmount(medCount - 1);
                                }
                                else
                                {
                                    player.getInventory().removeItem(inHand);
                                }
                                
                                new ApplyAid(player, player);
                                player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " +
                                        ChatColor.GREEN + "You begin to apply first aid to your wounds...");
                            }
                            // end med kit stuff
                            return;
                        }
                        
                        
                        
                        
                        
                        
                        //String itemName = meta.getDisplayName();
                        if (meta.hasLore())
                        {
                            List<String> lore = meta.getLore();
                            Iterator<String> it = lore.iterator();
                            
                            String meleeName = "null";
                            String gunName = "null";
                            String demoName = "null";
                            while (it.hasNext())
                            {
                                String st = it.next();
                                if (Guns.GunCheck.contains(st))
                                {
                                    gunName = st;
                                }
                                if (Guns.MeleeCheck.contains(st))
                                {
                                    meleeName = st;
                                }
                                
                                if (Guns.DemoCheck.contains(st))
                                {
                                    demoName = st;
                                }
                            }
                            
                            if (!(demoName.equalsIgnoreCase("null")))
                            {
                                // do sneak reload
                                if (player.isSneaking())
                                {
                                    if (isReloading.contains(player.getName()))
                                    {
                                        return;
                                    }
                                    
                                    if (isBandaging.containsKey(player.getName()))
                                    {
                                        player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "Your hands are busy applying first aid!");
                                        return;
                                    }
                                    
                                    int reloadTime = 0;
                                    if (Guns.DemoReload.containsKey(demoName))
                                    {
                                        reloadTime = Guns.DemoReload.get(demoName);
                                    }
                                    
                                    String gunType = lore.get(0);
                                    String gunTypeStripped = ChatColor.stripColor(gunType);
                                    String ammoName = ChatColor.GOLD + gunTypeStripped + " Ammo Clip";

                                   // Bukkit.broadcastMessage("ammoName is: " + ammoName);
                                    boolean hasAmmo = false;
                                    for (ItemStack stack : player.getInventory().getContents())
                                    {
                                        if (stack != null)
                                        {
                                            if (stack.hasItemMeta())
                                            {
                                                if (stack.getItemMeta().hasLore())
                                                {
                                                    String disp = stack.getItemMeta().getDisplayName();
                                                   // Bukkit.broadcastMessage("disp is: " + disp);
                                                    if (disp.equalsIgnoreCase(ammoName))
                                                    {
                                                      //  Bukkit.broadcastMessage("hasAmmo = true");
                                                        hasAmmo = true;
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    if (hasAmmo)
                                    {
                                        new reloadDemo(player, demoName);
                                        player.getInventory().removeItem(inHand);
                                        isReloading.add(player.getName());
                                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, (reloadTime * 20), 3), true);
                                        player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.GOLD + "Reloading your " + demoName + ".");
                                        return;
                                    }
                                    else
                                    {
                                        player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "No more ammo clips!");
                                        return;
                                    }
                                }
                                
                                long delay = 1000;
                                
                                // re apply gun delay to prevent ridiculous iterator checks
                                
                                boolean canShoot = false;
                                
                                long currenttime = getCurrentTimeLong();
                                
                                if (shootDelay.containsKey(player.getName()))
                                {
                                    long oldtime = shootDelay.get(player.getName());
                                    //player.sendMessage("oldtime = " + oldtime);
                                    long totaltime = currenttime - oldtime;
                                    //player.sendMessage("totaltime = " + totaltime);
                                    if (totaltime > delay || totaltime < 0)
                                    {
                                        canShoot = true;
                                        shootDelay.remove(player.getName());
                                    }
                                    else
                                    {
                                        canShoot = false;
                                        return;
                                    }
                                }
                                else
                                {
                                    canShoot = true;
                                }
                                
                                // do rest
                                if (canShoot)
                                {
                                    if (isReloading.contains(player.getName()))
                                    {
                                        return;
                                    }

                                    int maxAmmo = 0;
                                    if (Guns.DemoAmmo.containsKey(demoName))
                                    {
                                        maxAmmo = Guns.DemoAmmo.get(demoName);
                                    }

                                    

                                    String type = "null";

                                    //Bukkit.broadcastMessage("demoName is: " + demoName);
                                    if (Guns.DemoType.containsKey(demoName))
                                    {
                                        type = Guns.DemoType.get(demoName);
                                    }

                                    //Bukkit.broadcastMessage("type is: " + type);
                                    int ID = 0;
                                    if (Game.playersMaps.containsKey(player.getName()))
                                    {
                                        ID = Game.playersMaps.get(player.getName());
                                    }

                                    String dname = meta.getDisplayName();

                                    int start = dname.indexOf("(");
                                    int end = dname.indexOf("/");
                                    String ammoCount = dname.substring(start + 1, end);

                                    int currentammo = Integer.parseInt(ammoCount);
                                    //player.sendMessage("current ammo is: " + currentammo);
                                    int ammo = currentammo - 1;
                                    if (ammo < 0)
                                    {
                                        int reloadTime = 0;
                                        if (Guns.DemoReload.containsKey(demoName))
                                        {
                                            reloadTime = Guns.DemoReload.get(demoName);
                                        }

                                        for (String name : Game.playersMaps.keySet())
                                        {
                                            int id = Game.playersMaps.get(name);
                                            if (id == ID)
                                            {
                                                Player inGame = Bukkit.getPlayer(name);
                                                inGame.playSound(player.getLocation(), Sound.CLICK, 4, 1);
                                            } 
                                        }

                                        Material ammoType = Guns.ammoTypes.get(type);

                                        if (!(Game.autoReload.contains(player.getName())))
                                        {
                                            String gunType = lore.get(0);
                                            String gunTypeStripped = ChatColor.stripColor(gunType);
                                            String ammoName = ChatColor.GOLD + gunTypeStripped + " Ammo Clip";

                                           // Bukkit.broadcastMessage("ammoName is: " + ammoName);
                                            boolean hasAmmo = false;
                                            for (ItemStack stack : player.getInventory().getContents())
                                            {
                                                if (stack != null)
                                                {
                                                    if (stack.hasItemMeta())
                                                    {
                                                        if (stack.getItemMeta().hasLore())
                                                        {
                                                            String disp = stack.getItemMeta().getDisplayName();
                                                           // Bukkit.broadcastMessage("disp is: " + disp);
                                                            if (disp.equalsIgnoreCase(ammoName))
                                                            {
                                                              //  Bukkit.broadcastMessage("hasAmmo = true");
                                                                hasAmmo = true;
                                                            }
                                                        }
                                                    }
                                                }
                                            }

                                            if (hasAmmo)
                                            {
                                                new reloadDemo(player, demoName);
                                                player.getInventory().removeItem(inHand);
                                                isReloading.add(player.getName());
                                                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, (reloadTime * 20), 3));
                                                player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.GOLD + "Reloading your " + demoName + ".");
                                                return;
                                            }
                                            else
                                            {
                                                player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "No more ammo clips!");
                                                return;
                                            }

                                        }



                                        return;
                                    }
                                    meta.setDisplayName(ChatColor.LIGHT_PURPLE + demoName + " " + ChatColor.GRAY + "(" + ammo + "/" + maxAmmo + ")");
                                    inHand.setItemMeta(meta);

                                    // do shot update
                                    int shotCount = 0;
                                    if (playerShots.containsKey(player.getName()))
                                    {
                                        shotCount = playerShots.get(player.getName());
                                        playerShots.remove(player.getName());
                                    }
                                    shotCount++;
                                    playerShots.put(player.getName(), shotCount);

                                    if (type.equalsIgnoreCase("Artillery"))
                                    {
                                        shootDelay.put(player.getName(), delay);
                                        
                                        LargeFireball s = (LargeFireball) player.launchProjectile(LargeFireball.class);
                                        s.setShooter(player);
                                        s.setVelocity(player.getLocation().getDirection().multiply(3));

                                        if (demoName.equalsIgnoreCase("AT4 Bazooka"))
                                        {
                                            s.setMetadata("AT4 Bazooka", new FixedMetadataValue(plugin, player.getName()));
                                        }

                                        if (demoName.equalsIgnoreCase("L.A.W."))
                                        {
                                            s.setMetadata("L.A.W.", new FixedMetadataValue(plugin, player.getName()));
                                        }

                                        for (String name : Game.playersMaps.keySet())
                                        {
                                            int id = Game.playersMaps.get(name);
                                            if (id == ID)
                                            {
                                                Player inGame = Bukkit.getPlayer(name);
                                                //inGame.playSound(player.getLocation(), Sound.BLAZE_HIT, 3, 2);
                                                inGame.playSound(player.getLocation(), Sound.EXPLODE, 3, 2);
                                                inGame.playSound(player.getLocation(), Sound.FIZZ, 3, 2);
                                            } 
                                        }
                                    }
                                } // end demo 

                                
                            }
                            
                            
                            
                            if (!(gunName.equalsIgnoreCase("null")))
                            {
                                if (isReloading.contains(player.getName()))
                                {
                                    return;
                                }
                                // grab all gun info
                                long delay = 0;
                                if (Guns.GunDelay.containsKey(gunName))
                                {
                                    delay = Guns.GunDelay.get(gunName);
                                }
                                int maxAmmo = 0;
                                if (Guns.GunAmmo.containsKey(gunName))
                                {
                                    maxAmmo = Guns.GunAmmo.get(gunName);
                                }
                                String type = "null";
                                
                                if (Guns.GunType.containsKey(gunName))
                                {
                                    type = Guns.GunType.get(gunName);
                                }
                                
                                // do sneak reload
                                if (player.isSneaking())
                                {
                                    if (isReloading.contains(player.getName()))
                                    {
                                        return;
                                    }
                                    
                                    if (isBandaging.containsKey(player.getName()))
                                    {
                                        player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "Your hands are busy applying first aid!");
                                        return;
                                    }
                                    
                                    int reloadTime = 0;
                                    if (Guns.GunReload.containsKey(gunName))
                                    {
                                        reloadTime = Guns.GunReload.get(gunName);
                                    }
                                    
                                    
                                    
                                    String gunType = lore.get(0);
                                    String gunTypeStripped = ChatColor.stripColor(gunType);
                                    String ammoName = ChatColor.GOLD + gunTypeStripped + " Ammo Clip";
                                    //Bukkit.broadcastMessage("ammoName : " + ammoName);
                                    
                                    boolean hasAmmo = false;
                                    for (ItemStack stack : player.getInventory().getContents())
                                    {
                                        if (stack != null)
                                        {
                                            if (stack.hasItemMeta())
                                            {
                                                if (stack.getItemMeta().hasLore())
                                                {
                                                    String disp = stack.getItemMeta().getDisplayName();
                                                    //Bukkit.broadcastMessage("disp is: " + disp);
                                                    if (disp.equalsIgnoreCase(ammoName))
                                                    {
                                                        //Bukkit.broadcastMessage("hasAmmo = true");
                                                        hasAmmo = true;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    
                                    if (hasAmmo)
                                    {
                                        new reloadGun(player, gunName);
                                        player.getInventory().removeItem(inHand);
                                        isReloading.add(player.getName());
                                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, (reloadTime * 20), 3), true);
                                        player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.GOLD + "Reloading your " + gunName + ".");
                                        return;
                                    }
                                    else
                                    {
                                        player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "No more ammo clips!");
                                        return;
                                    }
                                }
                                
                                // re apply gun delay to prevent ridiculous iterator checks
                                
                                boolean canShoot = false;
                                
                                long currenttime = getCurrentTimeLong();
                                
                                if (shootDelay.containsKey(player.getName()))
                                {
                                    long oldtime = shootDelay.get(player.getName());
                                    //player.sendMessage("oldtime = " + oldtime);
                                    long totaltime = currenttime - oldtime;
                                    //player.sendMessage("totaltime = " + totaltime);
                                    if (totaltime > delay || totaltime < 0)
                                    {
                                        canShoot = true;
                                        shootDelay.remove(player.getName());
                                    }
                                    else
                                    {
                                        canShoot = false;
                                        return;
                                    }
                                }
                                else
                                {
                                    canShoot = true;
                                }
                                
                                // do rest
                                
                                if (canShoot)
                                {
                                    int ID = 0;
                                    if (Game.playersMaps.containsKey(player.getName()))
                                    {
                                        ID = Game.playersMaps.get(player.getName());
                                    }

                                    //do ammo
                                    
                                    
                                    String dname = meta.getDisplayName();

                                    int start = dname.indexOf("(");
                                    int end = dname.indexOf("/");
                                    String ammoCount = dname.substring(start + 1, end);

                                    int currentammo = Integer.parseInt(ammoCount);
                                    //player.sendMessage("current ammo is: " + currentammo);
                                    int ammo = currentammo - 1;
                                    if (ammo < 0)
                                    {
                                        int reloadTime = 0;
                                        if (Guns.GunReload.containsKey(gunName))
                                        {
                                            reloadTime = Guns.GunReload.get(gunName);
                                        }
                                        
                                        for (String name : Game.playersMaps.keySet())
                                        {
                                            int id = Game.playersMaps.get(name);
                                            if (id == ID)
                                            {
                                                Player inGame = Bukkit.getPlayer(name);
                                                inGame.playSound(player.getLocation(), Sound.CLICK, 4, 1);
                                            } 
                                        }
                                        
                                        Material ammoType = Guns.ammoTypes.get(type);
                                    
                                        if (!(Game.autoReload.contains(player.getName())))
                                        {
                                            String gunType = lore.get(0);
                                            String gunTypeStripped = ChatColor.stripColor(gunType);
                                            String ammoName = ChatColor.GOLD + gunTypeStripped + " Ammo Clip";

                                            boolean hasAmmo = false;
                                            for (ItemStack stack : player.getInventory().getContents())
                                            {
                                                if (stack != null)
                                                {
                                                    if (stack.hasItemMeta())
                                                    {
                                                        if (stack.getItemMeta().hasLore())
                                                        {
                                                            String disp = stack.getItemMeta().getDisplayName();

                                                            if (disp.equalsIgnoreCase(ammoName))
                                                            {
                                                                hasAmmo = true;
                                                            }
                                                        }
                                                    }
                                                }
                                            }

                                            if (hasAmmo)
                                            {
                                                new reloadGun(player, gunName);
                                                player.getInventory().removeItem(inHand);
                                                isReloading.add(player.getName());
                                                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, (reloadTime * 20), 3), true);
                                                player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.GOLD + "Reloading your " + gunName + ".");
                                                return;
                                            }
                                            else
                                            {
                                                player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "No more ammo clips!");
                                                return;
                                            }
                                        }
                                        
                                        
                                        
                                        
                                        return;
                                    }
                                    
                                    meta.setDisplayName(ChatColor.LIGHT_PURPLE + gunName + " " + ChatColor.GRAY + "(" + ammo + "/" + maxAmmo + ")");
                                    inHand.setItemMeta(meta);
                                    
                                    // do shot update
                                    int shotCount = 0;
                                    if (playerShots.containsKey(player.getName()))
                                    {
                                        shotCount = playerShots.get(player.getName());
                                        playerShots.remove(player.getName());
                                    }
                                    shotCount++;
                                    playerShots.put(player.getName(), shotCount);
                                    
                                    if (type.equalsIgnoreCase("pistol"))
                                    {
                                        Snowball s = (Snowball) player.launchProjectile(Snowball.class);
                                        s.setShooter(player);
                                        s.setVelocity(player.getLocation().getDirection().multiply(5));
                                        shootDelay.put(player.getName(), currenttime);
                                        for (String name : Game.playersMaps.keySet())
                                        {
                                            int id = Game.playersMaps.get(name);
                                            if (id == ID)
                                            {
                                                Player inGame = Bukkit.getPlayer(name);
                                                //inGame.playSound(player.getLocation(), Sound.BLAZE_HIT, 3, 2);
                                                inGame.playSound(player.getLocation(), Sound.IRONGOLEM_HIT, 3, 2);
                                                inGame.playSound(player.getLocation(), Sound.ZOMBIE_METAL, 3, 2);
                                            } 
                                        }
                                    }
                                    
                                    if (type.equalsIgnoreCase("SMG"))
                                    {
                                        Snowball s = (Snowball) player.launchProjectile(Snowball.class);
                                        s.setShooter(player);
                                        s.setVelocity(player.getLocation().getDirection().multiply(6));
                                        shootDelay.put(player.getName(), currenttime);
                                        for (String name : Game.playersMaps.keySet())
                                        {
                                            int id = Game.playersMaps.get(name);
                                            if (id == ID)
                                            {
                                                Player inGame = Bukkit.getPlayer(name);
                                                //inGame.playSound(player.getLocation(), Sound.BLAZE_HIT, 3, 2);
                                                inGame.playSound(player.getLocation(), Sound.ITEM_BREAK, 3, 2);
                                                inGame.playSound(player.getLocation(), Sound.NOTE_SNARE_DRUM, 3, 0);
                                            } 
                                        }
                                    }
                                    
                                    if (type.equalsIgnoreCase("shotgun"))
                                    {
                                        /*
                                        Location loc1 = player.getEyeLocation().toVector().add(player.getLocation().getDirection().multiply(1)).toLocation(player.getWorld(), player.getLocation().getYaw(), player.getLocation().getPitch());
                                        Location loc2 = player.getEyeLocation().toVector().add(player.getLocation().getDirection().multiply(2)).toLocation(player.getWorld(), player.getLocation().getYaw(), player.getLocation().getPitch());
                                        Location loc3 = player.getEyeLocation().toVector().add(player.getLocation().getDirection().multiply(3)).toLocation(player.getWorld(), player.getLocation().getYaw(), player.getLocation().getPitch());
                                        Location loc4 = player.getEyeLocation().toVector().add(player.getLocation().getDirection().multiply(2)).toLocation(player.getWorld(), player.getLocation().getYaw(), player.getLocation().getPitch());
                                        Location loc5 = player.getEyeLocation().toVector().add(player.getLocation().getDirection().multiply(1)).toLocation(player.getWorld(), player.getLocation().getYaw(), player.getLocation().getPitch());

                                        Egg egg1 = (Egg)player.launchProjectile(Egg.class);
                                        Vector v1 = loc1.getDirection().normalize();
                                        egg1.setVelocity(v1.multiply(v1.length() * 2));

                                        Egg egg2 = (Egg)player.launchProjectile(Egg.class);
                                        Vector v2 = loc2.getDirection().normalize();
                                        egg1.setVelocity(v2.multiply(v2.length() * 2));

                                        Egg egg3 = (Egg)player.launchProjectile(Egg.class);
                                        Vector v3 = loc3.getDirection().normalize();
                                        egg1.setVelocity(v3.multiply(v3.length() * 2));

                                        Egg egg4 = (Egg)player.launchProjectile(Egg.class);
                                        Vector v4 = loc4.getDirection().normalize();
                                        egg1.setVelocity(v4.multiply(v4.length() * 2));

                                        Egg egg5 = (Egg)player.launchProjectile(Egg.class);
                                        Vector v5 = loc5.getDirection().normalize();
                                        egg1.setVelocity(v5.multiply(v5.length() * 2));
                                        */
                                        
                                        Arrow a = player.getWorld().spawnArrow(player.getEyeLocation(), player.getLocation().getDirection(), (20/10.0F), (125/10.0F));
                                        Arrow a2 = player.getWorld().spawnArrow(player.getEyeLocation(), player.getLocation().getDirection(), (20/10.0F), (130/10.0F));
                                        Arrow a3 = player.getWorld().spawnArrow(player.getEyeLocation(), player.getLocation().getDirection(), (20/10.0F), (135/10.0F));
                                        Arrow a4 = player.getWorld().spawnArrow(player.getEyeLocation(), player.getLocation().getDirection(), (20/10.0F), (140/10.0F));
                                        Arrow a5 = player.getWorld().spawnArrow(player.getEyeLocation(), player.getLocation().getDirection(), (20/10.0F), (145/10.0F));
                                        
                                        a.setShooter(player);
                                        a2.setShooter(player);
                                        a3.setShooter(player);
                                        a4.setShooter(player);
                                        a5.setShooter(player);
                                        
                                        a.setVelocity(a.getVelocity());
                                        a2.setVelocity(a2.getVelocity());
                                        a3.setVelocity(a3.getVelocity());
                                        a4.setVelocity(a4.getVelocity());
                                        a5.setVelocity(a5.getVelocity());
                                        
                                        
                                        /*
                                        egg1.setShooter(player);
                                        egg2.setShooter(player);
                                        egg3.setShooter(player);
                                        egg4.setShooter(player);
                                        egg5.setShooter(player);
                                        */

                                        shootDelay.put(player.getName(), currenttime);

                                        //play sounds
                                        for (String name : Game.playersMaps.keySet())
                                        {
                                            int id = Game.playersMaps.get(name);
                                            if (id == ID)
                                            {
                                                Player inGame = Bukkit.getPlayer(name);
                                                inGame.playSound(player.getLocation(), Sound.EXPLODE, 3, 2);
                                                inGame.playSound(player.getLocation(), Sound.GHAST_FIREBALL, 3, 1);
                                                inGame.playSound(player.getLocation(), Sound.ZOMBIE_WOOD, 3,1);
                                            } 
                                        }
                                    }
                                    
                                    if (type.equalsIgnoreCase("rifle"))
                                    {
                                        Snowball a = (Snowball) player.launchProjectile(Snowball.class);
                                        a.setShooter(player);
                                        a.setVelocity(player.getLocation().getDirection().multiply(5));
                                        shootDelay.put(player.getName(), currenttime);
                                        for (String name : Game.playersMaps.keySet())
                                        {
                                            int id = Game.playersMaps.get(name);
                                            if (id == ID)
                                            {
                                                Player inGame = Bukkit.getPlayer(name);
                                                //inGame.playSound(player.getLocation(), Sound.PISTON_EXTEND, 3, 2);
                                                inGame.playSound(player.getLocation(), Sound.IRONGOLEM_HIT, 3, 2);
                                                inGame.playSound(player.getLocation(), Sound.SKELETON_HURT, 3, 2);
                                                inGame.playSound(player.getLocation(), Sound.ZOMBIE_WOOD, 3, 2);
                                            } 
                                        }
                                    }
                                    
                                    if (type.equalsIgnoreCase("bow"))
                                    {
                                        event.setCancelled(true);
                                        Egg a = (Egg) player.launchProjectile(Egg.class);
                                        a.setShooter(player);
                                        a.setVelocity(player.getLocation().getDirection().multiply(5));
                                        shootDelay.put(player.getName(), currenttime);
                                        for (String name : Game.playersMaps.keySet())
                                        {
                                            int id = Game.playersMaps.get(name);
                                            if (id == ID)
                                            {
                                                Player inGame = Bukkit.getPlayer(name);
                                                inGame.playSound(player.getLocation(), Sound.SHOOT_ARROW, 3, 2);
                                            } 
                                        }
                                    }
                                }
                                
                            }
                        }
                    }
                }
            }
        }
        
    }
    
    @EventHandler(priority=EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent event)
    {
        if (!(event.getPlayer().isOp()))
        {
            event.setCancelled(true);
        }
        
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event)
    {
        Player player = event.getPlayer();
        Block b = event.getBlock();
        
        if (SignListener.signLocs.containsValue(b))
        {
            event.setCancelled(true);
        }
        else
        {
            if (!(player.isOp()))
            {
                event.setCancelled(true);
            }
        }
    }
    
    
    private class ApplyAid implements Runnable
    {
        Player player;
        Player target;
        int taskID;
        int count;
        
        Location pLoc;
        Location tLoc;
        int countCheck = 4;
        public ApplyAid(Player player, Player target)
        {
            this.player = player;
            this.target = target;
            this.pLoc = player.getLocation();
            this.tLoc = target.getLocation();
            this.taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, 0, 20);
            this.count = 0;
            isBandaging.put(player.getName(), taskID);
        }
        
        @Override
        public void run()
        {
            count++;
            
            String role = "null";
            
            if (!(player.hasPotionEffect(PotionEffectType.SLOW)))
            {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 4), true);
            }
            
            if (Game.playerClass.containsKey(player.getName()))
            {
                role = Game.playerClass.get(player.getName());
            }
                
            if (!(target.isOnline()))
            {
                Bukkit.getScheduler().cancelTask(taskID);
                if (isBandaging.containsKey(player.getName()))
                {
                    isBandaging.remove(player.getName());
                }
                player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "Your Med Kit failed! Target moved too far away. (Offline)");
            }
            
            if (target.isDead())
            {
                Bukkit.getScheduler().cancelTask(taskID);
                if (isBandaging.containsKey(player.getName()))
                {
                    isBandaging.remove(player.getName());
                }
                player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "Your Med Kit failed! Target has died.");
            }
            
            if (!(role.equalsIgnoreCase("Medic")))
            {
                if (pLoc.distance(tLoc) > 5)
                {
                    Bukkit.getScheduler().cancelTask(taskID);
                    if (isBandaging.containsKey(player.getName()))
                    {
                        isBandaging.remove(player.getName());
                    }
                    player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "Your Med Kit failed! Target moved too far away.");
                }
            }
            else
            {
                countCheck = 2;
                
                if (target != player)
                {
                    if (pLoc.distance(tLoc) > 5)
                    {
                        Bukkit.getScheduler().cancelTask(taskID);
                        if (isBandaging.containsKey(player.getName()))
                        {
                            isBandaging.remove(player.getName());
                        }
                        player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "Your Med Kit failed! Target moved too far away.");
                    }
                }
            }
            
            
            
            if (count >= countCheck)
            {
                Bukkit.getScheduler().cancelTask(taskID);
                
                if (isBandaging.containsKey(player.getName()))
                {
                    isBandaging.remove(player.getName());
                }
                
                if (role.equalsIgnoreCase("Medic"))
                {
                    int level = 0;
                    if (Medic.Stats_MedKit.containsKey(player.getName()))
                    {
                        level = Medic.Stats_MedKit.get(player.getName());
                    }
                    
                    if (level >= 3)
                    {
                        if (target.hasPotionEffect(PotionEffectType.POISON))
                        {
                            target.removePotionEffect(PotionEffectType.POISON);
                            target.addPotionEffect(new PotionEffect(PotionEffectType.POISON,0,0));
                        }

                        if (target.hasPotionEffect(PotionEffectType.WITHER))
                        {
                            target.removePotionEffect(PotionEffectType.WITHER);
                            target.addPotionEffect(new PotionEffect(PotionEffectType.WITHER,0,0));
                        }

                        if (target.getFireTicks() > 0)
                        {
                            target.setFireTicks(0);
                        }
                    }
                }
               
                
                double targetHP = target.getHealth();
                if (isDowned.contains(target.getName()))
                {
                    isDowned.remove(target.getName());
                    
                    if (target != player)
                    {
                        target.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.GOLD + player.getName() + 
                                ChatColor.GREEN + " brought you back with some Triage.");
                        player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + 
                                ChatColor.GREEN + "You applied Triage to " + ChatColor.GOLD + target.getName() + "'s " + ChatColor.GREEN + " wounds.");
                    }
                    
                    double triageAmount = Medic.baseTriageAmount;
                    double bonusAmount = 0;
                    if (role.equalsIgnoreCase("Medic"))
                    {
                        bonusAmount = Medic.getTriageBonusAmount(player.getName());
                    }
                    
                    double totalAmount = triageAmount + bonusAmount;
                    
                    if (totalAmount > target.getMaxHealth())
                    {
                        target.setHealth(target.getMaxHealth());
                        double difference = totalAmount - target.getHealth();
                        
                        int boostValue = 0;
                        if (difference > 4)
                        {
                            boostValue = 1;
                        }
                        if (difference > 8)
                        {
                            boostValue = 2;
                        }
                        target.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 400, boostValue), true);
                        target.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.GOLD + player.getName() + "'s " + 
                                ChatColor.GREEN + "expert Triage skills gave you some bonus health!");
                    }
                    else
                    {
                        target.setHealth(totalAmount);
                    }
                    
                    
                }
                else
                {
                    double baseAmount = Medic.baseMedKit;
                    double bonusAmount = 0;
                    if (role.equalsIgnoreCase("Medic"))
                    {
                        bonusAmount = Medic.getHealBonusAmount(player.getName());
                    }

                    double totalAmount = baseAmount + bonusAmount;

                    if ((targetHP + totalAmount) > target.getMaxHealth())
                    {
                        target.setHealth(target.getMaxHealth());
                    }
                    else
                    {
                        target.setHealth(targetHP + totalAmount);
                    }

                    //Bukkit.broadcastMessage("targetHP: " + targetHP + " | totalAmount: " + totalAmount);


                    if (target != player)
                    {
                        target.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.GOLD + player.getName() + 
                                ChatColor.GREEN + " bandages your wounds.");
                        player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + 
                                ChatColor.GREEN + "You bandage " + ChatColor.GOLD + target.getName() + "'s " + ChatColor.GREEN + " wounds.");
                    }
                    else
                    {
                        player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.GREEN + "You bandage your wounds.");
                        
                        
                        if (role.equalsIgnoreCase("Medic"))
                        {
                            int absorb = Medic.getCombatArmorBonusAmount(player.getName());
                            player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 600, absorb), true);
                        }
                        
                    }
                }
                
                
                
            }
        }
    }
    
    private class reloadDemo implements Runnable
    {
        Player player;
        int taskID;
        String demoName;
        int reloadTime;
        String demoType;
        public reloadDemo (Player player, String demoName)
        {
            this.demoName = demoName;
            this.demoType = Guns.DemoType.get(demoName);
            this.reloadTime = (Guns.DemoReload.get(demoName) * 20);
            this.player = player;
            this.taskID = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, this, reloadTime);
        }

        @Override
        public void run() 
        {
            if (!(player.isOnline()))
            {
                Bukkit.getScheduler().cancelTask(taskID);
            }
            
            ItemStack gun = Guns.DemoList.get(demoName).clone();
            
            EnhancementListener.getRangedWeaponEnhancements(gun, player);
            /*
            if (!(player.getItemInHand().equals(gun)))
            {
                //player.sendMessage("Wrong gun. This one is: " + gun.toString());
                player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.GOLD + "Reload failed. You changed guns too early!");
                if (isReloading.contains(player.getName()))
                {
                    isReloading.remove(player.getName());
                }
                Bukkit.getScheduler().cancelTask(taskID);
            }
            */
            
            if (!(isDowned.contains(player.getName())))
            {
                if (player.hasPotionEffect(PotionEffectType.SLOW))
                {
                    player.removePotionEffect(PotionEffectType.SLOW);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 0, 0));
                }
            }
            
            
            if (gun.hasItemMeta())
            {

                if (gun.getItemMeta().hasLore())
                {
                    int clipCount = 0;
                    
                    player.getInventory().addItem(gun);
                    player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.GOLD + "Reload complete!");

                   // Material ammoType = Guns.ammoTypes.get(gunType);
                   // ItemStack ammoStack = Guns.miscItemStacks.get(gunType).clone();
                    
                    String demoTypeStripped = ChatColor.stripColor(demoType);
                    String ammoName = ChatColor.GOLD + demoTypeStripped + " Ammo Clip";

                    for (ItemStack stack : player.getInventory().getContents())
                    {
                        if (stack != null)
                        {
                            if (stack.hasItemMeta())
                            {
                                if (stack.getItemMeta().hasLore())
                                {
                                    String disp = stack.getItemMeta().getDisplayName();

                                    if (disp.equalsIgnoreCase(ammoName))
                                    {
                                        clipCount = stack.getAmount();

                                        if (clipCount > 1)
                                        {
                                            stack.setAmount(clipCount - 1);
                                        }
                                        else
                                        {
                                            player.getInventory().removeItem(stack);
                                        }

                                        break;
                                    }
                                }
                            }
                        }
                    }

                }

                Bukkit.getScheduler().cancelTask(taskID);
                if (isReloading.contains(player.getName()))
                {
                    isReloading.remove(player.getName());
                }
            }
        }
        
    }
    
    private class reloadGun implements Runnable
    {
        Player player;
        int taskID;
        String gunName;
        int reloadTime;
        String gunType;
        public reloadGun (Player player, String gunName)
        {
            this.gunName = gunName;
            this.gunType = Guns.GunType.get(gunName);
            this.player = player;
            
            String role = "null";
            if (Game.playerClass.containsKey(player.getName()))
            {
                role = Game.playerClass.get(player.getName());
            }
            
            if (role.equalsIgnoreCase("Sharpshooter"))
            {
                double bonus = Sharpshooter.getMarksmanTrainingBonusAmount(player.getName());
                this.reloadTime = (int) ((Guns.GunReload.get(gunName) * 20) * bonus);
                this.taskID = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, this, reloadTime);
            }
            else
            {
                this.reloadTime = (Guns.GunReload.get(gunName) * 20);
                this.taskID = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, this, reloadTime);
            }
        }

        @Override
        public void run() 
        {
            if (!(player.isOnline()))
            {
                Bukkit.getScheduler().cancelTask(taskID);
            }
            
            ItemStack gun = Guns.GunList.get(gunName).clone();
            
            EnhancementListener.getRangedWeaponEnhancements(gun, player);
            
            if (!(isDowned.contains(player.getName())))
            {
                if (player.hasPotionEffect(PotionEffectType.SLOW))
                {
                    player.removePotionEffect(PotionEffectType.SLOW);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 0, 0));
                }
            }
            
            /*
            if (!(player.getItemInHand().equals(gun)))
            {
                //player.sendMessage("Wrong gun. This one is: " + gun.toString());
                player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.GOLD + "Reload failed. You changed guns too early!");
                if (isReloading.contains(player.getName()))
                {
                    isReloading.remove(player.getName());
                }
                Bukkit.getScheduler().cancelTask(taskID);
            }
            */
            
            if (gun.hasItemMeta())
            {
                if (gun.getItemMeta().hasLore())
                {
                    int clipCount = 0;
                    
                    player.getInventory().addItem(gun);
                    player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.GOLD + "Reload complete!");

                   // Material ammoType = Guns.ammoTypes.get(gunType);
                   // ItemStack ammoStack = Guns.miscItemStacks.get(gunType).clone();
                    
                    String gunTypeStripped = ChatColor.stripColor(gunType);
                    String ammoName = ChatColor.GOLD + gunTypeStripped + " Ammo Clip";

                    for (ItemStack stack : player.getInventory().getContents())
                    {
                        if (stack != null)
                        {
                            if (stack.hasItemMeta())
                            {
                                if (stack.getItemMeta().hasLore())
                                {
                                    String disp = stack.getItemMeta().getDisplayName();

                                    if (disp.equalsIgnoreCase(ammoName))
                                    {
                                        clipCount = stack.getAmount();

                                        if (clipCount > 1)
                                        {
                                            stack.setAmount(clipCount - 1);
                                        }
                                        else
                                        {
                                            player.getInventory().removeItem(stack);
                                        }

                                        break;
                                    }
                                }
                            }
                        }
                    }

                }
                

                Bukkit.getScheduler().cancelTask(taskID);
                if (isReloading.contains(player.getName()))
                {
                    isReloading.remove(player.getName());
                }
            }
        }
        
    }
    
    
    private class downedEvent implements Runnable
    {
        Player player;
        int x;
        int taskID;
        
        public downedEvent (Player player)
        {
            this.player = player;
            this.x = 0;
            this.taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, 0, 20);
            launchFirework(player);
        }

        @Override
        public void run() 
        {
            x++;
            
            if (isBandaging.containsKey(player.getName()))
            {
                int bandageID = isBandaging.get(player.getName());
                Bukkit.getScheduler().cancelTask(bandageID);
                player.sendMessage(ChatColor.DARK_GRAY + "[SR"+ ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "You are DOWNED and cannot complete your first aid.");
                isBandaging.remove(player.getName());
            }
            
            if (!(isDowned.contains(player.getName())))
            {
                Bukkit.getScheduler().cancelTask(taskID);
                return;
            }
            
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 4), true);
           // player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 40, 4), true);
            
            if (x % 3 == 0)
            {
                launchFirework(player);
            }

            if (x >= Game.downedTime)
            {
                Bukkit.getScheduler().cancelTask(taskID);
                
                int round = 0;
                int kills = 0;
                int exp = 0;
                double coins = 0;

                if (playerKills.containsKey(player.getName()))
                {
                    kills = playerKills.get(player.getName());
                }

                int ID = 0;
                if (Game.playersMaps.containsKey(player.getName()))
                {
                    ID = Game.playersMaps.get(player.getName());
                }

                if (Game.mapRound.containsKey(ID))
                {
                    round = Game.mapRound.get(ID);
                }
                
                if (EXP.playerEXP.containsKey(player.getName()))
                {
                    exp = EXP.playerEXP.get(player.getName());
                }
                
                if (Game.playerCoins.containsKey(player.getName()))
                {
                    coins = Game.playerCoins.get(player.getName());
                }
                
                double rankmultiplier = 1;
                String rankname = rankEL.playernamerank.get(player.getName().toLowerCase());

                if(rankname.toLowerCase().contains("vip"))
                {
                    rankmultiplier = 1.25;
                }
                if(rankname.toLowerCase().contains("elite"))
                {
                    rankmultiplier = 1.5;
                }
                if(rankname.toLowerCase().contains("hero"))
                {
                    rankmultiplier = 1.75;
                }
                if(rankname.toLowerCase().contains("champion"))
                {
                    rankmultiplier = 2;
                }
                
                coins = coins * rankmultiplier;
                
                player.sendMessage(ChatColor.DARK_GRAY + "[SR"+ ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "You died on round " + round + " after killing " + kills + " mobs!");
                player.sendMessage(ChatColor.DARK_GRAY + "[SR"+ ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "You gained " + exp + " experience that game!");
                player.sendMessage(ChatColor.DARK_GRAY + "[SR"+ ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "You earned " + coins + " coins that game!");
                SRKF.LOG.info("[SRKF] - Player " + player.getName() + " teleported to Lobby due to dying.");
                player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "Teleporting you back to lobby due to death.");
                // do teleport out

                toLobby(player);
            }
        }
        
    }
    
    
    private void cleanUpHashes(String pName)
    {
        if (Game.autoReload.contains(pName))
        {
            Game.autoReload.remove(pName);
        }
        
        
        if (incomingPlayer.containsKey(pName))
        {
            incomingPlayer.remove(pName);
        }
        
        if (Game.playerMoney.containsKey(pName))
        {
            Game.playerMoney.remove(pName);
        }
        
        if (Game.playerClass.containsKey(pName))
        {
            Game.playerClass.remove(pName);
        }
        
        if (Game.playersMaps.containsKey(pName))
        {
            Game.playersMaps.remove(pName);
        }
        
        if (isDowned.contains(pName))
        {
            isDowned.remove(pName);
        }
        
        if (isReloading.contains(pName))
        {
            isReloading.remove(pName);
        }
        
        if (isBandaging.containsKey(pName))
        {
            isBandaging.remove(pName);
        }
        
        if (playerKills.containsKey(pName))
        {
            playerKills.remove(pName);
        }
        
        if (playerHits.containsKey(pName))
        {
            playerHits.remove(pName);
        }
        
        if (playerShots.containsKey(pName))
        {
            playerShots.remove(pName);
        }
        
        if (EXP.playerEXP.containsKey(pName))
        {
            EXP.playerEXP.remove(pName);
        }
        
        if (EXP.playerLevel.containsKey(pName))
        {
            EXP.playerLevel.remove(pName);
        }
        
        if (shootDelay.containsKey(pName))
        {
            shootDelay.remove(pName);
        }
        
        if (InventoryListener.playerPrimary.containsKey(pName))
        {
            InventoryListener.playerPrimary.remove(pName);
        }
        
        if (InventoryListener.playerSecondary.containsKey(pName))
        {
            InventoryListener.playerSecondary.remove(pName);
        }
        
        if (InventoryListener.playerMedKits.containsKey(pName))
        {
            InventoryListener.playerMedKits.remove(pName);
        }
        
        if (InventoryListener.playerGrenade.containsKey(pName))
        {
            InventoryListener.playerGrenade.remove(pName);
        }
        
        if (InventoryListener.playerSpecial.containsKey(pName))
        {
            InventoryListener.playerSpecial.remove(pName);
        }
        
        if (InventoryListener.playerPrimaryAmmo.containsKey(pName))
        {
            InventoryListener.playerPrimaryAmmo.remove(pName);
        }
        
        if (InventoryListener.playerSecondaryAmmo.containsKey(pName))
        {
            InventoryListener.playerSecondaryAmmo.remove(pName);
        }
        
        if (Game.gameRoundSkip.containsKey(pName))
        {
            Game.gameRoundSkip.remove(pName);
        }
        
        if (Game.playerCoins.containsKey(pName))
        {
            Game.playerCoins.remove(pName);
        }
        
        if (weldingSB.contains(pName))
        {
            weldingSB.remove(pName);
        }
        
    }
    
    public static void loadPlayerInv (Player player)
    {
        if (InventoryListener.playerPrimary.containsKey(player.getName()))
        {
            ItemStack primary = InventoryListener.playerPrimary.get(player.getName()).clone();
            ItemMeta primMeta = primary.getItemMeta();
            List<String> primLore = primMeta.getLore();
            String name = primLore.get(0);
            boolean isRanged = false;
            if (Guns.GunList.containsKey(name))
            {
                isRanged = true;
            }
            
            if (isRanged)
            {
                EnhancementListener.getRangedWeaponEnhancements(primary, player);
            }
            else
            {
                EnhancementListener.getMeleeWeaponEnhancements(primary, player);
            }
            
            
            player.getInventory().addItem(primary);
        }

        if (InventoryListener.playerSecondary.containsKey(player.getName()))
        {
            ItemStack secondary = InventoryListener.playerSecondary.get(player.getName()).clone();
            ItemMeta secondaryMeta = secondary.getItemMeta();
            List<String> secondaryLore = secondaryMeta.getLore();
            String name = secondaryLore.get(0);
            boolean isRanged = false;
            if (Guns.GunList.containsKey(name))
            {
                isRanged = true;
            }
            
            if (isRanged)
            {
                EnhancementListener.getRangedWeaponEnhancements(secondary, player);
            }
            else
            {
                EnhancementListener.getMeleeWeaponEnhancements(secondary, player);
            }
            
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

        /*
        ItemStack food = new ItemStack(Material.COOKED_FISH, 10);
        ItemMeta foodMeta = food.getItemMeta();
        foodMeta.setDisplayName(ChatColor.DARK_AQUA + "Rations");
        food.setItemMeta(foodMeta);
        player.getInventory().addItem(food);
        */

        //

        ItemStack kevlarVest = Guns.miscItemStacks.get("Kevlar Vest").clone();
        ItemStack kevlarPants = Guns.miscItemStacks.get("Kevlar Pants").clone();
        ItemStack kevlarBoots = Guns.miscItemStacks.get("Kevlar Boots").clone();

        EnhancementListener.getChestEnhancements(kevlarVest, player);
        EnhancementListener.getLegEnhancements(kevlarPants, player);
        EnhancementListener.getBootEnhancements(kevlarBoots, player);
        
        player.getInventory().setChestplate(kevlarVest);
        player.getInventory().setLeggings(kevlarPants);
        player.getInventory().setBoots(kevlarBoots);
    }
    
    
    private void launchFirework(Player player)
    {
        Firework fw = (Firework)player.getWorld().spawn(player.getLocation(), Firework.class);
        DyeColor fwColor = DyeColor.RED;
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
    
    
    public static void toLobby(Player player)
    {
        String msgType = "backtolobby";
        String playerName = player.getName();

        try
        {
            String ip = SRKF.config.getString("ProxyIP");
            int port = SRKF.config.getInt("ProxySocketPort");

            Socket lobbySocket = new Socket(ip, port);
            ObjectOutputStream oos = new ObjectOutputStream(lobbySocket.getOutputStream());

            oos.writeObject(new String[]
            {
               msgType, playerName
            });


            ObjectInputStream ois = new ObjectInputStream(lobbySocket.getInputStream());
            if (((String)ois.readObject()).equalsIgnoreCase("true"))
            {
                SRKF.LOG.log(Level.INFO, "[SRKF] - Fired off Socket Object: {0} and received reply for {1} on port {2}", new Object[]{msgType, ip, port});
            }

            ois.close();
            oos.close();
            lobbySocket.close();


        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
