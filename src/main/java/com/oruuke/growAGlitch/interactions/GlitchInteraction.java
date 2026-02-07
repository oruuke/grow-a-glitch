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
import com.hypixel.hytale.server.core.util.Config;
import com.hypixel.hytale.server.npc.NPCPlugin;
import com.hypixel.hytale.server.npc.entities.NPCEntity;
import com.oruuke.growAGlitch.GlitchConfig;
import com.oruuke.growAGlitch.GrowAGlitch;
import it.unimi.dsi.fastutil.Pair;

import javax.annotation.Nonnull;
import java.util.Objects;
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

//        Get all available entities via config registry
        Config<GlitchConfig> config = GrowAGlitch.getConfigRegistry();
        assert config != null;
        GlitchConfig glitchConfig = config.get();
        String[] randomEntities = glitchConfig.getRandomEntities();

//        Required random entities config for glitch
        if (randomEntities.length < 1) {
            interactionContext.getState().state = InteractionState.Failed;
            LOGGER.atInfo().log("No entities found for glitch interaction");
            return;
        }

//        Random asset picker
        Random randomPick = new Random();
        int i = randomPick.nextInt(randomEntities.length);
        String newEntityId = randomEntities[i];

//        Target data for replacing
        Ref<EntityStore> targetRef = interactionContext.getTargetEntity();
        assert targetRef != null;
        TransformComponent transform = store.getComponent(targetRef, EntityModule.get().getTransformComponentType());
        assert transform != null;

        world.execute(() -> {
//            Spawn new entity
            Pair<Ref<EntityStore>, INonPlayerCharacter> result = NPCPlugin.get().spawnNPC(store, newEntityId, null,
                    transform.getPosition(), transform.getRotation());
            if (result != null) {
//                Double check new entity exists
                Ref<EntityStore> npcRef = result.first();
                INonPlayerCharacter npcInterface = result.second();
                NPCEntity npcComponent = store.getComponent(npcRef, Objects.requireNonNull(NPCEntity.getComponentType()));
                if (npcInterface != null && npcComponent != null) {
                    store.removeEntity(targetRef, RemoveReason.REMOVE);
                    LOGGER.atInfo().log("Swapped: " + targetRef.getClass().getName() + " ...for: " + newEntityId);
                }
            }
        });
    }
}
