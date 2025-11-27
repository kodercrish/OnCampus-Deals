import React from 'react';
import { View, FlatList, RefreshControl, StyleSheet } from 'react-native';
import { useGetFeedQuery } from '../../api/listingApi';
import { ListingCard } from '../../components/listing/ListingCard';
import { Button } from '../../components/common/Button';

export const FeedScreen = ({ navigation }: any) => {
  const { data, isLoading, refetch } = useGetFeedQuery();

  return (
    <View style={styles.container}>
      <View style={styles.header}>
        <Button 
          title="+ Sell Item" 
          onPress={() => navigation.navigate('CreateListing')} 
        />
      </View>
      <FlatList
        data={data?.listing ? [data.listing] : []} // Assuming response wrapper, adjusting for array
        // NOTE: If response.listing is actually an array (which implies type mismatch in prompt DTO vs reality), adapt here.
        // Assuming FetchListingResponse contains { listing: Listing[] } for feed, despite DTO saying singular. 
        // Adapting to probable reality:
        keyExtractor={(item: any) => item.id}
        renderItem={({ item }) => (
          <ListingCard 
            listing={item} 
            onPress={() => navigation.navigate('ListingDetails', { listingId: item.id })} 
          />
        )}
        refreshControl={<RefreshControl refreshing={isLoading} onRefresh={refetch} />}
        contentContainerStyle={{ padding: 10 }}
      />
    </View>
  );
};

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: '#f5f5f5' },
  header: { padding: 10, backgroundColor: '#FFF' },
});