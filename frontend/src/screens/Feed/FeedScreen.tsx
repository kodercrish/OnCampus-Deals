// src/screens/Feed/FeedScreen.tsx
import React, { useMemo } from 'react';
import {
  View,
  FlatList,
  RefreshControl,
  StyleSheet,
  SafeAreaView,
} from 'react-native';
import { useGetFeedQuery } from '../../api/listingApi';
import { ListingCard } from '../../components/listing/ListingCard';
import { Button } from '../../components/common/Button';
import { Text } from '../../components/common/Text';

export const FeedScreen = ({ navigation }: any) => {
  const { data, isLoading, refetch, error } = useGetFeedQuery();

  const listings: any[] = useMemo(() => {
    if (!data) return [];
    // server returns { listings: Listing[] } or maybe { data: { listings: [...] } }
    let maybe: any = data;
    if (maybe.data && typeof maybe.data === 'string') {
      try { maybe = JSON.parse(maybe.data); } catch (_) { maybe = maybe.data; }
    } else if (maybe.data) {
      maybe = maybe.data;
    }

    if (Array.isArray(maybe)) return maybe;
    if (Array.isArray(maybe.listings)) return maybe.listings;
    if (Array.isArray(maybe.listing)) return maybe.listing;
    if (maybe.listing && typeof maybe.listing === 'object') return [maybe.listing];

    return [];
  }, [data]);

  console.log('feed data:', data, 'normalized listings length:', listings.length, 'error:', error);

  const renderItem = ({ item }: { item: any }) => (
    <ListingCard
      listing={item}
      onPress={(id?: string) => navigation.navigate('ListingDetails', { listingId: id ?? item.id })}
    />
  );

  return (
    <SafeAreaView style={styles.screen}>
      <View style={styles.header}>
        <Text variant="h2">OnCampus Deals</Text>
        <View style={styles.headerButtons}>
          <Button title="+ Sell Item" onPress={() => navigation.navigate('CreateListing')} />
        </View>
      </View>

      <FlatList
        data={listings}
        keyExtractor={(item: any, idx) => item.id ?? item.listingId ?? String(idx)}
        renderItem={renderItem}
        contentContainerStyle={styles.listContent}
        refreshControl={<RefreshControl refreshing={isLoading} onRefresh={() => refetch()} />}
        ListEmptyComponent={
          <View style={styles.empty}>
            {isLoading ? <Text>Loading...</Text> : <Text>No listings yet.</Text>}
          </View>
        }
      />
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  screen: { flex: 1, backgroundColor: '#f5f5f7' },
  header: {
    padding: 12,
    borderBottomWidth: 1,
    borderBottomColor: '#eee',
    backgroundColor: '#fff',
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
  },
  headerButtons: { flexDirection: 'row' },
  listContent: { padding: 10, paddingBottom: 40 },
  empty: { padding: 20, alignItems: 'center' },
});
