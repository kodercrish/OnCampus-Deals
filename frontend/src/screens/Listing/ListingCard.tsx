// src/components/listing/ListingCard.tsx
import React from 'react';
import {
  View,
  StyleSheet,
  Image,
  Dimensions,
  TouchableOpacity,
  ActivityIndicator,
} from 'react-native';
import { Text } from '../../components/common/Text';
import { Button } from '../../components/common/Button';

const { width: SCREEN_WIDTH } = Dimensions.get('window');

type ListingCardProps = {
  listing: any;
  onPress?: (id?: string) => void;
};

export const ListingCard = ({ listing, onPress }: ListingCardProps) => {
  const images: string[] = React.useMemo(() => {
    if (!listing) return [];
    const imgs = listing.images ?? listing.imageUrls ?? listing.photos ?? null;
    if (!imgs) return [];
    if (Array.isArray(imgs) && imgs.length > 0 && typeof imgs[0] === 'string') {
      return imgs as string[];
    }
    if (Array.isArray(imgs) && typeof imgs[0] === 'object') {
      return imgs.map((it: any) => it.imageUrl ?? it.image_url ?? it.url ?? '').filter(Boolean);
    }
    if (typeof imgs === 'string') return [imgs];
    return [];
  }, [listing]);

  const firstImage = images.length > 0 ? images[0] : null;

  const [loading, setLoading] = React.useState(false);
  const [error, setError] = React.useState(false);

  React.useEffect(() => {
    setError(false);
    setLoading(false);
  }, [firstImage]);

  const title = listing.title ?? listing.name ?? 'Untitled';
  const price = listing.price ?? null;
  const desc = listing.description ?? '';

  const placeholder = require('../../../assets/placeholder.png');

  return (
    <TouchableOpacity
      activeOpacity={0.9}
      onPress={() => onPress?.(listing.id ?? listing.listingId)}
      style={styles.card}
    >
      <View style={styles.imageContainer}>
        {firstImage && !error ? (
          <>
            <Image
              source={{ uri: firstImage }}
              style={styles.image}
              resizeMode="cover"
              onLoadStart={() => { setLoading(true); setError(false); }}
              onLoad={() => setLoading(false)}
              onError={(e) => { console.warn('Image load error', e.nativeEvent || e); setLoading(false); setError(true); }}
            />
            {loading && (
              <View style={styles.loadingOverlay}>
                <ActivityIndicator size="small" />
              </View>
            )}
          </>
        ) : (
          <Image source={placeholder} style={styles.image} resizeMode="cover" />
        )}
      </View>

      <View style={styles.content}>
        <Text variant="h3" style={styles.title}>{title}</Text>

        <View style={styles.row}>
          <Text variant="h2" style={styles.price}>{price != null ? `₹${Number(price).toFixed(2)}` : '—'}</Text>
          <View style={{ flex: 1 }} />
          <Button title="View" onPress={() => onPress?.(listing.id ?? listing.listingId)} />
        </View>

        <Text variant="body" style={styles.description} numberOfLines={3}>{desc}</Text>

        <Text variant="caption" style={styles.meta}>
          {listing.sellerName ? `Seller: ${listing.sellerName}` : `Seller ID: ${listing.sellerId ?? 'Unknown'}`}
        </Text>
      </View>
    </TouchableOpacity>
  );
};

const CARD_PADDING = 10;
const CARD_WIDTH = SCREEN_WIDTH - CARD_PADDING * 2;

const styles = StyleSheet.create({
  card: {
    backgroundColor: '#fff',
    borderRadius: 10,
    marginBottom: 12,
    overflow: 'hidden',
    elevation: 2,
    shadowColor: '#000',
    shadowOpacity: 0.05,
    shadowRadius: 4,
  },
  imageContainer: {
    width: '100%',
    height: 200,
    backgroundColor: '#eee',
    justifyContent: 'center',
    alignItems: 'center',
  },
  image: {
    width: '100%',
    height: 200,
  },
  loadingOverlay: {
    position: 'absolute',
    alignSelf: 'center',
    top: '45%',
  },
  content: {
    padding: 12,
  },
  title: {
    marginBottom: 6,
  },
  price: { color: '#2ecc71', fontWeight: '700' },
  description: { marginTop: 8, color: '#444' },
  row: { flexDirection: 'row', alignItems: 'center' },
  meta: { marginTop: 8, color: '#888', fontSize: 12 },
});
