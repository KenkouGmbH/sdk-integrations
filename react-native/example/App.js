/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow strict-local
 */

import React, { useEffect, useState } from 'react';
import {
  SafeAreaView,
  ScrollView,
  StatusBar,
  StyleSheet,
  Text,
  useColorScheme,
  View,
  Button,
  DeviceEventEmitter,
  NativeModules,
  NativeEventEmitter
} from 'react-native';

import {
  Colors,
  DebugInstructions,
  Header,
  LearnMoreLinks,
  ReloadInstructions,
} from 'react-native/Libraries/NewAppScreen';

import KenkouSdk from 'react-native-kenkou-sdk'

const { ModelWithEmitter } = NativeModules;

const eventEmitter = new NativeEventEmitter(ModelWithEmitter);

const Section = ({ children, title }): React$Node => {
  const isDarkMode = useColorScheme() === 'dark';
  return (
    <View style={styles.sectionContainer}>
      <Text
        style={[
          styles.sectionTitle,
          {
            color: isDarkMode ? Colors.white : Colors.black,
          },
        ]}>
        {title}
      </Text>
      <Text
        style={[
          styles.sectionDescription,
          {
            color: isDarkMode ? Colors.light : Colors.dark,
          },
        ]}>
        {children}
      </Text>
    </View>
  );
};

const App: () => React$Node = () => {
  const isDarkMode = useColorScheme() === 'dark';

  const [value, setValue] = useState(0)

  const startMeasurement = () => {
    KenkouSdk.startMeasurement(1)
  }

  const clearUserData = () => {
    KenkouSdk.clearUserData(1)
  }

  const startMeasurementOnboarding = () => {
    KenkouSdk.startMeasurementOnboarding(1)
  }

  const startHeadlessMeasurement = () => {
    KenkouSdk.startHeadlessMeasurement(1)
  }

  const backgroundStyle = {
    backgroundColor: isDarkMode ? Colors.darker : Colors.lighter,
  };

  const trackHeartBeat = (event) => {
    console.log('trackHeartBeat =====>', event);
    setValue(event)
  };

  useEffect(() => {
    DeviceEventEmitter.addListener('heartBeat', trackHeartBeat);
    eventEmitter.addListener('heartBeat', trackHeartBeat)
  }, []);

  return (
    <SafeAreaView style={backgroundStyle}>
      <StatusBar barStyle={isDarkMode ? 'light-content' : 'dark-content'} />
      <ScrollView
        contentInsetAdjustmentBehavior="automatic"
        style={backgroundStyle}>
        <Header />
        <View
          style={{
            backgroundColor: isDarkMode ? Colors.black : Colors.white,
          }}>
          <Section title="Start Measurement">
            <Button title="Start Measurement" onPress={startMeasurement}></Button>
          </Section>
          <Section title="Clear User Data">
            <Button title="Clear User Data" onPress={clearUserData}></Button>
          </Section>
          <Section title="Start Measurement Onboarding">
            <Button title="Start Measurement Onboarding" onPress={startMeasurementOnboarding}></Button>
          </Section>
          <Section title="Start Headless Measurement">
            <Button title="Start Headless Measurement" onPress={startHeadlessMeasurement}></Button>
          </Section>
          <Section title="Learn More">
            Headless Measurement value: {value}
          </Section>
        </View>
      </ScrollView>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  sectionContainer: {
    marginTop: 32,
    paddingHorizontal: 24,
  },
  sectionTitle: {
    fontSize: 24,
    fontWeight: '600',
  },
  sectionDescription: {
    marginTop: 8,
    fontSize: 18,
    fontWeight: '400',
  },
  highlight: {
    fontWeight: '700',
  },
});

export default App;
