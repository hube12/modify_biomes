package com.seedfinding.neil.mixin;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.List;

@Mixin(BiomeSource.class)
public class BiomeSourceAccessor {
    @Shadow
    List<Biome> biomes;
}
