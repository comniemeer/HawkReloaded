package uk.co.oliwali.HawkEye;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import uk.co.oliwali.HawkEye.commands.BaseCommand;
import uk.co.oliwali.HawkEye.commands.DeleteCommand;
import uk.co.oliwali.HawkEye.commands.HelpCommand;
import uk.co.oliwali.HawkEye.commands.HereCommand;
import uk.co.oliwali.HawkEye.commands.InfoCommand;
import uk.co.oliwali.HawkEye.commands.PageCommand;
import uk.co.oliwali.HawkEye.commands.PreviewApplyCommand;
import uk.co.oliwali.HawkEye.commands.PreviewCancelCommand;
import uk.co.oliwali.HawkEye.commands.PreviewCommand;
import uk.co.oliwali.HawkEye.commands.RebuildCommand;
import uk.co.oliwali.HawkEye.commands.ReloadCommand;
import uk.co.oliwali.HawkEye.commands.RollbackCommand;
import uk.co.oliwali.HawkEye.commands.SearchCommand;
import uk.co.oliwali.HawkEye.commands.ToolBindCommand;
import uk.co.oliwali.HawkEye.commands.ToolCommand;
import uk.co.oliwali.HawkEye.commands.ToolResetCommand;
import uk.co.oliwali.HawkEye.commands.TptoCommand;
import uk.co.oliwali.HawkEye.commands.UndoCommand;
import uk.co.oliwali.HawkEye.commands.WriteLogCommand;
import uk.co.oliwali.HawkEye.database.ConnectionManager;
import uk.co.oliwali.HawkEye.database.DataManager;
import uk.co.oliwali.HawkEye.listeners.MonitorBlockListener;
import uk.co.oliwali.HawkEye.listeners.MonitorEntityListener;
import uk.co.oliwali.HawkEye.listeners.MonitorFallingBlockListener;
import uk.co.oliwali.HawkEye.listeners.MonitorLiquidFlow;
import uk.co.oliwali.HawkEye.listeners.MonitorPlayerListener;
import uk.co.oliwali.HawkEye.listeners.MonitorWorldEditListener;
import uk.co.oliwali.HawkEye.listeners.MonitorWorldListener;
import uk.co.oliwali.HawkEye.listeners.ToolListener;
import uk.co.oliwali.HawkEye.util.Config;
import uk.co.oliwali.HawkEye.util.Util;
import uk.co.oliwali.HawkEye.worldedit.WESessionFactory;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;

public class HawkEye extends JavaPlugin {
	
	public String name;
	public String version;
	public Config config;
	public static Server server;
	public static HawkEye instance;
	public MonitorBlockListener monitorBlockListener = new MonitorBlockListener(this);
	public MonitorEntityListener monitorEntityListener = new MonitorEntityListener(this);
	public MonitorPlayerListener monitorPlayerListener = new MonitorPlayerListener(this);
	public MonitorWorldListener monitorWorldListener = new MonitorWorldListener(this);
	public MonitorFallingBlockListener monitorFBListerner = new MonitorFallingBlockListener(this);
	public MonitorWorldEditListener monitorWorldEditListener = new MonitorWorldEditListener();
	public MonitorLiquidFlow monitorLiquidFlow;
	public ToolListener toolListener = new ToolListener();
	private DataManager dbmanager;
	public static List<BaseCommand> commands = new ArrayList<BaseCommand>();
	public static HashMap<String, HashMap<String,Integer>> InvSession = new HashMap<String, HashMap<String,Integer>>();
	public static WorldEditPlugin worldEdit = null;
	
	/**
	 * Safely shuts down HawkEye
	 */
	@Override
	public void onDisable() {
		if (this.dbmanager != null) {
			this.dbmanager.run();
			
			if (!ConnectionManager.getConnections().isEmpty()) {
				while (this.dbmanager.isInsertThreadBusy() || ConnectionManager.areConsOpen()) {
					Util.debug("Not ready");
					
					if (DataManager.getQueue().size() != 0) {
						this.dbmanager.run();
					}
				}
			}
		}
		
		DataManager.close();
		Util.info("Version " + version + " disabled!");
	}
	
	/**
	 * Starts up HawkEye initiation process
	 */
	@Override
	public void onEnable() {
		PluginManager pm = getServer().getPluginManager();
		
		// Check bukkit dependencies
		try {
			Class.forName("org.bukkit.event.hanging.HangingPlaceEvent");
		} catch (ClassNotFoundException ex) {
			Util.info("HawkEye requires CraftBukkit 1.4+ to run properly!");
			pm.disablePlugin(this);
			return;
		}
		
		HawkEye.instance = this;
		HawkEye.server = getServer();
		this.name = this.getDescription().getName();
		this.version = this.getDescription().getVersion();
		
		Util.info("Starting HawkEye " + this.version + " initiation process...");
		
		// Load config
		this.config = new Config(this);
		
		new SessionManager();
		
		// Initiate database connection
		try {
			this.dbmanager = new DataManager(this);
			this.getServer().getScheduler().runTaskTimerAsynchronously(this, dbmanager, Config.LogDelay * 20, Config.LogDelay * 20);
		} catch (Exception e) {
			Util.severe("Error initiating HawkEye database connection, disabling plugin");
			pm.disablePlugin(this);
			return;
		}
		
		this.checkDependencies(pm);
		
		// This must be created while the plugin is loading as the constructor is dependent
		this.monitorLiquidFlow = new MonitorLiquidFlow(this);
		
		this.registerListeners(pm);
		this.registerCommands();
		
		if (Config.BungeeCord) {
			if (Config.ServerName == null || Config.ServerName.isEmpty()) {
				Util.info("BungeeCord server name fetching enabled, and server name is not set yet. Scheduling a name-update task.");
				
				BungeeServerNameTask bungeeServerNameTask = new BungeeServerNameTask();
				
				bungeeServerNameTask.runTaskTimer(this, 10 * 20L, 15 * 20L);
				
				this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
				this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", bungeeServerNameTask);
			}
		}
		
		Util.info("Version " + version + " enabled!");
	}
	
	/**
	 * Checks if required plugins are loaded
	 * @param pm PluginManager
	 */
	private void checkDependencies(PluginManager pm) {
		Plugin we = pm.getPlugin("WorldEdit");
		if (we != null) worldEdit = (WorldEditPlugin)we;
	}
	
	/**
	 * Registers event listeners
	 * @param pm PluginManager
	 */
	public void registerListeners(PluginManager pm) {
		// Register events
		this.monitorBlockListener.registerEvents();
		this.monitorPlayerListener.registerEvents();
		this.monitorEntityListener.registerEvents();
		this.monitorWorldListener.registerEvents();
		this.monitorFBListerner.registerEvents();
		this.monitorLiquidFlow.registerEvents();
		this.monitorLiquidFlow.startCacheCleaner();
		
		pm.registerEvents(toolListener, this);
		
		if (HawkEye.worldEdit != null)  {
			if (DataType.SUPER_PICKAXE.isLogged()) {
				pm.registerEvents(this.monitorWorldEditListener, this); //Yes we still need to log superpick!
			}
			
			// This makes sure we OVERRIDE any other plugin that tried to register a EditSessionFactory!
			if (DataType.WORLDEDIT_BREAK.isLogged() || DataType.WORLDEDIT_PLACE.isLogged()) {
				Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
					@Override
					public void run() {
						try {
							Class.forName("com.sk89q.worldedit.extent.logging.AbstractLoggingExtent");
							
							new WESessionFactory();
						} catch (ClassNotFoundException ex) {
							Util.warning("[!] Failed to initialize WorldEdit logging [!]");
							Util.warning("[!] Please upgrade WorldEdit to 6.0+       [!]");
						}
					}
				}, 2L);
			}
		}
	}
	
	/**
	 * Registers commands for use by the command manager
	 */
	private void registerCommands() {
		// Add commands
		HawkEye.commands.add(new HelpCommand());
		HawkEye.commands.add(new ToolBindCommand());
		HawkEye.commands.add(new ToolResetCommand());
		HawkEye.commands.add(new ToolCommand());
		HawkEye.commands.add(new SearchCommand());
		HawkEye.commands.add(new PageCommand());
		HawkEye.commands.add(new TptoCommand());
		HawkEye.commands.add(new HereCommand());
		HawkEye.commands.add(new PreviewApplyCommand());
		HawkEye.commands.add(new PreviewCancelCommand());
		HawkEye.commands.add(new PreviewCommand());
		HawkEye.commands.add(new RollbackCommand());
		HawkEye.commands.add(new UndoCommand());
		HawkEye.commands.add(new RebuildCommand());
		HawkEye.commands.add(new DeleteCommand());
		HawkEye.commands.add(new InfoCommand());
		HawkEye.commands.add(new WriteLogCommand());
		HawkEye.commands.add(new ReloadCommand());
	}
	
	/**
	 * Command manager for HawkEye
	 * @param sender - {@link CommandSender}
	 * @param cmd - {@link Command}
	 * @param commandLabel - String
	 * @param args[] - String[]
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String args[]) {
		if (cmd.getName().equalsIgnoreCase("hawk")) {
			if (args.length == 0) {
				args = new String[] { "help" };
			}
			
			outer: for (BaseCommand command : commands.toArray(new BaseCommand[0])) {
				String[] cmds = command.name.split(" ");
				
				for (int i = 0; i < cmds.length; i++) {
					if (i >= args.length || !cmds[i].equalsIgnoreCase(args[i])) {
						continue outer;
					}
				}
				
				return command.run(this, sender, args, commandLabel);
			}
			
			HawkEye.commands.get(0).run(this, sender, args, commandLabel);
			return true;
		}
		
		return false;
	}
}