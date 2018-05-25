package service;

import java.util.List;
import java.util.Optional;

import domain.entity.Video;

/**
 * Interface that defines how to extract <b>video</b> 
 * content from the web
 * 
 * @author Victor Ferreira
 * @since 13-05-2018
 */
public interface VideoScrapper extends Scrappable{

	/**
	 * Extract the list of videos from the web
	 * 
	 * @param videoNameForSearch
	 *            is the String containing the name that will be used to search for
	 *            the video
	 * @return List<Video> containing all the videos found in the search.
	 */
	public Optional<List<Video>> getVideos(String videoNameForSearch);


}
