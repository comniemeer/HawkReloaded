package uk.co.oliwali.HawkEye;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.bukkit.command.CommandSender;

import uk.co.oliwali.HawkEye.entry.DataEntry;
import uk.co.oliwali.HawkEye.util.Config;
import uk.co.oliwali.HawkEye.util.Util;

public class LogManager {
	
	public static void log(PlayerSession session) {
		CommandSender sender = session.getSender();
		List<DataEntry> results = session.getSearchResults();
		
		if (results == null || results.size() == 0) {
			Util.sendMessage(sender, "&cNo results found");
			return;
		}
		
		if (results.size() > Config.MaxLog) {
			Util.sendMessage(sender, "&cMax log results: " + Config.MaxLog);
			return;
		}
		
		String t = new SimpleDateFormat("MM-dd_HH-mm-ss").format(Calendar.getInstance().getTime());
		String name = "Log-" + t + ".txt";
		Util.sendMessage(sender, "&7Attempting to write &c" + results.size() + " &7results to &c" + name + "&7!");
		BufferedWriter writer = null;
		int i = 0;
		
		try {
			writer = new BufferedWriter(new FileWriter(new File(HawkEye.instance.getDataFolder(), name)));
			
			writer.write("|------(Log By " + sender.getName() + ")------|" + "\n");
			
			for (DataEntry entry : results) {
				String uuidComponent = (entry.getPlayerUUID() == null ? "" :  ", " + entry.getPlayerUUID().toString());
				
				writer.write("ID:" + entry.getDataId() + ", " + entry.getTimestamp() + ", " + entry.getPlayerName() + uuidComponent + ", " + entry.getType().getConfigName() + "\n");
				writer.write("Loc: " + entry.getWorld() + "," + entry.getX() + "," + entry.getY() + "," + entry.getZ() + " Data: " + entry.getStringData() + "\n");
				writer.write("--"+"\n");
				
				i++;
			}
		} catch (IOException e) {
			Util.warning(e.getMessage());
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					Util.warning(e.getMessage());
				}
			}
		}
		
		Util.sendMessage(sender, "&7Successfully wrote &c" + i + " &7results to &c" + name + "&7!");
	}
}