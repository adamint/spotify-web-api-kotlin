/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2022; Original author: Adam Ratzman */
package com.adamratzman.spotify

/**
 * Scopes provide Spotify users using third-party apps the confidence
 * that only the information they choose to share will be shared, and nothing more.
 *
 * Each represents a distinct privilege and may be required by one or more endpoints as discussed
 * on the [Spotify Authorization Documentation](https://developer.spotify.com/documentation/general/guides/scopes/)
 *
 * @param uri The scope id
 */
public enum class SpotifyScope(public val uri: String) {
    /**
     * Remote control playback of Spotify. This scope is currently available to Spotify iOS and Android App Remote SDKs.
     *
     * **Visible to users**: Communicate with the Spotify app on your device.
     */
    AppRemoteControl("app-remote-control"),

    /**
     * Read access to user's private playlists.
     *
     * **Visible to users**: Access your private playlists.
     */
    PlaylistReadPrivate("playlist-read-private"),

    /**
     * Include collaborative playlists when requesting a user's playlists.
     *
     * **Visible to users**: Access your collaborative playlists.
     */
    PlaylistReadCollaborative("playlist-read-collaborative"),

    /**
     * Write access to a user's public playlists.
     *
     * **Visible to users**: Manage your public playlists.
     */
    PlaylistModifyPublic("playlist-modify-public"),

    /**
     * Write access to a user's private playlists.
     *
     * **Visible to users**: Manage your private playlists.
     */
    PlaylistModifyPrivate("playlist-modify-private"),

    /**
     * Control playback of a Spotify track. This scope is currently available to Spotify Playback SDKs, including the iOS SDK, Android SDK and Web Playback SDK. The user must have a Spotify Premium account.
     *
     * **Visible to users**: Play music and control playback on your other devices.
     */
    Streaming("streaming"),

    /**
     * Let the application upload playlist covers and profile images
     *
     * **Visible to users**: Upload images to personalize your profile or playlist cover
     */
    UgcImageUpload("ugc-image-upload"),

    /**
     * Write/delete access to the list of artists and other users that the user follows.
     *
     * **Visible to users**: Manage who you are following.
     */
    UserFollowModify("user-follow-modify"),

    /**
     * Read access to the list of artists and other users that the user follows.
     *
     * **Visible to users**: Access your followers and who you are following.
     */
    UserFollowRead("user-follow-read"),

    /**
     * Read access to a user's "Your Music" library.
     *
     * **Visible to users**: Access your saved tracks and albums.
     */
    UserLibraryRead("user-library-read"),

    /**
     * Write/delete access to a user's "Your Music" library.
     *
     * **Visible to users**: Manage your saved tracks and albums.
     */
    UserLibraryModify("user-library-modify"),

    /**
     * Write access to a user’s playback state
     *
     * **Visible to users**: Control playback on your Spotify clients and Spotify Connect devices.
     */
    UserModifyPlaybackState("user-modify-playback-state"),

    /**
     * Read access to user’s subscription details (type of user account).
     *
     * **Visible to users**: Access your subscription details.
     */
    UserReadPrivate("user-read-private"),

    /**
     * Read access to user’s email address.
     *
     * **Visible to users**: Get your real email address.
     */
    UserReadEmail("user-read-email"),

    /**
     * Read access to a user's top artists and tracks.
     *
     * **Visible to users**: Read your top artists and tracks.
     */
    UserTopRead("user-top-read"),

    /**
     * Read access to a user’s player state.
     *
     * **Visible to users**: Read your currently playing track and Spotify Connect devices information.
     */
    UserReadPlaybackState("user-read-playback-state"),

    /**
     * Read access to a user’s playback position in a content.
     *
     * **Visible to users**: Read your position in content you have played.
     */
    UserReadPlaybackPosition("user-read-playback-position"),

    /**
     * Read access to a user’s currently playing track
     *
     * **Visible to users**: Read your currently playing track
     */
    UserReadCurrentlyPlaying("user-read-currently-playing"),

    /**
     * Read access to a user’s recently played tracks.
     *
     * **Visible to users**: Access your recently played items.
     */
    UserReadRecentlyPlayed("user-read-recently-played");
}
