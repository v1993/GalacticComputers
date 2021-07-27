package com.v1993.galacticcomputers.gccore;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.prefab.DriverSidedTileEntity;

import java.lang.reflect.Field;

import com.v1993.galacticcomputers.utils.NamedManagedEnvironment;

import micdoodle8.mods.galacticraft.core.tile.TileEntityFuelLoader;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DriverFuelLoader extends DriverSidedTileEntity {
	@Override
	public Class<?> getTileEntityClass() {
		return TileEntityFuelLoader.class;
	}

	public static class InternalManagedEnvironment extends NamedManagedEnvironment<TileEntityFuelLoader> {
		public InternalManagedEnvironment(TileEntityFuelLoader tileEntity) {
			super(tileEntity, "fuel_loader");
		}

		@Callback(doc = "function(): bool -- Is fuel being loaded right now")
		public Object[] isLoading(final Context context, final Arguments args) throws Exception {
			// They have unexpectedly declared it private, so use reflection
			try {
				Field f = TileEntityFuelLoader.class.getDeclaredField("loadedFuelLastTick");
				f.setAccessible(true);
				return new Object[] { (boolean) f.get(tileEntity) };
			} catch (Exception e) {
				throw new Exception("failed to get fueler status");
			}
		}

		@Callback(doc = "function(): table -- Get infromation about loader's fuel tank")
		public Object[] getFuelTank(final Context context, final Arguments args) {
			return new Object[] { tileEntity.fuelTank.getInfo() };
		}

		@Callback(doc = "function(): bool -- Is fueler enabled")
		public Object[] isEnabled(final Context context, final Arguments args) {
			return new Object[] { !tileEntity.disabled };
		}

		@Callback(doc = "function(bool: enable) -- Turn fueler on/off")
		public Object[] setEnabled(final Context context, final Arguments args) {
			tileEntity.disabled = !args.checkBoolean(0);
			return new Object[] {};
		}
	}

	@Override
	public ManagedEnvironment createEnvironment(World world, BlockPos pos, EnumFacing facing) {
		TileEntityFuelLoader tile = (TileEntityFuelLoader) world.getTileEntity(pos);
		return new InternalManagedEnvironment(tile);
	}
}
