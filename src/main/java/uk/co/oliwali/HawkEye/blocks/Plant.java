package uk.co.oliwali.HawkEye.blocks;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import uk.co.oliwali.HawkEye.DataType;

public class Plant implements HawkBlock {
	
	@Override
	public void restore(Block block, Material type, byte data) {
		Block down = block.getRelative(BlockFace.DOWN);
		
		down.setType(Material.SOIL);
		down.setData((byte) 1);
		block.setType(type, false);
		block.setData(data);
	}
	
	@Override
	public void logAttachedBlocks(Block block, Player player, DataType dataType) {
		return;
	}
	
	@Override
	public Block getCorrectBlock(Block block) {
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