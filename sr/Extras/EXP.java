package sr.Extras;

import java.util.HashMap;
import java.util.TreeMap;
import org.bukkit.entity.EntityType;
import sr.SRKF;

/**
 *
 * @author Cross
 */
public class EXP 
{
    public SRKF plugin;
    public EXP (SRKF plugin)
    {
        this.plugin = plugin;
    }
    
    public static HashMap<String, Integer> playerLevel = new HashMap<>();
    public static HashMap<String, Integer> playerEXP = new HashMap<>();
    
    public static TreeMap<Integer, Integer> levelAmounts = new TreeMap<>();
    static
    {
        levelAmounts.put(1,0);
        levelAmounts.put(1000, 2);
        levelAmounts.put(2777, 3);
        levelAmounts.put(5697, 4);
        levelAmounts.put(10248, 5);
        levelAmounts.put(17031, 6);
        levelAmounts.put(26784, 7);
        levelAmounts.put(40391, 8);
        levelAmounts.put(58895, 9);
        levelAmounts.put(83511, 10);
        levelAmounts.put(115645, 11);
        levelAmounts.put(156898, 12);
        levelAmounts.put(209088, 13);
        levelAmounts.put(274259, 14);
        levelAmounts.put(354692, 15);
        levelAmounts.put(452925, 16);
        levelAmounts.put(571762, 17);
        levelAmounts.put(714286, 18);
        levelAmounts.put(883872, 19);
        levelAmounts.put(1084206, 20);
        levelAmounts.put(1319289, 21);
        levelAmounts.put(1593459, 22);
        levelAmounts.put(1911400, 23);
        levelAmounts.put(2278153, 24);
        levelAmounts.put(2699136, 25);
    }
    
    public static HashMap<Integer, Integer> levelCosts = new HashMap<>();
    static
    {
        levelCosts.put(1,0);
        levelCosts.put(1000, 2);
        levelCosts.put(2777, 3);
        levelCosts.put(5697, 4);
        levelCosts.put(10248, 5);
        levelCosts.put(17031, 6);
        levelCosts.put(26784, 7);
        levelCosts.put(40391, 8);
        levelCosts.put(58895, 9);
        levelCosts.put(83511, 10);
        levelCosts.put(115645, 11);
        levelCosts.put(156898, 12);
        levelCosts.put(209088, 13);
        levelCosts.put(274259, 14);
        levelCosts.put(354692, 15);
        levelCosts.put(452925, 16);
        levelCosts.put(571762, 17);
        levelCosts.put(714286, 18);
        levelCosts.put(883872, 19);
        levelCosts.put(1084206, 20);
        levelCosts.put(1319289, 21);
        levelCosts.put(1593459, 22);
        levelCosts.put(1911400, 23);
        levelCosts.put(2278153, 24);
        levelCosts.put(2699136, 25);
    }
    
    public static HashMap<EntityType, Integer> mobValue = new HashMap<>();
    static
    {
        mobValue.put(EntityType.ZOMBIE, 24);
        mobValue.put(EntityType.SPIDER, 32);
        mobValue.put(EntityType.IRON_GOLEM, 250);
        mobValue.put(EntityType.SKELETON, 44);
        mobValue.put(EntityType.CREEPER, 44);
        mobValue.put(EntityType.PIG_ZOMBIE, 60);
    }
    
    public static Integer getClosestLevel(TreeMap<Integer, Integer> levelMap, Integer exp)
    {
        return levelMap.ceilingKey(levelMap.headMap(exp, true).lastKey());
    }
}
