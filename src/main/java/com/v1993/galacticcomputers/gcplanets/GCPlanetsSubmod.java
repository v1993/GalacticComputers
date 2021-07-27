package com.v1993.galacticcomputers.gcplanets;

import li.cil.oc.api.Driver;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import com.v1993.galacticcomputers.SubMod;

public class GCPlanetsSubmod extends SubMod {
	@Override
	public void init(FMLInitializationEvent event) {
		// Specific machine drivers
		// Mars-tier machines
		Driver.add(new DriverLaunchController());
		/*
		 * There is only one method and it's broken:
		 * https://github.com/TeamGalacticraft/Galacticraft-Legacy/issues/31
		 * Driver.add(new DriverCryogenicChamber());
		 */
		Driver.add(new DriverTerraformer());
		Driver.add(new DriverGasLiquefier());
		Driver.add(new DriverMethaneSynthesizer());
		Driver.add(new DriverWaterElectrolyzer());

		// Asteroids-tier machines
		Driver.add(new DriverTelepad());
		// Add astro miner here if someone asks for it

		// Venus-tier machines
		Driver.add(new DriverGeothermalGenerator());
		Driver.add(new DriverSolarArray());
	}
}
