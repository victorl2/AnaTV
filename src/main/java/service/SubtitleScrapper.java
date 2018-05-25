package service;

import domain.entity.Subtitle;

/**
 * Interface that defines how to extract 
 * <b>subtitles</b> from the web
 * 
 * @author Victor Ferreira
 * @since 21-05-2018
 */
public interface SubtitleScrapper extends Scrappable{
	
	/**
	 * Extract the subtitle from the web
	 * 
	 * @param subtitleNameForSearch
	 *            is the String containing the name that will be used to search for
	 *            the subtitle.
	 * @return Subtitle found for the given parameter in the search.
	 */
	public Subtitle getSubtitles(String subtitleNameForSearch);
}
