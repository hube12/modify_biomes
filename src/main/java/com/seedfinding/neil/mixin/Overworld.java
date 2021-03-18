package net.fabricmc.example.mixin;

import kaptainwutax.biomeutils.source.BiomeSource;
import kaptainwutax.seedutils.mc.Dimension;
import kaptainwutax.seedutils.mc.MCVersion;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.VanillaLayeredBiomeSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VanillaLayeredBiomeSource.class)
public class Overworld {
    @Shadow
    @Final
    private Registry<Biome> biomeRegistry;
    public BiomeSource biomeSource;

    @Inject(method = "<init>(JZZLnet/minecraft/util/registry/Registry;)V", at = @At("RETURN"))
    public void VanillaLayeredBiomeSource(long seed, boolean legacyBiomeInitLayer, boolean largeBiomes, Registry<Biome> biomeRegistry, CallbackInfo ci) {
        biomeSource = BiomeSource.of(Dimension.OVERWORLD, MCVersion.v1_16, seed);
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
