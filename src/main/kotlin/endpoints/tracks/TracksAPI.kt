package endpoints.tracks

import main.SpotifyAPI
import main.toObject
import obj.*
import java.util.stream.Collectors

class TracksAPI(api: SpotifyAPI) : Endpoint(api) {
    fun getTrack(trackId: String, market: String? = null): Track {
        return get("https://api.spotify.com/v1/tracks/$trackId${if (market != null) "?market=$market" else ""}").toObject()
    }

    fun getTracks(market: String? = null, vararg trackIds: String): TrackQNList {
        return get("https://api.spotify.com/v1/tracks?ids=${trackIds.toList().stream().collect(Collectors.joining(","))}${if (market != null) "&market=$market" else ""}")
                .removePrefix("{\n  \"tracks\" : ").removeSuffix("}").toObject()
    }

    fun getAudioAnalysis(trackId: String): AudioAnalysis {
        return get("https://api.spotify.com/v1/audio-analysis/$trackId").toObject()
    }

    fun getAudioFeatures(trackId: String): AudioFeatures {
        return get("https://api.spotify.com/v1/audio-features/$trackId").toObject()
    }

    fun getAudioFeatures(vararg trackIds: String): AudioFeaturesList {
        return get("https://api.spotify.com/v1/audio-features?ids=${trackIds.toList().stream().collect(Collectors.joining(","))}")
                .removePrefix("{\n  \"audio_features\" :").removeSuffix("}").toObject()
    }

}