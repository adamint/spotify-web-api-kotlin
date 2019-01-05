[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.endpoints.public](../index.md) / [UserAPI](./index.md)

# UserAPI

`open class UserAPI : `[`SpotifyEndpoint`](../../com.adamratzman.spotify.utils/-spotify-endpoint/index.md)

Endpoints for retrieving information about a user’s profile.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `UserAPI(api: `[`SpotifyAPI`](../../com.adamratzman.spotify.main/-spotify-a-p-i/index.md)`)`<br>Endpoints for retrieving information about a user’s profile. |

### Inherited Properties

| Name | Summary |
|---|---|
| [api](../../com.adamratzman.spotify.utils/-spotify-endpoint/api.md) | `val api: `[`SpotifyAPI`](../../com.adamratzman.spotify.main/-spotify-a-p-i/index.md) |

### Functions

| Name | Summary |
|---|---|
| [getProfile](get-profile.md) | `fun getProfile(user: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`SpotifyRestAction`](../../com.adamratzman.spotify.main/-spotify-rest-action/index.md)`<`[`SpotifyPublicUser`](../../com.adamratzman.spotify.utils/-spotify-public-user/index.md)`?>`<br>Get public profile information about a Spotify user. |

### Inherited Functions

| Name | Summary |
|---|---|
| [toAction](../../com.adamratzman.spotify.utils/-spotify-endpoint/to-action.md) | `fun <T> toAction(supplier: `[`Supplier`](http://docs.oracle.com/javase/8/docs/api/java/util/function/Supplier.html)`<`[`T`](../../com.adamratzman.spotify.utils/-spotify-endpoint/to-action.md#T)`>): `[`SpotifyRestAction`](../../com.adamratzman.spotify.main/-spotify-rest-action/index.md)`<`[`T`](../../com.adamratzman.spotify.utils/-spotify-endpoint/to-action.md#T)`>` |
| [toActionPaging](../../com.adamratzman.spotify.utils/-spotify-endpoint/to-action-paging.md) | `fun <Z, T : `[`AbstractPagingObject`](../../com.adamratzman.spotify.utils/-abstract-paging-object/index.md)`<`[`Z`](../../com.adamratzman.spotify.utils/-spotify-endpoint/to-action-paging.md#Z)`>> toActionPaging(supplier: `[`Supplier`](http://docs.oracle.com/javase/8/docs/api/java/util/function/Supplier.html)`<`[`T`](../../com.adamratzman.spotify.utils/-spotify-endpoint/to-action-paging.md#T)`>): `[`SpotifyRestActionPaging`](../../com.adamratzman.spotify.main/-spotify-rest-action-paging/index.md)`<`[`Z`](../../com.adamratzman.spotify.utils/-spotify-endpoint/to-action-paging.md#Z)`, `[`T`](../../com.adamratzman.spotify.utils/-spotify-endpoint/to-action-paging.md#T)`>` |

### Inheritors

| Name | Summary |
|---|---|
| [ClientUserAPI](../../com.adamratzman.spotify.endpoints.client/-client-user-a-p-i/index.md) | `class ClientUserAPI : `[`UserAPI`](./index.md)<br>Endpoints for retrieving information about a user’s profile. |
