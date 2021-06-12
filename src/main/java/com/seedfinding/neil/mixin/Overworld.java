package com.seedfinding.neil.mixin;

import kaptainwutax.biomeutils.source.BiomeSource;
import kaptainwutax.mcutils.state.Dimension;
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

import static com.seedfinding.neil.ModifyBiomes.MC_VERSION;

@Mixin(VanillaLayeredBiomeSource.class)
public class Overworld {
	@Shadow
	@Final
	private Registry<Biome> biomeRegistry;
	public BiomeSource biomeSource;

	@Inject(method = "<init>(JZZLnet/minecraft/util/registry/Registry;)V", at = @At("RETURN"))
	public void VanillaLayeredBiomeSource(long seed, boolean legacyBiomeInitLayer, boolean largeBiomes, Registry<Biome> biomeRegistry, CallbackInfo ci) {
		biomeSource = BiomeSource.of(Dimension.OVERWORLD, MC_VERSION, seed);
	}

	@Inject(method = "getBiomeForNoiseGen(III)Lnet/minecraft/world/biome/Biome;", at = @At("HEAD"), cancellable = true)
	public void getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ, CallbackInfoReturnable<Biome> cir) {
		cir.cancel(); // remove the old method
		kaptainwutax.biomeutils.biome.Biome biome = biomeSource.getBiomeForNoiseGen(biomeX, biomeY, biomeZ);
		cir.setReturnValue(this.biomeRegistry.get(biome.getId()));
	}
}
