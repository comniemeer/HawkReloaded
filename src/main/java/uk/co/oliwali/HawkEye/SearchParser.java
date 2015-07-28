package uk.co.oliwali.HawkEye;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import uk.co.oliwali.HawkEye.util.Config;
import uk.co.oliwali.HawkEye.util.Util;

import com.sk89q.worldedit.bukkit.selections.Selection;

/**
 * Class for parsing HawkEye arguments ready to be used by an instance of {@SearchQuery}
 * @author oliverw92
 */
public class SearchParser {
	
	public CommandSender player = null;
	public List<String> players = new ArrayList<String>();
	public Vector loc = null;
	public Vector minLoc = null;
	public Vector maxLoc = null;
	public Integer radius = null;
	public List<DataType> actions = new ArrayList<DataType>();
	public String[] worlds = null;
	public String dateFrom = null;
	public String dateTo = null;
	public String[] filters = null;
	
	public SearchParser() { }
	
	public SearchParser(CommandSender player) {
		this.player = player;
	}
	
	public SearchParser(CommandSender player, int radius) {
		this.player = player;
		this.radius = radius;
		
		this.parseLocations();
	}
	
	public SearchParser(CommandSender player, List<String> args) throws IllegalArgumentException {
		this.player = player;
		
		String lastParam = "";
		boolean paramSet = false;
		boolean worldedit = false;
		
		for (int i = 0; i < args.size(); i++) {
			String arg = args.get(i);
			
			if (arg.isEmpty()) {
				continue;
			}
			
			if (!paramSet) {
				if (arg.length() < 2) {
					throw new IllegalArgumentException("Invalid argument format: &7" + arg);
				}
				
				if (!arg.substring(1,2).equals(":")) {
					if (arg.contains(":")) {
						throw new IllegalArgumentException("Invalid argument format: &7" + arg);
					}
					
					// No arg specified, treat as player
					this.players.add(arg);
					continue;
				}
				
				lastParam = arg.substring(0, 1).toLowerCase();
				paramSet = true;
				
				if (arg.length() == 2) {
					if (i == (args.size() - 1)) {
						// No values specified
						throw new IllegalArgumentException("Invalid argument format: &7" + arg);
					} else {
						// User put a space between the colon and value
						continue;
					}
				}
				
				// Get values out of argument
				arg = arg.substring(2);
			}
			
			if (paramSet) {
				if (arg.isEmpty()) {
					throw new IllegalArgumentException("Invalid argument format: &7" + lastParam + ":");
				}
				
				String[] values = arg.split(",");
				
				if (lastParam.equals("p")) {
					// Players
					for (String p : values) {
						this.players.add(p.toLowerCase());
					}
				} else if (lastParam.equals("w")) {
					// Worlds
					this.worlds = values;
				} else if (lastParam.equals("f")) {
					// Filters
					if (this.filters != null) {
						this.filters = Util.concat(filters, values);
					} else {
						this.filters = values;
					}
				} else if (lastParam.equals("b")) {
					// Blocks
					for (int j = 0; j < values.length; j++) {
						if (Material.getMaterial(values[j]) != null) {
							values[j] = Material.getMaterial(values[j]).toString();
						}
					}
				} else if (lastParam.equals("a")) {
					// Actions
					for (String value : values) {
						DataType type = DataType.fromName(value);
						
						if (type == null) {
							throw new IllegalArgumentException("Invalid action supplied: &7" + value);
						}
						
						if (!Util.hasPerm(player, "search." + type.getConfigName().toLowerCase())) {
							throw new IllegalArgumentException("You do not have permission to search for: &7" + type.getConfigName());
						}
						
						this.actions.add(type);
					}
				} else if (lastParam.equals("s")) {
					// EditSpeed
					if (!Util.isInteger(values[0])) {
						throw new IllegalArgumentException("Invalid edit-speed supplied: &7" + values[0]);
					}
					
					int speed = Integer.parseInt(values[0]);
					
					if (speed > Config.MaxEditSpeed) {
						throw new IllegalArgumentException("Max edit-speed: &7" + Config.MaxEditSpeed);
					}
					
					SessionManager.getSession(player).setEditSpeed(speed);
				} else if (lastParam.equals("l") && player instanceof Player) {
					// Location
					if (values[0].equalsIgnoreCase("here")) {
						this.loc = ((Player) player).getLocation().toVector();
					} else {
						this.loc = new Vector();
						this.loc.setX(Integer.parseInt(values[0]));
						this.loc.setY(Integer.parseInt(values[1]));
						this.loc.setZ(Integer.parseInt(values[2]));
					}
				} else if (lastParam.equals("r") && player instanceof Player) {
					// Radius
					if (!Util.isInteger(values[0])) {
						if ((values[0].equalsIgnoreCase("we") || values[0].equalsIgnoreCase("worldedit")) && HawkEye.worldEdit != null) {
							Selection sel = HawkEye.worldEdit.getSelection((Player) player);
							int lRadius = (int) Math.ceil(sel.getLength() / 2);
							int wRadius = (int) Math.ceil(sel.getWidth() / 2);
							int hRadius = (int) Math.ceil(sel.getHeight() / 2);
							
							if (Config.MaxRadius != 0 && (lRadius > Config.MaxRadius || wRadius > Config.MaxRadius || hRadius > Config.MaxRadius)) {
								throw new IllegalArgumentException("Selection too large, max radius: &7" + Config.MaxRadius);
							}
							
							worldedit = true;
							
							this.minLoc = new Vector(sel.getMinimumPoint().getX(), sel.getMinimumPoint().getY(), sel.getMinimumPoint().getZ());
							this.maxLoc = new Vector(sel.getMaximumPoint().getX(), sel.getMaximumPoint().getY(), sel.getMaximumPoint().getZ());
						} else if (values[0].equals("*")) {
							if (!player.hasPermission("hawkeye.override")) {
								throw new IllegalArgumentException("You do not have permission to override the MaxRadius!");
							}
							
							this.radius = -1;
						} else {
							throw new IllegalArgumentException("Invalid radius supplied: &7" + values[0]);
						}
					} else {
						this.radius = Integer.parseInt(values[0]);
						
						if (Config.MaxRadius != 0 && this.radius > Config.MaxRadius) {
							throw new IllegalArgumentException("Radius too large, max allowed: &7" + Config.MaxRadius);
						}
						
						if (this.radius < 0) {
							throw new IllegalArgumentException("Radius too small");
						}
					}
				} else if (lastParam.equals("t")) {
					//Time
					int type = 2;
					boolean isTo = false;
					
					for (int j = 0; j < arg.length(); j++) {
						String c = arg.substring(j, j + 1);
						
						if (!Util.isInteger(c)) {
							if (c.equals("m") || c .equals("s") || c.equals("h") || c.equals("d") || c.equals("w")) {
								type = 0;
							}
							
							if (c.equals("-") || c.equals(":")) {
								type = 1;
							}
						}
					}
					
					// If the time is in the format '0w0d0h0m0s'
					if (type == 0) {
						int weeks = 0;
						int days = 0;
						int hours = 0;
						int mins = 0;
						int secs = 0;
						
						String nums = "";
						
						for (int j = 0; j < values[0].length(); j++) {
							String c = values[0].substring(j, j + 1);
							
							if (c.equals("!")) { //If the number has a ! infront of it the time inverts
								c = values[0].substring(j, j+2).replace("!", "");
								isTo = true;
							} else {
								if (Util.isInteger(c)) {
									nums += c;
									continue;
								}
								
								int num = Integer.parseInt(nums);
								
								if (c.equals("w")) {
									weeks = num;
								} else if (c.equals("d")) {
									days = num;
								} else if (c.equals("h")) {
									hours = num;
								} else if (c.equals("m")) {
									mins = num;
								} else if (c.equals("s")) {
									secs = num;
								} else {
									throw new IllegalArgumentException("Invalid time measurement: &7" + c);
								}
								
								nums = "";
							}
						}
						
						Calendar cal = Calendar.getInstance();
						
						cal.add(Calendar.WEEK_OF_YEAR, -1 * weeks);
						cal.add(Calendar.DAY_OF_MONTH, -1 * days);
						cal.add(Calendar.HOUR, -1 * hours);
						cal.add(Calendar.MINUTE, -1 * mins);
						cal.add(Calendar.SECOND, -1 * secs);
						
						SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						
						if (isTo) {
							this.dateTo = form.format(cal.getTime());
						} else {
							this.dateFrom = form.format(cal.getTime());
						}
					} else if (type == 1) {
						// If the time is in the format 'yyyy-MM-dd HH:mm:ss'
						if (values.length == 1) {
							SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
							this.dateFrom = form.format(Calendar.getInstance().getTime()) + " " + values[0];
						}
						
						if (values.length >= 2) {
							this.dateFrom = values[0] + " " + values[1];
						}
						
						if (values.length == 4) {
							this.dateTo = values[2] + " " + values[3];
						}
					} else if (type == 2) {
						// Invalid time format
						throw new IllegalArgumentException("Invalid time format!");
					}
				} else {
					throw new IllegalArgumentException("Invalid parameter supplied: &7" + lastParam);
				}
				
				paramSet = false;
			}
		}
		
		// Sort out locations
		if (!worldedit) {
			this.parseLocations();
		}
	}
	
	/**
	 * Formats min and max locations if the radius is set
	 */
	public void parseLocations() {
		if (!(this.player instanceof Player)) {
			return;
		}
		
		// Check if there is a max radius
		if (this.radius == null && Config.MaxRadius != 0) {
			this.radius = Config.MaxRadius;
		}
		
		// If the radius is set we need to format the min and max locations
		if (this.radius != null && this.radius > 0) {
			// Check if location and world are supplied
			if (this.loc == null) {
				this.loc = ((Player) this.player).getLocation().toVector();
			}
			
			if (this.worlds == null) {
				this.worlds = new String[] { ((Player) this.player).getWorld().getName() };
			}
			
			// Format min and max
			this.minLoc = new Vector(this.loc.getX() - this.radius, this.loc.getY() - this.radius, this.loc.getZ() - this.radius);
			this.maxLoc = new Vector(this.loc.getX() + this.radius, this.loc.getY() + this.radius, this.loc.getZ() + this.radius);
		}
	}
}