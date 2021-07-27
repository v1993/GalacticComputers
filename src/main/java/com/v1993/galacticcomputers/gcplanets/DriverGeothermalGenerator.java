package com.v1993.galacticcomputers.gcplanets;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.prefab.DriverSidedTileEntity;

import com.v1993.galacticcomputers.utils.NamedManagedEnvironment;

import micdoodle8.mods.galacticraft.planets.venus.tile.TileEntityGeothermalGenerator;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DriverGeothermalGenerator extends DriverSidedTileEntity {
	@Override
	public Class<?> getTileEntityClass() {
		return TileEntityGeothermalGenerator.class;
	}

	public static class InternalManagedEnvironment extends NamedManagedEnvironment<TileEntityGeothermalGenerator> {
		public InternalManagedEnvironment(TileEntityGeothermalGenerator tileEntity) {
			super(tileEntity, "geothermal_generator");
		}

		@Callback(doc = "function(): boolean -- Check if generator have valid sulfur spout")
		public Object[] isOnSpout(final Context context, final Arguments args) {
			return new Object[] { tileEntity.hasValidSpout() };
		}

		@Callback(doc = "function(): number -- Energy being produced per tick")
		public Object[] getEnergyProduction(final Context context, final Arguments args) {
			return new Object[] { tileEntity.generateWatts };
		}

		@Callback(doc = "function(): boolean -- Is energy production enabled")
		public Object[] isEnabled(final Context context, final Arguments args) {
			return new Object[] { !tileEntity.disabled };
		}

		@Callback(doc = "function(enabled: boolean) -- Enable or disable energy production")
		public Object[] setEnabled(final Context context, final Arguments args) {
			tileEntity.disabled = !args.checkBoolean(0);
			return new Object[] {};
		}
	}

	@Override
	public ManagedEnvironment createEnvironment(World world, BlockPos pos, EnumFacing facing) {
		TileEntityGeothermalGenerator tile = (TileEntityGeothermalGenerator) world.getTileEntity(pos);
		return new InternalManagedEnvironment(tile);
	}
}
