package com.v1993.galacticcomputers.utils;

import java.util.Map;

import li.cil.oc.api.driver.Converter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public abstract class ConverterItem implements Converter {

	@Override
	final public void convert(final Object value, final Map<Object, Object> output) {
		if (value instanceof ItemStack) {
			final ItemStack stack = (ItemStack) value;
			final Item item = stack.getItem();
			convertItem(item, stack, output);
		}
	}

	abstract protected void convertItem(final Item item, final ItemStack stack, final Map<Object, Object> output);
}
