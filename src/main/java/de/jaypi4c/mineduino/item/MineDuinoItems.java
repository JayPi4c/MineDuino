package de.jaypi4c.mineduino.item;

import de.jaypi4c.mineduino.MineDuino;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class MineDuinoItems {

    public static final Item ADIOMANTIUM_DUST = registerItem("adiomantium_dust", new Item(new Item.Settings()));
    public static final Item ADIOMANTIUM_INGOT = registerItem("adiomantium_ingot", new Item(new Item.Settings()));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(MineDuino.MOD_ID, name), item);
    }

    public static void registerItems() {
        MineDuino.LOGGER.info("Registering Items for  " + MineDuino.MOD_ID);
    }


}
