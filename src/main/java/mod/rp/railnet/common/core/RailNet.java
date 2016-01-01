package mod.rp.railnet.common.core;

import java.io.File;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import mod.rp.railnet.CONFIG;

/**
 * TODO Update comments
 * <h1>RailNet Main Activity Class</h1>
 * <p>
 * RailNet creates a network for railways.
 * </p>
 * 
 * @author MajorR
 * @version 0.0.1
 * @since 0.0.1
 *
 */
@Mod(modid = CONFIG.MOD.ID, name = CONFIG.MOD.NAME, version = CONFIG.MOD.NAME, certificateFingerprint = CONFIG.MOD.FINGERPRINT, acceptedMinecraftVersions = CONFIG.MC.VERSION)
public final class RailNet {

	/**
	 * The instance of RailNet in this session so that it may be found by other
	 * mods
	 */
	@Instance(CONFIG.MOD.ID)
	public static RailNet INSTANCE;

	// ========================================
	// Properties
	// ========================================
	/** The pointer to the configuration folder */
	private File configFolder;

	// ========================================
	// Event Handlers
	// ========================================

	/**
	 * Run on the FML PreInitialiation Call when building mods.
	 * 
	 * @param event
	 */
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {

		// Set local config file.
		configFolder = new File(event.getModConfigurationDirectory(), CONFIG.MOD.CONFIG_FOLDER);
		CONFIG.preInit();

		// Setup how data is transfered between MC Clients
		// PacketHandler.init();

		// Check for latest version of mod. May be disabled in railnet.cfg
		// StartupChecks.checkForNewVersion();


		// StackFilter.initialize();

		// ModuleManager.preInit();

		// proxy.preInitClient();

		// If OpenBlocks is available, notify of mod donation link.
		// FMLInterModComms.sendMessage("OpenBlocks", "donateUrl",
		// "http://www.railnet.co/donate/");

	}

}
