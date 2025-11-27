import React, { useState } from 'react';
import { View, TextInput, FlatList, StyleSheet } from 'react-native';
import { useLazySearchListingsQuery } from '../../api/searchApi';
import { ListingCard } from '../../components/listing/ListingCard';
import { Button } from '../../components/common/Button';

export const SearchScreen = ({ navigation }: any) => {
  const [keyword, setKeyword] = useState('');
  const [triggerSearch, { data, isLoading }] = useLazySearchListingsQuery();

  const handleSearch = () => {
    triggerSearch({ keyword });
  };

  return (
    <View style={styles.container}>
      <View style={styles.header}>
        <TextInput 
          style={styles.input} 
          placeholder="Search items..." 
          value={keyword}
          onChangeText={setKeyword}
        />
        <Button title="Go" onPress={handleSearch} loading={isLoading} />
      </View>
      <FlatList
        data={data?.results || []}
        keyExtractor={(item) => item.id}
        renderItem={({ item }) => (
          <ListingCard 
            listing={item} 
            onPress={() => navigation.navigate('ListingDetails', { listingId: item.id })} 
          />
        )}
      />
    </View>
  );
};

const styles = StyleSheet.create({
  container: { flex: 1 },
  header: { flexDirection: 'row', padding: 10, alignItems: 'center' },
  input: { flex: 1, borderWidth: 1, borderColor: '#ccc', borderRadius: 8, padding: 8, marginRight: 10, backgroundColor: '#FFF' },
});