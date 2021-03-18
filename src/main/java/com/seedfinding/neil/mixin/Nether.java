package com.seedfinding.neil.mixin;

import com.mojang.datafixers.util.Pair;
import kaptainwutax.biomeutils.source.BiomeSource;
import kaptainwutax.seedutils.mc.Dimension;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
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
        biomeSource = BiomeSource.of(Dimension.NETHER, MC_VERSION, seed);
    }

    @Inject(method = "Lnet/minecraft/world/biome/source/MultiNoiseBiomeSource;getBiomeForNoiseGen(III)Lnet/minecraft/world/biome/Biome;", at = @At("HEAD"), cancellable = true)
    public void getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ, CallbackInfoReturnable<Biome> cir) throws Exception {
        cir.cancel(); // get hold over the method
        kaptainwutax.biomeutils.Biome biome = biomeSource.getBiomeForNoiseGen(biomeX, biomeY, biomeZ);

        Biome biomeMC = BuiltinBiomes.THE_VOID;
        if (kaptainwutax.biomeutils.Biome.NETHER_WASTES.equals(biome)) {
            biomeMC = BuiltinRegistries.BIOME.getOrThrow(BiomeKeys.NETHER_WASTES);
        } else if (kaptainwutax.biomeutils.Biome.CRIMSON_FOREST.equals(biome)) {
            biomeMC = BuiltinRegistries.BIOME.getOrThrow(BiomeKeys.CRIMSON_FOREST);
        } else if (kaptainwutax.biomeutils.Biome.WARPED_FOREST.equals(biome)) {
            biomeMC = BuiltinRegistries.BIOME.getOrThrow(BiomeKeys.WARPED_FOREST);
        } else if (kaptainwutax.biomeutils.Biome.BASALT_DELTAS.equals(biome)) {
            biomeMC = BuiltinRegistries.BIOME.getOrThrow(BiomeKeys.BASALT_DELTAS);
        } else if (kaptainwutax.biomeutils.Biome.SOUL_SAND_VALLEY.equals(biome)) {
            biomeMC = BuiltinRegistries.BIOME.getOrThrow(BiomeKeys.SOUL_SAND_VALLEY);
        }else{
            System.out.println("WARNING "+biome.getName());
        }
        cir.setReturnValue(biomeMC);


    }


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
