package sr.Tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import sr.Game;
import sr.SRKF;

/**
 *
 * @author Cross
 */
public class commandoInspireTask implements Runnable
{
    private SRKF plugin;
    private int durationTimeBonus;
    private int reuseTime;
    private Player player;
    private int taskID;
    
    private int baseDuration = 300;
    private int baseReuse = 1200;
    private int totalDuration;
    public commandoInspireTask(SRKF plugin, Player player, int durationTimeBonus, int reuseTime)
    {
        this.player = player;
        this.plugin = plugin;
        this.durationTimeBonus = durationTimeBonus;
        this.reuseTime = reuseTime;
        
        this.totalDuration = baseDuration + durationTimeBonus;
        this.taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, reuseTime, reuseTime);
    }

    @Override
    public void run() 
    {
        if (!(player.isOnline()))
        {
            Bukkit.getScheduler().cancelTask(taskID);
        }
        
        double random = Math.random();
        PotionEffectType type = PotionEffectType.SPEED;
        if (random >= 0 && random < 0.33)
        {
            type = PotionEffectType.SPEED;
        }
        if (random >= 0.33 && random < 0.66)
        {
            type = PotionEffectType.DAMAGE_RESISTANCE;
        }
        if (random >= 0.66)
        {
            type = PotionEffectType.INCREASE_DAMAGE;
        }
        
        for (Player p : Bukkit.getOnlinePlayers())
        {
            if (p.getWorld().equals(player.getWorld()))
            {
                if (p.getLocation().distance(player.getLocation()) < 32)
                {
                    if (type == PotionEffectType.SPEED)
                    {
                        if (Game.playerClass.containsKey(p.getName()))
                        {
                            String role = Game.playerClass.get(p.getName());
                            if (!(role.equalsIgnoreCase("Berserker")))
                            {
                                p.addPotionEffect(new PotionEffect(type, totalDuration, 0), true);
                            }
                        }
                    }
                    else
                    {
                        p.addPotionEffect(new PotionEffect(type, totalDuration, 0), true);
                    }
                }
            }
        }
    }
    
}
