/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.models

import com.adamratzman.spotify.SpotifyClientApi
import com.adamratzman.spotify.utils.Locale
import com.adamratzman.spotify.utils.Market
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Basic information about a Spotify show
 *
 * @param copyrights The copyright statements of the show.
 * @param description A description of the show.
 * @param explicit Whether or not the show has explicit content (true = yes it does; false = no it does not OR unknown).
 * @param images The cover art for the show in various sizes, widest first.
 * @param isExternallyHosted True if all of the show’s episodes are hosted outside of Spotify’s CDN. This field might be null in some cases.
 * @param mediaType The media type of the show.
 * @param name The name of the show.
 * @param publisher The publisher of the show.
 * @param type The object type: “show”.
 *
 * @property availableMarkets A list of the countries in which the show can be played, identified by their ISO 3166-1 alpha-2 code.
 * @property languages A list of the languages used in the show, identified by their ISO 639 code.
 */
@Serializable
public data class SimpleShow(
    @SerialName("available_markets") private val availableMarketsString: List<String> = listOf(),
    @SerialName("external_urls") override val externalUrlsString: Map<String, String>,
    val copyrights: List<SpotifyCopyright>,
    val description: String,
    val explicit: Boolean,
    override val href: String,
    override val id: String,
    val images: List<SpotifyImage>,
    @SerialName("is_externally_hosted") val isExternallyHosted: Boolean? = null,
    @SerialName("languages") private val languagesString: List<String>,
    @SerialName("media_type") val mediaType: String,
    val name: String,
    val publisher: String,
    val type: String,
    override val uri: ShowUri
) : CoreObject() {
    val availableMarkets: List<Market> get() = availableMarketsString.map { Market.valueOf(it) }

    val languages: List<Locale> get() = languagesString.map { Locale.valueOf(it.replace("-", "_")) }

    /**
     * Converts this [SimpleShow] to a full [Show] object
     *
     * @param market Provide this parameter if you want the list of returned items to be relevant to a particular country.
     */
    public suspend fun toFullShow(market: Market? = null): Show? = (api as? SpotifyClientApi)?.shows?.getShow(id, market)
}

/**
 * Information about a Spotify show, including its episodes
 *
 * @param copyrights The copyright statements of the show.
 * @param description A description of the show.
 * @param explicit Whether or not the show has explicit content (true = yes it does; false = no it does not OR unknown).
 * @param images The cover art for the show in various sizes, widest first.
 * @param isExternallyHosted True if all of the show’s episodes are hosted outside of Spotify’s CDN. This field might be null in some cases.
 * @param mediaType The media type of the show.
 * @param name The name of the show.
 * @param publisher The publisher of the show.
 * @param type The object type: “show”.
 * @param episodes A [NullablePagingObject] of the show’s episodes.
 *
 * @property availableMarkets A list of the countries in which the show can be played, identified by their ISO 3166-1 alpha-2 code.
 * @property languages A list of the languages used in the show, identified by their ISO 639 code.
 */
@Serializable
public data class Show(
    @SerialName("available_markets") private val availableMarketsString: List<String> = listOf(),
    val copyrights: List<SpotifyCopyright>,
    val description: String,
    val explicit: Boolean,
    val episodes: NullablePagingObject<SimpleEpisode>,
    @SerialName("external_urls") override val externalUrlsString: Map<String, String>,
    override val href: String,
    override val id: String,
    val images: List<SpotifyImage>,
    @SerialName("is_externally_hosted") val isExternallyHosted: Boolean? = null,
    @SerialName("languages") val languagesString: List<String>,
    @SerialName("media_type") val mediaType: String,
    val name: String,
    val publisher: String,
    val type: String,
    override val uri: ShowUri
) : CoreObject() {
    val availableMarkets: List<Market> get() = availableMarketsString.map { Market.valueOf(it) }

    val languages: List<Locale> get() = languagesString.map { Locale.valueOf(it.replace("-", "_")) }
}

@Serializable
internal data class ShowList(val shows: List<SimpleShow?>)
