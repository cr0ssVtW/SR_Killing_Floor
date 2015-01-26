package sr.Managers;

import com.mysql.jdbc.Connection;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import sr.Extras.Guns;
import sr.Extras.MapNames;
import sr.Game;
import sr.Listeners.SignListener;
import sr.SRKF;

/**
 *
 * @author Cross
 */
public class Database 
{
    SRKF plugin;
    SignListener sl;
    
    Connection conn = null;
    
    String URL;
    int Port;
    String dbName;
    String driver = "com.mysql.jdbc.Driver";
    String userName;
    String password;
    
    public boolean connected = false;
    
    Statement statement;
    ResultSet rs = null;
    
    int updateQuery = 0;
    
    private Socket socket;
    private int ProxySocketPort = SRKF.config.getInt("ProxySocketPort");
    private String proxyIP = SRKF.config.getString("ProxyIP");
    
    
    public Database (SRKF plugin)
    {
        this.plugin = plugin;
        
        establishConnection();
        new getAllDataForSigns(this.statement, this.rs, this.conn);
    }
    
    
    
    private void establishConnection()
    {
        this.URL = SRKF.config.getString("sqlURL");
        this.Port = SRKF.config.getInt("sqlPort");
        this.dbName = SRKF.config.getString("Database");
        this.userName = SRKF.config.getString("userName");
        this.password = SRKF.config.getString("userPass");
        
        try
        {
            Class.forName(this.driver).newInstance();
            this.conn = (Connection) DriverManager.getConnection("jdbc:mysql://" + this.URL + ":" + this.Port + "/" + this.dbName, this.userName, this.password);
            SRKF.LOG.log(Level.INFO, "[SRKF] - Connected to SQL Database: {0}", this.dbName);
            connected = true;
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e)
        {
            SRKF.LOG.log(Level.SEVERE, "[SRKF] - Failed to connect to SQL Database: {0}", this.dbName);
            e.printStackTrace();
        }
    }
    
    public void closeConnection()
    {
        try
        {
            this.conn.close();
            SRKF.LOG.log(Level.INFO, "[SRKF] - SQL Database connection to {0} closed", this.dbName);
        } catch (Exception e)
        {
          //  log("Unable to close connection");
        }
    }
    
    // Set Lobby for other servers
    
    public void setLobby()
    {
        String ip = Bukkit.getIp();
        int port = SRKF.config.getInt("socketPort");
        int lobby = 1;
        
        try
        {
            if (connected)
            {
                this.statement = this.conn.createStatement();
                this.statement.executeUpdate("INSERT INTO Lobby(Lobby, IP, Port) VALUES('" + lobby + "','" + ip + "','" + port + "') "
                        + "ON DUPLICATE KEY UPDATE ip='" + ip + "', port='" + port + "';");
                
                this.statement.close();
                
                SRKF.LOG.log(Level.WARNING, "[SRKF] Updated Lobby in SQL Database to this servers IP and Socket Port");
            }
            else
            {
                SRKF.LOG.log(Level.WARNING, "[SRKF] Not connected to SQL database.");
            }

        } catch (SQLException e)
        {
            SRKF.LOG.log(Level.SEVERE, "[SRKF] - Failed setLobby()");
            e.printStackTrace();
        }
    }
    
    // Get Lobby Info
    
    public int getLobbyIP()
    {
        int IP = 0;
        
        try
        {
            if (connected)
            {
                this.statement = this.conn.createStatement();
                this.rs = this.statement.executeQuery("SELECT IP from Lobby WHERE Lobby='1')");
                
                if (this.rs.next())
                {
                    IP = this.rs.getInt("IP");
                }
                
                this.rs.close();
                this.statement.close();
            }
            else
            {
                SRKF.LOG.log(Level.WARNING, "[SRKF] Not connected to SQL database.");
            }
            
        } catch (SQLException e)
        {
            SRKF.LOG.log(Level.SEVERE, "[SRKF] - Failed getLobbyIP()");
            e.printStackTrace();
        }
        
        return IP;
    }
    
    public String getLobbyPort()
    {
        String Port = "";
        
        try
        {
            if (connected)
            {
                this.statement = this.conn.createStatement();
                this.rs = this.statement.executeQuery("SELECT Port from Lobby WHERE Lobby='1')");
                
                if (this.rs.next())
                {
                    Port = this.rs.getString("Port");
                }
                
                this.rs.close();
                this.statement.close();
            }
            else
            {
                SRKF.LOG.log(Level.WARNING, "[SRKF] Not connected to SQL database.");
            }
            
        } catch (SQLException e)
        {
            SRKF.LOG.log(Level.SEVERE, "[SRKF] - Failed getLobbyPort()");
            e.printStackTrace();
        }
        
        return Port;
    }
    
    // Map & Sign Update Stuff
    
    public String checkMapName(int ID)
    {
        String name = "";
        
        try
        {
            if (connected)
            {
                this.statement = this.conn.createStatement();
                this.rs = this.statement.executeQuery("SELECT MapName from Servers WHERE ID = '" + ID + "'");
                
                if (this.rs.next())
                {
                    name = this.rs.getString("MapName");
                }
                
                this.rs.close();
                this.statement.close();
            }
            else
            {
                SRKF.LOG.log(Level.WARNING, "[SRKF] Not connected to SQL database.");
            }
            
        } catch (SQLException e)
        {
            SRKF.LOG.log(Level.SEVERE, "[SRKF] - Failed checkMapName (ID: " + ID + ") for DB: {0}", this.dbName);
            e.printStackTrace();
        }
        
        return name;
    }
    
    public int checkMapPlayers(int ID)
    {
        int count = 0;
        
        try
        {
            if (connected)
            {
                this.statement = this.conn.createStatement();
                this.rs = this.statement.executeQuery("SELECT Players from Servers WHERE ID = '" + ID + "'");
                
                if (this.rs.next())
                {
                    count = this.rs.getInt("Players");
                }
                
                this.rs.close();
                this.statement.close();
            }
            else
            {
                SRKF.LOG.log(Level.WARNING, "[SRKF] Not connected to SQL database.");
            }
            
        } catch (SQLException e)
        {
            SRKF.LOG.log(Level.SEVERE, "[SRKF] - Failed checkMapPlayers (ID: " + ID + ") for DB: {0}", this.dbName);
            e.printStackTrace();
        }
        
        return count;
    }
    
    public String checkMapSocketAddress(int ID)
    {
        String address = "";
        
        try
        {
            if (connected)
            {
                this.statement = this.conn.createStatement();
                this.rs = this.statement.executeQuery("SELECT SocketAddress from Servers WHERE ID = '" + ID + "'");
                
                if (this.rs.next())
                {
                    address = this.rs.getString("SocketAddress");
                }
                
                this.rs.close();
                this.statement.close();
            }
            else
            {
                SRKF.LOG.log(Level.WARNING, "[SRKF] Not connected to SQL database.");
            }
            
        } catch (SQLException e)
        {
            SRKF.LOG.log(Level.SEVERE, "[SRKF] - Failed checkMapSocketAddress (ID: " + ID + ") for DB: {0}", this.dbName);
            e.printStackTrace();
        }
        
        return address;
    }
    
    public int checkMapSocketPort(int ID)
    {
        int port = 0;
        
        try
        {
            if (connected)
            {
                this.statement = this.conn.createStatement();
                this.rs = this.statement.executeQuery("SELECT SocketPort from Servers WHERE ID = '" + ID + "'");
                
                if (this.rs.next())
                {
                    port = this.rs.getInt("SocketPort");
                }
                
                this.rs.close();
                this.statement.close();
            }
            else
            {
                SRKF.LOG.log(Level.WARNING, "[SRKF] Not connected to SQL database.");
            }
            
        } catch (SQLException e)
        {
            SRKF.LOG.log(Level.SEVERE, "[SRKF] - Failed checkMapSocketPort (ID: " + ID + ") for DB: {0}", this.dbName);
            e.printStackTrace();
        }
        
        return port;
    }
    
    
    public void addServer(String playerName, String mapName, String address, int port, String worldName, int X1, int Y1, int Z1, int X2, int Y2, int Z2)
    {
        String serverName = "";
        String msgType = "createmap";
        
        String serveraddress = SRKF.config.getString("ServerAddress");
        int serversocketport = SRKF.config.getInt("socketPort");
        
        try
        {
            this.socket = new Socket(this.proxyIP, this.ProxySocketPort);
            ObjectOutputStream oos = new ObjectOutputStream(this.socket.getOutputStream());

            oos.writeObject(new String[]
            {
               msgType, playerName
            });

            SRKF.LOG.log(Level.INFO, "[SRKF] - Fired off createMap object for " + this.proxyIP + " on port " + this.ProxySocketPort);

            ObjectInputStream ois = new ObjectInputStream(this.socket.getInputStream());
            serverName = ois.readObject().toString();

            ois.close();
            oos.close();
            this.socket.close();


        } catch (Exception e)
        {
            e.printStackTrace();
        }
        
        try
        {
            if (connected)
            {
                String SQL = "INSERT INTO Servers(Status,MapName,Players,ProxyName,World,x1,x2,y1,y2,z1,z2,SocketAddress,SocketPort) VALUES('" + (3) + "','" + mapName + "','" + (0) + "','" 
                        + serverName + "','" + worldName + "','" + X1 + "','" + X2 + "','" + Y1 + "','" + Y2 + "',' " + Z1 + "','" + Z2 + "','"
                        + serveraddress + "','" + serversocketport + "')";
                this.statement = this.conn.createStatement();
                this.updateQuery = this.statement.executeUpdate(SQL);
                
                this.statement.close();  
            }
            else
            {
                SRKF.LOG.log(Level.WARNING, "[SRKF] Not connected to SQL database.");
            }
        } catch (SQLException e)
        {
            SRKF.LOG.log(Level.SEVERE, "[SRKF] Could not add Server to Database: {0}", this.dbName);
            e.printStackTrace();
        }
    }
    
    public boolean checkMapID(int ID)
    {
        boolean checked = false;
        
        try
        {
            if (connected)
            {
                this.statement = this.conn.createStatement();
                this.rs = this.statement.executeQuery("SELECT ID from Servers WHERE ID = '" + ID + "'");
                
                if (this.rs.next())
                {
                    checked = true;
                }
                
                this.rs.close();
                this.statement.close();
            }
            else
            {
                SRKF.LOG.log(Level.WARNING, "[SRKF] Not connected to SQL database.");
            }
            
        } catch (SQLException e)
        {
            SRKF.LOG.log(Level.SEVERE, "[SRKF] - Failed checkMapID (ID: " + ID + ") for DB: {0}", this.dbName);
            e.printStackTrace();
        }
        
        return checked;
    }
    
    public int getLastID()
    {
        int ID = 0;
        
        try
        {
            if (connected)
            {
                this.statement = this.conn.createStatement();
                this.rs = this.statement.executeQuery("SELECT MAX(ID) AS id FROM Servers");
                
                if (this.rs.next())
                {
                    ID = this.rs.getInt("id");
                }
                
                this.rs.close();
                this.statement.close();
            }
            else
            {
                SRKF.LOG.log(Level.WARNING, "[SRKF] - Not connected to SQL database.");
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        
        return ID;
    }
    
    
    public void removeServer(int id)
    {
        try
        {
            if (connected)
            {
                String SQL = "DELETE FROM Servers WHERE ID=" + id + ";";
                this.statement = this.conn.createStatement();
                this.updateQuery = this.statement.executeUpdate(SQL);
                
                this.statement.close();
            }
            else
            {
                SRKF.LOG.log(Level.WARNING, "[SRKF] Not connected to SQL database.");
            }
        } catch (SQLException e)
        {
            SRKF.LOG.log(Level.SEVERE, "[SRKF] Could not remove map with id " + id + " from Server Database: {0}", this.dbName);
            e.printStackTrace();
        }
    }
    
    public int getCount()
    {
        int count = 0;
        
        try
        {
            if (connected)
            {
                String SQL = "SELECT COUNT(ID) FROM Servers";
                this.statement = this.conn.createStatement();
                this.rs = this.statement.executeQuery(SQL);
                
                if (this.rs.next())
                {
                    count = this.rs.getInt(1);
                }
                
                this.rs.close();
                this.statement.close();
                
                
            }
        } catch (Exception e)
        {
            SRKF.LOG.log(Level.SEVERE, "[SRKF] Could not retrieve row count from Server Database: {0}", this.dbName);
            e.printStackTrace();
        }
        
        return count;
    }
    
    public int getSignCount()
    {
        int count = 0;
        
        try
        {
            if (connected)
            {
                String SQL = "SELECT COUNT(ServerID) as count FROM Signs";
                this.statement = this.conn.createStatement();
                this.rs = this.statement.executeQuery(SQL);
                
                if (this.rs.next())
                {
                    count = this.rs.getInt("count");
                    SRKF.LOG.info("getSignCount() is : " + count);
                }
                
                this.rs.close();
                this.statement.close();
                
                
            }
        } catch (Exception e)
        {
            SRKF.LOG.log(Level.SEVERE, "[SRKF] Could not retrieve sign row count from Server Database: {0}", this.dbName);
            e.printStackTrace();
        }
        
        return count;
    }
    
    public void addSign(int ID, int X1, int Y1, int Z1)
    {
        try
        {
            if (connected)
            {
                String SQL = "INSERT INTO Signs(ServerID,x,y,z) VALUES('" + ID + "','" + X1 + "','" + Y1 + "','" + Z1 + "')";
                this.statement = this.conn.createStatement();
                this.updateQuery = this.statement.executeUpdate(SQL);
                
                this.statement.close();
            }
            else
            {
                SRKF.LOG.log(Level.WARNING, "[SRKF] Not connected to SQL database.");
            }
        } catch (SQLException e)
        {
            SRKF.LOG.log(Level.SEVERE, "[SRKF] Could not add Sign to Database: {0}", this.dbName);
            e.printStackTrace();
        }
    }
    
    public void removeSign(int ID)
    {
        try
        {
            if (connected)
            {
                String SQL = "DELETE FROM Signs WHERE ServerID=" + ID + ";";
                this.statement = this.conn.createStatement();
                this.updateQuery = this.statement.executeUpdate(SQL);
                
                this.statement.close();
            }
            else
            {
                SRKF.LOG.log(Level.WARNING, "[SRKF] Not connected to SQL database.");
            }
        } catch (SQLException e)
        {
            SRKF.LOG.log(Level.SEVERE, "[SRKF] Could not remove Sign for Server ID: " + ID + " from Sign Table for Database: {0}", this.dbName);
            e.printStackTrace();
        }
    }
    
    public void loadSignsFromDB()
    {
        try
        {
            if (connected)
            {
                String SQL = "SELECT * FROM Signs";
                this.statement = this.conn.createStatement();
                this.rs = this.statement.executeQuery(SQL);

                while (this.rs.next())
                {
                    int ID = this.rs.getInt("ServerID");
                    int x = this.rs.getInt("x");
                    int y = this.rs.getInt("y");
                    int z = this.rs.getInt("z");

                    Block b = Bukkit.getWorld("Lobby").getBlockAt(x, y, z);

                    SignListener.loadSigns(ID, b);
                }

                this.rs.close();
                this.statement.close();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        
    }
    
    
    private class getAllDataForSigns implements Runnable
    {
        int taskID;

        Statement statement;
        ResultSet rs;
        Connection conn;
        
        public getAllDataForSigns(Statement statement, ResultSet rs, Connection conn)
        {
            this.statement = statement;
            this.rs = rs;
            this.conn = conn;
            
            this.taskID = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, this, 200, 200);
        }
        
        @Override
        public void run() 
        {
            try
            {
                if (connected && SRKF.isLobby)
                {
                    String SQL = "Select ID, Status, MapName, Players FROM Servers";
                    this.statement = this.conn.createStatement();
                    this.rs = this.statement.executeQuery(SQL);
                    
                    while (this.rs.next())
                    {
                        int ID = this.rs.getInt("ID");
                        int status = this.rs.getInt("Status");
                        String mapName = this.rs.getString("MapName");
                        int players = this.rs.getInt("Players");
                        
                        //Bukkit.broadcastMessage("Sent to updateSigns()");
                        SignListener.updateSigns(ID, status, mapName, players);
                    }
                    
                    this.rs.close();
                    this.statement.close();
                }
                
            } catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
    }
    
    public boolean checkServerReady(int ID)
    {
        boolean ready = false;
        
        try
        {
            if (connected)
            {
                this.statement = this.conn.createStatement();
                this.rs = this.statement.executeQuery("SELECT Status, Players from Servers WHERE ID = '" + ID + "'");
                
                if (this.rs.next())
                {
                    int status = this.rs.getInt("Status");
                    int players = this.rs.getInt("Players");
                    
                   // Bukkit.broadcastMessage("Status is: " + status + " | Players: " + players);
                    
                    if (status == 3 && (players < SignListener.maxPlayers))
                    {
                        ready = true;
                    }
                }
                
                this.rs.close();
                this.statement.close();
            }
            else
            {
                SRKF.LOG.log(Level.WARNING, "[SRKF] Not connected to SQL database.");
            }
            
        } catch (SQLException e)
        {
            SRKF.LOG.log(Level.SEVERE, "[SRKF] - Failed serverready check (ID: " + ID + ") for DB: {0}", this.dbName);
            e.printStackTrace();
        }
        
        return ready;
    }
    
    public String getProxyServerName(int ID)
    {
        String ProxyName = "";
        
        try
        {
            if (connected)
            {
                String SQL = "SELECT ProxyName FROM Servers WHERE ID = '" + ID + "'";
                this.statement = this.conn.createStatement();
                this.rs = this.statement.executeQuery(SQL);
                
                if (this.rs.next())
                {
                    ProxyName = this.rs.getString("ProxyName");
                }
                
                this.rs.close();
                this.statement.close();
            }
            else
            {
                SRKF.LOG.log(Level.WARNING, "[SRKF] Not connected to SQL database.");
            }
            
        } catch (SQLException e)
        {
            SRKF.LOG.log(Level.SEVERE, "[SRKF] - Failed getProxyServerName check (ID: " + ID + ") for DB: {0}", this.dbName);
            e.printStackTrace();
        }
        
        return ProxyName;
    }
    
    public String getMapWorld(int ID)
    {
        String worldName = "";
        
        try
        {
            if (connected)
            {
                String SQL = "SELECT World FROM Servers WHERE ID = '" + ID + "'";
                this.statement = this.conn.createStatement();
                this.rs = this.statement.executeQuery(SQL);
                
                if (this.rs.next())
                {
                    worldName = this.rs.getString("World");
                }
                
                this.rs.close();
                this.statement.close();
            }
            else
            {
                SRKF.LOG.log(Level.WARNING, "[SRKF] Not connected to SQL database.");
            }
            
        } catch (SQLException e)
        {
            SRKF.LOG.log(Level.SEVERE, "[SRKF] - Failed getMapWorld check (ID: " + ID + ") for DB: {0}", this.dbName);
            e.printStackTrace();
        }
        
        return worldName;
    }
    
    
    public boolean createMapName(String mapName)
    {
        boolean success = false;
        
        try
        {
            if (connected)
            {
                String SQL = "INSERT INTO MapNames(Name) VALUES('" + mapName + "')";
                this.statement = this.conn.createStatement();
                this.updateQuery = this.statement.executeUpdate(SQL);
                
                this.statement.close();
                
                MapNames.mapNames.add(mapName);
                
                success = true;
            }
            else
            {
                SRKF.LOG.log(Level.WARNING, "[SRKF] Not connected to SQL database.");
            }
            
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        
        return success;
    }
    
    public boolean removeMapName(String mapName)
    {
        boolean success = false;
        
        try
        {
            if (connected)
            {
                String SQL = "DELETE FROM MapNames WHERE Name=" + mapName + ";";
                this.statement = this.conn.createStatement();
                this.updateQuery = this.statement.executeUpdate(SQL);
                
                this.statement.close();
                
                MapNames.mapNames.remove(mapName);
                
                success = true;
            }
            else
            {
                SRKF.LOG.log(Level.WARNING, "[SRKF] Not connected to SQL database.");
            }
            
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        
        return success;
    }
    
    public void getMapNames()
    {
        try
        {
            if (connected)
            {
                String SQL = "SELECT * FROM MapNames";
                this.statement = this.conn.createStatement();
                this.rs = this.statement.executeQuery(SQL);
                
                while (this.rs.next())
                {
                    String mapName = this.rs.getString("Name");
                    MapNames.mapNames.add(mapName);
                }
                
                this.rs.close();
                this.statement.close();
            }
            else
            {
                SRKF.LOG.log(Level.WARNING, "[SRKF] Not connected to SQL database.");
            }
            
        } catch (SQLException e)
        {
            e.printStackTrace();
        }   
    }
    
    public boolean addMapSpawn(int ID, String mapName, int x, int y, int z)
    {
        boolean success = false;
        try
        {
            if (connected)
            {
                this.statement = this.conn.createStatement();
                this.statement.addBatch("CREATE TABLE IF NOT EXISTS Spawns_" + mapName + "(Spawn SMALLINT PRIMARY KEY NOT NULL, x SMALLINT NOT NULL, y SMALLINT NOT NULL, z SMALLINT NOT NULL);");
                this.statement.addBatch("INSERT INTO Spawns_" + mapName + "(Spawn, x, y, z) VALUES(" + ID + "," + x + "," + y + "," + z + ") "
                        + "ON DUPLICATE KEY UPDATE x=" + x + ", y=" + y + ", z=" + z + ";");
                this.statement.executeBatch();
                
                this.statement.close();
                
                if (!(MapNames.mapNames.contains(mapName)))
                {
                    MapNames.mapNames.add(mapName);
                }
                
                success = true;
            }
            else
            {
                SRKF.LOG.log(Level.WARNING, "[SRKF] Not connected to SQL database.");
            }
            
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        
        return success;
    }
    
    public boolean addMobSpawn(int ID, String mapName, int x, int y, int z)
    {
        boolean success = false;
        try
        {
            if (connected)
            {
                this.statement = this.conn.createStatement();
                this.statement.addBatch("CREATE TABLE IF NOT EXISTS MobSpawns_" + mapName + "(Spawn SMALLINT PRIMARY KEY NOT NULL, x SMALLINT NOT NULL, y SMALLINT NOT NULL, z SMALLINT NOT NULL);");
                this.statement.addBatch("INSERT INTO MobSpawns_" + mapName + "(Spawn, x, y, z) VALUES(" + ID + "," + x + "," + y + "," + z + ") "
                        + "ON DUPLICATE KEY UPDATE x=" + x + ", y=" + y + ", z=" + z + ";");
                this.statement.executeBatch();
                
                this.statement.close();
                
                if (!(MapNames.mapNames.contains(mapName)))
                {
                    MapNames.mapNames.add(mapName);
                }
                
                success = true;
            }
            else
            {
                SRKF.LOG.log(Level.WARNING, "[SRKF] Not connected to SQL database.");
            }
            
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        
        return success;
    }
    
    public String getMapName(int ID)
    {
        String name = "";
        
        try
        {
            if (connected)
            {
                String SQL = "SELECT MapName FROM Servers WHERE ID=" + ID + ";";
                this.statement = this.conn.createStatement();
                this.rs = this.statement.executeQuery(SQL);
                if (this.rs.next())
                {
                    name = this.rs.getString("MapName");
                }
                this.rs.close();
                this.statement.close();
            }
            else
            {
                SRKF.LOG.log(Level.WARNING, "[SRKF] Not connected to SQL database.");
            }
            
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        
        return name;
    }
    
    public Location getSpawnRandom(String mapName, String worldName)
    {
        Location loc = null;
        
        int x = 0;
        int y = 0;
        int z = 0;
        
        try
        {
            if (connected)
            {
                String SQL = "SELECT x, y, z FROM Spawns_" + mapName.toLowerCase() + " ORDER BY rand() LIMIT 1";
                this.statement = this.conn.createStatement();
                this.rs = this.statement.executeQuery(SQL);
                if (this.rs.next())
                {
                    x = this.rs.getInt("x");
                    y = this.rs.getInt("y");
                    z = this.rs.getInt("z");
                    try
                    {
                        World world = Bukkit.getWorld(worldName);
                        loc = new Location(world, x, y, z);
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                this.rs.close();
                this.statement.close();
            }
            else
            {
                SRKF.LOG.log(Level.WARNING, "[SRKF] Not connected to SQL database.");
            }
            
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        
        //Bukkit.broadcastMessage("Location is: " + loc + " || world is: " + worldName + " || x is: " + x + " || y is: " + y + " || z is: " + z);
        return loc;
        
    }
    
    public int getMobSpawnCount (int ID, String mapName)
    {
        int count = 0;
        
        try
        {
            if (connected)
            {
                String SQL = "SELECT Spawn FROM MobSpawns_" + mapName.toLowerCase() + ";";
                this.statement = this.conn.createStatement();
                this.rs = this.statement.executeQuery(SQL);
                while(rs.next())
                {
                    count++;
                }
                this.rs.close();
                this.statement.close();
            }
            else
            {
                SRKF.LOG.log(Level.WARNING, "[SRKF] Not connected to SQL database.");
            }
            
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        SRKF.LOG.log(Level.INFO, "[SRKF] - Counted " + count + " mob spawns for Map: " + mapName);
        return count;
    }
    
    public Location getMobSpawns(int ID, String mapName, String worldName)
    {
        Location loc = null;
        
        int count = 0;
        
        int x = 0;
        int y = 0;
        int z = 0;
                
        try
        {
            if (connected)
            {
                String SQL = "SELECT x, y, z FROM MobSpawns_" + mapName.toLowerCase() + ";";
                this.statement = this.conn.createStatement();
                this.rs = this.statement.executeQuery(SQL);
                while(rs.next())
                {
                    count++;
                    
                    x = this.rs.getInt("x");
                    y = this.rs.getInt("y");
                    z = this.rs.getInt("z");
                    try
                    {
                        World world = Bukkit.getWorld(worldName);
                        loc = new Location(world, x, y, z);
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    
                    if (loc != null)
                    {
                        String mapID = mapName + "_" + ID;
                        String mapSpawn = mapID + "_" + count;
                        SRKF.LOG.log(Level.INFO, "[SRKF] - Loading Mob Spawn " + count + " into Memory for Key: " + mapSpawn);
                        Game.mobSpawns.put(mapSpawn, loc);
                    }
                }
                this.rs.close();
                this.statement.close();
            }
            else
            {
                SRKF.LOG.log(Level.WARNING, "[SRKF] Not connected to SQL database.");
            }
            
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        
        //Bukkit.broadcastMessage("Location is: " + loc + " || world is: " + worldName + " || x is: " + x + " || y is: " + y + " || z is: " + z);
        return loc;
        
    }
    
    public Location getMapMinLocation(int ID)
    {
        Location loc = null;
        
        String worldName = "";
        int x1 = 0;
        int y1 = 0;
        int z1 = 0;
        
        try
        {
            if (connected)
            {
                String SQL = "SELECT World, x1, y1, z1 FROM Servers WHERE ID=" + ID + ";";
                this.statement = this.conn.createStatement();
                this.rs = this.statement.executeQuery(SQL);
                if (this.rs.next())
                {
                    worldName = this.rs.getString("World");
                    x1 = this.rs.getInt("x1");
                    y1 = this.rs.getInt("y1");
                    z1 = this.rs.getInt("z1");
                    
                    try
                    {
                        World world = Bukkit.getWorld(worldName);
                        loc = new Location(world, x1, y1, z1);
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                this.rs.close();
                this.statement.close();
            }
            else
            {
                SRKF.LOG.log(Level.WARNING, "[SRKF] Not connected to SQL database.");
            }
            
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        
        //Bukkit.broadcastMessage("Location is: " + loc + " || world is: " + worldName + " || x is: " + x + " || y is: " + y + " || z is: " + z);
        return loc;
    }
    
    public Location getMapMaxLocation(int ID)
    {
        Location loc = null;
        
        String worldName = "";
        int x2 = 0;
        int y2 = 0;
        int z2 = 0;
        
        try
        {
            if (connected)
            {
                String SQL = "SELECT World, x2, y2, z2 FROM Servers WHERE ID=" + ID + ";";
                this.statement = this.conn.createStatement();
                this.rs = this.statement.executeQuery(SQL);
                if (this.rs.next())
                {
                    worldName = this.rs.getString("World");
                    x2 = this.rs.getInt("x2");
                    y2 = this.rs.getInt("y2");
                    z2 = this.rs.getInt("z2");
                    
                    try
                    {
                        World world = Bukkit.getWorld(worldName);
                        loc = new Location(world, x2, y2, z2);
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                this.rs.close();
                this.statement.close();
            }
            else
            {
                SRKF.LOG.log(Level.WARNING, "[SRKF] Not connected to SQL database.");
            }
            
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        
        //Bukkit.broadcastMessage("Location is: " + loc + " || world is: " + worldName + " || x is: " + x + " || y is: " + y + " || z is: " + z);
        return loc;
    }
    
    public void setMapStatus (int ID, int status)
    {
        try
        {
            if (connected)
            {
                String SQL = "UPDATE Servers SET Status = " + status + " WHERE ID=" + ID + ";";
                this.statement = this.conn.createStatement();
                this.updateQuery = this.statement.executeUpdate(SQL);
                this.statement.close();
                
                SRKF.LOG.log(Level.INFO, "[SRKF] - Updated map status for ID: " + ID + " to status: " + status);
            }
            else
            {
                SRKF.LOG.log(Level.WARNING, "[SRKF] Not connected to SQL database.");
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    
    public int getPlayerCount(int ID)
    {
        int count = 0;
        
        try
        {
            if (connected)
            {
                String SQL = "SELECT Players FROM Servers WHERE ID=" + ID + ";";
                this.statement = this.conn.createStatement();
                this.rs = this.statement.executeQuery(SQL);
                
                if (this.rs.next())
                {
                    count = this.rs.getInt("Players");
                }
                
                this.rs.close();
                this.statement.close();
                
            }
            else
            {
                SRKF.LOG.log(Level.WARNING, "[SRKF] Not connected to SQL database.");
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        } 
        
        return count;
    }
    
    public void setMapPlayers(int ID, int Amount)
    {
        try
        {
            if (connected)
            {
                String SQL = "UPDATE Servers SET Players = " + Amount + " WHERE ID=" + ID + ";";
                this.statement = this.conn.createStatement();
                this.updateQuery = this.statement.executeUpdate(SQL);
                this.statement.close();
                
                SRKF.LOG.log(Level.INFO, "[SRKF] - Updated map players for ID: " + ID + " to players: " + Amount);
            }
            else
            {
                SRKF.LOG.log(Level.WARNING, "[SRKF] Not connected to SQL database.");
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        } 
    }
    
    public void setMapPlayers(int ID, String status)
    {
        int players = getPlayerCount(ID);
        int newplayers = players;
        
        if (status.equalsIgnoreCase("join"))
        {
            newplayers = players + 1;
        }
        
        if (status.equalsIgnoreCase("leave"))
        {
            newplayers = players - 1;
        }
        
        try
        {
            if (connected)
            {
                String SQL = "UPDATE Servers SET Players = " + newplayers + " WHERE ID=" + ID + ";";
                this.statement = this.conn.createStatement();
                this.updateQuery = this.statement.executeUpdate(SQL);
                this.statement.close();
                
                SRKF.LOG.log(Level.INFO, "[SRKF] - Updated map players for ID: " + ID + " to players: " + newplayers);
            }
            else
            {
                SRKF.LOG.log(Level.WARNING, "[SRKF] Not connected to SQL database.");
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        } 
    }
    
    
    public void getDemo()
    {
        int count = 0;
         
        try
        {
            if (connected)
            {
                String SQL = "SELECT * FROM Demolitions;";
                this.statement = this.conn.createStatement();
                this.rs = this.statement.executeQuery(SQL);
                while(rs.next())
                {
                    count++;
                    
                    String demoName = this.rs.getString("demoName");
                    String demoType = this.rs.getString("demoType");
                    double demoDamage = this.rs.getDouble("demoDamage");
                    int demoAmmo = this.rs.getInt("demoAmmo");
                    int demoReload = this.rs.getInt("demoReload");
                    int demoPrimary = this.rs.getInt("demoPrimary");
                    int demoCost = this.rs.getInt("demoCost");
                    String demoMat = this.rs.getString("demoMaterial");
                    
                    Material demoMaterial = Material.getMaterial(demoMat);
                    
                    Guns.craftDemo(demoName, demoType, demoDamage, demoPrimary, demoAmmo, demoCost, demoMaterial, demoReload);
                    
                }
                this.rs.close();
                this.statement.close();
            }
            else
            {
                SRKF.LOG.log(Level.WARNING, "[SRKF] Not connected to SQL database.");
            }
            
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    
    public void getGuns()
    {
        int count = 0;
         
        try
        {
            if (connected)
            {
                String SQL = "SELECT * FROM Guns;";
                this.statement = this.conn.createStatement();
                this.rs = this.statement.executeQuery(SQL);
                while(rs.next())
                {
                    count++;
                    
                    String gunName = this.rs.getString("gunName");
                    String gunType = this.rs.getString("gunType");
                    double gunDamage = this.rs.getDouble("gunDamage");
                    long gunDelay = this.rs.getInt("gunDelay");
                    int gunAmmo = this.rs.getInt("gunAmmo");
                    int gunReload = this.rs.getInt("gunReload");
                    int gunPrimary = this.rs.getInt("gunPrimary");
                    int gunCost = this.rs.getInt("gunCost");
                    String gunMat = this.rs.getString("gunMaterial");
                    
                    Material gunMaterial = Material.getMaterial(gunMat);
                    
                    Guns.craftGun(gunName, gunType, gunDamage, gunDelay, gunAmmo, gunReload, gunPrimary, gunCost, gunMaterial);
                    
                }
                this.rs.close();
                this.statement.close();
            }
            else
            {
                SRKF.LOG.log(Level.WARNING, "[SRKF] Not connected to SQL database.");
            }
            
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    
    public void getMelee()
    {
        int count = 0;
         
        try
        {
            if (connected)
            {
                String SQL = "SELECT * FROM Melee;";
                this.statement = this.conn.createStatement();
                this.rs = this.statement.executeQuery(SQL);
                while(rs.next())
                {
                    count++;
                    String meleeName = this.rs.getString("meleeName");
                    String meleeType = this.rs.getString("meleeType");
                    double meleeDamage = this.rs.getDouble("meleeDamage");
                    int meleeDelay = this.rs.getInt("meleeDelay");
                    double meleeRange = this.rs.getInt("meleeRange");
                    int meleePrimary = this.rs.getInt("meleePrimary");
                    int meleeCost = this.rs.getInt("meleeCost");
                    String meleeMat = this.rs.getString("meleeMaterial");
                    Material meleeMaterial = Material.getMaterial(meleeMat);
                    Guns.craftMelee(meleeName, meleeType, meleeDamage, meleeDelay, meleeRange, meleePrimary, meleeCost, meleeMaterial);
                }
                this.rs.close();
                this.statement.close();
            }
            else
            {
                SRKF.LOG.log(Level.WARNING, "[SRKF] Not connected to SQL database.");
            }
            
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    
    public boolean addMapDoor(int ID, String mapName, int x, int y, int z)
    {
        boolean success = false;
        try
        {
            if (connected)
            {
                this.statement = this.conn.createStatement();
                this.statement.addBatch("CREATE TABLE IF NOT EXISTS Doors_" + mapName + "(Door SMALLINT PRIMARY KEY NOT NULL, x SMALLINT NOT NULL, y SMALLINT NOT NULL, z SMALLINT NOT NULL);");
                this.statement.addBatch("INSERT INTO Doors_" + mapName + "(Door, x, y, z) VALUES(" + ID + "," + x + "," + y + "," + z + ") "
                        + "ON DUPLICATE KEY UPDATE x=" + x + ", y=" + y + ", z=" + z + ";");
                this.statement.executeBatch();
                
                this.statement.close();
                
                if (!(MapNames.mapNames.contains(mapName)))
                {
                    MapNames.mapNames.add(mapName);
                }
                
                success = true;
                
                SRKF.LOG.log(Level.INFO, "[SRKF] Created Door ID " + ID + " for map + " + mapName + " at " + x + ", " + y + ", " + z);
            }
            else
            {
                SRKF.LOG.log(Level.WARNING, "[SRKF] Not connected to SQL database.");
            }
            
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        
        return success;
    }
    
    public Location loadMapDoors(int ID, String mapName, String worldName)
    {
        Location loc = null;
        
        int count = 0;
        
        int x = 0;
        int y = 0;
        int z = 0;
                
        try
        {
            if (connected)
            {
                String SQL = "SELECT x, y, z FROM Doors_" + mapName.toLowerCase() + ";";
                this.statement = this.conn.createStatement();
                this.rs = this.statement.executeQuery(SQL);
                while(rs.next())
                {
                    count++;
                    
                    x = this.rs.getInt("x");
                    y = this.rs.getInt("y");
                    z = this.rs.getInt("z");
                    try
                    {
                        World world = Bukkit.getWorld(worldName);
                        loc = new Location(world, x, y, z);
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    
                    if (loc != null)
                    {
                       
                        String mapID = mapName + "_" + ID;
                        String doorSpawn = "Door_" + mapID + "_" + count;
                        
                        Game.setDoorPositions(loc);
                        
                        if (Game.mapDoorLoc.contains(loc))
                        {
                            Game.mapDoorLoc.remove(loc);
                        }
                        Game.mapDoorLoc.add(loc);
                        
                        if (Game.mapDoorHealth.containsKey(loc))
                        {
                            Game.mapDoorHealth.remove(loc);
                        }
                        Game.mapDoorHealth.put(loc, 0);
                        
                        SRKF.LOG.log(Level.INFO, "[SRKF] - Loading fresh Doors (" + count + ") into Memory for Key: " + doorSpawn);
                    }
                }
                this.rs.close();
                this.statement.close();
            }
            else
            {
                SRKF.LOG.log(Level.WARNING, "[SRKF] Not connected to SQL database.");
            }
            
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        
        //Bukkit.broadcastMessage("Location is: " + loc + " || world is: " + worldName + " || x is: " + x + " || y is: " + y + " || z is: " + z);
        return loc;
        
    }
    
    public boolean addMapShopDoorway(int ID, String mapName, int x, int y, int z, int x2, int y2, int z2, Material mat)
    {
        String matString = mat.toString();
        Bukkit.broadcastMessage(matString);
        boolean success = false;
        try
        {
            if (connected)
            {
                this.statement = this.conn.createStatement();
                this.statement.addBatch("CREATE TABLE IF NOT EXISTS Shop_" + mapName + "(Shop SMALLINT PRIMARY KEY NOT NULL, doorx SMALLINT NOT NULL, doory SMALLINT NOT NULL, doorz SMALLINT NOT NULL, doorx2 SMALLINT NOT NULL, doory2 SMALLINT NOT NULL, doorz2 SMALLINT NOT NULL, shopx SMALLINT NOT NULL, shopy SMALLINT NOT NULL, shopz SMALLINT NOT NULL, exitx SMALLINT NOT NULL, exity SMALLINT NOT NULL, exitz SMALLINT NOT NULL, doormaterial VARCHAR (255) NOT NULL);");
                this.statement.addBatch("INSERT INTO Shop_" + mapName + "(Shop, doorx, doory, doorz, doorx2, doory2, doorz2, shopx, shopy, shopz, exitx, exity, exitz, doormaterial) VALUES(" + ID + "," + x + "," + y + "," + z + "," + x2 + "," + y2 + "," + z2 + "," + 0 + "," + 0 + "," + 0 + "," + 0 + "," + 0 + "," + 0 + ",'" + matString + "') "
                        + "ON DUPLICATE KEY UPDATE doorx=" + x + ", doory=" + y + ", doorz=" + z + ", doorx2=" + x2 + ", doory2=" + y2 + ", doorz2=" + z2 + ", doormaterial='" + matString + "';");
                this.statement.executeBatch();
                
                this.statement.close();
                
                if (!(MapNames.mapNames.contains(mapName)))
                {
                    MapNames.mapNames.add(mapName);
                }
                
                success = true;
                
                SRKF.LOG.log(Level.INFO, "[SRKF] Created Shop Door ID " + ID + " for map + " + mapName + " at " + x + ", " + y + ", " + z);
            }
            else
            {
                SRKF.LOG.log(Level.WARNING, "[SRKF] Not connected to SQL database.");
            }
            
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        
        return success;
    }
    
    public boolean addMapShopKepeerLocation(int ID, String mapName, int x, int y, int z)
    {
        boolean success = false;
        try
        {
            if (connected)
            {
                this.statement = this.conn.createStatement();
                this.statement.addBatch("CREATE TABLE IF NOT EXISTS Shop_" + mapName + "(Shop SMALLINT PRIMARY KEY NOT NULL, doorx SMALLINT NOT NULL, doory SMALLINT NOT NULL, doorz SMALLINT NOT NULL, doorx2 SMALLINT NOT NULL, doory2 SMALLINT NOT NULL, doorz2 SMALLINT NOT NULL, shopx SMALLINT NOT NULL, shopy SMALLINT NOT NULL, shopz SMALLINT NOT NULL, exitx SMALLINT NOT NULL, exity SMALLINT NOT NULL, exitz SMALLINT NOT NULL, doormaterial VARCHAR (255) NOT NULL);");
                this.statement.addBatch("INSERT INTO Shop_" + mapName + "(Shop, doorx, doory, doorz, doorx2, doory2, doorz2, shopx, shopy, shopz, exitx, exity, exitz, doormaterial) VALUES(" + ID + "," + 0 + "," + 0 + "," + 0 + "," + 0 + "," + 0 + "," + 0 + "," + x + "," + y + "," + z + "," + 0 + "," + 0 + "," + 0 + ",'" + "AIR" + "') "
                        + "ON DUPLICATE KEY UPDATE shopx=" + x + ", shopy=" + y + ", shopz=" + z + ";");
                this.statement.executeBatch();
                
                this.statement.close();
                
                if (!(MapNames.mapNames.contains(mapName)))
                {
                    MapNames.mapNames.add(mapName);
                }
                
                success = true;
                
                SRKF.LOG.log(Level.INFO, "[SRKF] Created Shop Keeper ID " + ID + " for map + " + mapName + " at " + x + ", " + y + ", " + z);
            }
            else
            {
                SRKF.LOG.log(Level.WARNING, "[SRKF] Not connected to SQL database.");
            }
            
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        
        return success;
    }
    
    public boolean addMapShopExitLocation(int ID, String mapName, int x, int y, int z)
    {
        boolean success = false;
        try
        {
            if (connected)
            {
                this.statement = this.conn.createStatement();
                this.statement.addBatch("CREATE TABLE IF NOT EXISTS Shop_" + mapName + "(Shop SMALLINT PRIMARY KEY NOT NULL, doorx SMALLINT NOT NULL, doory SMALLINT NOT NULL, doorz SMALLINT NOT NULL, doorx2 SMALLINT NOT NULL, doory2 SMALLINT NOT NULL, doorz2 SMALLINT NOT NULL, shopx SMALLINT NOT NULL, shopy SMALLINT NOT NULL, shopz SMALLINT NOT NULL, exitx SMALLINT NOT NULL, exity SMALLINT NOT NULL, exitz SMALLINT NOT NULL, doormaterial VARCHAR (255) NOT NULL);");
                this.statement.addBatch("INSERT INTO Shop_" + mapName + "(Shop, doorx, doory, doorz, doorx2, doory2, doorz2, shopx, shopy, shopz, exitx, exity, exitz, doormaterial) VALUES(" + ID + "," + 0 + "," + 0 + "," + 0 + "," + 0 + "," + 0 + "," + 0 + "," + 0 + "," + 0 + "," + 0 + "," + x + "," + y + "," + z +",'" + "AIR" + "') "
                        + "ON DUPLICATE KEY UPDATE exitx=" + x + ", exity=" + y + ", exitz=" + z + ";");
                this.statement.executeBatch();
                
                this.statement.close();
                
                if (!(MapNames.mapNames.contains(mapName)))
                {
                    MapNames.mapNames.add(mapName);
                }
                
                success = true;
                
                SRKF.LOG.log(Level.INFO, "[SRKF] Created Shop Exit ID " + ID + " for map + " + mapName + " at " + x + ", " + y + ", " + z);
            }
            else
            {
                SRKF.LOG.log(Level.WARNING, "[SRKF] Not connected to SQL database.");
            }
            
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        
        return success;
    }
    
    public void loadShops(int ID, String mapName, String worldName)
    {
        String doorMaterial = "Material.AIR";
        int count = 0;
        
        try
        {
            if (connected)
            {
                String SQL = "SELECT * FROM Shop_" + mapName.toLowerCase() + ";";
                this.statement = this.conn.createStatement();
                this.rs = this.statement.executeQuery(SQL);
                while(rs.next())
                {
                    Location[] loc = new Location[4];
        
                    Location tempdoorloc1 = null;
                    Location tempdoorloc2 = null;
                    Location shoploc = null;
                    Location exitloc = null;

                    

                    int doorx = 0;
                    int doory = 0;
                    int doorz = 0;

                    int doorx2 = 0;
                    int doory2 = 0;
                    int doorz2 = 0;

                    int shopx = 0;
                    int shopy = 0;
                    int shopz = 0;

                    int exitx = 0;
                    int exity = 0;
                    int exitz = 0;
                    
                    count++;
                    
                    doorx = this.rs.getInt("doorx");
                    doory = this.rs.getInt("doory");
                    doorz = this.rs.getInt("doorz");
                    
                    doorx2 = this.rs.getInt("doorx2");
                    doory2 = this.rs.getInt("doory2");
                    doorz2 = this.rs.getInt("doorz2");
                    
                    shopx = this.rs.getInt("shopx");
                    shopy = this.rs.getInt("shopy");
                    shopz = this.rs.getInt("shopz");
                    
                    exitx = this.rs.getInt("exitx");
                    exity = this.rs.getInt("exity");
                    exitz = this.rs.getInt("exitz");
                    
                    doorMaterial = this.rs.getString("doormaterial");
                    
                    try
                    {
                        World world = Bukkit.getWorld(worldName);
                        tempdoorloc1 = new Location(world, doorx, doory, doorz);
                        tempdoorloc2 = new Location(world, doorx2, doory2, doorz2);
                        shoploc = new Location(world, shopx, shopy, shopz);
                        exitloc = new Location(world, exitx, exity, exitz);
                        
                        loc[0] = exitloc;
                        loc[1] = tempdoorloc1;
                        loc[2] = tempdoorloc2;
                        loc[3] = shoploc;
                        
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                        SRKF.LOG.warning("[SRKF] - Could not create locations[] for loadShops()");
                    }
                    
                    if (loc.length > 0)
                    {
                        String mapID = mapName + "_" + ID;
                        String shopSpawn = "Shop_" + mapID + "_" + count;
                        if (Game.GameidShopnumberShoplocations.containsKey(shopSpawn))
                        {
                            Game.GameidShopnumberShoplocations.remove(shopSpawn);
                        }
                        Game.GameidShopnumberShoplocations.put(shopSpawn, loc);
                        
                        Material doorMat = Material.getMaterial(doorMaterial);
                        if (Game.GameidShopDoorwayMaterial.containsKey(shopSpawn))
                        {
                            Game.GameidShopDoorwayMaterial.remove(shopSpawn);
                        }
                        Game.GameidShopDoorwayMaterial.put(shopSpawn, doorMat);
                        
                        Location[] locs = Game.GameidShopnumberShoplocations.get(shopSpawn);

                        Location loc0 = locs[0];
                        Location loc1 = locs[1];
                        Location loc2 = locs[2];
                        Location loc3 = locs[3];

                        //Set door to the material

                        World w = loc1.getWorld();
                        int minx = Math.min(loc1.getBlockX(), loc2.getBlockX()),
                        miny = Math.min(loc1.getBlockY(), loc2.getBlockY()),
                        minz = Math.min(loc1.getBlockZ(), loc2.getBlockZ()),
                        maxx = Math.max(loc1.getBlockX(), loc2.getBlockX()),
                        maxy = Math.max(loc1.getBlockY(), loc2.getBlockY()),
                        maxz = Math.max(loc1.getBlockZ(), loc2.getBlockZ());
                        for (int x = minx; x<=maxx;x++)
                        {
                            for (int y = miny; y<=maxy;y++)
                            {
                                for (int z = minz; z<=maxz;z++)
                                {
                                    Block b = w.getBlockAt(x, y, z);
                                    Chunk c = b.getChunk();
                                    
                                    if (!(c.isLoaded()))
                                    {
                                        c.load();
                                    }
                                    
                                    if (b.getType() != doorMat)
                                    {
                                        b.setType(doorMat);
                                    }
                                }
                            }
                        }
                        
                        SRKF.LOG.log(Level.INFO, "[SRKF] - Loading Shop Location (" + count + ") into Memory for Key: " + shopSpawn);
                    }
                    else
                    {
                        SRKF.LOG.warning("[SRKF] - loadShops() loc.length FAILED.");
                    }
                }
                this.rs.close();
                this.statement.close();
            }
            else
            {
                SRKF.LOG.log(Level.WARNING, "[SRKF] Not connected to SQL database.");
            }
            
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        
        //Bukkit.broadcastMessage("Location is: " + loc + " || world is: " + worldName + " || x is: " + x + " || y is: " + y + " || z is: " + z);
       // Bukkit.broadcastMessage("Length is: " + loc.length);
        
    }
    
}
