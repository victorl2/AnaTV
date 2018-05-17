package service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.inject.Module;

import bt.Bt;
import bt.data.Storage;
import bt.data.file.FileSystemStorage;
import bt.dht.DHTConfig;
import bt.dht.DHTModule;
import bt.runtime.BtClient;
import bt.runtime.Config;

public class ScrapperServiceImpl {
	public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
		final String example = "magnet:?xt=urn:btih:39a42f6469c74a4d878d18f87da81fd0fa57b646&dn=Green+Day+-+Boulevard+Of+Broken+Dreams&tr=udp%3A%2F%2Ftracker.leechers-paradise.org%3A6969&tr=udp%3A%2F%2Ftracker.zer0day.to%3A1337&tr=udp%3A%2F%2Ftracker.coppersurfer.tk%3A6969";
		download(example);
	}
	
	
	

	private static InputStream read(URL url) {
		try {
			HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
			httpcon.addRequestProperty("User-Agent", "Mozilla/4.0");

			return httpcon.getInputStream();
		} catch (IOException e) {
			String error = e.toString();
			throw new RuntimeException(e);
		}
	}

	private static void crawl(String url) throws IOException {

		Response response = Jsoup.connect(url).followRedirects(false).execute();

		System.out.println(response.statusCode() + " : " + url);

		if (response.hasHeader("locati	on")) {
			String redirectUrl = response.header("location");
			crawl(redirectUrl);
		}

	}
	
	public void downloadLegenda() {
		System.out.println("XXXXXXXXXXXXXXXXXXXXXXXX");
		Document paginaLegenda = Jsoup.connect("http://legendei.com/designated-survivor-s02e20/").userAgent(ua).get();

		final String caminhoLegenda = paginaLegenda.select("a.buttondown").attr("href");
		System.out.println(caminhoLegenda);

		URL linkLegenda = new URL(caminhoLegenda);
		InputStream in = read(linkLegenda);

		File file = new File("/home/victor/Code/legendaFinal.rar");
		OutputStream outputStream = new FileOutputStream(file);
		IOUtils.copy(in, outputStream);
		outputStream.close();
	}
	
	public void extractList() throws IOException {
		final String URL_SERIES = "http://1337x.to/search/designated+survivor/1/";
		final String BASE_URL = "http://1337x.to";
		final String LEGENDAS = "http://legendei.com/";

		String ua = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_6_8) AppleWebKit/534.30 (KHTML, like Gecko) Chrome/12.0.742.122 Safari/534.30";

		Document doc = Jsoup.connect(URL_SERIES).userAgent(ua).get();

		Elements listaElementos = doc.select("tbody").select("tr");
		String magnetTest = "";

		List<String> linksDownload = new ArrayList<>();

		listaElementos.forEach(detailedInfo -> {
			Element content = detailedInfo.select("td.name").select("a[href*=/torrent/]").first();

			final String href = content.attr("href");
			String magnet = "";
			try {
				Document magnetPage = Jsoup.connect(BASE_URL + href).userAgent(ua).get();
				magnet = magnetPage.select("a:contains(Magnet Download)").attr("href");
			} catch (IOException e) {
				e.printStackTrace();
			}

			String qualidade = "PADRAO";

			if (content.text().contains("1080")) {
				qualidade = "FullHD";
			} else if (content.text().contains("720")) {
				qualidade = "HD";
			}

			System.out.println("nome:" + content.text());
			System.out.println("qualidade:" + qualidade);
			System.out.println("seeds:" + detailedInfo.select("td.seeds").text());
			System.out.println("size:" + detailedInfo.select("td.size").text()
					.substring(0, detailedInfo.select("td.size").text().indexOf("MB")).trim() + "MB");
			System.out.println("data:" + detailedInfo.select("td.coll-date").text());
			System.out.println("link:" + href);
			System.out.println(magnet);
			System.out.println("**********************\n");
			
			linksDownload.add(magnet);
			return;
		});
	}

	private static void download(String magnetUrl) throws UnknownHostException, NoSuchAlgorithmException, IOException {
		System.out.println(magnetUrl);
		
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
		config.setPeerDiscoveryInterval(Duration.ofSeconds(4,2));
	
		
		System.out.println("MAX PEERS:" + config.getMaxPeerConnectionsPerTorrent());
		System.out.println("MAX ACTIVE PEERS:" + config.getMaxConcurrentlyActivePeerConnectionsPerTorrent());

		// get download directory
		Path targetDirectory = new File("/home/victor/Code/").toPath();

		// create file system based backend for torrent data
		Storage storage = new FileSystemStorage(targetDirectory);

		// create client with a private runtime
		
		BtClient client = Bt
				.client()
				.config(config)
				.magnet(magnetUrl)
				.storage(storage)
				.autoLoadModules()
				.module(dhtModule)
				.initEagerly()
				.sequentialSelector()
				.stopWhenDownloaded()
				.build();
		
		
		client.startAsync().join();
	}

}
