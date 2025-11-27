import React from 'react';
import { View, StyleSheet, TextInput, Alert } from 'react-native';
import { useForm, Controller } from 'react-hook-form';
import { useSignupMutation } from '../../api/authApi';
import { useDispatch } from 'react-redux';
import { setCredentials } from '../../store/authSlice';
import { Button } from '../../components/common/Button';
import { Text } from '../../components/common/Text';

export const SignupScreen = () => {
  const { control, handleSubmit } = useForm();
  const [signup, { isLoading }] = useSignupMutation();
  const dispatch = useDispatch();

  const onSubmit = async (data: any) => {
    try {
      const response = await signup(data).unwrap();
      if (response.token && response.userId) {
        dispatch(setCredentials({ 
          token: response.token, 
          userId: response.userId 
        }));
      } else {
        Alert.alert('Success', response.message);
      }
    } catch (err: any) {
      Alert.alert('Error', 'Signup failed');
    }
  };

  return (
    <View style={styles.container}>
      <Text variant="h1" style={styles.title}>Register</Text>
      
      <Controller
        control={control}
        name="fullName"
        render={({ field: { onChange, value } }) => (
          <TextInput style={styles.input} placeholder="Full Name" onChangeText={onChange} value={value} />
        )}
      />
      <Controller
        control={control}
        name="email"
        render={({ field: { onChange, value } }) => (
          <TextInput style={styles.input} placeholder="Email" onChangeText={onChange} value={value} />
        )}
      />
      <Controller
        control={control}
        name="password"
        render={({ field: { onChange, value } }) => (
          <TextInput style={styles.input} placeholder="Password" secureTextEntry onChangeText={onChange} value={value} />
        )}
      />

      <Button title="Sign Up" onPress={handleSubmit(onSubmit)} loading={isLoading} />
    </View>
  );
};

const styles = StyleSheet.create({
  container: { flex: 1, justifyContent: 'center', padding: 20 },
  title: { textAlign: 'center', marginBottom: 30 },
  input: { borderWidth: 1, borderColor: '#ddd', padding: 10, borderRadius: 8, marginBottom: 15 },
});