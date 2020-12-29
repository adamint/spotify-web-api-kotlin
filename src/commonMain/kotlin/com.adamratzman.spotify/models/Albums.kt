/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package com.adamratzman.spotify.models

import com.adamratzman.spotify.utils.Market
import com.adamratzman.spotify.utils.match
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Simplified Album object that can be used to retrieve a full [Album]
 *
 * @property href A link to the Web API endpoint providing full details of the album.
 * @property id The Spotify ID for the album.
 * @property albumGroup Optional. The field is present when getting an artist’s albums. Possible values
 * are “album”, “single”, “compilation”, “appears_on”. Compare to album_type this field represents relationship
 * between the artist and the album.
 * @property artists The artists of the album. Each artist object includes a link in href to more detailed information about the artist.
 * @property availableMarkets The markets in which the album is available: ISO 3166-1 alpha-2 country codes. Note
 * that an album is considered available in a market when at least 1 of its tracks is available in that market.
 * @property images The cover art for the album in various sizes, widest first.
 * @property name The name of the album. In case of an album takedown, the value may be an empty string.
 * @property type The object type: “album”
 * @property releaseDate The date the album was first released, for example 1981. Depending on the precision,
 * it might be shown as 1981-12 or 1981-12-15.
 * @property releaseDatePrecisionString The precision with which release_date value is known: year , month , or day.
 * @property albumType The type of the album: one of “album”, “single”, or “compilation”.
 * @property restrictions Part of the response when Track Relinking is applied, the original track is not available
 * in the given market, and Spotify did not have any tracks to relink it with. The track response will still contain
 * metadata for the original track, and a restrictions object containing the reason why the track is not available:
 * "restrictions" : {"reason" : "market"}
 */
@Serializable
public data class SimpleAlbum(
    @SerialName("album_type") private val albumTypeString: String,
    @SerialName("available_markets") private val availableMarketsString: List<String> = listOf(),
    @SerialName("external_urls") override val externalUrlsString: Map<String, String>,
    override val href: String,
    override val id: String,
    override val uri: AlbumUri,

    val artists: List<SimpleArtist>,
    val images: List<SpotifyImage>,
    val name: String,
    val type: String,
    val restrictions: Restrictions? = null,
    @SerialName("release_date") private val releaseDateString: String,
    @SerialName("release_date_precision") val releaseDatePrecisionString: String,
    @SerialName("total_tracks") val totalTracks: Int? = null,
    @SerialName("album_group") private val albumGroupString: String? = null
) : CoreObject() {
    val availableMarkets: List<Market> get() = availableMarketsString.map { Market.valueOf(it) }

    val albumType: AlbumResultType
        get() = albumTypeString.let { _ ->
            AlbumResultType.values().first { it.id.equals(albumTypeString, true) }
        }

    val releaseDate: ReleaseDate get() = getReleaseDate(releaseDateString)

    val albumGroup: AlbumResultType?
        get() = albumGroupString?.let { _ ->
            AlbumResultType.values().find { it.id == albumGroupString }
        }

    /**
     * Converts this [SimpleAlbum] into a full [Album] object with the given
     * market
     *
     * @param market Provide this parameter if you want the list of returned items to be relevant to a particular country.
     */
    public suspend fun toFullAlbum(market: Market? = null): Album? = api.albums.getAlbum(id, market)
}

public data class ReleaseDate(val year: Int, val month: Int?, val day: Int?)

/**
 * Album search type
 */
public enum class AlbumResultType(public val id: String) {
    ALBUM("album"),
    SINGLE("single"),
    COMPILATION("compilation"),
    APPEARS_ON("appears_on");
}

/**
 * Represents an Album on Spotify
 *
 * @property albumType The type of the album: one of "album" , "single" , or "compilation".
 * @property artists The artists of the album. Each artist object includes a link in href to more detailed
 * information about the artist.
 * @property availableMarkets The markets in which the album is available:
 * ISO 3166-1 alpha-2 country codes. Note that an album is considered
 * available in a market when at least 1 of its tracks is available in that market.
 * @property copyrights The copyright statements of the album.
 * @property externalIds Known external IDs for the album.
 * @property genres A list of the genres used to classify the album. For example: "Prog Rock" ,
 * "Post-Grunge". (If not yet classified, the array is empty.)
 * @property href A link to the Web API endpoint providing full details of the album.
 * @property id The Spotify ID for the album.
 * @property images The cover art for the album in various sizes, widest first.
 * @property label The label for the album.
 * @property name The name of the album. In case of an album takedown, the value may be an empty string.
 * @property popularity The popularity of the album. The value will be between 0 and 100, with 100 being the most
 * popular. The popularity is calculated from the popularity of the album’s individual tracks.
 * @property releaseDate The date the album was first released, for example 1981. Depending on the precision,
 * it might be shown as 1981-12 or 1981-12-15.
 * @property releaseDatePrecision The precision with which release_date value is known: year , month , or day.
 * @property tracks The tracks of the album.
 * @property type The object type: “album”
 * @property totalTracks the total amount of tracks in this album
 * @property restrictions Part of the response when Track Relinking is applied, the original track is not available
 * in the given market, and Spotify did not have any tracks to relink it with.
 * The track response will still contain metadata for the original track, and a
 * restrictions object containing the reason why the track is not available: "restrictions" : {"reason" : "market"}
 */
@Serializable
public data class Album(
    @SerialName("album_type") private val albumTypeString: String,
    @SerialName("available_markets") private val availableMarketsString: List<String> = listOf(),
    @SerialName("external_ids") private val externalIdsString: Map<String, String> = hashMapOf(),
    @SerialName("external_urls") override val externalUrlsString: Map<String, String> = mapOf(),
    override val href: String,
    override val id: String,
    override val uri: AlbumUri,

    val artists: List<SimpleArtist>,
    val copyrights: List<SpotifyCopyright>,
    val genres: List<String>,
    val images: List<SpotifyImage>,
    val label: String,
    val name: String,
    val popularity: Int,
    @SerialName("release_date") private val releaseDateString: String,
    @SerialName("release_date_precision") val releaseDatePrecision: String,
    val tracks: PagingObject<SimpleTrack>,
    val type: String,
    @SerialName("total_tracks") val totalTracks: Int,
    val restrictions: Restrictions? = null
) : CoreObject() {
    val availableMarkets: List<Market> get() = availableMarketsString.map { Market.valueOf(it) }

    val externalIds: List<ExternalId> get() = externalIdsString.map { ExternalId(it.key, it.value) }

    val albumType: AlbumResultType get() = AlbumResultType.values().first { it.id == albumTypeString }

    val releaseDate: ReleaseDate get() = getReleaseDate(releaseDateString)
}

/**
 * Describes an album's copyright information
 *
 * @property text The copyright text for this album.
 * @property type The type of copyright: C = the copyright,
 * P = the sound recording (performance) copyright.
 */
@Serializable
public data class SpotifyCopyright(
    @SerialName("text") private val textString: String,
    @SerialName("type") private val typeString: String
) {
    val text: String
        get() = textString
                .removePrefix("(P)")
                .removePrefix("(C)")
                .trim()

    val type: CopyrightType get() = CopyrightType.values().match(typeString)!!
}

@Serializable
internal data class AlbumsResponse(val albums: List<Album?>)

/**
 * Copyright statement type of an Album
 */
public enum class CopyrightType(public val identifier: String) : ResultEnum {
    COPYRIGHT("C"),
    SOUND_PERFORMANCE_COPYRIGHT("P");

    override fun retrieveIdentifier(): String = identifier
}

internal fun getReleaseDate(releaseDateString: String) = when (releaseDateString.count { it == '-' }) {
    0 -> ReleaseDate(releaseDateString.toInt(), null, null)
    1 -> {
        val split = releaseDateString.split("-").map { it.toInt() }
        ReleaseDate(split[0], split[1], null)
    }
    2 -> {
        val split = releaseDateString.split("-").map { it.toInt() }
        ReleaseDate(split[0], split[1], split[2])
    }
    else -> throw IllegalArgumentException()
}
