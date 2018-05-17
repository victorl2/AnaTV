package domain.entity;

import java.util.Calendar;

/**
 * Video encapsulating the video content as its atributes
 * @author Victor Ferreira
 * @since 13-05-2018
 */
public class Video {
	/**
	 * exact name of the file on the site from where it was extract
	 */
	private String name;
	
	/**
	 * Descriptive name for display
	 */
	private String descriptiveName;
	
	/**
	 * Magnet(torrent) link for download
	 */
	private String magnetLink;
	
	/**
	 * Quality classification for the video
	 */
	private String quality;
	
	/**
	 * Amount of seeds in the torrent
	 */
	private int seeds;
	
	/**
	 * Size of the video in MB
	 */
	private long size;
	
	/**
	 * Date in wicth the file was updloaded on the web
	 */
	private Calendar torrentUploadDate;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescriptiveName() {
		return descriptiveName;
	}

	public void setDescriptiveName(String descriptiveName) {
		this.descriptiveName = descriptiveName;
	}

	public String getMagnetLink() {
		return magnetLink;
	}

	public void setMagnetLink(String magnetLink) {
		this.magnetLink = magnetLink;
	}

	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	public int getSeeds() {
		return seeds;
	}

	public void setSeeds(int seeds) {
		this.seeds = seeds;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public Calendar getTorrentUploadDate() {
		return torrentUploadDate;
	}

	public void setTorrentUploadDate(Calendar torrentUploadDate) {
		this.torrentUploadDate = torrentUploadDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((magnetLink == null) ? 0 : magnetLink.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Video)) {
			return false;
		}
		Video other = (Video) obj;
		if (magnetLink == null) {
			if (other.magnetLink != null) {
				return false;
			}
		} else if (!magnetLink.equals(other.magnetLink)) {
			return false;
		}
		return true;
	}
	
}
