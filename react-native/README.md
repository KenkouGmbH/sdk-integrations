# React Native Kenkou SDK Guide

* Install dependecies, if necessary
    ```sh
    npm install -g yarn react-native-cli
    ```
* Set up your development environment as [described in the docs](https://reactnative.dev/docs/environment-setup)
* Git clone the repo
* Build the plugin:
    ```sh
    cd react-native-kenkou-sdk
    yarn
    yarn prepare
    cd ..
    ```
* To run the Android example app:
    ```sh
    cd example
    yarn
    yarn android
    ```
* To run the iOS example app
    ```sh
    cd example
    yarn
    pod install
    yarn ios
    ```
