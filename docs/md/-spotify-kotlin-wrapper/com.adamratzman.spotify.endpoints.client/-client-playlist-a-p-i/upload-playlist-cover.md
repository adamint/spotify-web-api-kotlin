[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.endpoints.client](../index.md) / [ClientPlaylistAPI](index.md) / [uploadPlaylistCover](./upload-playlist-cover.md)

# uploadPlaylistCover

`fun uploadPlaylistCover(playlist: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, imagePath: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, imageFile: `[`File`](http://docs.oracle.com/javase/8/docs/api/java/io/File.html)`? = null, image: `[`BufferedImage`](http://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html)`? = null, imageData: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, imageUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null): `[`SpotifyRestAction`](../../com.adamratzman.spotify.main/-spotify-rest-action/index.md)`<`[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`>`

Replace the image used to represent a specific playlist. Image type **must** be jpeg.

Must specify a JPEG image path or image data, maximum payload size is 256 KB

### Parameters

`playlist` - the spotify id or uri for the playlist.

`imagePath` - Optionally specify the full local path to the image

`imageUrl` - Optionally specify a URL to the image

`imageFile` - Optionally specify the image [File](http://docs.oracle.com/javase/8/docs/api/java/io/File.html)

`image` - Optionally specify the image's [BufferedImage](http://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html) object

`imageData` - Optionally specify the Base64-encoded image data yourself

### Exceptions

`IIOException` - if the image is not found

`BadRequestException` - if invalid data is provided