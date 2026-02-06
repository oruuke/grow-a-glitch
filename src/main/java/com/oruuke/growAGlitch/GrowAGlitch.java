package com.oruuke.growAGlitch;

import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.Interaction;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.util.Config;
import com.oruuke.growAGlitch.events.JoinEvent;
import com.oruuke.growAGlitch.interactions.GlitchInteraction;

public class GrowAGlitch extends JavaPlugin {
    // Static reference to plugin instance for registry access
    private static GrowAGlitch instance;

    // Config
    private final Config<GlitchConfig> config = this.withConfig("GlitchConfig", GlitchConfig.CODEC);

    // Constructor
    public GrowAGlitch(JavaPluginInit init) {
        super(init);
        // Register plugin instance for static access
        instance = this;
    }

    @Override
    protected void setup() {
        config.save();
//        Register custom interaction with unique ID
        this.getEventRegistry().registerGlobal(PlayerReadyEvent.class, JoinEvent::onPlayerReady);
        this.getCodecRegistry(Interaction.CODEC).register("Glitch_Interaction", GlitchInteraction.class, GlitchInteraction.CODEC);
    }

    // Static getter for config
    public static Config<GlitchConfig> getConfigRegistry() {
        return instance != null ? instance.config : null;
    }
}
