import React from 'react';
import { View, StyleSheet, TextInput, Alert } from 'react-native';
import { useForm, Controller } from 'react-hook-form';
import { useLoginMutation } from '../../api/authApi';
import { useDispatch } from 'react-redux';
import { setCredentials } from '../../store/authSlice';
import { Button } from '../../components/common/Button';
import { Text } from '../../components/common/Text';

export const LoginScreen = ({ navigation }: any) => {
  const { control, handleSubmit } = useForm();
  const [login, { isLoading }] = useLoginMutation();
  const dispatch = useDispatch();

  const onSubmit = async (data: any) => {
    try {
      const response = await login(data).unwrap();
      if (response.token && response.userId) {
        dispatch(setCredentials({ 
          token: response.token, 
          userId: response.userId,
          role: response.role 
        }));
      }
    } catch (err: any) {
      Alert.alert('Login Failed', 'Invalid credentials');
    }
  };

  return (
    <View style={styles.container}>
      <Text variant="h1" style={styles.title}>OnCampus Deals</Text>
      
      <Controller
        control={control}
        name="email"
        render={({ field: { onChange, value } }) => (
          <TextInput
            style={styles.input}
            placeholder="College Email"
            onChangeText={onChange}
            value={value}
            autoCapitalize="none"
          />
        )}
      />

      <Controller
        control={control}
        name="password"
        render={({ field: { onChange, value } }) => (
          <TextInput
            style={styles.input}
            placeholder="Password"
            secureTextEntry
            onChangeText={onChange}
            value={value}
          />
        )}
      />

      <Button title="Login" onPress={handleSubmit(onSubmit)} loading={isLoading} />
      <Button 
        title="Create Account" 
        onPress={() => navigation.navigate('Signup')} 
        color="gray" 
      />
    </View>
  );
};

const styles = StyleSheet.create({
  container: { flex: 1, justifyContent: 'center', padding: 20 },
  title: { textAlign: 'center', marginBottom: 30 },
  input: {
    borderWidth: 1,
    borderColor: '#ddd',
    padding: 10,
    borderRadius: 8,
    marginBottom: 15,
  },
});