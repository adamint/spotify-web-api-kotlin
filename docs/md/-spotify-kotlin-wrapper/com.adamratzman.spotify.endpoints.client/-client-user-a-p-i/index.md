[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.endpoints.client](../index.md) / [ClientUserAPI](./index.md)

# ClientUserAPI

`class ClientUserAPI : `[`UserAPI`](../../com.adamratzman.spotify.endpoints.public/-user-a-p-i/index.md)

Endpoints for retrieving information about a user’s profile.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `ClientUserAPI(api: `[`SpotifyAPI`](../../com.adamratzman.spotify.main/-spotify-a-p-i/index.md)`)`<br>Endpoints for retrieving information about a user’s profile. |

### Functions

| Name | Summary |
|---|---|
| [getUserProfile](get-user-profile.md) | `fun getUserProfile(): `[`SpotifyRestAction`](../../com.adamratzman.spotify.main/-spotify-rest-action/index.md)`<`[`SpotifyUserInformation`](../../com.adamratzman.spotify.utils/-spotify-user-information/index.md)`>`<br>Get detailed profile information about the current user (including the current user’s username). |

### Inherited Functions

| Name | Summary |
|---|---|
| [getProfile](../../com.adamratzman.spotify.endpoints.public/-user-a-p-i/get-profile.md) | `fun getProfile(user: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`SpotifyRestAction`](../../com.adamratzman.spotify.main/-spotify-rest-action/index.md)`<`[`SpotifyPublicUser`](../../com.adamratzman.spotify.utils/-spotify-public-user/index.md)`?>`<br>Get public profile information about a Spotify user. |
