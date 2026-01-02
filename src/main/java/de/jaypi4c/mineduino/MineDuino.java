package de.jaypi4c.mineduino;

import de.jaypi4c.mineduino.block.MineDuinoBlocks;
import de.jaypi4c.mineduino.block.entity.MineDuinoBlockEntities;
import de.jaypi4c.mineduino.communication.ChannelManager;
import de.jaypi4c.mineduino.item.MineDuinoItemGroups;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MineDuino implements ModInitializer {

    public static final String MOD_ID = "mineduino";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ChannelManager.init();
        MineDuinoItemGroups.registerItemGroups();
        MineDuinoBlocks.registerBlocks();
        MineDuinoBlockEntities.registerBlockEntities();
    }
}
