package uk.co.oliwali.HawkEye.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.material.MaterialData;

import uk.co.oliwali.HawkEye.DataType;
import uk.co.oliwali.HawkEye.HawkEvent;
import uk.co.oliwali.HawkEye.HawkEye;
import uk.co.oliwali.HawkEye.User;
import uk.co.oliwali.HawkEye.database.DataManager;
import uk.co.oliwali.HawkEye.entry.BlockChangeEntry;

public class MonitorLiquidFlow extends HawkEyeListener {
	
	private List<Material> fluidBlocks = new ArrayList<Material>();
	private HashMap<Location, User> playerCache = new HashMap<Location, User>(10);
	private int cacheRunTime = 10;
	private int timerId = -1;
	
	public MonitorLiquidFlow(HawkEye HawkEye) {
		super(HawkEye);
		
		this.loadFluidBlocks();
	}
	
	public void loadFluidBlocks() {
		this.fluidBlocks.add(Material.AIR);
		this.fluidBlocks.add(Material.POWERED_RAIL);
		this.fluidBlocks.add(Material.DETECTOR_RAIL);
		this.fluidBlocks.add(Material.LONG_GRASS);
		this.fluidBlocks.add(Material.DEAD_BUSH);
		this.fluidBlocks.add(Material.AIR);
		this.fluidBlocks.add(Material.YELLOW_FLOWER);
		this.fluidBlocks.add(Material.RED_ROSE);
		this.fluidBlocks.add(Material.BROWN_MUSHROOM);
		this.fluidBlocks.add(Material.RED_MUSHROOM);
		this.fluidBlocks.add(Material.TORCH);
		this.fluidBlocks.add(Material.FIRE);
		this.fluidBlocks.add(Material.REDSTONE_WIRE);
		this.fluidBlocks.add(Material.CROPS);
		this.fluidBlocks.add(Material.RAILS);
		this.fluidBlocks.add(Material.LEVER);
		this.fluidBlocks.add(Material.STONE_PLATE);
		this.fluidBlocks.add(Material.REDSTONE_TORCH_OFF);
		this.fluidBlocks.add(Material.REDSTONE_TORCH_ON);
		this.fluidBlocks.add(Material.SNOW);
		this.fluidBlocks.add(Material.DIODE_BLOCK_OFF);
		this.fluidBlocks.add(Material.DIODE_BLOCK_ON);
	}
	
	/**
	 * Clears the Player cache when it's been 10 seconds after a waterflow event
	 * Every time the event fires, the timer resets to allow the water to be tracked
	 */
	public void startCacheCleaner() {
		if (DataType.PLAYER_LAVA_FLOW.isLogged() || DataType.PLAYER_WATER_FLOW.isLogged()) {
			Bukkit.getScheduler().cancelTask(this.timerId);
			
			this.timerId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.plugin, new Runnable() {
				@Override
				public void run() {
					MonitorLiquidFlow.this.cacheRunTime--;
					
					if (MonitorLiquidFlow.this.cacheRunTime == 0) {
						MonitorLiquidFlow.this.playerCache.clear();
					}
				}
			}, 20L, 20L);
		}
	}
	
	/**
	 * Resets cache timer and
	 * adds the new location
	 */
	public void addToCache(Location l, User user) {
		this.cacheRunTime = 10; // Reset cache timer
		
		this.playerCache.put(l, user); // Add location to cache
	}
	
	@HawkEvent(dataType = {DataType.PLAYER_LAVA_FLOW, DataType.PLAYER_WATER_FLOW})
	public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
		Material bucket = event.getBucket();
		Location loc = event.getBlockClicked().getRelative(event.getBlockFace()).getLocation();
		
		if ((bucket == Material.WATER_BUCKET && DataType.PLAYER_WATER_FLOW.isLogged()) || (bucket == Material.LAVA_BUCKET && DataType.PLAYER_LAVA_FLOW.isLogged())) {
			this.playerCache.put(loc, new User(event.getPlayer().getUniqueId(), event.getPlayer().getName()));
		}
	}
	
	@HawkEvent(dataType = {DataType.PLAYER_LAVA_FLOW, DataType.PLAYER_WATER_FLOW})
	public void onPlayerBlockFromTo(BlockFromToEvent event) {
		// Only interested in liquids flowing
		if (!event.getBlock().isLiquid()) {
			return;
		}
		
		Location loc = event.getToBlock().getLocation();
		BlockState from = event.getBlock().getState();
		BlockState to = event.getToBlock().getState();
		
		if (from.getType() == to.getType()) {
			return;
		}
		
		Location fromloc = from.getLocation();
		User user = this.playerCache.get(fromloc);
		
		if (user == null) {
			return; // This is basically what containsKey does, but is 10x faster :)
		}
		
		MaterialData data = from.getData();
		
		// Lava
		if (from.getType() == Material.LAVA || from.getType() == Material.STATIONARY_LAVA) {
			if (this.fluidBlocks.contains(to.getType())) {
				// Flowing into a normal block
				data.setData((byte) (from.getRawData() + 1));
				from.setData(data);
			} else if (to.getType() == Material.WATER || to.getType() == Material.STATIONARY_WATER) {
				// Flowing into water
				from.setType(event.getFace() == BlockFace.DOWN ? Material.LAVA : Material.COBBLESTONE);
				data.setData((byte) 0);
				from.setData(data);
			}
			
			DataManager.addEntry(new BlockChangeEntry(user, DataType.PLAYER_LAVA_FLOW, loc, to, from));
			this.addToCache(loc, user);
		} else if (from.getType() == Material.WATER || from.getType() == Material.STATIONARY_WATER) {
			// Water
			// Normal block
			if (this.fluidBlocks.contains(to.getType())) {
				data.setData((byte) (from.getRawData() + 1));
				from.setData(data);
				DataManager.addEntry(new BlockChangeEntry(user, DataType.PLAYER_WATER_FLOW, loc, to, from));
				this.addToCache(loc, user);
			}
			
			// If we are flowing over lava, cobble or obsidian will form
			BlockState lower = event.getToBlock().getRelative(BlockFace.DOWN).getState();
			
			if (lower.getType() == Material.LAVA || lower.getType() == Material.STATIONARY_LAVA) {
				from.setType(lower.getData().getData() == 0 ? Material.OBSIDIAN : Material.COBBLESTONE);
				loc.setY(loc.getY() - 1);
				DataManager.addEntry(new BlockChangeEntry(user, DataType.PLAYER_WATER_FLOW, loc, lower, from));
				this.addToCache(loc, user);
			}
		}
	}
	
	@HawkEvent(dataType = {DataType.LAVA_FLOW, DataType.WATER_FLOW})
	public void onBlockFromTo(BlockFromToEvent event) {
		// Only interested in liquids flowing
		if (!event.getBlock().isLiquid()) {
			return;
		}
		
		Location loc = event.getToBlock().getLocation();
		BlockState from = event.getBlock().getState();
		BlockState to = event.getToBlock().getState();
		
		if (from.getType() == to.getType()){
			return;
		}
		
		MaterialData data = from.getData();
		
		// Lava
		if (from.getType() == Material.LAVA || from.getType() == Material.STATIONARY_LAVA) {
			if (this.fluidBlocks.contains(to.getType())) {
				// Flowing into a normal block
				data.setData((byte) (from.getRawData() + 1));
				from.setData(data);
			} else if (to.getType() == Material.WATER || to.getType() == Material.STATIONARY_WATER) {
				// Flowing into water
				from.setType(event.getFace() == BlockFace.DOWN ? Material.LAVA : Material.COBBLESTONE);
				data.setData((byte) 0);
				from.setData(data);
			}
			
			DataManager.addEntry(new BlockChangeEntry("Environment", DataType.LAVA_FLOW, loc, to, from));
		} else if (from.getType() == Material.WATER || from.getType() == Material.STATIONARY_WATER) {
			// Water
			// Normal block
			if (this.fluidBlocks.contains(to.getType())) {
				data.setData((byte) (from.getRawData() + 1));
				from.setData(data);
				DataManager.addEntry(new BlockChangeEntry("Environment", DataType.WATER_FLOW, loc, to, from));
			}
			
			// If we are flowing over lava, cobble or obsidian will form
			BlockState lower = event.getToBlock().getRelative(BlockFace.DOWN).getState();
			
			if (lower.getType() == Material.LAVA || lower.getType() == Material.STATIONARY_WATER) {
				from.setType(lower.getData().getData() == 0 ? Material.OBSIDIAN : Material.COBBLESTONE);
				loc.setY(loc.getY() - 1);
				DataManager.addEntry(new BlockChangeEntry("Environment", DataType.WATER_FLOW, loc, lower, from));
			}
		}
	}
}