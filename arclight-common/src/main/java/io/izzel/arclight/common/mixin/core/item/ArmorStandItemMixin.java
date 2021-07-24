package io.izzel.arclight.common.mixin.core.item;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.ArmorStandItem;
import net.minecraft.world.item.context.UseOnContext;
import org.bukkit.craftbukkit.v.event.CraftEventFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArmorStandItem.class)
public class ArmorStandItemMixin {

    private transient ArmorStand arclight$entity;

    @Redirect(method = "useOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/decoration/ArmorStand;moveTo(DDDFF)V"))
    public void arclight$captureEntity(ArmorStand armorStandEntity, double x, double y, double z, float yaw, float pitch) {
        armorStandEntity.moveTo(x, y, z, yaw, pitch);
        arclight$entity = armorStandEntity;
    }

    @Inject(method = "useOn", cancellable = true, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"))
    public void arclight$entityPlace(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        if (CraftEventFactory.callEntityPlaceEvent(context, arclight$entity).isCancelled()) {
            cir.setReturnValue(InteractionResult.FAIL);
        }
        arclight$entity = null;
    }
}
