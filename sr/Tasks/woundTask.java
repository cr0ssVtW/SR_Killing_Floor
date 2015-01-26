package sr.Tasks;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import sr.Game;
import sr.Roles.Sharpshooter;
import sr.SRKF;

/**
 *
 * @author Cross
 */
public class woundTask implements Runnable
{
    public SRKF plugin;
    private LivingEntity le;
    private int taskID;
    private int x;
    private Player shooter;
    
    public woundTask(SRKF plugin, Player shooter, LivingEntity le)
    {
        this.plugin = plugin;
        this.le = le;
        this.shooter = shooter;
        this.taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, 200, 10);
        this.x = 0;
    }

    @Override
    public void run() 
    {
        x++;
        
        if (x > 10 || le.isDead() || (!(shooter.isOnline())) )
        {
            if (Game.hasWound.contains(le.getUniqueId()))
            {
                Game.hasWound.remove(le.getUniqueId());
            }
            Bukkit.getScheduler().cancelTask(taskID);
        }
        int level = 0;
        
        if (Sharpshooter.Stats_Wound.containsKey(shooter.getName()))
        {
            level = Sharpshooter.Stats_Wound.get(shooter.getName());
        }
        
        if (level > 0)
        {
            // get stats
            double maxHealth = le.getMaxHealth();

            double getWoundAmount = Sharpshooter.getWoundBonusAmount(shooter.getName());

            double dmgAmount = (maxHealth * getWoundAmount) / 2;

            // do bleed

            le.damage(dmgAmount, shooter);
            le.getWorld().playEffect(le.getLocation(), Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
        }
        else
        {
            if (Game.hasWound.contains(le.getUniqueId()))
            {
                Game.hasWound.remove(le.getUniqueId());
            }
            Bukkit.getScheduler().cancelTask(taskID);
        }
        
        
    }
}
