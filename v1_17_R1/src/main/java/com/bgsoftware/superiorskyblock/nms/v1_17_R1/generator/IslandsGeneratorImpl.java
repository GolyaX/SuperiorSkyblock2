package com.bgsoftware.superiorskyblock.nms.v1_17_R1.generator;

import com.bgsoftware.common.reflection.ReflectField;
import com.bgsoftware.superiorskyblock.SuperiorSkyblockPlugin;
import com.bgsoftware.superiorskyblock.world.generator.IslandsGenerator;
import net.minecraft.core.IRegistry;
import net.minecraft.world.level.biome.BiomeBase;
import net.minecraft.world.level.chunk.BiomeStorage;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.v1_17_R1.block.CraftBlock;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@SuppressWarnings({"unused", "NullableProblems"})
public class IslandsGeneratorImpl extends IslandsGenerator {

    private static final ReflectField<BiomeBase[]> BIOME_BASE_ARRAY = new ReflectField<>(
            BiomeStorage.class, BiomeBase[].class, "f");
    private static final ReflectField<BiomeStorage> BIOME_STORAGE = new ReflectField<>(
            "org.bukkit.craftbukkit.VERSION.generator.CustomChunkGenerator$CustomBiomeGrid", BiomeStorage.class, "biome");

    private final SuperiorSkyblockPlugin plugin;

    public IslandsGeneratorImpl(SuperiorSkyblockPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biomeGrid) {
        ChunkData chunkData = createChunkData(world);

        Biome targetBiome;

        switch (world.getEnvironment()) {
            case NETHER -> {
                try {
                    targetBiome = Biome.valueOf(plugin.getSettings().getWorlds().getNether().getBiome());
                } catch (IllegalArgumentException error) {
                    targetBiome = Biome.NETHER_WASTES;
                }
            }
            case THE_END -> {
                try {
                    targetBiome = Biome.valueOf(plugin.getSettings().getWorlds().getEnd().getBiome());
                } catch (IllegalArgumentException error) {
                    targetBiome = Biome.THE_END;
                }
            }
            default -> {
                try {
                    targetBiome = Biome.valueOf(plugin.getSettings().getWorlds().getNormal().getBiome());
                } catch (IllegalArgumentException error) {
                    targetBiome = Biome.PLAINS;
                }
            }
        }

        setBiome(biomeGrid, targetBiome);

        if (chunkX == 0 && chunkZ == 0 && world.getEnvironment() == plugin.getSettings().getWorlds().getDefaultWorld()) {
            chunkData.setBlock(0, 99, 0, Material.BEDROCK);
        }

        return chunkData;
    }

    @Override
    public List<BlockPopulator> getDefaultPopulators(World world) {
        return Collections.emptyList();
    }

    @Override
    public Location getFixedSpawnLocation(World world, Random random) {
        return new Location(world, 0, 100, 0);
    }

    private static void setBiome(ChunkGenerator.BiomeGrid biomeGrid, Biome biome) {
        BiomeStorage biomeStorage = BIOME_STORAGE.get(biomeGrid);
        BiomeBase[] biomeBases = BIOME_BASE_ARRAY.get(biomeStorage);

        BiomeBase biomeBase = CraftBlock.biomeToBiomeBase((IRegistry<BiomeBase>) biomeStorage.e, biome);

        if (biomeBases == null)
            return;

        Arrays.fill(biomeBases, biomeBase);
    }

}
