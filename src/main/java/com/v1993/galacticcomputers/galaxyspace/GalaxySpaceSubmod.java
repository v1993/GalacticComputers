package com.v1993.galacticcomputers.galaxyspace;

import li.cil.oc.api.Driver;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import com.v1993.galacticcomputers.SubMod;

public class GalaxySpaceSubmod extends SubMod {
	@Override
	public void init(FMLInitializationEvent event) {
		Driver.add(new DriverFuelGenerator());

		Driver.add(new DriverPlanetaryShield());
		Driver.add(new DriverWindTurbine());
		Driver.add(new DriverHybridSolarPanel());
		Driver.add(new DriverHybridSolarPanel());
		Driver.add(new DriverGravityModule());
		Driver.add(new DriverMagneticFieldGenerator());
		Driver.add(new DriverPanelsController());
		Driver.add(new DriverSolarWindPanel());
	}
}
