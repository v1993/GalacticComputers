package com.v1993.galacticcomputers.galaxyspace;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.prefab.DriverSidedTileEntity;

import com.v1993.galacticcomputers.utils.NamedManagedEnvironment;

import galaxyspace.systems.SolarSystem.planets.overworld.tile.TileEntityPanelController;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

// Note: this is an exact copy of GC solar panel code.
// But there is no decent (non-reflective) way to avoid this!

public class DriverPanelsController extends DriverSidedTileEntity {
	@Override
	public Class<?> getTileEntityClass() {
		return TileEntityPanelController.class;
	}

	public static class InternalManagedEnvironment extends NamedManagedEnvironment<TileEntityPanelController> {
		public InternalManagedEnvironment(TileEntityPanelController tileEntity) {
			super(tileEntity, "panels_controller");
		}

		@Callback(doc = "function(): number -- Energy being produced per tick")
		public Object[] getEnergyProduction(final Context context, final Arguments args) {
			return new Object[] {
					Math.max(tileEntity.heatGJperTick - TileEntityPanelController.MIN_GENERATE_GJ_PER_TICK, 0) };
		}

		@Callback(doc = "function(): number -- Get solar boost")
		public Object[] getBoost(final Context context, final Arguments args) {
			// Taken straight from GUI code
			return new Object[] { Math.round((tileEntity.getSolarBoost() - 1) * 1000) / 10.0F };
		}
	}

	@Override
	public ManagedEnvironment createEnvironment(World world, BlockPos pos, EnumFacing facing) {
		TileEntityPanelController tile = (TileEntityPanelController) world.getTileEntity(pos);
		return new InternalManagedEnvironment(tile);
	}
}
