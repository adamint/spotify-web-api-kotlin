package com.adamratzman.spotify.endpoints.pub.tracks

import com.adamratzman.spotify.main.SpotifyAPI
import com.adamratzman.spotify.obj.*
import java.util.stream.Collectors

class TracksAPI(api: SpotifyAPI) : SpotifyEndpoint(api) {
    fun getTrack(trackId: String, market: String? = null): Track? {
        return get("https://api.spotify.com/v1/tracks/$trackId${if (market != null) "?market=$market" else ""}").toObject(api)
    }

    fun getTracks(market: String? = null, vararg trackIds: String): List<Track?> {
        return get("https://api.spotify.com/v1/tracks?ids=${trackIds.toList().stream().collect(Collectors.joining(","))}${if (market != null) "&market=$market" else ""}")
                .toObject<TracksResponse>(api).tracks
    }

    fun getAudioAnalysis(trackId: String): AudioAnalysis {
        return get("https://api.spotify.com/v1/audio-analysis/$trackId").toObject(api)
    }

    fun getAudioFeatures(trackId: String): AudioFeatures {
        return get("https://api.spotify.com/v1/audio-features/$trackId").toObject(api)
    }

    fun getAudioFeatures(vararg trackIds: String): List<AudioFeatures> {
        return get("https://api.spotify.com/v1/audio-features?ids=${trackIds.toList().stream().collect(Collectors.joining(","))}")
                .toObject<AudioFeaturesResponse>(api).audio_features
    }

}