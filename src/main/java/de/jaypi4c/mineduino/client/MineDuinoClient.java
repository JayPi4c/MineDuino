package de.jaypi4c.mineduino.client;

import de.jaypi4c.mineduino.MineDuino;
import de.jaypi4c.mineduino.component.MineDuinoComponentTypes;
import de.jaypi4c.mineduino.item.MineDuinoItems;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;

public class MineDuinoClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        MineDuino.LOGGER.info("Initializing Client for {}", MineDuino.MOD_ID);

        ModelPredicateProviderRegistry.register(MineDuinoItems.GADGET,
                Identifier.of(MineDuino.MOD_ID, "gadget_state"),
                (stack, _, _, _) -> {
                    boolean isActive = stack.getOrDefault(MineDuinoComponentTypes.GADGET_STATE, false);
                    return isActive ? 1.0f : 0.0f;
                });
    }
}
