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

import org.frostbite.karren.Karren;
import org.knowm.yank.Yank;

public class DbItem {
    public int itemID;
    public String itemName;
    public String itemDesc;
    public int itemBaseValue;
    public String itemImage;
    public int itemBaseRarity;

    public DbItem() {
    }

    public DbItem(int itemID, String itemName, String itemDesc, int itemBaseValue, String itemImage, int itemBaseRarity) {
        this.itemID = itemID;
        this.itemName = itemName;
        this.itemDesc = itemDesc;
        this.itemBaseValue = itemBaseValue;
        this.itemImage = itemImage;
        this.itemBaseRarity = itemBaseRarity;
    }

    public int getItemID() {
        return itemID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public int getItemBaseValue() {
        return itemBaseValue;
    }

    public void setItemBaseValue(int itemBaseValue) {
        this.itemBaseValue = itemBaseValue;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public int getItemBaseRarity() {
        return itemBaseRarity;
    }

    public void setItemBaseRarity(int itemBaseRarity) {
        this.itemBaseRarity = itemBaseRarity;
    }

    public void update(){
        if(Karren.conf.getAllowSQLRW()) {
            String sql = "UPDATE Item SET ItemName=?, ItemDesc=?, ItemValue=?, ItemImage=?, ItemRarity=? WHERE ItemID=?";
            Yank.execute(sql, new Object[]{itemName, itemDesc, itemBaseValue, itemImage, itemBaseRarity, itemID});
        }
    }
}
