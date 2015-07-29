package uk.co.oliwali.HawkEye.callbacks;

import org.bukkit.command.CommandSender;

import uk.co.oliwali.HawkEye.LogManager;
import uk.co.oliwali.HawkEye.PlayerSession;
import uk.co.oliwali.HawkEye.database.SearchQuery.SearchError;
import uk.co.oliwali.HawkEye.util.Util;

public class WriteLogCallback extends BaseCallback {
	
	private final PlayerSession session;
	private final CommandSender sender;
	
	public WriteLogCallback(PlayerSession session) {
		this.session = session;
		this.sender = session.getSender();
		
		Util.sendMessage(this.sender, "&cSearching for matching results...");
	}
	
	@Override
	public void execute() {
		this.session.setSearchResults(results);
		LogManager.log(this.session);
	}
	
	@Override
	public void error(SearchError error, String message) {
		Util.sendMessage(this.sender, message);
	}
}