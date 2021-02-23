# spotify-web-api-kotlin Android target extended features

Please also read the Android section of the spotify-web-api-kotlin readme, as it contains details about how to support
non-debug/release release types.

spotify-web-api-kotlin contains wrappers around Spotify's `android-auth` and `android-sdk` (Spotify remote) libraries
that make it easier to implement authentication and playback features, while only needing to learn how to use one
library.

## Table of Contents

* [Sample application](#sample-application)
* [Authentication](#authentication)
    + [Credential store](#spotify-credential-store)
    + [Authentication prerequisites](#authentication-prerequisites)
    + [PKCE auth (refreshable client authentication)](#pkce-auth)
    + [spotify-auth integration (implicit auth)](#implicit-auth)
    + [Using SpotifyApi in your application](#using-spotifyapi-in-your-application)
* [spotify-remote integration (WIP)](#spotify-remote-integration)
* [Broadcast Notifications](#broadcast-notifications)
    + [JVM, Android, JS, Native](#jvm-android-js)
    + [Android information](#android)

## Sample application

There is a sample application demonstrating authentication, remote integration, and broadcast notifications. You may
find it useful to scaffold parts of your application, or just to learn more about the Android features in this library.
See https://github.com/Nielssg/Spotify-Api-Test-App

## Authentication

spotify-web-api-kotlin comes with two built-in authorization schemes. The library includes a full implementation of the
PKCE authorization scheme, which allows you to refresh the token you obtain indefinitely*, as well as wrapping around
the `spotify-auth` to provide a simple way to perform implicit grant authorization (non-refreshable tokens).

\* PKCE tokens are refreshable unless they are revoked. If they are revoked, requests will fail with error 400, and you
should begin authorization flow again.

### Spotify Credential Store

By default, credentials are stored in the `SpotifyDefaultCredentialStore`, which under-the-hood creates and updates
an `EncryptedSharedPreferences` instance.

#### Creating an instance of the credential store

```kotlin
 val credentialStore by lazy {
    SpotifyDefaultCredentialStore(
        clientId = "YOUR_SPOTIFY_CLIENT_ID",
        redirectUri = "YOUR_SPOTIFY_REDIRECT_URI",
        applicationContext = YOUR_APPLICATION.context
    )
}
```

It is recommended to maintain only one instance of the credential store.

#### Setting credentials

You can set credentials in several different ways. The first two are recommended, for simplicity.

1. You can pass an instance of `SpotifyApi` using `SpotifyDefaultCredentialStore.setSpotifyApi(api: GenericSpotifyApi)`.
   This will directly set the `token` property.
2. You can set the `token` property using `SpotifyDefaultCredentialStore.token = YOUR_TOKEN`. This will set all three
   saved properties, mentioned below.
3. You can set the `spotifyTokenExpiresAt`, `spotifyAccessToken`, and `spotifyRefreshToken` properties. Please note that
   all of them are used to create a `Token`, so failing to update any single property may result in unintended
   consequences.

Example:

```kotlin
val credentialStore = (application as MyApplication).model.credentialStore
credentialStore.setSpotifyApi(spotifyApi)
```

#### Getting an instance of SpotifyApi from the credential store

Based on the type of authorization you used to authenticate the user, you will either call `getSpotifyImplicitGrantApi`
or `getSpotifyClientPkceApi`. Both methods allow you to optionally pass parameters to configure the returned
`SpotifyApi`.

#### Saving credentials somewhere other than the credential store (TBD)

Unfortunately, you are only able to store credentials in the credential store at this time if you decide to use the
authentication features of this library. PRs are welcome to address this limitation.

### Authentication prerequisites

1. You
   must [register your application](https://developer.spotify.com/documentation/general/guides/app-settings/#register-your-app)
   on Spotify. You must specify at least one application redirect uri (such as myapp://myauthcallback) - you will need
   this later.
2. Though this is not required, for security reasons you should follow the **Register Your App** part of the Spotify
   Android guide listed
   [here](https://developer.spotify.com/documentation/android/quick-start/) to generate a fingerprint for your app.

**Note**: Ensure that you are not using the same redirect uri for both PKCE/implicit authorization. If you need to use
both, please register two distinct redirect uris.

### PKCE Auth

PKCE authorization lets you obtain a refreshable Spotify token. This means that you do not need to keep prompting your
users to re-authenticate (or force them to wait a second for automatic login). Please read the "PKCE" section of
the [README](README.md) if you'd like to learn more.

#### 1. Create a class implementing AbstractSpotifyPkceLoginActivity

You first need to create a class that extends AbstractSpotifyPkceLoginActivity that will be used for the actual user
authorization.

Example:

```kotlin
internal var pkceClassBackTo: Class<out Activity>? = null

class SpotifyPkceLoginActivityImpl : AbstractSpotifyPkceLoginActivity() {
    override val clientId = BuildConfig.SPOTIFY_CLIENT_ID
    override val redirectUri = BuildConfig.SPOTIFY_REDIRECT_URI_PKCE
    override val scopes = SpotifyScope.values().toList()

    override fun onSuccess(api: SpotifyClientApi) {
        val model = (application as SpotifyPlaygroundApplication).model
        model.credentialStore.setSpotifyApi(api)
        val classBackTo = pkceClassBackTo ?: ActionHomeActivity::class.java
        pkceClassBackTo = null
        toast("Authentication via PKCE has completed. Launching ${classBackTo.simpleName}..")
        startActivity(Intent(this, classBackTo))
    }

    override fun onFailure(exception: Exception) {
        exception.printStackTrace()
        pkceClassBackTo = null
        toast("Auth failed: ${exception.message}")
    }
}
```

#### 2. Add the following activity to your Android Manifest
Note: the protocol of your redirect uri corresponds to the Android scheme, and the path corresponds to the 
host. Ex: for the redirect uri `myapp://authcallback`, the scheme is `myapp` and the host is `authcallback`.

```xml

<application>
    ...
    <activity android:name="YOUR_CLASS_IMPLEMENTING_AbstractSpotifyPkceLoginActivity"
              android:launchMode="singleTop">
        <intent-filter>
            <action android:name="android.intent.action.VIEW"/>

            <category android:name="android.intent.category.DEFAULT"/>
            <category android:name="android.intent.category.BROWSABLE"/>

            <data android:scheme="YOUR_REDIRECT_SCHEME" android:host="YOUR_REDIRECT_HOST"/>
        </intent-filter>
    </activity>
</application>
```

#### 3. Begin Spotify authorization flow with Activity.startSpotifyClientPkceLoginActivity
Now, you just need to begin the authorization flow by calling `Activity.startSpotifyClientPkceLoginActivity` in any 
activity in your application.

Example:
```kotlin
pkceClassBackTo = classBackTo // from the previous code sample, return to an activity after auth success
startSpotifyClientPkceLoginActivity(YOUR_CLASS_IMPLEMENTING_PKCE_LOGIN_ACTIVITY::class.java)
```

#### 4. ???

#### 5. Profit (more accurately, your onSuccess or onFailure methods will be called)

### Implicit auth
Implicit grant authorization, provided by wrapping the `spotify-auth` library, returns a temporary, non-refreshable 
access token. Implementing this authorization method is very similar to PKCE authorization, as both follow the 
same general format.

#### 1. Create a class implementing AbstractSpotifyAppLoginActivity or AbstractSpotifyAppCompatImplicitLoginActivity.
You first need to create a class that extends `AbstractSpotifyAppLoginActivity` or `AbstractSpotifyAppCompatImplicitLoginActivity` 
that will be used for the actual user authorization. The only difference between these two classes is that 
`AbstractSpotifyAppLoginActivity` extends from `Activity`, while `AbstractSpotifyAppCompatImplicitLoginActivity` extends 
from `AppCompatActivity`.

Example:

```kotlin
class SpotifyImplicitLoginActivityImpl : AbstractSpotifyAppImplicitLoginActivity() {
    override val state: Int = 1337
    override val clientId: String = BuildConfig.SPOTIFY_CLIENT_ID
    override val redirectUri: String = BuildConfig.SPOTIFY_REDIRECT_URI_AUTH
    override val useDefaultRedirectHandler: Boolean = false
    override fun getRequestingScopes(): List<SpotifyScope> = SpotifyScope.values().toList()

    override fun onSuccess(spotifyApi: SpotifyImplicitGrantApi) {
        val model = (application as SpotifyPlaygroundApplication).model
        model.credentialStore.setSpotifyApi(spotifyApi)
        toast("Authentication via spotify-auth has completed. Launching TrackViewActivity..")
        startActivity(Intent(this, ActionHomeActivity::class.java))
    }

    override fun onFailure(errorMessage: String) {
        toast("Auth failed: $errorMessage")
    }
}
```

#### 2. Add the following activity to your Android Manifest
Note: the protocol of your redirect uri corresponds to the Android scheme, and the path corresponds to the
host. Ex: for the redirect uri `myapp://authcallback`, the scheme is `myapp` and the host is `authcallback`.

```xml

<application>
    ...
    <activity
            android:name="YOUR_CLASS_IMPLEMENTING_AbstractSpotifyAppImplicitLoginActivity"
            android:theme="@android:style/Theme.Translucent.NoTitleBar">
        <intent-filter>
            <action android:name="android.intent.action.VIEW" />
            <category android:name="android.intent.category.DEFAULT" />
            <category android:name="android.intent.category.BROWSABLE" />
            <data android:scheme="YOUR_REDIRECT_SCHEME" android:host="YOUR_REDIRECT_HOST"/>
        </intent-filter>
    </activity>

    <activity
            android:name="com.spotify.sdk.android.auth.LoginActivity"
            android:theme="@android:style/Theme.Translucent.NoTitleBar">

        <intent-filter>
            <data android:scheme="YOUR_REDIRECT_SCHEME" android:host="YOUR_REDIRECT_HOST"/>
        </intent-filter>
    </activity>
</application>
```

#### 3. Begin Spotify authorization flow with Activity.startSpotifyImplicitLoginActivity
Now, you just need to begin the authorization flow by calling `Activity.startSpotifyImplicitLoginActivity(spotifyLoginImplementationClass: Class<T>)` in any
activity in your application.

Example:
```kotlin
SpotifyDefaultCredentialStore.activityBackOnImplicitAuth = classBackTo // use if you're using guardValidSpotifyImplicitApi, though this is not recommended
startSpotifyImplicitLoginActivity(SpotifyImplicitLoginActivityImpl::class.java)
```

#### 4. ???

#### 5. Profit (more accurately, your onSuccess or onFailure methods will be called)


### Using SpotifyApi in your application
Based on the type of authorization you used to authenticate the user, you will either call `SpotifyDefaultCredentialStore.getSpotifyImplicitGrantApi`
or `SpotifyDefaultCredentialStore.getSpotifyClientPkceApi`. Both methods allow you to optionally pass parameters to configure the returned
`SpotifyApi`.

You will want to write a guard to handle what happens when `SpotifyException.ReAuthenticationNeededException` is thrown. 

A basic guard is `Activity.guardValidImplicitSpotifyApi`, which will launch the provided 
activity after a user authenticates successfully, if the implicit token has expired.

A more complex guard can be found in the [sample application](https://github.com/Nielssg/Spotify-Api-Test-App/blob/main/app/src/main/java/com/adamratzman/spotifyandroidexample/auth/VerifyLoggedInUtils.kt).

## Spotify Remote integration
Spotify remote integration is still a WIP.

## Broadcast Notifications
You can easily add support for handling Spotify app broadcast notifications by implementing 
the `AbstractSpotifyBroadcastReceiver` class and registering the receiver in a Fragment or 
Activity. 

Supported broadcast types: queue changes, playback state changes, and metadata changes.

Note that "Device Broadcast Status" must be enabled in the Spotify app and the active Spotify device must be the Android 
device that your app is on to receive notifications.

This library provides a `registerSpotifyBroadcastReceiver` method that you can use to 
easily register your created broadcast receiver.

An example implementation of `AbstractSpotifyBroadcastReceiver` and use of `registerSpotifyBroadcastReceiver` 
are provided below. Please see the sample app for a complete implementation.

```kotlin
class SpotifyBroadcastReceiver(val activity: ViewBroadcastsActivity) : AbstractSpotifyBroadcastReceiver() {
    override fun onMetadataChanged(data: SpotifyMetadataChangedData) {
        activity.broadcasts += data
        println("broadcast: ${data}")
    }

    override fun onPlaybackStateChanged(data: SpotifyPlaybackStateChangedData) {
        activity.broadcasts += data
        println("broadcast: $data")
    }

    override fun onQueueChanged(data: SpotifyQueueChangedData) {
        activity.broadcasts += data
        println("broadcast: $data")
    }
}

class ViewBroadcastsActivity : BaseActivity() {
    lateinit var spotifyBroadcastReceiver: SpotifyBroadcastReceiver
    val broadcasts: MutableList<SpotifyBroadcastEventData> = mutableStateListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        spotifyBroadcastReceiver = SpotifyBroadcastReceiver(this)

        ...

        registerSpotifyBroadcastReceiver(spotifyBroadcastReceiver, *SpotifyBroadcastType.values())
    }
}

```