// src/screens/TestImageScreen.tsx
import React from 'react';
import { View, Image, StyleSheet, Dimensions } from 'react-native';

const { width } = Dimensions.get('window');

export default function TestImageScreen({ route }: any) {
  const url = route.params?.url ?? route.params?.uri ?? null;
  return (
    <View style={styles.container}>
      {url ? <Image source={{ uri: url }} style={styles.img} resizeMode="contain" /> : null}
    </View>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, justifyContent: 'center', alignItems: 'center', backgroundColor: '#fff' },
  img: { width: width - 40, height: width - 40 },
});
