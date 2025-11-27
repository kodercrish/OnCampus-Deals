import React from 'react';
import { Text as RNText, TextProps, StyleSheet } from 'react-native';

interface Props extends TextProps {
  variant?: 'h1' | 'h2' | 'body' | 'caption';
}

export const Text: React.FC<Props> = ({ variant = 'body', style, ...props }) => {
  return <RNText style={[styles[variant], style]} {...props} />;
};

const styles = StyleSheet.create({
  h1: { fontSize: 24, fontWeight: 'bold', color: '#000' },
  h2: { fontSize: 18, fontWeight: '600', color: '#333' },
  body: { fontSize: 14, color: '#444' },
  caption: { fontSize: 12, color: '#777' },
});