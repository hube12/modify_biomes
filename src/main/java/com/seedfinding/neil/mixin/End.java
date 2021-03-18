package com.seedfinding.neil.mixin;

import kaptainwutax.biomeutils.source.BiomeSource;
import kaptainwutax.seedutils.mc.Dimension;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.TheEndBiomeSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.seedfinding.neil.ModifyBiomes.MC_VERSION;

@Mixin(TheEndBiomeSource.class)
public class End {
    @Shadow @Final private Registry<Biome> biomeRegistry;
    public BiomeSource biomeSource;

    @Inject(method = "Lnet/minecraft/world/biome/source/TheEndBiomeSource;<init>(Lnet/minecraft/util/registry/Registry;JLnet/minecraft/world/biome/Biome;Lnet/minecraft/world/biome/Biome;Lnet/minecraft/world/biome/Biome;Lnet/minecraft/world/biome/Biome;Lnet/minecraft/world/biome/Biome;)V", at = @At("RETURN"))
    private void TheEndBiomeSource(Registry<Biome> biomeRegistry, long seed, Biome centerBiome, Biome highlandsBiome, Biome midlandsBiome, Biome smallIslandsBiome, Biome barrensBiome) {
        this.biomeSource=BiomeSource.of(Dimension.END,MC_VERSION,seed);
    }

    @Inject(method = "Lnet/minecraft/world/biome/source/TheEndBiomeSource;getBiomeForNoiseGen(III)Lnet/minecraft/world/biome/Biome;",at=@At("HEAD"), cancellable = true)
    public void getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ, CallbackInfoReturnable<Biome> cir) {
        cir.cancel();
        kaptainwutax.biomeutils.Biome biome=biomeSource.getBiomeForNoiseGen(biomeX,biomeY,biomeZ);
        cir.setReturnValue(this.biomeRegistry.get(biome.getId()));
    }
}