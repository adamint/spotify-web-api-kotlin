[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.utils](../index.md) / [SimpleAlbum](index.md) / [restrictions](./restrictions.md)

# restrictions

`val restrictions: `[`Restrictions`](../-restrictions/index.md)`?`

Part of the response when Track Relinking is applied, the original track is not available
in the given market, and Spotify did not have any tracks to relink it with. The track response will still contain
metadata for the original track, and a restrictions object containing the reason why the track is not available:
"restrictions" : {"reason" : "market"}

### Property

`restrictions` - Part of the response when Track Relinking is applied, the original track is not available
in the given market, and Spotify did not have any tracks to relink it with. The track response will still contain
metadata for the original track, and a restrictions object containing the reason why the track is not available:
"restrictions" : {"reason" : "market"}