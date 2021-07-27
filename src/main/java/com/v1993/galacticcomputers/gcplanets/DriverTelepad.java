package com.v1993.galacticcomputers.gcplanets;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.prefab.DriverSidedBlock;

import java.lang.reflect.Method;

import com.v1993.galacticcomputers.utils.NamedManagedEnvironment;

import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityShortRangeTelepad;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityTelepadFake;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

// Note: since we're handling both telepad and its "fake" blocks, DriverSidedTileEntity isn't enough
// We also have to override basic methods for electric devices because fake telepad blocks report nonsense
public class DriverTelepad extends DriverSidedBlock {
	@Override
	public boolean worksWith(final World world, final BlockPos pos, final EnumFacing side) {
		final TileEntity tileEntity = world.getTileEntity(pos);
		if (tileEntity == null)
			return false;

		final Class<?> tileEntityClass = tileEntity.getClass();

		return TileEntityShortRangeTelepad.class.isAssignableFrom(tileEntityClass)
				|| TileEntityTelepadFake.class.isAssignableFrom(tileEntityClass);
	}

	public static class InternalManagedEnvironment extends NamedManagedEnvironment<TileEntityShortRangeTelepad> {
		public InternalManagedEnvironment(TileEntityShortRangeTelepad tileEntity) {
			super(tileEntity, "telepad");
		}

		// Address management

		@Callback(doc = "function(): number -- Get telepad's own address")
		public Object[] getAddress(final Context context, final Arguments args) {
			return new Object[] { tileEntity.address };
		}

		@Callback(doc = "function(number: frequency) -- Set telepad's own address")
		public Object[] setAddress(final Context context, final Arguments args) {
			tileEntity.setAddress(args.checkInteger(0));
			return new Object[] {};
		}

		@Callback(doc = "function(): boolean -- Check if telepad's own address is valid (nonnegative and unique)")
		public Object[] isAddressValid(final Context context, final Arguments args) {
			return new Object[] { tileEntity.addressValid };
		}

		@Callback(doc = "function(): number -- Get telepad's destination address")
		public Object[] getTargetAddress(final Context context, final Arguments args) {
			return new Object[] { tileEntity.targetAddress };
		}

		@Callback(doc = "function(number: frequency) -- Set telepad's destination address")
		public Object[] setTargetAddress(final Context context, final Arguments args) {
			tileEntity.setTargetAddress(args.checkInteger(0));
			return new Object[] {};
		}

		@Callback(doc = "function(): boolean, string -- Check if telepad's destination address is valid (with description string)")
		public Object[] getTargetStatus(final Context context, final Arguments args) {
			switch (tileEntity.targetAddressResult) {
			case VALID:
				return new Object[] { true, "SUCCESS" };
			case NOT_FOUND:
				return new Object[] { false, "NOT_FOUND" };
			case TOO_FAR:
			case WRONG_DIM:
				return new Object[] { false, "TOO_FAR" };
			case TARGET_DISABLED:
				return new Object[] { false, "TARGET_DISABLED" };
			default:
				return new Object[] { false, "UNKNOWN" };
			}
		}

		// Enable/disable telepad

		@Callback(doc = "function(): boolean -- Check if telepad's enabled")
		public Object[] isEnabled(final Context context, final Arguments args) {
			return new Object[] { !tileEntity.disabled };
		}

		@Callback(doc = "function(enabled: boolean) -- Enable or disable telepad")
		public Object[] setEnabled(final Context context, final Arguments args) {
			tileEntity.disabled = !args.checkBoolean(0);
			return new Object[] {};
		}

	}

	@Override
	public ManagedEnvironment createEnvironment(World world, BlockPos pos, EnumFacing facing) {
		final TileEntity tile = world.getTileEntity(pos);
		TileEntityShortRangeTelepad telepad;

		if (TileEntityShortRangeTelepad.class.isAssignableFrom(tile.getClass())) {
			// It's all good, we're working with main telepad tile
			telepad = (TileEntityShortRangeTelepad) tile;
		} else {
			try {
				// We're working with "fake" tile and this is gonna suck
				final Method getBaseTelepad = TileEntityTelepadFake.class.getDeclaredMethod("getBaseTelepad");
				getBaseTelepad.setAccessible(true);
				telepad = ((TileEntityShortRangeTelepad) getBaseTelepad.invoke(tile));
			} catch (Exception e) {
				// I'm not gonna deal with this trash any further
				return null;
			}
		}

		return (telepad != null) ? (new InternalManagedEnvironment(telepad)) : null;
	}
}
