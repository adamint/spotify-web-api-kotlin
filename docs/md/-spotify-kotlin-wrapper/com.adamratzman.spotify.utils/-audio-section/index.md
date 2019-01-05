[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.utils](../index.md) / [AudioSection](./index.md)

# AudioSection

`data class AudioSection`

### Parameters

`start` - The starting point (in seconds) of the section.

`duration` - The duration (in seconds) of the section.

`confidence` - The confidence, from 0.0 to 1.0, of the reliability of the section’s “designation”.

`loudness` - The overall loudness of the section in decibels (dB). Loudness values are useful
for comparing relative loudness of sections within tracks.

`tempo` - The overall estimated tempo of the section in beats per minute (BPM). In musical terminology, tempo
is the speed or pace of a given piece and derives directly from the average beat duration.

`tempoConfidence` - The confidence, from 0.0 to 1.0, of the reliability of the tempo. Some tracks contain tempo
changes or sounds which don’t contain tempo (like pure speech) which would correspond to a low value in this field.

`key` - The estimated overall key of the section. The values in this field ranging from 0 to 11 mapping to
pitches using standard Pitch Class notation (E.g. 0 = C, 1 = C♯/D♭, 2 = D, and so on). If no key was detected,
the value is -1.

`keyConfidence` - The confidence, from 0.0 to 1.0, of the reliability of the key.
Songs with many key changes may correspond to low values in this field.

`mode` - Indicates the modality (major or minor) of a track, the type of scale from which its melodic content is
derived. This field will contain a 0 for “minor”, a 1 for “major”, or a -1 for no result. Note that the major key
(e.g. C major) could more likely be confused with the minor key at 3 semitones lower (e.g. A minor) as both
keys carry the same pitches.

`modeConfidence` - The confidence, from 0.0 to 1.0, of the reliability of the mode.

`timeSignature` - An estimated overall time signature of a track. The time signature (meter) is a notational
convention to specify how many beats are in each bar (or measure). The time signature ranges from 3 to 7
indicating time signatures of “3/4”, to “7/4”.

`timeSignatureConfidence` - The confidence, from 0.0 to 1.0, of the reliability of the time_signature.
Sections with time signature changes may correspond to low values in this field.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `AudioSection(start: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`, duration: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`, confidence: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`, loudness: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`, tempo: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`, tempoConfidence: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`, key: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, keyConfidence: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`, mode: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, modeConfidence: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`, timeSignature: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, timeSignatureConfidence: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`)` |

### Properties

| Name | Summary |
|---|---|
| [confidence](confidence.md) | `val confidence: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)<br>The confidence, from 0.0 to 1.0, of the reliability of the section’s “designation”. |
| [duration](duration.md) | `val duration: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)<br>The duration (in seconds) of the section. |
| [key](key.md) | `val key: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>The estimated overall key of the section. The values in this field ranging from 0 to 11 mapping to pitches using standard Pitch Class notation (E.g. 0 = C, 1 = C♯/D♭, 2 = D, and so on). If no key was detected, the value is -1. |
| [keyConfidence](key-confidence.md) | `val keyConfidence: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)<br>The confidence, from 0.0 to 1.0, of the reliability of the key. Songs with many key changes may correspond to low values in this field. |
| [loudness](loudness.md) | `val loudness: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)<br>The overall loudness of the section in decibels (dB). Loudness values are useful for comparing relative loudness of sections within tracks. |
| [mode](mode.md) | `val mode: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Indicates the modality (major or minor) of a track, the type of scale from which its melodic content is derived. This field will contain a 0 for “minor”, a 1 for “major”, or a -1 for no result. Note that the major key (e.g. C major) could more likely be confused with the minor key at 3 semitones lower (e.g. A minor) as both keys carry the same pitches. |
| [modeConfidence](mode-confidence.md) | `val modeConfidence: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)<br>The confidence, from 0.0 to 1.0, of the reliability of the mode. |
| [start](start.md) | `val start: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)<br>The starting point (in seconds) of the section. |
| [tempo](tempo.md) | `val tempo: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)<br>The overall estimated tempo of the section in beats per minute (BPM). In musical terminology, tempo is the speed or pace of a given piece and derives directly from the average beat duration. |
| [tempoConfidence](tempo-confidence.md) | `val tempoConfidence: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)<br>The confidence, from 0.0 to 1.0, of the reliability of the tempo. Some tracks contain tempo changes or sounds which don’t contain tempo (like pure speech) which would correspond to a low value in this field. |
| [timeSignature](time-signature.md) | `val timeSignature: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>An estimated overall time signature of a track. The time signature (meter) is a notational convention to specify how many beats are in each bar (or measure). The time signature ranges from 3 to 7 indicating time signatures of “3/4”, to “7/4”. |
| [timeSignatureConfidence](time-signature-confidence.md) | `val timeSignatureConfidence: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)<br>The confidence, from 0.0 to 1.0, of the reliability of the time_signature. Sections with time signature changes may correspond to low values in this field. |
