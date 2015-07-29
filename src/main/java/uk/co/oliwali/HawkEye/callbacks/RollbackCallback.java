package uk.co.oliwali.HawkEye.callbacks;

import org.bukkit.command.CommandSender;

import uk.co.oliwali.HawkEye.PlayerSession;
import uk.co.oliwali.HawkEye.Rollback;
import uk.co.oliwali.HawkEye.Rollback.RollbackType;
import uk.co.oliwali.HawkEye.database.SearchQuery.SearchError;
import uk.co.oliwali.HawkEye.util.Util;

/**
 * Implementation of BaseCallback for use in rollback commands
 * @author oliverw92
 */
public class RollbackCallback extends BaseCallback {
	
	private final PlayerSession session;
	private final CommandSender sender;
	private final RollbackType type;
	
	public RollbackCallback(PlayerSession session, RollbackType type) {
		this.type = type;
		this.session = session;
		this.sender = session.getSender();
		
		Util.sendMessage(this.sender, "&cSearching for matching results to rollback...");
	}
	
	@Override
	public void execute() {
		this.session.setRollbackResults(results);
		
		new Rollback(this.type, this.session);
	}
	
	@Override
	public void error(SearchError error, String message) {
		Util.sendMessage(this.session.getSender(), message);
	}
}