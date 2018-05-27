package service;

import java.util.List;

/**
 * Defines the basic functionality for web scrapping
 * 
 * @author Victor Ferreira
 * @param <T>
 *            is the type of content that definition is being applied
 * @since 25-05-2018
 */
public interface Scrapper<T extends Content> {

	/**
	 * Get the a list containing all the content linked to the string provided as a
	 * search term
	 * 
	 * @param nameForSearch
	 *            to scrappe the content
	 * @return List with all the content found for the search term.
	 */
	public List<T> getContent(final String nameForSearch);
}
