package com.bgsoftware.superiorskyblock.nms.v1_19_R1;

import com.bgsoftware.superiorskyblock.nms.ICachedBlock;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

public final class NMSCachedBlock implements ICachedBlock {

    private final BlockData blockData;

    public NMSCachedBlock(Block block) {
        this.blockData = block.getBlockData();
    }

    @Override
    public void setBlock(Location location) {
        World world = location.getWorld();
        if (world != null)
            world.getBlockAt(location).setBlockData(blockData);
    }

}
