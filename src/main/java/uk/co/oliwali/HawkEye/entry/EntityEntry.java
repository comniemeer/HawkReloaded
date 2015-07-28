package uk.co.oliwali.HawkEye.entry;

import java.sql.Timestamp;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import uk.co.oliwali.HawkEye.DataType;
import uk.co.oliwali.HawkEye.util.EntityUtil;
/**
 * Represents a mob-type entry in the database
 * Rollbacks will set the mob to the data value
 * @author bob7l
 */
public class EntityEntry extends DataEntry {
	
	public EntityEntry(int playerId, Timestamp timestamp, int dataId, int typeId, String data, int worldId, int x, int y, int z) { 
		super(playerId, timestamp, dataId, typeId, data, worldId, x, y ,z);
	}
	
	public EntityEntry(Player player, DataType type, Location loc, String en) {
		this.setInfo(player, type, loc);
		
		this.data = en;
	}
	
	@Override
	public String getStringData() {
		return this.data;
	}
	
	@Override
	public boolean rollback(Block block) {
		EntityUtil.setEntityString(block, this.data);
		
		return true;
	}
}