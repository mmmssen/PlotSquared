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
package com.plotsquared.bukkit.generator;

import com.plotsquared.core.PlotSquared;
import com.plotsquared.core.generator.IndependentPlotGenerator;
import com.plotsquared.core.location.ChunkWrapper;
import com.plotsquared.core.plot.PlotArea;
import com.plotsquared.core.plot.world.PlotAreaManager;
import com.plotsquared.core.queue.QueueCoordinator;
import com.plotsquared.core.queue.ScopedQueueCoordinator;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Random;

final class BlockStatePopulator extends BlockPopulator {

    private final IndependentPlotGenerator plotGenerator;
    private final PlotAreaManager plotAreaManager;

    private QueueCoordinator queue;

    public BlockStatePopulator(
            final @NonNull IndependentPlotGenerator plotGenerator,
            final @NonNull PlotAreaManager plotAreaManager
    ) {
        this.plotGenerator = plotGenerator;
        this.plotAreaManager = plotAreaManager;
    }

    @Override
    public void populate(final @NonNull World world, final @NonNull Random random, final @NonNull Chunk source) {
        if (this.queue == null) {
            this.queue = PlotSquared.platform().globalBlockQueue().getNewQueue(new BukkitWorld(world));
        }
        final PlotArea area = this.plotAreaManager.getPlotArea(world.getName(), null);
        if (area == null) {
            return;
        }
        final ChunkWrapper wrap = new ChunkWrapper(area.getWorldName(), source.getX(), source.getZ());
        final ScopedQueueCoordinator chunk = this.queue.getForChunk(wrap.x, wrap.z,
                com.plotsquared.bukkit.util.BukkitWorld.getMinWorldHeight(world),
                com.plotsquared.bukkit.util.BukkitWorld.getMaxWorldHeight(world) - 1
        );
        if (this.plotGenerator.populateChunk(chunk, area)) {
            this.queue.enqueue();
        }
    }

}
