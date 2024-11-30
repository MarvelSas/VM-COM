import { IProduct } from "./product.model";

export interface IShopCardResponse {
    timeStamp: string;
    statusCode: number;
    status: string;
    message: string;
    data: IShopCardData;
  }
  
  export interface IShopCardData {
    id: number;
    shopCardLines: IShopCardLine[];
    totalPrice: number;
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