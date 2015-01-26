package sr.Utilities;

import org.bukkit.util.Vector;

/**
 *
 * @author Cross
 */
public class AngleUtils 
{
    public static double maxAngle = 90;
    
    public static double getAngle(Vector vec1, Vector vec2)
    {
        return vec1.angle(vec2) * 180.0F / 3.141592653589793D;
    }
}
