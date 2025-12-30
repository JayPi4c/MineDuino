// language: java
package de.jaypi4c.mineduino.block.entity;

import de.jaypi4c.mineduino.block.Receiver;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.*;

public class ReceiverBlockEntity extends BlockEntity {

    // weak set so unloaded block entities can be GC'd
    private static final Set<ReceiverBlockEntity> INSTANCES =
            Collections.newSetFromMap(new WeakHashMap<>());
    private boolean powered = false;

    public ReceiverBlockEntity(BlockPos pos, BlockState state) {
        super(MineDuinoBlockEntities.RECEIVER_BLOCK_ENTITY, pos, state);
        synchronized (INSTANCES) {
            INSTANCES.add(this);
        }
    }

    // called by ChannelManager when a message arrives
    public static void broadcastTrigger() {
        // copy to avoid concurrent modification and to avoid holding lock while calling trigger
        List<ReceiverBlockEntity> copy = new ArrayList<>();
        synchronized (INSTANCES) {
            copy.addAll(INSTANCES);
        }
        for (ReceiverBlockEntity be : copy) {
            // trigger will check world != null and !world.isClient
            be.trigger();
        }
    }

    @Override
    public void markRemoved() {
        synchronized (INSTANCES) {
            INSTANCES.remove(this);
        }
        super.markRemoved();
    }


    public boolean isPowered() {
        return powered;
    }

    public void trigger() {
        World world = this.getWorld();
        if (world == null || world.isClient) return;

        this.powered = !this.powered;

        // update block state property so redstone reads the new value
        Receiver.setPowered(world, this.pos, this.powered);

        markDirty();
    }

    @Override
    public void writeNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(tag, registryLookup);
        tag.putBoolean("Powered", powered);
    }

    @Override
    public void readNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(tag, registryLookup);
        if (tag.contains("Powered")) powered = tag.getBoolean("Powered");
    }
}
