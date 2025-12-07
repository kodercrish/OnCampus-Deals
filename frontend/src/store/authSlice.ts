// src/store/authSlice.ts
import { createSlice, PayloadAction } from '@reduxjs/toolkit';

type AuthState = {
  token: string | null;
  userId: string | null;
  role?: string | null;
  isAuthenticated: boolean;
};

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
      state.role = action.payload.role ?? null;
      state.isAuthenticated = true;
    },
    logout: (state) => {
      state.token = null;
      state.userId = null;
      state.role = null;
      state.isAuthenticated = false;
    },
    // optional: hydrate from persisted token
    setAuthFromStorage: (state, action: PayloadAction<{ token: string; userId: string; role?: string }>) => {
      state.token = action.payload.token;
      state.userId = action.payload.userId;
      state.role = action.payload.role ?? null;
      state.isAuthenticated = !!action.payload.token;
    },
  },
});

export const { setCredentials, logout, setAuthFromStorage } = authSlice.actions;
export default authSlice.reducer;
