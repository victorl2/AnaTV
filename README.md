# AnaTV
 
AnaTV is app that facilitate access to content on the web suchs as movies and series. Using simple target web crawlers content already avaliable on the web is extracted in a more friendly format. The content extracted is mainly in the torrent format, it enables better file sharing but also standard hosted content is also extracted to provide subtitles for the videos.

## Web Crawling
The location where the content is located is not the main focus of the application, the aim is to provide a generic interface that will be usefull regardless of the location we are scraping content from. Currently the library used to crawl the web is [jsoup](https://jsoup.org/), but if the need arises we can change to a more robust tool that can emulate a full-fledged browser.

## Handling torrent
To handle the torrent format _(to be precise magnet links)_ the library [Bt](https://github.com/atomashpolskiy/bt) is the choice for this task, the library is being maintained with active development.
