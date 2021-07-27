package com.v1993.galacticcomputers.gcplanets;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.prefab.DriverSidedTileEntity;

import com.v1993.galacticcomputers.utils.NamedManagedEnvironment;

import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityMethaneSynthesizer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DriverMethaneSynthesizer extends DriverSidedTileEntity {
	@Override
	public Class<?> getTileEntityClass() {
		return TileEntityMethaneSynthesizer.class;
	}

	public static class InternalManagedEnvironment extends NamedManagedEnvironment<TileEntityMethaneSynthesizer> {
		public InternalManagedEnvironment(TileEntityMethaneSynthesizer tileEntity) {
			super(tileEntity, "methane_synth"); // Full name is awful to type
		}

		@Callback(doc = "function(): table -- Get infromation about hydrogen tank")
		public Object[] getHydrogenTank(final Context context, final Arguments args) {
			return new Object[] { tileEntity.gasTank.getInfo() };
		}

		@Callback(doc = "function(): table -- Get infromation about CO2 tank")
		public Object[] getCarbonTank(final Context context, final Arguments args) {
			return new Object[] { tileEntity.gasTank2.getInfo() };
		}

		@Callback(doc = "function(): table -- Get infromation about methane tank")
		public Object[] getMethaneTank(final Context context, final Arguments args) {
			return new Object[] { tileEntity.liquidTank.getInfo() };
		}

		@Callback(doc = "function(): bool -- Is synthesizer enabled")
		public Object[] isEnabled(final Context context, final Arguments args) {
			return new Object[] { !tileEntity.disabled };
		}

		@Callback(doc = "function(bool: enable) -- Turn synthesizer on/off")
		public Object[] setEnabled(final Context context, final Arguments args) {
			tileEntity.disabled = !args.checkBoolean(0);
			return new Object[] {};
		}
	}

	@Override
	public ManagedEnvironment createEnvironment(World world, BlockPos pos, EnumFacing facing) {
		TileEntityMethaneSynthesizer tile = (TileEntityMethaneSynthesizer) world.getTileEntity(pos);
		return new InternalManagedEnvironment(tile);
	}
}
