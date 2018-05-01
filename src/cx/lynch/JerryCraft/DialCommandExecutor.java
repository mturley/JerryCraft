package cx.lynch.JerryCraft;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class DialCommandExecutor implements CommandExecutor {
	public JerryCraft plugin;

	public DialCommandExecutor(JerryCraft plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("dial")) {
			// Can't use from console.
			if (!(sender instanceof Player)) {
				sender.sendMessage("In-Game only.");
			} else {
				Player player = (Player) sender;
				boolean valid = false;
				int x = 0, y = 0, z = 0;
				String world;

				// Determine what type of command is issued.
				if (args.length == 1) {
					// TODO: A database for warps
					switch (args[0]) {
					case "spawn":
						x = 270;
						y = 65;
						z = 330;
						world = "world";
					}
				} else if (args.length == 2) {
					// Make the portalgun Item & add it to the player's inventory
					ItemStack portalGun = new ItemStack(Material.DIAMOND_HOE);
					portalGun.addEnchantment(Enchantment.VANISHING_CURSE, 1);

					ItemMeta portalGunMeta = portalGun.getItemMeta();
					portalGunMeta.setDisplayName(player.getDisplayName() + "'s Portal Gun");

					List<String> portalGunLore = new ArrayList<String>();
					portalGunLore.add(args[1]);

					portalGunMeta.setLore(portalGunLore);
					portalGun.setItemMeta(portalGunMeta);

					player.getInventory().addItem(portalGun);
					return true;
				} else {
					x = Integer.parseInt(args[0]);
					y = Integer.parseInt(args[1]);
					z = Integer.parseInt(args[2]);
				}

				// Set the destination world
				world = player.getLocation().getWorld().getName();
				if (args.length > 3) {
					world = args[3];
				}

				// Check to see if player has resources.
				if (player.hasPermission("rmportal.free")) {
					valid = true;
				} else if (!valid) {
					for (ItemStack is : player.getInventory().getContents()) {
						if (is != null) {
							if (is.getType() == Material.ENDER_PEARL && is.getAmount() >= 8) {
								is.setAmount(is.getAmount() - 8);
								valid = true;
								break;
							}
						}
					}
				}

				// Tell the player if they need more resources
				if (!valid) {
					player.sendMessage(plugin.chat + "8 Ender Pearls are required to use this.");
					return true;
				}

				// Make the portalgun Item & add it to the player's inventory
				ItemStack portalGun = new ItemStack(Material.IRON_HOE);
				portalGun.addEnchantment(Enchantment.VANISHING_CURSE, 1);

				ItemMeta portalGunMeta = portalGun.getItemMeta();
				portalGunMeta.setDisplayName(player.getDisplayName() + "'s Portal Gun");

				List<String> portalGunLore = new ArrayList<String>();
				portalGunLore.add("Bound to " + x + ", " + y + ", " + z);
				portalGunLore.add("In dimension: " + world);
				portalGunLore.add(x + "," + y + "," + z + "," + world);

				portalGunMeta.setLore(portalGunLore);
				portalGun.setItemMeta(portalGunMeta);

				player.getInventory().addItem(portalGun);
			}
			return true;
		}
		return false;
	}
}
