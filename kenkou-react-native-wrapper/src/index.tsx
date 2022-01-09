import React from 'react';
import {
  findNodeHandle,
  NativeModules,
  Platform,
  requireNativeComponent,
  UIManager,
  ViewStyle,
} from 'react-native';

const LINKING_ERROR =
  `The package 'kenkou-react-native-wrapper' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo managed workflow\n';

const Kenkou = NativeModules.RNKenkou
  ? NativeModules.RNKenkou
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

// Visual Methods

export function initialize(token: string) {
  return Kenkou.initialize(token);
}

export function presentMeasurementInstructions() {
  return Kenkou.presentMeasurementInstructions();
}

export function presentOnboardingQuestionnaire() {
  return Kenkou.presentOnboardingQuestionnaire();
}

export function presentMeasurement() {
  return Kenkou.presentMeasurement();
}

export function presentPostMeasurementQuestionnaire(measurement: Object) {
  return Kenkou.presentPostMeasurementQuestionnaire(measurement);
}

export function presentMeasurementResults(measurement: Object) {
  return Kenkou.presentMeasurementResults(measurement);
}

export function presentHistoryItem(historyItem: Object) {
  return Kenkou.presentHistoryItem(historyItem);
}

export function presentHistory(history: Array<Object>) {
  return Kenkou.presentHistory(history);
}

// Headless Methods

export function initializeHeadless(token: string) {
  return Kenkou.initializeHeadless(token);
}

export function saveOnboardingQuestionnaireAnswers(answers: Object) {
  return Kenkou.saveOnboardingQuestionnaireAnswers(answers);
}

export function startMeasurement(params: Object) {
  return Kenkou.startMeasurement(params);
}

export function stopMeasurement() {
  return Kenkou.stopMeasurement();
}

export function savePostMeasurementQuestionnaireAnswers(
  measurement: Object,
  answers: Object
) {
  return Kenkou.savePostMeasurementQuestionnaireAnswers(measurement, answers);
}

export function getHistory() {
  return Kenkou.getHistory();
}

// Native UI Component

const ComponentName = 'RNKenkouCameraView';

type CameraViewProps = {
  ref?: any;
  onMeasure: Function;
  statusBarTranslucent?: boolean;
  style?: ViewStyle;
};

const RCTCameraView =
  UIManager.getViewManagerConfig(ComponentName) != null
    ? requireNativeComponent<CameraViewProps>(ComponentName)
    : () => {
        throw new Error(LINKING_ERROR);
      };

export const CameraView = React.forwardRef(
  ({ onMeasure, ...props }: CameraViewProps, ref) => {
    const cameraRef = React.useRef();
    React.useImperativeHandle(ref, () => ({
      startMeasurement: () => {
        UIManager.dispatchViewManagerCommand(
          findNodeHandle(cameraRef.current as any),
          UIManager.getViewManagerConfig(ComponentName).Commands
            .startMeasurement,
          []
        );
      },
    }));
    return (
      <RCTCameraView
        ref={cameraRef}
        onMeasure={(event: any) => onMeasure(event.nativeEvent)}
        {...props}
      />
    );
  }
);
