package de.jaypi4c.mineduino.item;

import de.jaypi4c.mineduino.MineDuino;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class MineDuinoItems {

    public static final Item GADGET = registerItem("gadget", new GadgetItem(new Item.Settings()));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(MineDuino.MOD_ID, name), item);
    }

    public static void registerItems() {
        MineDuino.LOGGER.info("Registering Items for {}", MineDuino.MOD_ID);
    }

}
