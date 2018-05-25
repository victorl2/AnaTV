package service;

import java.nio.file.Path;

/**
 * Defines what a element should do to be 
 * considered scrappable
 * 
 * @author Victor Ferreira
 * @since 25-05-2018
 */
public interface Scrappable {
	
	/**
	 * Download a content that was scrapped from the web
	 * @param url that starts the download
	 * @return Path pointing to the file on disk
	 */
	public Path download(final String url);
}
