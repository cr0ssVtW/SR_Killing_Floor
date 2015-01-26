package sr.Roles;

import java.util.HashMap;
import sr.SRKF;

/**
 *
 * @author Cross
 */
public class Engineer 
{
    public SRKF plugin;
    
    public Engineer (SRKF plugin)
    {
        this.plugin = plugin;
    }
    
    public static HashMap<String, Integer> Stats_BallisticTraining = new HashMap<>();
    public static HashMap<String, Integer> Stats_Welding = new HashMap<>();
    public static HashMap<String, Integer> Stats_DemolitionMastery = new HashMap<>();
    public static HashMap<String, Integer> Stats_HeavyArtillery = new HashMap<>();
    
    
    
    public static int getBallisticTrainingBonusAmount(String name)
    {
        double amount = 0.5;
        int level = 0;
        if (Stats_BallisticTraining.containsKey(name))
        {
            level = Stats_BallisticTraining.get(name);
        }
        
        double bonusAmount = Math.floor(amount * level);
        int bonus = (int) bonusAmount;
        return bonus;
    }
    
    public static int getWeldingBonusAmount(String name)
    {
        int amount = 1;
        int level = 0;
        if (Stats_Welding.containsKey(name))
        {
            level = Stats_Welding.get(name);
        }
        
        int bonusAmount = amount * level;
        
        return bonusAmount;
    }
    
    public static double getDemoltionMasteryBonusAmount(String name)
    {
        double amount = 0;
        int level = 0;
        if (Stats_DemolitionMastery.containsKey(name))
        {
            level = Stats_DemolitionMastery.get(name);
        }
        
        double bonusAmount = amount + level;
        
        return bonusAmount;
    }
    
    public static double getHeavyArtilleryBonusAmount(String name)
    {
        double baseamount = 3;
        int level = 0;
        
        if (Stats_HeavyArtillery.containsKey(name))
        {
            level = Stats_HeavyArtillery.get(name);
        }
        
        double bonusAmount = baseamount * level;
        
        return bonusAmount;
    }
}
