/*
 *       _____  _       _    _____                                _
 *      |  __ \| |     | |  / ____|                              | |
 *      | |__) | | ___ | |_| (___   __ _ _   _  __ _ _ __ ___  __| |
 *      |  ___/| |/ _ \| __|\___ \ / _` | | | |/ _` | '__/ _ \/ _` |
 *      | |    | | (_) | |_ ____) | (_| | |_| | (_| | | |  __/ (_| |
 *      |_|    |_|\___/ \__|_____/ \__, |\__,_|\__,_|_|  \___|\__,_|
 *                                    | |
 *                                    |_|
 *            PlotSquared plot management system for Minecraft
 *               Copyright (C) 2014 - 2022 IntellectualSites
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.plotsquared.core.queue;

import com.plotsquared.core.location.Location;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.biome.BiomeType;
import com.sk89q.worldedit.world.block.BaseBlock;
import com.sk89q.worldedit.world.block.BlockState;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Queue that is limited to a single chunk
 */
public class ChunkQueueCoordinator extends ScopedQueueCoordinator {

    public final BiomeType[][][] biomeResult;
    public final BlockState[][][] result;
    private final int width;
    private final int length;
    private final BlockVector3 bot;
    private final BlockVector3 top;
    private final World weWorld;

    public ChunkQueueCoordinator(
            final @NonNull World weWorld,
            @NonNull BlockVector3 bot,
            @NonNull BlockVector3 top,
            boolean biomes
    ) {
        super(null, Location.at("", 0, weWorld.getMinY(), 0), Location.at("", 15, weWorld.getMaxY(), 15));
        this.weWorld = weWorld;
        this.width = top.getX() - bot.getX() + 1;
        this.length = top.getZ() - bot.getZ() + 1;
        this.result = new BlockState[weWorld.getMaxY() - weWorld.getMinY() + 1][width][length];
        this.biomeResult = biomes ? new BiomeType[weWorld.getMaxY() - weWorld.getMinY() + 1][width][length] : null;
        this.bot = bot;
        this.top = top;
    }

    public @NonNull BlockState[][][] getBlocks() {
        return result;
    }

    @Override
    public boolean setBiome(int x, int z, @NonNull BiomeType biomeType) {
        if (this.biomeResult != null) {
            for (int y = weWorld.getMinY(); y <= weWorld.getMaxY(); y++) {
                this.storeCacheBiome(x, y, z, biomeType);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean setBiome(int x, int y, int z, @NonNull BiomeType biomeType) {
        if (this.biomeResult != null) {
            this.storeCacheBiome(x, y, z, biomeType);
            return true;
        }
        return false;
    }

    @Override
    public boolean setBlock(int x, int y, int z, @NonNull BlockState id) {
        this.storeCache(x, y, z, id);
        return true;
    }

    @Override
    public boolean setBlock(int x, int y, int z, @NonNull Pattern pattern) {
        this.storeCache(x, y, z, pattern.applyBlock(BlockVector3.at(x, y, z)).toImmutableState());
        return true;
    }

    private void storeCache(final int x, final int y, final int z, final @NonNull BlockState id) {
        int yIndex = getYIndex(y);
        BlockState[][] resultY = result[yIndex];
        if (resultY == null) {
            result[yIndex] = resultY = new BlockState[length][];
        }
        BlockState[] resultYZ = resultY[z];
        if (resultYZ == null) {
            resultY[z] = resultYZ = new BlockState[width];
        }
        resultYZ[x] = id;
    }

    private void storeCacheBiome(final int x, final int y, final int z, final @NonNull BiomeType id) {
        int yIndex = getYIndex(y);
        BiomeType[][] resultY = biomeResult[yIndex];
        if (resultY == null) {
            biomeResult[yIndex] = resultY = new BiomeType[length][];
        }
        BiomeType[] resultYZ = resultY[z];
        if (resultYZ == null) {
            resultY[z] = resultYZ = new BiomeType[width];
        }
        resultYZ[x] = id;
    }

    @Override
    public boolean setBlock(int x, int y, int z, final @NonNull BaseBlock id) {
        this.storeCache(x, y, z, id.toImmutableState());
        return true;
    }

    @Override
    public @Nullable BlockState getBlock(int x, int y, int z) {
        BlockState[][] blocksY = result[getYIndex(y)];
        if (blocksY != null) {
            BlockState[] blocksYZ = blocksY[z];
            if (blocksYZ != null) {
                return blocksYZ[x];
            }
        }
        return null;
    }

    @Override
    public @Nullable World getWorld() {
        return weWorld;
    }

    @Override
    public @NonNull Location getMax() {
        return Location.at(getWorld().getName(), top.getX(), top.getY(), top.getZ());
    }

    @Override
    public @NonNull Location getMin() {
        return Location.at(getWorld().getName(), bot.getX(), bot.getY(), bot.getZ());
    }

    private int getYIndex(int y) {
        return y - weWorld.getMinY();
    }

}
