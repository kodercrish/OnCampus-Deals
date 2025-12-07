// src/screens/TestImageScreen.tsx
import React from 'react';
import { View, Image, StyleSheet, Dimensions } from 'react-native';
import { Text } from '../../components/common/Text';
import { SafeAreaView } from 'react-native-safe-area-context';

const { width } = Dimensions.get('window');

const TestImageScreen = ({ route }: any) => {
  const { url } = route.params ?? {};
  return (
    <SafeAreaView style={styles.container}>
      <Text variant="h2">Test Image</Text>
      <View style={styles.imageWrap}>
        {url ? (
          <Image source={{ uri: url }} style={styles.image} resizeMode="contain" />
        ) : (
          <Text>No URL provided</Text>
        )}
      </View>
    </SafeAreaView>
  );
};

export default TestImageScreen;

const styles = StyleSheet.create({
  container: { flex: 1, padding: 12 },
  imageWrap: { flex: 1, marginTop: 20, alignItems: 'center', justifyContent: 'center' },
  image: { width: width - 40, height: width - 40 },
});
