package service;

import domain.entity.utils.Torrent;

/**
 * Defines the basic functionality
 * that any content must have
 * 
 * @author Victor Ferreira
 * @param <T>
 * @since 26-05-2018
 */
public interface Content {
	
	/**
	 * Generate a torrent from the current content,
	 * the content must have some kind of link pointing
	 * to a web content.
	 * @return Torrent file
	 */
	public Torrent generateTorrent();
	
	
}
