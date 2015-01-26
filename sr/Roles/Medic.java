package sr.Roles;

import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import sr.SRKF;

/**
 *
 * @author Cross
 */
public class Medic 
{
    public SRKF plugin;
    
    public Medic (SRKF plugin)
    {
        this.plugin = plugin;
    }
    
    public static double baseMedKit = 7;
    public static double baseTriageAmount = 7;
    
    public static HashMap<String, Integer> Stats_MedKit = new HashMap<>();
    public static HashMap<String, Integer> Stats_Shotguns = new HashMap<>();
    public static HashMap<String, Integer> Stats_Triage = new HashMap<>();
    public static HashMap<String, Integer> Stats_CombatArmor = new HashMap<>();
    
    
    
    public static double getShotgunBonusAmount(String name)
    {
        double amount = 0;
        int level = 0;
        if (Stats_Shotguns.containsKey(name))
        {
            level = Stats_Shotguns.get(name);
        }
        double bonusAmount = amount + level;
        
        return bonusAmount;
    }
    
    public static double getHealBonusAmount(String name)
    {
        double amount = 0;
        int level = 0;
        if (Stats_MedKit.containsKey(name))
        {
            level = Stats_MedKit.get(name);
        }
        double bonusAmount = amount + level;
        
        return bonusAmount;
    }
    
    public static double getTriageBonusAmount(String name)
    {
        double amount = 2;
        int level = 0;
        if (Stats_Triage.containsKey(name))
        {
            level = Stats_Triage.get(name);
        }
        
        double bonusAmount = amount * level;
        
        return bonusAmount;
    }
    
    public static int getCombatArmorBonusAmount(String name)
    {
        int level = 0;
        
        if (Stats_CombatArmor.containsKey(name))
        {
            level = Stats_CombatArmor.get(name);
        }
        
        return level;
    }
}
