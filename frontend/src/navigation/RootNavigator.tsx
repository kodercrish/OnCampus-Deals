import { NavigationContainer } from '@react-navigation/native';
import { useSelector, useDispatch } from 'react-redux';
import { RootState } from '../store';
import { useEffect, useState } from 'react';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { restoreSession } from '../store/authSlice';
import AuthNavigator from './AuthNavigator';
// TODO: replace with AppNavigator when home is implemented
import AppNavigator from './AuthNavigator';

export default function RootNavigator() {
  const dispatch = useDispatch();
  const token = useSelector((state: RootState) => state.auth.token);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function loadData() {
      const storedToken = await AsyncStorage.getItem('token');
      const storedUserId = await AsyncStorage.getItem('userId');

      dispatch(
        restoreSession({
          token: storedToken,
          userId: storedUserId,
        })
      );

      setLoading(false);
    }

    loadData();
  }, []);

  if (loading) return null; // or splash screen

  return (
    <NavigationContainer>
      {token ? <AppNavigator /> : <AuthNavigator />}
    </NavigationContainer>
  );
}
