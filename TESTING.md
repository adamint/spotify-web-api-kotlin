# Testing

We use the multiplatform kotlin.test framework to run tests.

You must create a Spotify application [here](https://developer.spotify.com/dashboard/applications) to get credentials.

To run **only** public endpoint tests, you only need `SPOTIFY_CLIENT_ID` and `SPOTIFY_CLIENT_SECRET` as environment variables.

To additionally run **all** private (client) endpoint tests, you need a valid Spotify application, redirect uri, and token string. 
The additional environment variables you will need to add are `SPOTIFY_REDIRECT_URI` and `SPOTIFY_TOKEN_STRING`.

To specifically run player tests, you must include the `SPOTIFY_ENABLE_PLAYER_TESTS`=true environment variable.

Some tests may fail if you do not allow access to all required scopes. To mitigate this, you can individually grant
each scope or use the following code snippet to print out the Spotify token string (given a generated authorization code). 
However, you can painlessly generate a valid token by using this site: https://adamratzman.com/projects/spotify/generate-token

To run tests, run `gradle jvmTest`, `gradle macosX64Test`, `gradle testDebugUnitTest`, or any other target.

To output all http requests to the console, set the `SPOTIFY_LOG_HTTP`=true environment variable.

To build the maven artifact locally, you will need to follow these steps:
- Create `gradle.properties` if it doesn't exist already.
- Follow [this guide](https://gist.github.com/phit/bd3c6d156a2fa5f3b1bc15fa94b3256c). Instead of `.gpg` extension, use `.kbx` for your secring.
- Run `gradle publishToMavenLocal`

You can use this artifact to test locally by adding the `mavenLocal()` repository in any local gradle project.

To build docs, run `gradle dokka`. They will be located under the docs directory in the repostiory root, and 
are ignored. This is how we generate release docs.