package mod.rp.railnet;

public class CONFIG {

	/**
	 * Properties and references related to the mod
	 * 
	 * @author MajorR
	 *
	 */
	public class MOD {
		public static final String VERSION = "0.0.5d";
		public static final String ID = "RailNet";
		public static final String NAME = "RailNet";
		public static final String DEPENDENCIES = "required-after:Forge@[10.13.0.1199,);"
				+ "after:Railcraft";
		public static final String FINGERPRINT = "--";

		public static final String CONFIG_FOLDER = "railnet";
		public static final String NET_CHANNEL = "railnet";
	}

	/**
	 * Properties and references related to Minecraft
	 * 
	 * @author MajorR
	 *
	 */
	public class MC {
		public static final String VERSION = "[1.7.10,1.8)";
	}

	/**
	 * Set up and load the configuration file. Should be called in the mod
	 * preInit() event handler.
	 */
	public static void preInit() {

	}

}
