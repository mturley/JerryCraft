package cx.lynch.JerryCraft;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PortalGunListener implements Listener {
	private JerryCraft plugin;

	public PortalGunListener(JerryCraft plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onUse(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		ItemStack i = p.getInventory().getItemInMainHand();

		if (i.getType() == Material.IRON_HOE && i.getEnchantments().get(Enchantment.VANISHING_CURSE) != null) {

			ItemMeta meta = i.getItemMeta();
			List<String> lore = meta.getLore();

			String[] portalData;
			try {
				portalData = lore.get(2).split(",");
			} catch (Exception ex) {
				p.sendMessage(plugin.chat + "Why did you put that on a hoe, man?");
				return;
			}

			int x = Integer.parseInt(portalData[0]);
			int y = Integer.parseInt(portalData[1]);
			int z = Integer.parseInt(portalData[2]);
			String w = portalData[3];

			i.setAmount(0);
			Location destination = new Location(plugin.getServer().getWorld(w), x, y, z);

			for (Location cable : plugin.CableBlocks) {
				if (destination.distance(cable) < 100) {
					if (!p.hasPermission("JerryCraft.dial.anywhere")) {
						p.sendMessage(plugin.chat
								+ "Another rick has set something up to block me from teleporting there...");
						return;
					}
				}
			}

			Block targetBlock = p.getTargetBlock(null, 100);
			Location playerLocation = p.getEyeLocation();
			double blockDistance = targetBlock.getLocation().distance(playerLocation);
			Location targetLocation = playerLocation.add(playerLocation.getDirection().multiply(blockDistance))
					.subtract(playerLocation.getDirection());

			plugin.PortalBlocks.add(new PortalBlock(targetLocation, destination, plugin));
		} else if (i.getType() == Material.DIAMOND_HOE && i.getEnchantments().get(Enchantment.VANISHING_CURSE) != null) {
			ItemMeta meta = i.getItemMeta();
			List<String> lore = meta.getLore();
			String destinationServer = lore.get(0);
			
			i.setAmount(0);
			
			Block targetBlock = p.getTargetBlock(null, 100);
			Location playerLocation = p.getEyeLocation();
			double blockDistance = targetBlock.getLocation().distance(playerLocation);
			Location targetLocation = playerLocation.add(playerLocation.getDirection().multiply(blockDistance))
					.subtract(playerLocation.getDirection());
			
			plugin.PortalBlocks.add(new PortalBlock(targetLocation, destinationServer, plugin));
			
		}
	}
}
