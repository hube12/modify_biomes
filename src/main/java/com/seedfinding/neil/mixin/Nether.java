package net.fabricmc.example.mixin;

import com.mojang.datafixers.util.Pair;
import kaptainwutax.biomeutils.source.BiomeSource;
import kaptainwutax.seedutils.mc.Dimension;
import kaptainwutax.seedutils.mc.MCVersion;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.MultiNoiseBiomeSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Mixin(MultiNoiseBiomeSource.class)
public class Nether {
    @Shadow
    @Final
    private Registry<Biome> biomeRegistry;
    public BiomeSource biomeSource;

    public NetherParametersAccessor noise;


    @Inject(method = "<init>(JLjava/util/List;Lnet/minecraft/world/biome/source/MultiNoiseBiomeSource$NoiseParameters;Lnet/minecraft/world/biome/source/MultiNoiseBiomeSource$NoiseParameters;Lnet/minecraft/world/biome/source/MultiNoiseBiomeSource$NoiseParameters;Lnet/minecraft/world/biome/source/MultiNoiseBiomeSource$NoiseParameters;Ljava/util/Optional;)V", at = @At("RETURN"))
    private void MultiNoiseBiomeSource(long seed, List<Pair<Biome.MixedNoisePoint, Supplier<Biome>>> biomePoints, NetherParametersAccessor temperatureNoiseParameters, NetherParametersAccessor humidityNoiseParameters, MultiNoiseBiomeSource.NoiseParameters altitudeNoiseParameters, MultiNoiseBiomeSource.NoiseParameters weirdnessNoiseParameters, Optional<Pair<Registry<Biome>, MultiNoiseBiomeSource.Preset>> instance, CallbackInfo ci) {
        biomeSource = BiomeSource.of(Dimension.NETHER, MCVersion.v1_16, seed);
    }

    @Inject(method = "getBiomeForNoiseGen(III)Lnet/minecraft/world/biome/Biome;", at = @At("HEAD"), cancellable = true)
    public void getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ, CallbackInfoReturnable<Biome> cir) {
        cir.cancel(); // remove the old method
        kaptainwutax.biomeutils.Biome biome = biomeSource.getBiomeForNoiseGen(biomeX, biomeY, biomeZ);
        int id = biome.getId();
        Biome biomeMC = this.biomeRegistry.get(id);
        cir.setReturnValue(biomeMC);
    }
}
