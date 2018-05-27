package example;

import static org.junit.Assert.assertEquals;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;


import domain.entity.structure.Video;
import service.VideoScrapper;
import service.impl.Scrapper1337xImpl;

public class ExampleTest {
	
	private VideoScrapper videoService = new Scrapper1337xImpl();
	
	@Test
	public void simpleTest() {
		final String value = "content";
		assertEquals("content",value);
	}
	
	@Test
	public void getVideos() {
		List<Video> videos = videoService.getContent("Billions s02e05");
		Assert.assertTrue(videos.size() > 5);
	}
	
	@Test
	public void downloadVideo() {
		Video video = new Video();
		video.setMagnetLink("magnet:?xt=urn:btih:7FA8D73411A59276EC5F64E7660E5A868BD4F819&dn=Meghan+Trainor-No+Excuses.mp3&tr=udp%3A%2F%2Ftracker.openbittorrent.com%3A80%2Fannounce&tr=udp%3A%2F%2Ftracker.opentrackr.org%3A1337%2Fannounce&tr=udp%3A%2F%2Ftracker.zer0day.to%3A1337%2Fannounce&tr=udp%3A%2F%2Ftracker.leechers-paradise.org%3A6969%2Fannounce&tr=udp%3A%2F%2Fcoppersurfer.tk%3A6969%2Fannounce");
		
		video.generateTorrent()
			.download().join();
	}
	
	@Test
	public void getEmptyVideoList() {
		String searchTerm = "wqhuawhhass";
		assertEquals(0,videoService.getContent(searchTerm).size());
	}
}
