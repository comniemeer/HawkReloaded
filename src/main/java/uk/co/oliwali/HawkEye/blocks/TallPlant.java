package uk.co.oliwali.HawkEye.blocks;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import uk.co.oliwali.HawkEye.DataType;
import uk.co.oliwali.HawkEye.database.DataManager;
import uk.co.oliwali.HawkEye.entry.BlockEntry;

public class TallPlant extends Default {
	
	@Override
	public void logAttachedBlocks(Block block, Player player, DataType dataType) {
		block = block.getRelative(BlockFace.UP);
		
		while (HawkBlockType.getHawkBlock(block.getType()).equals(this)) {
			DataManager.addEntry(new BlockEntry(player, dataType, block));
			
			block = block.getRelative(BlockFace.UP);
		}
	}
	
	@Override
	public boolean isTopBlock() {
		return true;
	}
}