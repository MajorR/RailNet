package mod.rp.railnet.common.core.clients;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import mod.rp.railnet.common.core.routing.RailNetSegment;
import mod.rp.railnet.common.core.routing.RailNetwork;
import mod.rp.railnet.common.core.routing.RailNetwork.RailNetCommand;

/**
 * 
 * @author MajorR
 *
 */
public abstract class RailNetClient {

	/** List of all RailNew Clients, regardless of domain */
	private static final Map<UUID, RailNetClient> clients = new HashMap<UUID, RailNetClient>();

	/**
	 * Client can handle an the maximum number of segments of an integer (2
	 * <sup>31</sup>-1)
	 * 
	 * @see RailNetClient#maxSegments
	 * @see RailNetClient#getMaxSegments()
	 * @see RailNetClient#setMaxSegments(int)
	 */
	public static final int UNLIMITED_SEGMENTS = Integer.MAX_VALUE;
	// ---------------------------
	// Client properties
	// ---------------------------
	/** Unique ID of client */
	private final UUID uuid;

	/** User-Defined name of client */
	private String name;

	/** RailNet network of Client */
	private RailNetwork network;

	/** Rail segments connected to client */
	private List<RailNetSegment> connectedSegments;

	/**
	 * The maximum number of connecting segments that can be handled by this
	 * client.
	 */
	private int maxSegments = RailNetClient.UNLIMITED_SEGMENTS;

	// ===========================
	// Constructors
	// ===========================
	public RailNetClient(RailNetwork network, String name) {
		uuid = UUID.randomUUID();
		setName(name);
		setNetwork(network);
		clients.put(getID(), this);
	}

	public RailNetClient(String name) {
		this(null, name);
	}

	public RailNetClient(RailNetwork network) {
		this(network, null);
	}

	public RailNetClient() {
		this(null, null);
	}

	// ===========================
	// Client Methods
	// ===========================
	/**
	 * Returns
	 * 
	 * @return
	 */
	public static final List<RailNetClient> getConnectedClients(RailNetClient client) {
		// Iterate through segments and return the opposite end clients.
		List<RailNetClient> out = new ArrayList<RailNetClient>();
		for (RailNetSegment s : client.getConnectedSegments())
			out.add(s.getConnectingClient(client));
		return out;
	}

	public static final void cleanSegments(RailNetClient client) {
		for (RailNetSegment seg : client.connectedSegments)
			if (seg.getConnectingClient(client) == null)
				client.disconnectSegment(seg);
	}

	// ===========================
	// Abstract Methods
	// ===========================

	/**
	 * Perform action upon request from RailNetwork.
	 * 
	 * @param commandID
	 * @param param
	 * @return <code>true</code> if client will comply with command; otherwise,
	 *         return <code>false</code>.
	 */
	public abstract boolean netCommand(RailNetCommand commandID, Object param);

	// ===========================
	// Static Methods
	// ===========================

	/**
	 * Gets the connected clients of a client by UUID.
	 * 
	 * @param id
	 * @return If Client exists, return array of connected clients; otherwise,
	 *         return <code>null</code>.
	 */
	public static final List<RailNetClient> getConnectedClients(UUID id) {
		if (clients.containsKey(id))
			return getConnectedClients(clients.get(id));
		return null;
	}

	// ===========================
	// Getters and Setters
	// ===========================

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public RailNetwork getNetwork() {
		return network;
	}

	public void setNetwork(RailNetwork network) {
		this.network = network;
	}

	public UUID getID() {
		return this.uuid;
	}

	public List<RailNetSegment> getConnectedSegments() {
		return connectedSegments;
	}

	public void setConnectedSegments(List<RailNetSegment> connectedSegments) {
		this.connectedSegments = connectedSegments;
	}

	public void connectSegment(RailNetSegment segment) {
		segment.connectClient(this);
		getConnectedSegments().add(segment);
	}

	public boolean disconnectSegment(RailNetSegment segment) {
		segment.disconnectClient(this);
		return getConnectedSegments().remove(segment);
	}

	public int getMaxSegments() {
		return maxSegments;
	}

	public void setMaxSegments(int maxSegments) {
		this.maxSegments = maxSegments;
	}

}
