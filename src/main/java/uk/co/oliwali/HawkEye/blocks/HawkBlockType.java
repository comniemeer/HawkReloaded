package uk.co.oliwali.HawkEye.blocks;

import org.bukkit.Material;

public class HawkBlockType {

	public static final TallPlant tallplant = new TallPlant();
	public static final Default Default = new Default();
	public static final TopBlock topblock = new TopBlock();
	public static final AttachedBlock attachedblock = new AttachedBlock();
	public static final BasicBlock basicblock = new BasicBlock();
	public static final VineBlock vine = new VineBlock();
	public static final BedBlock bed = new BedBlock();
	public static final DoorBlock door = new DoorBlock();
	public static final Plant plant = new Plant();
	public static final Container container = new Container();
	public static final SignBlock sign = new SignBlock();
	public static final PistonBlock piston = new PistonBlock();
	public static final LeafBlock leaf = new LeafBlock();
	public static final DoublePlant doubleplant = new DoublePlant();

	public static HawkBlock getHawkBlock(Material type) {
		switch (type) {
			case LEAVES:
				return leaf;
			case CHEST:
			case TRAPPED_CHEST:
			case FURNACE:
			case BURNING_FURNACE:
			case DISPENSER:
			case DROPPER:
			case HOPPER:
				return container;
			case SIGN_POST:
			case WALL_SIGN:
				return sign;
			case BED_BLOCK:
				return bed;
			case WOODEN_DOOR:
			case IRON_DOOR_BLOCK:
				return door;
			case CROPS:
			case PUMPKIN_STEM:
			case MELON_STEM:
			case CARROT:
			case POTATO:
				return plant;
			case CACTUS:
			case SUGAR_CANE_BLOCK:
				return tallplant;
			case TORCH:
			case LADDER:
			case RAILS:
			case LEVER:
			case REDSTONE_TORCH_OFF:
			case REDSTONE_TORCH_ON:
			case STONE_BUTTON:
			case TRAP_DOOR:
			case COCOA:
			case TRIPWIRE_HOOK:
			case STEP:
			case ACTIVATOR_RAIL:
				return attachedblock;
			case SAPLING:
			case POWERED_RAIL:
			case DETECTOR_RAIL:
			case LONG_GRASS:
			case DEAD_BUSH:
			case YELLOW_FLOWER:
			case RED_ROSE:
			case BROWN_MUSHROOM:
			case RED_MUSHROOM:
			case STONE_PLATE:
			case WOOD_PLATE:
			case SNOW:
			case CAKE_BLOCK:
			case DIODE_BLOCK_OFF:
			case REDSTONE_WIRE:
			case DIODE_BLOCK_ON:
			case TRIPWIRE:
			case FLOWER_POT:
			case GOLD_PLATE:
			case IRON_PLATE:
			case REDSTONE_COMPARATOR_OFF:
			case REDSTONE_COMPARATOR_ON:
			case NETHER_WARTS:
			case CARPET:
			case REDSTONE_COMPARATOR:
			case DIODE:
				return topblock;
			case GLASS:
			case NOTE_BLOCK:
			case WEB:
			case TNT:
			case FIRE:
			case WOOD_STAIRS:
			case WORKBENCH:
			case ICE:
			case JUKEBOX:
			case GLOWSTONE:
			case IRON_FENCE:
			case THIN_GLASS:
			case FENCE_GATE:
			case BRICK_STAIRS:
			case SMOOTH_STAIRS:
			case WATER_LILY:
			case NETHER_FENCE:
			case NETHER_BRICK_STAIRS:
			case ENCHANTMENT_TABLE:
			case BREWING_STAND:
			case CAULDRON:
			case ENDER_PORTAL:
			case ENDER_PORTAL_FRAME:
			case DRAGON_EGG:
			case WOOD_STEP:
			case SANDSTONE_STAIRS:
			case ENDER_CHEST:
			case SPRUCE_WOOD_STAIRS:
			case BIRCH_WOOD_STAIRS:
			case JUNGLE_WOOD_STAIRS:
			case COMMAND:
			case BEACON:
			case SKULL:
			case DAYLIGHT_DETECTOR:
			case QUARTZ_STAIRS:
			case SKULL_ITEM:
			case CAULDRON_ITEM:
			case BREWING_STAND_ITEM:
				return basicblock;
			case VINE:
				return vine;
			case PISTON_STICKY_BASE:
			case PISTON_BASE:
			case PISTON_EXTENSION:
				return piston;
			case DOUBLE_PLANT:
				return doubleplant;
	
			default: return Default;
		}
	}
}