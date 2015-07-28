package uk.co.oliwali.HawkEye.entry;

import java.sql.Timestamp;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import uk.co.oliwali.HawkEye.DataType;
import uk.co.oliwali.HawkEye.util.BlockUtil;

/**
 * Represents a block-type entry in the database
 * Rollbacks will set the block to the data value
 * @author oliverw92
 */
public class BlockEntry extends DataEntry {
	
	public BlockEntry(int playerId, Timestamp timestamp, int dataId, int typeId, String data, int worldId, int x, int y, int z) { 
		super(playerId, timestamp, dataId, typeId, data, worldId, x, y, z);
	}
	
	public BlockEntry(String cause, DataType type, Block block) {
		this.setInfo(null, cause, type, block.getLocation());
		
		this.data = BlockUtil.getBlockString(block);
	}
	
	public BlockEntry(UUID uuid, String playerName, DataType type, Block block) {
		this.setInfo(uuid, playerName, type, block.getLocation());
		
		this.data = BlockUtil.getBlockString(block);
	}
	
	public BlockEntry(Player player, DataType type, Block block) {
		this.setInfo(player, type, block.getLocation());
		
		this.data = BlockUtil.getBlockString(block);
	}
	
	public BlockEntry(Player player, DataType type, Block block, Location loc) {
		this.setInfo(player, type, loc);
		
		this.data = BlockUtil.getBlockString(block);
	}
	
	public BlockEntry(UUID uuid, String cause, DataType dataType, Material type, int blockdata, Location loc) {
		this.setInfo(uuid, cause, dataType, loc);
		
		if (blockdata != 0) {
			this.data = type.toString() + ":" + blockdata;
		} else {
			this.data = type.toString();
		}
	}
	
	@Override
	public String getStringData() {
		return BlockUtil.getBlockStringName(data);
	}
	
	@Override
	public boolean rollback(Block block) {
		BlockUtil.setBlockString(block, data);
		return true;
	}
	
	@Override
	public boolean rollbackPlayer(Block block, Player player) {
		player.sendBlockChange(block.getLocation(), BlockUtil.getMaterialFromString(data), BlockUtil.getDataFromString(data));
		return true;
	}
	
	@Override
	public boolean rebuild(Block block) {
		if (this.data == null) {
			return false;
		} else {
			block.setType(Material.AIR);
		}
		
		return true;
	}
}