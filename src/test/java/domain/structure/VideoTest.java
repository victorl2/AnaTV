package domain.structure;
import org.junit.Test;

import domain.entity.structure.Video;


public class VideoTest {
	
	@Test
	public void downloadVideo() {
		Video video = new Video();
		video.setMagnetLink("magnet:?xt=urn:btih:7FA8D73411A59276EC5F64E7660E5A868BD4F819&dn=Meghan+Trainor-No+Excuses.mp3&tr=udp%3A%2F%2Ftracker.openbittorrent.com%3A80%2Fannounce&tr=udp%3A%2F%2Ftracker.opentrackr.org%3A1337%2Fannounce&tr=udp%3A%2F%2Ftracker.zer0day.to%3A1337%2Fannounce&tr=udp%3A%2F%2Ftracker.leechers-paradise.org%3A6969%2Fannounce&tr=udp%3A%2F%2Fcoppersurfer.tk%3A6969%2Fannounce");
		
		video.generateTorrent()
			.download().join();
	}
	
	
}
