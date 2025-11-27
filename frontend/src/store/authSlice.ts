import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import { saveString, remove } from '../utils/storage';

interface AuthState {
  token: string | null;
  userId: string | null;
  role: string | null;
  isAuthenticated: boolean;
}

const initialState: AuthState = {
  token: null,
  userId: null,
  role: null,
  isAuthenticated: false,
};

const authSlice = createSlice({
  name: 'auth',
  initialState,
  reducers: {
    setCredentials: (
      state,
      action: PayloadAction<{ token: string; userId: string; role?: string }>
    ) => {
      state.token = action.payload.token;
      state.userId = action.payload.userId;
      state.role = action.payload.role || 'student';
      state.isAuthenticated = true;
      saveString('token', action.payload.token);
      saveString('userId', action.payload.userId);
    },
    logout: (state) => {
      state.token = null;
      state.userId = null;
      state.role = null;
      state.isAuthenticated = false;
      remove('token');
      remove('userId');
    },
  },
});

export const { setCredentials, logout } = authSlice.actions;
export default authSlice.reducer;