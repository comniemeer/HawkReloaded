package uk.co.oliwali.HawkEye.entry;

import java.sql.Timestamp;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

import uk.co.oliwali.HawkEye.DataType;
import uk.co.oliwali.HawkEye.User;
import uk.co.oliwali.HawkEye.util.BlockUtil;

/**
 * Represents a block change entry - one block changing to another
 * @author oliverw92
 */
public class BlockChangeEntry extends DataEntry {
	
	private String from = null;
	private String to = null;
	
	public BlockChangeEntry(int playerId, Timestamp timestamp, int dataId, int typeId, String data, int worldId, int x, int y, int z) { 
		super(playerId, timestamp, dataId, typeId, worldId, x, y ,z);
		interpretSqlData(data);
	}
	
	public BlockChangeEntry(Player player, DataType type, Location loc, BlockState from, BlockState to) {
		this.setInfo(player, type, loc);
		
		this.from = BlockUtil.getBlockString(from);
		this.to = BlockUtil.getBlockString(to);
	}
	
	public BlockChangeEntry(Player player, DataType dataType, Location loc, BlockState from, Material type) {
		this.setInfo(player, dataType, loc);
		
		this.from = BlockUtil.getBlockString(from);
		this.to = type.toString();
	}
	
	public BlockChangeEntry(String cause, DataType type, Location loc, BlockState from, BlockState to) {
		this.setInfo(null, cause, type, loc);
		
		this.from = BlockUtil.getBlockString(from);
		this.to = BlockUtil.getBlockString(to);
	}
	
	public BlockChangeEntry(User user, DataType type, Location loc, BlockState from, BlockState to) {
		this.setInfo(user.getUUID(), user.getName(), type, loc);
		
		this.from = BlockUtil.getBlockString(from);
		this.to = BlockUtil.getBlockString(to);
	}
	
	public BlockChangeEntry(Player player, DataType type, Location loc, String from, String to) {
		this.setInfo(player, type, loc);
		
		this.from = from;
		this.to = to;
	}
	
	public BlockChangeEntry(String cause, DataType type, Location loc, String from, String to) {
		this.setInfo(null, cause, type, loc);
		
		this.from = from;
		this.to = to;
	}
	
	public BlockChangeEntry(Player player, DataType type, Location loc, String from, BlockState to) {
		setInfo(player, type, loc);
		
		this.from = from;
		this.to = BlockUtil.getBlockString(to);
	}
	
	public BlockChangeEntry(UUID uuid, String playerName, DataType type, Location loc, Material blockfrom, int blockfromdata, Material blockto, int blockdatato) {
		this.setInfo(uuid, playerName, type, loc);
		
		if (blockfromdata != 0) {
			this.from = blockfrom.toString() + ":" + blockfromdata;
		} else {
			this.from = blockfrom.toString();
		}
		
		if (blockdatato != 0) {
			this.to = blockto.toString() + ":" + blockdatato;
		} else {
			this.to = blockto.toString();
		}
	}
	
	public BlockChangeEntry(User user, DataType type, Location loc, BlockState from, String to) {
		this.setInfo(user.getUUID(), user.getName(), type, loc);
		
		this.from = BlockUtil.getBlockString(from);
		this.to = to;
	}
	
	@Override
	public String getStringData() {
		if (this.from == null || this.from.equals("0")) {
			return BlockUtil.getBlockStringName(this.to);
		}
		
		return BlockUtil.getBlockStringName(this.from) + " changed to " + BlockUtil.getBlockStringName(this.to);
	}
	
	@Override
	public String getSqlData() {
		return this.from + "-" + this.to;
	}
	
	@Override
	public boolean rollback(Block block) {
		if (this.from == null) {
			block.setType(Material.AIR);
		} else {
			BlockUtil.setBlockString(block, this.from);
		}
		
		return true;
	}
	
	@Override
	public boolean rollbackPlayer(Block block, Player player) {
		if (this.from == null) {
			player.sendBlockChange(block.getLocation(), 0, (byte) 0);
		} else {
			player.sendBlockChange(block.getLocation(), BlockUtil.getMaterialFromString(this.from), BlockUtil.getDataFromString(this.from));
		}
		
		return true;
	}
	
	@Override
	public boolean rebuild(Block block) {
		if (this.to == null) {
			return false;
		} else {
			BlockUtil.setBlockString(block, this.to);
		}
		
		return true;
	}
	
	@Override
	public void interpretSqlData(String data) {
		if (data.indexOf("-") == -1) {
			this.from = null;
			this.to = data;
			
			System.out.print("test");
		} else {
			this.from = data.substring(0, data.indexOf("-"));
			this.to = data.substring(data.indexOf("-") + 1);
		}
	}
}