package domain.entity.estructure;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import domain.entity.utils.Torrent;
import service.Content;

/**
 * Interface that defines how to extract content from the web
 * 
 * @author Victor Ferreira
 * @since 13-05-2018
 */
public class Subtitle implements Content{
	
	private String name;
	
	private List<String> subs;
	
	private String urlDownload;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getSubs() {
		return subs;
	}

	public void setSubs(List<String> subs) {
		this.subs = subs;
	}

	public String getUrlDownload() {
		return urlDownload;
	}

	public void setUrlDownload(String urlDownload) {
		this.urlDownload = urlDownload;
	}

	@Override
	public Torrent generateTorrent() {
		
		
		//Torrent of the type subtitle
		class SubtitleTorrent extends Torrent{
			
			@Override
			public CompletableFuture<?> download() {
				return CompletableFuture.completedFuture(this);
			}
			
		}
		
		SubtitleTorrent torrent = new SubtitleTorrent();
		torrent.defineUrl(this.urlDownload);
		return torrent;
	}
	
}
