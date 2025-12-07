// src/api/userApi.ts
import { baseApi } from './baseApi';

export const userApi = baseApi.injectEndpoints({
  endpoints: (builder) => ({
    fetchUser: builder.query<any, { userId: string }>({
      query: ({ userId }) => ({
        url: '/user/fetch',   // change if your User service endpoint differs
        method: 'POST',
        body: { userId },
      }),
      providesTags: (res, err, arg) => [{ type: 'User', id: arg.userId }],
    }),
  }),
});

export const { useFetchUserQuery } = userApi;
