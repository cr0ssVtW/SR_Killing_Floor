package sr.Managers;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashSet;
import java.util.Iterator;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import sr.Extras.MapNames;
import sr.Extras.Roles;
import sr.Game;
import sr.Listeners.SignListener;
import sr.SRKF;
import sr.classChooser;
import sr.perkChooser;
import sr.roundChanger;

/**
 *
 * @author Cross
 */
public class Commands implements CommandExecutor
{    
    public SRKF plugin;
    public SignListener sl;
    
    public Commands (SRKF plugin)
    {
        this.plugin = plugin;
    }
    
    
    public static WorldGuardPlugin worldGuard = WGBukkit.getPlugin();
    WorldEditPlugin we = SRKF.wep;
    private Socket socket;
    private int ProxySocketPort = 51050;
    private String proxyIP = "69.162.96.210";
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args)
    {
        Player player = null;
        
        if (sender instanceof Player)
        {
            player = (Player) sender;
        }

        
        if (cmdLabel.equalsIgnoreCase("exit") || cmdLabel.equalsIgnoreCase("quit") || cmdLabel.equalsIgnoreCase("leave"))
        {
            player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + "Exiting back to Lobby...");
            String msgType = "backtolobby";
            String playerName = player.getName();

            try
            {
                String proxyIP = SRKF.config.getString("ProxyIP");
                int ProxySocketPort = SRKF.config.getInt("ProxySocketPort");

                Socket socket = new Socket(proxyIP, ProxySocketPort);
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

                oos.writeObject(new String[]
                {
                   msgType, playerName
                });


                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                if (((String)ois.readObject()).equalsIgnoreCase("true"))
                {
                    SRKF.LOG.log(Level.INFO, "[SRKF] - Fired off exit/quit command: " + msgType + " and received reply for " + proxyIP + " on port " + ProxySocketPort);
                }

                ois.close();
                oos.close();
                socket.close();


            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        
        if (cmdLabel.equalsIgnoreCase("skip"))
        {
            if (args.length > 0)
            {
                return true;
            }
            
            if (SRKF.isLobby)
            {
                return true;
            }
            
            if (Game.playersMaps.containsKey(player.getName()))
            {
                int ID = Game.playersMaps.get(player.getName());
                
                if (Game.isRoundChanging.containsKey(ID))
                {
                    int currentRound = Game.mapRound.get(ID);
                    
                    if (!(Game.gameRoundSkip.containsKey(player.getName())))
                    {
                        Game.gameRoundSkip.put(player.getName(), ID);
                        
                        
                        int voteCount = 0;
                        if (Game.mapSkipAmount.containsKey(ID))
                        {
                            voteCount = Game.mapSkipAmount.get(ID);
                        }
                        voteCount = voteCount + 1;
                        
                        int playerCount = 0;
                        for (int gameID : Game.playersMaps.values())
                        {
                            if (ID == gameID)
                            {
                                playerCount = playerCount + 1;
                            }
                        }
                        
                        int votesNeeded = (int) Math.round((double) playerCount * 0.67);
                        
                        if (playerCount == 2)
                        {
                            votesNeeded = 2;
                        }
                        
                        
                        if (voteCount >= votesNeeded)
                        {
                            // skip round
                            
                            if (Game.mapSkipAmount.containsKey(ID))
                            {
                                Game.mapSkipAmount.remove(ID);
                            }
                            
                            if (Game.mapIDBeingSkipped.contains(ID));
                            {
                                Game.mapIDBeingSkipped.remove(ID);
                            }
                            Game.mapIDBeingSkipped.add(ID);
                            
                            new roundChanger(plugin, ID, currentRound);
                            
                            for (String name : Game.playersMaps.keySet())
                            {
                                int id = Game.playersMaps.get(name);
                                if (id == ID)
                                {
                                    Player p = Bukkit.getPlayer(name);
                                    p.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.GREEN + "Round is being skipped!");
                                }
                            }
                        }
                        else
                        {
                            if (Game.mapSkipAmount.containsKey(ID))
                            {
                                Game.mapSkipAmount.remove(ID);
                            }
                            Game.mapSkipAmount.put(ID, voteCount);
                            
                            for (String name : Game.playersMaps.keySet())
                            {
                                int id = Game.playersMaps.get(name);
                                if (id == ID)
                                {
                                    Player p = Bukkit.getPlayer(name);
                                    p.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.GOLD + player.getName() + ChatColor.DARK_AQUA + " casts a vote to skip round change. Votes: " + voteCount + " | Required: " + votesNeeded);
                                }
                            }
                        }
                        
                    }
                    else
                    {
                        player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "You already voted to skip!");
                        return true;
                    }
                    
                    
                }
                else
                {
                    player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "Not currently in between rounds!");
                    return true;
                }
                
            }
        }
        
        if (cmdLabel.equalsIgnoreCase("roleinfo"))
        {
            if (args.length > 0)
            {
                if (args.length == 1)
                {
                    String className = args[0];
                
                    if (Roles.roleNames.contains(className))
                    {
                        String msg = Roles.roleDetailedInfo.get(className);
                        player.sendMessage(msg);
                    }
                    else
                    {
                        player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "That role doesn't exist!");
                    }
                }
                else
                {
                    player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "Wrong Syntax: Try: /roleinfo <rollName> (ex: /roleinfo Medic)");
                }
            }
            else
            {
                String pRole = SRKF.player_dbManager.getRole(player.getName());
                
                if (!(pRole.equalsIgnoreCase("Null")))
                {
                    if (Roles.roleNames.contains(pRole))
                    {
                        String msg = Roles.roleDetailedInfo.get(pRole);
                        player.sendMessage(msg);
                    }
                }
                else
                {
                    player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "You don't have a role chosen. Try: /roleinfo <rollName> (ex: /roleinfo Medic)");
                }
            }
        }
        
        if (cmdLabel.equalsIgnoreCase("autoreload"))
        {
            if (!(SRKF.isLobby))
            {
                if (Game.autoReload.contains(player.getName()))
                {
                    Game.autoReload.remove(player.getName());
                    player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.DARK_AQUA + "You will now auto reload when empty.");
                }
                else
                {
                    Game.autoReload.add(player.getName());
                    player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.DARK_AQUA + "You will no longer auto reload when empty.");
                }
            }
        }
        
        if (cmdLabel.equalsIgnoreCase("perks") || cmdLabel.equalsIgnoreCase("perk"))
        {
            if (SRKF.isLobby)
            {
                new perkChooser(player);
            }
            else
            {
                player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "Can only choose your role/loadout in the Lobby!");
            }
        }
        
        if (cmdLabel.equalsIgnoreCase("role") || cmdLabel.equalsIgnoreCase("roles") || cmdLabel.equalsIgnoreCase("class"))
        {
            if (SRKF.isLobby)
            {
                new classChooser(player);
            }
            else
            {
                player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "Can only choose your role/loadout in the Lobby!");
            }
        }
        
        
        if (cmdLabel.equalsIgnoreCase("srkf"))
        {
            if (args.length > 0)
            {
                if (args.length == 1)
                {
                    String command = args[0];
                    
                    if (command.equalsIgnoreCase("help"))
                    {
                        //do help
                        
                    }
                    else
                    if (command.equalsIgnoreCase("spawnmob"))
                    {
                        Block b = player.getTargetBlock(null, 100);
                        Location loc = b.getLocation();
                        
                    }
                    else
                    if (command.equalsIgnoreCase("purge"))
                    {
                        if (player.isOp())
                        {
                            int count = 0;
                            
                            for (Entity e : player.getWorld().getEntities())
                            {
                                if (e instanceof LivingEntity)
                                {
                                    LivingEntity le = (LivingEntity) e;
                                    if (!(le instanceof Player))
                                    {
                                        le.remove();
                                        count++;
                                    }
                                }
                            }
                            
                            player.sendMessage(ChatColor.GOLD + "Purged all Living Entities (not players) from your world. Count: " + count);
                        }
                    }
                    else
                    {
                        return true;
                    }
                }
                
                if (args.length == 2)
                {
                    String command = args[0];
                    String option = args[1];
                    
                    if (command.equalsIgnoreCase("purge"))
                    {
                        if (player.isOp())
                        {
                            if (isInteger(option))
                            {
                                int ID = Integer.parseInt(option);
                                if (Game.gameStarted.contains(ID))
                                {
                                    for (String name : Game.playersMaps.keySet())
                                    {
                                        int id = Game.playersMaps.get(name);
                                        if (id == ID)
                                        {
                                            Player p = Bukkit.getPlayer(name);
                                            if (!(p.isOp()))
                                            {
                                                p.damage(50);
                                            }
                                            else
                                            {
                                                SRKF.dbManager.setMapPlayers(ID, "leave");
                                                Game.playersMaps.remove(name);
                                            }
                                        } 
                                    }
                                }
                                
                                player.sendMessage(ChatColor.GOLD + "Purged all players from Game ID: " + ID);
                            }
                        }
                    }
                    else
                    if (command.equalsIgnoreCase("create"))
                    {
                        if (!(player.isOp()) || SRKF.isLobby == true)
                        {
                            return true;
                        }
                        
                        if (MapNames.mapNames.contains(option.toLowerCase()))
                        {
                            char[] array = option.toCharArray();
                            array[0] = Character.toUpperCase(array[0]);
                            option = new String(array);
                            

                            createMap(player, option);

                        }
                        else
                        {
                            player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "Invalid Map. Try one of the following:");
                            Iterator<String> it = MapNames.mapNames.iterator();
                            while (it.hasNext())
                            {
                                String mapname = it.next();
                                player.sendMessage(ChatColor.GREEN + mapname);
                            }
                        }
                    }
                    else
                    if (command.equalsIgnoreCase("remove"))
                    {
                        if (!(player.isOp()) || SRKF.isLobby == true)
                        {
                            return true;
                        }
                        
                        if (isInteger(option))
                        {
                            // remove ID from Server database
                            int id = Integer.parseInt(option);
                            try
                            {
                                SRKF.dbManager.removeServer(id);
                                player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.GREEN + "Removed Map ID " + id);
                            } catch (Exception e)
                            {
                                player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "Failed to remove ID " + id);
                            }
                        }
                        else
                        {
                            player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "Must remove an ID.");
                            return true;
                        }
                    }
                    else
                    {
                        player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "Invalid command. Try </srkf help> for more information.");
                        return true;
                    }
                }
                
                if (args.length == 3)
                {
                    String command = args[0];
                    String option = args[1].toLowerCase();
                    String option2 = args[2].toLowerCase();
                    if (command.equalsIgnoreCase("shopdoor"))
                    {
                        if (!(player.isOp()) || SRKF.isLobby == true)
                        {
                            return true;
                        }
                        
                        boolean checked = false;
                        
                        if (MapNames.mapNames.contains(option))
                        {
                            checked = true;
                        }
                        
                        if (checked)
                        {
                            if (isInteger(option2))
                            {
                                String mapName = option;
                                int ID = Integer.parseInt(option2);
                                
                                createShopDoor(player, mapName, ID);
                            }
                        }
                    }
                    else
                    if (command.equalsIgnoreCase("shopexit"))
                    {
                        if (!(player.isOp()) || SRKF.isLobby == true)
                        {
                            return true;
                        }
                        
                        boolean checked = false;
                        
                        if (MapNames.mapNames.contains(option))
                        {
                            checked = true;
                        }
                        
                        if (checked)
                        {
                            if (isInteger(option2))
                            {
                                int ID = Integer.parseInt(option2);
                                String mapName = option;
                                int x = player.getLocation().getBlockX();
                                int y = player.getLocation().getBlockY();
                                int z = player.getLocation().getBlockZ();
                                
                                boolean success = SRKF.dbManager.addMapShopExitLocation(ID, mapName, x, y, z);
                                
                                if (success)
                                {
                                    player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.GREEN + "Created Exit Shop Point: " + ChatColor.YELLOW + ID 
                                            + ChatColor.GREEN + " for Map: " + ChatColor.YELLOW + mapName + ChatColor.GREEN 
                                            + " at X: " + ChatColor.YELLOW + x + ChatColor.GREEN + " Y: " + ChatColor.YELLOW + y + ChatColor.GREEN + " Z: " + ChatColor.YELLOW + z);
                                }
                                else
                                {
                                    player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "Exit Shop Point " + ID + " for Map: " + mapName + " failed to create. Check logs.");
                                    return true;
                                }
                                
                            }
                        }
                    }
                    else
                    if (command.equalsIgnoreCase("shopkeeper"))
                    {
                        if (!(player.isOp()) || SRKF.isLobby == true)
                        {
                            return true;
                        }
                        
                        boolean checked = false;
                        
                        if (MapNames.mapNames.contains(option))
                        {
                            checked = true;
                        }
                        
                        if (checked)
                        {
                            if (isInteger(option2))
                            {
                                int ID = Integer.parseInt(option2);
                                String mapName = option;
                                int x = player.getLocation().getBlockX();
                                int y = player.getLocation().getBlockY();
                                int z = player.getLocation().getBlockZ();
                                
                                boolean success = SRKF.dbManager.addMapShopKepeerLocation(ID, mapName, x, y, z);
                                
                                if (success)
                                {
                                    player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.GREEN + "Created Shop Keeper Point: " + ChatColor.YELLOW + ID 
                                            + ChatColor.GREEN + " for Map: " + ChatColor.YELLOW + mapName + ChatColor.GREEN 
                                            + " at X: " + ChatColor.YELLOW + x + ChatColor.GREEN + " Y: " + ChatColor.YELLOW + y + ChatColor.GREEN + " Z: " + ChatColor.YELLOW + z);
                                }
                                else
                                {
                                    player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "Shop Keeper Point " + ID + " for Map: " + mapName + " failed to create. Check logs.");
                                    return true;
                                }
                                
                            }
                        }
                    }
                    else
                    if (command.equalsIgnoreCase("door"))
                    {
                        if (!(player.isOp()) || SRKF.isLobby == true)
                        {
                            return true;
                        }
                        
                        boolean checked = false;
                        
                        if (MapNames.mapNames.contains(option))
                        {
                            checked = true;
                        }
                        
                        if (checked)
                        {
                            if (isInteger(option2))
                            {
                                int ID = Integer.parseInt(option2);
                                String mapName = option;
                                int x = player.getLocation().getBlockX();
                                int y = player.getLocation().getBlockY();
                                int z = player.getLocation().getBlockZ();
                                
                                boolean success = SRKF.dbManager.addMapDoor(ID, mapName, x, y, z);
                                
                                if (success)
                                {
                                    player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.GREEN + "Created Door Point: " + ChatColor.YELLOW + ID 
                                            + ChatColor.GREEN + " for Map: " + ChatColor.YELLOW + mapName + ChatColor.GREEN 
                                            + " at X: " + ChatColor.YELLOW + x + ChatColor.GREEN + " Y: " + ChatColor.YELLOW + y + ChatColor.GREEN + " Z: " + ChatColor.YELLOW + z);
                                }
                                else
                                {
                                    player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "Door Point " + ID + " for Map: " + mapName + " failed to create. Check logs.");
                                    return true;
                                }
                                
                            }
                        }
                        else
                        {
                            player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "Map Name: " 
                                    + ChatColor.YELLOW + option + ChatColor.RED + " not found.");
                            return true;
                        }
                    }
                    else
                    if (command.equalsIgnoreCase("mobspawn"))
                    {
                        if (!(player.isOp()) || SRKF.isLobby == true)
                        {
                            return true;
                        }
                        
                        boolean checked = false;
                        
                        if (MapNames.mapNames.contains(option))
                        {
                            checked = true;
                        }
                        
                        if (checked)
                        {
                            if (isInteger(option2))
                            {
                                int ID = Integer.parseInt(option2);
                                String mapName = option;
                                int x = player.getLocation().getBlockX();
                                int y = player.getLocation().getBlockY();
                                int z = player.getLocation().getBlockZ();
                                
                                boolean success = SRKF.dbManager.addMobSpawn(ID, mapName, x, y, z);
                                
                                if (success)
                                {
                                    player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.GREEN + "Created Mob Spawn: " + ChatColor.YELLOW + ID 
                                            + ChatColor.GREEN + " for Map: " + ChatColor.YELLOW + mapName + ChatColor.GREEN 
                                            + " at X: " + ChatColor.YELLOW + x + ChatColor.GREEN + " Y: " + ChatColor.YELLOW + y + ChatColor.GREEN + " Z: " + ChatColor.YELLOW + z);
                                }
                                else
                                {
                                    player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "Spawn Point " + ID + " for Map: " + mapName + " failed to create. Check logs.");
                                    return true;
                                }
                                
                            }
                        }
                        else
                        {
                            player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "Map Name: " 
                                    + ChatColor.YELLOW + option + ChatColor.RED + " not found.");
                            return true;
                        }
                        
                    }
                    else
                    if (command.equalsIgnoreCase("sign"))
                    {
                        if (!(player.isOp()) || SRKF.isLobby == false)
                        {
                            return true;
                        }
                        
                        if (option.equalsIgnoreCase("create"))
                        {
                            if (isInteger(option2))
                            {
                                int idnum = Integer.parseInt(option2);

                                boolean checked = SRKF.dbManager.checkMapID(idnum);

                                if (checked)
                                {
                                    createSign(player, idnum);
                                }
                                else
                                {
                                    player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "Map ID not found.");
                                    return true;
                                }
                            }
                        }
                        
                        if (option.equalsIgnoreCase("remove"))
                        {
                            if (isInteger(option2))
                            {
                                int idnum = Integer.parseInt(option2);

                                boolean checked = SRKF.dbManager.checkMapID(idnum);

                                if (checked)
                                {
                                    removeSign(player, idnum);
                                }
                                else
                                {
                                    player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "Map ID not found.");
                                    return true;
                                }
                            }
                        }
                        
                        
                    }
                    else
                    if (command.equalsIgnoreCase("spawn"))
                    {
                        if (!(player.isOp()) || SRKF.isLobby == true)
                        {
                            return true;
                        }
                        
                        boolean checked = false;
                        
                        if (MapNames.mapNames.contains(option))
                        {
                            checked = true;
                        }
                        
                        if (checked)
                        {
                            if (isInteger(option2))
                            {
                                int ID = Integer.parseInt(option2);
                                String mapName = option;
                                int x = player.getLocation().getBlockX();
                                int y = player.getLocation().getBlockY();
                                int z = player.getLocation().getBlockZ();
                                
                                boolean success = SRKF.dbManager.addMapSpawn(ID, mapName, x, y, z);
                                
                                if (success)
                                {
                                    player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.GREEN + "Created Spawn: " + ChatColor.YELLOW + ID 
                                            + ChatColor.GREEN + " for Map: " + ChatColor.YELLOW + mapName + ChatColor.GREEN 
                                            + " at X: " + ChatColor.YELLOW + x + ChatColor.GREEN + " Y: " + ChatColor.YELLOW + y + ChatColor.GREEN + " Z: " + ChatColor.YELLOW + z);
                                }
                                else
                                {
                                    player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "Spawn Point " + ID + " for Map: " + mapName + " failed to create. Check logs.");
                                    return true;
                                }
                                
                            }
                        }
                        else
                        {
                            player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "Map Name: " 
                                    + ChatColor.YELLOW + option + ChatColor.RED + " not found.");
                            return true;
                        }
                        
                    }
                    else
                    if (command.equalsIgnoreCase("map"))
                    {
                        if (!(player.isOp()) || SRKF.isLobby == true)
                        {
                            return true;
                        }
                        
                        if (option.equalsIgnoreCase("create"))
                        {
                            boolean created = SRKF.dbManager.createMapName(option2);
                            
                            if (created)
                            {
                                player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.GREEN + "Created new MapName: " + ChatColor.YELLOW + option2);
                            }
                            else
                            {
                                player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "Failed to create Map Name: " + ChatColor.YELLOW + option2 + " - Duplicate? Check logs.");
                                return true;
                            }
                            
                        }
                        else
                        if (option.equalsIgnoreCase("remove"))
                        {
                            boolean removed = SRKF.dbManager.removeMapName(option2);
                            
                            if (removed)
                            {
                                player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.GREEN + "Removed MapName: " + ChatColor.YELLOW + option2);
                            }
                            else
                            {
                                player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "Failed to remove Map Name: " + ChatColor.YELLOW + option2 + " - Doesn't exist? Check logs.");
                                return true;
                            }
                        }
                        else
                        {
                            player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "Invalid command. Try </srkf help> for more information.");
                            return true;
                        }
                    }
                    else
                    {
                        player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "Invalid command. Try </srkf help> for more information.");
                        return true;
                    }
                }
            }
            else
            {
                player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "Invalid command. Try </srkf help> for more information.");
                return true;
            }
        }
        
        return true;
    }
    
    
    
    private boolean isInteger(String s) 
    {
        try
        {
            Integer.parseInt(s);
        } catch (NumberFormatException e)
        {
            return false;
        }
        return true;
    }
    
    private void createSpawnPoint(Player player, int ID)
    {
        Selection sel = we.getSelection(player);
        
        if (sel == null)
        {
            player.sendMessage(ChatColor.DARK_GRAY + "[SR"+ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "Requires a WorldEdit Selection first!");
            return;
        }
        
        Location max = sel.getMaximumPoint();
        Location min = sel.getMinimumPoint();
        
        
    }
    
    
    private void removeSign(Player player, int ID)
    {
        Selection sel = we.getSelection(player);
        
        if (sel == null)
        {
            player.sendMessage(ChatColor.DARK_GRAY + "[SR"+ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "Requires a WorldEdit Selection first!");
            return;
        }
        
        Location max = sel.getMaximumPoint();
        Location min = sel.getMinimumPoint();
        
        String worldName = max.getWorld().getName();
        
        if (worldName.equalsIgnoreCase("lobby"))
        {
            int x1 = max.getBlockX();
            int y1 = max.getBlockY();
            int z1 = max.getBlockZ();
            
            int x2 = min.getBlockX();
            int y2 = min.getBlockY();
            int z2 = min.getBlockZ();
            
            if ((x1 == x2) && (y1 == y2) && (z1 == z2))
            {
                Block b = Bukkit.getWorld(worldName).getBlockAt(x1, y1, z1);
                
                if (!(b.getType() == Material.WALL_SIGN))
                {
                    player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "Must be a Wall Sign!");
                    return;
                }
                
                if (sl.signLocs.containsKey(ID))
                {
                    try
                    {
                        Block block = SignListener.signLocs.get(ID);
                        block.setType(Material.AIR);
                        sl.signLocs.remove(ID);
                        SRKF.dbManager.removeSign(ID);
                        player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] "
                                + ChatColor.GOLD + "Removed sign location from database for server ID: " + ID);
                        
                    } catch (Exception e)
                    {
                        player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "Failed to remove old Sign from DB.");
                        e.printStackTrace();
                        return;
                    }
                }
                else
                {
                    player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] "
                                + ChatColor.RED + "No sign in database for this location for ID: " + ID);
                }
            }
            else
            {
                player.sendMessage(ChatColor.DARK_GRAY + "[SR"+ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "Must be one block and a sign.");
                return;
            }
        }
        
    }
    private void createSign(Player player, int ID)
    {
        Selection sel = we.getSelection(player);
        
        if (sel == null)
        {
            player.sendMessage(ChatColor.DARK_GRAY + "[SR"+ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "Requires a WorldEdit Selection first!");
            return;
        }
        
        Location max = sel.getMaximumPoint();
        Location min = sel.getMinimumPoint();
        
        String worldName = max.getWorld().getName();
        
        if (worldName.equalsIgnoreCase("lobby"))
        {
            int x1 = max.getBlockX();
            int y1 = max.getBlockY();
            int z1 = max.getBlockZ();
            
            int x2 = min.getBlockX();
            int y2 = min.getBlockY();
            int z2 = min.getBlockZ();
            
            if ((x1 == x2) && (y1 == y2) && (z1 == z2))
            {
                Block b = Bukkit.getWorld(worldName).getBlockAt(x1, y1, z1);
                
                if (!(b.getType() == Material.WALL_SIGN))
                {
                    player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "Must be a Wall Sign!");
                    return;
                }
                
                if (sl.signLocs.containsKey(ID))
                {
                    try
                    {
                        Block block = SignListener.signLocs.get(ID);
                        block.setType(Material.AIR);
                        sl.signLocs.remove(ID);
                        SRKF.dbManager.removeSign(ID);
                        player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] "
                                + ChatColor.GOLD + "Removed old sign location from database for server ID: " + ID);
                        
                    } catch (Exception e)
                    {
                        player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "Failed to remove old Sign from DB.");
                        e.printStackTrace();
                        return;
                    }
                }

                try
                {
                    sl.signLocs.put(ID, b);
                    SRKF.dbManager.addSign(ID, x1, y1, z1);

                    player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " 
                            + ChatColor.GREEN + "Created Sign for Server (" + ID + ") at location (X: " + x1 + " | Y: " + y1 + " | Z:" + z1 + ")");
                    
                } catch (Exception e)
                {
                    player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "Failed to create new Map");
                    e.printStackTrace();
                }
            }
            else
            {
                player.sendMessage(ChatColor.DARK_GRAY + "[SR"+ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "Must be one block and a sign.");
                return;
            }
        }
    }
    
    private void createMap(Player player, String mapName)
    {
        Selection sel = we.getSelection(player);

        if (sel == null)
        {
            player.sendMessage(ChatColor.DARK_GRAY + "[SR"+ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "Requires a WorldEdit Selection first!");
            return;
        }

        Location max = sel.getMaximumPoint();
        Location min = sel.getMinimumPoint();

        String worldName = max.getWorld().getName();

        int x1 = max.getBlockX();
        int y1 = max.getBlockY();
        int z1 = max.getBlockZ();

        int x2 = min.getBlockX();
        int y2 = min.getBlockY();
        int z2 = min.getBlockZ();

        String address = Bukkit.getIp();
        int port = Bukkit.getPort();

        int ID = 0;

        String name = player.getName();
        try
        {
            SRKF.dbManager.addServer(name, mapName, address, port, worldName, x1, y1, z1, x2, y2, z2);
            
            ID = SRKF.dbManager.getLastID();

            player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.GREEN + "Created Map (" + mapName + ") with ID: " 
                + ID);

        } catch (Exception e)
        {
            player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "Failed to create new Map");
        }
    }
    
    private void createShopDoor(Player player, String mapName, int shopID)
    {
        Selection sel = we.getSelection(player);
        
        if (sel == null)
        {
            player.sendMessage(ChatColor.DARK_GRAY + "[SR"+ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "Requires a WorldEdit Selection first!");
            return;
        }
        
        Location max = sel.getMaximumPoint();
        Location min = sel.getMinimumPoint();
        
        String worldName = max.getWorld().getName();
        
        int x1 = max.getBlockX();
        int y1 = max.getBlockY();
        int z1 = max.getBlockZ();

        int x2 = min.getBlockX();
        int y2 = min.getBlockY();
        int z2 = min.getBlockZ();
        
        Material mat = min.getBlock().getType();
        
        boolean success = SRKF.dbManager.addMapShopDoorway(shopID, mapName, x1, y1, z1, x2, y2, z2, mat);
                                
        if (success)
        {
            player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.GREEN + "Created Shop Door Point: " + ChatColor.YELLOW + shopID 
                    + ChatColor.GREEN + " for Map: " + ChatColor.YELLOW + mapName + ChatColor.GREEN 
                    + " at X: " + ChatColor.YELLOW + x1 + ChatColor.GREEN + " Y: " + ChatColor.YELLOW + y1 + ChatColor.GREEN + " Z: " + ChatColor.YELLOW + z1);
        }
        else
        {
            player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "Shop Door Point " + shopID + " for Map: " + mapName + " failed to create. Check logs.");
        }
        
        
    }
    
}
