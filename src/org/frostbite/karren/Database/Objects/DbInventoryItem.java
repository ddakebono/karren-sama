/*
 * Copyright (c) 2018 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.Database.Objects;

import java.sql.Timestamp;

public class DbInventoryItem {
    public int inventoryItemID;
    public DbItem itemID;
    public String userID;
    public Timestamp itemAcquired;

    public DbInventoryItem(){

    }

    public DbInventoryItem(int inventoryItemID, DbItem itemID, String userID, Timestamp itemAcquired) {
        this.inventoryItemID = inventoryItemID;
        this.itemID = itemID;
        this.userID = userID;
        this.itemAcquired = itemAcquired;
    }

    public DbItem getItemID() {
        return itemID;
    }

    public int getInventoryItemID() {
        return inventoryItemID;
    }

    public String getUserID() {
        return userID;
    }

    public Timestamp getItemAcquired() {
        return itemAcquired;
    }

    public void setItemAcquired(Timestamp itemAcquired) {
        this.itemAcquired = itemAcquired;
    }
}
