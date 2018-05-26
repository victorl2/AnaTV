package service.impl;

import java.io.IOException;
import java.util.ArrayList;
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
	
	private static final String BASE_URL = "http://1337x.to/";
	private static final String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_6_8)"
			+ " AppleWebKit/534.30 (KHTML, like Gecko) Chrome/12.0.742.122 Safari/534.30";

	
	public List<Video> getContent(final String videoNameForSearch) {
		final String series = BASE_URL + "search/designated+survivor/1/";
	
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
				
				//Video name
				currentVideo.setName(content.text());
				
				//Amount of seeds
				currentVideo.setSeeds(
						Integer.parseInt(detailedInfo.
								select("td.seeds")
								.text()
								)
						);
				
				//Definind video size
				currentVideo.setSize(
						Long.parseLong(
								detailedInfo
								.select("td.size")
								.text()
								.substring(0, 
										detailedInfo
										.select("td.size")
										.text()
										.indexOf("MB")).trim()
						)
					);
				
				//Video upload date
	//			currentVideo.setTorrentUploadDate(
	//					,detailedInfo.select("td.coll-date")
	//					.text())
	//			;
					
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
