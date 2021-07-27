package com.v1993.galacticcomputers.gcplanets;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.prefab.DriverSidedTileEntity;

import com.v1993.galacticcomputers.utils.NamedManagedEnvironment;

import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityElectrolyzer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DriverWaterElectrolyzer extends DriverSidedTileEntity {
	@Override
	public Class<?> getTileEntityClass() {
		return TileEntityElectrolyzer.class;
	}

	public static class InternalManagedEnvironment extends NamedManagedEnvironment<TileEntityElectrolyzer> {
		public InternalManagedEnvironment(TileEntityElectrolyzer tileEntity) {
			super(tileEntity, "electrolyzer");
		}

		@Callback(doc = "function(): table -- Get infromation about water tank")
		public Object[] getWaterTank(final Context context, final Arguments args) {
			return new Object[] { tileEntity.waterTank.getInfo() };
		}

		@Callback(doc = "function(): table, table -- Get infromation about output tanks")
		public Object[] getOutputTanks(final Context context, final Arguments args) {
			return new Object[] { tileEntity.liquidTank.getInfo(), tileEntity.liquidTank2.getInfo() };
		}

		@Callback(doc = "function(): bool -- Is electrolyzer enabled")
		public Object[] isEnabled(final Context context, final Arguments args) {
			return new Object[] { !tileEntity.disabled };
		}

		@Callback(doc = "function(bool: enable) -- Turn electrolyzer on/off")
		public Object[] setEnabled(final Context context, final Arguments args) {
			tileEntity.disabled = !args.checkBoolean(0);
			return new Object[] {};
		}
	}

	@Override
	public ManagedEnvironment createEnvironment(World world, BlockPos pos, EnumFacing facing) {
		TileEntityElectrolyzer tile = (TileEntityElectrolyzer) world.getTileEntity(pos);
		return new InternalManagedEnvironment(tile);
	}
}
