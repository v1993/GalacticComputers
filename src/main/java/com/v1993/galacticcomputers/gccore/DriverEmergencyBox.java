package com.v1993.galacticcomputers.gccore;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.prefab.DriverSidedTileEntity;

import com.v1993.galacticcomputers.utils.NamedManagedEnvironment;

import micdoodle8.mods.galacticraft.core.tile.TileEntityEmergencyBox;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DriverEmergencyBox extends DriverSidedTileEntity {
	@Override
	public Class<?> getTileEntityClass() {
		return TileEntityEmergencyBox.class;
	}

	public static class InternalManagedEnvironment extends NamedManagedEnvironment<TileEntityEmergencyBox> {
		public InternalManagedEnvironment(TileEntityEmergencyBox tileEntity) {
			super(tileEntity, "emergency_box");
		}

		@Callback(doc = "function(): boolean -- Is there emergency kit inside")
		public Object[] haveKit(final Context context, final Arguments args) {
			IBlockState state = tileEntity.getWorld().getBlockState(tileEntity.getPos());
			Block block = state.getBlock();

			return new Object[] { block.getMetaFromState(state) == 1 };
		}

		// I don't think we *really* need ability to control those little doors on box.
		// But feel free to ask for them!
	}

	@Override
	public ManagedEnvironment createEnvironment(World world, BlockPos pos, EnumFacing facing) {
		TileEntityEmergencyBox tile = (TileEntityEmergencyBox) world.getTileEntity(pos);
		return new InternalManagedEnvironment(tile);
	}
}
