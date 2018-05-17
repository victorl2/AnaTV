package service;

import java.util.List;

import domain.entity.Subtitle;
import domain.entity.Video;

/**
 * Interface that defines how to extract content from the web
 * 
 * @author Victor Ferreira
 * @since 13-05-2018
 */
public interface ScrapperService {

	/**
	 * Extract the list of videos from the web
	 * 
	 * @param videoNameForSearch
	 *            is the String containing the name that will be used to search for
	 *            the video
	 * @return List<Video> containing all the videos found in the search.
	 */
	public List<Video> getVideoFromSource(String videoNameForSearch);

	/**
	 * Extract the subtitle from the web
	 * 
	 * @param subtitleNameForSearch
	 *            is the String containing the name that will be used to search for
	 *            the subtitle.
	 * @return Subtitle found for the given parameter in the search.
	 */
	public Subtitle getSubtitleFromSource(String subtitleNameForSearch);

}
