package de.jaypi4c.mineduino;

import de.jaypi4c.mineduino.block.MineDuinoBlocks;
import de.jaypi4c.mineduino.block.entity.MineDuinoBlockEntities;
import de.jaypi4c.mineduino.component.MineDuinoComponentTypes;
import de.jaypi4c.mineduino.item.MineDuinoItemGroups;
import de.jaypi4c.mineduino.item.MineDuinoItems;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MineDuino implements ModInitializer {

    public static final String MOD_ID = "mineduino";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        MineDuinoComponentTypes.registerDataComponentTypes();
        MineDuinoItems.registerItems();
        MineDuinoItemGroups.registerItemGroups();
        MineDuinoBlocks.registerBlocks();
        MineDuinoBlockEntities.registerBlockEntities();
    }
}
