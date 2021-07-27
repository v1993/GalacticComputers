package com.v1993.galacticcomputers.gccore;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.prefab.DriverSidedTileEntity;

import com.v1993.galacticcomputers.utils.NamedManagedEnvironment;

import micdoodle8.mods.galacticraft.core.tile.TileEntityCoalGenerator;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DriverCoalGenerator extends DriverSidedTileEntity {
	@Override
	public Class<?> getTileEntityClass() {
		return TileEntityCoalGenerator.class;
	}

	public static class InternalManagedEnvironment extends NamedManagedEnvironment<TileEntityCoalGenerator> {
		public InternalManagedEnvironment(TileEntityCoalGenerator tileEntity) {
			super(tileEntity, "coal_generator");
		}

		@Callback(doc = "function(): number -- Get generator's hull heat (won't generate energy if below 100%)")
		public Object[] getHullHeat(final Context context, final Arguments args) {
			return new Object[] { Math.min(tileEntity.heatGJperTick, TileEntityCoalGenerator.MIN_GENERATE_GJ_PER_TICK)
					/ TileEntityCoalGenerator.MIN_GENERATE_GJ_PER_TICK * 100 };
		}

		@Callback(doc = "function(): number -- Energy being produced per tick")
		public Object[] getEnergyProduction(final Context context, final Arguments args) {
			if (tileEntity.heatGJperTick < TileEntityCoalGenerator.MIN_GENERATE_GJ_PER_TICK) {
				return new Object[] { 0 };
			}
			return new Object[] { tileEntity.heatGJperTick - TileEntityCoalGenerator.MIN_GENERATE_GJ_PER_TICK };
		}

		@Callback(doc = "function(): boolean -- Is there actively burning fuel in generator")
		public Object[] isBurning(final Context context, final Arguments args) {
			return new Object[] { tileEntity.itemCookTime > 0 };
		}
	}

	@Override
	public ManagedEnvironment createEnvironment(World world, BlockPos pos, EnumFacing facing) {
		TileEntityCoalGenerator tile = (TileEntityCoalGenerator) world.getTileEntity(pos);
		return new InternalManagedEnvironment(tile);
	}
}
