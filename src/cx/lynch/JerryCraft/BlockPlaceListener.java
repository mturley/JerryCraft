package cx.lynch.JerryCraft;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener implements Listener {
	private JerryCraft plugin;

	public BlockPlaceListener(JerryCraft plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) throws SQLException {
		Block block = e.getBlock();
		Chunk chunk = block.getChunk();
		
		if ((e.getBlock().getType().equals(Material.IRON_BLOCK))
				&& (e.getBlock().getLocation().add(0, 1, 0).getBlock().getType().equals(Material.REDSTONE_BLOCK))) {
			plugin.CableBlocks.add(e.getBlock().getLocation());
			e.getPlayer().sendMessage(plugin.chat + "This should stop other ricks from using their portal gun here...");
		} else if ((e.getBlock().getType().equals(Material.REDSTONE_BLOCK))
				&& (e.getBlock().getLocation().add(0, -1, 0).getBlock().getType().equals(Material.IRON_BLOCK))) {
			plugin.CableBlocks.add(e.getBlock().getLocation().add(0, -1, 0));
			e.getPlayer().sendMessage(plugin.chat + "This should stop other ricks from using their portal gun here...");
		}
		
		/* Infinite Universe Framework, currently in progress */
		int chunkx = chunk.getX();
		int chunkz = chunk.getZ();

		int blockx = block.getX();
		int blocky = block.getY();
		int blockz = block.getZ();

		int chunk_id;

		String test = "select chunk_id from `jerrytest`.`chunkledger` where chunk_x = " + chunkx + " and chunk_z = "
				+ chunkz + ";";
		// server.getLogger().info(test);

		ResultSet chunkFindInitial = plugin.statement.executeQuery(test);

		if (!chunkFindInitial.next()) {
			// For whatever reason, return_generated_keys always returns 1/true...
			String qnewchunk = "insert into `jerrytest`.`chunkledger` values (null," + Integer.toString(chunkx)
			+ ", " + Integer.toString(chunkz) + ", CURRENT_TIMESTAMP);";
			PreparedStatement stmt;
			stmt = plugin.connection.prepareStatement(qnewchunk, Statement.RETURN_GENERATED_KEYS);
			stmt.executeUpdate();
			ResultSet x = stmt.getGeneratedKeys();
			x.next();
			chunk_id = x.getInt(1);
			stmt.close();
		} else {
			chunk_id = chunkFindInitial.getInt("chunk_id");
		}

		plugin.getLogger().info(Integer.toString(chunk_id));

		int update = plugin.statement.executeUpdate("insert into `jerrytest`.`blockledger` values (null, " + chunk_id + ", "
				+ blockx + ", " + blocky + ", " + blockz + ", \"" + block.getType().toString() + "\");");
	}
}
