package uk.co.oliwali.HawkEye.blocks;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import uk.co.oliwali.HawkEye.DataType;
import uk.co.oliwali.HawkEye.database.DataManager;
import uk.co.oliwali.HawkEye.entry.SignEntry;
import uk.co.oliwali.HawkEye.util.BlockUtil;

public class SignBlock implements HawkBlock {
	
	@Override
	public void restore(Block block, Material type, byte data) {
		block.setType(type, false);
		block.setData(data);
	}
	
	@Override
	public void logAttachedBlocks(Block block, Player player, DataType dataType) {
		Block up = block.getRelative(BlockFace.UP);
		HawkBlock hawkBlock = HawkBlockType.getHawkBlock(up.getType());
		
		if (hawkBlock.isTopBlock()) {
			hawkBlock.logAttachedBlocks(up, player, dataType);
			
			if (hawkBlock instanceof SignBlock && DataType.SIGN_BREAK.isLogged()) {
				DataManager.addEntry(new SignEntry(player, DataType.SIGN_BREAK, hawkBlock.getCorrectBlock(up)));
			}
		}

		for (BlockFace face : BlockUtil.faces) {
			Block attch = block.getRelative(face);
			
			if (attch.getType() == Material.WALL_SIGN && DataType.SIGN_BREAK.isLogged()) {
				DataManager.addEntry(new SignEntry(player, DataType.SIGN_BREAK, attch));
			}
		}
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