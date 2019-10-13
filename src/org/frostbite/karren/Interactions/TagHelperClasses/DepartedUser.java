/*
 * Copyright (c) 2019 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.Interactions.TagHelperClasses;

public class DepartedUser {
    public long userID;
    public boolean isDeparted;

    public DepartedUser(long userID, boolean isDeparted) {
        this.userID = userID;
        this.isDeparted = isDeparted;
    }
}
