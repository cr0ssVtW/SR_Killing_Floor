package sr.Listeners;

import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.event.Listener;
import sr.SRKF;

/**
 *
 * @author Cross
 */
public class SignListener implements Listener
{
    public SRKF plugin;
    
    public static int maxPlayers = 10;
    public static HashMap<Integer, Block> signLocs = new HashMap<>();
    
    public SignListener(SRKF plugin)
    {
        this.plugin = plugin;
    }
    
    public static void loadSigns(int ID, Block b)
    {
        signLocs.put(ID, b);
        SRKF.LOG.log(Level.INFO, "[SRKF] - Loaded Sign ID: {0} from SQL database.", ID);
    }
    
    public static void updateSigns(int ID, int Status, String mapName, int Players)
    {
        Iterator<Integer> it = signLocs.keySet().iterator();
        
        String status = "";
        
        while (it.hasNext())
        {
            int id = it.next();
            if (id == ID)
            {   
                Block b = signLocs.get(id);
                
                BlockState state = b.getState();
                
                if (state instanceof Sign)
                {
                    Sign sign = (Sign) state;
                    
                    if (Status == 3)
                    {
                        status = "Join up!";
                    }

                    if (Status == 2)
                    {
                        status = "Cleaning...";
                    }

                    if (Status == 1)
                    {
                        status = "In Progress!";
                    }

                    sign.setLine(0, "ID: "+id);
                    sign.setLine(1, mapName);
                    sign.setLine(2, Players+" / " + maxPlayers);
                    sign.setLine(3, status);

                    sign.update();
                }
            }
        }
    }
    
    
}
