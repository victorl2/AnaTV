package service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import domain.entity.structure.Video;
import service.VideoScrapper;

/**
 * Implementation that shows how exacly the content
 * will be extracted from the website: 
 * <b>http://1337x.to</b>
 * 
 * @author Victor Ferreira
 * @since 21-05-2018
 *
 */
public class Scrapper1337xImpl implements VideoScrapper{
	Logger LOGGER = Logger.getLogger(Scrapper1337xImpl.class.getName());
	
	private static final String BASE_URL = "http://1337x.to";
	private static final String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_6_8)"
			+ " AppleWebKit/534.30 (KHTML, like Gecko) Chrome/12.0.742.122 Safari/534.30";

	
	public List<Video> getContent(final String videoNameForSearch) {
		final String series = BASE_URL 
				+ String.format("/search/%s/1/", videoNameForSearch.toLowerCase().replace(' ', '+'));
	
		Document doc;
		
		try {
			doc = Jsoup
				.connect(series)
				.userAgent(userAgent)
				.get();
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "Não foi possível acessar a página " + BASE_URL);
			LOGGER.log(Level.WARNING, "Trace:", e);
			return new ArrayList<>();
		}
		
		//Check for invalid search term (no results found)
		Element element = doc.select(".box-info-detail").first();
		String errorMsg = "No results were returned. Please refine your search";
		
		if(element == null || element.toString().contains(errorMsg)) {
			LOGGER.log(Level.WARNING, () -> 
					String.format("Invalid search term, no results found for '%s'", videoNameForSearch));
			return Arrays.asList();
		}
		
		try {
			return getVideosFromElementList(doc
					.select("tbody")
					.select("tr"));	
		}catch(Exception e) {
			LOGGER.log(Level.WARNING, "Trace:", e);
			return new ArrayList<>();
		}
		

	}
	
	/**
	 * Extract the list of videos
	 * @param elementList that describes the content of the extract html page
	 * @return Optional encapsulating a list of <b>Video</b>
	 */
	private List<Video> getVideosFromElementList(Elements elementList) {
		final String fullHD = "FullHD";
		final String hd = "HD";
		final String standard = "Padrão";
		
		
		List<Video> videos = elementList.stream()
			.map(detailedInfo -> {
				Video currentVideo = new Video();
				
				Element content = detailedInfo
						.select("td.name")
						.select("a[href*=/torrent/]")
						.first();
	
				final String href = content.attr("href");
				
				try {
					Document magnetPage = Jsoup.connect(BASE_URL + href).userAgent(userAgent).get();
					currentVideo.setMagnetLink(magnetPage
							.select("a:contains(Magnet Download)")
							.attr("href"));
				} catch (IOException e) {
					LOGGER.log(Level.WARNING, "Não foi possível obter o link magnetico do vídeo atual analisado");
					LOGGER.log(Level.WARNING, "Trace:",e);
					return null;
				}
				
				currentVideo.setName(content.text());
				currentVideo.setSeeds(
						Integer.parseInt(detailedInfo.
								select("td.seeds")
								.text()
								)
						);
				
				String size = detailedInfo.select("td.size").text();
				
				String MB = "MB";
				String KB = "KB";
				String GB = "GB";
				
				int sizeEnd = -1;
				float weight = 1;
				
				if(size.contains(MB)) {
					sizeEnd = size.indexOf(MB);
				}else if(size.contains(GB)) {
					weight = 1000;
					sizeEnd = size.indexOf(GB);
				}else {
					weight = 0.001f;
					sizeEnd = size.indexOf(KB);
				}
					 
				
				if(size != null && size.length() > 0) {
					//Definind video size
					int fileSize = (int) Float.parseFloat(size
							.substring(0, sizeEnd)
							.trim());
					
					//Normalizing value to MB
					currentVideo.setSize((int)(fileSize * weight));
				}	
					
				//Defining video quality
				if (content.text().contains("1080")) {
					currentVideo.setQuality(fullHD);
				} else if (content.text().contains("720")) {
					currentVideo.setQuality(hd);
				}else {
					currentVideo.setQuality(standard);
				}
				
				return currentVideo;
		}).filter(video -> video != null)
			.collect(Collectors.toList());
		
		return videos.size() > 0 ? videos : new ArrayList<>();
	}


}
