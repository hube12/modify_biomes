package com.seedfinding.neil.mixin;

import com.mojang.datafixers.util.Pair;
import kaptainwutax.biomeutils.source.BiomeSource;
import kaptainwutax.seedutils.mc.Dimension;
import kaptainwutax.seedutils.mc.MCVersion;
import net.fabricmc.fabric.impl.biome.InternalBiomeData;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.BuiltinBiomes;
import net.minecraft.world.biome.source.MultiNoiseBiomeSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static com.seedfinding.neil.ModifyBiomes.MC_VERSION;

@Mixin(MultiNoiseBiomeSource.class)
public class Nether {
    public BiomeSource biomeSource;


    @Inject(method = "<init>(JLjava/util/List;Lnet/minecraft/world/biome/source/MultiNoiseBiomeSource$NoiseParameters;Lnet/minecraft/world/biome/source/MultiNoiseBiomeSource$NoiseParameters;Lnet/minecraft/world/biome/source/MultiNoiseBiomeSource$NoiseParameters;Lnet/minecraft/world/biome/source/MultiNoiseBiomeSource$NoiseParameters;Ljava/util/Optional;)V", at = @At("RETURN"))
    private void MultiNoiseBiomeSource(long seed, List<Pair<Biome.MixedNoisePoint, Supplier<Biome>>> biomePoints, MultiNoiseBiomeSource.NoiseParameters temperatureNoiseParameters, MultiNoiseBiomeSource.NoiseParameters humidityNoiseParameters, MultiNoiseBiomeSource.NoiseParameters altitudeNoiseParameters, MultiNoiseBiomeSource.NoiseParameters weirdnessNoiseParameters, Optional<Pair<Registry<Biome>, MultiNoiseBiomeSource.Preset>> instance, CallbackInfo ci) {
        biomeSource = BiomeSource.of(Dimension.NETHER,MC_VERSION, seed);
    }

    @Inject(method = "Lnet/minecraft/world/biome/source/MultiNoiseBiomeSource;getBiomeForNoiseGen(III)Lnet/minecraft/world/biome/Biome;",at=@At("HEAD"), cancellable = true)
    public void getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ, CallbackInfoReturnable<Biome> cir) {
        cir.cancel(); // get hold over the method
        kaptainwutax.biomeutils.Biome biome=biomeSource.getBiomeForNoiseGen(biomeX,biomeY,biomeZ);
        cir.setReturnValue(BuiltinRegistries.BIOME.get(biome.getId()));
    }

}
