package de.jaypi4c.mineduino.item;

import de.jaypi4c.mineduino.MineDuino;
import de.jaypi4c.mineduino.component.MineDuinoComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class GadgetItem extends Item {

    public GadgetItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        MineDuino.LOGGER.info("GadgetItem used by {}", user.getName().getString());
        ItemStack stack = user.getStackInHand(hand);
        if (!world.isClient) {
            MineDuino.LOGGER.info("Toggling gadget state for {}", user.getName().getString());
            boolean isActive = Boolean.TRUE.equals(stack.get(MineDuinoComponentTypes.GADGET_STATE));
            stack.set(MineDuinoComponentTypes.GADGET_STATE, !isActive);
        }
        return TypedActionResult.success(stack, world.isClient);
    }
}
