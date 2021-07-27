package com.v1993.galacticcomputers.gccore;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.prefab.DriverSidedTileEntity;

import com.v1993.galacticcomputers.utils.NamedManagedEnvironment;

import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenCollector;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DriverOxygenCollector extends DriverSidedTileEntity {
	@Override
	public Class<?> getTileEntityClass() {
		return TileEntityOxygenCollector.class;
	}

	public static class InternalManagedEnvironment extends NamedManagedEnvironment<TileEntityOxygenCollector> {
		public InternalManagedEnvironment(TileEntityOxygenCollector tileEntity) {
			super(tileEntity, "oxygen_collector");
		}

		@Callback(doc = "function(): number -- Get oxygen being collected per second")
		public Object[] getCollectionSpeed(final Context context, final Arguments args) {
			return new Object[] { tileEntity.lastOxygenCollected * 20 };
		}
	}

	@Override
	public ManagedEnvironment createEnvironment(World world, BlockPos pos, EnumFacing facing) {
		TileEntityOxygenCollector tile = (TileEntityOxygenCollector) world.getTileEntity(pos);
		return new InternalManagedEnvironment(tile);
	}
}
