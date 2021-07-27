package com.v1993.galacticcomputers.galaxyspace;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.prefab.DriverSidedTileEntity;

import com.v1993.galacticcomputers.utils.NamedManagedEnvironment;

import galaxyspace.systems.SolarSystem.planets.overworld.tile.TileEntityGravitationModule;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DriverGravityModule extends DriverSidedTileEntity {
	@Override
	public Class<?> getTileEntityClass() {
		return TileEntityGravitationModule.class;
	}

	public static class InternalManagedEnvironment extends NamedManagedEnvironment<TileEntityGravitationModule> {
		public InternalManagedEnvironment(TileEntityGravitationModule tileEntity) {
			super(tileEntity, "gravity_module");
		}

		@Callback(doc = "function(): bool -- Are effects visualized")
		public Object[] areEffectsVisible(final Context context, final Arguments args) {
			return new Object[] { tileEntity.getEffectsVisible() };
		}

		@Callback(doc = "function(bool: visibility) -- Set effects visibility")
		public Object[] setEffectsVisible(final Context context, final Arguments args) {
			tileEntity.setEffectsVisible(args.checkBoolean(0));
			return new Object[] {};
		}

		@Callback(doc = "function(): bool -- Is gravity module enabled")
		public Object[] isEnabled(final Context context, final Arguments args) {
			return new Object[] { !tileEntity.disabled };
		}

		@Callback(doc = "function(bool: enable) -- Turn gravity module on/off")
		public Object[] setEnabled(final Context context, final Arguments args) {
			tileEntity.disabled = !args.checkBoolean(0);
			return new Object[] {};
		}

		@Callback(doc = "function(): number -- Get radius of affected area")
		public Object[] getGravityRadius(final Context context, final Arguments args) {
			return new Object[] { tileEntity.getGravityRadius() };
		}

		@Callback(doc = "function(): boolean -- Check if gravity effects are active")
		public Object[] isActive(final Context context, final Arguments args) {
			return new Object[] {
					tileEntity.getEnergyStoredGC() > 0.0F && tileEntity.hasEnoughEnergyToRun && !tileEntity.disabled };
		}
	}

	@Override
	public ManagedEnvironment createEnvironment(World world, BlockPos pos, EnumFacing facing) {
		TileEntityGravitationModule tile = (TileEntityGravitationModule) world.getTileEntity(pos);
		return new InternalManagedEnvironment(tile);
	}
}
