package uk.co.oliwali.HawkEye.callbacks;

import org.bukkit.command.CommandSender;

import uk.co.oliwali.HawkEye.DisplayManager;
import uk.co.oliwali.HawkEye.PlayerSession;
import uk.co.oliwali.HawkEye.database.SearchQuery.SearchError;
import uk.co.oliwali.HawkEye.util.Util;

/**
 * Implementation of BaseCallback for use in search commands
 * @author oliverw92
 */
public class SearchCallback extends BaseCallback {
	
	private final PlayerSession session;
	private final CommandSender sender;
	
	public SearchCallback(PlayerSession session) {
		this.session = session;
		this.sender = session.getSender();
		
		Util.sendMessage(this.sender, "&cSearching for matching results...");
	}
	
	@Override
	public void execute() {
		this.session.setSearchResults(results);
		DisplayManager.displayPage(this.session, 1);
	}
	
	@Override
	public void error(SearchError error, String message) {
		Util.sendMessage(this.sender, message);
	}
}