import React, { useState } from 'react';
import { View, TextInput, StyleSheet, FlatList } from 'react-native';
import { Text } from '../../components/common/Text';
import { Button } from '../../components/common/Button';
import { useSearchListingsMutation } from '../../api/searchApi';
import { ListingCard } from '../../components/listing/ListingCard';

export const SearchScreen = ({ navigation }: any) => {
  const [keyword, setKeyword] = useState('');
  const [category, setCategory] = useState('');
  const [minPrice, setMinPrice] = useState('');
  const [maxPrice, setMaxPrice] = useState('');

  const [searchListings, { data, isLoading }] = useSearchListingsMutation();

  const results = data?.results ?? [];

  const onSearch = async () => {
    try {
      await searchListings({
        keyword: keyword || null,
        category: category || null,
        minPrice: minPrice ? Number(minPrice) : null,
        maxPrice: maxPrice ? Number(maxPrice) : null,
      }).unwrap();
    } catch (e) {
      console.log('Search error:', e);
    }
  };

  return (
    <FlatList
      ListHeaderComponent={
        <View style={styles.container}>
          <Text variant="h2" style={{ marginBottom: 8 }}>
            Search Listings
          </Text>

          <TextInput
            placeholder="Keyword (Laptop, Chair...)"
            style={styles.input}
            value={keyword}
            onChangeText={setKeyword}
          />

          <TextInput
            placeholder="Category (optional)"
            style={styles.input}
            value={category}
            onChangeText={setCategory}
          />

          <View style={styles.row}>
            <TextInput
              placeholder="Min Price"
              keyboardType="numeric"
              style={[styles.input, { flex: 1, marginRight: 6 }]}
              value={minPrice}
              onChangeText={setMinPrice}
            />
            <TextInput
              placeholder="Max Price"
              keyboardType="numeric"
              style={[styles.input, { flex: 1, marginLeft: 6 }]}
              value={maxPrice}
              onChangeText={setMaxPrice}
            />
          </View>

          <Button
            title={isLoading ? 'Searching...' : 'Search'}
            onPress={onSearch}
          />
        </View>
      }
      data={results}
      keyExtractor={(item) => item.id}
      renderItem={({ item }) => (
        <ListingCard
          listing={item}
          onPress={() =>
            navigation.navigate('ListingDetails', {
              listingId: item.id,
            })
          }
        />
      )}
      ListEmptyComponent={
        !isLoading && (
          <Text variant="caption" style={{ marginTop: 20, textAlign: 'center' }}>
            No results yet.
          </Text>
        )
      }
      contentContainerStyle={{ padding: 16 }}
    />
  );
};

const styles = StyleSheet.create({
  container: { padding: 16 },
  input: {
    borderWidth: 1,
    borderColor: '#ccc',
    backgroundColor: '#fff',
    borderRadius: 8,
    padding: 10,
    marginBottom: 12,
  },
  row: {
    flexDirection: 'row',
    marginBottom: 12,
  },
});