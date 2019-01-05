[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.utils](../index.md) / [AudioSegment](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`AudioSegment(start: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`, duration: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`, confidence: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`, loudnessStart: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`, loudnessMaxTime: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`, loudnessMax: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`, loudnessEnd: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`? = null, pitches: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`>, timbre: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`>)`

### Parameters

`start` - The starting point (in seconds) of the segment.

`duration` - The duration (in seconds) of the segment.

`confidence` - The confidence, from 0.0 to 1.0, of the reliability of the segmentation. Segments of the song which
are difficult to logically segment (e.g: noise) may correspond to low values in this field.

`loudnessStart` - The onset loudness of the segment in decibels (dB). Combined with loudness_max and
loudness_max_time, these components can be used to desctibe the “attack” of the segment.

`loudnessMaxTime` - The segment-relative offset of the segment peak loudness in seconds. Combined with
loudness_start and loudness_max, these components can be used to desctibe the “attack” of the segment.

`loudnessMax` - The peak loudness of the segment in decibels (dB). Combined with loudness_start and
loudness_max_time, these components can be used to desctibe the “attack” of the segment.

`loudnessEnd` - The offset loudness of the segment in decibels (dB). This value should be equivalent to the
loudness_start of the following segment.

`pitches` - A “chroma” vector representing the pitch content of the segment, corresponding to the 12 pitch classes
C, C#, D to B, with values ranging from 0 to 1 that describe the relative dominance of every pitch in the chromatic scale

`timbre` - Timbre is the quality of a musical note or sound that distinguishes different types of musical
instruments, or voices. Timbre vectors are best used in comparison with each other.