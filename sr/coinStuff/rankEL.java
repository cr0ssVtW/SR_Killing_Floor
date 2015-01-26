
package sr.coinStuff;

import java.util.HashMap;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import sr.SRKF;


public class rankEL
  implements Listener
{
    
  public static HashMap<String, String> playernamerank = new HashMap<String, String>();

  public SRKF plugin;

  public rankEL(SRKF plugin)
  {
    this.plugin = plugin;
  }

    @EventHandler(priority = EventPriority.HIGHEST)
public void onjoindonor(PlayerJoinEvent event)
        {
            
            Player player = event.getPlayer();
            //rankdatabase db = new rankdatabase(plugin);
            if (SRKF.coindb.connected)
            {
            boolean isthere = SRKF.coindb.isthererank(player.getName().toLowerCase());
            
              if (isthere == true)
              {
                  
                  String rank = SRKF.coindb.getRank(player.getName().toLowerCase());
                  
                  String pname = player.getName().toLowerCase();
                  if(playernamerank.containsKey(pname))
                  {
                    playernamerank.remove(pname);
                  }
                  
                  if(rank != null)
                  {
                      playernamerank.put(player.getName().toLowerCase(), rank.toLowerCase());
                  }
                  else
                  {
                      playernamerank.put(player.getName().toLowerCase(), "member");
                  }

              }
              else
              {

               String pname = player.getName().toLowerCase();
                  if(playernamerank.containsKey(pname))
                  {
                    playernamerank.remove(pname);
                  }
               playernamerank.put(player.getName().toLowerCase(), "member");

              }
              
            }
            else
            {
                  String pname = player.getName().toLowerCase();
                     if(playernamerank.containsKey(pname))
                     {
                         playernamerank.remove(pname);
                     }
                  playernamerank.put(player.getName().toLowerCase(), "member"); 
            }

            
        }
 
  @EventHandler
public void onquitdonor(PlayerQuitEvent event)
{

    Player player = event.getPlayer();
    String pname = player.getName().toLowerCase();

    if(playernamerank.containsKey(pname))
    {
        playernamerank.remove(pname);
    }


}



}