export interface Activity {
    id: string;
    title: string;
    price: number;
    rating: number;
    specialOffer?: boolean;
    supplier: {
        name: string;
        rating: number;
    };
}
