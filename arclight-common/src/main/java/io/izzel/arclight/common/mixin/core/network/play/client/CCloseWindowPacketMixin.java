package io.izzel.arclight.common.mixin.core.network.play.client;

import net.minecraft.network.protocol.game.ServerboundContainerClosePacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ServerboundContainerClosePacket.class)
public class CCloseWindowPacketMixin {

    @Shadow private int containerId;

    public void arclight$constructor() {
        throw new RuntimeException();
    }

    public void arclight$constructor(int id) {
        arclight$constructor();
        this.containerId = id;
    }
}
