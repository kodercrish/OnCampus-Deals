import React from 'react';
import { TouchableOpacity, StyleSheet, ActivityIndicator } from 'react-native';
import { Text } from './Text';

interface Props {
  title: string;
  onPress: () => void;
  loading?: boolean;
  color?: string;
}

export const Button: React.FC<Props> = ({ title, onPress, loading, color = '#007AFF' }) => {
  return (
    <TouchableOpacity 
      style={[styles.container, { backgroundColor: color }]} 
      onPress={onPress}
      disabled={loading}
    >
      {loading ? (
        <ActivityIndicator color="#FFF" />
      ) : (
        <Text style={styles.text}>{title}</Text>
      )}
    </TouchableOpacity>
  );
};

const styles = StyleSheet.create({
  container: {
    padding: 12,
    borderRadius: 8,
    alignItems: 'center',
    marginVertical: 5,
  },
  text: {
    color: '#FFF',
    fontWeight: '600',
  },
});