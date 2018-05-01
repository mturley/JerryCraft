package cx.lynch.JerryCraft;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.Material;

public class BlockBreakListener implements Listener {
	public Statement connection;
	public JerryCraft server;

	public BlockBreakListener(Statement connection, JerryCraft server) {
		this.connection = connection;
		this.server = server;
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) throws SQLException {
		Block block = e.getBlock();
		Chunk chunk = block.getChunk();

		/* Basic Portalgun Blocker */
		if ((e.getBlock().getType().equals(Material.IRON_BLOCK))
				&& (e.getBlock().getLocation().add(0, 1, 0).getBlock().getType().equals(Material.REDSTONE_BLOCK))) {
			Player p = e.getPlayer();
			server.CableBlocks.remove(e.getBlock().getLocation());
			p.sendMessage(server.chat + "There goes another good gadget...");
		} else if ((e.getBlock().getType().equals(Material.REDSTONE_BLOCK))
				&& (e.getBlock().getLocation().add(0, -1, 0).getBlock().getType().equals(Material.IRON_BLOCK))) {
			Player p = e.getPlayer();
			server.CableBlocks.remove(e.getBlock().getLocation().add(0, -1, 0));
			p.sendMessage(server.chat + "There goes another good gadget...");
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

		ResultSet chunkFindInitial = connection.executeQuery(test);

		if (!chunkFindInitial.next()) {
			// For whatever reason, return_generated_keys always returns 1/true...
			String qnewchunk = "insert into `jerrytest`.`chunkledger` values (null," + Integer.toString(chunkx)
			+ ", " + Integer.toString(chunkz) + ", CURRENT_TIMESTAMP);";
			PreparedStatement stmt;
			stmt = server.connection.prepareStatement(qnewchunk, Statement.RETURN_GENERATED_KEYS);
			stmt.executeUpdate();
			ResultSet x = stmt.getGeneratedKeys();
			x.next();
			chunk_id = x.getInt(1);
			stmt.close();
		} else {
			chunk_id = chunkFindInitial.getInt("chunk_id");
		}

		server.getLogger().info(Integer.toString(chunk_id));

		int update = connection.executeUpdate("insert into `jerrytest`.`blockledger` values (null, " + chunk_id + ", "
				+ blockx + ", " + blocky + ", " + blockz + ", \"" + Material.AIR.toString() + "\");");

	}
}
