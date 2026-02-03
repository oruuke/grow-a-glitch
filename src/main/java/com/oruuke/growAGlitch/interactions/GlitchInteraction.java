package com.oruuke.growAGlitch.interactions;

import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.*;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.protocol.*;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.EntityModule;
import com.hypixel.hytale.server.core.modules.entity.component.*;
import com.hypixel.hytale.server.core.modules.interaction.interaction.CooldownHandler;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.SimpleInstantInteraction;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.npc.INonPlayerCharacter;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.NPCPlugin;
import it.unimi.dsi.fastutil.Pair;

import javax.annotation.Nonnull;
import java.util.Random;

public class GlitchInteraction extends SimpleInstantInteraction {
    public static final BuilderCodec<GlitchInteraction> CODEC = BuilderCodec.builder(
            GlitchInteraction.class, GlitchInteraction::new, SimpleInstantInteraction.CODEC
    ).build();

    public static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    @Override
    protected void firstRun(@Nonnull InteractionType interactionType, @Nonnull InteractionContext interactionContext, @Nonnull CooldownHandler cooldownHandler) {
//        Percent chance of occuring (less control over gameplay)
//        Moved to 100% on left swing only, for better skill expression
//        Random randomChance = new Random();
//        boolean isSuccessful = randomChance.nextInt(1,101) <= 50;
//        if (!isSuccessful) {
//            return;
//        }

        CommandBuffer<EntityStore> commandBuffer = interactionContext.getCommandBuffer();
//        Verify entity is alive and commands can be sent
        if (commandBuffer == null) {
            interactionContext.getState().state = InteractionState.Failed;
            LOGGER.atInfo().log("CommandBuffer is null");
            return;
        }

//        Example getter usage
        World world = commandBuffer.getExternalData().getWorld();
        Store<EntityStore> store = commandBuffer.getExternalData().getStore();

//        Required abstraction for interacting with entity
        Ref<EntityStore> playerRef = interactionContext.getEntity();
        Player player = commandBuffer.getComponent(playerRef, Player.getComponentType());
        if (player == null) {
            interactionContext.getState().state = InteractionState.Failed;
            LOGGER.atInfo().log("Player is null");
            return;
        }

//        Random asset picker
        Random randomPick = new Random();
        int i = randomPick.nextInt(LivingEntities.ENTITIES.size());
        String entity = LivingEntities.ENTITIES.get(i);

        Ref<EntityStore> targetRef = interactionContext.getTargetEntity();
        assert targetRef != null;
        TransformComponent transform = store.getComponent(targetRef, EntityModule.get().getTransformComponentType());
        assert transform != null;

        world.execute(() -> {
            // Use the NPCPlugin helper to spawn the NPC.
            Pair<Ref<EntityStore>, INonPlayerCharacter> result = NPCPlugin.get().spawnNPC(store, entity, null,
                    transform.getPosition(), transform.getRotation());
            if (result != null) {
                // Successfully spawned
                Ref<EntityStore> npcRef = result.first();
                // Retrieve the NPC interface if needed for further interaction
                INonPlayerCharacter npc = result.second();
                if (npcRef != null && npc != null) {
                    store.removeEntity(targetRef, RemoveReason.REMOVE);
                }
            }
        });
    }
}
