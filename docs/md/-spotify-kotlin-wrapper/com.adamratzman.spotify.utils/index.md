[SpotifyKotlinWrapper](../index.md) / [com.adamratzman.spotify.utils](./index.md)

## Package com.adamratzman.spotify.utils

### Types

| Name | Summary |
|---|---|
| [AbstractPagingObject](-abstract-paging-object/index.md) | `abstract class AbstractPagingObject<T> : `[`ArrayList`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-array-list/index.html)`<`[`T`](-abstract-paging-object/index.md#T)`>` |
| [Album](-album/index.md) | `data class Album` |
| [AlbumResultType](-album-result-type/index.md) | `enum class AlbumResultType`<br>Album type |
| [AlbumURI](-album-u-r-i/index.md) | `class AlbumURI : `[`SpotifyUri`](-spotify-uri/index.md)<br>Represents a Spotify **Album** URI, parsed from either a Spotify ID or taken from an endpoint. |
| [Artist](-artist/index.md) | `data class Artist` |
| [ArtistURI](-artist-u-r-i/index.md) | `class ArtistURI : `[`SpotifyUri`](-spotify-uri/index.md)<br>Represents a Spotify **Artist** URI, parsed from either a Spotify ID or taken from an endpoint. |
| [AudioAnalysis](-audio-analysis/index.md) | `data class AudioAnalysis` |
| [AudioAnalysisMeta](-audio-analysis-meta/index.md) | `data class AudioAnalysisMeta` |
| [AudioFeatures](-audio-features/index.md) | `data class AudioFeatures` |
| [AudioSection](-audio-section/index.md) | `data class AudioSection` |
| [AudioSegment](-audio-segment/index.md) | `data class AudioSegment` |
| [Context](-context/index.md) | `data class Context` |
| [CurrentlyPlayingContext](-currently-playing-context/index.md) | `data class CurrentlyPlayingContext` |
| [CurrentlyPlayingObject](-currently-playing-object/index.md) | `data class CurrentlyPlayingObject` |
| [Cursor](-cursor/index.md) | `data class Cursor`<br>The cursor to use as key to find the next page of items. |
| [CursorBasedPagingObject](-cursor-based-paging-object/index.md) | `class CursorBasedPagingObject<T> : `[`AbstractPagingObject`](-abstract-paging-object/index.md)`<`[`T`](-cursor-based-paging-object/index.md#T)`>`<br>The cursor-based paging object is a container for a set of objects. It contains a key called items (whose value is an array of the requested objects) along with other keys like next and cursors that can be useful in future calls. |
| [Device](-device/index.md) | `data class Device` |
| [ErrorObject](-error-object/index.md) | `data class ErrorObject`<br>Contains a parsed error from Spotify |
| [ErrorResponse](-error-response/index.md) | `data class ErrorResponse`<br>Wrapper around [ErrorObject](-error-object/index.md) |
| [FeaturedPlaylists](-featured-playlists/index.md) | `data class FeaturedPlaylists`<br>Spotify featured playlists (on the Browse tab) |
| [Followers](-followers/index.md) | `data class Followers`<br>Spotify user's followers |
| [Linkable](-linkable/index.md) | `abstract class Linkable`<br>Allow for track relinking |
| [LinkedTrack](-linked-track/index.md) | `data class LinkedTrack`<br>Represents a [relinked track](https://github.com/adamint/spotify-web-api-kotlin/blob/master/README.md#track-relinking). This is playable in the searched market. If null, the API result is playable in the market. |
| [Market](-market/index.md) | `enum class Market`<br>Represents Spotify markets (countries + distinctive territories) |
| [PagingObject](-paging-object/index.md) | `class PagingObject<T> : `[`AbstractPagingObject`](-abstract-paging-object/index.md)`<`[`T`](-paging-object/index.md#T)`>`<br>The offset-based paging object is a container for a set of objects. It contains a key called items (whose value is an array of the requested objects) along with other keys like previous, next and limit that can be useful in future calls. |
| [PagingTraversalType](-paging-traversal-type/index.md) | `enum class PagingTraversalType` |
| [PlayHistory](-play-history/index.md) | `data class PlayHistory` |
| [PlayHistoryContext](-play-history-context/index.md) | `data class PlayHistoryContext` |
| [Playlist](-playlist/index.md) | `data class Playlist` |
| [PlaylistTrack](-playlist-track/index.md) | `data class PlaylistTrack` |
| [PlaylistTrackInfo](-playlist-track-info/index.md) | `data class PlaylistTrackInfo`<br>A collection containing a link ( href ) to the Web API endpoint where full details of the playlist’s tracks can be retrieved, along with the total number of tracks in the playlist. |
| [PlaylistURI](-playlist-u-r-i/index.md) | `class PlaylistURI : `[`SpotifyUri`](-spotify-uri/index.md)<br>Represents a Spotify **Playlist** URI, parsed from either a Spotify ID or taken from an endpoint. |
| [RecommendationResponse](-recommendation-response/index.md) | `data class RecommendationResponse` |
| [RecommendationSeed](-recommendation-seed/index.md) | `data class RecommendationSeed`<br>Seed from which the recommendation was constructed |
| [RelinkingAvailableResponse](-relinking-available-response/index.md) | `abstract class RelinkingAvailableResponse : `[`Linkable`](-linkable/index.md) |
| [Restrictions](-restrictions/index.md) | `data class Restrictions`<br>Contains an explanation of why a track is not available |
| [SavedAlbum](-saved-album/index.md) | `data class SavedAlbum` |
| [SavedTrack](-saved-track/index.md) | `data class SavedTrack` |
| [SimpleAlbum](-simple-album/index.md) | `data class SimpleAlbum : `[`Linkable`](-linkable/index.md) |
| [SimpleArtist](-simple-artist/index.md) | `data class SimpleArtist : `[`Linkable`](-linkable/index.md) |
| [SimplePlaylist](-simple-playlist/index.md) | `data class SimplePlaylist : `[`Linkable`](-linkable/index.md) |
| [SimpleTrack](-simple-track/index.md) | `data class SimpleTrack : `[`RelinkingAvailableResponse`](-relinking-available-response/index.md) |
| [SpotifyCategory](-spotify-category/index.md) | `data class SpotifyCategory`<br>Spotify music category |
| [SpotifyCopyright](-spotify-copyright/index.md) | `data class SpotifyCopyright`<br>Describes an album's copyright |
| [SpotifyEndpoint](-spotify-endpoint/index.md) | `abstract class SpotifyEndpoint` |
| [SpotifyImage](-spotify-image/index.md) | `data class SpotifyImage`<br>A Spotify image |
| [SpotifyPublicUser](-spotify-public-user/index.md) | `data class SpotifyPublicUser` |
| [SpotifyUri](-spotify-uri/index.md) | `sealed class SpotifyUri`<br>Represents a Spotify URI, parsed from either a Spotify ID or taken from an endpoint. |
| [SpotifyUserInformation](-spotify-user-information/index.md) | `data class SpotifyUserInformation` |
| [TimeInterval](-time-interval/index.md) | `data class TimeInterval` |
| [Token](-token/index.md) | `data class Token`<br>Represents a Spotify Token, retrieved through instantiating a new [SpotifyAPI](#) |
| [Track](-track/index.md) | `data class Track : `[`RelinkingAvailableResponse`](-relinking-available-response/index.md) |
| [TrackAnalysis](-track-analysis/index.md) | `data class TrackAnalysis` |
| [TrackURI](-track-u-r-i/index.md) | `class TrackURI : `[`SpotifyUri`](-spotify-uri/index.md)<br>Represents a Spotify **Track** URI, parsed from either a Spotify ID or taken from an endpoint. |
| [UserURI](-user-u-r-i/index.md) | `class UserURI : `[`SpotifyUri`](-spotify-uri/index.md)<br>Represents a Spotify **User** URI, parsed from either a Spotify ID or taken from an endpoint. |
| [VideoThumbnail](-video-thumbnail/index.md) | `data class VideoThumbnail` |

### Exceptions

| Name | Summary |
|---|---|
| [BadRequestException](-bad-request-exception/index.md) | `open class BadRequestException : `[`Exception`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-exception/index.html) |
| [SpotifyUriException](-spotify-uri-exception/index.md) | `class SpotifyUriException : `[`BadRequestException`](-bad-request-exception/index.md) |
