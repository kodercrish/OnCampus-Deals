import { baseApi } from './baseApi';
import { SEARCH } from './endpoints';

export type SearchRequest = {
  keyword?: string | null;
  category?: string | null;
  minPrice?: number | null;
  maxPrice?: number | null;
};

export type SearchResult = {
  id: string;
  title: string;
  description: string;
  price: number;
  category: string;
  sellerId: string;
  status: string;
};

export type SearchResponse = {
  results: SearchResult[] | null;
};

export const searchApi = baseApi.injectEndpoints({
  endpoints: (builder) => ({
    searchListings: builder.mutation<SearchResponse, SearchRequest>({
      query: (body) => ({
        url: SEARCH.LISTINGS,
        method: 'POST',
        body,
      }),
    }),
  }),
});

export const { useSearchListingsMutation } = searchApi;
