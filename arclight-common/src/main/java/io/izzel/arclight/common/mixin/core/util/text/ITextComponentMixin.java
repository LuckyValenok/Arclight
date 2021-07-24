package io.izzel.arclight.common.mixin.core.util.text;

import com.google.common.collect.Streams;
import io.izzel.arclight.common.bridge.util.text.ITextComponentBridge;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import net.minecraft.network.chat.Component;

@Mixin(Component.class)
public interface ITextComponentMixin extends ITextComponentBridge, Iterable<Component> {

    // @formatter:off
    @Shadow List<Component> getSiblings();
    // @formatter:on

    default Stream<Component> stream() {
        class Func implements Function<Component, Stream<? extends Component>> {

            @Override
            public Stream<? extends Component> apply(Component component) {
                return ((ITextComponentBridge) component).bridge$stream();
            }
        }
        return Streams.concat(Stream.of((Component) this), this.getSiblings().stream().flatMap(new Func()));
    }

    @Override
    default @NotNull Iterator<Component> iterator() {
        return this.stream().iterator();
    }

    @Override
    default Stream<Component> bridge$stream() {
        return stream();
    }

    @Override
    default Iterator<Component> bridge$iterator() {
        return iterator();
    }
}
