import React from 'react';
import { View, ScrollView, Image, StyleSheet, Alert } from 'react-native';
import { useSelector } from 'react-redux';
import { RootState } from '../../store';
import { 
  useGetListingDetailsQuery, 
  useMarkSoldMutation, 
  useMarkUnavailableMutation,
  useDeleteListingMutation 
} from '../../api/listingApi';
import { useCreateChatMutation } from '../../api/chatApi';
import { Button } from '../../components/common/Button';
import { Text } from '../../components/common/Text';

export const ListingDetailsScreen = ({ route, navigation }: any) => {
  const { listingId } = route.params;
  const { userId, role } = useSelector((state: RootState) => state.auth);
  
  const { data, isLoading } = useGetListingDetailsQuery({ listingId });
  const [markSold] = useMarkSoldMutation();
  const [deleteListing] = useDeleteListingMutation();
  const [createChat] = useCreateChatMutation();

  if (isLoading || !data?.listing) return <Text>Loading...</Text>;

  const { listing, images } = data;
  const isOwner = listing.sellerId === userId;
  const isAdmin = role === 'admin';

  const handleContact = async () => {
    try {
      const res = await createChat({ listingId, sellerId: listing.sellerId }).unwrap();
      navigation.navigate('ChatScreen', { 
        chatId: res.chatId, 
        otherUserName: listing.sellerName || 'Seller' 
      });
    } catch (e) {
      Alert.alert('Error', 'Could not start chat');
    }
  };

  const handleDelete = async () => {
    try {
      await deleteListing({ listingId }).unwrap();
      navigation.goBack();
    } catch (e) { Alert.alert('Error', 'Delete failed'); }
  };

  return (
    <ScrollView style={styles.container}>
      <ScrollView horizontal pagingEnabled style={{ height: 250 }}>
        {images?.map((img: any, i: number) => (
          <Image key={i} source={{ uri: img.url }} style={styles.image} />
        ))}
      </ScrollView>

      <View style={styles.details}>
        <Text variant="h1">{listing.title}</Text>
        <Text variant="h2" style={styles.price}>${listing.price}</Text>
        <Text variant="body" style={styles.desc}>{listing.description}</Text>
        <Text variant="caption">Seller: {listing.sellerName || 'Unknown'}</Text>

        <View style={styles.actions}>
          {!isOwner && (
            <Button title="Contact Seller" onPress={handleContact} />
          )}

          {(isOwner || isAdmin) && (
            <>
              <Button title="Delete" onPress={handleDelete} color="red" />
              {isOwner && listing.status === 'AVAILABLE' && (
                <Button 
                  title="Mark Sold" 
                  onPress={() => markSold({ listingId })} 
                  color="green" 
                />
              )}
            </>
          )}
        </View>
      </View>
    </ScrollView>
  );
};

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: '#FFF' },
  image: { width: 400, height: 250, resizeMode: 'cover' },
  details: { padding: 20 },
  price: { color: '#2ecc71', marginVertical: 10 },
  desc: { marginBottom: 20 },
  actions: { marginTop: 20 },
});