package uk.co.oliwali.HawkEye.undoData;

import org.bukkit.Material;
import org.bukkit.block.BlockState;

import uk.co.oliwali.HawkEye.blocks.HawkBlockType;

public class UndoBlock {
	
	protected BlockState state;
	
	public UndoBlock(BlockState state) {
		this.state = state;
	}
	
	public void undo() {
		if (this.state != null) {
			final Material type = this.state.getType();
			final byte data = this.state.getData().getData();
			
			HawkBlockType.getHawkBlock(type).restore(this.state.getBlock(), type, data);
		}
	}
	
	public BlockState getState() {
		return this.state;
	}
}