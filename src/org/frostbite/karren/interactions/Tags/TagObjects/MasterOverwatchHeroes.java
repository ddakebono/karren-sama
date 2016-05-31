/*
 * Copyright (c) 2016 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.interactions.Tags.TagObjects;

public enum MasterOverwatchHeroes{
    BASTION (15),
    DVA (22),
    GENJI (21),
    HANZO (16),
    JUNKRAT (2),
    LUCIO (3),
    MCCREE (6),
    MEI (20),
    MERCY (17),
    PHARAH (11),
    REAPER (8),
    REINHARDT (12),
    ROADHOG (1),
    SOLDIER76 (4),
    SYMMETRA (13),
    TORBJORN (14),
    TRACER (7),
    WIDOWMAKER (9),
    WINSTON (10),
    ZARYA (5),
    ZENYATTA (18);

    private final int heroNumber;

    MasterOverwatchHeroes(int heroNumber) {
        this.heroNumber = heroNumber;
    }

    public int getHeroNumber() {
        return heroNumber;
    }
}
