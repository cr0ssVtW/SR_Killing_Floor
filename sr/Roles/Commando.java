package sr.Roles;

import java.util.HashMap;
import sr.SRKF;

/**
 *
 * @author Cross
 */
public class Commando 
{
    public SRKF plugin;
    
    public Commando (SRKF plugin)
    {
        this.plugin = plugin;
    }
    
    public static HashMap<String, Integer> Stats_RangerInstructor = new HashMap<>();
    public static HashMap<String, Integer> Stats_Pain = new HashMap<>();
    public static HashMap<String, Integer> Stats_Juggernaut = new HashMap<>();
    public static HashMap<String, Integer> Stats_Inspire = new HashMap<>();
    
    
    
    public static double getRangerInstructorBonusAmount(String name)
    {
        double amount = 0.03;
        int level = 0;
        if (Stats_RangerInstructor.containsKey(name))
        {
            level = Stats_RangerInstructor.get(name);
        }
        
        double bonusAmount = 1 + (amount * level);
        
        return bonusAmount;
    }
    
    public static double getPainBonusAmount(String name)
    {
        double amount = 0;
        int level = 0;
        if (Stats_Pain.containsKey(name))
        {
            level = Stats_Pain.get(name);
        }
        
        double bonusAmount = amount + level;
        
        return bonusAmount;
    }
    
    
    public static double getJuggernautBonusAmount(String name)
    {
        double amount = 0;
        int level = 0;
        if (Stats_Juggernaut.containsKey(name))
        {
            level = Stats_Juggernaut.get(name);
        }
        
        double bonusAmount = amount + level;
        
        return bonusAmount;
    }
    
    public static int getInspireBonusDurationAmount(String name)
    {
        int baseamount = 20;
        int level = 0;
        
        if (Stats_Inspire.containsKey(name))
        {
            level = Stats_Inspire.get(name);
        }
        
        int bonusAmount = baseamount * level;
        
        return bonusAmount;
    }
    
    public static int getInspireBonusFrequencyAmount(String name)
    {
        int baseamount = 1200;
        int tick = 20;
        int level = 0;
        
        if (Stats_Inspire.containsKey(name))
        {
            level = Stats_Inspire.get(name);
        }
        
        int bonusAmount = baseamount - (level * tick);
        
        return bonusAmount;
    }
}
