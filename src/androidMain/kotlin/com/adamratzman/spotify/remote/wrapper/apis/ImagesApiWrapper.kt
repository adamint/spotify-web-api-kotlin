package com.adamratzman.spotify.remote.wrapper.apis

import android.graphics.Bitmap
import com.adamratzman.spotify.remote.impl.SpotifyAppRemoteWrapper
import com.adamratzman.spotify.remote.wrapper.apis.responses.CallResult
import com.adamratzman.spotify.remote.wrapper.apis.responses.toLibraryCallResult
import com.spotify.protocol.types.Image.Dimension
import com.spotify.protocol.types.ImageUri

public class ImagesApiWrapper(private val spotifyAppRemoteWrapper: SpotifyAppRemoteWrapper) :
    SpotifyAppRemoteWrappedApi {
    public fun getImage(
        imageUri: ImageUri,
        dimension: Dimension? = null,
        callback: ((Bitmap) -> Unit)? = null
    ): CallResult<Bitmap, Bitmap> {
        return if (dimension == null) spotifyAppRemoteWrapper.mImagesApi.getImage(imageUri)
            .toLibraryCallResult({ it }, callback)
        else spotifyAppRemoteWrapper.mImagesApi.getImage(imageUri, dimension)
            .toLibraryCallResult({ it }, callback)
    }
}