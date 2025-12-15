package de.jaypi4c.mineduino.block.entity;

import de.jaypi4c.mineduino.MineDuino;
import de.jaypi4c.mineduino.block.MineDuinoBlocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class MineDuinoBlockEntities {

    public static void registerBlockEntities() {
        MineDuino.LOGGER.info("Registering Block Entities for " + MineDuino.MOD_ID);
    }

    public static final BlockEntityType<InteractorBlockEntity> INTERACTOR_BLOCK_ENTITY =
            Registry.register(Registries.BLOCK_ENTITY_TYPE,
                    Identifier.of(MineDuino.MOD_ID, "interactor_block_entity"),
                    BlockEntityType
                            .Builder
                            .create(InteractorBlockEntity::new, MineDuinoBlocks.INTERACTOR)
                            .build(null));

}
