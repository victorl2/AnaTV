package domain.entity.estructure;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.io.IOUtils;

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
			/**
			 * Download of default files from web content
			 */
			public CompletableFuture<?> download(){
				URL linkLegenda = null;
				
				OutputStream outputStream = null;
				InputStream in = null;
				
				try {
					linkLegenda = new URL(this.contentLink);
					
					HttpURLConnection httpcon = (HttpURLConnection) linkLegenda.openConnection();
					httpcon.addRequestProperty("User-Agent", "Mozilla/4.0");
					in = httpcon.getInputStream();
					
					outputStream = new FileOutputStream(this.contentLocation.toFile());
					IOUtils.copy(in, outputStream);
					outputStream.close();
				} catch (IOException e) {
					return CompletableFuture.completedFuture(false);
				}
				
				return CompletableFuture.completedFuture(true);
			}
				
			
		}
		
		SubtitleTorrent torrent = new SubtitleTorrent();
		torrent.defineUrl(this.urlDownload);
		return torrent;
	}
	
}
