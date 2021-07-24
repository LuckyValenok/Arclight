package io.izzel.arclight.common.mixin.core.world.server;

import io.izzel.arclight.common.bridge.world.WorldBridge;
import io.izzel.arclight.common.bridge.world.server.ChunkManagerBridge;
import io.izzel.arclight.common.mod.util.ArclightCallbackExecutor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import javax.annotation.Nullable;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import java.util.function.BooleanSupplier;

@Mixin(ChunkMap.class)
public abstract class ChunkManagerMixin implements ChunkManagerBridge {

    // @formatter:off
    @Shadow @Nullable protected abstract ChunkHolder getUpdatingChunkIfPresent(long chunkPosIn);
    @Shadow protected abstract Iterable<ChunkHolder> getChunks();
    @Shadow abstract boolean noPlayersCloseForSpawning(ChunkPos chunkPosIn);
    @Shadow protected abstract void tick();
    @Shadow @Final @Mutable public ChunkGenerator generator;
    @Invoker("tick") public abstract void bridge$tick(BooleanSupplier hasMoreTime);
    @Invoker("setViewDistance") public abstract void bridge$setViewDistance(int i);
    // @formatter:on

    @Redirect(method = "readChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;dimension()Lnet/minecraft/resources/ResourceKey;"))
    private ResourceKey<DimensionType> arclight$useTypeKey(ServerLevel serverWorld) {
        return ((WorldBridge) serverWorld).bridge$getTypeKey();
    }

    public final ArclightCallbackExecutor callbackExecutor = new ArclightCallbackExecutor();

    @Override
    public ArclightCallbackExecutor bridge$getCallbackExecutor() {
        return this.callbackExecutor;
    }

    @Override
    public ChunkHolder bridge$chunkHolderAt(long chunkPos) {
        return getUpdatingChunkIfPresent(chunkPos);
    }

    @Override
    public Iterable<ChunkHolder> bridge$getLoadedChunksIterable() {
        return this.getChunks();
    }

    @Override
    public boolean bridge$isOutsideSpawningRadius(ChunkPos chunkPosIn) {
        return this.noPlayersCloseForSpawning(chunkPosIn);
    }

    @Override
    public void bridge$tickEntityTracker() {
        this.tick();
    }

    @Override
    public void bridge$setChunkGenerator(ChunkGenerator generator) {
        this.generator = generator;
    }
}
