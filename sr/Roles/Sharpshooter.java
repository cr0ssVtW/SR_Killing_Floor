package sr.Roles;

import java.util.HashMap;
import org.bukkit.Bukkit;
import sr.SRKF;

/**
 *
 * @author Cross
 */
public class Sharpshooter 
{
    public SRKF plugin;
    
    public Sharpshooter (SRKF plugin)
    {
        this.plugin = plugin;
    }
    
    public static HashMap<String, Integer> Stats_MarksmanTraining = new HashMap<>();
    public static HashMap<String, Integer> Stats_CalledShot = new HashMap<>();
    public static HashMap<String, Integer> Stats_Lucky = new HashMap<>();
    public static HashMap<String, Integer> Stats_Wound = new HashMap<>();
    
    
    
    public static double getMarksmanTrainingBonusAmount(String name)
    {
        double amount = 0.05;
        int level = 0;
        if (Stats_MarksmanTraining.containsKey(name))
        {
            level = Stats_MarksmanTraining.get(name);
        }
        
        double bonusAmount = (double) 1 - (amount * level);
        
        return bonusAmount;
    }
    
    public static double getCalledShotBonus(String name)
    {
        double amount = 0.08;
        int level = 0;
        if (Stats_CalledShot.containsKey(name))
        {
            level = Stats_CalledShot.get(name);
        }
        
        double bonusAmount = amount * level;
        
        return bonusAmount;
    }
    
    public static double getLuckyBonusAmount(String name)
    {
        double amount = 0.01;
        int level = 0;
        if (Stats_Lucky.containsKey(name))
        {
            level = Stats_Lucky.get(name);
        }
        
        double bonusAmount = amount * level;
        
        return bonusAmount;
    }
    
    public static double getWoundBonusAmount(String name)
    {
        double baseamount = 0.05;
        int level = 0;
        
        if (Stats_Wound.containsKey(name))
        {
            level = Stats_Wound.get(name);
        }
        
        double bonusAmount = (baseamount) + (double)((double)level / 100);
        
        return bonusAmount;
    }
}
