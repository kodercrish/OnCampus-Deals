import { baseApi } from './baseApi';
import { ADMIN } from './endpoints';
import { Listing } from '../types/models';
import { DeleteListingRequest, DeleteListingResponse } from '../types/dtos';

export const adminApi = baseApi.injectEndpoints({
  endpoints: (builder) => ({
    fetchAllListings: builder.query<{ listings: Listing[] }, void>({
      query: () => ADMIN.FETCH_ALL_LISTINGS,
      providesTags: ['Listing'],
    }),
    adminDeleteListing: builder.mutation<DeleteListingResponse, DeleteListingRequest>({
      query: (body) => ({
        url: ADMIN.DELETE_LISTING,
        method: 'DELETE',
        body,
      }),
      invalidatesTags: ['Listing'],
    }),
  }),
});

export const { useFetchAllListingsQuery, useAdminDeleteListingMutation } = adminApi;