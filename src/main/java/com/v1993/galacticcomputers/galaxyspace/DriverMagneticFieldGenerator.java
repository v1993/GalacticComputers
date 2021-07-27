package com.v1993.galacticcomputers.galaxyspace;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.prefab.DriverSidedTileEntity;

import com.v1993.galacticcomputers.utils.NamedManagedEnvironment;

import galaxyspace.systems.SolarSystem.planets.overworld.tile.TileEntityRadiationStabiliser;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DriverMagneticFieldGenerator extends DriverSidedTileEntity {
	@Override
	public Class<?> getTileEntityClass() {
		return TileEntityRadiationStabiliser.class;
	}

	public static class InternalManagedEnvironment extends NamedManagedEnvironment<TileEntityRadiationStabiliser> {
		public InternalManagedEnvironment(TileEntityRadiationStabiliser tileEntity) {
			super(tileEntity, "magnetic_generator");
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

		@Callback(doc = "function(): bool -- Is magnetic field enabled")
		public Object[] isEnabled(final Context context, final Arguments args) {
			return new Object[] { !tileEntity.disabled };
		}

		@Callback(doc = "function(bool: enable) -- Turn magnetic field on/off")
		public Object[] setEnabled(final Context context, final Arguments args) {
			tileEntity.disabled = !args.checkBoolean(0);
			return new Object[] {};
		}

		@Callback(doc = "function(): number -- Get radius of protected area")
		public Object[] getBubbleSize(final Context context, final Arguments args) {
			return new Object[] { tileEntity.getBubbleSize() };
		}

		@Callback(doc = "function(): boolean -- Check if radiation protection is active")
		public Object[] isActive(final Context context, final Arguments args) {
			return new Object[] {
					tileEntity.getEnergyStoredGC() > 0.0F && tileEntity.hasEnoughEnergyToRun && !tileEntity.disabled };
		}
	}

	@Override
	public ManagedEnvironment createEnvironment(World world, BlockPos pos, EnumFacing facing) {
		TileEntityRadiationStabiliser tile = (TileEntityRadiationStabiliser) world.getTileEntity(pos);
		return new InternalManagedEnvironment(tile);
	}
}
