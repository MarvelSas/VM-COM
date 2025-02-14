export interface ICategory {
  id: number;
  name: string;
}

export class Category {
  constructor(private _id, public name) {}

  get id() {
    return this._id;
  }
}

export interface ICategoriesAddResponseData {
  data: {
    productCategory: boolean;
  };
  message: string;
  status: string;
  statusCode: number;
  timeStamp: string;
}

export interface ICategoriesGetResponseData {
  data: {
    productCategories: any;
  };
}
