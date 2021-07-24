package io.izzel.arclight.common.mixin.core.inventory.container;

import io.izzel.arclight.common.bridge.inventory.container.PosContainerBridge;
import io.izzel.arclight.common.mixin.core.inventory.InventoryMixin;
import net.minecraft.world.inventory.LoomMenu;
import org.bukkit.Location;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(targets = "net/minecraft/world/inventory/LoomMenu$1")
public abstract class LoomContainer1Mixin extends InventoryMixin {

    @Shadow(aliases = {"this$0", "field_213913_a"}, remap = false) private LoomMenu outerThis;

    @Override
    public Location getLocation() {
        return ((PosContainerBridge) outerThis).bridge$getWorldLocation();
    }
}
