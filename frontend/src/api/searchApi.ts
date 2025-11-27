import { baseApi } from './baseApi';
import { SEARCH } from './endpoints';
import { SearchRequest, SearchResponse } from '../types/dtos';

export const searchApi = baseApi.injectEndpoints({
  endpoints: (builder) => ({
    searchListings: builder.query<SearchResponse, SearchRequest>({
      query: (params) => ({
        url: SEARCH.LISTINGS,
        params,
      }),
    }),
  }),
});

export const { useLazySearchListingsQuery } = searchApi;