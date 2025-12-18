package de.jaypi4c.mineduino.block;

import com.mojang.serialization.MapCodec;
import de.jaypi4c.mineduino.MineDuino;
import de.jaypi4c.mineduino.block.entity.InteractorBlockEntity;
import de.jaypi4c.mineduino.communication.ChannelManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class Interactor extends BlockWithEntity {

    public static final BooleanProperty POWERED = Properties.POWERED;
    public static final MapCodec<Interactor> CODEC = Interactor.createCodec(Interactor::new);

    public Interactor(Settings settings) {
        super(settings);
        // set default blockstate with POWERED = false
        this.setDefaultState(this.stateManager.getDefaultState().with(POWERED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
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
                    // ChannelManager.simpleChannel.sendLEDCommand(13, true);
                    // not claimed yet
                    ibe.setOwner(id);
                    return ActionResult.SUCCESS_NO_ITEM_USED;
                } else if (!owner.equals(id)) {
                    // claimed by another player
                    return ActionResult.FAIL;
                } else {
                    // ChannelManager.simpleChannel.sendLEDCommand(13, false);
                    // claimed by the same player
                    MineDuino.LOGGER.info("Interactor at {} used by its owner {}. Unclaiming it...", pos, player.getName().getString());
                    ibe.setOwner(InteractorBlockEntity.UNOWNED_UUID);
                    return ActionResult.SUCCESS_NO_ITEM_USED;
                }
            }
        }
        return super.onUse(state, world, pos, player, hit);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        if (!world.isClient()) {
            boolean powered = world.isReceivingRedstonePower(pos);
            boolean prev = state.get(POWERED);
            if (powered != prev) {
                // update the block state so the property reflects the new power state
                world.setBlockState(pos, state.with(POWERED, powered), 3);
                // run your logic when power changes
                onPowerChange(world, pos, powered);
            }
        }
        super.neighborUpdate(state, world, pos, block, fromPos, notify);
    }


    private void onPowerChange(World world, BlockPos pos, boolean powered) {
        BlockEntity be = world.getBlockEntity(pos);
        if (be instanceof InteractorBlockEntity ibe) {
            if (powered) {
                // example: run code when powered
                ChannelManager.simpleChannel.sendLEDCommand(13, true);
                // call custom block-entity logic if needed:
                // ibe.onPowered();
            } else {
                ChannelManager.simpleChannel.sendLEDCommand(13, false);
                // ibe.onUnpowered();
            }
        }
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
