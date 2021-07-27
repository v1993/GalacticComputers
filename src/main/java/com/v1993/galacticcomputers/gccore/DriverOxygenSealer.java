package com.v1993.galacticcomputers.gccore;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.prefab.DriverSidedTileEntity;

import com.v1993.galacticcomputers.utils.NamedManagedEnvironment;

import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenSealer;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DriverOxygenSealer extends DriverSidedTileEntity {
	@Override
	public Class<?> getTileEntityClass() {
		return TileEntityOxygenSealer.class;
	}

	public static class InternalManagedEnvironment extends NamedManagedEnvironment<TileEntityOxygenSealer> {
		public InternalManagedEnvironment(TileEntityOxygenSealer tileEntity) {
			super(tileEntity, "oxygen_sealer");
		}

		@Callback(doc = "function(): bool -- Is sealer enabled")
		public Object[] isEnabled(final Context context, final Arguments args) {
			return new Object[] { !tileEntity.disabled };
		}

		@Callback(doc = "function(bool: enable) -- Turn sealer on/off")
		public Object[] setEnabled(final Context context, final Arguments args) {
			tileEntity.disabled = !args.checkBoolean(0);
			return new Object[] {};
		}

		@Callback(doc = "function(): bool -- Is sealer providing oxygen to surrounding area")
		public Object[] isSealed(final Context context, final Arguments args) {
			return new Object[] { tileEntity.sealed };
		}

		@Callback(doc = "function(): bool -- Is thermal control enabled")
		public Object[] isThermalControlEnabled(final Context context, final Arguments args) {
			return new Object[] { tileEntity.thermalControlEnabled() };
		}

		@Callback(doc = "function(): bool -- Is thermal control working")
		public Object[] isThermalControlWorking(final Context context, final Arguments args) {
			IBlockState stateAbove = tileEntity.getWorld().getBlockState(tileEntity.getPos().up());
			Block blockAbove = stateAbove.getBlock();

			return new Object[] { (blockAbove == GCBlocks.breatheableAir || blockAbove == GCBlocks.brightBreatheableAir)
					&& (blockAbove.getMetaFromState(stateAbove) == 1) };
		}
	}

	@Override
	public ManagedEnvironment createEnvironment(World world, BlockPos pos, EnumFacing facing) {
		TileEntityOxygenSealer tile = (TileEntityOxygenSealer) world.getTileEntity(pos);
		return new InternalManagedEnvironment(tile);
	}
}
