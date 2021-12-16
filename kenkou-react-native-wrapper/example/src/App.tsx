import * as React from 'react';

import { StyleSheet, View, Button, Text } from 'react-native';
import {
  getHistory,
  initialize,
  presentHistory,
  presentMeasurement,
  presentMeasurementInstructions,
  presentOnboardingQuestionnaire,
  saveOnboardingQuestionnaireAnswers,
  startMeasurement,
  stopMeasurement,
} from 'kenkou-react-native-wrapper';

export default function App() {
  React.useEffect(() => {
    initialize('');
  }, []);

  return (
    <View style={styles.container}>
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
              console.log(error.code);
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
              console.log(error.code);
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
              console.log(error.code);
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
              console.log(error.code);
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
              console.log(error);
            }
          }}
          title="Save Answers"
        />
      </View>
      <View style={styles.button}>
        <Button
          onPress={async () => {
            try {
              await startMeasurement();
            } catch (error: any) {
              console.log(error);
            }
          }}
          title="Start Measurement"
        />
      </View>
      <View style={styles.button}>
        <Button
          onPress={async () => {
            try {
              await stopMeasurement();
            } catch (error: any) {
              console.log(error);
            }
          }}
          title="Stop Measurement"
        />
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  button: {
    marginVertical: 10,
  },
});
