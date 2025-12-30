package de.jaypi4c.mineduino.block;

import com.mojang.serialization.MapCodec;
import de.jaypi4c.mineduino.MineDuino;
import de.jaypi4c.mineduino.block.entity.InteractorBlockEntity;
import de.jaypi4c.mineduino.communication.ChannelManager;
import de.jaypi4c.mineduino.gui.screens.SettingsScreen;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class Interactor extends BlockWithEntity {

    public static final MapCodec<Interactor> CODEC = Interactor.createCodec(Interactor::new);

    public Interactor(Settings settings) {
        super(settings);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!world.isClient()) {
            UUID id = player.getUuid();
            MineDuino.LOGGER.info("Interactor used by player: {} with UUID: {}", player.getName().getString(), id);
            BlockEntity be = world.getBlockEntity(pos);
            if (be instanceof InteractorBlockEntity ibe) {
                UUID owner = ibe.getOwner();
                if (owner.equals(InteractorBlockEntity.UNOWNED_UUID)) {
                    ChannelManager.getInstance().sendLEDCommand(true);
                    // not claimed yet
                    ibe.setOwner(id);
                    return ActionResult.SUCCESS_NO_ITEM_USED;
                } else if (!owner.equals(id)) {
                    // claimed by another player
                    return ActionResult.FAIL;
                } else {
                    ChannelManager.getInstance().sendLEDCommand(false);
                    // claimed by the same player
                    MineDuino.LOGGER.info("Interactor at {} used by its owner {}. Unclaiming it...", pos, player.getName().getString());
                    ibe.setOwner(InteractorBlockEntity.UNOWNED_UUID);
                    return ActionResult.SUCCESS_NO_ITEM_USED;
                }
            }
        } else if (player.isSneaking()) {
            MinecraftClient.getInstance().setScreen(new SettingsScreen(null, Text.empty()));
        }
        return super.onUse(state, world, pos, player, hit);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new InteractorBlockEntity(pos, state);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

}
