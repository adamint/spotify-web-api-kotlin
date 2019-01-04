/* Created by Adam Ratzman (2018) */
package com.adamratzman.spotify.endpoints.public

import com.adamratzman.spotify.main.SpotifyAPI
import com.adamratzman.spotify.main.SpotifyRestAction
import com.adamratzman.spotify.utils.AudioAnalysis
import com.adamratzman.spotify.utils.AudioFeatures
import com.adamratzman.spotify.utils.AudioFeaturesResponse
import com.adamratzman.spotify.utils.BadRequestException
import com.adamratzman.spotify.utils.EndpointBuilder
import com.adamratzman.spotify.utils.Market
import com.adamratzman.spotify.utils.SpotifyEndpoint
import com.adamratzman.spotify.utils.Track
import com.adamratzman.spotify.utils.TrackList
import com.adamratzman.spotify.utils.TrackURI
import com.adamratzman.spotify.utils.catch
import com.adamratzman.spotify.utils.encode
import com.adamratzman.spotify.utils.toObject
import java.util.function.Supplier

/**
 * Endpoints for retrieving information about one or more tracks from the Spotify catalog.
 */
class TracksAPI(api: SpotifyAPI) : SpotifyEndpoint(api) {
    /**
     * Get Spotify catalog information for a single track identified by its unique Spotify ID.
     *
     * @param track the spotify id or uri for the track.
     * @param market Provide this parameter if you want to apply [Track Relinking](https://github.com/adamint/spotify-web-api-kotlin/blob/master/README.md#track-relinking)
     *
     * @return nullable Track. This behavior is *the same* as in `getTracks`
     */
    fun getTrack(track: String, market: Market? = null): SpotifyRestAction<Track?> {
        return toAction(Supplier {
            catch { get(EndpointBuilder("/tracks/${TrackURI(track).id.encode()}").with("market", market?.code).toString())
                .toObject<Track>(api) }
        })
    }

    /**
     * Get Spotify catalog information for multiple tracks based on their Spotify IDs.
     *
     * @param tracks the spotify id or uri for the tracks.
     * @param market Provide this parameter if you want to apply [Track Relinking](https://github.com/adamint/spotify-web-api-kotlin/blob/master/README.md#track-relinking)
     *
     * @return List of possibly-null full [Track] objects.
     */
    fun getTracks(vararg tracks: String, market: Market? = null): SpotifyRestAction<List<Track?>> {
        return toAction(Supplier {
            get(EndpointBuilder("/tracks").with("ids", tracks.joinToString(",") { TrackURI(it).id.encode() })
                    .with("market", market?.code).toString())
                .toObject<TrackList>(api).tracks
        })
    }

    /**
     * Get a detailed audio analysis for a single track identified by its unique Spotify ID.
     *
     * @param track the spotify id or uri for the track.
     *
     * @throws BadRequestException if [track] cannot be found
     */
    fun getAudioAnalysis(track: String): SpotifyRestAction<AudioAnalysis> {
        return toAction(Supplier {
            get(EndpointBuilder("/audio-analysis/${TrackURI(track).id.encode()}").toString())
                .toObject<AudioAnalysis>(api)
        })
    }

    /**
     * Get audio feature information for a single track identified by its unique Spotify ID.
     *
     * @param track the spotify id or uri for the track.
     *
     * @throws BadRequestException if [track] cannot be found
     */
    fun getAudioFeatures(track: String): SpotifyRestAction<AudioFeatures> {
        return toAction(Supplier {
            get(EndpointBuilder("/audio-features/${TrackURI(track).id.encode()}").toString())
                .toObject<AudioFeatures>(api)
        })
    }

    /**
     * Get audio features for multiple tracks based on their Spotify IDs.
     *
     * @param tracks the spotify id or uri for the tracks.
     *
     * @return Ordered list of possibly-null [AudioFeatures] objects.
     */
    fun getAudioFeatures(vararg tracks: String): SpotifyRestAction<List<AudioFeatures?>> {
        return toAction(Supplier {
            get(EndpointBuilder("/audio-features").with("ids", tracks.joinToString(",") { TrackURI(it).id.encode() }).toString())
                    .toObject<AudioFeaturesResponse>(api).audio_features
        })
    }
}
