package com.adamratzman.endpoints.priv.library

import com.adamratzman.main.clientApi
import org.junit.Test

import org.junit.Assert.*

class UserLibraryAPITest {
    @Test
    fun getSavedTracks() {
        println(clientApi.userLibrary.getSavedTracks())
    }
    @Test
    fun getSavedAlbums() {
        println(clientApi.userLibrary.getSavedAlbums())
    }

    @Test
    fun savedTracksContains() {
        println(clientApi.userLibrary.savedTracksContains("0udZHhCi7p1YzMlvI4fXoK", "3SF5puV5eb6bgRSxBeMOk9"))
    }

    @Test
    fun savedAlbumsContains() {
        println(clientApi.userLibrary.savedAlbumsContains("5GFNkpB5E3L6LFlkqpQvQv"))
    }

    @Test
    fun saveTracks() {
        println(clientApi.userLibrary.saveTracks("2gpgza83pGT4mJYjvb5cZo"))
    }

    @Test
    fun saveAlbums() {
        println(clientApi.userLibrary.saveAlbums("7rDvst38yYrJFGqs4W25Y8"))
    }

    @Test
    fun removeSavedTracks() {
        println(clientApi.userLibrary.removeSavedTracks("2gpgza83pGT4mJYjvb5cZo"))
    }

    @Test
    fun removeSavedAlbums() {
        println(clientApi.userLibrary.removeSavedAlbums("7rDvst38yYrJFGqs4W25Y8"))
    }
}