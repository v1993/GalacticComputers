package com.v1993.galacticcomputers.gccore;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.prefab.DriverSidedTileEntity;

import com.v1993.galacticcomputers.utils.NamedManagedEnvironment;

import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase;
import micdoodle8.mods.galacticraft.core.tile.TileEntityTelemetry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;

import java.lang.reflect.Field;
import java.util.HashMap;

// Telemetry is hard, sorry if something about it breaks.
// Being on server side makes things a little easier, but more confusing.

public class DriverTelemetryUnit extends DriverSidedTileEntity {
	@Override
	public Class<?> getTileEntityClass() {
		return TileEntityTelemetry.class;
	}

	public static class InternalManagedEnvironment extends NamedManagedEnvironment<TileEntityTelemetry> {
		// It comes from GC Planets, so we need to be extra careful
		static protected Class<?> classAstroMiner = null;

		public InternalManagedEnvironment(TileEntityTelemetry tileEntity) {
			super(tileEntity, "telemetry_unit");
		}

		@Callback(doc = "function(): boolean -- Is telemetry unit linked to something")
		public Object[] isLinked(final Context context, final Arguments args) {
			return new Object[] { tileEntity.linkedEntity != null };
		}

		@Callback(doc = "function(): table -- Read telemetry data from linked entity (try it and see results!)")
		public Object[] readTelemetry(final Context context, final Arguments args) {
			final Entity e = tileEntity.linkedEntity;
			if (e == null) {
				return new Object[] { null };
			}

			if (classAstroMiner == null && GalacticraftCore.isPlanetsLoaded) {
				try {
					classAstroMiner = Class
							.forName("micdoodle8.mods.galacticraft.planets.asteroids.entities.EntityAstroMiner");
				} catch (Exception ex) {
					// Should not happen, but whatever
				}
			}

			// Our resulting table (yup, it works!)
			HashMap<String, Object> m = new HashMap<String, Object>();

			// Now, since we are working on server, things are much different from how GC
			// intends them to be.
			// We have direct access to source entity yet lack access to GC's intended
			// telemetry array.
			// Some stuff is now much easier while other stuff suddenly requires reflection.
			// Server-side telemetry is weird.

			// Looks like everything have this method, which is handy for us
			m.put("name", e.getName());

			{
				// From GC sources (we add this info to every target 'cause why not)
				double xmotion = e.motionX;
				double ymotion = e instanceof EntityLivingBase ? e.motionY + 0.0784D : e.motionY;
				double zmotion = e.motionZ;
				// Blocks per second
				double speed = Math.sqrt(xmotion * xmotion + ymotion * ymotion + zmotion * zmotion) * 20D;
				m.put("speed", speed);
			}

			// Coordinates (perhaps add a config option to disable?)
			m.put("x", e.posX);
			m.put("y", e.posY);
			m.put("z", e.posZ);

			if (e instanceof EntityPlayer) {
				m.put("type", "PLAYER");
				m.put("food", ((EntityPlayer) e).getFoodStats().getFoodLevel() * 5);

				GCPlayerStats stats = GCPlayerStats.get(e);
				double oxygen = stats.getAirRemaining() * 4096 + stats.getAirRemaining2();
				// GUI code shows 90 and 180 as "OK", but we just report result
				m.put("oxygenSecondsLeft", oxygen * 9 / 20);
			} else if (e instanceof EntitySpaceshipBase) {
				final EntitySpaceshipBase es = (EntitySpaceshipBase) e;
				m.put("type", "ROCKET");
				m.put("countdown", es.timeUntilLaunch / 20F);
				m.put("isIgnited", es.launchPhase != 0);
				// m.put("altitude", es.posY); // We already store coordinates
				m.put("fuelTank", es.fuelTank.getInfo());
			} else if (classAstroMiner != null && classAstroMiner.isInstance(e)) {
				m.put("type", "ASTRO_MINER");
				// This is going to be painful thanks to reflection
				// No way I'm going to wrap each line into try-catch block!

				try {
					// Energy status
					m.put("storedEnergy", classAstroMiner.getField("energyLevel").getInt(e));
					m.put("maxEnergy", 12000); // It's hardcoded in original code too, so...
				} catch (Exception ex) {
					// Well, I !
				}

				try {
					// AI status
					int ai = classAstroMiner.getField("AIstate").getInt(e);
					String status = "UNKNOWN";

					if (ai == classAstroMiner.getField("AISTATE_STUCK").getInt(e)) {
						status = "STUCK";
					} else if (ai == classAstroMiner.getField("AISTATE_ATBASE").getInt(e)) {
						status = "DOCKED";
					} else if (ai == classAstroMiner.getField("AISTATE_TRAVELLING").getInt(e)) {
						status = "TRAVELLING";
					} else if (ai == classAstroMiner.getField("AISTATE_MINING").getInt(e)) {
						status = "MINING";
					} else if (ai == classAstroMiner.getField("AISTATE_RETURNING").getInt(e)) {
						status = "RETURNING";
					} else if (ai == classAstroMiner.getField("AISTATE_DOCKING").getInt(e)) {
						status = "DOCKING";
					} else if (ai == classAstroMiner.getField("AISTATE_OFFLINE").getInt(e)) {
						status = "OFFLINE";
					}

					m.put("status", status);
				} catch (Exception ex) {
					// Sorry. You're on your own.
				}
			} else if (e instanceof EntityLivingBase) {
				m.put("type", "MOB");
			} else {
				m.put("type", "GENERIC");
			}

			// This is in a separate block because it also includes players.
			if (e instanceof EntityLivingBase) {
				final EntityLivingBase el = (EntityLivingBase) e;
				m.put("health", el.getHealth());
				m.put("maxHealth", el.getMaxHealth());
				m.put("recentlyHurt", el.hurtTime > 0);

				// Pulse rate in BpM
				try {
					Field f = TileEntityTelemetry.class.getDeclaredField("pulseRate");
					f.setAccessible(true);
					m.put("pulseRate", ((int) f.get(tileEntity)) / 10);
				} catch (Exception ex) {
					// Not really important
				}
			}

			return new Object[] { m };
		}
	}

	@Override
	public ManagedEnvironment createEnvironment(World world, BlockPos pos, EnumFacing facing) {
		TileEntityTelemetry tile = (TileEntityTelemetry) world.getTileEntity(pos);
		return new InternalManagedEnvironment(tile);
	}
}
