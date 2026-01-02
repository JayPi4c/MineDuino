package de.jaypi4c.mineduino.item;

import de.jaypi4c.mineduino.MineDuino;
import de.jaypi4c.mineduino.block.MineDuinoBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class MineDuinoItemGroups {

    public static final ItemGroup MINEDUINO_GROUP = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(MineDuino.MOD_ID, "mineduino_items"),
            FabricItemGroup.builder()
                    .icon(() -> new ItemStack((MineDuinoBlocks.SENDER)))
                    .displayName(Text.translatable("itemgroup.mineduino.items"))
                    .entries(((ignored, entries) -> {
                        entries.add(MineDuinoBlocks.INTERACTOR);
                        entries.add(MineDuinoBlocks.RECEIVER);
                        entries.add(MineDuinoBlocks.SENDER);
                    }))
                    .build());

    public static void registerItemGroups() {
        MineDuino.LOGGER.info("Registering Item Groups for " + MineDuino.MOD_ID);
    }

}
