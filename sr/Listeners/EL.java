package sr.Listeners;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_7_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftVillager;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Item;
import org.bukkit.entity.LargeFireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityBreakDoorEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import sr.CustomEntities.CustomEntityIronGolem;
import sr.Extras.EXP;
import sr.Extras.Guns;
import sr.Game;
import sr.Roles.Engineer;
import sr.SRKF;
import sr.ShopStuff.shopInventory;

/**
 *
 * @author Cross
 */
public class EL implements Listener
{
    public SRKF plugin;
    
    public EL (SRKF plugin)
    {
        this.plugin = plugin;
    }
    
    
    @EventHandler
    public void entityRegainHealth (EntityRegainHealthEvent event)
    {
        if (!(SRKF.isLobby))
        {
            event.setAmount(0);
            event.setCancelled(true);    
        }
        else
        {
            event.setAmount(10);
        }
    }
    
    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent event)
    {
        Chunk c = event.getChunk();
        Entity[] e = c.getEntities();
        for (Entity ent : e)
        {
            UUID uid = ent.getUniqueId();
            if (Game.mobID.containsKey(uid))
            {
                event.setCancelled(true);
                if (!(c.isLoaded()))
                {
                    c.load();
                }
                
                SRKF.LOG.info("[SRKF] - Prevented chunk unloading due to UID mob in chunk.");
            }
        }
    }
    
    @EventHandler
    public void shopKeeperInteract(PlayerInteractEntityEvent event)
    {
        Player player = event.getPlayer();
        if (PL.isSpectating.contains(player.getName()))
        {
            event.setCancelled(true);
            return;
        }
        
        if (event.getRightClicked() instanceof CraftVillager || event.getRightClicked() instanceof Villager)
        {
            if (!(PL.isReloading.contains(event.getPlayer().getName())))
            {
                int gameID = Game.playersMaps.get(event.getPlayer().getName());
                new shopInventory(event.getPlayer(), gameID);
                event.setCancelled(true);
            }
            else
            {
                event.getPlayer().sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "You must finish reloading before you can shop.");
                event.setCancelled(true);
                event.getPlayer().closeInventory();
            }
            
        }
    }
    
    
    @EventHandler
    public void onProjectileHit (ProjectileHitEvent event)
    {
        
        if (event.getEntity() instanceof Arrow)
        {
            event.getEntity().remove();
        }
        
        if (event.getEntity() instanceof LargeFireball)
        {
            LargeFireball fb = (LargeFireball) event.getEntity();
            if (fb.hasMetadata("AT4 Bazooka"))
            {
                List<MetadataValue> list = fb.getMetadata("AT4 Bazooka");
                Iterator<MetadataValue> it = list.iterator();
                String pName = "bleh";
                while (it.hasNext())
                {
                    MetadataValue bleh = it.next();
                    pName = bleh.asString();
                }

                String demoName = "AT4 Bazooka";
                Location loc = fb.getLocation();
                OfflinePlayer op = Bukkit.getOfflinePlayer(pName);
                if (op.isOnline())
                {
                    Player player = (Player) op;
                    new shootRocket(player, loc, demoName);
                }
            }
            
            if (fb.hasMetadata("L.A.W."))
            {
                List<MetadataValue> list = fb.getMetadata("L.A.W.");
                Iterator<MetadataValue> it = list.iterator();
                String pName = "bleh";
                while (it.hasNext())
                {
                    MetadataValue bleh = it.next();
                    pName = bleh.asString();
                }

                String demoName = "L.A.W.";
                Location loc = fb.getLocation();
                OfflinePlayer op = Bukkit.getOfflinePlayer(pName);
                if (op.isOnline())
                {
                    Player player = (Player) op;
                    new shootLaw(player, loc, demoName);
                }
            }
            
        }
        
        if (event.getEntity() instanceof Snowball)
        {
            Snowball ball = (Snowball) event.getEntity();
            if (ball.hasMetadata("Grenade"))
            {
                List<MetadataValue> list = ball.getMetadata("Grenade");
                Iterator<MetadataValue> it = list.iterator();
                String pName = "bleh";
                while (it.hasNext())
                {
                    MetadataValue bleh = it.next();
                    pName = bleh.asString();
                }
                
                Location loc = ball.getLocation();
                OfflinePlayer op = Bukkit.getOfflinePlayer(pName);
                if (op.isOnline())
                {
                    Player player = (Player) op;
                    Item grenade = loc.getWorld().dropItemNaturally(loc, new ItemStack(Material.FIREBALL));
                    new throwGrenade(player, loc, grenade);
                }
                
                ball.removeMetadata("Grenade", plugin);
                
            }
        }
    }
    
    @EventHandler
    public void onEntityBreakDoor(EntityBreakDoorEvent event)
    {
        event.setCancelled(true);
    }
    
    @EventHandler
    public void onEntityDeath (EntityDeathEvent event)
    {
        UUID uid = event.getEntity().getUniqueId();
        
        if (event.getEntity() instanceof CustomEntityIronGolem)
        {
            CustomEntityIronGolem e = (CustomEntityIronGolem) event.getEntity();
            event.getDrops().clear();
            event.setDroppedExp(0);
            

            if (Game.igID.contains(e))
            {
                Game.igID.remove(e);
            }
            /*
             * Drop cash
             */
            
            CraftWorld w3 = e.world.getWorld();
            int x = (int) e.locX;
            int y = (int) e.locY;
            int z = (int) e.locZ;
            Block block = w3.getBlockAt(x, y, z);
            Location loc = block.getLocation();
            
            //Bukkit.broadcastMessage("CustomEntityIronGolem reached");
            double rand = Math.random();
            w3.dropItemNaturally(loc, new ItemStack(Material.EMERALD));

        }
        else
        {
            LivingEntity e = event.getEntity();
            event.getDrops().clear();
            event.setDroppedExp(0);

            
            if (Game.leID.contains(e))
            {
                Game.leID.remove(e);
            }
            /*
             * Drop cash
             */

            if (e instanceof IronGolem || e instanceof Blaze)
            {
                double rand = Math.random();
                e.getWorld().dropItemNaturally(e.getLocation(), new ItemStack(Material.EMERALD));
            }
            else
            {
                double rand = Math.random();
                if (rand >= 0.25)
                {
                    e.getWorld().dropItemNaturally(e.getLocation(), new ItemStack(Material.DIAMOND));
                }
                else
                if (rand <= 0.00)
                {
                    e.getWorld().dropItemNaturally(e.getLocation(), new ItemStack(Material.EMERALD));
                }
            }
        }
        
        if (Game.hasWound.contains(uid))
        {
            Game.hasWound.remove(uid);
        }
        
        /*
         * Update Mob HP
         */
        if (Game.mobHealth.containsKey(uid))
        {
            Game.mobHealth.remove(uid);
        }
        
        /*
         * Update Game Kills
         */
        if (Game.mobID.containsKey(uid))
        {
            int gameID = Game.mobID.get(uid);
            int kills = 0;
            if (PL.mapKills.containsKey(gameID))
            {
                kills = PL.mapKills.get(gameID);
                PL.mapKills.remove(gameID);
            }
            kills = kills + 1;
            PL.mapKills.put(gameID, kills);
            //Bukkit.broadcastMessage("Mob died. Kill count is: " + kills);
            Game.updateMobCount(gameID);
                
            Game.mobID.remove(uid);
        }
        /*
         * 
         */
        
        if (event.getEntity().getKiller() instanceof Projectile)
        {
            Projectile proj = (Projectile) event.getEntity().getKiller();
            
            if (proj.getShooter() instanceof Player)
            {
                Player killer = (Player) proj.getShooter();
                if (Game.playersMaps.containsKey(killer.getName()))
                {
                    /*
                     * Do money
                     */
                    
                    int cash = 0;
                    if (Game.playerMoney.containsKey(killer.getName()))
                    {
                        cash = Game.playerMoney.get(killer.getName());
                    }

                    double rand = Math.random();
                    
                    
                    if (event.getEntity() instanceof IronGolem || event.getEntity() instanceof Blaze)
                    {
                        if (rand >= 0.10)
                        {
                            Random rand2 = new Random();
                            int randNum = rand2.nextInt((InventoryListener.emeraldMax - InventoryListener.emeraldMin) + 1) + InventoryListener.emeraldMin;

                            cash = cash + randNum;
                            
                            Game.playerMoney.put(killer.getName(), cash);
                            //killer.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.GOLD + "Your kill earned you " + ChatColor.GREEN + "$" + randNum 
                            //        + ChatColor.GOLD + " cash, total is now: " + ChatColor.GREEN + "$" + cash);
                            
                            // update scoreboard
                            Scoreboard sb = killer.getScoreboard();
                            if (sb != null)
                            {
                                Objective objective = sb.getObjective(DisplaySlot.SIDEBAR);
                                Score score = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.GOLD + " Cash: " + ChatColor.GREEN + "$"));
                                score.setScore(cash);
                            }
                        }
                    }
                    else
                    {
                        if (rand >= 0.10)
                        {
                            Random rand2 = new Random();
                            int randNum = rand2.nextInt((InventoryListener.diamondMax - InventoryListener.diamondMin) + 1) + InventoryListener.diamondMin;

                            cash = cash + randNum;

                            Game.playerMoney.put(killer.getName(), cash);
                            //killer.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.GOLD + "Your kill earned you " + ChatColor.GREEN + "$" + randNum 
                            //        + ChatColor.GOLD + " cash, total is now: " + ChatColor.GREEN + "$" + cash);
                            
                            // update scoreboard
                            Scoreboard sb = killer.getScoreboard();
                            if (sb != null)
                            {
                                Objective objective = sb.getObjective(DisplaySlot.SIDEBAR);
                                Score score = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.GOLD + " Cash: " + ChatColor.GREEN + "$"));
                                score.setScore(cash);
                            }
                        }
                    }
                    
                    
                    
                    /*
                     * Player Kill update
                     */
                    int killCount = 0;

                    if (PL.playerKills.containsKey(killer.getName()))
                    {
                        killCount = PL.playerKills.get(killer.getName());
                        PL.playerKills.remove(killer.getName());
                    }

                    killCount++;
                    PL.playerKills.put(killer.getName(), killCount);

                    /*
                     * Player EXP update
                     */
                    int gameID = Game.playersMaps.get(killer.getName());
                    
                    EntityType type = event.getEntityType();
                    int exp = 0;
                    if (EXP.mobValue.containsKey(type))
                    {
                        exp = EXP.mobValue.get(type);
                    }
                    int round = Game.mapRound.get(gameID);
                    
                    double newEXP = Math.floor(exp + (1.25 * round));
                    int prevEXP = 0;
                    if (EXP.playerEXP.containsKey(killer.getName()))
                    {
                        prevEXP = EXP.playerEXP.get(killer.getName());
                        EXP.playerEXP.remove(killer.getName());
                    }
                    int newExpInt = (int) newEXP;
                    
                    Random random = new Random();
                    int vary = random.nextInt(6);
                    int totalEXP = prevEXP + newExpInt + vary;
                    //Bukkit.broadcastMessage("totalEXP is: " + totalEXP);
                    
                    if (totalEXP > 2699136)
                    {
                        totalEXP = 2699136;
                    }
                    
                    EXP.playerEXP.put(killer.getName(), totalEXP);
                    
                    /*
                     * Game Kill update
                     */

                    Game.updateMobCount(gameID);

                }
            }
        }
        
        if (event.getEntity().getKiller() instanceof Player)
        {
            Player killer = event.getEntity().getKiller();
            
            if (Game.playersMaps.containsKey(killer.getName()))
            {
                /*
                 * Give money
                 */
                
                int cash = 0;
                if (Game.playerMoney.containsKey(killer.getName()))
                {
                    cash = Game.playerMoney.get(killer.getName());
                }
                
                double rand = Math.random();
                if (rand >= 0.02)
                {
                    Random rand2 = new Random();
                    int randNum = rand2.nextInt((InventoryListener.diamondMax - InventoryListener.diamondMin) + 1) + InventoryListener.diamondMin;

                    cash = cash + randNum;

                    Game.playerMoney.put(killer.getName(), cash);
                    //killer.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.GOLD + "Your kill earned you " + ChatColor.GREEN + "$" + randNum 
                    //        + ChatColor.GOLD + " cash, total is now: " + ChatColor.GREEN + "$" + cash);
                    
                    // update scoreboard
                    Scoreboard sb = killer.getScoreboard();
                    if (sb != null)
                    {
                        Objective objective = sb.getObjective(DisplaySlot.SIDEBAR);
                        Score score = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.GOLD + " Cash: " + ChatColor.GREEN + "$"));
                        score.setScore(cash);
                    }
                }
                else
                if (rand <= 0.00)
                {
                    Random rand2 = new Random();
                    int randNum = rand2.nextInt((InventoryListener.emeraldMax - InventoryListener.emeraldMin) + 1) + InventoryListener.emeraldMin;

                    cash = cash + randNum;

                    Game.playerMoney.put(killer.getName(), cash);
                    //killer.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.GOLD + "Your kill earned you " + ChatColor.GREEN + "$" + randNum 
                    //        + ChatColor.GOLD + " cash, total is now: " + ChatColor.GREEN + "$" + cash);
                    
                    // update scoreboard
                    Scoreboard sb = killer.getScoreboard();
                    if (sb != null)
                    {
                        Objective objective = sb.getObjective(DisplaySlot.SIDEBAR);
                        Score score = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.GOLD + " Cash: " + ChatColor.GREEN + "$"));
                        score.setScore(cash);
                    }
                }
                
                
                /*
                 * Player Kill update
                 */
                int killCount = 0;
                
                if (PL.playerKills.containsKey(killer.getName()))
                {
                    killCount = PL.playerKills.get(killer.getName());
                    PL.playerKills.remove(killer.getName());
                }
                
                killCount++;
                PL.playerKills.put(killer.getName(), killCount);
                
                /*
                 * Player EXP update
                 */
                int gameID = Game.playersMaps.get(killer.getName());

                EntityType type = event.getEntityType();
                int exp = 0;
                if (EXP.mobValue.containsKey(type))
                {
                    exp = EXP.mobValue.get(type);
                }
                
                int round = 0;
                if (Game.mapRound.containsKey(gameID))
                {
                    round = Game.mapRound.get(gameID);
                }

                double newEXP = Math.floor(exp + (1.25 * round));
                int prevEXP = 0;
                if (EXP.playerEXP.containsKey(killer.getName()))
                {
                    prevEXP = EXP.playerEXP.get(killer.getName());
                    EXP.playerEXP.remove(killer.getName());
                }
                int newExpInt = (int) newEXP;
                    
                
                
                Random random = new Random();
                int vary = random.nextInt(6);
                int totalEXP = prevEXP + newExpInt + vary;
                //Bukkit.broadcastMessage("totalEXP is: " + totalEXP);
                
                if (totalEXP > 2699136)
                {
                    totalEXP = 2699136;
                }

                EXP.playerEXP.put(killer.getName(), totalEXP);
                    
                /*
                 * Game Kill update
                 */
                
                Game.updateMobCount(gameID);
                
            }
        }
        
        if (event.getEntity() != null)
        {
            event.getEntity().remove();
        }
    }
    
    @EventHandler
    public void onCombust (EntityCombustEvent event)
    {
        Entity ent = event.getEntity();
        
        if (ent instanceof LivingEntity)
        {
            LivingEntity le = (LivingEntity) ent;
            
            if (!(le instanceof Player))
            {
                event.setCancelled(true);
                event.setDuration(0);
            }
        }
    }
    
    @EventHandler
    public void onMobSpawn (CreatureSpawnEvent event)
    {
        LivingEntity le = event.getEntity();
        
        if (event.getSpawnReason() == SpawnReason.EGG)
        {
            event.getEntity().remove();
        }
        
        if (!(event.getSpawnReason() == SpawnReason.CUSTOM))
        {
            event.getEntity().remove();
        }
        
        
        
        /*
        EntityType type = event.getEntityType();
        Location loc = event.getLocation();
        World world = ((CraftWorld) loc.getWorld()).getHandle();
        String worldName = world.getWorldData().getName();
        
        for (CustomEntityType customEntity : CustomEntityType.values())
        {
            if (type == customEntity.getEntityType())
            {
                customEntity.spawnEntity(loc);
            }
        }
        */
        
        
        
        /*
         * multiply follow range by 3 of default (32 typically - 2 chunks). This should set to 96 (6 chunks) 
         * As view distance is set to 10, this should be sufficient.
         */
        
    
        /*
        UUID leID = le.getUniqueId();
        
        if (Game.mobID.containsKey(leID))
        {
            SRKF.LOG.info("SRKF - Spawning Living Entity: " + le.getType());
            CustomAttributes.applyFollowRange(le, 7);
            //CustomAttributes.applyMovementSpeed(le, 2);
            CustomAttributes.applyKnockbackResistance(le, 15);
            
            //check round
            int ID = Game.mobID.get(leID);
            
            if (Game.mapRound.containsKey(ID))
            {

                int round = Game.mapRound.get(ID);
                
                if (le.getType() == EntityType.ZOMBIE)
                {
                    double setZombieHP = Math.ceil((5 + (1.66 * round)) * 1.33);
                    SRKF.LOG.info("Zombie HP: " + setZombieHP);
                    le.setMaxHealth(setZombieHP);
                }
                
                if (le.getType() == EntityType.SPIDER)
                {
                    double setSpiderHP = Math.ceil((2 + (1.66 * round)) * 1.33);
                    SRKF.LOG.info("Spider HP: " + setSpiderHP);
                    le.setMaxHealth(setSpiderHP);
                }
                
                
                if (le.getType() == EntityType.SKELETON)
                {
                    double setSkeletonHP = Math.ceil((7 + (1.66 * round)) * 1.33);
                    SRKF.LOG.info("Spider HP: " + setSkeletonHP);
                    le.setMaxHealth(setSkeletonHP);
                }
                
                
                if (le.getType() == EntityType.CREEPER)
                {
                    double setCreeperHP = Math.ceil((13 + (1.66 * round)) * 1.33);
                    SRKF.LOG.info("Creeper HP: " + setCreeperHP);
                    le.setMaxHealth(setCreeperHP);
                }
                
                if (le.getType() == EntityType.IRON_GOLEM)
                {
                    double setGolemHP = Math.ceil((33 + (1.66 * round)) * 1.33);
                    SRKF.LOG.info("Flesh Pound HP: " + setGolemHP);
                    le.setMaxHealth(setGolemHP);
                }
            }
    
        }
        */
        
    }
    
    private class shootLaw implements Runnable
    {
        private Player player;

        private int taskID;
        private Location loc;
        String demoName;
        public shootLaw(Player player, Location loc, String demoName)
        {
            this.demoName = demoName;
            this.player = player;
            this.loc = loc;
            this.taskID = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, this, 0);
        }
        
        @Override
        public void run() 
        {
            World w = loc.getWorld();
            
            Location loc2 = loc.clone();
            loc2.setZ(loc2.getZ() - 1);
            loc2.setZ(loc2.getX() - 1);
            
            Location loc3 = loc.clone();
            loc3.setZ(loc3.getZ() + 1);
            loc3.setZ(loc3.getX() + 1);
            
            Location loc4 = loc.clone();
            loc4.setZ(loc4.getZ() + 2);
            loc4.setZ(loc4.getX() + 2);
            
            Location loc5 = loc.clone();
            loc5.setZ(loc5.getZ() - 2);
            loc5.setZ(loc5.getX() - 2);
            
            w.createExplosion(loc, 0.0F, false);
            w.createExplosion(loc2, 0.0F, false);
            w.createExplosion(loc3, 0.0F, false);
            w.createExplosion(loc4, 0.0F, false);
            w.createExplosion(loc5, 0.0F, false);
            
            for (Entity e : w.getEntities())
            {
                if (e instanceof LivingEntity)
                {
                    //Bukkit.broadcastMessage("is le");
                    LivingEntity le = (LivingEntity) e;
                    if (!(le instanceof Player))
                    {
                        //Bukkit.broadcastMessage("is not player");
                        if (le.getWorld().equals(w))
                        {
                            //Bukkit.broadcastMessage("is same world");
                            if (loc.distance(le.getLocation()) <= 6.0D)
                            {
                                //Bukkit.broadcastMessage("le is: " + le.getType().toString());
                                UUID uid = le.getUniqueId();
                                PL.rocketHit.put(uid, player.getName());
                                double dmg = Guns.DemoDamage.get(demoName);
                                double bonusdmg = 0;
                                String role = Game.playerClass.get(player.getName());
                                
                                if (role.equalsIgnoreCase("Engineer"))
                                {
                                    bonusdmg = Engineer.getHeavyArtilleryBonusAmount(player.getName());
                                }
                                
                                double total = dmg + bonusdmg;
                                
                                if (!(le instanceof Villager) || !(le instanceof CraftVillager))
                                {
                                    le.damage(total);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    private class shootRocket implements Runnable
    {
        private Player player;

        private int taskID;
        private Location loc;
        String demoName;
        public shootRocket(Player player, Location loc, String demoName)
        {
            this.demoName = demoName;
            this.player = player;
            this.loc = loc;
            this.taskID = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, this, 0);
        }
        
        @Override
        public void run() 
        {
            World w = loc.getWorld();
            
            Location loc2 = loc.clone();
            loc2.setZ(loc2.getZ() - 1);
            loc2.setZ(loc2.getX() - 1);
            
            Location loc3 = loc.clone();
            loc3.setZ(loc3.getZ() + 1);
            loc3.setZ(loc3.getX() + 1);
            
            w.createExplosion(loc, 0.0F, false);
            w.createExplosion(loc2, 0.0F, false);
            w.createExplosion(loc3, 0.0F, false);
            
            for (Entity e : w.getEntities())
            {
                if (e instanceof LivingEntity)
                {
                    //Bukkit.broadcastMessage("is le");
                    LivingEntity le = (LivingEntity) e;
                    if (!(le instanceof Player))
                    {
                        //Bukkit.broadcastMessage("is not player");
                        if (le.getWorld().equals(w))
                        {
                            //Bukkit.broadcastMessage("is same world");
                            if (loc.distance(le.getLocation()) <= 3.0D)
                            {
                                //Bukkit.broadcastMessage("le is: " + le.getType().toString());
                                UUID uid = le.getUniqueId();
                                PL.rocketHit.put(uid, player.getName());
                                double dmg = Guns.DemoDamage.get(demoName);
                                double bonusdmg = 0;
                                String role = Game.playerClass.get(player.getName());
                                
                                if (role.equalsIgnoreCase("Engineer"))
                                {
                                    bonusdmg = Engineer.getHeavyArtilleryBonusAmount(player.getName());
                                }
                                
                                double total = dmg + bonusdmg;
                                
                                if (!(le instanceof Villager) || !(le instanceof CraftVillager))
                                {
                                    le.damage(total);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    private class throwGrenade implements Runnable
    {
        private Player player;
        private int taskID;
        private Location loc;
        private Item item;
        
        public throwGrenade(Player player, Location loc, Item item)
        {
            this.player = player;
            this.taskID = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, this, 40);
            this.loc = loc;
            this.item = item;
        }
        
        @Override
        public void run() 
        {
            this.item.remove();
            
            World w = loc.getWorld();
            
            w.createExplosion(loc, 0.0F, false);
            
            for (Entity e : w.getEntities())
            {
                if (e instanceof LivingEntity)
                {
                    //Bukkit.broadcastMessage("is le");
                    LivingEntity le = (LivingEntity) e;
                    if (!(le instanceof Player))
                    {
                        //Bukkit.broadcastMessage("is not player");
                        if (le.getWorld().equals(w))
                        {
                            //Bukkit.broadcastMessage("is same world");
                            if (loc.distance(le.getLocation()) <= 8.0D)
                            {
                                //Bukkit.broadcastMessage("le is: " + le.getType().toString());
                                UUID uid = le.getUniqueId();
                                PL.grenadeHit.put(uid, player.getName());
                                double damage = Guns.DemoDamage.get("Grenade");
                                double bonusdmg = 0;
                                String role = Game.playerClass.get(player.getName());
                                
                                if (role.equalsIgnoreCase("Engineer"))
                                {
                                    bonusdmg = Engineer.getDemoltionMasteryBonusAmount(player.getName());
                                }
                                
                                double total = damage + bonusdmg;
                                
                                if (!(le instanceof Villager) || !(le instanceof CraftVillager))
                                {
                                    le.damage(total);
                                }
                               
                            }
                        }
                    }
                }
            }
        }
        
    }
    
}
