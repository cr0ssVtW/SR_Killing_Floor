package sr.Extras;

import java.util.UUID;
import net.minecraft.server.v1_7_R1.AttributeInstance;
import net.minecraft.server.v1_7_R1.AttributeModifier;
import net.minecraft.server.v1_7_R1.EntityInsentient;
import net.minecraft.server.v1_7_R1.GenericAttributes;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;

/**
 *
 * @author Cross
 */
public class CustomAttributes 
{
    public static final UUID maxHealthUID = UUID.fromString("f8b0a945-2d6a-4bdb-9a6f-59c285bf1e5d");
    public static final UUID followRangeUID = UUID.fromString("1737400d-3c18-41ba-8314-49a158481e1e");
    public static final UUID knockbackResistanceUID = UUID.fromString("8742c557-fcd5-4079-a462-b58db99b0f2c");
    public static final UUID movementSpeedUID = UUID.fromString("206a89dc-ae78-4c4d-b42c-3b31db3f5a7c");
    public static final UUID attackDamageUID = UUID.fromString("7bbe3bb1-079d-4150-ac6f-669e71550776");
    
    
    public static void applyMaxHealth(LivingEntity le, double multiplier)
    {
        EntityInsentient nmsEntity = (EntityInsentient) ((CraftLivingEntity) le).getHandle();
        AttributeInstance attributes = nmsEntity.getAttributeInstance(GenericAttributes.a);
        AttributeModifier modifier = new AttributeModifier(maxHealthUID, "SRKF max health multiplier", multiplier, 1);

        attributes.b(modifier);
        attributes.a(modifier);
    }
    
    public static void applyFollowRange(LivingEntity le, double multiplier)
    {
        EntityInsentient nmsEntity = (EntityInsentient) ((CraftLivingEntity) le).getHandle();
        AttributeInstance attributes = nmsEntity.getAttributeInstance(GenericAttributes.b);
        AttributeModifier modifier = new AttributeModifier(followRangeUID, "SRKF follow range multiplier", multiplier, 1);

        attributes.b(modifier);
        attributes.a(modifier);
    }
    
    public static void applyKnockbackResistance(LivingEntity le)
    {
        EntityInsentient nmsEntity = (EntityInsentient) ((CraftLivingEntity) le).getHandle();
        AttributeInstance attributes = nmsEntity.getAttributeInstance(GenericAttributes.c);
        AttributeModifier modifier = new AttributeModifier(knockbackResistanceUID, "SRKF knockback resistance multiplier", 1.0D, 1);
        //double value = attributes.getValue();
        //Bukkit.broadcastMessage("value is " + value);

        attributes.setValue(1.0D);
        attributes.b(modifier);
        attributes.a(modifier);
        //value = attributes.getValue();
        //Bukkit.broadcastMessage("value is now " + value);
    }
    
    public static void applyMovementSpeed(LivingEntity le, double multiplier)
    {
        EntityInsentient nmsEntity = (EntityInsentient) ((CraftLivingEntity) le).getHandle();
        AttributeInstance attributes = nmsEntity.getAttributeInstance(GenericAttributes.d);
        AttributeModifier modifier = new AttributeModifier(movementSpeedUID, "SRKF movement speed multiplier", multiplier, 1);

        attributes.b(modifier);
        attributes.a(modifier);
    }
    
    public static void applyAttackDamage(LivingEntity le, double multiplier)
    {
        EntityInsentient nmsEntity = (EntityInsentient) ((CraftLivingEntity) le).getHandle();
        AttributeInstance attributes = nmsEntity.getAttributeInstance(GenericAttributes.e);
        AttributeModifier modifier = new AttributeModifier(attackDamageUID, "SRKF attack damage multiplier", multiplier, 1);

        attributes.b(modifier);
        attributes.a(modifier);
    }
}
