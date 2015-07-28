package uk.co.oliwali.HawkEye.worldedit;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockState;

import uk.co.oliwali.HawkEye.DataType;
import uk.co.oliwali.HawkEye.database.DataManager;
import uk.co.oliwali.HawkEye.entry.BlockChangeEntry;
import uk.co.oliwali.HawkEye.entry.BlockEntry;
import uk.co.oliwali.HawkEye.entry.SignEntry;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.extent.logging.AbstractLoggingExtent;

public class HawkSession extends AbstractLoggingExtent {
	
	private final Actor player;
	private final World world;
	
	public HawkSession(Actor player, com.sk89q.worldedit.world.World worldedit_world, Extent extent) {
		super(extent);
		
		this.player = player;
		this.world = ((BukkitWorld) worldedit_world).getWorld();
	}
	
	@Override
	protected void onBlockChange(Vector v, BaseBlock block) {
		BlockState bs = null;
		Material type = this.world.getBlockAt(v.getBlockX(), v.getBlockY(), v.getBlockZ()).getType();
		int bdata = this.world.getBlockAt(v.getBlockX(), v.getBlockY(), v.getBlockZ()).getData();
		
		if (type == Material.SIGN_POST || type == Material.WALL_SIGN) {
			bs = this.world.getBlockAt(v.getBlockX(), v.getBlockY(), v.getBlockZ()).getState();
		}
		
		Location loc = new Location(this.world, v.getBlockX(), v.getBlockY(), v.getBlockZ());
		
		if (block.getType() != 0) {
			if (block.getType() == type.getId() && block.getData() == bdata) {
				return;
			}
			
			DataManager.addEntry(new BlockChangeEntry(this.player.getUniqueId(), this.player.getName(), DataType.WORLDEDIT_PLACE, loc, type, bdata, Material.getMaterial(block.getType()), block.getData()));
		} else {
			if ((type == Material.SIGN_POST || type == Material.WALL_SIGN) && DataType.SIGN_BREAK.isLogged()) {
				DataManager.addEntry(new SignEntry(this.player.getUniqueId(), this.player.getName(), DataType.SIGN_BREAK, bs));
			} else if (type != Material.AIR) {
				DataManager.addEntry(new BlockEntry(this.player.getUniqueId(), this.player.getName(), DataType.WORLDEDIT_BREAK, type, bdata, loc));
			}
		}
	}
}