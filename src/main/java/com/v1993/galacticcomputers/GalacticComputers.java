package com.v1993.galacticcomputers;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.fml.common.Loader;

import java.util.List;
import java.util.ArrayList;

import com.v1993.galacticcomputers.gccore.GCCoreSubmod;

@Mod(modid = GalacticComputers.MODID, name = GalacticComputers.NAME, version = GalacticComputers.VERSION, dependencies = "required-after:opencomputers@[1.7,);required-after:galacticraftcore@[4.0.2,);after:galacticraftplanets@[4.0.2,);after:galaxyspace@[2.0.16,)")
public class GalacticComputers {
	public static final String MODID = "galacticcomputers";
	public static final String NAME = "Galactic Computers";
	public static final String VERSION = "0.1"; // FIXME: use build-time replacement

	private static Logger logger;

	private static List<SubMod> submods;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		submods = new ArrayList<SubMod>();

		logger.info("Loading GalacticComputers integrations");

		logger.info("Unconditionally loading GalactiCraft Core integration");
		submods.add(new GCCoreSubmod());

		try {
			if (Loader.isModLoaded("galacticraftcore")) {
				logger.info("Loading GalactiCraft Planets integration");
				submods.add((SubMod) Class.forName("com.v1993.galacticcomputers.gcplanets.GCPlanetsSubmod")
						.getDeclaredConstructor().newInstance());
			}
		} catch (Exception e) {
			logger.error("Failed to load GC Planets support!");
			logger.catching(e);
		}

		try {
			if (Loader.isModLoaded("galaxyspace")) {
				logger.info("Loading GalaxySpace integration");
				submods.add((SubMod) Class.forName("com.v1993.galacticcomputers.galaxyspace.GalaxySpaceSubmod")
						.getDeclaredConstructor().newInstance());
			}
		} catch (Exception e) {
			logger.error("Failed to load GalaxySpace support!");
			logger.catching(e);
		}

		for (SubMod smod : submods) {
			smod.preInit(event);
		}

	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		for (SubMod smod : submods) {
			smod.init(event);
		}
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		for (SubMod smod : submods) {
			smod.postInit(event);
		}
	}
}