package sr.ShopStuff;

import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import sr.Extras.Guns;
import sr.Game;
import sr.Roles.Engineer;
import sr.SRKF;
import sr.coinStuff.EnhancementListener;

/**
 *
 * @author Cross
 */
public class shopInventoryUpdate 
{
    public SRKF plugin;
 
 
    public shopInventoryUpdate(SRKF plugin)
    {
        this.plugin = plugin;
    }
   
    public shopInventoryUpdate(Player player, ItemStack chosenItem) 
    {
        if (chosenItem.hasItemMeta())
        {
            if (chosenItem.getItemMeta().hasDisplayName())
            {
                if (chosenItem.getItemMeta().hasLore())
                {
                    ItemMeta chosenItemMeta = chosenItem.getItemMeta();
                    List<String> chosenItemLore = chosenItemMeta.getLore();

                    String type = chosenItemLore.get(0);
                    String chosenType = ChatColor.stripColor(type);
                    String chosenName = chosenItemLore.get(1);
                    String chosenDisp = chosenItemMeta.getDisplayName();
                    
                   // Bukkit.broadcastMessage("chosenType is: " + chosenType);
                   // Bukkit.broadcastMessage("chosenName is: " + chosenName);
                    
                    String primaryCheck = "notprimary";
                    
                    Iterator<String> loreIt = chosenItemLore.iterator();
                    
                    while(loreIt.hasNext())
                    {
                        String theline = loreIt.next();
                        if (theline.contains("Primary Weapon"))
                        {
                            primaryCheck = "primary";
                            break;
                        }
                        
                        if (theline.contains("Secondary Weapon"))
                        {
                            primaryCheck = "secondary";
                            break;
                        }
                    }
                    
                    
                    /*
                     *  Role Check
                     */
                    String role = "null";
                    if (Game.playerClass.containsKey(player.getName()))
                    {
                        role = Game.playerClass.get(player.getName());
                    }
                    
                    if (chosenType.contains("Melee"))
                    {
                        if (primaryCheck.equalsIgnoreCase("secondary"))
                        {
                            if (!role.equalsIgnoreCase("Commando") || !role.equalsIgnoreCase("Berserker"))
                            {
                                player.sendMessage(ChatColor.DARK_GRAY + "[SR " + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "Your role cannot use Melee as a Secondary Weapon!");
                                player.closeInventory();
                                return;
                            }
                        }
                        
                        if (primaryCheck.equalsIgnoreCase("primary"))
                        {
                            if (!role.equalsIgnoreCase("Berserker"))
                            {
                                player.sendMessage(ChatColor.DARK_GRAY + "[SR " + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "Your role cannot use Melee as a Primary Weapon!");
                                player.closeInventory();
                                return;
                            }
                        }
                    }
                    
                    if (chosenType.contains("Artillery"))
                    {
                        if (!role.equalsIgnoreCase("Engineer"))
                        {
                            player.sendMessage(ChatColor.DARK_GRAY + "[SR " + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "Your role cannot use Artillery!");
                            player.closeInventory();
                            return;
                        }
                        else
                        {
                            int level = 0;
                            if (Engineer.Stats_HeavyArtillery.containsKey(player.getName()))
                            {
                                level = Engineer.Stats_HeavyArtillery.get(player.getName());
                            }
                            
                            SRKF.LOG.info("[SRKF] - Artillery level is: " + level);
                            
                            if (chosenName.contains("Bazooka"))
                            {
                                if (level < 2)
                                {
                                    player.sendMessage(ChatColor.DARK_GRAY + "[SR " + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "Your training in Heavy Artillery is too low!");
                                    player.closeInventory();
                                    return;
                                }
                            }
                            
                            if (chosenName.contains("L.A.W."))
                            {
                                if (level < 8)
                                {
                                    player.sendMessage(ChatColor.DARK_GRAY + "[SR " + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "Your training in Heavy Artillery is too low!");
                                    player.closeInventory();
                                    return;
                                }
                            }
                            
                        }
                    }
                    
                    /*
                     * Cash check
                     */
                    
                    Inventory playerInv = player.getInventory();
                    
                    int playerCash = 0;
                    if (Game.playerMoney.containsKey(player.getName()))
                    {
                        playerCash = Game.playerMoney.get(player.getName());
                    }
                    
                    int itemCost = 0;
                    
                    if (Guns.GunCost.containsKey(chosenName))
                    {
                        itemCost = Guns.GunCost.get(chosenName);
                    }
                    
                    if (Guns.MeleeCost.containsKey(chosenName))
                    {
                        itemCost = Guns.MeleeCost.get(chosenName);
                    }
                    
                    if (Guns.miscItemCosts.containsKey(chosenName))
                    {
                        itemCost = Guns.miscItemCosts.get(chosenName);
                    }
                    
                    if (Guns.DemoCost.containsKey(chosenName))
                    {
                        itemCost = Guns.DemoCost.get(chosenName);
                    }
                    
                    if (playerCash >= itemCost)
                    {
                        if (Game.playerMoney.containsKey(player.getName()))
                        {
                            Game.playerMoney.remove(player.getName());
                        }
                        int newBalance = playerCash - itemCost;
                        Game.playerMoney.put(player.getName(), newBalance);
                        
                        Scoreboard sb = player.getScoreboard();
                        if (sb != null)
                        {
                            Objective obj = sb.getObjective(DisplaySlot.SIDEBAR);
                            Score score = obj.getScore(Bukkit.getOfflinePlayer(ChatColor.GOLD + " Cash: " + ChatColor.GREEN + "$"));
                            score.setScore(newBalance);
                        }
                        
                        player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.GREEN + "You have purchased the " + chosenName + "!");
                        player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.GOLD + "Remaining Cash: " + ChatColor.GREEN + "$" + newBalance);
                        int gameID = Game.playersMaps.get(player.getName());
                        if (Game.isRoundChanging.containsKey(gameID))
                        {
                            player.setLevel(newBalance);
                        }
                    }
                    else
                    {
                        player.sendMessage(ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "You do not have enough Cash for this.");
                        return;
                    }
                    
                   // Bukkit.broadcastMessage("primaryCheck is: " + primaryCheck);
                    
                    if (primaryCheck.equalsIgnoreCase("primary") || primaryCheck.equalsIgnoreCase("secondary")) // the thing imbuying is a primary or secondary
                    {
                        String weaponType = "primary";
                        
                        if (primaryCheck.equalsIgnoreCase("secondary"))
                        {
                            weaponType = "secondary";
                        }
                        
                        
                        ItemStack[] tempContents = playerInv.getContents();
                        
                        int i = 0;
                        
                        
                        for (ItemStack stack : tempContents)
                        {
                            if (stack != null)
                            {
                                if (stack.hasItemMeta())
                                {
                                    if (stack.getItemMeta().hasDisplayName())
                                    {
                                        if (stack.getItemMeta().hasLore())
                                        {
                                            ItemMeta stackMeta = stack.getItemMeta();
                                            List<String> stackLore = stackMeta.getLore();
                                            String stackName = stackLore.get(1);
                                            String stackType = stackLore.get(0);
                                            String stackDisp = stackMeta.getDisplayName();

                                            boolean isGun = false;
                                            boolean isMelee = false;
                                            boolean isDemo = false;

                                            String stackPrimaryCheck = "notprimary";

                                            Iterator<String> stackIt = stackLore.iterator();


                                            if (Guns.GunList.containsKey(stackName))
                                            {
                                                isGun = true;
                                            }
                                            if (Guns.DemoList.containsKey(stackName))
                                            {
                                                isDemo = true;
                                            }
                                            if (Guns.MeleeList.containsKey(stackName))
                                            {
                                                isMelee = true;
                                            }

                                            while (stackIt.hasNext())
                                            {
                                                String theline = stackIt.next();

                                                if (theline.contains("Primary Weapon"))
                                                {
                                                    stackPrimaryCheck = "primary";
                                                }

                                                if (theline.contains("Secondary Weapon"))
                                                {
                                                    stackPrimaryCheck = "secondary";
                                                }
                                            }


                                            if (stackPrimaryCheck.equals(primaryCheck))
                                            {
                                                //Bukkit.broadcastMessage("PrimaryCheck is: " + primaryCheck);
                                                //Bukkit.broadcastMessage("stackPrimaryCheck is: " + stackPrimaryCheck);
                                                //Bukkit.broadcastMessage("stack disp is: " + stack.getItemMeta().getDisplayName());
                                                // item types match up (primary or secondary)
                                                // remove the associated clip for it if applicable
                                                playerInv.removeItem(stack);

                                                String ammoName = ChatColor.stripColor(stackType + " Ammo Clip");

                                                //Bukkit.broadcastMessage("ammoName is: " + ammoName);
                                                
                                                Inventory playerInv2 = player.getInventory();
                                                ItemStack[] contents = playerInv2.getContents();
                                                int z = 0;
                                                for (ItemStack ammoStack : contents)
                                                {
                                                    if (ammoStack != null)
                                                    {
                                                        if (ammoStack.hasItemMeta())
                                                        {
                                                            if (ammoStack.getItemMeta().hasDisplayName())
                                                            {
                                                                String ammoDisp = ammoStack.getItemMeta().getDisplayName();

                                                                //Bukkit.broadcastMessage("ammoDisp is: " + ammoDisp);

                                                                if (ammoDisp.contains(ammoName))
                                                                {
                                                                    // matched
                                                                    //Bukkit.broadcastMessage("matched | " + ammoStack.getType().toString());
                                                                    playerInv2.removeItem(ammoStack);
                                                                }
                                                            }
                                                        }
                                                    }

                                                    

                                                    z = z + 1;
                                                }

                                            }


                                        }
                                    }
                                }
                            }
                            
                            
                            i = i + 1;
                        } // end first while loop
                        
                    } // end initial primaryCheck
                    
                    
                    if (primaryCheck.equalsIgnoreCase("primary")) // update melee weapon hash if it is melee weapon.
                    {
                        if (Game.playerMeleeWeapon.containsKey(player.getName()))
                        {
                            Game.playerMeleeWeapon.remove(player.getName());
                        }
                        
                        if (Guns.MeleeList.containsKey(chosenName))
                        {
                            Game.playerMeleeWeapon.put(player.getName(), chosenName);
                        }
                    }
                    
                    int paperAmount = 0;
                    int grenadeAmount = 0;
                    
                    ItemStack firstAmmo = null;
                    String firstAmmoType = "null";
                    ItemStack secondAmmo = null;
                    String secondAmmoType = "null";
                    ItemStack meleeweapon = null;
                    ItemStack[] tempinv = playerInv.getContents();
                    
                    int i = 0;
                    
                    for (ItemStack is : tempinv)
                    {
                        if (is != null)
                        {
                            if (is.getType() == Material.INK_SACK) // update grenades
                            {
                                if (is.hasItemMeta())
                                {
                                    String disp = is.getItemMeta().getDisplayName();
                                    
                                    if (disp.contains("Grenade"))
                                    {
                                        int amt = is.getAmount();
                                        grenadeAmount = amt;
                                        playerInv.removeItem(is);
                                    }
                                }
                            }
                            
                            if (is.getType() == Material.PAPER) // update med kits
                            {
                                int amt = is.getAmount();
                                paperAmount = amt;
                                playerInv.removeItem(is);
                            }
                            
                            if (is.hasItemMeta()) // update ammo 
                            {
                                if (is.getItemMeta().hasDisplayName())
                                {
                                    if (is.getItemMeta().hasLore())
                                    {
                                        List<String> itemLore = is.getItemMeta().getLore();
                                        String isName = itemLore.get(1);
                                        String isType = itemLore.get(0);
                                        String isType2 = ChatColor.stripColor(isType);
                                        
                                        ///Bukkit.broadcastMessage("isName is: " + isName);
                                        //Bukkit.broadcastMessage("isType is: " + isType);
                                        //Bukkit.broadcastMessage("isType2 is: " + isType2);
   
                                        if (Guns.GunType.containsKey(isName))
                                        {
                                            if (chosenName.contains("Ammo"))
                                            {
                                               // Bukkit.broadcastMessage("in Ammo");
                                                if (firstAmmo == null)
                                                {
                                                   // Bukkit.broadcastMessage("firstAmmo");
                                                    firstAmmo = Guns.miscItemStacks.get(isType2).clone();
                                                    int amount = Guns.ammoCounts.get(isType2);
                                                    //Bukkit.broadcastMessage("amount is: " + amount);
                                                    firstAmmoType = isType2;
                                                    firstAmmo.setAmount(amount);
                                                    
                                                    String ammoName = ChatColor.stripColor(isType2 + " Ammo Clip");
                                                    for (ItemStack ammoStack : tempinv)
                                                    {
                                                        if (ammoStack != null)
                                                        {
                                                            if (ammoStack.hasItemMeta())
                                                            {
                                                                if (ammoStack.getItemMeta().hasDisplayName())
                                                                {
                                                                    String ammoDisp = ammoStack.getItemMeta().getDisplayName();

                                                                    //Bukkit.broadcastMessage("ammoDisp is: " + ammoDisp);

                                                                    if (ammoDisp.contains(ammoName))
                                                                    {
                                                                        // matched
                                                                        //Bukkit.broadcastMessage("matched | " + ammoStack.getType().toString());
                                                                        playerInv.removeItem(ammoStack);
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                    
                                                    player.updateInventory();
                                                }
                                                else
                                                {
                                                  //  Bukkit.broadcastMessage("sedondAmmo");
                                                    secondAmmo = Guns.miscItemStacks.get(isType2).clone();
                                                    int amount = Guns.ammoCounts.get(isType2);
                                                    secondAmmoType = isType2;
                                                    secondAmmo.setAmount(amount);
                                                    
                                                    String ammoName = ChatColor.stripColor(isType2 + " Ammo Clip");
                                                    for (ItemStack ammoStack : tempinv)
                                                    {
                                                        if (ammoStack != null)
                                                        {
                                                            if (ammoStack.hasItemMeta())
                                                            {
                                                                if (ammoStack.getItemMeta().hasDisplayName())
                                                                {
                                                                    String ammoDisp = ammoStack.getItemMeta().getDisplayName();

                                                                    //Bukkit.broadcastMessage("ammoDisp is: " + ammoDisp);

                                                                    if (ammoDisp.contains(ammoName))
                                                                    {
                                                                        // matched
                                                                        //Bukkit.broadcastMessage("matched | " + ammoStack.getType().toString());
                                                                        playerInv.removeItem(ammoStack);
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                    
                                                    player.updateInventory();
                                                }
                                            }
                                        }
                                        
                                        if (Guns.DemoType.containsKey(isName))
                                        {
                                            if (chosenName.contains("Ammo") && (!(isType2.equalsIgnoreCase("Explosives"))))
                                            {
                                                if (firstAmmo == null)
                                                {
                                                    //Bukkit.broadcastMessage("demo firstAmmo");
                                                    firstAmmo = Guns.miscItemStacks.get(isType2).clone();
                                                    int amount = Guns.ammoCounts.get(isType2);
                                                    firstAmmoType = isType2;
                                                    firstAmmo.setAmount(amount);
                                                    
                                                    String ammoName = ChatColor.stripColor(isType2 + " Ammo Clip");
                                                    for (ItemStack ammoStack : tempinv)
                                                    {
                                                        if (ammoStack != null)
                                                        {
                                                            if (ammoStack.hasItemMeta())
                                                            {
                                                                if (ammoStack.getItemMeta().hasDisplayName())
                                                                {
                                                                    String ammoDisp = ammoStack.getItemMeta().getDisplayName();

                                                                    //Bukkit.broadcastMessage("ammoDisp is: " + ammoDisp);

                                                                    if (ammoDisp.contains(ammoName))
                                                                    {
                                                                        // matched
                                                                        //Bukkit.broadcastMessage("matched | " + ammoStack.getType().toString());
                                                                        playerInv.removeItem(ammoStack);
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                    
                                                    player.updateInventory();
                                                }
                                                else
                                                {
                                                   // Bukkit.broadcastMessage("demo secondAmmo");
                                                    secondAmmo = Guns.miscItemStacks.get(isType2).clone();
                                                    int amount = Guns.ammoCounts.get(isType2);
                                                    secondAmmoType = isType2;
                                                    secondAmmo.setAmount(amount);
                                                    
                                                    String ammoName = ChatColor.stripColor(isType2 + " Ammo Clip");
                                                    for (ItemStack ammoStack : tempinv)
                                                    {
                                                        if (ammoStack != null)
                                                        {
                                                            if (ammoStack.hasItemMeta())
                                                            {
                                                                if (ammoStack.getItemMeta().hasDisplayName())
                                                                {
                                                                    String ammoDisp = ammoStack.getItemMeta().getDisplayName();

                                                                    //Bukkit.broadcastMessage("ammoDisp is: " + ammoDisp);

                                                                    if (ammoDisp.contains(ammoName))
                                                                    {
                                                                        // matched
                                                                        //Bukkit.broadcastMessage("matched | " + ammoStack.getType().toString());
                                                                        playerInv.removeItem(ammoStack);
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                    
                                                    player.updateInventory();
                                                }
                                            }
                                        }
                                        
                                        if (Guns.MeleeList.containsKey(chosenName) && Guns.MeleeList.containsKey(isName))
                                        {
                                            String theCheck = itemLore.get(4);
                                            //Bukkit.broadcastMessage("is disp is:" + is.getItemMeta().getDisplayName());
                                            if (theCheck.contains("Primary"))
                                            {
                                                theCheck = "primary";
                                            }
                                            else
                                            {
                                                theCheck = "secondary";
                                            }
                                            
                                            if (theCheck.equalsIgnoreCase(primaryCheck))
                                            {
                                                meleeweapon = is;
                                                //Bukkit.broadcastMessage("meleeweapon is now " + is.getItemMeta().getDisplayName());
                                            }
                                            
                                        }
                                        
                                    }
                                }
                            }
                            
                            i = i + 1;
                        }
                    } // end for loop
                    
                    ItemStack tempstack = null; // this is the stack we'll be giving
                    ItemStack gunAmmo = null; // ammo stack we'll be giving;
                    
                    if (Guns.GunList.containsKey(chosenName) || Guns.DemoList.containsKey(chosenName))
                    {
                        if (!(chosenName.equalsIgnoreCase("Grenade")))
                        {
                            if (Guns.GunList.containsKey(chosenName))
                            {
                                tempstack = Guns.GunList.get(chosenName).clone();
                                
                                gunAmmo = Guns.miscItemStacks.get(chosenType).clone();
                                int amount = Guns.ammoCounts.get(chosenType);
                                gunAmmo.setAmount(amount);
                            }
                            
                            if (Guns.DemoList.containsKey(chosenName))
                            {
                                tempstack = Guns.DemoList.get(chosenName).clone();
                                
                                gunAmmo = Guns.miscItemStacks.get(chosenType).clone();
                                int amount = Guns.ammoCounts.get(chosenType);
                                gunAmmo.setAmount(amount);
                            }
                        }
                    }
                    
                    if (Guns.MeleeList.containsKey(chosenName))
                    {
                        tempstack = Guns.MeleeList.get(chosenName).clone();
                        
                        if (meleeweapon != null)
                        {
                            Bukkit.broadcastMessage("removing meleeweapon: " + meleeweapon.getItemMeta().getDisplayName());
                            playerInv.removeItem(meleeweapon);
                        }
                    }
                    
                    
                    boolean paperadded = false;
                    
                    if (chosenName.contains("Medical Kit"))
                    {
                        paperadded = true;
                        if (paperAmount > 0)
                        {
                            paperAmount = paperAmount + 1;
                            
                            if (paperAmount > Guns.maxMedKits)
                            {
                                paperAmount = Guns.maxMedKits;
                                
                                String message = ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.DARK_GRAY + "Maximum amount of Med Kits is: " + Guns.maxMedKits;
                                player.sendMessage(message);
                                
                                playerCash = Game.playerMoney.get(player.getName());
                                if (Game.playerMoney.containsKey(player.getName()))
                                {
                                    Game.playerMoney.remove(player.getName());
                                }
                                
                                int newBalance = playerCash + itemCost;
                                Game.playerMoney.put(player.getName(), newBalance);
                                player.setLevel(newBalance);
                                Scoreboard sb = player.getScoreboard();
                                if (sb != null)
                                {
                                    Objective objective = sb.getObjective(DisplaySlot.SIDEBAR);
                                    Score score = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.GOLD + " Cash: " + ChatColor.GREEN + "$"));
                                    score.setScore(newBalance);
                                }
                            }
                            tempstack = Guns.miscItemStacks.get("Medical Kit").clone();
                            tempstack.setAmount(paperAmount);
                        }
                        else
                        {
                            tempstack = Guns.miscItemStacks.get("Medical Kit").clone();
                        }
                    } // end med kit check
                    
                    boolean grenadeadded = false;
                    
                    if (chosenName.contains("Grenade"))
                    {
                        grenadeadded = true;
                        
                        if (grenadeAmount > 0)
                        {
                            grenadeAmount = grenadeAmount + 1;
                            if (grenadeAmount > Guns.maxGrenades)
                            {
                                grenadeAmount = Guns.maxGrenades;
                                String message = ChatColor.DARK_GRAY + "[SR" + ChatColor.DARK_RED + "KF" + ChatColor.DARK_GRAY + "] " + ChatColor.DARK_GRAY + "Maximum amount of Grenades is: " + Guns.maxGrenades;
                                player.sendMessage(message);

                                playerCash = Game.playerMoney.get(player.getName());
                                if (Game.playerMoney.containsKey(player.getName()))
                                {
                                    Game.playerMoney.remove(player.getName());
                                }
                                
                                int newBalance = playerCash + itemCost;
                                Game.playerMoney.put(player.getName(), newBalance);
                                player.setLevel(newBalance);
                                Scoreboard sb = player.getScoreboard();
                                if (sb != null)
                                {
                                    Objective objective = sb.getObjective(DisplaySlot.SIDEBAR);
                                    Score score = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.GOLD + " Cash: " + ChatColor.GREEN + "$"));
                                    score.setScore(newBalance);
                                }
                            }
                            
                            tempstack = Guns.DemoList.get("Grenade").clone();
                            tempstack.setAmount(grenadeAmount);
                        }
                        else
                        {
                            tempstack = Guns.DemoList.get("Grenade").clone();
                        }
                    } // end grenade check
                    
                    if (chosenName.contains("Ammo"))
                    {
                        if (firstAmmo != null)
                        {
                            int bonusAmount = 0;
                            String roleName = "Medic";
                            if (Game.playerClass.containsKey(player.getName()))
                            {
                                roleName = Game.playerClass.get(player.getName());
                            }
                            
                            if (roleName.equalsIgnoreCase("Engineer"))
                            {
                                bonusAmount = Engineer.getBallisticTrainingBonusAmount(player.getName());
                            }
                            
                            
                            int maxCount = Guns.ammoCounts.get(firstAmmoType);
                            //Bukkit.broadcastMessage("maxCount is: " + maxCount);
                            int total = maxCount + bonusAmount;
                            //Bukkit.broadcastMessage("total is: " + total);
                            firstAmmo.setAmount(total);
                            playerInv.addItem(firstAmmo);
                        }
                        
                        if (secondAmmo != null)
                        {
                            int bonusAmount = 0;
                            String roleName = "Medic";
                            if (Game.playerClass.containsKey(player.getName()))
                            {
                                roleName = Game.playerClass.get(player.getName());
                            }
                            
                            if (roleName.equalsIgnoreCase("Engineer"))
                            {
                                bonusAmount = Engineer.getBallisticTrainingBonusAmount(player.getName());
                            }
                            
                            int maxCount = Guns.ammoCounts.get(secondAmmoType);
 
                            //Bukkit.broadcastMessage("maxCount is: " + maxCount)
                                    ;
                            int total = maxCount + bonusAmount;
                            
                            //Bukkit.broadcastMessage("total is: " + total);
                            secondAmmo.setAmount(total);
                            playerInv.addItem(secondAmmo);
                        }
                    } // end refill check
                    
                    if (tempstack != null)
                    {
                        // add in the item now
                        ItemMeta itemMeta = tempstack.getItemMeta();
                        List<String> itemLore = itemMeta.getLore();
                        String itemName = itemLore.get(1);
                        
                        boolean isRanged = false;
                        if (Guns.GunList.containsKey(itemName) || Guns.DemoList.containsKey(itemName))
                        {
                            isRanged = true;
                        }
                        
                        if (isRanged)
                        {
                            EnhancementListener.getRangedWeaponEnhancements(tempstack, player);
                        }
                        else
                        {
                            EnhancementListener.getMeleeWeaponEnhancements(tempstack, player);
                        }
                        
                        playerInv.addItem(tempstack); // grant item purchased
                        
                        if (gunAmmo != null)
                        {
                            playerInv.addItem(gunAmmo);
                        }
                    } // end chosen stack
                    
                    if (paperAmount > 0 && paperadded == false)
                    {
                        ItemStack paperStack = Guns.miscItemStacks.get("Medical Kit").clone();
                        paperStack.setAmount(paperAmount);
                        playerInv.addItem(paperStack);
                    }

                    if (grenadeAmount > 0 && grenadeadded == false)
                    {
                        ItemStack grenadeStack = Guns.DemoList.get("Grenade").clone();
                        grenadeStack.setAmount(grenadeAmount);
                        playerInv.addItem(grenadeStack);
                    }
                    
                    
                }
                else
                {
                    player.closeInventory();
                }
            }
            else
            {
                player.closeInventory();
            }
        }
        else
        {
            player.closeInventory();
        }
    }
       
}
