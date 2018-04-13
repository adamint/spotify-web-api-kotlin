package com.adamratzman.spotify.endpoints.pub.tracks

import com.adamratzman.spotify.main.SpotifyAPI
import com.adamratzman.spotify.utils.*
import java.util.function.Supplier
import java.util.stream.Collectors

class TracksAPI(api: SpotifyAPI) : SpotifyEndpoint(api) {
    fun getTrack(trackId: String, market: Market? = null): SpotifyRestAction<Track> {
        return toAction(Supplier {
            get("https://api.spotify.com/v1/tracks/${trackId.encode()}${if (market != null) "?market=${market.code}" else ""}").toObject<Track>(api)
        })
    }

    fun getTracks(market: Market? = null, vararg trackIds: String): SpotifyRestAction<List<Track>> {
        return toAction(Supplier {
            get("https://api.spotify.com/v1/tracks?ids=${trackIds.map { it.encode() }.stream().collect(Collectors.joining(","))}${if (market != null) "&market=${market.code}" else ""}")
                    .toObject<TracksResponse>(api).tracks
        })
    }

    fun getAudioAnalysis(trackId: String): SpotifyRestAction<AudioAnalysis> {
        return toAction(Supplier {
            get("https://api.spotify.com/v1/audio-analysis/${trackId.encode()}").toObject<AudioAnalysis>(api)
        })
    }

    fun getAudioFeatures(trackId: String): SpotifyRestAction<AudioFeatures> {
        return toAction(Supplier {
            get("https://api.spotify.com/v1/audio-features/${trackId.encode()}").toObject<AudioFeatures>(api)
        })
    }

    fun getAudioFeatures(vararg trackIds: String): SpotifyRestAction<List<AudioFeatures>> {
        return toAction(Supplier {
            get("https://api.spotify.com/v1/audio-features?ids=${trackIds.map { it.encode() }.stream().collect(Collectors.joining(","))}")
                    .toObject<AudioFeaturesResponse>(api).audio_features
        })
    }

}