package com.v1993.galacticcomputers.gccore;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.prefab.DriverSidedTileEntity;

import com.v1993.galacticcomputers.utils.NamedManagedEnvironment;

import micdoodle8.mods.galacticraft.core.tile.TileEntityRefinery;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DriverRefinery extends DriverSidedTileEntity {
	@Override
	public Class<?> getTileEntityClass() {
		return TileEntityRefinery.class;
	}

	public static class InternalManagedEnvironment extends NamedManagedEnvironment<TileEntityRefinery> {
		public InternalManagedEnvironment(TileEntityRefinery tileEntity) {
			super(tileEntity, "refinery");
		}

		@Callback(doc = "function(): table -- Get infromation about oil tank")
		public Object[] getOilTank(final Context context, final Arguments args) {
			return new Object[] { tileEntity.oilTank.getInfo() };
		}

		@Callback(doc = "function(): table -- Get infromation about fuel tank")
		public Object[] getFuelTank(final Context context, final Arguments args) {
			return new Object[] { tileEntity.fuelTank.getInfo() };
		}

		@Callback(doc = "function(): bool -- Is refinery enabled")
		public Object[] isEnabled(final Context context, final Arguments args) {
			return new Object[] { !tileEntity.disabled };
		}

		@Callback(doc = "function(bool: enable) -- Turn refinery on/off")
		public Object[] setEnabled(final Context context, final Arguments args) {
			tileEntity.disabled = !args.checkBoolean(0);
			return new Object[] {};
		}
	}

	@Override
	public ManagedEnvironment createEnvironment(World world, BlockPos pos, EnumFacing facing) {
		TileEntityRefinery tile = (TileEntityRefinery) world.getTileEntity(pos);
		return new InternalManagedEnvironment(tile);
	}
}
