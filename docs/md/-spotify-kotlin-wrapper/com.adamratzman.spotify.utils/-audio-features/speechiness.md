[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.utils](../index.md) / [AudioFeatures](index.md) / [speechiness](./speechiness.md)

# speechiness

`val speechiness: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)

Speechiness detects the presence of spoken words in a track. The more exclusively
speech-like the recording (e.g. talk show, audio book, poetry), the closer to 1.0 the attribute value.
Values above 0.66 describe tracks that are probably made entirely of spoken words. Values between 0.33
and 0.66 describe tracks that may contain both music and speech, either in sections or layered, including
such cases as rap music. Values below 0.33 most likely represent music and other non-speech-like tracks.

### Property

`speechiness` - Speechiness detects the presence of spoken words in a track. The more exclusively
speech-like the recording (e.g. talk show, audio book, poetry), the closer to 1.0 the attribute value.
Values above 0.66 describe tracks that are probably made entirely of spoken words. Values between 0.33
and 0.66 describe tracks that may contain both music and speech, either in sections or layered, including
such cases as rap music. Values below 0.33 most likely represent music and other non-speech-like tracks.