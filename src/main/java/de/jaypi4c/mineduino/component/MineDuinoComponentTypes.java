package de.jaypi4c.mineduino.component;

import com.mojang.serialization.Codec;
import de.jaypi4c.mineduino.MineDuino;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.function.UnaryOperator;

public class MineDuinoComponentTypes {

    public static final ComponentType<Boolean> GADGET_STATE = register("gadget_state", builder -> builder.codec(Codec.BOOL));

    private static <T> ComponentType<T> register(String name, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(MineDuino.MOD_ID, name),
                builderOperator.apply(ComponentType.builder()).build());
    }

    public static void registerDataComponentTypes() {
        MineDuino.LOGGER.info("Registering Data Component Types for {}", MineDuino.MOD_ID);
    }

}
