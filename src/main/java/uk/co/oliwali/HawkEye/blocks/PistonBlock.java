package uk.co.oliwali.HawkEye.blocks;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import uk.co.oliwali.HawkEye.DataType;

public class PistonBlock implements HawkBlock {
	
	@Override
	public void restore(Block block, Material type, byte data) {
		if (type == Material.PISTON_EXTENSION) {
			block = block.getRelative(getPistonFromExtension(data));
		}
		
		switch(data) { //Check data just to be sure they will be placed correctly! 
			case 10:
			case 2:
				block.setType(type, true);
				block.setData((byte) 2);
				break;
			case 4:
			case 12:
				block.setType(type, true);
				block.setData((byte) 4);
				break;
			case 3:
			case 11:
				block.setType(type, true);
				block.setData((byte) 3);
				break;
			case 13:
			case 5:
				block.setType(type, true);
				block.setData((byte) 5);
				break;
			case 8:
			case 0:
				block.setType(type, true);
				block.setData((byte) 0);
				break;
			case 1:
			case 9:
				block.setType(type, true);
				block.setData((byte) 1);
				break;
			default: return;
		}
	}
	
	@Override
	public void logAttachedBlocks(Block block, Player player, DataType dataType) {
		return;
	}
	
	@Override
	public Block getCorrectBlock(Block block) {
		if (block.getType() == Material.PISTON_EXTENSION) {
			return block.getRelative(getPistonFromExtension(block.getData()));
		}
		
		return block;
	}
	
	@Override
	public boolean isTopBlock() {
		return false;
	}

	@Override
	public boolean isAttached() {
		return false;
	}
	
	public BlockFace getPistonFromExtension(byte data) {
		switch(data) {
			case 10:
			case 2: return BlockFace.SOUTH;
			case 4:
			case 12: return BlockFace.EAST;
			case 3:
			case 11: return BlockFace.NORTH;
			case 13:
			case 5: return BlockFace.WEST;
			case 8:
			case 0: return BlockFace.UP;
			case 1:
			case 9: return BlockFace.DOWN;
			default: return BlockFace.EAST;
		}
	}
}