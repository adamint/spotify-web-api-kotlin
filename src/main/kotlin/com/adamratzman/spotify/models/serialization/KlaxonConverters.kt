/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.models.serialization

import com.adamratzman.spotify.SpotifyAPI
import com.adamratzman.spotify.models.Album
import com.adamratzman.spotify.models.FeaturedPlaylists
import com.adamratzman.spotify.models.Followers
import com.adamratzman.spotify.models.Playlist
import com.adamratzman.spotify.models.SavedTrack
import com.adamratzman.spotify.models.SpotifyPublicUser
import com.beust.klaxon.Converter
import com.beust.klaxon.JsonValue

internal fun getPublicUserConverter(api: SpotifyAPI) = object : Converter {
    override fun canConvert(cls: Class<*>): Boolean {
        return cls == SpotifyPublicUser::class.java
    }

    override fun fromJson(jv: JsonValue): Any? {
        return jv.obj?.let { obj ->
            if (obj.string("id")?.isNotBlank() == true) {
                SpotifyPublicUser(
                        obj.obj("external_urls")!!.map.mapValues { it.value as String },
                        obj.string("href")!!,
                        obj.string("id")!!,
                        obj.string("uri")!!,
                        obj.string("display_name"),
                        obj.obj("followers")?.let { api.klaxon.parseFromJsonObject<Followers>(it) }
                                ?: Followers(null, -1),
                        obj.array("images") ?: listOf(),
                        obj.string("type")!!
                        )
            } else {
                // this is the *Spotify* user, and is SOMEHOW broken
                SpotifyPublicUser(
                        _externalUrls = hashMapOf(), _href = "https://api.spotify.com/v1/users/spotify",
                        _id = "spotify", type = "user", _uri = "spotify:user:spotify"
                )
            }
        }
    }

    override fun toJson(value: Any): String = api.klaxon.toJsonString(value)
}

internal fun getSavedTrackConverter(api: SpotifyAPI) = object : Converter {
    override fun canConvert(cls: Class<*>): Boolean {
        return cls == SavedTrack::class.java
    }

    override fun fromJson(jv: JsonValue): Any? {
        return jv.obj?.let { obj ->
            SavedTrack(
                    obj.string("added_at")!!,
                    api.klaxon.parseFromJsonObject(obj.obj("track")!!)!!
            )
        }
    }

    override fun toJson(value: Any): String = api.klaxon.toJsonString(value)
}

internal fun getAlbumConverter(api: SpotifyAPI) = object : Converter {
    override fun canConvert(cls: Class<*>): Boolean {
        return cls == Album::class.java
    }

    override fun fromJson(jv: JsonValue): Any? {
        return jv.obj?.let { obj ->
            return Album(
                    obj.string("album_type")!!,
                    obj.array("available_markets") ?: listOf(),
                    obj.obj("external_urls")!!.map.mapValues { it.value as String },
                    obj.obj("external_ids")!!.map.mapValues { it.value as String },
                    obj.string("href")!!,
                    obj.string("id")!!,
                    obj.string("uri")!!,
                    obj.array("artists")!!,
                    obj.array("copyrights")!!,
                    obj.array("genres")!!,
                    obj.array("images")!!,
                    obj.string("label")!!,
                    obj.string("name")!!,
                    obj.int("popularity")!!,
                    obj.string("release_date")!!,
                    obj.string("release_date_precision")!!,
                    obj.obj("tracks")!!.toJsonString().toPagingObject(endpoint = api.tracks),
                    obj.string("type")!!,
                    obj.int("total_tracks")!!
            )
        }
    }

    override fun toJson(value: Any): String = api.klaxon.toJsonString(value)
}

internal fun getFeaturedPlaylistsConverter(api: SpotifyAPI) = object : Converter {
    override fun canConvert(cls: Class<*>): Boolean {
        return cls == FeaturedPlaylists::class.java
    }

    override fun fromJson(jv: JsonValue): Any? {
        return jv.obj?.let { obj ->
            FeaturedPlaylists(
                    obj.string("message")!!,
                    obj.obj("playlists")!!.toJsonString().toPagingObject(endpoint = api.tracks)
            )
        }
    }

    override fun toJson(value: Any): String = api.klaxon.toJsonString(value)
}

internal fun getPlaylistConverter(api: SpotifyAPI) = object : Converter {
    override fun canConvert(cls: Class<*>): Boolean {
        return cls == Playlist::class.java
    }

    override fun fromJson(jv: JsonValue): Any? {
        return jv.obj?.let { obj ->
            return Playlist(
                    obj.obj("external_urls")!!.map.mapValues { it.value as String },
                    obj.string("href")!!,
                    obj.string("id")!!,
                    obj.string("uri")!!,
                    obj.boolean("collaborative")!!,
                    obj.string("description")!!,
                    api.klaxon.parseFromJsonObject(obj.obj("followers")!!)!!,
                    obj.string("primary_color"),
                    obj.array("images")!!,
                    obj.string("name")!!,
                    api.klaxon.parseFromJsonObject(obj.obj("owner")!!)!!,
                    obj.boolean("public"),
                    obj.string("snapshot_id")!!,
                    obj.obj("tracks")!!.toJsonString().toPagingObject(endpoint = api.tracks),
                    obj.string("type")!!
            )
        }
    }

    override fun toJson(value: Any): String = api.klaxon.toJsonString(value)
}