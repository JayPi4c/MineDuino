package de.jaypi4c.mineduino.block;

import de.jaypi4c.mineduino.MineDuino;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class MineDuinoBlocks {

    public static final Block INTERACTOR = registerBlock("interactor",
            new Interactor(AbstractBlock.Settings.create()
                    .strength(3f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.METAL)));

    public static final Block RECEIVER = registerBlock("receiver",
            new Receiver(AbstractBlock.Settings.create()
                    .strength(3f)
                    .sounds(BlockSoundGroup.METAL)));

    public static final Block SENDER = registerBlock("sender",
            new Sender(AbstractBlock.Settings.create()
                    .strength(3f)
                    .sounds(BlockSoundGroup.METAL)));

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(MineDuino.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, Identifier.of(MineDuino.MOD_ID, name), new BlockItem(block, new Item.Settings()));
    }

    public static void registerBlocks() {
        MineDuino.LOGGER.info("Registering Blocks for  " + MineDuino.MOD_ID);
    }
}
