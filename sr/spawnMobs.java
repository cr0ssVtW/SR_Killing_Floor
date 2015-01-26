package sr;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import sr.Listeners.PL;
import sr.ShopStuff.shopCreate;

/**
 *
 * @author Cross
 */
public class spawnMobs implements Runnable
{
    public SRKF plugin;
    
    int ID;
    Location min;
    Location max;
    static World world;
    int taskID;

    int clock;
    
    Block minblock;
    Block maxblock;
    int x1;
    int y1;
    int z1;
    int x2;
    int y2;
    int z2;
    
    String mapName;
    int mobSpawnCount;
    String mapID;
    
    int timeoutTime = 120;
    
    public spawnMobs(SRKF plugin, int ID, Location min, Location max)
    {
        this.plugin = plugin;
        this.ID = ID;
        this.min = min;
        this.max = max;
        this.world = min.getWorld();
        this.taskID = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, this, 0, 10);
        if (Game.gameWarmup.contains(ID) || Game.gameStarted.contains(ID))
        {
            Bukkit.getServer().getScheduler().cancelTask(this.taskID);
            SRKF.LOG.log(Level.WARNING, "[SRKF] - Cancelled task spawnMobs for ID: {0} because ID already exists in gameWarmup/gameStarted", ID);
        }
        else
        {
            Game.gameStarted.add(this.ID);
            SRKF.LOG.log(Level.INFO, "[SRKF] - Spawns Started for ID: {0}", ID);
        }
        this.mapName = SRKF.dbManager.getMapName(ID);
        this.mobSpawnCount = SRKF.dbManager.getMobSpawnCount(ID, mapName);
        this.mapID = mapName + "_" + ID;
        
        this.minblock = this.min.getBlock();
        this.maxblock = this.max.getBlock();
        
        this.x1 = this.minblock.getX();
        this.y1 = this.minblock.getY();
        this.z1 = this.minblock.getZ();
        
        this.x2 = this.maxblock.getX();
        this.y2 = this.maxblock.getY();
        this.z2 = this.maxblock.getZ();
        
        this.clock = 0;
    }

    @Override
    public void run() 
    {
        /*
         * Check if players left in Game
         * do cleanup
         */
        
        if (!(Game.playersMaps.containsValue(ID)))
        {
            Bukkit.getScheduler().cancelTask(this.taskID); // cancel task as map is clear of players
            if (Game.gameMobCheckerHash.containsKey(ID))
            {
                int mobCheckerID = Game.gameMobCheckerHash.get(ID);
                Bukkit.getScheduler().cancelTask(mobCheckerID); // cancel mobChecker for this go through.
            }
            
            int status = 2; // cleaning status
            SRKF.dbManager.setMapStatus(ID, status);
            
            if (Game.isRoundChanging.containsKey(ID))
            {
                Game.isRoundChanging.remove(ID);
            }
            
            if (Game.mapIDSpawned.containsKey(ID))
            {
                Game.mapIDSpawned.remove(ID);
            }
            
            if (Game.mapRound.containsKey(ID))
            {
                Game.mapRound.remove(ID);
            }
            
            if (PL.mapKills.containsKey(ID))
            {
                PL.mapKills.remove(ID);
            }
            
            HashMap<UUID, Integer> hashcopy = new HashMap<>(Game.mobID);
            for (UUID uid : hashcopy.keySet())
            {
                int gameID = Game.mobID.get(uid);
                
                if (gameID == ID)
                {
                    Game.mobID.remove(uid);
                }
            }
            
            /*
             * 
             * 
             * do cleanup for all players in this game kills log to database
             * 
             * 
             */
            
            
            
            World gameWorld = min.getWorld();
            int removecount = 0;
            
            for (Entity e : gameWorld.getEntities())
            {
                if (e instanceof LivingEntity)
                {
                    LivingEntity le = (LivingEntity) e;
                    if (!(le instanceof Player))
                    {
                        le.remove();
                        removecount++;
                    }
                }
                else
                {
                    e.remove();
                    removecount++;
                }
            }

            
            SRKF.LOG.log(Level.INFO, "[SRKF] - Cleaned up ID: " + this.ID + " and removed " + removecount + " entities.");
            
            if (Game.gameStarted.contains(ID))
            {
                Game.gameStarted.remove(ID);
                SRKF.LOG.log(Level.INFO, "[SRKF] - Removed Game ID: " + this.ID + " from gameStarted hash.");
            }
            
            SRKF.dbManager.setMapPlayers(ID, 0);
            
            status = 3; // ready to join
            SRKF.dbManager.setMapStatus(ID, status);
        }
        
        /*
         * End cleanup.
         * 
         * Start game stuff.
         */
        
        clock++;
        // game begins
        if (Game.gameStarted.contains(ID))
        {
            int thisRound = 0;
            
            if (Game.mapRound.containsKey(ID))
            {
                thisRound = Game.mapRound.get(ID);
            }
            int playerCount = 0;
            
            for (int gameID : Game.playersMaps.values())
            {
                if (gameID == ID)
                {
                    playerCount++;
                }
            }
            
            double firstExp = Math.pow(thisRound, 1.5);
            double secondExp = Math.pow(3.5, playerCount);
            double thirdExp = Math.pow(3.7, playerCount);
            
            int form = (int) Math.floor((firstExp + thisRound + 3) * (playerCount * ((secondExp)/(thirdExp))));
            int limitMax = form;
            int roundSpawnLimit = form;
            
            if (Game.mapIDSpawnedLimit.containsKey(ID))
            {
                roundSpawnLimit = Game.mapIDSpawnedLimit.get(ID);
            }
            else
            {
                Game.mapIDSpawnedLimit.put(ID, limitMax);
            }
            
            //SRKF.LOG.info("roundSpawnLimit: " + roundSpawnLimit);
            
            if (Game.isRoundChanging.containsKey(ID))
            {
                int oldClock = Game.isRoundChanging.get(ID);
                int roundChangeTime = oldClock + Game.roundChangeTime;
                
                if (clock >= roundChangeTime)
                {
                    Game.isRoundChanging.remove(ID);
                    new roundChanger(plugin, ID, thisRound);
                }
                
                //SRKF.LOG.log(Level.INFO, "[SRKF] - Changing Rounds on ID: " + ID + ". Halting spawns.");
                
                return;
                
            }
            
            int spawncount = 0;
            
            if (Game.mapIDSpawned.containsKey(ID))
            {
                spawncount = Game.mapIDSpawned.get(ID);
            }
            
            
            //SRKF.LOG.log(Level.INFO, "[SRKF] - SpawnCount is: " + spawncount);
            
            if (spawncount >= roundSpawnLimit)
            {
                // do checks for all mobs dead
                if (PL.mapKills.containsKey(ID))
                {
                    int killCount = PL.mapKills.get(ID);
                    
                    //Bukkit.broadcastMessage("roundSpawnLimit is: " + roundSpawnLimit);
                    //Bukkit.broadcastMessage("killCount is: " + killCount);
                    
                    if (killCount >= roundSpawnLimit)
                    {
                        if (Game.isRoundChanging.containsKey(ID))
                        {
                            Game.isRoundChanging.remove(ID);
                        }
                        Game.isRoundChanging.put(ID, clock);
                        new shopCreate(plugin, ID);
                        
                        
                        double rand = Math.random();
                        String msg = "";

                        if (rand >= 0 && rand < 33)
                        {
                            msg = ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.DARK_PURPLE + "A voice shouts, " + ChatColor.GOLD + "\"Hey! Need supplies? Come over here, but be fast.\"";
                        }
                        if (rand >= 33 && rand < 66)
                        {
                            msg = ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.DARK_PURPLE + "A voice shouts, " + ChatColor.GOLD + "\"Get your gear, quick!\"";
                        }
                        if (rand >= 66)
                        {
                            msg = ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.DARK_PURPLE + "A voice shouts, " + ChatColor.GOLD + "\"I gotta get out of here soon. Hurry!\"";
                        }
                        
                        for (String name : Game.playersMaps.keySet())
                        {
                            int id = Game.playersMaps.get(name);
                            if (id == ID)
                            {
                                Player player = Bukkit.getPlayer(name);
                                player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.GOLD + "The next round will begin in " + (Game.roundChangeTime / 2) + " seconds.");
                                player.sendMessage(msg);
                            }
                        }
                        return;
                    }
                    
                    if (killCount >= (int) (Math.floor((double) roundSpawnLimit * 0.67)))
                    {
                        int oldClock = 0;
                
                        // do checks for timeout.
                        if (!(Game.timeoutCheck.containsKey(ID)))
                        {
                            Game.timeoutCheck.put(ID, clock);
                        }
                        else
                        {
                            oldClock = Game.timeoutCheck.get(ID);
                            //Bukkit.broadcastMessage("clock is: " + clock);
                            //Bukkit.broadcastMessage("oldClock is: " + oldClock);

                            if (clock >= (oldClock + timeoutTime))
                            {
                                if (Game.isRoundChanging.containsKey(ID))
                                {
                                    Game.isRoundChanging.remove(ID);
                                }
                                Game.isRoundChanging.put(ID, clock);
                                new shopCreate(plugin, ID);
                                
                                double rand = Math.random();
                                String msg = "";
                                
                                if (rand >= 0 && rand < 33)
                                {
                                    msg = ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.DARK_PURPLE + "A voice shouts, " + ChatColor.GOLD + "\"Hey! Need supplies? Come over here, but be fast.\"";
                                }
                                if (rand >= 33 && rand < 66)
                                {
                                    msg = ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.DARK_PURPLE + "A voice shouts, " + ChatColor.GOLD + "\"Get your gear, quick!\"";
                                }
                                if (rand >= 66)
                                {
                                    msg = ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.DARK_PURPLE + "A voice shouts, " + ChatColor.GOLD + "\"I gotta get out of here soon. Hurry!\"";
                                }
                                
                                for (String name : Game.playersMaps.keySet())
                                {
                                    int id = Game.playersMaps.get(name);
                                    if (id == ID)
                                    {
                                        Player player = Bukkit.getPlayer(name);
                                        player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.GOLD + "Round ended due to timeout.");
                                        player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.GOLD + "The next round will begin in " + (Game.roundChangeTime / 2) + " seconds.");
                                        player.sendMessage(msg);
                                    }
                                }

                                return;
                            }
                        }
                    }
                }
                return; // stop spawning
            }
            
            //SRKF.LOG.log(Level.INFO, "[SRKF] - Spawned Mob for ID: " + ID + " on round. " + thisRound + " | with spawn limit " + roundSpawnLimit);
            
            Random rand = new Random();
            
            int thisSpawnNum = rand.nextInt(mobSpawnCount) + 1;
            
            Location spawnLoc = null;
            String locKey = mapID + "_" + thisSpawnNum;
            if (Game.mobSpawns.containsKey(locKey))
            {
                spawnLoc = Game.mobSpawns.get(locKey);
            }
            
            if (spawnLoc != null)
            {
                /*
                 * Do Mob Spawns
                 */
                 String potentialPlayer = getRandomPlayerName(ID);
                 new MobSpawn(plugin, spawnLoc, ID, spawncount, thisRound, potentialPlayer);
                 
            }
            else
            {
                SRKF.LOG.log(Level.SEVERE, "[SRKF] - Map ID: " + this.ID + " failed to spawn mob for spawnLoc: " + locKey + ". Location is null.");
            }
        }
        
    }
    
    
    private String getRandomPlayerName(int ID)
    {
        HashMap<String, Integer> copy = new HashMap<>(Game.playersMaps);
        String name = "bleh";
        
        int playerPool = 0;
        
        for (int gameID : copy.values())
        {
            if (gameID == ID)
            {
                playerPool = playerPool + 1;
            }
        }
        
        Random r = new Random();
        int luckyPlayer = r.nextInt(playerPool) + 1;
        int pCount = 0;
        
        for (String s : copy.keySet())
        {
            int gameID = copy.get(s);
            
            if (gameID == ID)
            {
                pCount = pCount + 1;
                if (pCount == luckyPlayer)
                {
                    name = s;
                    break;
                }
            }
            
        }
        
        return name;
    }
    
    
}
