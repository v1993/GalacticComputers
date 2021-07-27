package com.v1993.galacticcomputers.gccore;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.prefab.DriverSidedTileEntity;

import com.v1993.galacticcomputers.utils.NamedManagedEnvironment;

import micdoodle8.mods.galacticraft.core.tile.TileEntityCargoLoader;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DriverCargoLoader extends DriverSidedTileEntity {
	@Override
	public Class<?> getTileEntityClass() {
		return TileEntityCargoLoader.class;
	}

	public static class InternalManagedEnvironment extends NamedManagedEnvironment<TileEntityCargoLoader> {
		public InternalManagedEnvironment(TileEntityCargoLoader tileEntity) {
			super(tileEntity, "cargo_loader");
		}

		@Callback(doc = "function(): boolean, string -- Get last operation result as success and exact status (see docs for possible results)")
		public Object[] getStatus(final Context context, final Arguments args) {
			if (tileEntity.outOfItems) {
				return new Object[] { false, "OUT_OF_ITEMS" };
			} else if (tileEntity.noTarget) {
				return new Object[] { false, "TARGET_NOT_FOUND" };
			} else if (tileEntity.targetFull) {
				return new Object[] { false, "TARET_FULL" };
			} else if (tileEntity.targetNoInventory) {
				return new Object[] { false, "TARGET_LACKS_INVENTORY" };
			}

			return new Object[] { true, "SUCCESS" };
		}

		@Callback(doc = "function(): bool -- Is cargo loader enabled")
		public Object[] isEnabled(final Context context, final Arguments args) {
			return new Object[] { !tileEntity.disabled };
		}

		@Callback(doc = "function(bool: enable) -- Turn cargo loader on/off")
		public Object[] setEnabled(final Context context, final Arguments args) {
			tileEntity.disabled = !args.checkBoolean(0);
			return new Object[] {};
		}
	}

	@Override
	public ManagedEnvironment createEnvironment(World world, BlockPos pos, EnumFacing facing) {
		TileEntityCargoLoader tile = (TileEntityCargoLoader) world.getTileEntity(pos);
		return new InternalManagedEnvironment(tile);
	}
}
