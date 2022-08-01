package com.adamratzman.spotify.remote.wrapper.apis

import com.adamratzman.spotify.models.PagingObject
import com.adamratzman.spotify.models.Playable
import com.adamratzman.spotify.remote.impl.SpotifyAppRemoteWrapper
import com.adamratzman.spotify.remote.wrapper.SpotifyAppRemoteApi
import com.adamratzman.spotify.remote.wrapper.apis.responses.CallResult
import com.adamratzman.spotify.remote.wrapper.apis.responses.toLibraryCallResult
import com.spotify.protocol.types.Empty
import com.spotify.protocol.types.ListItem
import com.spotify.protocol.types.ListItems
import kotlinx.serialization.Serializable

/**
 * Get a list of content or start playback of a playable content item.
 *
 * Get an instance of a ContentApi with [SpotifyAppRemoteApi.contentApi].
 */
public class ContentApiWrapper(private val spotifyAppRemoteWrapper: SpotifyAppRemoteWrapper) :
    SpotifyAppRemoteWrappedApi {
    /**
     * Get a list of recommended content.
     *
     * @param type The type of content you want to fetch. May be one of defined [RecommendedContentType]
     * @param callback The callback to be invoked after value production. Optional, you can also use [CallResult.execute] to await
     * a value from a suspending context (recommended).
     */
    public fun getRecommendedContentItems(
        type: RecommendedContentType,
        callback: ((ListItemsWrapper) -> Unit)? = null
    ): CallResult<ListItems, ListItemsWrapper> {
        return spotifyAppRemoteWrapper.contentApi.getRecommendedContentItems(type.value)
            .toLibraryCallResult({ convertListItemsToListItemsWrapper(it) }, callback)
    }

    private fun convertListItemsToListItemsWrapper(listItems: ListItems): ListItemsWrapper {
        return with(listItems) {
            ListItemsWrapper(
                limit,
                offset,
                total,
                items.map { listItem ->
                    with(listItem) {
                        ListItemWrapper(
                            id,
                            uri,
                            imageUri?.raw?.let { ImageUriWrapper(it) },
                            title,
                            subtitle,
                            playable,
                            hasChildren
                        )
                    }
                }
            )
        }
    }

    /**
     * Get a list of child items of a browsable (non-playable) content item.
     *
     * @param item The content item (retrieved from [getRecommendedContentItems]) of which you want to access the children.
     * @param perPage The number of children to fetch
     * @param offset Index of first child item to fetch, 0-indexed
     * @param callback The callback to be invoked after value production. Optional, you can also use [CallResult.execute] to await
     * a value from a suspending context (recommended).
     */
    public fun getChildrenOfItem(
        item: ListItem,
        perPage: Int,
        offset: Int,
        callback: ((ListItemsWrapper) -> Unit)? = null
    ): CallResult<ListItems, ListItemsWrapper> {
        return spotifyAppRemoteWrapper.contentApi.getChildrenOfItem(item, perPage, offset)
            .toLibraryCallResult({ convertListItemsToListItemsWrapper(it) }, callback)
    }

    /**
     * Start playback of a playable ListItem. A ListItem is playable if [ListItem.playable] returns true.
     *
     * @param item The ListItem which you want to play.
     * @param callback The callback to be invoked after value production. Optional, you can also use [CallResult.execute] to await
     * a value from a suspending context (recommended).
     */
    public fun playContentItem(item: ListItem, callback: ((Unit) -> Unit)? = null): CallResult<Empty, Unit> {
        return spotifyAppRemoteWrapper.contentApi.playContentItem(item)
            .toLibraryCallResult({ }, callback)
    }
}

/**
 * Music type to be returned in recommendations.
 */
public enum class RecommendedContentType(internal val value: String) {
    Automotive("automotive"),
    Default("default"),
    Navigation("navigation"),
    Fitness("fitness"),
    Wake("wake"),
    Sleep("sleep")
}

/**
 * Represents a list of [ListItem], similar to a [PagingObject].
 *
 * @param limit Max number of items to return.
 * @param offset The offset at which to start returning results.
 * @param total The total amount of items.
 * @param items The list items associated with this request.
 */
@Serializable
public data class ListItemsWrapper(
    val limit: Int,
    val offset: Int,
    val total: Int,
    val items: List<ListItemWrapper>
)

/**
 * A list item (similar to a playable item, or [Playable]).
 *
 * @param id The Spotify id associated with this playable.
 * @param uri The Spotify uri associated with this playable.
 * @param imageUri The cover image associated with this playable.
 * @param title The title of the playable.
 * @param subtitle The subtitle of the playable, if any.
 * @param isPlayable Whether the playable can be played using [PlayerApiWrapper.play].
 * @param doesHaveChildren Whether the playable has child playables.
 *
 */
@Serializable
public data class ListItemWrapper(
    val id: String?,
    val uri: String?,
    val imageUri: ImageUriWrapper?,
    val title: String?,
    val subtitle: String?,
    val isPlayable: Boolean,
    val doesHaveChildren: Boolean
)

/**
 * A wrapper around Spotify ImageUri, containing raw image data.
 *
 * @param rawImageData The raw image data.
 */
@Serializable
public data class ImageUriWrapper(
    val rawImageData: String
)