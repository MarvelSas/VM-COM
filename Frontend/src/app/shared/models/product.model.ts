export interface IProductsResponse {
  products: IProduct[];
  totalAmountOfItems: number;
  totalAmountOfPages: number;
}

export interface IOneProductResponse {
  product: IProduct;
}

export interface IProductCategory {
  id: number;
  name: string;
}

export interface IProduct {
  id: number;
  name: string;
  price: string;
  photos: string[];
  mainPhotoId: number;
  productCategory: IProductCategory;
  description: string;
  productSpecificationLines: IProductSpecificationLines[];
  additionalInformation: string;
}

export interface IProductSpecificationLines {
  id?: number;
  title: string;
  value: string;
}

export interface IPageableParams {
  page?: number;
  totalPages?: number;
  pageSize?: number;
  category?: string;
  minPrice?: number;
  maxPrice?: number;
  inStock?: boolean;
  sortBy?: string;
  order?: string;
  name?: string;
}

export interface IAddProductReq {
  product: IProduct;
  quantity: number;
}
