import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import AsyncStorage from '@react-native-async-storage/async-storage';

interface AuthState {
  token: string | null;
  userId: string | null;
}

const initialState: AuthState = {
  token: null,
  userId: null,
};

export const authSlice = createSlice({
  name: 'auth',
  initialState,
  reducers: {
    setCredentials: (
      state,
      action: PayloadAction<{ token: string; userId: string }>
    ) => {
      state.token = action.payload.token;
      state.userId = action.payload.userId;

      // Persist values
      AsyncStorage.setItem('token', action.payload.token);
      AsyncStorage.setItem('userId', action.payload.userId);
    },

    logout: (state) => {
      state.token = null;
      state.userId = null;

      // Clear storage
      AsyncStorage.removeItem('token');
      AsyncStorage.removeItem('userId');
    },

    restoreSession: (
      state,
      action: PayloadAction<{ token: string | null; userId: string | null }>
    ) => {
      state.token = action.payload.token;
      state.userId = action.payload.userId;
    },
  },
});

export const { setCredentials, logout, restoreSession } = authSlice.actions;
export default authSlice.reducer;
