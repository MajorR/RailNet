package mod.rp.railnet.common.core.routing;

import java.util.UUID;

public class RailNetRoute {

	/** The ID of the train associated with this route */
	private UUID trainID;

	/**
	 * Destroy route when complete
	 * 
	 * @Default <code>true</code>
	 */
	private boolean destroyOnComplete;

	/**
	 * The number of route iterations completed. If <code>loop = false</code>,
	 * the loopCount will return <code>1</code> as it has completed its route.
	 */
	private int loopCount = 0;

	public class RoutePathSegment {

		private RailNetSegment segment;
		private boolean required;
		private long averageTime;
		private float weight;

	}

}
