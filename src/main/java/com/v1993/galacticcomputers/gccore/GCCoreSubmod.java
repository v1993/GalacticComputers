package com.v1993.galacticcomputers.gccore;

import li.cil.oc.api.Driver;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import com.v1993.galacticcomputers.SubMod;

public class GCCoreSubmod extends SubMod {
	@Override
	public void init(FMLInitializationEvent event) {
		// Converters
		Driver.add(new ConverterIItemElectricBase());
		/*
		 * Have only one useful method and it's broken Driver.add(new
		 * ConverterIItemOxygenSupply());
		 */
		Driver.add(new ConverterIItemThermal());

		// Abstract block drivers
		Driver.add(new DriverEnergyStorage());
		Driver.add(new DriverOxygenMachine());

		// Specific machine drivers
		// Oxygen
		Driver.add(new DriverBubbleDistributor());
		Driver.add(new DriverOxygenCollector());
		Driver.add(new DriverOxygenSealer());

		// Fuel and cargo
		Driver.add(new DriverRefinery());
		Driver.add(new DriverFuelLoader());
		Driver.add(new DriverCargoLoader());
		Driver.add(new DriverCargoUnloader());

		// Electricity
		Driver.add(new DriverSolarPanel());
		Driver.add(new DriverCoalGenerator());

		// Other stuff
		Driver.add(new DriverAirLockController());
		Driver.add(new DriverTelemetryUnit());
		Driver.add(new DriverEmergencyBox());
	}
}
