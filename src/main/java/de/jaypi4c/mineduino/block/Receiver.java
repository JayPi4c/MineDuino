package de.jaypi4c.mineduino.block;

import com.mojang.serialization.MapCodec;
import de.jaypi4c.mineduino.MineDuino;
import de.jaypi4c.mineduino.block.entity.ReceiverBlockEntity;
import de.jaypi4c.mineduino.communication.ChannelManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.schlunzis.jduino.protocol.tlv.TLVMessage;
import org.schlunzis.jduino.simple.SimpleChannel;

public class Receiver extends BlockWithEntity {
    public static final BooleanProperty POWERED = Properties.POWERED;
    public static final MapCodec<Receiver> CODEC = Interactor.createCodec(Receiver::new);

    public Receiver(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(POWERED, false));
        ChannelManager.addListener(message -> trigger((TLVMessage) message));
    }

    // helper to toggle from outside if needed
    public static void setPowered(World world, BlockPos pos, boolean powered) {
        BlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof Receiver && state.get(POWERED) != powered) {
            world.setBlockState(pos, state.with(POWERED, powered), 3);
            world.updateNeighborsAlways(pos, state.getBlock());
        }
    }

    public static void togglePowered(World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof Receiver) {
            boolean newPowered = !state.get(POWERED);
            setPowered(world, pos, newPowered);
        }
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
    }

    private void trigger(TLVMessage message) {
        if (message.type() == SimpleChannel.CMD_BUTTON) {
            MineDuino.LOGGER.info("Receiver triggered!");
            ReceiverBlockEntity.broadcastTrigger();
        }
    }

    @Override
    protected boolean emitsRedstonePower(BlockState state) {
        return true;
    }

    @Override
    protected int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return state.get(POWERED) ? 15 : 0;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ReceiverBlockEntity(pos, state);
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
}
