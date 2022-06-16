package com.adamratzman.spotify.remote.wrapper.apis

import com.adamratzman.spotify.remote.impl.SpotifyAppRemoteWrapper
import com.adamratzman.spotify.remote.wrapper.apis.responses.CallResult
import com.adamratzman.spotify.remote.wrapper.apis.responses.toLibraryCallResult
import com.spotify.protocol.types.Empty
import com.spotify.protocol.types.ListItem
import com.spotify.protocol.types.ListItems
import kotlinx.serialization.Serializable

public class ContentApiWrapper(private val spotifyAppRemoteWrapper: SpotifyAppRemoteWrapper) :
    SpotifyAppRemoteWrappedApi {
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

    public fun getChildrenOfItem(
        item: ListItem,
        perPage: Int,
        offset: Int,
        callback: ((ListItemsWrapper) -> Unit)? = null
    ): CallResult<ListItems, ListItemsWrapper> {
        return spotifyAppRemoteWrapper.contentApi.getChildrenOfItem(item, perPage, offset)
            .toLibraryCallResult({ convertListItemsToListItemsWrapper(it) }, callback)
    }

    public fun playContentItem(item: ListItem, callback: ((Unit) -> Unit)? = null): CallResult<Empty, Unit> {
        return spotifyAppRemoteWrapper.contentApi.playContentItem(item)
            .toLibraryCallResult({ }, callback)
    }
}

public enum class RecommendedContentType(public val value: String) {
    Automotive("automotive"),
    Default("default"),
    Navigation("navigation"),
    Fitness("fitness"),
    Wake("wake"),
    Sleep("sleep")
}

@Serializable
public data class ListItemsWrapper(
    val limit: Int,
    val offset: Int,
    val total: Int,
    val items: List<ListItemWrapper>
)

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

@Serializable
public data class ImageUriWrapper(
    val rawImageData: String
)