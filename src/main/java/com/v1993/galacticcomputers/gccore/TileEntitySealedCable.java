package com.v1993.galacticcomputers.gccore;

import li.cil.oc.api.Network;
import li.cil.oc.api.network.Analyzable;
import li.cil.oc.api.network.Node;
import li.cil.oc.api.network.Visibility;
import li.cil.oc.api.prefab.TileEntityEnvironment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;

public class TileEntitySealedCable extends TileEntityEnvironment implements Analyzable {
	@Override
	public Node[] onAnalyze(EntityPlayer arg0, EnumFacing arg1, float arg2, float arg3, float arg4) {
		return null;
	}
	
	TileEntitySealedCable() {
		node = Network.newNode(this, Visibility.None).create();
	}
}
