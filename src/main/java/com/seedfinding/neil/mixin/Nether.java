package com.seedfinding.neil.mixin;

import com.mojang.datafixers.util.Pair;
import kaptainwutax.biomeutils.biome.BiomePoint;
import kaptainwutax.biomeutils.biome.Biomes;
import kaptainwutax.biomeutils.source.BiomeSource;
import kaptainwutax.mcutils.state.Dimension;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import static com.seedfinding.neil.ModifyBiomes.MC_VERSION;

@Mixin(MultiNoiseBiomeSource.class)
public class Nether extends BiomeSourceAccessor {
	@Shadow
	@Final
	private List<Pair<BiomePoint, Supplier<Biome>>> biomePoints;
	public BiomeSource biomeSource;
	private static final BiomePoint[] DEFAULT_BIOME_POINTS = {
			new BiomePoint(kaptainwutax.biomeutils.biome.Biomes.NETHER_WASTES, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F),
			new BiomePoint(kaptainwutax.biomeutils.biome.Biomes.SOUL_SAND_VALLEY, 0.0F, -0.5F, 0.0F, 0.0F, 0.0F),
			new BiomePoint(kaptainwutax.biomeutils.biome.Biomes.CRIMSON_FOREST, 0.4F, 0.0F, 0.0F, 0.0F, 0.0F),
			new BiomePoint(kaptainwutax.biomeutils.biome.Biomes.WARPED_FOREST, 0.0F, 0.5F, 0.0F, 0.0F, 0.375F),
			new BiomePoint(kaptainwutax.biomeutils.biome.Biomes.BASALT_DELTAS, -0.5F, 0.0F, 0.0F, 0.0F, 0.175F)
	};
	public final Map<kaptainwutax.biomeutils.biome.Biome, Supplier<Biome>> biomeRegistry = new HashMap<>();

	@Inject(method = "<init>(JLjava/util/List;Lnet/minecraft/world/biome/source/MultiNoiseBiomeSource$NoiseParameters;Lnet/minecraft/world/biome/source/MultiNoiseBiomeSource$NoiseParameters;Lnet/minecraft/world/biome/source/MultiNoiseBiomeSource$NoiseParameters;Lnet/minecraft/world/biome/source/MultiNoiseBiomeSource$NoiseParameters;Ljava/util/Optional;)V", at = @At("RETURN"))
	private void MultiNoiseBiomeSource(long seed, List<Pair<Biome.MixedNoisePoint, Supplier<Biome>>> biomePoints, MultiNoiseBiomeSource.NoiseParameters temperatureNoiseParameters, MultiNoiseBiomeSource.NoiseParameters humidityNoiseParameters, MultiNoiseBiomeSource.NoiseParameters altitudeNoiseParameters, MultiNoiseBiomeSource.NoiseParameters weirdnessNoiseParameters, Optional<Pair<Registry<Biome>, MultiNoiseBiomeSource.Preset>> instance, CallbackInfo ci) throws Exception {
		biomeSource = BiomeSource.of(Dimension.NETHER, MC_VERSION, seed);
		for (Pair<BiomePoint, Supplier<Biome>> pair : this.biomePoints) {
			float small = Float.MAX_VALUE;
			BiomePoint current = null;
			for (BiomePoint mixedNoisePoint : DEFAULT_BIOME_POINTS) {
				float d = pair.getFirst().distanceTo(
						new BiomePoint(
								Biomes.THE_VOID,
								mixedNoisePoint.temperature,
								mixedNoisePoint.humidity,
								mixedNoisePoint.altitude,
								mixedNoisePoint.weirdness,
								mixedNoisePoint.weight
						));
				if (d < small) {
					small = d;
					current = mixedNoisePoint;
				}
			}
			if (small != 0F) {
				System.out.println("WARNING not found " + pair.getFirst().toString() + " in biomeutils");
			}
			if (current == null) {
				throw new Exception("Impossible");
			}

			biomeRegistry.put(current.biome, pair.getSecond());
		}
	}


	@Inject(method = "Lnet/minecraft/world/biome/source/MultiNoiseBiomeSource;getBiomeForNoiseGen(III)Lnet/minecraft/world/biome/Biome;", at = @At("HEAD"), cancellable = true)
	public void getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ, CallbackInfoReturnable<Biome> cir) {
		cir.cancel(); // get hold over the method
		kaptainwutax.biomeutils.biome.Biome biome = biomeSource.getBiomeForNoiseGen(biomeX, biomeY, biomeZ);
		cir.setReturnValue(biomeRegistry.get(biome).get());

	}
// at=@At("RETURN")
	// Biome orig= cir.getReturnValue();
	//  Biome biomeMC = BuiltinBiomes.THE_VOID;
	//        if (kaptainwutax.biomeutils.Biome.NETHER_WASTES.equals(biome)) {
	//            biomeMC = BuiltinRegistries.BIOME.getOrThrow(BiomeKeys.NETHER_WASTES);
	//        } else if (kaptainwutax.biomeutils.Biome.CRIMSON_FOREST.equals(biome)) {
	//            biomeMC = BuiltinRegistries.BIOME.getOrThrow(BiomeKeys.CRIMSON_FOREST);
	//        } else if (kaptainwutax.biomeutils.Biome.WARPED_FOREST.equals(biome)) {
	//            biomeMC = BuiltinRegistries.BIOME.getOrThrow(BiomeKeys.WARPED_FOREST);
	//        } else if (kaptainwutax.biomeutils.Biome.BASALT_DELTAS.equals(biome)) {
	//            biomeMC = BuiltinRegistries.BIOME.getOrThrow(BiomeKeys.BASALT_DELTAS);
	//        } else if (kaptainwutax.biomeutils.Biome.SOUL_SAND_VALLEY.equals(biome)) {
	//            biomeMC = BuiltinRegistries.BIOME.getOrThrow(BiomeKeys.SOUL_SAND_VALLEY);
	//        }else{
	//            System.out.println("WARNING "+biome.getName());
	//        }
	//        if (orig!=biomeMC){
	//            System.out.println("DIFFERENT "+biome.getName()+" "+biomeMC+" "+orig.toString());
	//        }

//        Biome biomeMC=null;
//        for (Map.Entry<RegistryKey<Biome>,Biome> entry: BuiltinRegistries.BIOME.getEntries()){
//            String name=entry.getKey().getValue().getPath();
//            if (name.equals(biome.getName())){
//                if (biomeMC!=null){
//                    System.out.println("WARNING DOUBLE ENTRY");
//                }
//                System.out.println("Found "+biome.getName());
//                biomeMC=entry.getValue();
//            }
//        }
//
//        if (biomeMC ==null){
//            throw new Exception("NOT POSSIBLE");
//        }
//        cir.setReturnValue(biomeMC);

//      FIXME this is much more efficient but doesnt work
//        cir.setReturnValue((Biome)BuiltinRegistries.BIOME.get(new Identifier(biome.getName())));
}
