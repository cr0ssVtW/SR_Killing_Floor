package sr.Tasks;

import net.minecraft.server.v1_7_R1.EntityCreature;
import net.minecraft.server.v1_7_R1.Navigation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import sr.SRKF;

/**
 *
 * @author Cross
 */
public class mobMoveTask implements Runnable
{
    public SRKF plugin;
    EntityCreature ec;
    LivingEntity le;
    Player player;
    int i;
    int taskID;
               
    public mobMoveTask(SRKF plugin, EntityCreature ec, Player player, LivingEntity le) 
    {
        this.plugin = plugin;
        this.ec = ec;
        this.le = le;
        this.player = player;
        i = 0;
        this.taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, 5L, 5L);
    }
     
    public void run() 
    {

        i = i + 1;

        if(i > 40)
        {
            Bukkit.getScheduler().cancelTask(taskID);
        }
        else
        {
            if (le.isDead())
            {
                Bukkit.getScheduler().cancelTask(taskID);
            }

            if(!(le.isDead()) && player.isOnline())
            {
                Location permloc = le.getLocation();
                Location mloc = le.getLocation();
                Location ploc = player.getLocation();

                if (mloc.getWorld().equals(ploc.getWorld()))
                {
                    if(mloc.distance(ploc) < 64)
                    {
                        Navigation nav = ec.getNavigation();
                        nav.a(ploc.getX(), ploc.getY(), ploc.getZ(), 1.5f);
                    }
                    else
                    {
                        double mlocx = mloc.getX();
                        double mlocy = mloc.getY();
                        double mlocz = mloc.getZ();

                        double plocx = ploc.getX();
                        double plocy = ploc.getY();
                        double plocz = ploc.getZ();

                        double newx = (mlocx + plocx)/2;
                        double newy = (mlocy + plocy)/2;
                        double newz = (mlocz + plocz)/2;

                        Location newloc = new Location(le.getWorld(),newx,newy,newz);

                        boolean havenewloc = false;
                        while(havenewloc == false)
                        {

                            if(newloc.distance(mloc) < 15)
                            {
                                havenewloc = true;
                            }
                            else
                            {
                                newx = (newx + mlocx)/2;
                                newy = (newy + mlocy)/2;
                                newz = (newz + mlocz)/2;

                                newloc = new Location(le.getWorld(),newx,newy,newz);
                            }
                        }

                        Navigation nav = ec.getNavigation();
                        nav.a(newloc.getX(), newloc.getY(), newloc.getZ(), 1.5f);


                    }
                }
                

            }
            else
            {
                Bukkit.getScheduler().cancelTask(taskID);
            }
        }
    }
}
