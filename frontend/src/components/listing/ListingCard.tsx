import React from 'react';
import { View, Image, StyleSheet, TouchableOpacity } from 'react-native';
import { Listing } from '../../types/models';
import { Text } from '../common/Text';

interface Props {
  listing: Listing;
  onPress: () => void;
}

export const ListingCard: React.FC<Props> = ({ listing, onPress }) => {
  const imageSource = listing.images?.[0]?.url 
    ? { uri: listing.images[0].url } 
    : { uri: 'https://via.placeholder.com/150' };

  return (
    <TouchableOpacity style={styles.card} onPress={onPress}>
      <Image source={imageSource} style={styles.image} />
      <View style={styles.content}>
        <Text variant="h2">{listing.title}</Text>
        <Text variant="h2" style={styles.price}>${listing.price}</Text>
        <Text variant="caption">{listing.category}</Text>
        {listing.status !== 'AVAILABLE' && (
          <Text style={styles.status}>{listing.status}</Text>
        )}
      </View>
    </TouchableOpacity>
  );
};

const styles = StyleSheet.create({
  card: {
    flexDirection: 'row',
    padding: 10,
    backgroundColor: '#FFF',
    marginBottom: 8,
    borderRadius: 8,
    elevation: 2,
  },
  image: {
    width: 80,
    height: 80,
    borderRadius: 4,
    backgroundColor: '#eee',
  },
  content: {
    marginLeft: 10,
    flex: 1,
    justifyContent: 'center',
  },
  price: {
    color: '#2ecc71',
    marginTop: 4,
  },
  status: {
    color: 'red',
    fontSize: 12,
    fontWeight: 'bold',
    marginTop: 4,
  },
});