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
package com.plotsquared.core.location;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * PlotSquared representation of a platform world
 *
 * @param <T> Platform world type
 */
public interface World<T> {

    /**
     * Get a {@link NullWorld} implementation
     *
     * @param <T> implementation-specific world object type e.g. a bukkit World
     * @return NullWorld instance
     */
    static <T> NullWorld<T> nullWorld() {
        return new NullWorld<>();
    }

    /**
     * Get the platform world represented by this world
     *
     * @return Platform world
     */
    @NonNull T getPlatformWorld();

    /**
     * Get the name of the world
     *
     * @return World name
     */
    @NonNull String getName();

    /**
     * Get the min world height. Inclusive.
     *
     * @since TODO
     */
    int getMinHeight();


    /**
     * Get the max world height. Exclusive.
     *
     * @since TODO
     */
    int getMaxHeight();

    class NullWorld<T> implements World<T> {

        private NullWorld() {
        }

        @NonNull
        @Override
        public T getPlatformWorld() {
            throw new UnsupportedOperationException("Cannot get platform world from NullWorld");
        }

        @Override
        public @NonNull String getName() {
            return "";
        }

        @Override
        public int getMinHeight() {
            return 0;
        }

        @Override
        public int getMaxHeight() {
            return 0;
        }

        @Override
        public boolean equals(final Object obj) {
            return obj instanceof NullWorld;
        }

        @Override
        public int hashCode() {
            return "null".hashCode();
        }

    }

}
