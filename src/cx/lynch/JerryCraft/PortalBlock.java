package cx.lynch.JerryCraft;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.minecraft.server.v1_12_R1.EnumParticle;
import net.minecraft.server.v1_12_R1.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_12_R1.PlayerConnection;

public class PortalBlock {
	public Location BlockLocation;
	public Location DestinationLocation;

	public String ChangeServerDestination;

	public boolean ChangeServer;

	private PortalBlock ScopeReference;
	private JerryCraft Plugin;

	public PortalBlock(final Location BlockLocation, String ServerDestination, final JerryCraft Plugin) {
		this.Plugin = Plugin;
		this.ScopeReference = this;

		this.ChangeServer = true;
		this.ChangeServerDestination = ServerDestination;

		this.BlockLocation = BlockLocation;
		
		Animate();
	}

	public PortalBlock(final Location BlockLocation, final Location DestinationLocation, final JerryCraft Plugin) {
		this.Plugin = Plugin;
		this.ScopeReference = this;
		this.ChangeServer = false;

		this.BlockLocation = BlockLocation;
		this.DestinationLocation = DestinationLocation;

		Animate();
	}

	public void Animate() {
		new BukkitRunnable() {
			private int Ticks = 160;

			@Override
			public void run() {
				PacketPlayOutWorldParticles PacketPortalLocation = new PacketPlayOutWorldParticles(EnumParticle.SLIME,
						true, (float) BlockLocation.getX(), (float) BlockLocation.getY(), (float) BlockLocation.getZ(),
						0, 0, 0, 0, 25, null);
				
				PacketPlayOutWorldParticles PacketPortalDestination = null;
				if (!ScopeReference.ChangeServer) {
				PacketPortalDestination = new PacketPlayOutWorldParticles(
						EnumParticle.SLIME, true, (float) DestinationLocation.getX(),
						(float) DestinationLocation.getY(), (float) DestinationLocation.getZ(), 0, 0, 0, 0, 25, null);
				}

				Collection<? extends Player> OnlinePlayers = Bukkit.getOnlinePlayers();
				for (Player P : OnlinePlayers) {
					PlayerConnection CP = ((CraftPlayer) P).getHandle().playerConnection;

					CP.sendPacket(PacketPortalLocation);
					if (!ScopeReference.ChangeServer) {
						CP.sendPacket(PacketPortalDestination);
					}
				}

				Ticks--;
				if (Ticks < 1) {
					Plugin.PortalBlocks.remove(ScopeReference);
					this.cancel();
				}
			}
		}.runTaskTimer(Plugin, 0, 1);
	}
}
