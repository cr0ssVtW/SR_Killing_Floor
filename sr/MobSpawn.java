package sr;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;
import net.minecraft.server.v1_7_R1.EntityCreature;
import net.minecraft.server.v1_7_R1.EntityLiving;
import net.minecraft.server.v1_7_R1.EntitySkeleton;
import net.minecraft.server.v1_7_R1.PathfinderGoal;
import net.minecraft.server.v1_7_R1.PathfinderGoalSelector;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_7_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftCreature;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftSkeleton;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import sr.CustomEntities.CustomEntityIronGolem;
import sr.Extras.CustomAttributes;

/**
 *
 * @author Cross
 */


public class MobSpawn 
{
    public SRKF plugin;
    
    public static HashMap<Integer, Integer> waveNumber = new HashMap<Integer, Integer>();
    
    Location loc;
    int ID;
    int spawncount;
    int round;
    String pName;
    public MobSpawn (SRKF plugin, Location spawnLoc, int gameID, int spawnCount, int roundNumber, String potentialPlayer)
    {
        this.plugin = plugin;
        this.loc = spawnLoc;
        this.ID = gameID;
        this.spawncount = spawnCount;
        this.round = roundNumber;
        this.pName = potentialPlayer;
        doMobSpawn();
    }
    
    private void doMobSpawn()
    {
        double crawlerChance = 0;
        double fleshPoundChance = 0;
        double skeletonChance = 0;
        double creeperChance = 0;
        double blazeChance = 0;
        if (round <= 1)
        {
            crawlerChance = 0.05;
            fleshPoundChance = 0;
        }
        if (round <= 3)
        {
            crawlerChance = 0.45;
            skeletonChance = 0.43;
            fleshPoundChance = 0;
            creeperChance = 0.10;
            blazeChance = 0.0;
        }
        if (round >= 4 && round <= 6)
        {
            crawlerChance = 0.66;
            skeletonChance = 0.57;
            creeperChance = 0.35;
            fleshPoundChance = 0.55;
            blazeChance = 0.15;
        }
        if (round >= 7 && round < 12)
        {
            crawlerChance = 0.90;
            skeletonChance = 0.75;
            creeperChance = 0.75;
            fleshPoundChance = 0.65;
            blazeChance = 0.65;
        }
        if (round >= 12)
        {
            crawlerChance = 1;
            skeletonChance = 1;
            creeperChance = 1;
            fleshPoundChance = 0.85;
            blazeChance = 1;
        }
        
        boolean success = false;
        
        double firstRoll = Math.random();
        if (firstRoll > 0.75)
        {
            // do something other than zombie.
            
            double secondRoll = Math.random();
            double thirdRoll = Math.random();
            double spawnRoll = Math.random();
            if (secondRoll >= thirdRoll)
            {
                // do skeleton and crawlers
                if (thirdRoll >= 0.67)
                {
                    // do skeleton
                    if (spawnRoll <= skeletonChance)
                    {
                        success = spawnSkeleton(loc, ID);
                        //Bukkit.broadcastMessage("spawnSkeleton");
                    }
                    else
                    {
                        success = spawnZombie(loc, ID);
                        //Bukkit.broadcastMessage("spawnZombie1");
                    }
                }
                else
                if (thirdRoll >= 0.33 && thirdRoll < 0.67)
                {
                    if (spawnRoll <= blazeChance)
                    {
                        success = spawnBlaze(loc, ID);
                    }
                    else
                    {
                        success = spawnZombie(loc, ID);
                    }
                }
                else
                {
                    // do crawler
                    if (spawnRoll <= crawlerChance)
                    {
                        Player luckyGuy = Bukkit.getPlayer(pName);
                        success = spawnCrawler(luckyGuy, ID);
                        //Bukkit.broadcastMessage("spawnCrawler");
                    }
                    else
                    {
                        success = spawnZombie(loc, ID);
                        //Bukkit.broadcastMessage("spawnZombie2");
                    }
                }
            }
            else
            {
                // do creeper and flesh pounds
                
                if (thirdRoll >= 0.5)
                {
                    // do creeper
                    if (spawnRoll <= creeperChance)
                    {
                        success = spawnCreeper(loc, ID);
                        //Bukkit.broadcastMessage("spawnCreeper");
                    }
                    else
                    {
                        success = spawnZombie(loc, ID);
                        //Bukkit.broadcastMessage("spawnZombie3");
                    }
                }
                else
                {
                    // do fleshpound
                    if (spawnRoll <= fleshPoundChance)
                    {
                        success = spawnFleshPound(loc, ID);
                        //Bukkit.broadcastMessage("spawnFleshPound");
                    }
                    else
                    {
                        success = spawnZombie(loc, ID);
                        //Bukkit.broadcastMessage("spawnZombie4");
                    }
                }
            }
            
            
            /*
            if (secondRoll <= fleshPoundChance && secondRoll <= crawlerChance)
            {
                success = spawnFleshPound(loc, ID);
            }
            else
            if (secondRoll >= fleshPoundChance && secondRoll <= crawlerChance)
            {
                Player luckyGuy = Bukkit.getPlayer(pName);
                success = spawnCrawler(luckyGuy, ID);
            }
            else
            {
                success = spawnZombie(loc, ID);
            }
            */
            
        }
        else
        {
            success = spawnZombie(loc, ID);
            //success = spawnFleshPound(loc, ID);
        }
        
        if (success)
        {
            spawncount++;
            if (Game.mapIDSpawned.containsKey(ID))
            {
                Game.mapIDSpawned.remove(ID);
            }
            //Bukkit.broadcastMessage("Mob spawned. Spawn count is: " + spawncount);
            Game.mapIDSpawned.put(ID, spawncount);
            
            Game.updateMobCount(ID);
        }
    }
    
    
    private boolean spawnZombie (Location loc, int ID)
    {
        boolean canSpawn = false;
        Player target = Bukkit.getPlayer(this.pName);
        Location targetLoc = target.getLocation();
        //loc.setY(loc.getBlockY() + 1);
        Block block = loc.getBlock();
        
        Location loc2 = loc.clone();
        loc2.setY(loc2.getY() + 1);
        Block block2 = loc2.getBlock();
        
        World world = (World) loc.getWorld();
        Chunk c = loc.getChunk();
        if (!(c.isLoaded()))
        {
            c.load();
        }
        
        if (block.getType() != Material.AIR || block2.getType() != Material.AIR)
        {
            canSpawn = false;
            SRKF.LOG.warning("[SRKF] - Failed to spawnZombie - block is not AIR.");
            //Bukkit.broadcastMessage("Failed to spawnZombie - block is not AIR.");
        }
        else
        {
            canSpawn = true;
            Zombie newEnt = (Zombie) Bukkit.getWorld(world.getName()).spawnEntity(loc, EntityType.ZOMBIE);
            EntityCreature ec = ((CraftCreature)newEnt).getHandle();
            LivingEntity le = (LivingEntity) newEnt;
            
            UUID eID = newEnt.getUniqueId();
            if (Game.mobID.containsKey(eID))
            {
                Game.mobID.remove(eID);
            }
            Game.mobID.put(eID, ID);
            
            if (Game.leID.contains(le))
            {
                Game.leID.remove(le);
            }
            Game.leID.add(le);
            
            double setZombieHP = Math.ceil((4 + (1.66 * round)) * 1.25);
            //SRKF.LOG.info("Zombie HP: " + setZombieHP);
            le.setMaxHealth(setZombieHP);
            le.setHealth(le.getMaxHealth());
            
            CustomAttributes.applyKnockbackResistance(le);
        }
        
        return canSpawn;
    }
    
    private boolean spawnSkeleton (Location loc, int ID)
    {
        boolean canSpawn = false;
        Player target = Bukkit.getPlayer(this.pName);
        Location targetLoc = target.getLocation();
        //loc.setY(loc.getBlockY() + 1);
        Block block = loc.getBlock();
        World world = (World) loc.getWorld();
        
        Location loc2 = loc.clone();
        loc2.setY(loc2.getY() + 1);
        Block block2 = loc2.getBlock();
        
        Location loc3 = loc.clone();
        loc3.setY(loc3.getY() + 2);
        Block block3 = loc3.getBlock();
        
        
        Chunk c = loc.getChunk();
        if (!(c.isLoaded()))
        {
            c.load();
        }
        
        if (block.getType() != Material.AIR && block2.getType() != Material.AIR && block3.getType() != Material.AIR)
        {
            canSpawn = false;
            SRKF.LOG.warning("[SRKF] - Failed to spawnSkeleton - block is not AIR.");
            //Bukkit.broadcastMessage("Failed to spawnSkeleton - block is not AIR.");
        }
        else
        {
            canSpawn = true;
            Skeleton newEnt = (Skeleton) Bukkit.getWorld(world.getName()).spawnEntity(loc, EntityType.SKELETON);
            EntityCreature ec = ((CraftCreature)newEnt).getHandle();
            LivingEntity le = (LivingEntity) newEnt;

            double random = Math.random();
            
            if (random >= 0.60)
            {
                newEnt.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 300, 0));
                newEnt.setSkeletonType(Skeleton.SkeletonType.WITHER);
                EntityEquipment ee = le.getEquipment();
                
                double Rand = Math.random();
                if (Rand >= 0.00 && Rand < 0.33)
                {
                    ee.setItemInHand(new ItemStack(Material.DIAMOND_AXE));
                }
                
                if (Rand >= 0.33 && Rand < 0.66)
                {
                    ee.setItemInHand(new ItemStack(Material.GOLD_SWORD));
                }
                
                if (Rand >= 0.66)
                {
                    ee.setItemInHand(new ItemStack(Material.STONE_SWORD));
                }
                
            }
            else
            {
                EntityEquipment ee = le.getEquipment();
                ee.setItemInHand(new ItemStack(Material.BOW));
                //changeIntoNormal(newEnt, false);
            }
            
            UUID eID = newEnt.getUniqueId();
            if (Game.mobID.containsKey(eID))
            {
                Game.mobID.remove(eID);
            }
            Game.mobID.put(eID, ID);
            
            if (Game.leID.contains(le))
            {
                Game.leID.remove(le);
            }
            Game.leID.add(le);
            
            double setSkeletonHP = Math.ceil((18 + (1.33 * round)) * 1.33);
            //SRKF.LOG.info("Skeleton HP: " + setSkeletonHP);
            le.setMaxHealth(setSkeletonHP);
            le.setHealth(le.getMaxHealth());
            
            CustomAttributes.applyKnockbackResistance(le);
                    
        }
        
        return canSpawn;
    }
    
    private boolean spawnCreeper (Location loc, int ID)
    {
        boolean canSpawn = false;
        Player target = Bukkit.getPlayer(this.pName);
        Location targetLoc = target.getLocation();
        //loc.setY(loc.getBlockY() + 1);
        Block block = loc.getBlock();
        World world = (World) loc.getWorld();
        
        Location loc2 = loc.clone();
        loc2.setY(loc2.getY() + 1);
        Block block2 = loc2.getBlock();
        
        Chunk c = loc.getChunk();
        if (!(c.isLoaded()))
        {
            c.load();
        }
        
        
        if (block.getType() != Material.AIR && block2.getType() != Material.AIR)
        {
            canSpawn = false;
            SRKF.LOG.warning("[SRKF] - Failed to spawnCreeper - block is not AIR.");
            //Bukkit.broadcastMessage("Failed to spawnCreeper - block is not AIR.");
        }
        else
        {
            canSpawn = true;
            Creeper newEnt = (Creeper) Bukkit.getWorld(world.getName()).spawnEntity(loc, EntityType.CREEPER);
            EntityCreature ec = ((CraftCreature)newEnt).getHandle();
            LivingEntity le = (LivingEntity) newEnt;
            
            UUID eID = newEnt.getUniqueId();
            if (Game.mobID.containsKey(eID))
            {
                Game.mobID.remove(eID);
            }
            Game.mobID.put(eID, ID);
            
            if (Game.leID.contains(le))
            {
                Game.leID.remove(le);
            }
            Game.leID.add(le);
            
            double setCreeperHP = Math.ceil((12 + (1.33 * round)) * 1.33);
            //SRKF.LOG.info("Creeper HP: " + setCreeperHP);
            le.setMaxHealth(setCreeperHP);
            le.setHealth(le.getMaxHealth());
            
            CustomAttributes.applyKnockbackResistance(le);
        }
        
        return canSpawn;
    }
    
    
    private boolean spawnBlaze (Location loc, int ID)
    {
        boolean canSpawn = false;
        Player target = Bukkit.getPlayer(this.pName);
        Location targetLoc = target.getLocation();
        //loc.setY(loc.getBlockY() + 1);
        Block block = loc.getBlock();
        World world = (World) loc.getWorld();
        
        Location loc2 = loc.clone();
        loc2.setY(loc2.getY() + 1);
        Block block2 = loc2.getBlock();
        
        Chunk c = loc.getChunk();
        if (!(c.isLoaded()))
        {
            c.load();
        }
        
        
        if (block.getType() != Material.AIR && block2.getType() != Material.AIR)
        {
            canSpawn = false;
            SRKF.LOG.warning("[SRKF] - Failed to spawnBlaze - block is not AIR.");
        }
        else
        if (round < 4)
        {
            canSpawn = false;
        }
        else
        {
            canSpawn = true;
            Blaze newEnt = (Blaze) Bukkit.getWorld(world.getName()).spawnEntity(loc, EntityType.BLAZE);
            EntityCreature ec = ((CraftCreature)newEnt).getHandle();
            LivingEntity le = (LivingEntity) newEnt;
            
            UUID eID = newEnt.getUniqueId();
            if (Game.mobID.containsKey(eID))
            {
                Game.mobID.remove(eID);
            }
            Game.mobID.put(eID, ID);
            
            if (Game.leID.contains(le))
            {
                Game.leID.remove(le);
            }
            Game.leID.add(le);
            
            
            
            double setBlazeHP = Math.ceil((50 + (3 * round)) * 2);
            //SRKF.LOG.info("Creeper HP: " + setCreeperHP);
            le.setMaxHealth(setBlazeHP);
            le.setHealth(le.getMaxHealth());
            Game.mobHealth.put(eID, setBlazeHP);
            SRKF.LOG.log(Level.INFO, "Blaze HP is: {0} - on round: {1}", new Object[]{setBlazeHP, round});
            CustomAttributes.applyKnockbackResistance(le);
        }
        
        return canSpawn;
    }
    
    private boolean spawnCrawler(Player player, int ID)
    {
        boolean canSpawn = false;
        Location loc = player.getLocation();
        loc.setY(loc.getBlockY() + 2);
        loc.setX(loc.getBlockX() + 2);
        loc.setZ(loc.getBlockZ() - 2);
        World world = (World) loc.getWorld();
        Block blockY = loc.getBlock();
        
        Location loc2 = loc.clone();
        loc2.setY(loc2.getY() + 1);
        Block block2 = loc2.getBlock();
        
        Chunk c = loc.getChunk();
        if (!(c.isLoaded()))
        {
            c.load();
        }
        
        if (blockY.getType() != Material.AIR && block2.getType() != Material.AIR)
        {
            canSpawn = false;
            SRKF.LOG.warning("[SRKF] - Failed to spawnCrawler - block is not AIR.");
            //Bukkit.broadcastMessage("Failed to spawnCrawler - block is not AIR.");
        }
        else
        {
            canSpawn = true;
            Spider newEnt = (Spider) Bukkit.getWorld(world.getName()).spawnEntity(loc, EntityType.SPIDER);
            EntityCreature ec = ((CraftCreature)newEnt).getHandle();
            LivingEntity le = (LivingEntity) newEnt;
            
            CustomAttributes.applyKnockbackResistance(le);
            
            UUID eID = newEnt.getUniqueId();
            if (Game.mobID.containsKey(eID))
            {
                Game.mobID.remove(eID);
            }
            Game.mobID.put(eID, ID);
            
            if (Game.leID.contains(le))
            {
                Game.leID.remove(le);
            }
            Game.leID.add(le);
            
            double setSpiderHP = Math.ceil((2 + (1.33 * round)) * 1.33);
            //SRKF.LOG.info("Spider HP: " + setSpiderHP);
            le.setMaxHealth(setSpiderHP);
            le.setHealth(le.getMaxHealth());
        }
        
        return canSpawn;
        
    }
    
    private boolean spawnFleshPound(Location loc, int ID)
    {
        boolean canSpawn = false;
        Player target = Bukkit.getPlayer(this.pName);
        Location targetLoc = target.getLocation();
        //loc.setY(loc.getBlockY() + 1);
        Block block = loc.getBlock();
        World world = (World) loc.getWorld();
        
        Location loc2 = loc.clone();
        loc2.setY(loc2.getY() + 1);
        Block block2 = loc2.getBlock();
        
        Location loc3 = loc.clone();
        loc3.setY(loc3.getY() + 2);
        Block block3 = loc3.getBlock();
        
        
        Chunk c = loc.getChunk();
        if (!(c.isLoaded()))
        {
            c.load();
        }
        
        if (block.getType() != Material.AIR && block2.getType() != Material.AIR && block3.getType() != Material.AIR)
        {
            canSpawn = false;
            SRKF.LOG.warning("[SRKF] - Failed to spawnFleshPound - block is not AIR.");
            //Bukkit.broadcastMessage("Failed to spawnFleshPound - block is not AIR.");
        }
        else
        if (round < 6)
        {
            canSpawn = false;
        }
        else
        {
            canSpawn = true;
            //IronGolem newEnt = (IronGolem) Bukkit.getWorld(world.getName()).spawnEntity(loc, EntityType.IRON_GOLEM);
            //EntityCreature ec = ((CraftCreature)newEnt).getHandle();
            //LivingEntity le = (LivingEntity) newEnt;
            
            //le.damage(5, target);
            
            
            Location location = loc;
            
            World w2 = location.getWorld();
            net.minecraft.server.v1_7_R1.World w3 = ((CraftWorld) w2).getHandle();
            CustomEntityIronGolem irongolem = new CustomEntityIronGolem(w3);
            irongolem.setPosition(location.getX(), location.getY(), location.getZ());
            w3.addEntity(irongolem, SpawnReason.CUSTOM);
            
            //LivingEntity le = (LivingEntity) irongolem;
            
            UUID eID = irongolem.getUniqueID();
            
            
            //UUID eID = newEnt.getUniqueId();
            if (Game.mobID.containsKey(eID))
            {
                Game.mobID.remove(eID);
            }
            Game.mobID.put(eID, ID);
            
            //if (Game.leID.contains(le))
            //{
            //    Game.leID.remove(le);
            //}
           // Game.leID.add(le);
            
            if (Game.igID.contains(irongolem))
            {
                Game.igID.remove(irongolem);
            }
            Game.igID.add(irongolem);
            
            double setGolemHP = Math.ceil((375 + ((50 * round) / 3) * (0.25 * round)) * 1.33);
           // SRKF.LOG.info("Flesh Pound HP: " + setGolemHP);
            irongolem.setHealth((float) setGolemHP);
            Game.mobHealth.put(eID, setGolemHP);
            
            
            SRKF.LOG.log(Level.INFO, "Flesh pound HP is: {0} - on round: {1}", new Object[]{irongolem.getHealth(), round});

        }
        
        return canSpawn;
    }

    /*
    public void changeIntoNormal(Skeleton skeleton, boolean giveRandomEnchantments)
    {
        EntitySkeleton ent = ((CraftSkeleton)skeleton).getHandle();
        try {
            ent.setSkeletonType(0);
            Method be = EntitySkeleton.class.getDeclaredMethod("bE");
            be.setAccessible(true);
            be.invoke(ent);
            if (giveRandomEnchantments){
                Method bf = EntityLiving.class.getDeclaredMethod("bF");
                bf.setAccessible(true);
                bf.invoke(ent);
            }
            Field selector = EntitySkeleton.class.getDeclaredField("goalSelector");
            selector.setAccessible(true);
            Field d = EntitySkeleton.class.getDeclaredField("d");
            d.setAccessible(true);
            PathfinderGoalSelector goals = (PathfinderGoalSelector) selector.get(ent);
            goals.a(4, (PathfinderGoal) d.get(ent));
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
    }
    */
}
