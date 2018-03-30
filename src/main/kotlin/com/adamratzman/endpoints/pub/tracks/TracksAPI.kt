package com.adamratzman.endpoints.pub.tracks

import com.adamratzman.main.SpotifyAPI
import com.adamratzman.main.toObject
import com.adamratzman.obj.*
import java.util.stream.Collectors

class TracksAPI(api: SpotifyAPI) : SpotifyEndpoint(api) {
    fun getTrack(trackId: String, market: String? = null): Track? {
        return get("https://api.spotify.com/v1/tracks/$trackId${if (market != null) "?market=$market" else ""}").toObject()
    }

    fun getTracks(market: String? = null, vararg trackIds: String): List<Track?> {
        return get("https://api.spotify.com/v1/tracks?ids=${trackIds.toList().stream().collect(Collectors.joining(","))}${if (market != null) "&market=$market" else ""}")
                .toObject<TracksResponse>().tracks
    }

    fun getAudioAnalysis(trackId: String): AudioAnalysis {
        return get("https://api.spotify.com/v1/audio-analysis/$trackId").toObject()
    }

    fun getAudioFeatures(trackId: String): AudioFeatures {
        return get("https://api.spotify.com/v1/audio-features/$trackId").toObject()
    }

    fun getAudioFeatures(vararg trackIds: String): List<AudioFeatures> {
        return get("https://api.spotify.com/v1/audio-features?ids=${trackIds.toList().stream().collect(Collectors.joining(","))}")
                .toObject<AudioFeaturesResponse>().audio_features
    }

}