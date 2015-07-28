package uk.co.oliwali.HawkEye.blocks;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import uk.co.oliwali.HawkEye.DataType;

public class DoorBlock implements HawkBlock {
	
	@Override
	public void restore(Block block, Material type, byte data) {
		if (data == 8 || data == 9) {
			return; //This means the invalid part of the door was logged
		}
		
		block.setType(type, false);
		block.setData(data);
		
		Block up = block.getRelative(BlockFace.UP);
		Block side = null;
		Block oside = null;
		
		if (data == 0) {
			side = block.getRelative(BlockFace.NORTH);
			oside = block.getRelative(BlockFace.SOUTH);
		} else if (data == 1) {
			side = block.getRelative(BlockFace.EAST);
			oside = block.getRelative(BlockFace.WEST);
		} else if (data == 2) {
			side = block.getRelative(BlockFace.SOUTH);
			oside = block.getRelative(BlockFace.NORTH);
		} else {
			side = block.getRelative(BlockFace.WEST);
			oside = block.getRelative(BlockFace.EAST);
		}
		
		Material type2 = side.getType();
		Material oType = oside.getType();
		
		if (type2 == Material.WOODEN_DOOR || type2 == Material.IRON_DOOR_BLOCK) {
			up.setType(type, false);
			up.setData((byte) 9);
		} else if (oType == Material.WOODEN_DOOR || oType == Material.IRON_DOOR_BLOCK) {
			oside.getRelative(BlockFace.UP).setType(type, false);
			oside.getRelative(BlockFace.UP).setData((byte) 9);
			up.setType(type, false);
			up.setData((byte) 8);
		} else {
			up.setType(type, false);
			up.setData((byte) 8);
		}
	}
	
	@Override
	public void logAttachedBlocks(Block block, Player player, DataType dataType) {
		return;
	}
	
	@Override
	public Block getCorrectBlock(Block block) {
		if (block.getData() == (byte) 8 || block.getData() == (byte) 9) { 
			return block.getRelative(BlockFace.DOWN);
		}
		
		return block;
	}
	
	@Override
	public boolean isTopBlock() {
		return true;
	}
	
	@Override
	public boolean isAttached() {
		return false;
	}
}