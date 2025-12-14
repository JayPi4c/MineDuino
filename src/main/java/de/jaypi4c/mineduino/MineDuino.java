package de.jaypi4c.mineduino;

import de.jaypi4c.mineduino.block.MineDuinoBlocks;
import de.jaypi4c.mineduino.item.MineDuinoItems;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MineDuino implements ModInitializer {
    public static final String MOD_ID = "mineduino";

    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        MineDuinoItems.registerItems();
        MineDuinoBlocks.registerBlocks();
    }
}