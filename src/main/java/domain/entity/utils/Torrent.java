package domain.entity.utils;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public abstract class Torrent{
	
	/**
	 * Default location for the content
	 */
	protected Path contentLocation;
	
	/**
	 * Link poiting to the content on the web
	 */
	protected String contentLink;
	

	public void defineUrl(String contentLink) {
		this.contentLink = contentLink;
	}
	
	public abstract CompletableFuture<?> download();
	
}
