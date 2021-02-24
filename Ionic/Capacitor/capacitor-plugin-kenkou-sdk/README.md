# capacitor-plugin-kenkou-sdk documentation

## Usage

```js
import {Plugins} from '@capacitor/core';
import 'capacitor-plugin-kenkou-sdk';

const {KenkouSDK} = Plugins;

// ...

KenkouSDK.startMeasurement();
KenkouSDK.clearUserData();
KenkouSDK.startMeasurementOnboarding();
```

## Run the project

+ You can build android/ios app using these commands.
    ```sh
    ionic build
    npx cap add <platform>
    npx cap sync
    npx cap open <platform>
    ```
    where `<platform>` is either `android` or `ios`.

## Android

* Add class in the MainActivity.java
    ```java
    add(KenkouSDK.class);
    ```
* Add theme.xml in the `res/values` folder with the following code
    ```xml
    <resources xmlns:tools="http://schemas.android.com/tools">
      <!-- Base application theme. -->
      <style name="Theme.SDKAndroid" parent="Theme.MaterialComponents.Light">
          <item name="colorPrimary">#2688E5</item>
          <item name="colorPrimaryVariant">#005cb2</item>
          <item name="colorOnPrimary">#ffffff</item>
          <item name="android:colorBackground">#e5e5e5</item>
          <item name="colorOnBackground">#121212</item>
          <item name="colorSurface">#ffffff</item>
          <item name="colorOnSurface">#121212</item>
          <item name="android:statusBarColor" tools:targetApi="l">?attr/colorPrimaryVariant</item>
      </style>
    </resources>
    ```
* Change the theme style in `AndroidManifest.xml`
    ```
    android:theme="@style/Theme.SDKAndroid"
    ```
* Update `build.gradle` and `gradle-wrapper.properties`
    ```gradle
    classpath 'com.android.tools.build:gradle:4.1.0'
    distributionUrl=https\://services.gradle.org/distributions/gradle-6.5-all.zip
    ```

## iOS
  
* Add this to the `Podfile` in `ios/App`
    ```pod
    source 'git@github.com:CocoaPods/Specs.git'
    source 'git@github.com:KenkouGmbH/SDKPodspec.git' # at the top

    // ...
      
    pod 'KenkouSDK', '~> 1.1.0'
    ```
* Run `pod install`
* Go to `node_modules/capacitor-plugin-kenkou-sdk`, and edit `CapacitorPluginKenkouSdk.podspec`:
    ```pod
    s.dependency 'Capacitor'
    s.dependency 'KenkouSDK'  /* add this line */
    s.swift_version = '5.1'
    ```  
* Then, run these commands again.
    ```sh
    npx cap sync ios
    npx cap open ios
    ```
