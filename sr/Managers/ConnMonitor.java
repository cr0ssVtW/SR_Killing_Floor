package sr.Managers;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import sr.Listeners.PL;
import sr.SRKF;

/**
 *
 * @author Cross
 * 
 */

public class ConnMonitor implements Runnable
{
    SocketManager plugin;
    private Socket socket;
    
    
    public ConnMonitor(Socket socket, SocketManager plugin) 
    {
        this.plugin = plugin;
        this.socket = socket;
        
        Thread th = new Thread(this);
        th.start();
    }

    @Override
    public void run() 
    {
        try
        {
            ObjectInputStream ois = new ObjectInputStream(this.socket.getInputStream());
            String[] message = (String[])ois.readObject();
            
            ObjectOutputStream oos = new ObjectOutputStream(this.socket.getOutputStream());
            oos.writeObject("true");
            SRKF.LOG.log(Level.INFO, "[SRKF] - Socket Object received!");
            ois.close();
            oos.close();
            
            this.socket.close();
            
            if (message[0].equalsIgnoreCase("spectator"))
            {
                String name = message[1];
                String mapID = message[2];
                int ID = Integer.parseInt(mapID);
                if (PL.incomingSpectator.containsKey(name))
                {
                    PL.incomingSpectator.remove(name);
                }
                PL.incomingSpectator.put(name, ID);
                SRKF.LOG.info("[SRKF] - Incoming spectator: " + name + " for Map ID: " + ID + " added to HashMap.");
            }
            
            if (message[0].equalsIgnoreCase("playerjoining"))
            {
                // player incoming to join map
                String name = message[1];
                String mapID = message[2];
                int ID = Integer.parseInt(mapID);
                
                if (PL.incomingPlayer.containsKey(name))
                {
                    PL.incomingPlayer.remove(name);
                }
                
                PL.incomingPlayer.put(name, ID);
                SRKF.LOG.info("[SRKF] - Incoming player: " + name + " for Map ID: " + ID + " added to HashMap.");
            }
            
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
