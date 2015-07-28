package uk.co.oliwali.HawkEye.blocks;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import uk.co.oliwali.HawkEye.DataType;
import uk.co.oliwali.HawkEye.database.DataManager;
import uk.co.oliwali.HawkEye.entry.BlockEntry;

public class VineBlock implements HawkBlock {
	
	@Override
	public void logAttachedBlocks(Block block, Player player, DataType dataType) {
		block = block.getRelative(BlockFace.DOWN);
		
		while (block.getType() == Material.VINE) {
			Material type = block.getRelative(this.getVineFace(block.getData())).getType();
			
			if (!type.isSolid()) {
				DataManager.addEntry(new BlockEntry(player, dataType, block));
			} else {
				break;
			}
			
			block = block.getRelative(BlockFace.DOWN);
		}
	}
	
	@Override
	public boolean isAttached() {
		return true;
	}
	
	public BlockFace getVineFace(int data) {
		switch(data) {
		case 1: return BlockFace.SOUTH;
		case 8: return BlockFace.EAST;
		case 4: return BlockFace.NORTH;
		case 2: return BlockFace.WEST;
		default: return BlockFace.NORTH;
		}
	}
	
	@Override
	public void restore(Block block, Material type, byte data) {
		block.setType(type, false);
		block.setData(data);
	}
	
	@Override
	public Block getCorrectBlock(Block block) {
		return block;
	}
	
	@Override
	public boolean isTopBlock() {
		return false;
	}
}