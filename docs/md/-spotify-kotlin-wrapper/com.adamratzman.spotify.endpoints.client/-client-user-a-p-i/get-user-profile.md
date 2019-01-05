[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.endpoints.client](../index.md) / [ClientUserAPI](index.md) / [getUserProfile](./get-user-profile.md)

# getUserProfile

`fun getUserProfile(): `[`SpotifyRestAction`](../../com.adamratzman.spotify.main/-spotify-rest-action/index.md)`<`[`SpotifyUserInformation`](../../com.adamratzman.spotify.utils/-spotify-user-information/index.md)`>`

Get detailed profile information about the current user (including the current user’s username).

The access token must have been issued on behalf of the current user.
Reading the user’s email address requires the user-read-email scope; reading country and product subscription level
requires the user-read-private scope. Reading the user’s birthdate requires the user-read-birthdate scope.

**Return**
Never-null [SpotifyUserInformation](../../com.adamratzman.spotify.utils/-spotify-user-information/index.md) object with possibly-null country, email, subscription and birthday fields

