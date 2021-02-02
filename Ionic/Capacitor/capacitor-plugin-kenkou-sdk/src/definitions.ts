declare module '@capacitor/core' {
  interface PluginRegistry {
    KenkouSDK: KenkouSDKPlugin;
  }
}

export interface KenkouSDKPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
