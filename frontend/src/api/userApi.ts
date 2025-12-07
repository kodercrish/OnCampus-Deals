// src/api/userApi.ts
import { baseApi } from './baseApi';
import { API_BASE } from '@env'; // not needed for endpoint path, baseApi handles baseUrl

type FetchUserRequest = { userId: string };
type FetchUserResponse = { user: { id: string; name?: string; email?: string } | null };

export const userApi = baseApi.injectEndpoints({
  endpoints: (builder) => ({
    fetchUser: builder.query<FetchUserResponse, FetchUserRequest>({
      query: (body) => ({ url: `/user/fetch`, method: 'POST', body }), // adjust endpoint to your user service path
      providesTags: (result, err, arg) => (result?.user ? [{ type: 'User' as const, id: result.user.id }] : []),
    }),
  }),
});

export const { useFetchUserQuery } = userApi;
