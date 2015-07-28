package uk.co.oliwali.HawkEye.blocks;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import uk.co.oliwali.HawkEye.DataType;
import uk.co.oliwali.HawkEye.database.DataManager;
import uk.co.oliwali.HawkEye.entry.BlockEntry;
import uk.co.oliwali.HawkEye.entry.SignEntry;
import uk.co.oliwali.HawkEye.util.BlockUtil;

public class Default implements HawkBlock {
	
	@Override
	public void restore(Block block, Material type, byte data) {
		block.setType(type, false);
		block.setData(data);
	}
	
	@Override
	public void logAttachedBlocks(Block block, Player player, DataType dataType) {
		Block topBlock = block.getRelative(BlockFace.UP);
		HawkBlock hawkBlock = HawkBlockType.getHawkBlock(topBlock.getType());
		
		if (hawkBlock.isTopBlock()) {
			hawkBlock.logAttachedBlocks(topBlock, player, dataType);
			
			if (hawkBlock instanceof SignBlock && DataType.SIGN_BREAK.isLogged()) {
				DataManager.addEntry(new SignEntry(player, DataType.SIGN_BREAK, hawkBlock.getCorrectBlock(topBlock)));
			} else {
				DataManager.addEntry(new BlockEntry(player, dataType, hawkBlock.getCorrectBlock(topBlock)));
			}
		}
		
		for (BlockFace face: BlockUtil.faces) {
			Block attch = block.getRelative(face);
			hawkBlock = HawkBlockType.getHawkBlock(attch.getType());
			
			if (hawkBlock.isAttached() && BlockUtil.isAttached(block, attch)) {
				hawkBlock.logAttachedBlocks(attch, player, dataType);
				
				if (attch.getType() == Material.WALL_SIGN && DataType.SIGN_BREAK.isLogged()) {
					DataManager.addEntry(new SignEntry(player, DataType.SIGN_BREAK, hawkBlock.getCorrectBlock(attch)));
				} else {
					DataManager.addEntry(new BlockEntry(player, dataType, hawkBlock.getCorrectBlock(attch)));
				}
			}
		}
	}
	
	@Override
	public Block getCorrectBlock(Block block) {
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
}