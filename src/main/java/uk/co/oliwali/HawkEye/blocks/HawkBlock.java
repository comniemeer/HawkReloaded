package uk.co.oliwali.HawkEye.blocks;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import uk.co.oliwali.HawkEye.DataType;

public interface HawkBlock {
	
	public void restore(Block block, Material type, byte data);
	
	public Block getCorrectBlock(Block block);
	
	public void logAttachedBlocks(Block block, Player player, DataType dataType);
	
	public boolean isTopBlock();
	
	public boolean isAttached();
}