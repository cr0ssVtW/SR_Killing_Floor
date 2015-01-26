package sr;

import org.bukkit.Location;

/**
 *
 * @author Cross
 */
public class Map 
{
    Location min;
    Location max;
    
    public Map(Location min, Location max)
    {
        this.max = max;
        this.min = min;
    }
    
    public boolean containsBlock(Location loc)
    {
        if (loc.getWorld() != this.min.getWorld())
        {
            return false;
        }
        
        double x = loc.getX();
        double y = loc.getY();
        double z = loc.getZ();
        
        return (x >= this.min.getBlockX()) && (x < this.max.getBlockX() + 1) &&
                (y >= this.min.getBlockY()) && (y < this.max.getBlockY() + 1) &&
                (z >= this.min.getBlockZ()) && (z < this.max.getBlockZ() + 1);
    }
    
    public Location getMax()
    {
        return this.max;
    }
    
    public Location getMin()
    {
        return this.min;
    }
}
