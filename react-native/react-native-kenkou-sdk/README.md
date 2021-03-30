# react-native-kenkou-sdk documentation

## Installation

+ Copy the npm package folder (example: at the root folder) and ```npm install && npm run prepare``` in the npm package folder.
  
+ ```npm install /path``` in the root folder. 

## Usage

```js
import KenkouSdk from "react-native-kenkou-sdk";

// ...

KenkouSdk.startMeasurement(1)
KenkouSdk.clearUserData(1)
KenkouSdk.startMeasurementOnboarding(1)
```

## Android Setting

+ After npm install, have to reset some points as the following steps.
    
  + Add theme.xml in the res/values folder with the following code
  
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
  
  + Change the theme style in the AndroidManifest.xml
  
  ```
  android:theme="@style/Theme.SDKAndroid"
  ```
  
  + Update the build.gradle and gradle-wrapper.properties
  
  ```gradle
  minSdkVersion = 21
  
  classpath 'com.android.tools.build:gradle:4.1.0'
  
  distributionUrl=https\://services.gradle.org/distributions/gradle-6.5-all.zip
  ```
  
## iOS Setting
  
+ After npm install, have to reset some points as the following steps.

  + Add these codes in the Podfile of the ios
  
  ```pod
  source 'git@github.com:CocoaPods/Specs.git'
  source 'git@github.com:KenkouGmbH/SDKPodspec.git' # adding at the top
  
  // ...
   
  platform :ios, '11.0' # update
  use_frameworks!
  
  // ...
   
  pod 'KenkouSDK', '~> 1.1.0'
  ```
  
  + Comment Flipper part
  
  ```pod
  use_flipper!
  post_install do |installer|
   flipper_post_install(installer)
  end
  ```
   
  + pod install

+ Open the xcode and add swift file in the root folder and confirm create bridging header.

+ Go to the Build Phases and add JavaScriptCore.framework in Link Binary With Libraries.
