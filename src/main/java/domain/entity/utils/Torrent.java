package domain.entity.utils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.io.Files;

/**
 * Torrent encapsulation for the content that can be downloaded
 * 
 * @author Victor Ferreira
 * @since 27-05-2018
 */
public abstract class Torrent {
	protected final Logger LOGGER = Logger.getLogger(Torrent.class.getName());

	public Torrent() {
		// Create a temp folder for the torrent storage
		this.contentLocation = Paths.get(Files.createTempDir().getAbsolutePath());
		LOGGER.log(Level.INFO, () -> String.format("Temporary folder(%s) created for the torrent", contentLocation));
	}

	/**
	 * Default location for the content
	 */
	protected Path contentLocation;

	/**
	 * Link poiting to the content on the web
	 */
	protected String contentLink;

	/**
	 * Define where on disk the torrent file should be saved
	 * 
	 * @param path
	 *            on disk to save the torrent
	 * @return the Torrent object itself
	 */
	final public Torrent defineOutputLocation(final String path) {
		this.contentLocation = Paths.get(path);
		return this;
	}

	/**
	 * Start the torrent download.
	 * 
	 * @return CompletableFuture to track when the file finished downloading.
	 */
	public abstract CompletableFuture<?> download();

}
