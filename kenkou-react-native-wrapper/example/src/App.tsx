import {
  CameraView,
  getHistory,
  initialize,
  initializeHeadless,
  presentHistory,
  presentMeasurement,
  presentMeasurementInstructions,
  presentOnboardingQuestionnaire,
  presentPostMeasurementQuestionnaire,
  saveOnboardingQuestionnaireAnswers,
  stopMeasurement,
} from 'kenkou-react-native-wrapper';
import React from 'react';
import {
  Alert,
  Button,
  PermissionsAndroid,
  Platform,
  SafeAreaView,
  ScrollView,
  StyleSheet,
  Text,
  View,
} from 'react-native';
import { LineChart } from 'react-native-charts-wrapper';
import type { OnboardingQuestionnaireAnswers, RealtimeData } from 'src/utils';

export default function App() {
  const cameraRef = React.useRef<any>(null);
  const [isMeasuring, setMeasuring] = React.useState(false);
  const [realtimeData, setRealtimeData] = React.useState<RealtimeData | null>(
    null
  );
  const [onboardingAnswers, setOnboardingAnswers] =
    React.useState<OnboardingQuestionnaireAnswers | null>(null);

  React.useEffect(() => {
    initialize('');
    initializeHeadless('');
  }, []);

  const triggerStop = async () => {
    try {
      const measurement = await stopMeasurement();
      if (measurement) {
        await presentPostMeasurementQuestionnaire(measurement);
      }
    } catch (error: any) {
      console.log(error.code, error.message);
    }
    setMeasuring(false);
  };

  return (
    <SafeAreaView style={styles.container}>
      <View style={styles.cameraContainer}>
        <CameraView
          ref={cameraRef}
          onError={(error: any) => {
            console.log(error);
            Alert.alert(error.message);
            setMeasuring(false);
          }}
          onMeasure={({ drawData, ...data }) => {
            console.log(Date.now(), data);
            setRealtimeData({ ...data, drawData });
            if (data.progress >= 1) {
              triggerStop();
            }
          }}
          style={styles.camera}
        />
      </View>
      <ScrollView contentContainerStyle={styles.contentContainer}>
        {isMeasuring ? (
          <View style={styles.button}>
            <Button
              disabled={!isMeasuring}
              onPress={triggerStop}
              title="Stop Measurement"
            />
          </View>
        ) : (
          <>
            <View style={styles.button}>
              <Text>Visual:</Text>
            </View>
            <View style={styles.button}>
              <Button
                onPress={async () => {
                  try {
                    const data = await presentMeasurement();
                    console.log('presentMeasurement', JSON.stringify(data));
                  } catch (error: any) {
                    console.log(error.code, error.message);
                    if (error.message) Alert.alert(error.message);
                  }
                }}
                title="Measure Now"
              />
            </View>
            <View style={styles.button}>
              <Button
                onPress={async () => {
                  try {
                    const data = await presentMeasurementInstructions();
                    console.log('presentMeasurementInstructions', data);
                  } catch (error: any) {
                    console.log(error.code, error.message);
                  }
                }}
                title="Do Perfect Measurement Tutorial"
              />
            </View>
            <View style={styles.button}>
              <Button
                onPress={async () => {
                  try {
                    const data = await presentOnboardingQuestionnaire();
                    console.log('presentOnboardingQuestionnaire', data);
                    setOnboardingAnswers(data);
                  } catch (error: any) {
                    console.log(error.code, error.message);
                  }
                }}
                title="Do Questionnaire"
              />
            </View>
            <View style={styles.button}>
              <Button
                onPress={async () => {
                  try {
                    const history = await getHistory();
                    console.log('getHistory', history);
                    const data = await presentHistory(history);
                    console.log('presentHistory', data);
                  } catch (error: any) {
                    console.log(error.code, error.message);
                  }
                }}
                title="Show History"
              />
            </View>
            <View style={styles.button}>
              <Text>Headless:</Text>
            </View>
            <View style={styles.button}>
              <Button
                disabled={!onboardingAnswers}
                onPress={async () => {
                  try {
                    if (onboardingAnswers !== null) {
                      const saved = await saveOnboardingQuestionnaireAnswers(
                        onboardingAnswers
                      );
                      console.log('saveOnboardingQuestionnaireAnswers', saved);
                      if (saved) {
                        setOnboardingAnswers(null);
                        Alert.alert('Onboarding questionnaire answers saved!');
                      }
                    }
                  } catch (error: any) {
                    console.log(error.code, error.message);
                  }
                }}
                title="Save Answers"
              />
            </View>
            <View style={styles.button}>
              <Button
                onPress={async () => {
                  try {
                    const granted =
                      Platform.OS === 'ios' ||
                      (await PermissionsAndroid.request(
                        PermissionsAndroid.PERMISSIONS.CAMERA
                      )) === 'granted';
                    if (granted) {
                      cameraRef.current?.startMeasurement();
                      setMeasuring(true);
                    }
                  } catch (error: any) {
                    console.log(error.code, error.message);
                  }
                }}
                title="Start Measurement"
              />
            </View>
          </>
        )}
        <View style={styles.info}>
          {realtimeData &&
            Object.keys(realtimeData).map((key) =>
              key === 'drawData' ? (
                <LineChart
                  key={key}
                  style={styles.graph}
                  data={{
                    dataSets: [
                      {
                        config: {
                          drawCircles: false,
                          drawFilled: true,
                          drawHorizontalHighlightIndicator: false,
                          drawVerticalHighlightIndicator: false,
                          fillAlpha: 200,
                        },
                        label: 'drawData',
                        values: realtimeData.drawData,
                      },
                    ],
                  }}
                />
              ) : (
                <Text key={key}>
                  <Text style={styles.infoKey}>{key}:</Text>{' '}
                  {`${realtimeData[key]}`}
                </Text>
              )
            )}
        </View>
      </ScrollView>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  cameraContainer: {
    alignItems: 'center',
    marginTop: 16,
  },
  camera: {
    width: 80,
    height: 80,
  },
  contentContainer: {
    alignItems: 'center',
    padding: 16,
  },
  button: {
    marginVertical: 10,
  },
  info: {
    alignSelf: 'stretch',
  },
  infoKey: {
    fontWeight: 'bold',
  },
  graph: {
    flexDirection: 'row',
    height: 250,
    alignItems: 'flex-end',
    marginVertical: 16,
  },
});
