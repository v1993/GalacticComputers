package com.v1993.galacticcomputers.galaxyspace;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.prefab.DriverSidedTileEntity;

import com.v1993.galacticcomputers.utils.NamedManagedEnvironment;

import galaxyspace.systems.SolarSystem.planets.overworld.tile.TileEntityFuelGenerator;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DriverFuelGenerator extends DriverSidedTileEntity {
	@Override
	public Class<?> getTileEntityClass() {
		return TileEntityFuelGenerator.class;
	}

	public static class InternalManagedEnvironment extends NamedManagedEnvironment<TileEntityFuelGenerator> {
		public InternalManagedEnvironment(TileEntityFuelGenerator tileEntity) {
			super(tileEntity, "fuel_generator");
		}

		@Callback(doc = "function(): table -- Get generator's fuel tank")
		public Object[] getFuelTank(final Context context, final Arguments args) {
			// This is silly but it works
			return tileEntity.getTankInfo(EnumFacing.UP);
		}

		@Callback(doc = "function(): number -- Energy being produced per tick")
		public Object[] getEnergyProduction(final Context context, final Arguments args) {
			if (tileEntity.heatGJperTick < TileEntityFuelGenerator.MIN_GENERATE_GJ_PER_TICK) {
				return new Object[] { 0 };
			}
			return new Object[] { tileEntity.heatGJperTick - TileEntityFuelGenerator.MIN_GENERATE_GJ_PER_TICK };
		}
	}

	@Override
	public ManagedEnvironment createEnvironment(World world, BlockPos pos, EnumFacing facing) {
		TileEntityFuelGenerator tile = (TileEntityFuelGenerator) world.getTileEntity(pos);
		return new InternalManagedEnvironment(tile);
	}
}
