package com.v1993.galacticcomputers.gcplanets;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.prefab.DriverSidedTileEntity;

import com.v1993.galacticcomputers.utils.NamedManagedEnvironment;

import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityCryogenicChamber;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DriverCryogenicChamber extends DriverSidedTileEntity {
	@Override
	public Class<?> getTileEntityClass() {
		return TileEntityCryogenicChamber.class;
	}

	public static class InternalManagedEnvironment extends NamedManagedEnvironment<TileEntityCryogenicChamber> {
		public InternalManagedEnvironment(TileEntityCryogenicChamber tileEntity) {
			super(tileEntity, "cryogenic_chamber");
		}

		@Callback(doc = "function(): boolean -- Check if cryogenic chamber is occupuied")
		public Object[] isOccupied(final Context context, final Arguments args) {
			return new Object[] { tileEntity.isOccupied };
		}
	}

	@Override
	public ManagedEnvironment createEnvironment(World world, BlockPos pos, EnumFacing facing) {
		return new InternalManagedEnvironment((TileEntityCryogenicChamber) world.getTileEntity(pos));
	}
}
