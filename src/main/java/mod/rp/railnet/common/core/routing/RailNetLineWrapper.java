package mod.rp.railnet.common.core.routing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import mod.rp.railnet.common.core.clients.RailNetClient;

public class RailNetLineWrapper {

	/** The id of the rail line */
	private UUID lineID;

	/**
	 * The id of the associated train. May be a
	 * {@link net.minecraft.entity.item.EntityMinecart MC Minecart} or, if
	 * available, {@link mods.railcraft.common.carts.Train Railcraft Train}
	 * UUID.
	 */
	private UUID trainID;

	/**
	 * The primary route used by the train in this line.
	 */
	private RailNetRoute primaryRoute;

	/**
	 * A secondary route that may be used when refueling or temporary rerouting.
	 * When the secondary route is complete, this rail line will fallback to the
	 * primary route.
	 * 
	 * @see #primaryRoute
	 */
	private RailNetRoute secondaryRoute;

	/**
	 * Calculated segments of the Line based on {@link RailNetSegment RailNet
	 * Segments}.
	 */
	private final Map<UUID, LineSegmentWrapper> lineSegments = new HashMap<UUID, LineSegmentWrapper>();

	/** Checkpoints the train must visit or pass within route */
	private List<RailNetClient> checkpoints = new ArrayList<RailNetClient>();

	// ===========================
	// Configurations
	// ===========================

	/**
	 * The order of checkpoints cannot be changed; however, if
	 * <code>ignoreOnError = true</code>, then checkpoints may be skipped based
	 * on reason.
	 * 
	 * @Default <code>true</code>
	 */
	private boolean orderedPoints;

	/**
	 * Repeat route continuously. The route must be a complete circuit to
	 * enable.
	 *
	 * @Default <code>true</code>
	 */
	private boolean loop;

	/**
	 * Allows net to automatically reroute train based on train needs (such as
	 * refueling)
	 * 
	 * @Default <code>true</code>
	 */
	private boolean allowSubroutine;

	/**
	 * Allows net to automatically modify route based on rail traffic and route
	 * weights
	 * 
	 * @Default <code>true</code>
	 */
	private boolean allowAutoRouteGen;

	/**
	 * Allows the train to be temporarily rerouted if path is congested or
	 * another train will benefit greater then this train will loose.
	 * 
	 * @Default <code>true</code>
	 */
	private boolean allowTemporaryRoutes;

	/**
	 * If there is an error in the route, the train will continue to follow
	 * non-conflicting routes, even if it means to skip checkpoints.
	 * 
	 * @Default <code>false</code>
	 */
	private boolean ignoreOnError;

	/**
	 * If there is an error in the route and cannot be ignored or
	 * <code>ignoreOnError = false</code>, the train will remain at a dock until
	 * error is resolved.
	 * 
	 * @Default <code>true</code>
	 */
	private boolean dockOnError;

	/**
	 * If <code>allowTemporaryRoutes = true</code>, the route will always be
	 * treated as a permanent route. Overtime, if
	 * <code>allowRouteChange = true</code>, the route may change based on
	 * weights
	 * 
	 * @Default <code>false</code>
	 */
	private boolean alwaysUpdateRoute;

	/**
	 * Allows the actual time it takes for the train to complete Route Segments
	 * affect the weights in determining the best paths.
	 * 
	 * @Default <code>true</code>
	 */
	private boolean allowRealTimeSync;

	/**
	 * Allows the actual time it takes for a train to complete route segments to
	 * infer unknown actual times from the base model when mapping is used.
	 * 
	 * @Required <code>{@link #allowRealTimeSync
	 *            allowRealTimeSync = true}</code>
	 * @Default <code>true</code>
	 */
	private boolean allowRealTimeAssume;

	/**
	 * Allows the router to pre-calculate times based on Map data (if available)
	 * to the best known for a track based on Railcraft weights per track.
	 * Accuracy depends on map accuracy. Perfect values cannot be guaranteed as
	 * routes may be affected by outside variables. This option may be resource
	 * intensive and real-time synchronization (
	 * <code>allowRealTimeSync = true</code> can be just as accurate with a few
	 * runs; however, allowing such calculations can enable complex rerouting
	 * without ever actually taking route.
	 * 
	 * @Required <code>{@link #allowMapData allowMapData = true}</code> <br>
	 *           <code>{@link #allowAutoRouteGen
	 *            allowAutoRouteGen = true}</code>
	 * @Default <code>false</code>
	 */
	private boolean allowAdvancedTimeCalc;

	/**
	 * Allows the router to assume a path is good, even if the client does not
	 * respond or is deleted without an update.
	 * 
	 * @Default <code>true</code>
	 */
	private boolean allowGhostPaths;

	/**
	 * The route will adjust to mapped paths if available; even if a weight for
	 * an unmapped path is considerably preferable.
	 * 
	 * @Default <code>false</code>
	 * @Required <code>{@link #allowMapData allowMapData = true}</code><br>
	 *           <code>{@link #allowAutoRouteGen
	 *            allowAutoRouteGeb = true}</code>
	 */
	private boolean preferMappedPaths;

	/**
	 * The weights of segments can be adjusted based on mapping Data.
	 * 
	 * @Default <code>true</code>
	 */
	private boolean allowMapData;

	/**
	 * Allow use of base model weights if time weights are undefined.
	 * 
	 * @Default <code>true</code>
	 */
	private boolean allowBaseModel;

	/**
	 * Use fuel consumption in calculating route paths.
	 * 
	 * @see #weight_fuel
	 * @Default <code>true</code>
	 */
	private boolean weightedFuel;

	/**
	 * Use average speed (<i>distance / time</i>) in calculating route paths.
	 * 
	 * @see #weight_speed
	 * @Default <code>true</code>
	 */
	private boolean weightedSpeed;

	/**
	 * Use the average time it takes to complete a segment in calculating route
	 * paths.
	 * 
	 * @see #weight_time
	 * @Default <code>true</code>
	 */
	private boolean weightedTime;

	/**
	 * Use the segment length in calculating route paths
	 * 
	 * @see #weight_distance
	 * @Default <code>true</code>
	 */
	private boolean weightedDistance;

	/**
	 * 
	 */
	private boolean weightedSegments;

	// ------------------------
	// Weights
	// ------------------------
	/** Weight of fuel consumption in determining routed segments */
	private float weight_fuel;
	/**
	 * Weight of the average speed (<i>distance / time</i>) in determining
	 * routed segments
	 */
	private float weight_speed;

	/**
	 * Weight of the amount of time it takes for the train to complete a segment
	 * in determining routed segments
	 */
	private float weight_time;

	/** Weight of the length of a segment in determining routed segments */
	private float weight_distance;

	// ===========================
	// Constructors
	// ===========================

	public class LineSegmentWrapper {

		public static final long TIME_NOT_DETERMINED = -1L;

		public static final float DEFAULT_WEIGHT = 1f;

		private final UUID segmentID;

		private long realTime;

		private boolean required;

		private boolean restricted;

		private float weight;

		private int fuel_comsumption;

		public LineSegmentWrapper(UUID segmentID) {
			this(segmentID, TIME_NOT_DETERMINED, false, false, DEFAULT_WEIGHT);
		}

		public LineSegmentWrapper(UUID segmentID, long realTime, boolean required) {
			this(segmentID, realTime, required, false, DEFAULT_WEIGHT);
		}

		public LineSegmentWrapper(UUID segmentID, boolean required, float weight) {
			this(segmentID, TIME_NOT_DETERMINED, required, false, DEFAULT_WEIGHT);
		}

		public LineSegmentWrapper(UUID segmentID, long realTime, boolean required, float weight) {
			this(segmentID, realTime, required, false, weight);
		}

		public LineSegmentWrapper(UUID segmentID, long realTime, boolean required, boolean restricted, float weight) {
			this.segmentID = segmentID;
			setRealTime(realTime);
			setRequired(required);
			setWeight(weight);
			setRestricted(restricted);
		}

		public UUID getSegmentID() {
			return segmentID;
		}

		public long getRealTime() {
			return realTime;
		}

		public void setRealTime(long realTime) {
			this.realTime = realTime;
		}

		public boolean isRequired() {
			return required;
		}

		public void setRequired(boolean required) {
			this.required = required;
		}

		public float getWeight() {
			return weight;
		}

		public void setWeight(float weight) {
			// Weights cannot be zero (0). Default to "No Effect"
			if (weight == 0)
				weight = DEFAULT_WEIGHT;
			this.weight = weight;
		}

		public boolean isRestricted() {
			return restricted;
		}

		public void setRestricted(boolean restricted) {
			this.restricted = restricted;
		}

		public int getFuel_comsumption() {
			return fuel_comsumption;
		}

		public void setFuel_comsumption(int fuel_comsumption) {
			this.fuel_comsumption = fuel_comsumption;
		}

	}
}
