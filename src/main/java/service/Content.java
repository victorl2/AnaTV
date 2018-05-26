package service;

import domain.entity.utils.Torrent;

public interface Content {
	
	/**
	 * Generate a torrent from the current content,
	 * the content must have some kind of link pointing
	 * to a web content.
	 * @return Torrent file
	 */
	public Torrent generateTorrent();
	
	
}
