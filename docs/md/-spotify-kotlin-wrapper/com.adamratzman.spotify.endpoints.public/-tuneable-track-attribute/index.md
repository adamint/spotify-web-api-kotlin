[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.endpoints.public](../index.md) / [TuneableTrackAttribute](./index.md)

# TuneableTrackAttribute

`enum class TuneableTrackAttribute`

Describes a track attribute

### Parameters

`attribute` - the spotify id for the track attribute

### Enum Values

| Name | Summary |
|---|---|
| [ACOUSTICNESS](-a-c-o-u-s-t-i-c-n-e-s-s.md) | A confidence measure from 0.0 to 1.0 of whether the track is acoustic. 1.0 represents high confidence the track is acoustic. |
| [DANCEABILITY](-d-a-n-c-e-a-b-i-l-i-t-y.md) | Danceability describes how suitable a track is for dancing based on a combination of musical elements including tempo, rhythm stability, beat strength, and overall regularity. A value of 0.0 is least danceable and 1.0 is most danceable. |
| [DURATION_IN_MILLISECONDS](-d-u-r-a-t-i-o-n_-i-n_-m-i-l-l-i-s-e-c-o-n-d-s.md) | The duration of the track in milliseconds. |
| [ENERGY](-e-n-e-r-g-y.md) | Energy is a measure from 0.0 to 1.0 and represents a perceptual measure of intensity and activity. Typically, energetic tracks feel fast, loud, and noisy. For example, death metal has high energy, while a Bach prelude scores low on the scale. Perceptual features contributing to this attribute include dynamic range, perceived loudness, timbre, onset rate, and general entropy. |
| [INSTRUMENTALNESS](-i-n-s-t-r-u-m-e-n-t-a-l-n-e-s-s.md) | Predicts whether a track contains no vocals. “Ooh” and “aah” sounds are treated as instrumental in this context. Rap or spoken word tracks are clearly “vocal”. The closer the instrumentalness value is to 1.0, the greater likelihood the track contains no vocal content. Values above 0.5 are intended to represent instrumental tracks, but confidence is higher as the value approaches 1.0. |
| [KEY](-k-e-y.md) | The key the track is in. Integers map to pitches using standard Pitch Class notation. E.g. 0 = C, 1 = C♯/D♭, 2 = D, and so on. |
| [LIVENESS](-l-i-v-e-n-e-s-s.md) | Detects the presence of an audience in the recording. Higher liveness values represent an increased probability that the track was performed live. A value above 0.8 provides strong likelihood that the track is live. |
| [LOUDNESS](-l-o-u-d-n-e-s-s.md) | The overall loudness of a track in decibels (dB). Loudness values are averaged across the entire track and are useful for comparing relative loudness of tracks. Loudness is the quality of a sound that is the primary psychological correlate of physical strength (amplitude). Values typical range between -60 and 0 db. |
| [MODE](-m-o-d-e.md) | Mode indicates the modality (major or minor) of a track, the type of scale from which its melodic content is derived. Major is represented by 1 and minor is 0. |
| [POPULARITY](-p-o-p-u-l-a-r-i-t-y.md) | The popularity of the track. The value will be between 0 and 100, with 100 being the most popular. The popularity is calculated by algorithm and is based, in the most part, on the total number of plays the track has had and how recent those plays are. Note: When applying track relinking via the market parameter, it is expected to find relinked tracks with popularities that do not match min_*, max_*and target* popularities. These relinked tracks are accurate replacements for unplayable tracks with the expected popularity scores. Original, non-relinked tracks are available via the linked_from attribute of the relinked track response. |
| [SPEECHINESS](-s-p-e-e-c-h-i-n-e-s-s.md) | Speechiness detects the presence of spoken words in a track. The more exclusively speech-like the recording (e.g. talk show, audio book, poetry), the closer to 1.0 the attribute value. Values above 0.66 describe tracks that are probably made entirely of spoken words. Values between 0.33 and 0.66 describe tracks that may contain both music and speech, either in sections or layered, including such cases as rap music. Values below 0.33 most likely represent music and other non-speech-like tracks. |
| [TEMPO](-t-e-m-p-o.md) | The overall estimated tempo of a track in beats per minute (BPM). In musical terminology, tempo is the speed or pace of a given piece and derives directly from the average beat duration. |
| [TIME_SIGNATURE](-t-i-m-e_-s-i-g-n-a-t-u-r-e.md) | An estimated overall time signature of a track. The time signature (meter) is a notational convention to specify how many beats are in each bar (or measure). |
| [VALENCE](-v-a-l-e-n-c-e.md) | A measure from 0.0 to 1.0 describing the musical positiveness conveyed by a track. Tracks with high valence sound more positive (e.g. happy, cheerful, euphoric), while tracks with low valence sound more negative (e.g. sad, depressed, angry). |

### Functions

| Name | Summary |
|---|---|
| [toString](to-string.md) | `fun toString(): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
