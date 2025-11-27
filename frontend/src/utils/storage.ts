import AsyncStorage from '@react-native-async-storage/async-storage';

export const saveString = async (key: string, value: string) => {
  try {
    await AsyncStorage.setItem(key, value);
    return true;
  } catch (e) {
    return false;
  }
};

export const loadString = async (key: string) => {
  try {
    return await AsyncStorage.getItem(key);
  } catch (e) {
    return null;
  }
};

export const remove = async (key: string) => {
  try {
    await AsyncStorage.removeItem(key);
  } catch (e) {}
};