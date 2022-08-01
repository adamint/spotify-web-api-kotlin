package com.adamratzman.spotify.remote.wrapper.apis

import android.graphics.Bitmap
import com.adamratzman.spotify.remote.impl.SpotifyAppRemoteWrapper
import com.adamratzman.spotify.remote.wrapper.SpotifyAppRemoteApi
import com.adamratzman.spotify.remote.wrapper.apis.responses.CallResult
import com.adamratzman.spotify.remote.wrapper.apis.responses.toLibraryCallResult
import com.spotify.protocol.types.Image.Dimension
import com.spotify.protocol.types.ImageUri

/**
 * Get images from the Spotify App
 *
 *  Get an instance of a ImagesApi with [SpotifyAppRemoteApi.imagesApi].
 */
public class ImagesApiWrapper(
    private val spotifyAppRemoteWrapper: SpotifyAppRemoteWrapper
) : SpotifyAppRemoteWrappedApi {
    /**
     * Fetch an image from the Spotify app and specify the preferred size by dimension, or [Dimension.LARGE] if not specified.
     *
     * @param imageUri The id of the image you want to fetch, from [PlayerTrackWrapper.imageUri]
     * @param dimension Predefined definition, or large by default.
     * @param callback The callback to be invoked after value production. Optional, you can also use [CallResult.execute] to await
     * a value from a suspending context (recommended).
     */
    public fun getImage(
        imageUri: ImageUri,
        dimension: Dimension = Dimension.LARGE,
        callback: ((Bitmap) -> Unit)? = null
    ): CallResult<Bitmap, Bitmap> {
        return spotifyAppRemoteWrapper.mImagesApi.getImage(imageUri, dimension)
            .toLibraryCallResult({ it }, callback)
    }
}