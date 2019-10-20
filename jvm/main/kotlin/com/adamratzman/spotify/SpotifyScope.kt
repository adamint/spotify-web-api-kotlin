/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify

/**
 * Scopes provide Spotify users using third-party apps the confidence
 * that only the information they choose to share will be shared, and nothing more.
 *
 * Each represents a distinct privilege and may be required by one or more endpoints as discussed
 * on the [Spotify Authorization Documentation](https://developer.spotify.com/documentation/general/guides/scopes/)
 *
 * @property uri The scope id
 */
enum class SpotifyScope(val uri: String) {
    /**
     * Remote control playback of Spotify. This scope is currently available to Spotify iOS and Android App Remote SDKs.
     *
     * **Visible to users**: Communicate with the Spotify app on your device.
     */
    APP_REMOTE_CONTROL("app-remote-control"),

    /**
     * Read access to user's private playlists.
     *
     * **Visible to users**: Access your private playlists.
     */
    PLAYLIST_READ_PRIVATE("playlist-read-private"),

    /**
     * Include collaborative playlists when requesting a user's playlists.
     *
     * **Visible to users**: Access your collaborative playlists.
     */
    PLAYLIST_READ_COLLABORATIVE("playlist-read-collaborative"),

    /**
     * Write access to a user's public playlists.
     *
     * **Visible to users**: Manage your public playlists.
     */
    PLAYLIST_MODIFY_PUBLIC("playlist-modify-public"),

    /**
     * Write access to a user's private playlists.
     *
     * **Visible to users**: Manage your private playlists.
     */
    PLAYLIST_MODIFY_PRIVATE("playlist-modify-private"),

    /**
     * Control playback of a Spotify track. This scope is currently available to Spotify Playback SDKs, including the iOS SDK, Android SDK and Web Playback SDK. The user must have a Spotify Premium account.
     *
     * **Visible to users**: Play music and control playback on your other devices.
     */
    STREAMING("streaming"),

    /**
     * Let the application upload playlist covers and profile images
     *
     * **Visible to users**: Upload images to personalize your profile or playlist cover
     */
    UGC_IMAGE_UPLOAD("ugc-image-upload"),

    /**
     * Write/delete access to the list of artists and other users that the user follows.
     *
     * **Visible to users**: Manage who you are following.
     */
    USER_FOLLOW_MODIFY("user-follow-modify"),

    /**
     * Read access to the list of artists and other users that the user follows.
     *
     * **Visible to users**: Access your followers and who you are following.
     */
    USER_FOLLOW_READ("user-follow-read"),

    /**
     * Read access to a user's "Your Music" library.
     *
     * **Visible to users**: Access your saved tracks and albums.
     */
    USER_LIBRARY_READ("user-library-read"),

    /**
     * Write/delete access to a user's "Your Music" library.
     *
     * **Visible to users**: Manage your saved tracks and albums.
     */
    USER_LIBRARY_MODIFY("user-library-modify"),

    /**
     * Write access to a user’s playback state
     *
     * **Visible to users**: Control playback on your Spotify clients and Spotify Connect devices.
     */
    USER_MODIFY_PLAYBACK_STATE("user-modify-playback-state"),

    /**
     * Read access to user’s subscription details (type of user account).
     *
     * **Visible to users**: Access your subscription details.
     */
    USER_READ_PRIVATE("user-read-private"),

    /**
     * Read access to the user's birthdate.
     *
     * **Visible to users**: Receive your birthdate.
     */
    USER_READ_BIRTHDATE("user-read-birthdate"),

    /**
     * Read access to user’s email address.
     *
     * **Visible to users**: Get your real email address.
     */
    USER_READ_EMAIL("user-read-email"),

    /**
     * Read access to a user's top artists and tracks.
     *
     * **Visible to users**: Read your top artists and tracks.
     */
    USER_TOP_READ("user-top-read"),

    /**
     * Read access to a user’s player state.
     *
     * **Visible to users**: Read your currently playing track and Spotify Connect devices information.
     */
    USER_READ_PLAYBACK_STATE("user-read-playback-state"),

    /**
     * Read access to a user’s currently playing track
     *
     * **Visible to users**: Read your currently playing track
     */
    USER_READ_CURRENTLY_PLAYING("user-read-currently-playing"),

    /**
     * Read access to a user’s recently played tracks.
     *
     * **Visible to users**: Access your recently played items.
     */
    USER_READ_RECENTLY_PLAYED("user-read-recently-played");
}
