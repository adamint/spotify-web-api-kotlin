/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package com.adamratzman.spotify.endpoints.public

import com.adamratzman.spotify.GenericSpotifyApi
import com.adamratzman.spotify.SpotifyException.BadRequestException
import com.adamratzman.spotify.http.EndpointBuilder
import com.adamratzman.spotify.http.SpotifyEndpoint
import com.adamratzman.spotify.http.encodeUrl
import com.adamratzman.spotify.models.AudioAnalysis
import com.adamratzman.spotify.models.AudioFeatures
import com.adamratzman.spotify.models.AudioFeaturesResponse
import com.adamratzman.spotify.models.PlayableUri
import com.adamratzman.spotify.models.Track
import com.adamratzman.spotify.models.TrackList
import com.adamratzman.spotify.models.serialization.toObject
import com.adamratzman.spotify.utils.Market
import com.adamratzman.spotify.utils.catch

@Deprecated("Endpoint name has been updated for kotlin convention consistency", ReplaceWith("TrackApi"))
public typealias TracksAPI = TrackApi

@Deprecated("Endpoint name has been updated for kotlin convention consistency", ReplaceWith("TrackApi"))
public typealias TrackAPI = TrackApi

/**
 * Endpoints for retrieving information about one or more tracks from the Spotify catalog.
 *
 * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/tracks/)**
 */
public class TrackApi(api: GenericSpotifyApi) : SpotifyEndpoint(api) {
    /**
     * Get Spotify catalog information for a single track identified by its unique Spotify ID.
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/tracks/get-track/)**
     *
     * @param track The id or uri for the track.
     * @param market Provide this parameter if you want to apply [Track Relinking](https://github.com/adamint/spotify-web-api-kotlin#track-relinking)
     *
     * @return possibly-null Track. This behavior is *the same* as in [getTracks]
     */
    public suspend fun getTrack(track: String, market: Market? = null): Track? = catch {
        get(
                EndpointBuilder("/tracks/${PlayableUri(track).id.encodeUrl()}").with(
                        "market",
                        market?.name
                ).toString()
        ).toObject(Track.serializer(), api, json)
    }

    /**
     * Get Spotify catalog information for multiple tracks based on their Spotify IDs.
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/tracks/get-several-tracks/)**
     *
     * @param tracks The id or uri for the tracks. Maximum **50**.
     * @param market Provide this parameter if you want to apply [Track Relinking](https://github.com/adamint/spotify-web-api-kotlin#track-relinking)
     *
     * @return List of possibly-null full [Track] objects.
     */
    public suspend fun getTracks(vararg tracks: String, market: Market? = null): List<Track?> {
        checkBulkRequesting(50, tracks.size)
        return bulkRequest(50, tracks.toList()) { chunk ->
            get(
                    EndpointBuilder("/tracks").with("ids", chunk.joinToString(",") { PlayableUri(it).id.encodeUrl() })
                            .with("market", market?.name).toString()
            ).toObject(TrackList.serializer(), api, json).tracks
        }.flatten()
    }

    /**
     * Get a detailed audio analysis for a single track identified by its unique Spotify ID.
     *
     * The Audio Analysis endpoint provides low-level audio analysis for all of the tracks in the Spotify catalog.
     * The Audio Analysis describes the track’s structure and musical content, including rhythm, pitch, and timbre.
     * All information is precise to the audio sample.
     *
     * Many elements of analysis include confidence values, a floating-point number ranging from 0.0 to 1.0.
     * Confidence indicates the reliability of its corresponding attribute. Elements carrying a small confidence value
     * should be considered speculative. There may not be sufficient data in the audio to compute the attribute with
     * high certainty.
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/tracks/get-audio-analysis/)**
     *
     * @param track The id or uri for the track.
     *
     * @throws BadRequestException if [track] cannot be found
     */
    public suspend fun getAudioAnalysis(track: String): AudioAnalysis =
            get(EndpointBuilder("/audio-analysis/${PlayableUri(track).id.encodeUrl()}").toString())
                    .toObject(AudioAnalysis.serializer(), api, json)

    /**
     * Get audio feature information for a single track identified by its unique Spotify ID.
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/tracks/get-audio-features/)**
     *
     * @param track The id or uri for the track.
     *
     * @throws BadRequestException if [track] cannot be found
     */
    public suspend fun getAudioFeatures(track: String): AudioFeatures =
            get(EndpointBuilder("/audio-features/${PlayableUri(track).id.encodeUrl()}").toString())
                    .toObject(AudioFeatures.serializer(), api, json)

    /**
     * Get audio features for multiple tracks based on their Spotify IDs.
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/tracks/get-several-audio-features/)**
     *
     * @param tracks vararg of track ids or uris. Maximum **100**.
     *
     * @return Ordered list of possibly-null [AudioFeatures] objects.
     */
    public suspend fun getAudioFeatures(vararg tracks: String): List<AudioFeatures?> {
        checkBulkRequesting(100, tracks.size)
        return bulkRequest(100, tracks.toList()) { chunk ->
            get(
                    EndpointBuilder("/audio-features").with(
                            "ids",
                            chunk.joinToString(",") { PlayableUri(it).id.encodeUrl() }).toString()
            )
                    .toObject(AudioFeaturesResponse.serializer(), api, json).audioFeatures
        }.flatten()
    }
}
