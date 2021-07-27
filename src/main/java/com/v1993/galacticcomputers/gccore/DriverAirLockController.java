package com.v1993.galacticcomputers.gccore;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.prefab.DriverSidedTileEntity;

import com.v1993.galacticcomputers.utils.NamedManagedEnvironment;

import micdoodle8.mods.galacticraft.core.tile.TileEntityAirLockController;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DriverAirLockController extends DriverSidedTileEntity {
	@Override
	public Class<?> getTileEntityClass() {
		return TileEntityAirLockController.class;
	}

	public static class InternalManagedEnvironment extends NamedManagedEnvironment<TileEntityAirLockController> {
		public InternalManagedEnvironment(TileEntityAirLockController tileEntity) {
			super(tileEntity, "airlock_controller");
		}

		@Callback(doc = "function(): boolean -- Is air lock open")
		public Object[] isOpen(final Context context, final Arguments args) {
			return new Object[] { !tileEntity.active };
		}
	}

	@Override
	public ManagedEnvironment createEnvironment(World world, BlockPos pos, EnumFacing facing) {
		TileEntityAirLockController tile = (TileEntityAirLockController) world.getTileEntity(pos);
		return new InternalManagedEnvironment(tile);
	}
}
