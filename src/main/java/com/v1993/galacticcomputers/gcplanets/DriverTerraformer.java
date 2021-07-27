package com.v1993.galacticcomputers.gcplanets;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.prefab.DriverSidedTileEntity;

import com.v1993.galacticcomputers.utils.NamedManagedEnvironment;

import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityTerraformer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DriverTerraformer extends DriverSidedTileEntity {
	@Override
	public Class<?> getTileEntityClass() {
		return TileEntityTerraformer.class;
	}

	public static class InternalManagedEnvironment extends NamedManagedEnvironment<TileEntityTerraformer> {
		public InternalManagedEnvironment(TileEntityTerraformer tileEntity) {
			super(tileEntity, "terraformer");
		}

		@Callback(doc = "function(): table -- Get infromation about water tank")
		public Object[] getWaterTank(final Context context, final Arguments args) {
			return new Object[] { tileEntity.waterTank.getInfo() };
		}

		@Callback(doc = "function(): boolean -- Is bubble visible")
		public Object[] isBubbleVisible(final Context context, final Arguments args) {
			return new Object[] { tileEntity.getBubbleVisible() };
		}

		@Callback(doc = "function(boolean: visibility) -- Set bubble visibility")
		public Object[] setBubbleVisible(final Context context, final Arguments args) {
			tileEntity.setBubbleVisible(args.checkBoolean(0));
			return new Object[] {};
		}

		@Callback(doc = "function(): number -- Get terraformer's action radius")
		public Object[] getRadius(final Context context, final Arguments args) {
			return new Object[] { tileEntity.getBubbleSize() };
		}

		@Callback(doc = "function(): boolean -- Is grass being planted")
		public Object[] isPlantingGrass(final Context context, final Arguments args) {
			return new Object[] { !tileEntity.grassDisabled };
		}

		@Callback(doc = "function(boolean: visibility) -- Turn planting grass on/off")
		public Object[] setPlantingGrass(final Context context, final Arguments args) {
			tileEntity.grassDisabled = !args.checkBoolean(0);
			return new Object[] {};
		}

		@Callback(doc = "function(): boolean -- Are trees being planted")
		public Object[] isPlantingTrees(final Context context, final Arguments args) {
			return new Object[] { !tileEntity.treesDisabled };
		}

		@Callback(doc = "function(boolean: visibility) -- Turn planting trees on/off")
		public Object[] setPlantingTrees(final Context context, final Arguments args) {
			tileEntity.treesDisabled = !args.checkBoolean(0);
			return new Object[] {};
		}
	}

	@Override
	public ManagedEnvironment createEnvironment(World world, BlockPos pos, EnumFacing facing) {
		TileEntityTerraformer tile = (TileEntityTerraformer) world.getTileEntity(pos);
		return new InternalManagedEnvironment(tile);
	}
}
