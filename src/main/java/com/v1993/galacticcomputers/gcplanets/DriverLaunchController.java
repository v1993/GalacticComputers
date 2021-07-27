package com.v1993.galacticcomputers.gcplanets;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.prefab.DriverSidedTileEntity;
import com.v1993.galacticcomputers.utils.NamedManagedEnvironment;

import micdoodle8.mods.galacticraft.api.entity.IDockable;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntityAutoRocket;
import micdoodle8.mods.galacticraft.core.tile.TileEntityLandingPad;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityLaunchController;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DriverLaunchController extends DriverSidedTileEntity {
	@Override
	public Class<?> getTileEntityClass() {
		return TileEntityLaunchController.class;
	}

	public static class InternalManagedEnvironment extends NamedManagedEnvironment<TileEntityLaunchController> {
		public InternalManagedEnvironment(TileEntityLaunchController tileEntity) {
			super(tileEntity, "launch_controller");
		}

		// Frequency management

		@Callback(doc = "function(): number -- Get controller's own frequency")
		public Object[] getFrequency(final Context context, final Arguments args) {
			final int frequency = tileEntity.frequency;
			return new Object[] { frequency };
		}

		@Callback(doc = "function(number: frequency) -- Set controller's own frequency")
		public Object[] setFrequency(final Context context, final Arguments args) {
			final int frequency = args.checkInteger(0);
			tileEntity.setFrequency(frequency);
			return new Object[] {};
		}

		@Callback(doc = "function(): boolean -- Check if controller's own frequency is valid (nonnegative and unique)")
		public Object[] isFrequencyValid(final Context context, final Arguments args) {
			return new Object[] { tileEntity.frequencyValid };
		}

		@Callback(doc = "function(): number -- Get controller's destination frequency")
		public Object[] getDestinationFrequency(final Context context, final Arguments args) {
			final int destFrequency = tileEntity.destFrequency;
			return new Object[] { destFrequency };
		}

		@Callback(doc = "function(number: frequency) -- Set controller's destination frequency")
		public Object[] setDestinationFrequency(final Context context, final Arguments args) {
			final int destFrequency = args.checkInteger(0);
			tileEntity.setDestinationFrequency(destFrequency);
			return new Object[] {};
		}

		@Callback(doc = "function(): boolean -- Check if controller's destination frequency is valid")
		public Object[] isDestinationFrequencyValid(final Context context, final Arguments args) {
			return new Object[] { tileEntity.destFrequencyValid };
		}

		// Enable/disable controller

		@Callback(doc = "function(): boolean -- Check if controller's enabled")
		public Object[] isEnabled(final Context context, final Arguments args) {
			return new Object[] { !tileEntity.disabled };
		}

		@Callback(doc = "function(enabled: boolean) -- Enable or disable controller")
		public Object[] setEnabled(final Context context, final Arguments args) {
			tileEntity.disabled = !args.checkBoolean(0);
			return new Object[] {};
		}

		// Auto launch control
		// TODO: add launch condition control?

		@Callback(doc = "function(): boolean -- Check if auto luanch is enabled")
		public Object[] isAutolaunchEnabled(final Context context, final Arguments args) {
			return new Object[] { tileEntity.launchSchedulingEnabled };
		}

		@Callback(doc = "function(enabled: boolean) -- Enable or disable auto launch")
		public Object[] setAutolaunchEnabled(final Context context, final Arguments args) {
			tileEntity.setLaunchSchedulingEnabled(args.checkBoolean(0));
			return new Object[] {};
		}

		// Direct access to launch pad/rocket stuff
		// TODO: provide access to some of those directly from launch pad if GC Planets
		// is not installed

		private TileEntityLandingPad getLaunchPad() {
			return (tileEntity.attachedDock instanceof TileEntityLandingPad)
					? (TileEntityLandingPad) tileEntity.attachedDock
					: null;
		}

		private EntityAutoRocket getRocket() {
			TileEntityLandingPad pad = getLaunchPad();
			if (pad != null) {
				IDockable docked = pad.getDockedEntity();
				return (docked instanceof EntityAutoRocket) ? (EntityAutoRocket) docked : null;
			}
			return null;
		}

		private boolean isControllerOperational() {
			return !tileEntity.getDisabled(0) && tileEntity.hasEnoughEnergyToRun;
		}

		@Callback(doc = "function(): boolean -- Check if rocket is present on connected launch pad")
		public Object[] isRocketPresent(final Context context, final Arguments args) {
			EntityAutoRocket rocket = getRocket();
			return new Object[] { rocket != null };
		}

		@Callback(doc = "function(): table or boolean[, string] -- Get information about rocket's fuel tank, returns false and message on error")
		public Object[] getRocketFuelTank(final Context context, final Arguments args) {
			EntityAutoRocket rocket = getRocket();
			if (rocket == null) {
				return new Object[] { false, "rocket not found" };
			}
			return new Object[] { rocket.fuelTank.getInfo() };
		}

		@Callback(doc = "function(): number or boolean[, string] -- Get rocket's countdown in seconds (with fractional part), returns false and message on error")
		public Object[] getCountdown(final Context context, final Arguments args) {
			EntityAutoRocket rocket = getRocket();
			if (rocket == null) {
				return new Object[] { false, "rocket not found" };
			}
			return new Object[] { rocket.timeUntilLaunch / 20F };
		}

		@Callback(doc = "function(): boolean[, string] -- Check is rocket ignited, returns false and message on error (check if message is present to see if it was an error)")
		public Object[] isRocketIgnited(final Context context, final Arguments args) {
			EntityAutoRocket rocket = getRocket();
			if (rocket == null) {
				return new Object[] { false, "rocket not found" };
			}
			return new Object[] { rocket.launchPhase != 0 };
		}

		@Callback(doc = "function(): boolean[, string] -- Check it's a passenger (player) rocket, returns false and message on error (check if message is present to see if it was an error)")
		public Object[] isRocketPassenger(final Context context, final Arguments args) {
			EntityAutoRocket rocket = getRocket();
			if (rocket == null) {
				return new Object[] { false, "rocket not found" };
			}
			return new Object[] { rocket.isPlayerRocket() };
		}

		@Callback(doc = "function([boolean: skipFuelCheck]): boolean, string -- Launch rocket, returns if launch succeeded and informational string. Skip fuel check at your own risk!")
		public Object[] launch(final Context context, final Arguments args) {
			boolean skipFuelCheck = args.optBoolean(0, false);

			if (tileEntity.getDisabled(0) || !tileEntity.hasEnoughEnergyToRun) {
				return new Object[] { false, "controller not operational" };
			}

			EntityAutoRocket rocket = getRocket();
			if (rocket == null) {
				return new Object[] { false, "rocket not found" };
			}

			if (rocket.launchPhase != 0) {
				return new Object[] { false, "rocket already ignited" };
			}

			if (!rocket.hasValidFuel()) {
				return new Object[] { false, "no fuel in rocket" };
			}

			if (!skipFuelCheck && rocket.fuelTank.getFluidAmount() <= rocket.getMaxFuel() * 2 / 5) {
				return new Object[] { false, "not enough fuel for safe launch" };
			}

			if (rocket.igniteWithResult()) {
				return new Object[] { true, "success" };
			} else {
				return new Object[] { false, "ignition failed" };
			}
		}

		// Other useful functions

		@Callback(doc = "function(): string -- Get owner of the controller")
		public Object[] getOwner(final Context context, final Arguments args) {
			final String owner = tileEntity.getOwnerName();
			return new Object[] { owner };
		}

		@Callback(doc = "function(): boolean -- Check if controller is fully working (configured, powered, etc.)")
		public Object[] isCargoReady(final Context context, final Arguments args) {
			return new Object[] { tileEntity.validFrequency() };
		}

		@Callback(doc = "function(): boolean -- Check if controller can launch passenger rockets (powered and enabled)")
		public Object[] isOccupied(final Context context, final Arguments args) {
			return new Object[] { isControllerOperational() };
		}

	};

	@Override
	public ManagedEnvironment createEnvironment(World world, BlockPos pos, EnumFacing facing) {
		return new InternalManagedEnvironment((TileEntityLaunchController) world.getTileEntity(pos));
	}
}
