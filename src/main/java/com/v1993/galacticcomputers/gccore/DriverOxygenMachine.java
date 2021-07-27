package com.v1993.galacticcomputers.gccore;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.prefab.DriverSidedTileEntity;

import com.v1993.galacticcomputers.utils.NamedManagedEnvironment;
import com.v1993.galacticcomputers.utils.ComponentPriority;

import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygen;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DriverOxygenMachine extends DriverSidedTileEntity {
	@Override
	public Class<?> getTileEntityClass() {
		return TileEntityOxygen.class;
	}

	public static class InternalManagedEnvironment extends NamedManagedEnvironment<TileEntityOxygen> {
		public InternalManagedEnvironment(TileEntityOxygen tileEntity) {
			super(tileEntity, "oxygen_machine");
		}

		@Override
		public int priority() {
			return ComponentPriority.GENERIC + 1;
		}

		@Callback(doc = "function(): table -- Get information about machine's oxygen tank")
		public Object[] getOxygenTank(final Context context, final Arguments args) {
			return new Object[] { tileEntity.tank.getInfo() };
		}
	}

	@Override
	public ManagedEnvironment createEnvironment(World world, BlockPos pos, EnumFacing facing) {
		TileEntityOxygen tile = (TileEntityOxygen) world.getTileEntity(pos);
		return new InternalManagedEnvironment(tile);
	}
}
