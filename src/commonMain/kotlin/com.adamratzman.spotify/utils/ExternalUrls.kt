/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2022; Original author: Adam Ratzman */
package com.adamratzman.spotify.utils

import com.adamratzman.spotify.models.ExternalUrl
import kotlinx.serialization.Serializable

/**
 * Represents the external urls associated with this Spotify asset.
 *
 * @param spotify The associated Spotify link for this asset, if one exists.
 * @param otherExternalUrls Other external urls, not including [spotify]
 */
@Serializable
public data class ExternalUrls(
    val spotify: String? = null,
    val otherExternalUrls: List<ExternalUrl>,
    private val allExternalUrls: List<ExternalUrl>
) : List<ExternalUrl> {
    override val size: Int = allExternalUrls.size
    override fun contains(element: ExternalUrl): Boolean = allExternalUrls.contains(element)
    override fun containsAll(elements: Collection<ExternalUrl>): Boolean = allExternalUrls.containsAll(elements)
    override fun get(index: Int): ExternalUrl = allExternalUrls.get(index)
    override fun indexOf(element: ExternalUrl): Int = allExternalUrls.indexOf(element)
    override fun isEmpty(): Boolean = allExternalUrls.isEmpty()
    override fun iterator(): Iterator<ExternalUrl> = allExternalUrls.iterator()
    override fun lastIndexOf(element: ExternalUrl): Int = allExternalUrls.lastIndexOf(element)
    override fun listIterator(): ListIterator<ExternalUrl> = allExternalUrls.listIterator()
    override fun listIterator(index: Int): ListIterator<ExternalUrl> = allExternalUrls.listIterator(index)
    override fun subList(fromIndex: Int, toIndex: Int): List<ExternalUrl> = allExternalUrls.subList(fromIndex, toIndex)
}

public fun getExternalUrls(externalUrlsString: Map<String, String>): ExternalUrls {
    val externalUrls = externalUrlsString.map { ExternalUrl(it.key, it.value) }

    return ExternalUrls(
        externalUrlsString["spotify"],
        externalUrls.filter { it.name != "spotify" },
        externalUrls
    )
}
