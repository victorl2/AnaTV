package service.impl;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import domain.entity.structure.Video;
import service.VideoScrapper;

public class Scrapper1337xImplTest {
	
	private VideoScrapper videoService = new Scrapper1337xImpl();
	
	@Test
	public void getVideos() {
		List<Video> videos = videoService.getContent("Billions s02e05");
		Assert.assertTrue(videos.size() > 5);
	}
	
	@Test
	public void getEmptyVideoList() {
		String searchTerm = "wqhuawhhass";
		assertEquals(0,videoService.getContent(searchTerm).size());
	}
	
}
