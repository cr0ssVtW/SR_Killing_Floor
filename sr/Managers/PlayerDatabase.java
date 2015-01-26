package sr.Managers;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import sr.Extras.EXP;
import sr.Roles.Berserker;
import sr.Roles.Commando;
import sr.Roles.Engineer;
import sr.Roles.Medic;
import sr.Roles.Sharpshooter;
import sr.Extras.Guns;
import sr.Extras.PerkInfo;
import sr.Extras.Roles;
import sr.Game;
import sr.Listeners.PL;
import sr.Listeners.SignListener;
import sr.SRKF;

/**
 *
 * @author Cross
 */
public class PlayerDatabase 
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
    
    //private Socket socket;
    //private int ProxySocketPort = 51050;
   // private String proxyIP = "69.162.96.210";
    
    
    public PlayerDatabase (SRKF plugin)
    {
        this.plugin = plugin;
        
        establishConnection();
    }
    
    
    
    private void establishConnection()
    {
        this.URL = SRKF.config.getString("playerSqlURL");
        this.Port = SRKF.config.getInt("playerSqlPort");
        this.dbName = SRKF.config.getString("playerdbName");
        this.userName = SRKF.config.getString("playeruserName");
        this.password = SRKF.config.getString("playeruserPass");
        
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
            //SRKF.LOG.severe("Unable to close connection");
        }
    }
    
    /*
     * Do all player specific handling stuff here.
     */
    
    
    
    public boolean checkPlayerExists(String pName)
    {
        boolean isCreated = false;
        
        try
        {
            if (connected)
            {
                this.statement = this.conn.createStatement();
                this.rs = this.statement.executeQuery("SELECT Player from Players WHERE Player = '" + pName + "'");
                
                if (this.rs.next())
                {
                    isCreated = true;
                }
                
                this.rs.close();
                this.statement.close();
            }
            else
            {
                SRKF.LOG.log(Level.WARNING, "[SRKF] Not connected to SQL database " + this.dbName);
            }
            
        } catch (SQLException e)
        {
            SRKF.LOG.log(Level.SEVERE, "[SRKF] - Failed checkPlayerExists (player: " + pName + ") for DB: {0}", this.dbName);
            e.printStackTrace();
        }
        
        return isCreated;
    }
    
    public void createNewPlayer(String pName)
    {
        String defaultRole = "Commando";
        
        int size = Roles.roleNames.size();
        int item = new Random().nextInt(size); // In real life, the Random object should be rather more shared than this
        
        int i = 0;
        
        for(String rName : Roles.roleNames)
        {
            if (i == item)
            {
                defaultRole = rName;
                break;
            }
            
            i = i + 1;
        }

        
        try
        {
            if (connected)
            {
                boolean isCreated = checkPlayerExists(pName);
                
                if (!(isCreated))
                {
                    createNewPlayerInventory(pName, defaultRole);
                    
                    createBerserker(pName);
                    createCommando(pName);
                    createEngineer(pName);
                    createMedic(pName);
                    createSharpshooter(pName);
                    
                    this.statement = this.conn.createStatement();
                    this.statement.executeUpdate("INSERT INTO Players(Player, Games, Kills, Deaths, Shots, Hits, Class, EXP, UnspentPoints, SpentPoints) "
                            + "VALUES('" + pName + "','" + 0 + "','" + 0 + "','" + 0 + "','" + 0 + "','" + 0 + "','" + defaultRole + "','" + 0 + "','" + PerkInfo.defaultPoints + "','" + 0 + "');");

                    this.statement.close();
                    
                    OfflinePlayer op = Bukkit.getOfflinePlayer(pName);
                    if (op.isOnline())
                    {
                        Player newP = (Player) op;
                        newP.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " 
                                + ChatColor.GREEN + "Welcome to SRKF!");
                        newP.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " 
                                + ChatColor.GREEN + "Since you are new here, you have been randomly assigned a role " + ChatColor.YELLOW + "(" + defaultRole + ")"
                                + ChatColor.GREEN + " with a default Inventory loadout!");
                        newP.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " 
                                + ChatColor.GREEN + "To change your role, type: " + ChatColor.GOLD + "/role");
                        newP.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " 
                                + ChatColor.GREEN + "To learn more about a role, type: " + ChatColor.GOLD + "/roleinfo <rolename>" + ChatColor.GREEN + " (note: just </roleinfo> displays info about YOUR role.)");
                        //newP.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " 
                        //        + ChatColor.GREEN + "To see your Inventory Loadout, type: " + ChatColor.GOLD + "/inventory");
                        newP.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " 
                                + ChatColor.GREEN + "Visit our website and create an account to change your Inventory Loadouts! Loadouts can only be changed in the Lobby, never while in a game.");
                        newP.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] "
                                + ChatColor.GOLD + "You now have " + ChatColor.AQUA + "3 Unspent Skill Points" + ChatColor.GOLD + " with which to level your perks!");
                        newP.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " 
                                + ChatColor.DARK_AQUA + "You will earn EXP from defeating monsters and clearing rounds within " + ChatColor.DARK_RED + "Killing Floor.");
                        newP.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " 
                                + ChatColor.DARK_AQUA + "As you level up, you will earn Skill Points which you can spend on leveling even more perks!");
                        newP.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " 
                                + ChatColor.GREEN + "To see or edit your Role Perks, type: " + ChatColor.GOLD + "/perks");
                    }

                    SRKF.LOG.log(Level.WARNING, "[SRKF] Created new Player: " + pName + " in Players database.");
                    
                    SRKF.player_dbManager.loadPlayerRoleStats(pName);
                }
                else
                {
                    SRKF.player_dbManager.loadPlayerRoleStats(pName);
                }
            }
            else
            {
                SRKF.LOG.log(Level.WARNING, "[SRKF] Not connected to SQL database.");
            }

        } catch (SQLException e)
        {
            SRKF.LOG.log(Level.SEVERE, "[SRKF] - Failed createNewPlayer()");
            e.printStackTrace();
        }
    }
    
    public void createNewPlayerInventory(String pName, String roleName)
    {
        String primary = Guns.getDefaultPrimary(roleName);
        String secondary = Guns.getDefaultSecondary(roleName);
        
        try
        {
            if (connected)
            {            
                this.statement = this.conn.createStatement();
                this.statement.executeUpdate("INSERT INTO Player_Loadout(Player, PrimaryWeapon, SecondaryWeapon) "
                        + "VALUES('" + pName + "','" + primary + "','" + secondary + "');");

                this.statement.close();

                SRKF.LOG.log(Level.WARNING, "[SRKF] Created new Player Inventory for : " + pName + " in Player_Loadout database.");
            }
            else
            {
                SRKF.LOG.log(Level.WARNING, "[SRKF] Not connected to SQL database.");
            }

        } catch (SQLException e)
        {
            SRKF.LOG.log(Level.SEVERE, "[SRKF] - Failed createPlayerInventory()");
            e.printStackTrace();
        }
    }
    
    public void createBerserker(String pName)
    {
        try
        {
            if (connected)
            {
                boolean isCreated = checkPlayerExists(pName);
                
                if (!(isCreated))
                {
                    this.statement = this.conn.createStatement();
                    this.statement.executeUpdate("INSERT INTO Class_Berserker(Player, Endurance, Strength, KevlarAdaptation, Ferocity) "
                            + "VALUES('" + pName + "','" + 0 + "','" + 0 + "','" + 0 + "','" + 0 + "');");

                    this.statement.close();

                    SRKF.LOG.log(Level.WARNING, "[SRKF] Created new Berserker: " + pName + " in Class_Berserker database.");
                }
            }
            else
            {
                SRKF.LOG.log(Level.WARNING, "[SRKF] Not connected to SQL database.");
            }

        } catch (SQLException e)
        {
            SRKF.LOG.log(Level.SEVERE, "[SRKF] - Failed createBerserker()");
            e.printStackTrace();
        }
    }
    
    public void createCommando(String pName)
    {
        try
        {
            if (connected)
            {
                boolean isCreated = checkPlayerExists(pName);
                
                if (!(isCreated))
                {
                    this.statement = this.conn.createStatement();
                    this.statement.executeUpdate("INSERT INTO Class_Commando(Player, RangerInstructor, Pain, Juggernaut, Inspire) "
                            + "VALUES('" + pName + "','" + 0 + "','" + 0 + "','" + 0 + "','" + 0 + "');");

                    this.statement.close();

                    SRKF.LOG.log(Level.WARNING, "[SRKF] Created new Commando: " + pName + " in Class_Commando database.");
                }
            }
            else
            {
                SRKF.LOG.log(Level.WARNING, "[SRKF] Not connected to SQL database.");
            }

        } catch (SQLException e)
        {
            SRKF.LOG.log(Level.SEVERE, "[SRKF] - Failed createCommando()");
            e.printStackTrace();
        }
    }
    
    public void createEngineer(String pName)
    {
        try
        {
            if (connected)
            {
                boolean isCreated = checkPlayerExists(pName);
                
                if (!(isCreated))
                {
                    this.statement = this.conn.createStatement();
                    this.statement.executeUpdate("INSERT INTO Class_Engineer(Player, BallisticTraining, Welding, DemolitionMastery, HeavyArtillery) "
                            + "VALUES('" + pName + "','" + 0 + "','" + 0 + "','" + 0 + "','" + 0 + "');");

                    this.statement.close();

                    SRKF.LOG.log(Level.WARNING, "[SRKF] Created new Engineer: " + pName + " in Class_Engineer database.");
                }
            }
            else
            {
                SRKF.LOG.log(Level.WARNING, "[SRKF] Not connected to SQL database.");
            }

        } catch (SQLException e)
        {
            SRKF.LOG.log(Level.SEVERE, "[SRKF] - Failed createEngineer()");
            e.printStackTrace();
        }
    }
    
    public void createMedic(String pName)
    {
        try
        {
            if (connected)
            {
                boolean isCreated = checkPlayerExists(pName);
                
                if (!(isCreated))
                {
                    this.statement = this.conn.createStatement();
                    this.statement.executeUpdate("INSERT INTO Class_Medic(Player, MedicalTraining, ShotgunMastery, Triage, CombatArmor) "
                            + "VALUES('" + pName + "','" + 0 + "','" + 0 + "','" + 0 + "','" + 0 + "');");

                    this.statement.close();

                    SRKF.LOG.log(Level.WARNING, "[SRKF] Created new Medic: " + pName + " in Class_Medic database.");
                }
            }
            else
            {
                SRKF.LOG.log(Level.WARNING, "[SRKF] Not connected to SQL database.");
            }

        } catch (SQLException e)
        {
            SRKF.LOG.log(Level.SEVERE, "[SRKF] - Failed createMedic()");
            e.printStackTrace();
        }
    }
    
    public void createSharpshooter(String pName)
    {
        try
        {
            if (connected)
            {
                boolean isCreated = checkPlayerExists(pName);
                
                if (!(isCreated))
                {
                    this.statement = this.conn.createStatement();
                    this.statement.executeUpdate("INSERT INTO Class_Sharpshooter(Player, MarksmanTraining, CalledShot, Lucky, Wound) "
                            + "VALUES('" + pName + "','" + 0 + "','" + 0 + "','" + 0 + "','" + 0 + "');");

                    this.statement.close();

                    SRKF.LOG.log(Level.WARNING, "[SRKF] Created new Sharpshooter: " + pName + " in Class_Sharpshooter database.");
                }
            }
            else
            {
                SRKF.LOG.log(Level.WARNING, "[SRKF] Not connected to SQL database.");
            }

        } catch (SQLException e)
        {
            SRKF.LOG.log(Level.SEVERE, "[SRKF] - Failed createSharpshooter()");
            e.printStackTrace();
        }
    }
    
    public void setRole(String pName, String roleName)
    {
        try
        {
            if (connected)
            {
                String SQL = "UPDATE Players SET Class = '" + roleName + "' WHERE Player= '" + pName + "';";
                this.statement = this.conn.createStatement();
                this.updateQuery = this.statement.executeUpdate(SQL);
                this.statement.close();
                
                SRKF.player_dbManager.loadPlayerRoleStats(pName);
                
                SRKF.LOG.log(Level.INFO, "[SRKF] - Updated Role for Player: " + pName + " to role: " + roleName);
            }
            else
            {
                SRKF.LOG.log(Level.WARNING, "[SRKF] Not connected to SQL database {0}", this.dbName);
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    
    public String getRole(String pName)
    {
        String roleName = "Null";
        
        try
        {
            if (connected)
            {
                String SQL = "SELECT Class FROM Players WHERE Player = '" + pName + "';";
                this.statement = this.conn.createStatement();
                this.rs = this.statement.executeQuery(SQL);
                
                if (this.rs.next())
                {
                    roleName = this.rs.getString("Class");
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
        
        return roleName;
    }
    
    public String getStringLoadoutItem(String pName, String ItemName)
    {
        String itemName = "Null";
        
        try
        {
            if (connected)
            {
               String SQL = "SELECT " + ItemName + " FROM Player_Loadout WHERE Player = '" + pName + "';";
                this.statement = this.conn.createStatement();
                this.rs = this.statement.executeQuery(SQL);
                
                if (this.rs.next())
                {
                    itemName = this.rs.getString(ItemName);
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
        
        return itemName;
    }
    
    public int getIntLoadoutItem(String pName, String ItemName)
    {
        int amount = 0;
        
        try
        {
            if (connected)
            {
               String SQL = "SELECT " + ItemName + " FROM Player_Loadout WHERE Player = '" + pName + "';";
                this.statement = this.conn.createStatement();
                this.rs = this.statement.executeQuery(SQL);
                
                if (this.rs.next())
                {
                    amount = this.rs.getInt(ItemName);
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
        
        return amount;
    }
    
    public void loadPlayerRoleStats(String pName)
    {
        String roleName = getRole(pName);
        
        try
        {
            if (connected)
            {
                String SQL = "SELECT * FROM Class_" + roleName + " WHERE Player = '" + pName + "';";
                this.statement = this.conn.createStatement();
                this.rs = this.statement.executeQuery(SQL);
                
                if (this.rs.next())
                {
                    if (roleName.equalsIgnoreCase("Berserker"))
                    {
                        int stat_Endurance = this.rs.getInt("Endurance");
                        int stat_Strength = this.rs.getInt("Strength");
                        int stat_KevlarAdaptation = this.rs.getInt("KevlarAdaptation");
                        int stat_Ferocity = this.rs.getInt("Ferocity");
                        
                        if (Berserker.Stats_Endurance.containsKey(pName))
                        {
                            Berserker.Stats_Endurance.remove(pName);
                        }
                        if (Berserker.Stats_Strength.containsKey(pName))
                        {
                            Berserker.Stats_Strength.remove(pName);
                        }
                        if (Berserker.Stats_KevlarAdaptation.containsKey(pName))
                        {
                            Berserker.Stats_KevlarAdaptation.remove(pName);
                        }
                        if (Berserker.Stats_Ferocity.containsKey(pName))
                        {
                            Berserker.Stats_Ferocity.remove(pName);
                        }
                        
                        Berserker.Stats_Endurance.put(pName, stat_Endurance);
                        Berserker.Stats_Strength.put(pName, stat_Strength);
                        Berserker.Stats_KevlarAdaptation.put(pName, stat_KevlarAdaptation);
                        Berserker.Stats_Ferocity.put(pName, stat_Ferocity);
                    } // end berserker load
                    
                    if (roleName.equalsIgnoreCase("Commando"))
                    {
                        int stat_RangerInstructor = this.rs.getInt("RangerInstructor");
                        int stat_Pain = this.rs.getInt("Pain");
                        int stat_Juggernaut = this.rs.getInt("Juggernaut");
                        int stat_Inspire = this.rs.getInt("Inspire");
                        
                        if (Commando.Stats_RangerInstructor.containsKey(pName))
                        {
                            Commando.Stats_RangerInstructor.remove(pName);
                        }
                        if (Commando.Stats_Pain.containsKey(pName))
                        {
                            Commando.Stats_Pain.remove(pName);
                        }
                        if (Commando.Stats_Juggernaut.containsKey(pName))
                        {
                            Commando.Stats_Juggernaut.remove(pName);
                        }
                        if (Commando.Stats_Inspire.containsKey(pName))
                        {
                            Commando.Stats_Inspire.remove(pName);
                        }
                        
                        Commando.Stats_RangerInstructor.put(pName, stat_RangerInstructor);
                        Commando.Stats_Pain.put(pName, stat_Pain);
                        Commando.Stats_Juggernaut.put(pName, stat_Juggernaut);
                        Commando.Stats_Inspire.put(pName, stat_Inspire);
                    } // end commando load
                    
                    if (roleName.equalsIgnoreCase("Engineer"))
                    {
                        int stat_BallisticTraining = this.rs.getInt("BallisticTraining");
                        int stat_Welding = this.rs.getInt("Welding");
                        int stat_DemolitionMastery = this.rs.getInt("DemolitionMastery");
                        int stat_HeavyArtillery = this.rs.getInt("HeavyArtillery");
                        
                        if (Engineer.Stats_BallisticTraining.containsKey(pName))
                        {
                            Engineer.Stats_BallisticTraining.remove(pName);
                        }
                        if (Engineer.Stats_Welding.containsKey(pName))
                        {
                            Engineer.Stats_Welding.remove(pName);
                        }
                        if (Engineer.Stats_DemolitionMastery.containsKey(pName))
                        {
                            Engineer.Stats_DemolitionMastery.remove(pName);
                        }
                        if (Engineer.Stats_HeavyArtillery.containsKey(pName))
                        {
                            Engineer.Stats_HeavyArtillery.remove(pName);
                        }
                        
                        Engineer.Stats_BallisticTraining.put(pName, stat_BallisticTraining);
                        Engineer.Stats_Welding.put(pName, stat_Welding);
                        Engineer.Stats_DemolitionMastery.put(pName, stat_DemolitionMastery);
                        Engineer.Stats_HeavyArtillery.put(pName, stat_HeavyArtillery);
                    } // end engineer load
                    
                    if (roleName.equalsIgnoreCase("Medic"))
                    {
                        int stat_MedicalTraining = this.rs.getInt("MedicalTraining");
                        int stat_ShotgunMastery = this.rs.getInt("ShotgunMastery");
                        int stat_Triage = this.rs.getInt("Triage");
                        int stat_CombatArmor = this.rs.getInt("CombatArmor");
                        
                        if (Medic.Stats_MedKit.containsKey(pName))
                        {
                            Medic.Stats_MedKit.remove(pName);
                        }
                        if (Medic.Stats_Shotguns.containsKey(pName))
                        {
                            Medic.Stats_Shotguns.remove(pName);
                        }
                        if (Medic.Stats_Triage.containsKey(pName))
                        {
                            Medic.Stats_Triage.remove(pName);
                        }
                        if (Medic.Stats_CombatArmor.containsKey(pName))
                        {
                            Medic.Stats_CombatArmor.remove(pName);
                        }
                        
                        Medic.Stats_MedKit.put(pName, stat_MedicalTraining);
                        Medic.Stats_Shotguns.put(pName, stat_ShotgunMastery);
                        Medic.Stats_Triage.put(pName, stat_Triage);
                        Medic.Stats_CombatArmor.put(pName, stat_CombatArmor);
                    } // end medic load
                    
                    if (roleName.equalsIgnoreCase("Sharpshooter"))
                    {
                        int stat_Marksman = this.rs.getInt("MarksmanTraining");
                        int stat_CalledShot = this.rs.getInt("CalledShot");
                        int stat_Lucky = this.rs.getInt("Lucky");
                        int stat_Wound = this.rs.getInt("Wound");
                        
                        if (Sharpshooter.Stats_MarksmanTraining.containsKey(pName))
                        {
                            Sharpshooter.Stats_MarksmanTraining.remove(pName);
                        }
                        if (Sharpshooter.Stats_CalledShot.containsKey(pName))
                        {
                            Sharpshooter.Stats_CalledShot.remove(pName);
                        }
                        if (Sharpshooter.Stats_Lucky.containsKey(pName))
                        {
                            Sharpshooter.Stats_Lucky.remove(pName);
                        }
                        if (Sharpshooter.Stats_Wound.containsKey(pName))
                        {
                            Sharpshooter.Stats_Wound.remove(pName);
                        }
                        
                        Sharpshooter.Stats_MarksmanTraining.put(pName, stat_Marksman);
                        Sharpshooter.Stats_CalledShot.put(pName, stat_CalledShot);
                        Sharpshooter.Stats_Lucky.put(pName, stat_Lucky);
                        Sharpshooter.Stats_Wound.put(pName, stat_Wound);
                    } // end sharpshooter load
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
    
    public int getRoleStat (String pName, String roleName, String statColumn)
    {
        int amount = 0;
        
        try
        {
            if (connected)
            {
               String SQL = "SELECT " + statColumn + " FROM Class_" + roleName + " WHERE Player = '" + pName + "';";
                this.statement = this.conn.createStatement();
                this.rs = this.statement.executeQuery(SQL);
                
                if (this.rs.next())
                {
                    amount = this.rs.getInt(statColumn);
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
        
        return amount;
    }
    
    public int getPlayerStat (String pName, String statColumn)
    {
        int amount = 0;
        
        try
        {
            if (connected)
            {
               String SQL = "SELECT " + statColumn + " FROM Players WHERE Player = '" + pName + "';";
                this.statement = this.conn.createStatement();
                this.rs = this.statement.executeQuery(SQL);
                
                if (this.rs.next())
                {
                    amount = this.rs.getInt(statColumn);
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
        
        return amount;
    }
    
    public void updatePlayerStats(String pName, boolean isDead, int currentRound, boolean gotChest)
    {
        try
        {
            if (connected)
            {
                boolean exists = checkPlayerExists(pName);
                if (exists)
                {
                    int games = getPlayerStat(pName, "Games");
                    int kills = getPlayerStat(pName, "Kills");
                    if (PL.playerKills.containsKey(pName))
                    {
                        kills = kills + PL.playerKills.get(pName);
                    }
                    int shots = getPlayerStat(pName, "Shots");
                    if (PL.playerShots.containsKey(pName))
                    {
                        shots = shots + PL.playerShots.get(pName);
                    }
                    int hits = getPlayerStat(pName, "Hits");
                    if (PL.playerHits.containsKey(pName))
                    {
                        hits = hits + PL.playerHits.get(pName);
                    }
                    int exp = getPlayerStat(pName, "EXP");
                    if (EXP.playerEXP.containsKey(pName))
                    {
                        exp = exp + EXP.playerEXP.get(pName);
                    }
                   // Bukkit.broadcastMessage("EXP is " + exp);
                    int oldLevel = getPlayerStat(pName, "Level");
                   // Bukkit.broadcastMessage("oldLevel is: " + oldLevel);
                    

                    int levelCheck = 0;
                    for (int xpValue : EXP.levelAmounts.keySet())
                    {
                       // Bukkit.broadcastMessage("xpValue is: " + xpValue);
                        if (exp >= xpValue)
                        {
                            levelCheck = EXP.levelCosts.get(xpValue);
                          //  Bukkit.broadcastMessage("levelCheck is: " + levelCheck);
                        }
                    }
                    
                    
                    int newLevel = levelCheck;
                    //Bukkit.broadcastMessage("newLevel is: " + newLevel);
                    int unspentPoints = getPlayerStat(pName, "UnspentPoints");
                    if (newLevel > oldLevel)
                    {
                        int howmuch = newLevel - oldLevel;
                       // Bukkit.broadcastMessage("howmuch is: " + howmuch);
                        unspentPoints = unspentPoints + howmuch;
                       // Bukkit.broadcastMessage("unspentPoints is: " + unspentPoints);
                    }
                    
                    int deaths = getPlayerStat(pName, "Deaths");
                    if (isDead)
                    {
                        deaths = deaths + 1;
                    }
                    
                    double coins = 0;
                    if (Game.playerCoins.containsKey(pName))
                    {
                        coins = Game.playerCoins.get(pName);
                    }
                    SRKF.coindb.UpdateCoins(pName.toLowerCase(), coins);
                    
                    int lastHighRound = getPlayerStat(pName, "HighestRound");
                    int updateRound = 0;
                    if (currentRound > lastHighRound)
                    {
                        updateRound = currentRound;
                    }
                    else
                    {
                        updateRound = lastHighRound;
                    }
                    
                    //Bukkit.broadcastMessage("EXP is: " + exp);
                    String SQL = "UPDATE Players SET Games='" + (games + 1) + "', Kills='" + kills + "', Deaths='" + deaths + "', Shots='" + shots + "', Hits='" + hits + "'," +
                            " EXP='" + exp + "', Level='" + newLevel + "', UnspentPoints='" + unspentPoints + "', HighestRound='" + updateRound + "'" +
                            " WHERE Player= '" + pName + "';";
                    this.statement = this.conn.createStatement();
                    this.updateQuery = this.statement.executeUpdate(SQL);
                    this.statement.close();

                    SRKF.LOG.log(Level.INFO, "[SRKF] - Updated stats for Player: " + pName);
                    
                    if (gotChest)
                    {
                        SRKF.coindb.addChest(pName.toLowerCase());
                    }
                }
                
            }
            else
            {
                SRKF.LOG.log(Level.WARNING, "[SRKF] Not connected to SQL database.");
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
            SRKF.LOG.warning("[SRKF] - Stacktrace above. Failed updatePlayerStats() for player: " + pName);
        }
    }
    
    public void playerSpendPoint(String pName)
    {
        int pointsUnspent = getPlayerStat(pName, "UnspentPoints");
        int pointsSpent = getPlayerStat(pName, "SpentPoints");
        
        try
        {
            if (connected)
            {
                //Bukkit.broadcastMessage("EXP is: " + exp);
                String SQL = "UPDATE Players SET UnspentPoints='" + (pointsUnspent - 1) + "', SpentPoints='" + (pointsSpent + 1) + "' WHERE Player= '" + pName + "';";
                this.statement = this.conn.createStatement();
                this.updateQuery = this.statement.executeUpdate(SQL);
                this.statement.close();

                SRKF.LOG.log(Level.INFO, "[SRKF] - Spent point for Player: " + pName);
            }
            else
            {
                SRKF.LOG.log(Level.WARNING, "[SRKF] Not connected to SQL database.");
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
            SRKF.LOG.warning("[SRKF] - Stacktrace. Failed levelPerkStat() for player: " + pName);
        }
    }
    
    public void respecPerks(String pName)
    {
        int pointsUnspent = getPlayerStat(pName, "UnspentPoints");
        int pointsSpent = getPlayerStat(pName, "SpentPoints");
        
        int totalPoints = pointsUnspent + pointsSpent;
        boolean hasCoins = false;
        double coins = SRKF.coindb.getCoins(pName.toLowerCase());
        double coinCost = Roles.respecCoinCost;
        
        // do coin check
        if (coins >= coinCost)
        {
            // update coins
            SRKF.coindb.spendCoins(pName, coinCost);
            
            // do respec
            try
            {
                if (connected)
                {
                    //Bukkit.broadcastMessage("EXP is: " + exp);
                    String SQL = "UPDATE Players SET UnspentPoints='" + (totalPoints) + "', SpentPoints='" + (0) + "' WHERE Player= '" + pName + "';";
                    this.statement = this.conn.createStatement();
                    this.updateQuery = this.statement.executeUpdate(SQL);
                    this.statement.close();

                    // update all other perks now
                    
                    setPerkStat(pName, "Berserker", "Strength", 0);
                    setPerkStat(pName, "Berserker", "Ferocity", 0);
                    setPerkStat(pName, "Berserker", "KevlarAdaptation", 0);
                    setPerkStat(pName, "Berserker", "Endurance", 0);
                    
                    setPerkStat(pName, "Commando", "Pain", 0);
                    setPerkStat(pName, "Commando", "RangerInstructor", 0);
                    setPerkStat(pName, "Commando", "Juggernaut", 0);
                    setPerkStat(pName, "Commando", "Inspire", 0);
                    
                    setPerkStat(pName, "Engineer", "BallisticTraining", 0);
                    setPerkStat(pName, "Engineer", "Welding", 0);
                    setPerkStat(pName, "Engineer", "DemolitionMastery", 0);
                    setPerkStat(pName, "Engineer", "HeavyArtillery", 0);
                    
                    setPerkStat(pName, "Medic", "MedicalTraining", 0);
                    setPerkStat(pName, "Medic", "ShotgunMastery", 0);
                    setPerkStat(pName, "Medic", "Triage", 0);
                    setPerkStat(pName, "Medic", "CombatArmor", 0);
                    
                    setPerkStat(pName, "Sharpshooter", "MarksmanTraining", 0);
                    setPerkStat(pName, "Sharpshooter", "CalledShot", 0);
                    setPerkStat(pName, "Sharpshooter", "Lucky", 0);
                    setPerkStat(pName, "Sharpshooter", "Wound", 0);
                    
                    SRKF.LOG.log(Level.INFO, "[SRKF] - Spent point for Player: " + pName);
                }
                else
                {
                    SRKF.LOG.log(Level.WARNING, "[SRKF] Not connected to SQL database.");
                }
            } catch (SQLException e)
            {
                e.printStackTrace();
                SRKF.LOG.warning("[SRKF] - Stacktrace. Failed respecPerks() for player: " + pName);
            }
        }
        
        
    }
    
    public void setPerkStat(String pName, String roleName, String perkName, int level)
    {
        try
        {
            if (connected)
            {
                //Bukkit.broadcastMessage("EXP is: " + exp);
                String SQL = "UPDATE Class_" + roleName + " SET " + perkName + "='" + (level) + "' WHERE Player= '" + pName + "';";
                this.statement = this.conn.createStatement();
                this.updateQuery = this.statement.executeUpdate(SQL);
                this.statement.close();

                //playerSpendPoint(pName);
                //Player player = Bukkit.getPlayer(pName);
                //player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.GREEN + "You have leveled up your " + perkName + "!");

                SRKF.LOG.log(Level.INFO, "[SRKF] - Updated perk: " + perkName + " for Player: " + pName + " to " + (level));
            }
            else
            {
                SRKF.LOG.log(Level.WARNING, "[SRKF] Not connected to SQL database.");
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
            SRKF.LOG.warning("[SRKF] - Stacktrace. Failed updatePerkStat() for player: " + pName);
        }
    }
    
    public void levelPerkStat(String pName, String roleName, String perkName)
    {
        int unspentPoints = getPlayerStat(pName, "UnspentPoints");
        
        if (unspentPoints > 0)
        {
            int level = getRoleStat(pName, roleName, perkName);
        
            if (level >= PerkInfo.MaxPerkLevel)
            {
                Player player = Bukkit.getPlayer(pName);
                player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "You have reached the Maximum Skill Level in " + ChatColor.GOLD + perkName + "!");
                return;
            }
            
            try
            {
                if (connected)
                {
                    //Bukkit.broadcastMessage("EXP is: " + exp);
                    String SQL = "UPDATE Class_" + roleName + " SET " + perkName + "='" + (level + 1) + "' WHERE Player= '" + pName + "';";
                    this.statement = this.conn.createStatement();
                    this.updateQuery = this.statement.executeUpdate(SQL);
                    this.statement.close();
                    
                    playerSpendPoint(pName);
                    Player player = Bukkit.getPlayer(pName);
                    player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.GREEN + "You have leveled up your " + perkName + "!");
                    
                    SRKF.LOG.log(Level.INFO, "[SRKF] - Leveled perk: " + perkName + " for Player: " + pName + " from " + level + " to " + (level + 1));
                }
                else
                {
                    SRKF.LOG.log(Level.WARNING, "[SRKF] Not connected to SQL database.");
                }
            } catch (SQLException e)
            {
                e.printStackTrace();
                SRKF.LOG.warning("[SRKF] - Stacktrace. Failed levelPerkStat() for player: " + pName);
            }
        }
        else
        {
            Player player = Bukkit.getPlayer(pName);
            player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "You do not have points to spend!");
            player.closeInventory();
        }
        
        
    }
}
