package com.v1993.galacticcomputers.gccore;

import li.cil.oc.api.Driver;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

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

	@GameRegistry.ObjectHolder("galacticcomputers:sealed_cable")
	public static BlockSealedCable blockSealedCable;

	@Override
	public void registerBlocks(RegistryEvent.Register<Block> event) {
		event.getRegistry().register(new BlockSealedCable());
		GameRegistry.registerTileEntity(TileEntitySealedCable.class,
				new ResourceLocation("galacticcomputers:sealed_cable_tile"));
	};

	@Override
	public void registerItems(RegistryEvent.Register<Item> event) {
		event.getRegistry()
				.register(new ItemBlock(blockSealedCable).setRegistryName(blockSealedCable.getRegistryName()));
	};

	@Override
	public void registerModels(ModelRegistryEvent event) {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(blockSealedCable), 0,
				new ModelResourceLocation("galacticcomputers:sealed_cable", "inventory"));
	}
}
