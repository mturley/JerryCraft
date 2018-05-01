package cx.lynch.JerryCraft;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class PlayerMoveListener implements Listener {
	public JerryCraft plugin;

	public PlayerMoveListener(JerryCraft plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		for (PortalBlock pb : plugin.PortalBlocks) {
			try {
				if (pb.BlockLocation.distance(e.getTo()) < 1.5) {
					if (!pb.ChangeServer) {
						e.getPlayer().teleport(pb.DestinationLocation);
					} else {
						ByteArrayDataOutput out = ByteStreams.newDataOutput();
						out.writeUTF("Connect");
						out.writeUTF(pb.ChangeServerDestination);    
						Player player = e.getPlayer();
						player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
					}
				}
			} catch (IllegalArgumentException ex) {
				/*
				 * Happens because the server lags when tp'ing a player So while they're in
				 * limbo it can't calculate the distance, but they're still technically
				 * colliding with the portal.
				 * 
				 * This exception can be safely ignored.
				 */
			}
		}
	}
}
