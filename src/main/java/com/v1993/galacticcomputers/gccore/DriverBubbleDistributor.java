package com.v1993.galacticcomputers.gccore;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.prefab.DriverSidedTileEntity;

import com.v1993.galacticcomputers.utils.NamedManagedEnvironment;

import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenDistributor;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DriverBubbleDistributor extends DriverSidedTileEntity {
	@Override
	public Class<?> getTileEntityClass() {
		return TileEntityOxygenDistributor.class;
	}

	public static class InternalManagedEnvironment extends NamedManagedEnvironment<TileEntityOxygenDistributor> {
		public InternalManagedEnvironment(TileEntityOxygenDistributor tileEntity) {
			super(tileEntity, "bubble_distributor");
		}

		@Callback(doc = "function(): bool -- Is bubble visible")
		public Object[] isBubbleVisible(final Context context, final Arguments args) {
			return new Object[] { tileEntity.getBubbleVisible() };
		}

		@Callback(doc = "function(bool: visibility) -- Set bubble visibility")
		public Object[] setBubbleVisible(final Context context, final Arguments args) {
			tileEntity.setBubbleVisible(args.checkBoolean(0));
			return new Object[] {};
		}

		@Callback(doc = "function(): number -- Get bubble radius")
		public Object[] getBubbleSize(final Context context, final Arguments args) {
			return new Object[] { tileEntity.getBubbleSize() };
		}
	}

	@Override
	public boolean worksWith(final World world, final BlockPos pos, final EnumFacing side) {
		if (super.worksWith(world, pos, side)) {
			TileEntityOxygenDistributor tile = (TileEntityOxygenDistributor) world.getTileEntity(pos);
			return tile.getMaxEnergyStoredGC() != 0;
		}
		return false;
	}

	@Override
	public ManagedEnvironment createEnvironment(World world, BlockPos pos, EnumFacing facing) {
		TileEntityOxygenDistributor tile = (TileEntityOxygenDistributor) world.getTileEntity(pos);
		return new InternalManagedEnvironment(tile);
	}
}
