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
  const { listingId } = route.params;
  const { data, isLoading } = useGetListingDetailsQuery({ listingId });
  const listing = data?.listing ?? null;
  const images = data?.images ?? listing?.images ?? [];

  const sellerId = listing?.sellerId ?? null;
  const { data: sellerData } = useFetchUserQuery({ userId: sellerId }, { skip: !sellerId });

  if (isLoading || !listing) {
    return (
      <View style={styles.center}>
        <ActivityIndicator size="large" />
      </View>
    );
  }

  const seller = sellerData?.user ?? null;

  return (
    <ScrollView style={styles.container}>
      <FlatList
        data={images.length ? images : [{ placeholder: true }]}
        horizontal
        pagingEnabled
        keyExtractor={(it: any, idx) => it.id ?? String(idx)}
        renderItem={({ item }) => (
          <Image source={{ uri: item.imageUrl ?? item.image_url ?? item.url ?? '' }} style={styles.image} />
        )}
        showsHorizontalScrollIndicator={false}
      />

      <View style={styles.body}>
        <Text variant="h2" style={styles.title}>{listing.title}</Text>
        <Text variant="h1" style={styles.price}>₹{Number(listing.price).toFixed(2)}</Text>

        <Text variant="h3" style={{ marginTop: 12 }}>Description</Text>
        <Text style={styles.description}>{listing.description}</Text>

        <View style={styles.sellerBox}>
          <Text variant="h3">Seller</Text>
          <Text style={styles.sellerName}>{seller?.name ?? 'Unknown'}</Text>
          <Text style={styles.sellerMeta}>{seller?.email ?? seller?.contact ?? ''}</Text>

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
  container: { flex: 1 },
  image: { width: SCREEN_W, height: SCREEN_W * 0.75, backgroundColor: '#eee' },
  body: { padding: 16 },
  title: { fontSize: 22, fontWeight: '700' },
  price: { fontSize: 20, color: '#2ecc71', marginTop: 6 },
  description: { marginTop: 8, color: '#444', fontSize: 15 },
  sellerBox: { marginTop: 16, padding: 12, backgroundColor: '#fafafa', borderRadius: 8 },
  sellerName: { fontSize: 16, fontWeight: '600', marginTop: 6 },
  sellerMeta: { color: '#666', marginBottom: 10 },
  center: { flex: 1, justifyContent: 'center', alignItems: 'center' },
});
