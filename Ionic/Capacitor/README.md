# Capacitor Kenkou SDK Guide

* Go to the `capacitor-plugin-kenkou-sdk` folder
* Copy `kenkou-measurement-sdk-$VERSION.aar` (distributed separately) to `android/lib/`
* Make sure that the following line in `android/build.gradle` contains the correct version number:
    ```gradle
    implementation files('lib/kenkou-measurement-sdk-$VERSION.aar')
    ```
* Run the following commands:
    ```sh
    npm install
    npm run build
    ```
* After that, go to the `example` folder
* If you haven't installed the ionic cli, install it with `npm install -g @ionic/cli`
* Run the following commands:
    ```sh
    npm install 
    npm run <platform>
    ```
    where `<platform>` is either `android` or `ios`.
