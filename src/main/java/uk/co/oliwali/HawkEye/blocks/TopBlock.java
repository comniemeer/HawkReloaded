package uk.co.oliwali.HawkEye.blocks;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import uk.co.oliwali.HawkEye.DataType;

public class TopBlock implements HawkBlock {
	
	@Override
	public void restore(Block block, Material type, byte data) {
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
		return true;
	}
}