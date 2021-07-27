package com.v1993.galacticcomputers.utils;

import li.cil.oc.api.Network;
import li.cil.oc.api.network.Visibility;
import li.cil.oc.api.prefab.AbstractManagedEnvironment;
import li.cil.oc.api.driver.NamedBlock;

public abstract class NamedManagedEnvironment<T> extends AbstractManagedEnvironment implements NamedBlock {
	protected final T tileEntity;
	protected final String name;

	public NamedManagedEnvironment(final T tileEntity, final String name) {
		this.tileEntity = tileEntity;
		this.name = name;
		setNode(Network.newNode(this, Visibility.Network).withComponent(name).create());
	}

	@Override
	public String preferredName() {
		return name;
	}

	@Override
	public int priority() {
		return ComponentPriority.SPECIFIC_MACHINE;
	}

}
