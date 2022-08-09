import { Dimensions } from './dimensions.interface';

export interface FurnitureCreateUpdate {
    name: string;
    dimensions: Dimensions;
    color: string;
}

export interface Furniture extends FurnitureCreateUpdate{
    id: number;
}
