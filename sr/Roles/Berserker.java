package sr.Roles;

import java.util.HashMap;
import sr.SRKF;

/**
 *
 * @author Cross
 */
public class Berserker 
{
    public SRKF plugin;
    
    public Berserker (SRKF plugin)
    {
        this.plugin = plugin;
    }
    
    public static HashMap<String, Integer> Stats_Endurance = new HashMap<>();
    public static HashMap<String, Integer> Stats_Strength = new HashMap<>();
    public static HashMap<String, Integer> Stats_KevlarAdaptation = new HashMap<>();
    public static HashMap<String, Integer> Stats_Ferocity = new HashMap<>();
    
    
    
    public static int getEnduranceBonusAmount(String name)
    {
        int speedBoostLevel = 0;
        
        double amount = 0.34;
        int level = 0;
        if (Stats_Endurance.containsKey(name))
        {
            level = Stats_Endurance.get(name);
        }
        double bonusAmount = amount * level;
        
        if (bonusAmount >= 1)
        {
            speedBoostLevel = 1;
        }
        if (bonusAmount >= 2)
        {
            speedBoostLevel = 2;
        }
        if (bonusAmount >= 3)
        {
            speedBoostLevel = 3;
        }
        if (bonusAmount >= 4)
        {
            speedBoostLevel = 4;
        }
        
        return speedBoostLevel;
    }
    
    public static double getStrengthBonusAmount(String name)
    {
        double amount = 0;
        int level = 0;
        if (Stats_Strength.containsKey(name))
        {
            level = Stats_Strength.get(name);
        }
        double bonusAmount = amount + level;
        
        return bonusAmount;
    }
    
    public static double getKevlarAdaptationBonusAmount(String name)
    {
        double amount = 0.05;
        int level = 0;
        if (Stats_KevlarAdaptation.containsKey(name))
        {
            level = Stats_KevlarAdaptation.get(name);
        }
        
        double bonusAmount = 1 - (amount * level);
        
        return bonusAmount;
    }
    
    public static double getFerocityBonusAmount(String name, double maxHealth, double health)
    {
        double baseamount = 1.5;
        int level = 0;
        
        if (Stats_Ferocity.containsKey(name))
        {
            level = Stats_Ferocity.get(name);
        }
        
        double healthbonus = 0;
        
        if (health < maxHealth)
        {
            double diff = maxHealth - health;
            double usable = Math.floor(diff / 2);
            
            healthbonus = (level * 0.05) * usable;
        }
        
        
        double bonusAmount = (baseamount) + (healthbonus); 
        
        return bonusAmount;
    }
}
