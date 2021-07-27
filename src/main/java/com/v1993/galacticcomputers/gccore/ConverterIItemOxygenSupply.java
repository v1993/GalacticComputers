package com.v1993.galacticcomputers.gccore;

import com.v1993.galacticcomputers.utils.ConverterItem;
import micdoodle8.mods.galacticraft.api.item.IItemOxygenSupply;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Map;

public class ConverterIItemOxygenSupply extends ConverterItem {

	@Override
	protected void convertItem(final Item item, final ItemStack stack, final Map<Object, Object> output) {
		if (item instanceof IItemOxygenSupply) {
			output.put("oxygenGasStored", ((IItemOxygenSupply) item).getOxygenStored(stack));
		}
	}

}
