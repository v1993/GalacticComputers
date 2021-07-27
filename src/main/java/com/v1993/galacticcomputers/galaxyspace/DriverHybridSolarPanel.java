package com.v1993.galacticcomputers.galaxyspace;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.prefab.DriverSidedTileEntity;

import com.v1993.galacticcomputers.utils.NamedManagedEnvironment;

import galaxyspace.systems.SolarSystem.planets.overworld.tile.TileEntityModernSolarPanel;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

// Note: this is an exact copy of GC solar panel code.
// But there is no decent (non-reflective) way to avoid this!

public class DriverHybridSolarPanel extends DriverSidedTileEntity {
	@Override
	public Class<?> getTileEntityClass() {
		return TileEntityModernSolarPanel.class;
	}

	public static class InternalManagedEnvironment extends NamedManagedEnvironment<TileEntityModernSolarPanel> {
		public InternalManagedEnvironment(TileEntityModernSolarPanel tileEntity) {
			super(tileEntity, "solar_panel");
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

		@Callback(doc = "function(): number -- Energy being produced per tick")
		public Object[] getEnergyProduction(final Context context, final Arguments args) {
			return new Object[] { tileEntity.generateWatts };
		}

		@Callback(doc = "function(): string -- Get current status of solar panel (see docs for possible values)")
		public Object[] getStatus(final Context context, final Arguments args) {
			if (tileEntity.getDisabled(0)) {
				return new Object[] { "DISABLED" };
			} else if (!tileEntity.getWorld().isDaytime()) {
				return new Object[] { "NIGHT_TIME" };
			} else if (tileEntity.getWorld().isRaining() || tileEntity.getWorld().isThundering()) {
				return new Object[] { "RAINING" };
			} else if (tileEntity.solarStrength == 0) {
				return new Object[] { "BLOCKED_FULLY" };
			} else if (tileEntity.solarStrength < 9) {
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
		TileEntityModernSolarPanel tile = (TileEntityModernSolarPanel) world.getTileEntity(pos);
		return new InternalManagedEnvironment(tile);
	}
}
