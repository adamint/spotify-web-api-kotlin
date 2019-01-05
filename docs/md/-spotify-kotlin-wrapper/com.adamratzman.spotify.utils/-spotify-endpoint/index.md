[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.utils](../index.md) / [SpotifyEndpoint](./index.md)

# SpotifyEndpoint

`abstract class SpotifyEndpoint`

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `SpotifyEndpoint(api: `[`SpotifyAPI`](../../com.adamratzman.spotify.main/-spotify-a-p-i/index.md)`)` |

### Properties

| Name | Summary |
|---|---|
| [api](api.md) | `val api: `[`SpotifyAPI`](../../com.adamratzman.spotify.main/-spotify-a-p-i/index.md) |

### Functions

| Name | Summary |
|---|---|
| [toAction](to-action.md) | `fun <T> toAction(supplier: `[`Supplier`](http://docs.oracle.com/javase/8/docs/api/java/util/function/Supplier.html)`<`[`T`](to-action.md#T)`>): `[`SpotifyRestAction`](../../com.adamratzman.spotify.main/-spotify-rest-action/index.md)`<`[`T`](to-action.md#T)`>` |
| [toActionPaging](to-action-paging.md) | `fun <Z, T : `[`AbstractPagingObject`](../-abstract-paging-object/index.md)`<`[`Z`](to-action-paging.md#Z)`>> toActionPaging(supplier: `[`Supplier`](http://docs.oracle.com/javase/8/docs/api/java/util/function/Supplier.html)`<`[`T`](to-action-paging.md#T)`>): `[`SpotifyRestActionPaging`](../../com.adamratzman.spotify.main/-spotify-rest-action-paging/index.md)`<`[`Z`](to-action-paging.md#Z)`, `[`T`](to-action-paging.md#T)`>` |

### Inheritors

| Name | Summary |
|---|---|
| [AlbumAPI](../../com.adamratzman.spotify.endpoints.public/-album-a-p-i/index.md) | `class AlbumAPI : `[`SpotifyEndpoint`](./index.md)<br>Endpoints for retrieving information about one or more albums from the Spotify catalog. |
| [ArtistsAPI](../../com.adamratzman.spotify.endpoints.public/-artists-a-p-i/index.md) | `class ArtistsAPI : `[`SpotifyEndpoint`](./index.md)<br>Endpoints for retrieving information about one or more artists from the Spotify catalog. |
| [BrowseAPI](../../com.adamratzman.spotify.endpoints.public/-browse-a-p-i/index.md) | `class BrowseAPI : `[`SpotifyEndpoint`](./index.md)<br>Endpoints for getting playlists and new album releases featured on Spotify’s Browse tab. |
| [ClientLibraryAPI](../../com.adamratzman.spotify.endpoints.client/-client-library-a-p-i/index.md) | `class ClientLibraryAPI : `[`SpotifyEndpoint`](./index.md)<br>Endpoints for retrieving information about, and managing, tracks that the current user has saved in their “Your Music” library. |
| [ClientPersonalizationAPI](../../com.adamratzman.spotify.endpoints.client/-client-personalization-a-p-i/index.md) | `class ClientPersonalizationAPI : `[`SpotifyEndpoint`](./index.md)<br>Endpoints for retrieving information about the user’s listening habits. |
| [ClientPlayerAPI](../../com.adamratzman.spotify.endpoints.client/-client-player-a-p-i/index.md) | `class ClientPlayerAPI : `[`SpotifyEndpoint`](./index.md)<br>These endpoints allow for viewing and controlling user playback. Please view [the official documentation](https://developer.spotify.com/web-api/working-with-connect/) for more information on how this works. This is in beta and is available for **premium users only**. Endpoints are **not** guaranteed to work |
| [FollowingAPI](../../com.adamratzman.spotify.endpoints.public/-following-a-p-i/index.md) | `open class FollowingAPI : `[`SpotifyEndpoint`](./index.md)<br>This endpoint allow you check the playlists that a Spotify user follows. |
| [PlaylistsAPI](../../com.adamratzman.spotify.endpoints.public/-playlists-a-p-i/index.md) | `open class PlaylistsAPI : `[`SpotifyEndpoint`](./index.md)<br>Endpoints for retrieving information about a user’s playlists |
| [SearchAPI](../../com.adamratzman.spotify.endpoints.public/-search-a-p-i/index.md) | `class SearchAPI : `[`SpotifyEndpoint`](./index.md)<br>Get Spotify catalog information about artists, albums, tracks or playlists that match a keyword string. |
| [TracksAPI](../../com.adamratzman.spotify.endpoints.public/-tracks-a-p-i/index.md) | `class TracksAPI : `[`SpotifyEndpoint`](./index.md)<br>Endpoints for retrieving information about one or more tracks from the Spotify catalog. |
| [UserAPI](../../com.adamratzman.spotify.endpoints.public/-user-a-p-i/index.md) | `open class UserAPI : `[`SpotifyEndpoint`](./index.md)<br>Endpoints for retrieving information about a user’s profile. |
