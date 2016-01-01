package mod.rp.railnet.common.core.routing;

import java.util.LinkedList;

import mod.rp.railnet.common.core.clients.RailNetClient;
import net.minecraft.block.BlockRailBase;

/**
 * Defines the track between {@link RailNetClient RailNetClients}. This
 * determines the following:
 * <ul>
 * <li>The connected clients on the two ends</li>
 * <li>The direction of the segment</li>
 * <li>Access restrictions (if any)</li>
 * <li>A detailed map of the segment if available. See {@link SegmentParts}</li>
 * </ul>
 * 
 * @author MajorR
 *
 */
public class RailNetSegment {

	/**
	 * The weight or element is undefined and should not be used in calculations
	 * <br>
	 * <code>UNDEFINED_BASE_TIME = {@value #UNDEFINED_BASE_TIME}</code>
	 */
	public static final long UNDEFINED_BASE_TIME = -1L;

	/** The clients associated with the segment. */
	private RailNetClient clientA, clientB;

	/** The direction of the track between clients */
	private SegmentDirection direction;

	/** The average time it takes for the mapper to travel end-to-end */
	private long baseTime;

	/**
	 * If all {@link #segmentParts segment parts} are mapped, this will reflect
	 * the number of blocks traveled along this segment; otherwise,
	 * <code>actualLength = {@link #UNDEFINED_BASE_TIME}</code>
	 */
	private int actualLength;

	/**
	 * Individual pieces of track data for map generation as collected by a
	 * mapping engine
	 */
	private LinkedList<SegmentParts> segmentParts = new LinkedList<SegmentParts>();

	// ====================

	/**
	 * 
	 * @param clientA
	 *            The client connecting to the first segment
	 * @param clientB
	 *            The client connecting to the last segment
	 * @param direction
	 *            The direction of travel allowed along segment
	 * @param segmentParts
	 *            The individual mapped parts of this segment
	 * @param baseTime
	 *            The amount of time taken by the base model to travel this
	 *            segment
	 */
	public RailNetSegment(RailNetClient clientA, RailNetClient clientB, SegmentDirection direction,
			LinkedList<SegmentParts> segmentParts, long baseTime) {
		setClientA(clientA);
		setClientB(clientB);
		setDirection(direction);
		setSegmentParts(segmentParts);
		setBaseTime(baseTime);
	}

	/**
	 * 
	 * @param clientA
	 *            The client connecting to the first segment
	 * @param clientB
	 *            The client connecting to the last segment
	 * @param direction
	 *            The direction of travel allowed along segment
	 * @param segmentParts
	 *            The individual mapped parts of this segment
	 */
	public RailNetSegment(RailNetClient clientA, RailNetClient clientB, SegmentDirection direction,
			LinkedList<SegmentParts> segmentParts) {
		this(clientA, clientB, direction, segmentParts, RailNetSegment.UNDEFINED_BASE_TIME);
	}

	public RailNetSegment(RailNetClient clientA, RailNetClient clientB, SegmentDirection direction) {
		this(clientA, clientB, direction, null, RailNetSegment.UNDEFINED_BASE_TIME);
	}

	public RailNetSegment(RailNetClient clientA, RailNetClient clientB, LinkedList<SegmentParts> segments,
			long baseTime) {
		this(clientA, clientB, SegmentDirection.BIDIRECTIONAL, segments, baseTime);
	}

	public RailNetSegment(RailNetClient clientA, RailNetClient clientB, LinkedList<SegmentParts> segments) {
		this(clientA, clientB, SegmentDirection.BIDIRECTIONAL, segments);
	}

	public RailNetSegment(RailNetClient clientA, SegmentDirection direction, LinkedList<SegmentParts> segments,
			long baseTime) {
		this(clientA, null, direction, segments, baseTime);
	}

	public RailNetSegment(RailNetClient clientA, SegmentDirection direction, LinkedList<SegmentParts> segments) {
		this(clientA, null, direction, segments);
	}

	public RailNetSegment(RailNetClient clientA, SegmentDirection direction) {
		this(clientA, null, direction, null);
	}

	public RailNetSegment(RailNetClient clientA, RailNetClient clientB) {
		this(clientA, clientB, SegmentDirection.BIDIRECTIONAL, null);
	}

	public RailNetSegment(RailNetClient clientA) {
		this(clientA, null, SegmentDirection.BIDIRECTIONAL, null);
	}

	/**
	 * The direction of a segment.
	 * 
	 * @author MajorR
	 *
	 */
	public static enum SegmentDirection {
		/**
		 * <p>
		 * <h1>ClientA ===> ClientB</h1>
		 * </p>
		 * The rail is omi-directional traveling from ClientA to ClientB.
		 */
		FORWARD,
		/**
		 * <p>
		 * <h1>ClientA <=== ClientB</h1>
		 * </p>
		 * The rail is omi-directional traveling from ClientB to Client A.
		 */
		REVERSE,
		/**
		 * <p>
		 * <h1>ClientA <==> ClientB</h1>
		 * </p>
		 * The rail is bi-directional and assume the train will remain in the
		 * same direction throughout the segment. No trains will travel in
		 * opposite directions while on the track. If a connecting client is
		 * omi-directional one the same pathway, this segment will be used as
		 * the omi-directional version. If two connecting segments have
		 * conflicting directions, the path cannot be used until resolved.
		 */
		BIDIRECTIONAL,
		/**
		 * <p>
		 * <h1>ClientA =<>= ClientB</h1>
		 * </p>
		 * The rail is bi-directional, but the train can reverse directions
		 * while on the track if needed.
		 */
		REVERSABLE,

		/**
		 * <p>
		 * <h1>ClientA X==X ClinetB</h1>
		 * </p>
		 * The segment is considered closed and, therefore, cannot be utilized.
		 */
		CLOSED
	}

	/**
	 * Individual segments of the main RailSegment. Each segment represents a
	 * straight line between two points.
	 * <ul>
	 * <li>A straight line is defined as a change in a maximum of two axes.</li>
	 * <li>A curve may be represented by overlapping perpendicular segments.
	 * </li>
	 * <li>Multiple curve patterns hinting diagonal directions may be
	 * represented by diagonal lines.</li>
	 * <li>A railType represents the entire segment.</li>
	 * <li>Segments may include a railType and may be split by different
	 * railTypes.</li>
	 * </ul>
	 * 
	 * @author MajorR
	 * @version 0.0.6
	 */
	public class SegmentParts {
		private int xA, xB;
		private int yA, yB;
		private int zA, zB;

		// TODO implement railType.
		/** The type of rail the segment part represents */
		private Class<? extends BlockRailBase> railType;

		// ===========================
		// Constructors
		// ===========================
		public SegmentParts(int xA, int yA, int zA, int xB, int yB, int zB, Class<? extends BlockRailBase> railType) {
			setPointA(xA, yA, zA);
			setPointB(xB, yB, zB);
			setRailType(railType);
		}

		public SegmentParts(int xA, int yA, int zA, int xB, int yB, int zB) {
			setPointA(xA, yA, zA);
			setPointB(xB, yB, zB);

		}

		public SegmentParts(int xA, int yA, int zA) {
			setPointA(xA, yA, zA);
		}

		public void setPointA(int x, int y, int z) {
			this.xA = x;
			this.yA = y;
			this.zA = z;
		}

		public void setPointB(int x, int y, int z) {
			this.xB = x;
			this.yB = y;
			this.zB = z;
		}

		public int[] getPointA() {
			int[] out = { xA, yA, zA };
			return out;
		}

		public int[] getPointB() {
			int[] out = { xB, yB, zB };
			return out;
		}

		public Class<? extends BlockRailBase> getRailType() {
			return railType;
		}

		public void setRailType(Class<? extends BlockRailBase> railType) {
			this.railType = railType;
		}
	}

	public RailNetClient getClientA() {
		return clientA;
	}

	public void setClientA(RailNetClient clientA) {
		this.clientA = clientA;
		invalidateMap();
	}

	public RailNetClient getClientB() {
		return clientB;
	}

	public void setClientB(RailNetClient clientB) {
		this.clientB = clientB;
		invalidateMap();
	}

	public SegmentDirection getDirection() {
		return direction;
	}

	public void setDirection(SegmentDirection direction) {
		this.direction = direction;
	}

	/**
	 * Used for terrain mapping, includes individual straight lines that connect
	 * throughout the rail segment and may include individual track data.
	 * 
	 * @return a LinkedList of the segment parts.
	 * @see SegmentParts
	 */
	public LinkedList<SegmentParts> getSegmentParts() {
		return segmentParts;
	}

	/**
	 * Set the individual sub-segments of the track. Used for terrain mapping.
	 * 
	 * @param segmentParts
	 */
	public void setSegmentParts(LinkedList<SegmentParts> segmentParts) {
		this.segmentParts = segmentParts;
	}

	/**
	 * Retrieves the client on the other end of the segment.
	 * 
	 * @param client
	 *            The known client.
	 * @return the client on opposite end; if client is the same on both ends or
	 *         no ending client is found, return <code>null</code>.
	 */
	public RailNetClient getConnectingClient(RailNetClient client) {
		if (client == clientA)
			return clientA == clientB ? null : clientB;
		return clientA == clientB ? null : clientA;
	}

	/**
	 * Connects a client as ClientA if empty; otherwise connects as ClientB. If
	 * client is connected, the map is invalidated.
	 * 
	 * @param railNetClient
	 * @return <code>true</code> if client is connected; otherwise
	 *         <code>false</code> if all clients are already defined.
	 * @see RailNetSegment#disconnectClient(RailNetClient)
	 * @see RailNetSegment#invalidateMap()
	 */
	public boolean connectClient(RailNetClient railNetClient) {
		if (getClientA() == null)
			setClientA(railNetClient);
		else if (getClientB() == null)
			setClientB(railNetClient);
		else
			return false;
		return true;

	}

	/**
	 * disconnects the client if client is part of segment. If client is
	 * connected, the map is invalidated.
	 * 
	 * @param railNetClient
	 * @return <code>true</code> if client is disconnected; otherwise
	 *         <code>false</code> if client is not part of segment.
	 * @see RailNetSegment#connectClient(RailNetClient)
	 * @see RailNetSegment#invalidateMap()
	 */
	public boolean disconnectClient(RailNetClient railNetClient) {
		if (getClientA() == railNetClient)
			setClientA(null);
		else if (getClientB() == railNetClient)
			setClientB(null);
		else
			return false;
		return true;
	}

	/**
	 * Deletes the SegmentParts Map.
	 */
	public void invalidateMap() {
		this.segmentParts.clear();
		this.baseTime = UNDEFINED_BASE_TIME;
	}

	public long getBaseTime() {
		return baseTime;
	}

	public void setBaseTime(long baseTime) {
		this.baseTime = baseTime;
	}

	public int getActualLength() {
		return actualLength;
	}

	public void setActualLength(int actualLength) {
		this.actualLength = actualLength;
	}
}