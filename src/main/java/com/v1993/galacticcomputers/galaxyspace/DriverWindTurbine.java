package com.v1993.galacticcomputers.galaxyspace;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.prefab.DriverSidedTileEntity;

import com.v1993.galacticcomputers.utils.NamedManagedEnvironment;

import galaxyspace.systems.SolarSystem.planets.overworld.tile.TileEntityWindGenerator;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DriverWindTurbine extends DriverSidedTileEntity {
	@Override
	public Class<?> getTileEntityClass() {
		return TileEntityWindGenerator.class;
	}

	public static class InternalManagedEnvironment extends NamedManagedEnvironment<TileEntityWindGenerator> {
		public InternalManagedEnvironment(TileEntityWindGenerator tileEntity) {
			super(tileEntity, "wind_turbine");
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

		@Callback(doc = "function(): string -- Get current status of wind turbine (see docs for possible values)")
		public Object[] getStatus(final Context context, final Arguments args) {
			if (tileEntity.getDisabled(0)) {
				return new Object[] { "DISABLED" };
			} else if (tileEntity.generateWatts > 0) {
				return new Object[] { "GENERATING" };
			} else {
				return new Object[] { "UNKNOWN" };
			}
		}

		@Callback(doc = "function(): number -- Get environmental boost")
		public Object[] getBoost(final Context context, final Arguments args) {
			// Taken straight from GUI code
			return new Object[] { Math.round((tileEntity.getWindBoost() - 1) * 1000) / 10.0F };
		}
	}

	@Override
	public ManagedEnvironment createEnvironment(World world, BlockPos pos, EnumFacing facing) {
		TileEntityWindGenerator tile = (TileEntityWindGenerator) world.getTileEntity(pos);
		return new InternalManagedEnvironment(tile);
	}
}
