package com.v1993.galacticcomputers.galaxyspace;

import com.v1993.galacticcomputers.utils.ConverterItem;
import galaxyspace.api.item.IModificationItem;
import galaxyspace.core.prefab.items.modules.ItemModule;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class ConverterIModificationItem extends ConverterItem {
	@Override
	public void convertItem(final Item item, final ItemStack stack, final Map<Object, Object> output) {
		if (item instanceof IModificationItem) {
			final IModificationItem mItem = (IModificationItem) item;
			final List<String> modules = new ArrayList<>();
			final ItemModule[] availableModules = mItem.getAvailableModules();

			for (ItemModule module : availableModules) {
				if (mItem.hasModule(module, stack)) {
					modules.add(module.getName());
				}
			}

			output.put("modules", modules);
		}
	}
}
