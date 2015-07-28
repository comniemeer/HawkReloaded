package uk.co.oliwali.HawkEye.listeners;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;

import uk.co.oliwali.HawkEye.DataType;
import uk.co.oliwali.HawkEye.HawkEvent;
import uk.co.oliwali.HawkEye.HawkEye;
import uk.co.oliwali.HawkEye.User;
import uk.co.oliwali.HawkEye.database.DataManager;
import uk.co.oliwali.HawkEye.entry.BlockChangeEntry;

/**
 * FallingBlockEntity listener class for HawkEye
 * Contains system for logging Fallingblocks
 * @author bob7l
 */
public class MonitorFallingBlockListener extends HawkEyeListener {
	
	private HashMap<Entity, User> blocks = new HashMap<Entity, User>();
	
	public MonitorFallingBlockListener(HawkEye HawkEye) {
		super(HawkEye);
	}
	
	@HawkEvent(dataType = DataType.FALLING_BLOCK)
	public void onBlockPlace(final BlockPlaceEvent event) {
		Material type = event.getBlock().getType();
		
		if ((type.equals(Material.SAND) || type.equals(Material.GRAVEL) || type.equals(Material.ANVIL)) && event.getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.AIR)) {
			HawkEye.server.getScheduler().scheduleSyncDelayedTask(HawkEye.instance, new Runnable() {
				@Override
				public void run() {
					Location l = event.getBlock().getLocation();
					
					for (Entity e : l.getWorld().getEntitiesByClass(FallingBlock.class)) {
						if (l.distanceSquared(e.getLocation()) <= 0.8) {
							MonitorFallingBlockListener.this.blocks.put(e, new User(event.getPlayer().getUniqueId(), event.getPlayer().getName()));
							return;
						}
					}
				}
			}, 6L);
			
			return;
		}
	}
	
	@HawkEvent(dataType = DataType.FALLING_BLOCK) 
	public void onEntityModifyBlock(EntityChangeBlockEvent event) {
		Entity entity = event.getEntity();
		
		if (entity instanceof FallingBlock && this.blocks.containsKey(entity)) {
			FallingBlock fb = (FallingBlock) entity;
			Block block = event.getBlock();
			String data = fb.getBlockData() == 0 ? fb.getMaterial().toString() : fb.getMaterial().toString() + ":" + fb.getBlockData();
			
			DataManager.addEntry(new BlockChangeEntry(this.blocks.get(entity), DataType.FALLING_BLOCK, block.getLocation(), event.getBlock().getState(), data));
		}
	}
}