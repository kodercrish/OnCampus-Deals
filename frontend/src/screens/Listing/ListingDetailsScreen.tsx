// src/screens/Listing/ListingDetailsScreen.tsx

import React from 'react';
import {
  View,
  StyleSheet,
  Image,
  FlatList,
  ActivityIndicator,
  ScrollView,
  Dimensions,
} from 'react-native';
import { Text } from '../../components/common/Text';
import { Button } from '../../components/common/Button';
import { useGetListingDetailsQuery } from '../../api/listingApi';
import { useFetchUserQuery } from '../../api/userApi';

const { width: SCREEN_W } = Dimensions.get('window');

export const ListingDetailsScreen = ({ route, navigation }: any) => {
  const { listingId } = route.params ?? {};

  if (!listingId) {
    return (
      <View style={styles.center}>
        <Text>No listing ID provided</Text>
      </View>
    );
  }

  // --- Fetch listing details ---
  const {
    data,
    isLoading,
    isFetching,
    error,
  } = useGetListingDetailsQuery({ listingId });

  // Backend response formal structure:
  // { message, listing: {...}, images: [...] }
  const listing = data?.listing ?? null;
  const images = data?.images ?? listing?.images ?? [];

  // --- Fetch seller details ---
  const sellerId = listing?.sellerId ?? null;
  const { data: sellerData, isFetching: sellerLoading } = useFetchUserQuery(
    { userId: sellerId },
    { skip: !sellerId }
  );
  const seller = sellerData?.user ?? null;

  // --- Loading Screen ---
  if (isLoading || isFetching) {
    return (
      <View style={styles.center}>
        <ActivityIndicator size="large" />
      </View>
    );
  }

  // --- Error or missing listing ---
  if (!listing) {
    return (
      <View style={styles.center}>
        <Text>Failed to load listing.</Text>
        <Button title="Go Back" onPress={() => navigation.goBack()} />
      </View>
    );
  }

  return (
    <ScrollView style={styles.container}>
      {/* --- Image Carousel --- */}
      <FlatList
        data={images.length ? images : [{ placeholder: true }]}
        horizontal
        pagingEnabled
        keyExtractor={(it: any, idx) => it.id ?? String(idx)}
        renderItem={({ item }) => {
          const uri =
            item.imageUrl ??
            item.image_url ??
            item.url ??
            (item.placeholder ? null : item);

          return (
            <Image
              source={
                { uri }
              }
              resizeMode="cover"
              style={styles.image}
            />
          );
        }}
        showsHorizontalScrollIndicator={false}
      />

      {/* --- Body Section --- */}
      <View style={styles.body}>
        <Text variant="h2" style={styles.title}>
          {listing.title}
        </Text>

        <Text variant="h1" style={styles.price}>
          ₹{Number(listing.price).toFixed(2)}
        </Text>

        <Text variant="h3" style={{ marginTop: 12 }}>
          Description
        </Text>
        <Text style={styles.description}>{listing.description}</Text>

        {/* --- Seller Information --- */}
        <View style={styles.sellerBox}>
          <Text variant="h3">Seller</Text>

          {sellerLoading ? (
            <ActivityIndicator size="small" />
          ) : (
            <>
              <Text style={styles.sellerName}>{seller?.name ?? 'Unknown'}</Text>
              <Text style={styles.sellerMeta}>
                {seller?.email ?? seller?.contact ?? ''}
              </Text>
            </>
          )}

          <Button
            title="Chat with Seller"
            onPress={() =>
              navigation.navigate('ChatScreen', {
                chatId: `${sellerId}-${listing.id}`,
                otherUserName: seller?.name ?? 'Seller',
              })
            }
          />
        </View>
      </View>
    </ScrollView>
  );
};

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: '#fff' },

  image: {
    width: SCREEN_W,
    height: SCREEN_W * 0.75,
    backgroundColor: '#eee',
  },

  body: {
    padding: 16,
  },

  title: {
    fontSize: 22,
    fontWeight: '700',
  },

  price: {
    fontSize: 20,
    color: '#2ecc71',
    marginTop: 6,
  },

  description: {
    marginTop: 8,
    color: '#444',
    fontSize: 15,
  },

  sellerBox: {
    marginTop: 16,
    padding: 12,
    backgroundColor: '#fafafa',
    borderRadius: 8,
  },

  sellerName: {
    fontSize: 16,
    fontWeight: '600',
    marginTop: 6,
  },

  sellerMeta: {
    color: '#666',
    marginBottom: 10,
  },

  center: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
});
