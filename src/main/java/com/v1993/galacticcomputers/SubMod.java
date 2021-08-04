package com.v1993.galacticcomputers;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

public class SubMod {
	// Common
	public void preInit(FMLPreInitializationEvent event) {

	}

	public void init(FMLInitializationEvent event) {
		// This is where you add drivers
	}

	public void postInit(FMLPostInitializationEvent event) {

	}

	public void registerBlocks(RegistryEvent.Register<Block> event) {

	}

	public void registerItems(RegistryEvent.Register<Item> event) {

	}

	@SideOnly(Side.CLIENT)
	public void registerModels(ModelRegistryEvent event) {

	}
}
