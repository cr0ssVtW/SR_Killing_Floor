package sr;

import java.util.HashMap;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import sr.Listeners.PL;

/**
 *
 * @author Cross
 */
public class roundChanger implements Runnable
{
    public SRKF plugin;
    
    int round;
    int ID;
    
    int taskID;
    public roundChanger(SRKF plugin, int ID, int round)
    {
        this.round = round;
        this.ID = ID;
        this.plugin = plugin;
        
        this.taskID = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, this, 0);
        
    }

    @Override
    public void run() 
    {
        if (Game.mapRound.containsKey(ID))
        {
            round = Game.mapRound.get(ID);
            Game.mapRound.remove(ID);
        }
        
        if (Game.timeoutCheck.containsKey(ID))
        {
            Game.timeoutCheck.remove(ID);
        }
        
        
        
        String worldName = "bleh";
        if (Game.gameIDWorldName.containsKey(ID))
        {
            worldName = Game.gameIDWorldName.get(ID);
        }
        
        if (!(worldName.equalsIgnoreCase("bleh")))
        {
            World world = Bukkit.getWorld(worldName);
            world.setTime(13000);
        }
        
        HashMap<String, Double> coinCopy = new HashMap<>(Game.playerCoins);
        for (String name : Game.playersMaps.keySet())
        {
            int id = Game.playersMaps.get(name);
            if (id == this.ID)
            {
                Player player = Bukkit.getPlayer(name);
                
                double coins = 0;
                if (coinCopy.containsKey(player.getName()))
                {
                    coins = coinCopy.get(player.getName());
                    Game.playerCoins.remove(player.getName());
                }
                coins = coins + (round * 2);
                
                Game.playerCoins.put(player.getName(), coins);
                player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " 
                        + ChatColor.GOLD + "Earned " + ChatColor.GREEN + (round * 2) + ChatColor.GOLD + " coins last round. Total this game: " + ChatColor.GREEN + coins);
            } 
        }

        round++;
        SRKF.LOG.log(Level.INFO, "[SRKF] - New Round Change Request for ID: {0}. Round is: {1} changing to :{2}", new Object[]{ID, round, round + 1});
        
        Game.mapRound.put(ID, round);
        
        for (String name : Game.playersMaps.keySet())
        {
            int id = Game.playersMaps.get(name);
            if (id == this.ID)
            {
                Player player = Bukkit.getPlayer(name);
                player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.GOLD + "Round " + round + " begins!");
                
            } 
        }
        
        
        if (Game.mapIDSpawned.containsKey(ID))
        {
            Game.mapIDSpawned.remove(ID);
        }
        
        if (Game.isRoundChanging.containsKey(ID))
        {
            Game.isRoundChanging.remove(ID);
        }
        
        if (Game.mapIDSpawnedLimit.containsKey(ID))
        {
            Game.mapIDSpawnedLimit.remove(ID);
        }
        
        if (Game.mapSkipAmount.containsKey(ID))
        {
            Game.mapSkipAmount.remove(ID);
        }
        
        HashMap<String, Integer> roundSkipCopy = new HashMap<>(Game.gameRoundSkip);
        for (String pName : roundSkipCopy.keySet())
        {
            int gameID = roundSkipCopy.get(pName);
            
            if (ID == gameID)
            {
                Game.gameRoundSkip.remove(pName);
            }
        }
        
        if (PL.mapKills.containsKey(ID))
        {
            PL.mapKills.remove(ID);
        }
        
        
    }
}
