import {
  CameraView,
  getHistory,
  initialize,
  initializeHeadless,
  presentHistory,
  presentMeasurement,
  presentMeasurementInstructions,
  presentOnboardingQuestionnaire,
  saveOnboardingQuestionnaireAnswers,
  // startMeasurement,
  stopMeasurement,
} from 'kenkou-react-native-wrapper';
import React from 'react';
import {
  Alert,
  Button,
  ScrollView,
  StyleSheet,
  Text,
  View,
} from 'react-native';

export default function App() {
  const cameraRef = React.useRef(null);
  const [isMeasuring, setMeasuring] = React.useState(false);
  const [realtimeData, setRealtimeData] = React.useState(null);

  React.useEffect(() => {
    initialize('');
    initializeHeadless('');
  }, []);

  return (
    <ScrollView contentContainerStyle={styles.container}>
      <CameraView
        ref={cameraRef}
        onMeasure={({ drawData, ...data }: any) => {
          console.log(Date.now(), data);
          setRealtimeData({ ...data, drawData });
          if (data.progress >= 1) setMeasuring(false);
        }}
        style={styles.camera}
      />
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
          onPress={async () => {
            try {
              const data = await saveOnboardingQuestionnaireAnswers({
                birth_year: 1993,
                heightCm: 5,
                sex: 'FEMALE',
                timestamp: 1639590299533,
                weightKg: 150,
              });
              console.log('saveOnboardingQuestionnaireAnswers', data);
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
              await cameraRef.current?.startMeasurement();
              // await startMeasurement({
              //   width: 80,
              //   height: 80,
              //   left: 20,
              //   top: 20,
              // });
              setMeasuring(true);
            } catch (error: any) {
              console.log(error.code, error.message);
            }
          }}
          title="Start Measurement"
        />
      </View>
      <View style={styles.button}>
        <Button
          disabled={!isMeasuring}
          onPress={async () => {
            try {
              await stopMeasurement();
              setMeasuring(false);
            } catch (error: any) {
              console.log(error.code, error.message);
            }
          }}
          title="Stop Measurement"
        />
      </View>
      <View style={styles.info}>
        {realtimeData &&
          Object.keys(realtimeData).map((key) => (
            <Text numberOfLines={2} key={key}>
              {key}: {`${realtimeData[key]}`}
            </Text>
          ))}
      </View>
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  container: {
    alignItems: 'center',
    padding: 16,
  },
  button: {
    marginVertical: 10,
  },
  camera: {
    width: 80,
    height: 80,
  },
  info: {
    alignSelf: 'stretch',
  },
});
