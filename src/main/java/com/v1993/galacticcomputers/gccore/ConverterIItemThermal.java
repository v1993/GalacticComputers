package com.v1993.galacticcomputers.gccore;

import com.v1993.galacticcomputers.utils.ConverterItem;
import micdoodle8.mods.galacticraft.api.item.IItemThermal;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Map;

public class ConverterIItemThermal extends ConverterItem {

	@Override
	protected void convertItem(final Item item, final ItemStack stack, final Map<Object, Object> output) {
		if (item instanceof IItemThermal) {
			final IItemThermal tItem = (IItemThermal) item;
			output.put("thermalTier", tItem.getThermalStrength());
		}
	}

}
