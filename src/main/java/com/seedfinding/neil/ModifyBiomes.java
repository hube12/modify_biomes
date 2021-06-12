package com.seedfinding.neil;

import kaptainwutax.mcutils.version.MCVersion;
import net.fabricmc.api.ModInitializer;

public class ModifyBiomes implements ModInitializer {
	public final static MCVersion MC_VERSION = MCVersion.v1_16;

	@Override
	public void onInitialize() {
		System.out.println("You have switched to an efficient biome source, welcome to the future");
	}
}
