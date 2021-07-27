package com.v1993.galacticcomputers.gcplanets;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.prefab.DriverSidedTileEntity;

import com.v1993.galacticcomputers.utils.NamedManagedEnvironment;

import micdoodle8.mods.galacticraft.planets.venus.tile.TileEntitySolarArrayController;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DriverSolarArray extends DriverSidedTileEntity {
	@Override
	public Class<?> getTileEntityClass() {
		return TileEntitySolarArrayController.class;
	}

	public static class InternalManagedEnvironment extends NamedManagedEnvironment<TileEntitySolarArrayController> {
		public InternalManagedEnvironment(TileEntitySolarArrayController tileEntity) {
			super(tileEntity, "solar_array");
		}

		@Callback(doc = "function(): number -- Energy being produced per tick")
		public Object[] getEnergyProduction(final Context context, final Arguments args) {
			return new Object[] { tileEntity.generateWatts };
		}

		@Callback(doc = "function(): boolean -- Is energy production enabled")
		public Object[] isEnabled(final Context context, final Arguments args) {
			return new Object[] { !tileEntity.getDisabled(0) };
		}

		@Callback(doc = "function(boolean: enable) -- Enable/disable energy production")
		public Object[] setEnabled(final Context context, final Arguments args) {
			tileEntity.setDisabled(0, !args.checkBoolean(0));
			return new Object[] {};
		}

		// Adapted from solar panel code
		@Callback(doc = "function(): string -- Get current status of solar array (see docs for possible values)")
		public Object[] getStatus(final Context context, final Arguments args) {
			int panelsAttached = tileEntity.getPossibleArraySize();
			int panelsActive = tileEntity.getActualArraySize();

			if (tileEntity.getDisabled(0)) {
				return new Object[] { "DISABLED" };
			} else if (panelsAttached == 0) {
				return new Object[] { "NO_PANELS" };
			} else if (!tileEntity.getWorld().isDaytime()) {
				return new Object[] { "NIGHT_TIME" };
			} else if (tileEntity.getWorld().isRaining() || tileEntity.getWorld().isThundering()) {
				return new Object[] { "RAINING" };
			} else if (panelsActive == 0) {
				return new Object[] { "BLOCKED_FULLY" };
			} else if (panelsActive < panelsAttached) {
				return new Object[] { "BLOCKED_PARTIALLY" };
			} else if (tileEntity.generateWatts > 0) {
				return new Object[] { "GENERATING" };
			} else {
				return new Object[] { "UNKNOWN" };
			}
		}

		@Callback(doc = "function(): number -- Get solar boost")
		public Object[] getBoost(final Context context, final Arguments args) {
			// Taken straight from GUI code
			return new Object[] { Math.round((tileEntity.getSolarBoost() - 1) * 1000) / 10.0F };
		}
	}

	@Override
	public ManagedEnvironment createEnvironment(World world, BlockPos pos, EnumFacing facing) {
		TileEntitySolarArrayController tile = (TileEntitySolarArrayController) world.getTileEntity(pos);
		return new InternalManagedEnvironment(tile);
	}
}
