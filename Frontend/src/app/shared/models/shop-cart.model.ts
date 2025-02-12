import { IProduct } from './product.model';

export interface IShopCard {
  data: {
    id: number;
    shopCardLines: IShopCardLine[];
    totalPrice: number;
  };
}

export interface IShopCardLine {
  id: number;
  product: IProduct;
  quantity: number;
}

export interface IProductCategory {
  id: number;
  name: string;
}

export interface ICartProducts {
  id: number;
  product: IProduct;
  quantity: number;
}
