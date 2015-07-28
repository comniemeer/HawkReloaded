package uk.co.oliwali.HawkEye.entry;

import java.sql.Timestamp;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import uk.co.oliwali.HawkEye.DataType;
import uk.co.oliwali.HawkEye.util.EntityUtil;


/**
 * Represents a hanging-type entry in the database
 * Rollbacks will set the block to the data value
 * @author bob7l
 */
public class HangingEntry extends DataEntry {
	
	public HangingEntry(int playerId, Timestamp timestamp, int dataId, int typeId, String data, int worldId, int x, int y, int z) { 
		super(playerId, timestamp, dataId, typeId, data, worldId, x, y ,z);
	}
	
	public HangingEntry(Player player, DataType type, Location loc, Material en, int da, String extra) {
		this.setInfo(player, type, loc);
		
		this.data = en.toString() + ":" + da + ":" + extra;
	}
	
	public HangingEntry(UUID uuid, String cause, DataType type, Location loc, Material en, int da, String extra) {
		this.setInfo(uuid, cause, type, loc);
		
		this.data = en.toString() + ":" + da + ":" + extra;
	}
	
	@Override
	public String getStringData() {
		return EntityUtil.getStringName(this.data);
	}
	
	@Override
	public boolean rollback(Block block) {
		EntityUtil.setBlockString(block, this.data);
		
		return true;
	}
	
	//Simply return true since we can't sendBlockChange (It's an entity)
	@Override
	public boolean rollbackPlayer(Block block, Player player) {
		return true;
	}
	
	//Simply return true since we can't rebuild (It's an entity)
	@Override
	public boolean rebuild(Block block) {
		return true;
	}
}