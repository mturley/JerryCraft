package cx.lynch.JerryCraft;

import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

public class ChunkLoadListener implements Listener {
	public JerryCraft plugin;

	public ChunkLoadListener(JerryCraft plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onEnterChunk(ChunkLoadEvent e) {
		Chunk loadedChunk = e.getChunk();
		plugin.getLogger().info("Loaded Chunk: " + loadedChunk.toString());
	}
}
