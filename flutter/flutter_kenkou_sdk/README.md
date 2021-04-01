# flutter_kenkou_sdk documentation

## Installation

+ Copy the plugin folder (example: at the root folder).
  
+ Add the plugin to the project. In the pubspec.yaml  

```yaml
dependencies:
  ...
  flutter_kenkou_sdk:
    path: ./flutter_kenkou_sdk # example
```

## Usage

```dart
import 'package:flutter_kenkou_sdk/flutter_kenkou_sdk.dart';

// ...

FlutterKenkouSdk.startMeasurement
FlutterKenkouSdk.clearUserData
FlutterKenkouSdk.startMeasurementOnboarding
```

## Android Setting

+ After plugin install, have to reset some points as the following steps.
    
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
  
  + Open android project with android studio and import sdk(.aar) ```File -> New -> New Module -> Import .JAR/.AAR Package```
    and then referencing it in the library module build.gradle file:
    ```
    dependencies {
      implementation project(':kenkou-measurement-sdk-1.14') # already set
    }
    ```

## iOS Setting

+ If flutter ios doesn't have podfile, ```flutter build ios``` in the root folder.

+ Add these codes in the Podfile.
  
  ```pod
  source 'git@github.com:CocoaPods/Specs.git'
  source 'git@github.com:KenkouGmbH/SDKPodspec.git' # adding at the top
  
  // ...
   
  pod 'KenkouSDK', '~> 1.1.0'
  ```
   
+ ```pod install```

+ If you have the error in the KenkouSDKUIComponents while run the project, you go to the Pods/KenkouSDKUIComponents/Resources/Measurement.xcassets.
  And check the KenkouSDKUIComponents option in the Target Membership of the right side.
