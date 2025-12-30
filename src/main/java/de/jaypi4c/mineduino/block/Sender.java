package de.jaypi4c.mineduino.block;

import com.mojang.serialization.MapCodec;
import de.jaypi4c.mineduino.communication.ChannelManager;
import net.minecraft.block.AbstractRedstoneGateBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.apache.commons.lang3.tuple.Triple;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Sender extends Block {

    public static final BooleanProperty POWERED = AbstractRedstoneGateBlock.POWERED;
    public static final MapCodec<Sender> CODEC = createCodec(Sender::new);

    public Sender(Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultState().with(POWERED, false));
    }

    private static void spawnParticles(BlockState state, WorldAccess world, BlockPos pos, Random random, float alpha) {
        final List<Triple<Double, Double, Double>> offsets = List.of(
                Triple.of(0.5, 0.5, 1.0),
                Triple.of(0.5, 0.5, 0.0),
                Triple.of(0.5, 1.0, 0.5),
                Triple.of(0.5, 0.0, 0.5),
                Triple.of(1.0, 0.5, 0.5),
                Triple.of(0.0, 0.5, 0.5)
        );
        for (Triple<Double, Double, Double> offset : offsets) {
            if (random.nextFloat() < .25f)
                world.addParticle(new DustParticleEffect(Vec3d.unpackRgb(13178547).toVector3f(), alpha),
                        pos.getX() + offset.getLeft(),
                        pos.getY() + offset.getMiddle(),
                        pos.getZ() + offset.getRight(),
                        random.nextDouble() - 0.5 / 10,
                        random.nextDouble() - 0.5 / 10,
                        random.nextDouble() - 0.5 / 10);
        }
    }

    @Override
    public MapCodec<Sender> getCodec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(POWERED, ctx.getWorld().isReceivingRedstonePower(ctx.getBlockPos()));
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (state.get(POWERED)) {
            spawnParticles(state, world, pos, random, 0.5F);
        }
    }

    @Override
    protected void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (!world.isClient) {
            boolean bl = state.get(POWERED);
            if (bl != world.isReceivingRedstonePower(pos)) {
                if (bl) {
                    world.scheduleBlockTick(pos, this, 4);
                    ChannelManager.simpleChannel.sendLEDCommand(13, false);
                } else {
                    world.setBlockState(pos, state.cycle(POWERED), Block.NOTIFY_LISTENERS);
                    ChannelManager.simpleChannel.sendLEDCommand(13, true);
                }
            }
        }
    }

    @Override
    protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (state.get(POWERED) && !world.isReceivingRedstonePower(pos)) {
            world.setBlockState(pos, state.cycle(POWERED), Block.NOTIFY_LISTENERS);
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
    }

}
