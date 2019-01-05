[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.utils](../index.md) / [Playlist](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`Playlist(collaborative: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`, description: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, externalUrls: `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>, followers: `[`Followers`](../-followers/index.md)`, href: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, id: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, primaryColor: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, images: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`SpotifyImage`](../-spotify-image/index.md)`>, name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, owner: `[`SpotifyPublicUser`](../-spotify-public-user/index.md)`, public: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`? = null, _snapshotId: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, tracks: `[`PagingObject`](../-paging-object/index.md)`<`[`PlaylistTrack`](../-playlist-track/index.md)`>, type: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, _uri: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, uri: `[`PlaylistURI`](../-playlist-u-r-i/index.md)` = PlaylistURI(_uri), snapshot: `[`Snapshot`](../../com.adamratzman.spotify.endpoints.client/-client-playlist-a-p-i/-snapshot/index.md)` = ClientPlaylistAPI.Snapshot(_snapshotId))`

### Parameters

`collaborative` - Returns true if context is not search and the owner allows other users to modify the playlist.
Otherwise returns false.

`description` - The playlist description. Only returned for modified, verified playlists, otherwise null.

`externalUrls` - Known external URLs for this playlist.

`followers` -

`href` - A link to the Web API endpoint providing full details of the playlist.

`id` - The Spotify ID for the playlist.

`primaryColor` - Unknown.

`images` - Images for the playlist. The array may be empty or contain up to three images.
The images are returned by size in descending order.Note: If returned, the source URL for the
image ( url ) is temporary and will expire in less than a day.

`name` - The name of the playlist.

`owner` - The user who owns the playlist

`public` - The playlist’s public/private status: true the playlist is public, false the playlist is private,
null the playlist status is not relevant

`snapshot` - The version identifier for the current playlist. Can be supplied in other requests to target
a specific playlist version

`tracks` - Information about the tracks of the playlist.

`type` - The object type: “playlist”

`uri` - The Spotify URI for the playlist.