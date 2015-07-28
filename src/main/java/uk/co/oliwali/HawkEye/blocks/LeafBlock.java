package uk.co.oliwali.HawkEye.blocks;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import uk.co.oliwali.HawkEye.DataType;
import uk.co.oliwali.HawkEye.database.DataManager;
import uk.co.oliwali.HawkEye.entry.BlockEntry;
import uk.co.oliwali.HawkEye.entry.SignEntry;
import uk.co.oliwali.HawkEye.util.BlockUtil;

public class LeafBlock extends BasicBlock { 
	
	@Override
	public void logAttachedBlocks(Block b, Player p, DataType type) {
		for (BlockFace face: BlockUtil.faces) {
			Block attch = b.getRelative(face);
			HawkBlock hawkBlock = HawkBlockType.getHawkBlock(attch.getType());
			
			if (hawkBlock.isAttached()) {
				hawkBlock.logAttachedBlocks(attch, p, type);
				if (hawkBlock instanceof SignBlock && DataType.SIGN_BREAK.isLogged()) {
					DataManager.addEntry(new SignEntry(p, DataType.SIGN_BREAK, hawkBlock.getCorrectBlock(attch)));
				} else if (hawkBlock instanceof VineBlock) {
					DataManager.addEntry(new BlockEntry(p, type, hawkBlock.getCorrectBlock(attch)));
				}
			}
		}
	}
}