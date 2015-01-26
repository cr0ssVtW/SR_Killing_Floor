package sr;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import sr.CustomEntities.CustomEntityType;
import sr.Extras.mobRemover;
import sr.Listeners.EL;
import sr.Listeners.ExtraListener;
import sr.Listeners.InventoryListener;
import sr.Listeners.PL;
import sr.Listeners.SignListener;
import sr.Managers.Commands;
import sr.Managers.Database;
import sr.Managers.PlayerDatabase;
import sr.Managers.SocketManager;
import sr.Tasks.doorTask;
import sr.Tasks.hungerRegenTask;
import sr.Tasks.mobTargettingTask;
import sr.Tasks.weldingTask;
import sr.coinStuff.EnhancementListener;
import sr.coinStuff.coinDB;
import sr.coinStuff.rankEL;

/**
 *
 * @author cr0ssVtW
 */
public class SRKF extends JavaPlugin {

    public static SRKF instance;
    public static Logger LOG = Logger.getLogger("Minecraft");
    public static boolean isLobby;
    public static WorldGuardPlugin worldGuard = WGBukkit.getPlugin();
    public static WorldEditPlugin wep;
    public PluginManager pm;

    public WorldEditPlugin getWorldEdit() 
    {
        Plugin plugin = pm.getPlugin("WorldEdit");

        if (plugin == null || !(plugin instanceof WorldEditPlugin)) 
        {
            return null;
        }

        return (WorldEditPlugin) plugin;
    }
    // Coin Stuff
    public static coinDB coindb;
    //
    public static PlayerDatabase player_dbManager;
    public static Database dbManager;
    public static SocketManager socketManager;
    public static File file;
    public static File file2;
    public static FileWriter logger;
    public static BufferedWriter logFile;
    public static FileConfiguration config;
    public static FileConfiguration spawns;

    public void loadConfig() {
        Boolean exists = new File("plugins/SRKF").exists();

        if (!exists) {
            Boolean create = new File("plugins/SRKF").mkdir();

            if (create) {
                LOG.info("[SRKF] - Directory /plugins/SRKF/ created.");
            }
        }
        file = new File("plugins/SRKF/config.yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
                LOG.info("[SRKF] - /plugins/SRKF/config.yml created.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        config = YamlConfiguration.loadConfiguration(file);

    }

    public void addDefault() {
        if (!config.contains("isLobby")) {
            try {
                config.set("isLobby", false);
                LOG.info("[SRKF] - [Config] isLobby defaulted to false.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String ip = Bukkit.getIp();

        // Socket Stuff

        int SocketPort = 51000;
        int proxySocketPort = 51050;


        if (!config.contains("socketPort")) {
            try {
                config.set("socketPort", SocketPort);
                LOG.info("[SRKF] - [Config] socketPort defaulted to " + SocketPort + ".");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (!config.contains("ServerAddress")) {
            try {
                config.set("ServerAddress", ip);
                LOG.info("[SRKF] - [Config] ServerAddress defaulted to " + ip + ".");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (!config.contains("ProxyIP")) {
            try {
                config.set("ProxyIP", ip);
                LOG.info("[SRKF] - [Config] ProxyIP defaulted to " + ip + ".");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (!config.contains("ProxySocketPort")) {
            try {
                config.set("ProxySocketPort", proxySocketPort);
                LOG.info("[SRKF] - [Config] ProxySocketPort defaulted to " + proxySocketPort + ".");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        // sql defaults

        int sqlPort = 3306;
        String sqlURL = "localhost";
        String dbName = "srkf";
        String userName = "cr0ss";
        String userPass = "x";

        if (!config.contains("sqlURL")) {
            try {
                config.set("sqlURL", sqlURL);
                LOG.info("[SRKF] - [Config] sqlURL defaulted to " + sqlURL + ".");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (!config.contains("sqlPort")) {
            try {
                config.set("sqlPort", sqlPort);
                LOG.info("[SRKF] - [Config] sqlPort defaulted to " + sqlPort + ".");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (!config.contains("Database")) {
            try {
                config.set("Database", dbName);
                LOG.info("[SRKF] - [Config] Database defaulted to " + dbName + ".");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (!config.contains("userName")) {
            try {
                config.set("userName", userName);
                LOG.info("[SRKF] - [Config] userName defaulted.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (!config.contains("userPass")) {
            try {
                config.set("userPass", userPass);
                LOG.info("[SRKF] - [Config] userPass defaulted.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 2nd sql - Player Database
        int playerSqlPort = 3306;
        String playerSqlURL = "localhost";
        String playerdbName = "srkf_stats";
        String playeruserName = "cr0ss";
        String playeruserPass = "x";

        if (!config.contains("playerSqlURL")) {
            try {
                config.set("playerSqlURL", playerSqlURL);
                LOG.info("[SRKF] - [Config] sqlURL defaulted to " + playerSqlURL + ".");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (!config.contains("playerSqlPort")) {
            try {
                config.set("playerSqlPort", playerSqlPort);
                LOG.info("[SRKF] - [Config] sqlPort defaulted to " + playerSqlPort + ".");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (!config.contains("playerdbName")) {
            try {
                config.set("playerdbName", playerdbName);
                LOG.info("[SRKF] - [Config] Database defaulted to " + playerdbName + ".");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (!config.contains("playeruserName")) {
            try {
                config.set("playeruserName", playeruserName);
                LOG.info("[SRKF] - [Config] playeruserName defaulted.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (!config.contains("playeruserPass")) {
            try {
                config.set("playeruserPass", playeruserPass);
                LOG.info("[SRKF] - [Config] playeruserPass defaulted.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //

        try {
            config.save(file);
            LOG.info("[SRKF] - [Config] Saved.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onEnable() {

        instance = this;

        loadConfig();
        LOG.info("[SRKF] - Loading config file. Checking if defaults needed...");

        if (file.exists()) {
            addDefault();
        }

        isLobby = config.getBoolean("isLobby");

        pm = Bukkit.getPluginManager();
        wep = getWorldEdit();
        boolean found = true;

        if (wep == null) {
            LOG.severe("[SRKF] - WorldEdit could not be found. Plugin disabled.");
            found = false;
        }

        if (!(found)) {
            pm.disablePlugin(this);
        }

        // register listeners
        getServer().getPluginManager().registerEvents(new SignListener(this), this);
        getServer().getPluginManager().registerEvents(new PL(this), this);
        getServer().getPluginManager().registerEvents(new EL(this), this);
        getServer().getPluginManager().registerEvents(new ExtraListener(this), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(this), this);
        getServer().getPluginManager().registerEvents(new rankEL(this), this);
        getServer().getPluginManager().registerEvents(new EnhancementListener(this), this);


        PluginDescriptionFile pdfFile = getDescription();
        LOG.log(Level.INFO, "[{0}] (By cr0ssVtW) - v{1} loaded.", new Object[]{pdfFile.getName(), pdfFile.getVersion()});

        // coin db
        coindb = new coinDB(this);

        // Socket
        socketManager = new SocketManager(this);

        // SQL
        dbManager = new Database(this);
        player_dbManager = new PlayerDatabase(this);



        LOG.log(Level.INFO, "[SRKF] - isLobby is: {0}", isLobby);

        if (dbManager.connected) 
        {
            dbManager.getGuns();
            LOG.info("[SRKF] - Guns loaded from database...");
            dbManager.getMelee();
            LOG.info("[SRKF] - Melee Weapons loaded from database...");
            dbManager.getDemo();
            LOG.info("[SRKF] - Demolitions loaded from database...");
            new hungerRegenTask(this);

            if (isLobby) {
                dbManager.setLobby();
                dbManager.loadSignsFromDB();
                LOG.info("[SRKF] - Signs loaded from database...");
            } else {
                try {
                    CustomEntityType.registerEntities();
                    LOG.info("[SRKF] - Registered Custom Entities Successfully.");
                } catch (Exception e) {
                    e.printStackTrace();
                    LOG.severe("[SRKF] - FAILED to register Custom Entities.");
                }




                /*
                 * Load in custom Entities
                 */

                /*
                 try
                 { 
                 @SuppressWarnings("rawtypes")
                 Class[] args = new Class[3];
                 args[0] = Class.class;
                 args[1] = String.class;
                 args[2] = int.class;
                 Method a = net.minecraft.server.v1_7_R1.EntityTypes.class.getDeclaredMethod("a", args);
                 a.setAccessible(true);
                 a.invoke(a, CustomEntityIronGolem.class, "IronGolem", 99);
                 LOG.info("[SRKF] - Iron Golem custom entity initiated...");
                 a.invoke(a, CustomEntitySkeleton.class, "Skeleton", 51);
                 LOG.info("[SRKF] - Skeleton custom entity initiated...");
                 }catch (Exception e)
                 {
                 e.printStackTrace();
                 this.setEnabled(false); 
                 LOG.severe("[SRKF] - FAILED TO LOAD CUSTOM ENTITIES. DISABLING PLUGIN.");
                 }
                 */


                dbManager.getMapNames();
                LOG.info("[SRKF] - Maps loaded from database...");

                /*
                 * Start tasks
                 */
                new mobRemover(this);
                new mobTargettingTask(this);
                new weldingTask(this);
                new doorTask(this);
            }
        } else {
            LOG.severe("Not connected to SQL databases. Disabling plugin.");
            this.setEnabled(false);
        }


        // Commands
        getCommand("srkf").setExecutor(new Commands(this));
        getCommand("perk").setExecutor(new Commands(this));
        getCommand("perks").setExecutor(new Commands(this));
        getCommand("role").setExecutor(new Commands(this));
        getCommand("roles").setExecutor(new Commands(this));
        getCommand("class").setExecutor(new Commands(this));
        getCommand("roleinfo").setExecutor(new Commands(this));
        getCommand("skip").setExecutor(new Commands(this));
        getCommand("autoreload").setExecutor(new Commands(this));
        getCommand("exit").setExecutor(new Commands(this));
        getCommand("quit").setExecutor(new Commands(this));
        getCommand("leave").setExecutor(new Commands(this));

    }

    @Override
    public void onDisable() {
        CustomEntityType.unregisterEntities();
        LOG.info("[SRKF] - Unregistering Entities.");
        dbManager.closeConnection();
        player_dbManager.closeConnection();
        LOG.info("[SRKF] - SQL connections closed. Plugin disabling...");
    }

    public static SRKF getInstance() {
        return instance;
    }
}
