import { baseApi } from './baseApi';
import { AUTH } from './endpoints';
import { LoginRequest, LoginResponse, SignupRequest, SignupResponse } from '../types/dtos';

export const authApi = baseApi.injectEndpoints({
  endpoints: (builder) => ({
    login: builder.mutation<LoginResponse, LoginRequest>({
      query: (credentials) => ({
        url: AUTH.LOGIN,
        method: 'POST',
        body: credentials,
      }),
    }),
    signup: builder.mutation<SignupResponse, SignupRequest>({
      query: (data) => ({
        url: AUTH.SIGNUP,
        method: 'POST',
        body: data,
      }),
    }),
  }),
});

export const { useLoginMutation, useSignupMutation } = authApi;