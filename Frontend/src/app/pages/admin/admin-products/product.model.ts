export interface IProductNew {
  id?: number;
  name: string;
  price: string;
  productCategory: { id: number; name: string };
  amount: number;
  description: string;
  photos?: string[];
  mainPhotoId: number;
  specificationLines: { title: string; value: string }[];
}

export interface IProductResponseData {
  timeStamp: string;
  statusCode: number;
  status: string;
  message: string;
  data: {
    product: boolean;
  };
}

export interface IResPhotoUpload {
  data: { productPhotoName: string };
}
