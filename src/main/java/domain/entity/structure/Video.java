package domain.entity.structure;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

import com.google.inject.Module;

import bt.Bt;
import bt.data.Storage;
import bt.data.file.FileSystemStorage;
import bt.dht.DHTConfig;
import bt.dht.DHTModule;
import bt.runtime.BtClient;
import bt.runtime.Config;
import domain.entity.utils.Torrent;
import service.Content;

/**
 * Video encapsulating the video content as its atributes
 * @author Victor Ferreira
 * @since 13-05-2018
 */
public class Video implements Content{
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
	private int size;

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

	public void setSize(int size) {
		this.size = size;
	}
	
	public Torrent generateTorrent() {	
		
		Video currentVideo = this;
		
		//Torrent of the type video
		class TorrentVideo extends Torrent {
			
			TorrentVideo() {
				this.contentLink = currentVideo.magnetLink;
			}
			
			public CompletableFuture<?> download() {
				
				Config config = new Config() {
					@Override
					public int getNumOfHashingThreads() {
						return Runtime.getRuntime().availableProcessors() * 2;
					}
					
				};
				
				// enable bootstrapping from public routers
				Module dhtModule = new DHTModule(new DHTConfig() {
					@Override
					public boolean shouldUseRouterBootstrap() {
						return true;
					}
				});
				config.setMaxConcurrentlyActivePeerConnectionsPerTorrent(500);
				config.setMaxPeerConnectionsPerTorrent(1000);
				config.setMaxPendingConnectionRequests(300);
				config.setPeerDiscoveryInterval(Duration.ofSeconds(10,1));
				config.setNumberOfPeersToRequestFromTracker(120);
				config.setMaxIOQueueSize(12000);
				

				// create file system based backend for torrent data
				Storage storage = new FileSystemStorage(this.contentLocation);

				// create client with a private runtime
				BtClient client = Bt
						.client()
						.config(config)
						.magnet(this.contentLink)
						.storage(storage)
						.autoLoadModules()
						.module(dhtModule)
						.initEagerly()
						.sequentialSelector()
						.stopWhenDownloaded()
						.build();
				
				return client.startAsync(state -> {
					if(state.getPiecesRemaining() == 0) {
						client.stop();
					}
				}, 1000).exceptionally(e -> {
					client.stop();
					return null;
				});
			}
		}
		
		return new TorrentVideo();
	}

	@Override
	public String toString() {
		return "Video [" + (name != null ? "name=" + name + ", " : "")
				+ (quality != null ? "quality=" + quality + ", " : "") + "size=" + size + "]";
	}

}
