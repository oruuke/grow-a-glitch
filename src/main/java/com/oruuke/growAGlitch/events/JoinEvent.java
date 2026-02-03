package com.oruuke.growAGlitch.events;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.inventory.Inventory;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.inventory.container.ItemContainer;

import java.awt.*;

public class JoinEvent {
    public static void onPlayerReady(PlayerReadyEvent event) {
        Player player = event.getPlayer();
        player.sendMessage(Message.raw("Welcome " + player.getDisplayName() + ", to the demo edition of Grow A Glitch! I snuck a few things in your pocket to get you going...").color(Color.pink).bold(true));

        Inventory inventory = player.getInventory();
        ItemStack hybiscus = new ItemStack("Hybiscus", 13);
        ItemStack sword = new ItemStack("Weapon_Sword_Adamantite", 1);
        ItemStack bench = new ItemStack("Bench_Farming", 1);
        ItemContainer itemContainer = inventory.getHotbar();
        itemContainer.addItemStack(hybiscus);
        itemContainer.addItemStack(sword);
        itemContainer.addItemStack(bench);
    }
}
