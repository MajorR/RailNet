package mod.rp.railnet.common.core.routing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import mod.rp.railnet.common.core.clients.RailNetClient;

public class RailNetwork {

	private static final List<RailNetwork> networks = new ArrayList<RailNetwork>();

	// Network properties
	/** Unique id of rail-network */
	private final UUID uuid;

	/** user name of rail-network */
	private String domain;

	/** connected clients to the railnet */
	private final Map<UUID, RailNetClient> clients = new HashMap<UUID, RailNetClient>();

	/** Segments connecting clients */
	private final Map<UUID, RailNetSegment> segments = new HashMap<UUID, RailNetSegment>();

	/** Routing table */
	private final Map<UUID, RailNetRoute> routingTable = new HashMap<UUID, RailNetRoute>();
	/** Client Map in form of nodes */

	/** Generated rail map from a terrain map engine */

	public RailNetwork() {
		uuid = UUID.randomUUID();
	}

	// ===========================
	// Getters and Setters
	// ===========================
	public String getNetDomain() {
		return domain;
	}

	public void setNewDomain(String domain) {
		this.domain = domain;
	}

	// ===========================
	// RailNet Commands
	// ===========================
	public enum RailNetCommand {
		/** Route to */
		ROUTE,
		/** Lock Client in Route to until new command */
		ROUTE_LOCKED,
		/** Use default route */
		ROUTE_DEFAULT,
		/** Sets the client's default route */
		ROUTE_SET,
		/** Disable notification updates */
		UPDATE_DISABLE,
		/** Enable notification updates (default) */
		UPDATE_ENABLE,
	}

}
