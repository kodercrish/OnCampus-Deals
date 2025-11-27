// import { baseApi } from './baseApi';
// import { AUTH_ENDPOINTS } from './endpoints';

// export interface LoginRequest {
//   email: string;
//   password: string;
// }

// export interface LoginResponse {
//   message: string;
//   token?: string;
//   userId?: string;
// }

// export interface SignupRequest {
//   fullName: string;
//   email: string;
//   password: string;
//   graduationYear?: number;
// }

// export interface SignupResponse {
//   message: string;
//   token?: string;
//   userId?: string;
// }

// export const authApi = baseApi.injectEndpoints({
//   endpoints: (builder) => ({
//     login: builder.mutation<LoginResponse, LoginRequest>({
//       query: (body) => ({
//         url: AUTH_ENDPOINTS.LOGIN,
//         method: 'POST',
//         body,
//       }),
//     }),

//     signup: builder.mutation<SignupResponse, SignupRequest>({
//       query: (body) => ({
//         url: AUTH_ENDPOINTS.SIGNUP,
//         method: 'POST',
//         body,
//       }),
//     }),
//   }),
// });

// export const { useLoginMutation, useSignupMutation } = authApi;

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