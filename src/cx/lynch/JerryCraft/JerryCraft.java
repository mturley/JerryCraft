package cx.lynch.JerryCraft;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Statement;

public class JerryCraft extends JavaPlugin {
	public ArrayList<PortalBlock> PortalBlocks = new ArrayList<PortalBlock>();
	public ArrayList<Location> CableBlocks = new ArrayList<Location>();

	public String chat = ChatColor.BLACK + "[" + ChatColor.GREEN + "testing" + ChatColor.BLACK + "] " + ChatColor.GREEN;

	public Connection connection;
	public Statement statement;

	@Override
	public void onEnable() {
		try {
			openConnection();
			this.statement = connection.createStatement();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// TODO: Load CableBlocks from SQL.
		getServer().getPluginManager().registerEvents(new PortalGunListener(this), this);
		getServer().getPluginManager().registerEvents(new PlayerMoveListener(this), this);

		getServer().getPluginManager().registerEvents(new ChunkLoadListener(this), this);
		getServer().getPluginManager().registerEvents(new BlockBreakListener(this.statement, this), this);
		getServer().getPluginManager().registerEvents(new BlockPlaceListener(this), this);

		getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		
		this.getCommand("dial").setExecutor(new DialCommandExecutor(this));

		getLogger().info("JerryCraft by Dan has been Enabled.");
	}

	@Override
	public void onDisable() {
		getLogger().info("JerryCraft has been Disabled.");
	}

	public void openConnection() throws SQLException, ClassNotFoundException {
		if (connection != null && !connection.isClosed()) {
			return;
		}

		synchronized (this) {
			if (connection != null && !connection.isClosed()) {
				return;
			}
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://" + "10.0.0.9" + ":" + "3306" + "/" + "jerrytest",
					"jerry", "jerrycraft");
		}
	}
}
