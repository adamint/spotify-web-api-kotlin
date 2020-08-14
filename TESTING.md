# Testing

We use [Spek](https://github.com/spekframework/spek) to run unit tests. You must add Maven Central to the gradle repositories 
in order to pull Spek.

You must create a Spotify application [here](https://developer.spotify.com/dashboard/applications) to get credentials.

To run **only** public endpoint tests, run

`gradle jvmTest` and `gradle testDebugUnitTest`

Note: You must have `SPOTIFY_CLIENT_ID` and `SPOTIFY_CLIENT_SECRET` as environment variables.

To run **all** tests, you need a valid Spotify application, redirect uri, and token string. use:

`gradle jvmTest` and `gradle testDebugUnitTest`

Note: In addition to `SPOTIFY_CLIENT_ID` and `SPOTIFY_CLIENT_SECRET`, you also must have the following environment 
variables set up: `SPOTIFY_REDIRECT_URI` and `SPOTIFY_TOKEN_STRING`

Some tests may fail if you do not allow access to all required scopes. To mitigate this, you can individually grant 
each scope or use the following code snippet to print out the Spotify token string (given a generated authorization code)

To build the maven artifact locally, you will need to follow these steps:
- Create `gradle.properties` if it doesn't exist already.
- Follow [this guide](https://gist.github.com/phit/bd3c6d156a2fa5f3b1bc15fa94b3256c). Instead of `.gpg` extension, use `.kbx` for your secring.