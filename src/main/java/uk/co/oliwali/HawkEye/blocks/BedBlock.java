package uk.co.oliwali.HawkEye.blocks;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import uk.co.oliwali.HawkEye.DataType;

public class BedBlock implements HawkBlock {
	
	@Override
	public void restore(Block block, Material type, byte data) {
		if (data > 7) return;
		
		byte beddata = 0;
		Block bed = null;

		if (data == 0) {
			bed = block.getRelative(BlockFace.SOUTH);
			beddata = 8;
		} else if (data == 1) {
			bed = block.getRelative(BlockFace.WEST);
			beddata = 9;
		} else if (data == 2) {
			bed = block.getRelative(BlockFace.NORTH);
			beddata = 10;
		} else if (data == 3) {
			bed = block.getRelative(BlockFace.EAST);
			beddata = 11;
		}
		
		if (bed != null) {
			bed.setType(type, false);
			bed.setData(beddata);
		}
		
		block.setType(type, false);
		block.setData(data);
	}
	
	@Override
	public void logAttachedBlocks(Block block, Player player, DataType dataType) {
		return;
	}
	
	@Override
	public Block getCorrectBlock(Block block) {
		if (block.getData() > 7) {
			return block.getRelative(getBedFace(block));
		}
		
		return block;
	}
	
	public static BlockFace getBedFace(Block block) {
		int Data = block.getData();
		
		switch(Data){
			case 8: return BlockFace.NORTH;
			case 9: return BlockFace.EAST;
			case 10: return BlockFace.SOUTH;
			case 11: return BlockFace.WEST;
		}
		
		return null;
	}
	
	@Override
	public boolean isTopBlock() {
		return false;
	}
	
	@Override
	public boolean isAttached() {
		return false;
	}
}