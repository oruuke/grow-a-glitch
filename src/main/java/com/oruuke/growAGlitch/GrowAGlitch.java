package com.oruuke.growAGlitch;

import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.Interaction;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.oruuke.growAGlitch.events.JoinEvent;
import com.oruuke.growAGlitch.interactions.GlitchInteraction;

public class GrowAGlitch extends JavaPlugin {
    public GrowAGlitch(JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
//        Register custom interaction with unique ID
        this.getEventRegistry().registerGlobal(PlayerReadyEvent.class, JoinEvent::onPlayerReady);
        this.getCodecRegistry(Interaction.CODEC).register("Glitch_Interaction", GlitchInteraction.class, GlitchInteraction.CODEC);
    }
}
