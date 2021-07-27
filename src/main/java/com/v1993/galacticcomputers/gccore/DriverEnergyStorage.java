package com.v1993.galacticcomputers.gccore;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.prefab.DriverSidedTileEntity;

import com.v1993.galacticcomputers.utils.NamedManagedEnvironment;

import java.lang.reflect.Method;

import com.v1993.galacticcomputers.utils.ComponentPriority;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.energy.tile.EnergyStorageTile;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DriverEnergyStorage extends DriverSidedTileEntity {
	@Override
	public Class<?> getTileEntityClass() {
		return EnergyStorageTile.class;
	}

	public static class InternalManagedEnvironment extends NamedManagedEnvironment<EnergyStorageTile> {
		public InternalManagedEnvironment(EnergyStorageTile tileEntity) {
			super(tileEntity, "gc_energy_device");
		}

		@Override
		public int priority() {
			return ComponentPriority.GENERIC;
		}

		@Callback(doc = "function(): number -- (GC) Get amount of energy currently stored")
		public Object[] getStoredEnergy(final Context context, final Arguments args) {
			return new Object[] { tileEntity.getEnergyStoredGC() };
		}

		@Callback(doc = "function(): number -- (GC) Get energy capacity of the machine")
		public Object[] getMaxEnergy(final Context context, final Arguments args) {
			return new Object[] { tileEntity.getMaxEnergyStoredGC() };
		}

		/*
		 * Not really useful, constant, often misleading
		 * 
		 * @Callback(doc = "function(): number -- Get energy tier of machine") public
		 * Object[] energyTier(final Context context, final Arguments args) { return new
		 * Object[] { tileEntity.getTierGC() }; }
		 */
	}

	// Some machines use this class but don't use electricity. What the hell?
	@Override
	public boolean worksWith(final World world, final BlockPos pos, final EnumFacing side) {
		if (super.worksWith(world, pos, side)) {
			EnergyStorageTile tile = (EnergyStorageTile) world.getTileEntity(pos);
			return tile.getMaxEnergyStoredGC() != 0;
		}
		return false;
	}

	@Override
	public ManagedEnvironment createEnvironment(World world, BlockPos pos, EnumFacing facing) {
		EnergyStorageTile tile = (EnergyStorageTile) world.getTileEntity(pos);
		// Telepads are made in confusing and annoying manner
		if (GalacticraftCore.isPlanetsLoaded) {
			// We find the real telepad entity and work with it
			try {
				final Class<?> fakepad = Class
						.forName("micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityTelepadFake");
				if (fakepad.isAssignableFrom(tile.getClass())) {
					final Method getBaseTelepad = fakepad.getDeclaredMethod("getBaseTelepad");
					getBaseTelepad.setAccessible(true);
					tile = ((EnergyStorageTile) getBaseTelepad.invoke(tile));
				}
			} catch (Exception e) {
				// Return null to ensure that incorrect information is not returned in case of
				// an error.
				// Am I putting more into this than some people do with real safety-critical
				// systems?..
				return null;
			}
		}

		return (tile != null) ? (new InternalManagedEnvironment(tile)) : null;
	}
}
