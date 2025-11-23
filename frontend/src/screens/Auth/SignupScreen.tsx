import React, { useState } from 'react';
import { View, Text, TextInput, Button, StyleSheet, Alert } from 'react-native';
import { useSignupMutation } from '../../api/authApi';
import { useDispatch } from 'react-redux';
import { setCredentials } from '../../store/authSlice';

import { NativeStackScreenProps } from '@react-navigation/native-stack';
import { AuthStackParamList } from '../../navigation/types';

type Props = NativeStackScreenProps<AuthStackParamList, 'Signup'>;

export default function SignupScreen({ navigation }: Props) {
  const dispatch = useDispatch();
  const [signup, { isLoading }] = useSignupMutation();

  const [fullName, setFullName] = useState('');
  const [graduationYear, setGraduationYear] = useState('');
  const [email, setEmail]                 = useState('');
  const [password, setPassword]           = useState('');

  async function handleSignup() {
    if (!fullName || !email || !password) {
      return Alert.alert('Error', 'Please fill all required fields');
    }

    try {
      const res = await signup({
        fullName,
        email,
        password,
        graduationYear: graduationYear ? Number(graduationYear) : undefined,
      }).unwrap();

      if (res.token && res.userId) {
        dispatch(setCredentials({ token: res.token, userId: res.userId }));
      }
    } catch (err: any) {
      Alert.alert('Signup failed', err?.data?.message ?? 'Something went wrong');
    }
  }

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Signup</Text>

      <TextInput
        style={styles.input}
        placeholder="Full Name"
        value={fullName}
        onChangeText={setFullName}
      />

      <TextInput
        style={styles.input}
        placeholder="Graduation Year (optional)"
        value={graduationYear}
        onChangeText={setGraduationYear}
        keyboardType="numeric"
      />

      <TextInput
        style={styles.input}
        placeholder="Email"
        autoCapitalize="none"
        keyboardType="email-address"
        value={email}
        onChangeText={setEmail}
      />

      <TextInput
        style={styles.input}
        placeholder="Password"
        secureTextEntry
        value={password}
        onChangeText={setPassword}
      />

      <Button
        title={isLoading ? 'Signing up...' : 'Signup'}
        onPress={handleSignup}
      />

      <Text style={styles.link} onPress={() => navigation.navigate('Login')}>
        Already have an account? Login
      </Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: { padding: 20, marginTop: 80 },
  title: { fontSize: 28, marginBottom: 20, fontWeight: 'bold' },
  input: {
    borderWidth: 1,
    borderColor: '#ccc',
    marginBottom: 12,
    padding: 12,
    borderRadius: 6,
  },
  link: { marginTop: 20, color: '#007bff', textAlign: 'center' },
});
