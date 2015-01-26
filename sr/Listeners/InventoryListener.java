package sr.Listeners;

import java.util.HashMap;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import sr.Extras.Guns;
import sr.Extras.Roles;
import sr.Game;
import sr.Roles.Engineer;
import sr.SRKF;
import sr.ShopStuff.demoInventory;
import sr.ShopStuff.meleeInventory;
import sr.ShopStuff.pistolInventory;
import sr.ShopStuff.rifleInventory;
import sr.ShopStuff.shopInventory;
import sr.perkChooser;
import sr.ShopStuff.shopInventoryUpdate;
import sr.ShopStuff.shotgunInventory;
import sr.ShopStuff.smgInventory;

/**
 *
 * @author Cross
 */
public class InventoryListener implements Listener
{
    public SRKF plugin;
    
    public InventoryListener (SRKF plugin)
    {
        this.plugin = plugin;
    }
    
    public static int diamondMin = 13;
    public static int diamondMax = 22;
    public static int emeraldMin = 100;
    public static int emeraldMax = 150;
    
    
    public static HashMap<String, ItemStack> playerHelmet = new HashMap<>();
    public static HashMap<String, ItemStack> playerChest = new HashMap<>();
    public static HashMap<String, ItemStack> playerLegs = new HashMap<>();
    public static HashMap<String, ItemStack> playerBoots = new HashMap<>();
    
    public static HashMap<String, ItemStack> playerPrimary = new HashMap<>();
    public static HashMap<String, ItemStack> playerSecondary = new HashMap<>();
    public static HashMap<String, ItemStack> playerMedKits = new HashMap<>();
    public static HashMap<String, ItemStack> playerGrenade = new HashMap<>();
    public static HashMap<String, ItemStack> playerSpecial = new HashMap<>();
    
    public static HashMap<String, ItemStack> playerPrimaryAmmo = new HashMap<>();
    public static HashMap<String, ItemStack> playerSecondaryAmmo = new HashMap<>();
        
    
    @EventHandler
    public void onPlayerDropItem (PlayerDropItemEvent event)
    {
        if (!(event.getPlayer().isOp()))
        {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onItemPickup (PlayerPickupItemEvent event)
    {
        Player player = event.getPlayer();
        Item i = event.getItem();
            
        if (SRKF.isLobby)
        {
            if (player.isOp())
            {
                return;
            }
            event.setCancelled(true);
            i.remove();
        }
        
        if (!(SRKF.isLobby))
        {
            if (PL.isSpectating.contains(player.getName()))
            {
                event.setCancelled(true);
                return;
            }
            
            event.setCancelled(true);
            i.remove();
        
            if (Game.playersMaps.containsKey(player.getName()))
            {
                ItemStack stack = i.getItemStack();

                if (stack.getType() == Material.DIAMOND)
                {
                    event.setCancelled(true);
                    i.remove();
                    
                    int gameID = Game.playersMaps.get(player.getName());
                    
                    int count = 0;
                    for (String name : Game.playersMaps.keySet())
                    {
                        int id = Game.playersMaps.get(name);
                        if (id == gameID)
                        {
                            count = count + 1;
                        }
                    }
                    
                    for (String name : Game.playersMaps.keySet())
                    {
                        int id = Game.playersMaps.get(name);
                        if (id == gameID)
                        {
                            Player team = Bukkit.getPlayer(name);
                            int cash = 0;
                            if (Game.playerMoney.containsKey(name))
                            {
                                cash = Game.playerMoney.get(name);
                                Game.playerMoney.remove(name);
                            }
                            Random rand = new Random();
                            int randNum = rand.nextInt(((diamondMax - diamondMin) + 1) + diamondMin);
                            int cashAmount = (int) Math.floor((double) (randNum / count) * 1.5);

                            cash = cash + cashAmount;

                            Game.playerMoney.put(name, cash);
                            //player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.GOLD + "You picked up " + ChatColor.GREEN + "$" + randNum 
                            //        + ChatColor.GOLD + " cash, total is now: " + ChatColor.GREEN + "$" + cash);



                            if (Game.isRoundChanging.containsKey(gameID))
                            {
                                team.setLevel(cash);
                            }

                            // update scoreboard
                            Scoreboard sb = team.getScoreboard();
                            if (sb != null)
                            {
                                Objective objective = sb.getObjective(DisplaySlot.SIDEBAR);
                                Score score = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.GOLD + " Cash: " + ChatColor.GREEN + "$"));
                                score.setScore(cash);
                            }

                            //player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1, 2);
                            team.playSound(team.getLocation(), Sound.LEVEL_UP, 1, 2);
                        } 
                    }
                    

                }

                if (stack.getType() == Material.EMERALD)
                {
                    int gameID = Game.playersMaps.get(player.getName());
                    
                    int count = 0;
                    for (String name : Game.playersMaps.keySet())
                    {
                        int id = Game.playersMaps.get(name);
                        if (id == gameID)
                        {
                            count = count + 1;
                        }
                    }
                    
                    for (String name : Game.playersMaps.keySet())
                    {
                        int id = Game.playersMaps.get(name);
                        if (id == gameID)
                        {
                            Player team = Bukkit.getPlayer(name);
                            int cash = 0;
                            if (Game.playerMoney.containsKey(name))
                            {
                                cash = Game.playerMoney.get(name);
                                Game.playerMoney.remove(name);
                            }
                            Random rand = new Random();
                            int randNum = rand.nextInt(((emeraldMax - emeraldMin) + 1) + emeraldMin);
                            int cashAmount = (int) Math.floor((double) (randNum / count) * 1.5);

                            cash = cash + cashAmount;

                            Game.playerMoney.put(name, cash);
                            //player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.GOLD + "You picked up " + ChatColor.GREEN + "$" + randNum 
                            //        + ChatColor.GOLD + " cash, total is now: " + ChatColor.GREEN + "$" + cash);



                            if (Game.isRoundChanging.containsKey(gameID))
                            {
                                team.setLevel(cash);
                            }

                            // update scoreboard
                            Scoreboard sb = team.getScoreboard();
                            if (sb != null)
                            {
                                Objective objective = sb.getObjective(DisplaySlot.SIDEBAR);
                                Score score = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.GOLD + " Cash: " + ChatColor.GREEN + "$"));
                                score.setScore(cash);
                            }

                            //player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1, 2);
                            team.playSound(team.getLocation(), Sound.LEVEL_UP, 1, 2);
                        } 
                    }
                }
            }
        }
        
    }
    
    @EventHandler
    public void onInventoryClick (InventoryClickEvent event)
    {
        Player player = (Player) event.getWhoClicked();
        
        if (event.getCurrentItem() != null)
        {
            ItemStack stack = event.getCurrentItem();
            
            if (event.getSlot() == 39)
            {
                event.setCancelled(true);
                player.closeInventory();
            }
        
            if (SRKF.isLobby)
            {
                String name = event.getInventory().getName();
                if (name.toLowerCase().contains("perks"))
                {
                    if (stack.hasItemMeta())
                    {
                        ItemMeta meta = stack.getItemMeta();
                        String disp = meta.getDisplayName();
                        event.setCancelled(true);
                        player.updateInventory();
                        
                        if (disp.contains("Click to level up"))
                        {
                            String[] roleDelim = disp.split(" - ");
                            String roleName = roleDelim[0];

                            String[] perkDelim = disp.split(": ");
                            String perkName = perkDelim[1];

                            player.closeInventory();

                            // do update of perk.

                            SRKF.player_dbManager.levelPerkStat(player.getName(), roleName, perkName);
                            SRKF.player_dbManager.loadPlayerRoleStats(player.getName());
                            new perkChooser(player);

                        }
                        
                    }
                } // end perk inventory
                
                if (name.toLowerCase().contains("role"))
                {
                    if (stack.hasItemMeta())
                    {
                        ItemMeta meta = stack.getItemMeta();
                        String disp = meta.getDisplayName();
                        event.setCancelled(true);
                        player.updateInventory();
                        
                        if (Roles.roleNames.contains(disp))
                        {
                            // close inv
                            player.closeInventory();

                            // update to that role
                            SRKF.player_dbManager.setRole(player.getName(), disp);

                            player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " 
                                    + ChatColor.GREEN + "You have chosen the " + ChatColor.GOLD + disp + ChatColor.GREEN + " role!");

                            player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " 
                                    + ChatColor.GREEN + "To learn more about the " + ChatColor.GOLD + disp + ChatColor.GREEN + " type: " + ChatColor.YELLOW + "/roleinfo");

                            player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " 
                                    + ChatColor.GREEN + "More detailed info can be found on our website at:\n" + ChatColor.GOLD + ChatColor.BOLD + "www.playcadia.net");
                            
                        }
                        
                        if (disp.contains("Respec"))
                        {
                            player.closeInventory();
                            SRKF.player_dbManager.respecPerks(player.getName());
                        }
                    
                    }
                } // end role inventory
                
            } // end lobby check
            else //if not lobby
            {
                if (player.isOp() && (!(Game.playersMaps.containsKey(player.getName()))))
                {
                    return;
                }
                
                if (PL.isSpectating.contains(player.getName()))
                {
                    return;
                }
                
                int gameID = Game.playersMaps.get(player.getName());
                
                String name = event.getInventory().getName();
                if (name.toLowerCase().contains("click to buy"))
                {
                    //Bukkit.broadcastMessage("slot" + event.getSlot() + " slottype " + event.getSlotType());
                    
                    if(event.getSlotType() != SlotType.CONTAINER)
                    {
                        return;
                    }
                    
                    /*
                    if(event.getSlot() > 8)
                    {
                        return;
                    }
                    */

                    ItemStack shopitem = event.getCurrentItem();
                    event.setCancelled(true);
                    Inventory inv = event.getInventory();

                    ItemStack is = event.getCursor();
                    is.setType(Material.AIR);

                    player.updateInventory();

                    if(shopitem == null)
                    {
                        return;
                    }
                    if(!(shopitem.hasItemMeta()))
                    {
                        return;
                    }
                    ItemMeta meta = shopitem.getItemMeta();

                    if(!(meta.hasDisplayName()))
                    {
                        return;
                    }
                    
                    String role = "null";
                    if (Game.playerClass.containsKey(player.getName()))
                    {
                        role = Game.playerClass.get(player.getName());
                    }
                    
                    String disp = meta.getDisplayName();
                    player.closeInventory();
                    if (disp.contains("Rifle"))
                    {
                        if (role.equalsIgnoreCase("Commando") || role.equalsIgnoreCase("Sharpshooter"))
                        {
                            new rifleInventory(player, gameID);
                        }
                        else
                        {
                            player.sendMessage(ChatColor.DARK_GRAY + "[SR " + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "Your role cannot use Rifles as a Primary Weapon!");
                            player.closeInventory();
                            return;
                        }
                    }
                    else
                    if (disp.contains("Pistol"))
                    {
                        new pistolInventory(player, gameID);
                    }
                    else
                    if (disp.contains("Shotgun"))
                    {
                        if (role.equalsIgnoreCase("Commando") || role.equalsIgnoreCase("Medic"))
                        {
                            new shotgunInventory(player, gameID);
                        }
                        else
                        {
                            player.sendMessage(ChatColor.DARK_GRAY + "[SR " + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "Your role cannot use Shotguns as a Primary Weapon!");
                            player.closeInventory();
                            return;
                        }
                    }
                    else
                    if (disp.contains("SMG"))
                    {
                        if (role.equalsIgnoreCase("Commando") || role.equalsIgnoreCase("Engineer"))
                        {
                            new smgInventory(player, gameID);
                        }
                        else
                        {
                            player.sendMessage(ChatColor.DARK_GRAY + "[SR " + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "Your role cannot use SMGs as a Primary Weapon!");
                            player.closeInventory();
                            return;
                        }
                    }
                    else
                    if (disp.contains("Melee"))
                    {
                        if (role.equalsIgnoreCase("Berserker") || role.equalsIgnoreCase("Commando"))
                        {
                            new meleeInventory(player, gameID);
                        }
                        else
                        {
                            player.sendMessage(ChatColor.DARK_GRAY + "[SR " + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "Your role cannot use Melee as a Primary Weapon!");
                            player.closeInventory();
                            return;
                        }
                    }
                    else
                    if (disp.contains("Demolitions"))
                    {
                        new demoInventory(player, gameID);
                    }
                    else
                    {
                        new shopInventoryUpdate(player, shopitem);
                    }

                    
                    return;
                }
                
                if (name.contains("Rifle") || name.contains("Pistol") || name.contains("SMG") || name.contains("Shotgun") || name.contains("Melee") || name.contains("Demo Weaps"))
                {
                    
                    if(event.getSlotType() != SlotType.CONTAINER)
                    {
                        return;
                    }

                    ItemStack shopitem = event.getCurrentItem();
                    event.setCancelled(true);
                    Inventory inv = event.getInventory();

                    ItemStack is = event.getCursor();
                    is.setType(Material.AIR);

                    player.updateInventory();

                    if(shopitem == null)
                    {
                        return;
                    }
                    if(!(shopitem.hasItemMeta()))
                    {
                        return;
                    }
                    ItemMeta meta = shopitem.getItemMeta();

                    if(!(meta.hasDisplayName()))
                    {
                        return;
                    }
                    
                    String disp = meta.getDisplayName();
                    player.closeInventory();
                    if (disp.contains("Main"))
                    {
                        new shopInventory(player, gameID);
                    }
                    else
                    {
                        new shopInventoryUpdate(player, shopitem);
                    }
                    
                }
            }
        }
        
    } //end inv click
    
    
    public static void setupPrimaryWeapon(String pName, String role)
    {
        if (playerPrimary.containsKey(pName))
        {
            playerPrimary.remove(pName);
        }
        /*
        String weapon = SRKF.player_dbManager.getStringLoadoutItem(pName, "PrimaryWeapon");
        
        if (Guns.GunList.containsKey(weapon))
        {
            ItemStack stack = Guns.GunList.get(weapon);
            playerPrimary.put(pName, stack);
        }
        else
        if (Guns.MeleeList.containsKey(weapon))
        {
            ItemStack stack = Guns.MeleeList.get(weapon);
            playerPrimary.put(pName, stack);
        }
        */

        String primaryName = Guns.getDefaultPrimary(role);

        //SRKF.LOG.info("primaryName is: " + primaryName);
        if (Guns.MeleeCheck.contains(primaryName))
        {
            if (Game.playerMeleeWeapon.containsKey(pName))
            {
                Game.playerMeleeWeapon.remove(pName);
            }
            Game.playerMeleeWeapon.put(pName, primaryName);
            
            ItemStack stack = Guns.MeleeList.get(primaryName).clone();
            playerPrimary.put(pName, stack);
            //SRKF.LOG.info("Added " + primaryName + " to playerPrimary");
        }

        if (Guns.GunCheck.contains(primaryName))
        {
            ItemStack stack = Guns.GunList.get(primaryName).clone();
            playerPrimary.put(pName, stack);
            //SRKF.LOG.info("Added " + primaryName + " to playerPrimary");
            setupPrimaryAmmo(pName, primaryName);
        }
        
    }
    
    public static void setupSecondaryWeapon(String pName, String role)
    {
        if (playerSecondary.containsKey(pName))
        {
            playerSecondary.remove(pName);
        }
        
        /*
        
        String weapon = SRKF.player_dbManager.getStringLoadoutItem(pName, "SecondaryWeapon");
        
        if (Guns.GunList.containsKey(weapon))
        {
            ItemStack stack = Guns.GunList.get(weapon);
            playerSecondary.put(pName, stack);
        }
        else
        if (Guns.MeleeList.containsKey(weapon))
        {
            ItemStack stack = Guns.MeleeList.get(weapon);
            playerSecondary.put(pName, stack);
        }
        */

        String secondaryName = Guns.getDefaultSecondary(role);

        if (Guns.MeleeCheck.contains(secondaryName))
        {
            ItemStack stack = Guns.MeleeList.get(secondaryName).clone();
            playerSecondary.put(pName, stack);
        }

        if (Guns.GunCheck.contains(secondaryName))
        {
            ItemStack stack = Guns.GunList.get(secondaryName).clone();
            playerSecondary.put(pName, stack);
            setupSecondaryAmmo(pName, secondaryName);
        }
        
    }
    
    public static void setupMedKits(String pName)
    {
        if (playerMedKits.containsKey(pName))
        {
            playerMedKits.remove(pName);
        }
        
        if (Guns.miscItemStacks.containsKey("Medical Kit"))
        {
            ItemStack medkits = Guns.miscItemStacks.get("Medical Kit").clone();
            medkits.setAmount(2);
            playerMedKits.put(pName, medkits);
        }
        
        
    }
    
    public static void setupGrenade(String pName)
    {
        if (playerGrenade.containsKey(pName))
        {
            playerGrenade.remove(pName);
        }
        
        int amount = 2;
        
        ItemStack grenades = Guns.DemoList.get("Grenade").clone();
        grenades.setAmount(amount);
        playerGrenade.put(pName, grenades);
        
    }
    
    public static void setupSpecialItem(String pName)
    {
        if (playerSpecial.containsKey(pName))
        {
            playerSpecial.remove(pName);
        }
        
        String weapon = "Welder";
        
        if (weapon.equalsIgnoreCase("Welder"))
        {
            ItemStack stack = Guns.miscItemStacks.get("Welder").clone();
            playerSpecial.put(pName, stack);
        }
        else
        if (weapon.equalsIgnoreCase("Syringe"))
        {
            ItemStack stack = new ItemStack(Material.SHEARS);
            ItemMeta stack_meta = stack.getItemMeta();
            stack_meta.setDisplayName(ChatColor.RED + "Syringe");
            stack.setItemMeta(stack_meta);
            playerSpecial.put(pName, stack);
        }
        
        /*
        else
        if (weapon.equalsIgnoreCase("Bazooka"))
        {
            ItemStack stack = new ItemStack(Material.STICK);
            ItemMeta stack_meta = stack.getItemMeta();
            stack_meta.setDisplayName(ChatColor.RED + "Bazooka");
            stack.setItemMeta(stack_meta);
            playerSpecial.put(pName, stack);
        }
        else
        if (weapon.equalsIgnoreCase("L.A.W."))
        {
            ItemStack stack = new ItemStack(Material.BONE);
            ItemMeta stack_meta = stack.getItemMeta();
            stack_meta.setDisplayName(ChatColor.RED + "L.A.W.");
            stack.setItemMeta(stack_meta);
            playerSpecial.put(pName, stack);
        }
        */
    }
    
    public static void setupPrimaryAmmo(String pName, String weapName)
    {
        if (playerPrimaryAmmo.containsKey(pName))
        {
            playerPrimaryAmmo.remove(pName);
        }
        
        
        if (Guns.GunCheck.contains(weapName))
        {
            String type = Guns.GunType.get(weapName);
            
            if (type.contains("Pistol"))
            {
                int clipsize = Guns.ammoCounts.get("Pistol");
                
                String role = SRKF.player_dbManager.getRole(pName);
                if (role.equalsIgnoreCase("Engineer"))
                {
                    clipsize = clipsize + Engineer.getBallisticTrainingBonusAmount(pName);
                }
                ItemStack priAmmo = Guns.miscItemStacks.get("Pistol").clone();
                priAmmo.setAmount(clipsize);
                playerPrimaryAmmo.put(pName, priAmmo);
            }

            if (type.contains("Rifle"))
            {
                int clipsize = Guns.ammoCounts.get("Rifle");
                
                String role = SRKF.player_dbManager.getRole(pName);
                if (role.equalsIgnoreCase("Engineer"))
                {
                    clipsize = clipsize + Engineer.getBallisticTrainingBonusAmount(pName);
                }
                ItemStack priAmmo = Guns.miscItemStacks.get("Rifle").clone();
                priAmmo.setAmount(clipsize);
                playerPrimaryAmmo.put(pName, priAmmo);
            }

            if (type.contains("Shotgun"))
            {
                int clipsize = Guns.ammoCounts.get("Shotgun");
                String role = SRKF.player_dbManager.getRole(pName);
                if (role.equalsIgnoreCase("Engineer"))
                {
                    clipsize = clipsize + Engineer.getBallisticTrainingBonusAmount(pName);
                }
                ItemStack priAmmo = Guns.miscItemStacks.get("Shotgun").clone();
                priAmmo.setAmount(clipsize);
                playerPrimaryAmmo.put(pName, priAmmo);
            }

            if (type.contains("SMG"))
            {
                int clipsize = Guns.ammoCounts.get("SMG");
                String role = SRKF.player_dbManager.getRole(pName);
                if (role.equalsIgnoreCase("Engineer"))
                {
                    clipsize = clipsize + Engineer.getBallisticTrainingBonusAmount(pName);
                }
                ItemStack priAmmo = Guns.miscItemStacks.get("SMG").clone();
                priAmmo.setAmount(clipsize);
                playerPrimaryAmmo.put(pName, priAmmo);
            }

            if (type.contains("Bow"))
            {
                int clipsize = Guns.ammoCounts.get("Bow");
                
                String role = SRKF.player_dbManager.getRole(pName);
                if (role.equalsIgnoreCase("Engineer"))
                {
                    clipsize = clipsize + Engineer.getBallisticTrainingBonusAmount(pName);
                }
                ItemStack priAmmo = Guns.miscItemStacks.get("Bow").clone();
                priAmmo.setAmount(clipsize);
                playerPrimaryAmmo.put(pName, priAmmo);
            }
        }

        
        
    }
    
    public static void setupSecondaryAmmo(String pName, String weapName)
    {
        if (playerSecondaryAmmo.containsKey(pName))
        {
            playerSecondaryAmmo.remove(pName);
        }

        if (Guns.GunCheck.contains(weapName))
        {
            String type = Guns.GunType.get(weapName);

            if (type.contains("Pistol"))
            {
                int clipsize = Guns.ammoCounts.get("Pistol");
                String role = SRKF.player_dbManager.getRole(pName);
                if (role.equalsIgnoreCase("Engineer"))
                {
                    clipsize = clipsize + Engineer.getBallisticTrainingBonusAmount(pName);
                }
                ItemStack secondAmmo = Guns.miscItemStacks.get("Pistol").clone();
                secondAmmo.setAmount(clipsize);
                playerSecondaryAmmo.put(pName, secondAmmo);
            }

            if (type.contains("Rifle"))
            {
                int clipsize = Guns.ammoCounts.get("Rifle");
                String role = SRKF.player_dbManager.getRole(pName);
                if (role.equalsIgnoreCase("Engineer"))
                {
                    clipsize = clipsize + Engineer.getBallisticTrainingBonusAmount(pName);
                }
                ItemStack secondAmmo = Guns.miscItemStacks.get("Rifle").clone();
                secondAmmo.setAmount(clipsize);
                playerSecondaryAmmo.put(pName, secondAmmo);
            }

            if (type.contains("Shotgun"))
            {
                int clipsize = Guns.ammoCounts.get("Shotgun");
                String role = SRKF.player_dbManager.getRole(pName);
                if (role.equalsIgnoreCase("Engineer"))
                {
                    clipsize = clipsize + Engineer.getBallisticTrainingBonusAmount(pName);
                }
                ItemStack secondAmmo = Guns.miscItemStacks.get("Shotgun").clone();
                secondAmmo.setAmount(clipsize);
                playerSecondaryAmmo.put(pName, secondAmmo);
            }

            if (type.contains("SMG"))
            {
                int clipsize = Guns.ammoCounts.get("SMG");
                String role = SRKF.player_dbManager.getRole(pName);
                if (role.equalsIgnoreCase("Engineer"))
                {
                    clipsize = clipsize + Engineer.getBallisticTrainingBonusAmount(pName);
                }
                ItemStack secondAmmo = Guns.miscItemStacks.get("SMG").clone();
                secondAmmo.setAmount(clipsize);
                playerSecondaryAmmo.put(pName, secondAmmo);
            }

            if (type.contains("Bow"))
            {
                int clipsize = Guns.ammoCounts.get("Bow");
                String role = SRKF.player_dbManager.getRole(pName);
                if (role.equalsIgnoreCase("Engineer"))
                {
                    clipsize = clipsize + Engineer.getBallisticTrainingBonusAmount(pName);
                }
                ItemStack secondAmmo = Guns.miscItemStacks.get("Bow").clone();
                secondAmmo.setAmount(clipsize);
                playerSecondaryAmmo.put(pName, secondAmmo);
            }
        }
        
    }
    
}
