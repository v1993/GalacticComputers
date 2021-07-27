package com.v1993.galacticcomputers.gccore;

import com.v1993.galacticcomputers.utils.ConverterItem;
import micdoodle8.mods.galacticraft.api.item.IItemElectricBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Map;

public class ConverterIItemElectricBase extends ConverterItem {
	@Override
	public void convertItem(final Item item, final ItemStack stack, final Map<Object, Object> output) {
		if (item instanceof IItemElectricBase) {
			final IItemElectricBase eItem = (IItemElectricBase) item;
			// Erase hard work done by IC2 integration
			// TODO: make into config option
			output.remove("canProvideEnergy");
			output.remove("charge");
			output.remove("maxCharge");
			output.remove("tier");
			output.remove("transferLimit");

			// We always can provide energy, it seems
			output.put("electricityStored", eItem.getElectricityStored(stack));
			output.put("maxElecticityStored", eItem.getMaxElectricityStored(stack));
			output.put("maxTransfter", eItem.getMaxTransferGC(stack));
			output.put("tierGC", eItem.getTierGC(stack));
		}
	}
}
