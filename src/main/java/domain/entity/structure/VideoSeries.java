package domain.entity.structure;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import domain.entity.utils.Torrent;

public class VideoSeries {
	
	private String nameSerie;
	
	private List<Video> episodes;
	private List<Subtitle> subs;
	
	@SuppressWarnings("unused")
	private VideoSeries() {}
	
	public VideoSeries(String name) {
		nameSerie = name;
		episodes = Arrays.asList();
		subs = Arrays.asList();
	}
	
	public List<Torrent> generateTorrents() {
		return episodes
			.stream()
				.map(video -> video.generateTorrent())
				.collect(Collectors.toList());
	}

	public List<Video> getVideos() {
		return episodes;
	}
	
	public VideoSeries addVideo(Video video) {
		episodes.add(video);
		return this;
	}
	
	public VideoSeries addSubtitle(Subtitle sub) {
		subs.add(sub);
		return this;
	}

	public String getNameSerie() {
		return nameSerie;
	}	
	
}
