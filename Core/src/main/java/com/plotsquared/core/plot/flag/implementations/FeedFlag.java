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
package com.plotsquared.core.plot.flag.implementations;

import com.plotsquared.core.configuration.caption.TranslatableCaption;
import com.plotsquared.core.plot.flag.FlagParseException;
import com.plotsquared.core.plot.flag.types.TimedFlag;
import net.kyori.adventure.text.minimessage.Template;
import org.checkerframework.checker.nullness.qual.NonNull;

public class FeedFlag extends TimedFlag<Integer, FeedFlag> {

    public static final FeedFlag FEED_NOTHING = new FeedFlag(new Timed<>(0, 0));

    public FeedFlag(@NonNull Timed<Integer> value) {
        super(value, 1, TranslatableCaption.of("flags.flag_description_feed"));
    }

    @Override
    protected Integer parseValue(String input) throws FlagParseException {
        int parsed;
        try {
            parsed = Integer.parseInt(input);
        } catch (Throwable throwable) {
            throw new FlagParseException(
                    this,
                    input,
                    TranslatableCaption.of("invalid.not_a_number"),
                    Template.of("value", input)
            );
        }
        if (parsed < 1) {
            throw new FlagParseException(
                    this,
                    input,
                    TranslatableCaption.of("invalid.number_not_positive"),
                    Template.of("value", String.valueOf(parsed))
            );
        }
        return parsed;
    }

    @Override
    protected Integer mergeValue(Integer other) {
        return this.getValue().getValue() + other;
    }

    @Override
    public String getExample() {
        return "10 5";
    }

    @Override
    protected FeedFlag flagOf(@NonNull Timed<Integer> value) {
        return new FeedFlag(value);
    }

}
