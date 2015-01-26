package sr.coinStuff;
 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import sr.SRKF;
 
 
public class coinDB {
   
String URL = "jdbc:mysql://localhost:3306/";
String dbName = "minigame_hub";
String driver = "com.mysql.jdbc.Driver";
String userName = "cr0ss";
String password = "N4i8c3k4";
 
Statement statement;
ResultSet rs = null;
Connection conn = null;
 int updateQuery = 0;
public boolean connected = false;
 
 public SRKF plugin;
 
 public coinDB(SRKF plugin)
{
this.plugin = plugin;
 
establishConnection();
 
}
 
 private void establishConnection()
{
try
{
Class.forName(this.driver).newInstance();
this.conn = DriverManager.getConnection(this.URL + this.dbName, this.userName, this.password);
// log("Connected");
System.out.println("Connected to Database " + this.dbName);;
connected = true;
} catch (Exception e)
{
// log("Unable to connect");
System.out.println("Could not connect to Database.");;
e.printStackTrace();
}
 
}
 
public void closeConnection()
{
try
{
this.conn.close();
// log("Connection closed");
} catch (Exception e)
{
// log("Unable to close connection");
}
}
public boolean isthererank(String name)
    {
        boolean isthere = false;
       
        try
        {
            if (connected)
            {
                this.statement = this.conn.createStatement();
               
                this.rs = this.statement.executeQuery("SELECT rank from PlayernameRank WHERE playername = '" + name + "'");
               
                if (this.rs.next())
                {
                    isthere = true;
                }
               
                this.rs.close();
                this.statement.close();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
       
        return isthere;
    }
public String getRank(String name)
    {
        String rank = null;
       
        try
        {
            if (connected)
            {
                this.statement = this.conn.createStatement();
               
                this.rs = this.statement.executeQuery("SELECT rank from PlayernameRank WHERE playername = '" + name + "'");
               
                if (this.rs.next())
                {
                    rank = this.rs.getString("rank");
                }
               
                this.rs.close();
                this.statement.close();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
       
        return rank;
    }
public double getCoins(String name)
    {
        double coins = 0;
       
        try
        {
            if (connected)
            {
                this.statement = this.conn.createStatement();
               
                this.rs = this.statement.executeQuery("SELECT Coins from PlayerCoins WHERE Playername = '" + name + "'");
               
                if (this.rs.next())
                {
                    coins = this.rs.getDouble("Coins");
                }
               
                this.rs.close();
                this.statement.close();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
       
        return coins;
    }
 
public void UpdateCoins(String name, double oldcoins)
    {
        double rankmultiplier = 1;
        String rankname = rankEL.playernamerank.get(name);
       
        if(rankname.toLowerCase().contains("vip"))
        {
            rankmultiplier = 1.25;
        }
        if(rankname.toLowerCase().contains("elite"))
        {
            rankmultiplier = 1.5;
        }
        if(rankname.toLowerCase().contains("hero"))
        {
            rankmultiplier = 1.75;
        }
        if(rankname.toLowerCase().contains("champion"))
        {
            rankmultiplier = 2;
        }
       
        oldcoins = oldcoins * rankmultiplier;
       
        double currentcoins = getCoins(name);
        double coins = oldcoins + currentcoins;
        
 
        try
        {
            if (connected)
            {
                this.statement = this.conn.createStatement();
                //this.updateQuery = statement.executeUpdate("UPDATE PlayerHats SET Currenthat=" + hat + " WHERE Playername = '" + name + "'");
                //this.updateQuery = statement.executeUpdate("UPDATE PlayerHats SET Currenthat='Grass' WHERE Playername='" + name + "'");
                this.updateQuery = statement.executeUpdate("UPDATE PlayerCoins SET Coins='" + coins + "'"
                        + " WHERE Playername = '" + name + "'");
                this.statement.close();
                
                SRKF.LOG.info("[SRKF] - Added " + oldcoins + " coins to " + name + ". Total: " + coins);
            }
           
        } catch (Exception e)
        {
            e.printStackTrace();
        }
       
        Player player1 = Bukkit.getPlayer(name);
        if (player1.isOnline())
        {
           player1.sendMessage(ChatColor.GOLD + "[" + ChatColor.AQUA + "Shop" + ChatColor.GOLD + "]" + ChatColor.GREEN + "Your Earned: " + oldcoins + " Current Coins: " + ChatColor.GOLD + coins);
        }
    }

public void spendCoins(String name, double oldcoins)
    {
       
       
        double currentcoins = getCoins(name);
        double newCoins = currentcoins - oldcoins;
 
        try
        {
            if (connected)
            {
                this.statement = this.conn.createStatement();
                //this.updateQuery = statement.executeUpdate("UPDATE PlayerHats SET Currenthat=" + hat + " WHERE Playername = '" + name + "'");
                //this.updateQuery = statement.executeUpdate("UPDATE PlayerHats SET Currenthat='Grass' WHERE Playername='" + name + "'");
                this.updateQuery = statement.executeUpdate("UPDATE PlayerCoins SET Coins='" + newCoins + "'"
                        + " WHERE Playername = '" + name + "'");
                this.statement.close();
            }
           
        } catch (Exception e)
        {
            e.printStackTrace();
        }
       
          Player player1 = Bukkit.getPlayer(name);
          if (player1.isOnline())
          {
             player1.sendMessage(ChatColor.GOLD + "[" + ChatColor.AQUA + "Shop" + ChatColor.GOLD + "]" + ChatColor.GREEN + "Your Spent: " + oldcoins + " Coin(s). Current Coins: " + ChatColor.GOLD + newCoins);
                         
          }
        }

public int getChests(String name)
{
    int chests = 0;
    
    try
    {
        if (connected)
        {
            this.statement = this.conn.createStatement();

            this.rs = this.statement.executeQuery("SELECT chests from PlayerChests WHERE playername = '" + name + "'");

            if (this.rs.next())
            {
                chests = this.rs.getInt("chests");
            }

            this.rs.close();
            this.statement.close();
        }
    } catch (Exception e)
    {
        e.printStackTrace();
    }

    return chests;
}


public void addChest(String name)
{
    int currentChests = getChests(name);
    
    currentChests = currentChests + 1;
    
    try
    {
        if (connected)
        {
            this.statement = this.conn.createStatement();
            //this.updateQuery = statement.executeUpdate("UPDATE PlayerHats SET Currenthat=" + hat + " WHERE Playername = '" + name + "'");
            //this.updateQuery = statement.executeUpdate("UPDATE PlayerHats SET Currenthat='Grass' WHERE Playername='" + name + "'");
            this.updateQuery = statement.executeUpdate("UPDATE PlayerChests SET chests='" + currentChests + "'"
                    + " WHERE playername = '" + name + "'");
            this.statement.close();
        }

    } catch (Exception e)
    {
        e.printStackTrace();
    }

    /*
    Player player1 = Bukkit.getPlayer(name);
    if (player1.isOnline())
    {
       player1.sendMessage(ChatColor.GOLD + "[" + ChatColor.AQUA + "Shop" + ChatColor.GOLD + "]" + ChatColor.GREEN + "You found a chest that game! Total Chests Owned: " + ChatColor.AQUA + (currentChests));
    }
    */
}
 
public boolean isthereenhancements(String name)
    {
        boolean isthere = false;
       
        try
        {
            if (connected)
            {
                this.statement = this.conn.createStatement();
               
                this.rs = this.statement.executeQuery("SELECT enhancements from PlayernameEnhancements WHERE Playername = '" + name + "'");
               
                if (this.rs.next())
                {
                    isthere = true;
                }
               
                this.rs.close();
                this.statement.close();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
       
        return isthere;
    }
public String getEnhancements(String name)
    {
        String hats = null;
       
        try
        {
            if (connected)
            {
                this.statement = this.conn.createStatement();
               
                this.rs = this.statement.executeQuery("SELECT enhancements from PlayernameEnhancements WHERE Playername = '" + name + "'");
               
                if (this.rs.next())
                {
                    hats = this.rs.getString("enhancements");
                }
               
                this.rs.close();
                this.statement.close();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
       
        return hats;
    }
 
public String getChestplate(String name)
    {
        String currentchest = null;
       
        try
        {
            if (connected)
            {
                this.statement = this.conn.createStatement();
               
                this.rs = this.statement.executeQuery("SELECT chestplate from PlayernameEnhancements WHERE Playername = '" + name + "'");
               
                if (this.rs.next())
                {
                    currentchest = this.rs.getString("chestplate");
                }
               
                this.rs.close();
                this.statement.close();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
       
        return currentchest;
    }
public String getLeggings(String name)
    {
        String currentlegs = null;
       
        try
        {
            if (connected)
            {
                this.statement = this.conn.createStatement();
               
                this.rs = this.statement.executeQuery("SELECT leggings from PlayernameEnhancements WHERE Playername = '" + name + "'");
               
                if (this.rs.next())
                {
                    currentlegs = this.rs.getString("leggings");
                }
               
                this.rs.close();
                this.statement.close();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
       
        return currentlegs;
    }
public String getBoots(String name)
    {
        String currentboots = null;
       
        try
        {
            if (connected)
            {
                this.statement = this.conn.createStatement();
               
                this.rs = this.statement.executeQuery("SELECT boots from PlayernameEnhancements WHERE Playername = '" + name + "'");
               
                if (this.rs.next())
                {
                    currentboots = this.rs.getString("boots");
                }
               
                this.rs.close();
                this.statement.close();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
       
        return currentboots;
    }
public String getMeleeweapon(String name)
    {
        String currentmeleeweapon = null;
       
        try
        {
            if (connected)
            {
                this.statement = this.conn.createStatement();
               
                this.rs = this.statement.executeQuery("SELECT meleeweapon from PlayernameEnhancements WHERE Playername = '" + name + "'");
               
                if (this.rs.next())
                {
                    currentmeleeweapon = this.rs.getString("meleeweapon");
                }
               
                this.rs.close();
                this.statement.close();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
       
        return currentmeleeweapon;
    }
public String getRangedweapon(String name)
    {
        String currentrangedweapon = null;
       
        try
        {
            if (connected)
            {
                this.statement = this.conn.createStatement();
               
                this.rs = this.statement.executeQuery("SELECT rangedweapon from PlayernameEnhancements WHERE Playername = '" + name + "'");
               
                if (this.rs.next())
                {
                    currentrangedweapon = this.rs.getString("rangedweapon");
                }
               
                this.rs.close();
                this.statement.close();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
       
        return currentrangedweapon;
    }
 
public String getItemInformation(String name)
    {
        String iteminfo = null;
       
        try
        {
            if (connected)
            {
                this.statement = this.conn.createStatement();
               
                this.rs = this.statement.executeQuery("SELECT Information from EnhancementsCostInfo WHERE Enhancementname = '" + name + "'");
               
                if (this.rs.next())
                {
                    iteminfo = this.rs.getString("Information");
                }
               
                this.rs.close();
                this.statement.close();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
       
        return iteminfo;
    }
 
 
}