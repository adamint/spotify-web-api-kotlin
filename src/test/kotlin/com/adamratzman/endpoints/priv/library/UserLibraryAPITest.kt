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
}