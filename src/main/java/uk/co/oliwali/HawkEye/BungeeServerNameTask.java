package uk.co.oliwali.HawkEye;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;

import uk.co.oliwali.HawkEye.util.Config;
import uk.co.oliwali.HawkEye.util.Util;

public class BungeeServerNameTask extends BukkitRunnable implements PluginMessageListener {
	
	@Override
	public void run() {
		if(Bukkit.getOnlinePlayers().size() > 0) {
			if (this.requestServerName() != null) {
				Util.debug("Server name request sent, task is shutting down.");
				this.cancel();
			}
		}
	}
	
	public String requestServerName() {
		if (!Config.BungeeCord) {
			return "";
		}
		
		if (Config.ServerName != null && !Config.ServerName.isEmpty()) {
			return Config.ServerName;
		} else {
			Player player = Bukkit.getOnlinePlayers().iterator().next(); // This will error if no player is online.
			ByteArrayOutputStream b = new ByteArrayOutputStream();
			DataOutputStream out = new DataOutputStream(b);
			
			try {
				out.writeUTF("GetServer");
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if (player != null) {
				player.sendPluginMessage(HawkEye.instance, "BungeeCord", b.toByteArray());
			} else {
				return null;
			}
		}
		
		return "";
	}
	
	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        try {
    		DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
    		String subChannel = in.readUTF();
    		
    		if (subChannel.equals("GetServer")) {
    			Config.ServerName = in.readUTF();
    			
				HawkEye.instance.getConfig().set("general.servername", Config.ServerName);
    			HawkEye.instance.saveConfig();
    			
				Util.info("Server name updated to '" + Config.ServerName + "'.");
            }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}