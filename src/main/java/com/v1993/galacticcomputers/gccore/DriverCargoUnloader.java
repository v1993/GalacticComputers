package com.v1993.galacticcomputers.gccore;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.prefab.DriverSidedTileEntity;

import com.v1993.galacticcomputers.utils.NamedManagedEnvironment;

import micdoodle8.mods.galacticraft.core.tile.TileEntityCargoUnloader;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DriverCargoUnloader extends DriverSidedTileEntity {
	@Override
	public Class<?> getTileEntityClass() {
		return TileEntityCargoUnloader.class;
	}

	public static class InternalManagedEnvironment extends NamedManagedEnvironment<TileEntityCargoUnloader> {
		public InternalManagedEnvironment(TileEntityCargoUnloader tileEntity) {
			super(tileEntity, "cargo_unloader");
		}

		@Callback(doc = "function(): boolean, string -- Get last operation result as success and exact status (see docs for possible results and gotchas)")
		public Object[] getStatus(final Context context, final Arguments args) {
			// Gotcha: there is no report for unloader's inventory overflow
			// Also, "TARGET_LACKS_INVENTORY" is not actually reported
			// TL;DR: unloader is a buggy mess
			if (tileEntity.noTarget) {
				return new Object[] { false, "TARGET_NOT_FOUND" };
			} else if (tileEntity.targetNoInventory) {
				// Never actually happens due to a bug
				return new Object[] { false, "TARGET_LACKS_INVENTORY" };
			} else if (tileEntity.targetEmpty) {
				return new Object[] { false, "TARET_EMPTY" };
			}

			return new Object[] { true, "SUCCESS" };
		}

		@Callback(doc = "function(): bool -- Is cargo unloader enabled")
		public Object[] isEnabled(final Context context, final Arguments args) {
			return new Object[] { !tileEntity.disabled };
		}

		@Callback(doc = "function(bool: enable) -- Turn cargo unloader on/off")
		public Object[] setEnabled(final Context context, final Arguments args) {
			tileEntity.disabled = !args.checkBoolean(0);
			return new Object[] {};
		}
	}

	@Override
	public ManagedEnvironment createEnvironment(World world, BlockPos pos, EnumFacing facing) {
		TileEntityCargoUnloader tile = (TileEntityCargoUnloader) world.getTileEntity(pos);
		return new InternalManagedEnvironment(tile);
	}
}
