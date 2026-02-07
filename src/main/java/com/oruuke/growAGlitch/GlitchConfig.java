package com.oruuke.growAGlitch;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;

public class GlitchConfig {
    // Build config
    public static final BuilderCodec<GlitchConfig> CODEC = BuilderCodec.builder(
            GlitchConfig.class, GlitchConfig::new)
            .append(new KeyedCodec<String[]>("RandomEntities", Codec.STRING_ARRAY),
                    (config, value) -> config.randomEntities = value,
                    (config) -> config.randomEntities).add()
            .build();

    // Set default value
    private String[] randomEntities = RandomEntities.ENTITIES;

    // Constructor - empty?
    public GlitchConfig() {
    }

    // Getter and setter
    public String[] getRandomEntities() {
        return randomEntities;
    }
//    public void setRandomEntities(String[] randomEntities) {
//        this.randomEntities = randomEntities;
//    }
}
