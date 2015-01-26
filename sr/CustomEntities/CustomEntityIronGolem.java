package sr.CustomEntities;

import net.minecraft.server.v1_7_R1.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_7_R1.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_7_R1.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_7_R1.PathfinderGoalMoveThroughVillage;
import net.minecraft.server.v1_7_R1.PathfinderGoalMoveTowardsRestriction;
import net.minecraft.server.v1_7_R1.PathfinderGoalMoveTowardsTarget;
import net.minecraft.server.v1_7_R1.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_7_R1.PathfinderGoalOfferFlower;
import net.minecraft.server.v1_7_R1.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_7_R1.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_7_R1.World;
import net.minecraft.server.v1_7_R1.EntityHuman;
import net.minecraft.server.v1_7_R1.GenericAttributes;
import net.minecraft.server.v1_7_R1.IMonster;

/**
 *
 * @author Cross
 */
public class CustomEntityIronGolem extends net.minecraft.server.v1_7_R1.EntityIronGolem
{
    public CustomEntityIronGolem(World world) 
    {
        
        super(world);
        this.a(1.4F, 2.9F);
        this.getNavigation().a(true);
        
        
        
        /*
        try
        {
            Field goala = this.goalSelector.getClass().getDeclaredField("a");
            goala.setAccessible(true);
            ((List<PathfinderGoal>) goala.get(this.goalSelector)).clear();
            Field targeta = this.targetSelector.getClass().getDeclaredField("a");
            targeta.setAccessible(true);
            ((List<PathfinderGoal>) targeta.get(this.targetSelector)).clear();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        
        Bukkit.broadcastMessage("CustomEntityIronGolem");
        */
        
        this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this, EntityHuman.class, 1.0D, true));
        this.goalSelector.a(2, new PathfinderGoalMoveTowardsTarget(this, 0.9D, 64.0F));
        this.goalSelector.a(3, new PathfinderGoalMoveThroughVillage(this, 0.6D, true));
        this.goalSelector.a(4, new PathfinderGoalMoveTowardsRestriction(this, 1.0D));
        this.goalSelector.a(5, new PathfinderGoalOfferFlower(this));
        this.goalSelector.a(6, new PathfinderGoalRandomStroll(this, 0.6D));
        this.goalSelector.a(7, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 6.0F));
        this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
        this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, true));
        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, 0, false, false));
        
        
        this.getAttributeInstance(GenericAttributes.a).setValue(1500.0D); // max health - this is being set in MobSpawn class
        this.getAttributeInstance(GenericAttributes.b).setValue(100.0D); // follow range in blocks
        this.getAttributeInstance(GenericAttributes.c).setValue(1.0D); // knockback resist (1.0 is max).
        this.getAttributeInstance(GenericAttributes.d).setValue(0.31D); // move speed
        
        
        
        /*
        this.goalSelector.a(1, new PathfinderGoalMeleeAttack(this, EntityHuman.class, 0.25F, true));
        this.goalSelector.a(2, new PathfinderGoalMoveTowardsTarget(this, 0.22F, 32.0F));
        this.goalSelector.a(3, new PathfinderGoalMoveThroughVillage(this, 0.16F, true));
        this.goalSelector.a(4, new PathfinderGoalMoveTowardsRestriction(this, 0.16F));
        this.goalSelector.a(5, new PathfinderGoalOfferFlower(this));
        this.goalSelector.a(6, new PathfinderGoalRandomStroll(this, 0.16F));
        this.goalSelector.a(7, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 6.0F));
        this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
        this.targetSelector.a(1, new PathfinderGoalDefendVillage(this));
        this.targetSelector.a(2, new PathfinderGoalHurtByTarget(this, false));
        this.targetSelector.a(3, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, 16, false, true));
        */
    }
    
    
    
    public void anger() 
    {
        this.targetSelector.a(3, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, 16, false, true));
    }
    
    
    public void rose(boolean bool) 
    {
        super.e(bool);
    }
    
}

