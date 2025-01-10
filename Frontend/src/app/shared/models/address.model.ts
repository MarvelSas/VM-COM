export interface IAddressResponse {
  timeStamp: string;
  statusCode: number;
  status: string;
  message: string;
  data: IAddressData;
}

export interface IAddressData {
  data: IAddress[];
}

export interface IAddress {
  id: number;
  firstName: string;
  lastName: string;
  phoneNumber: string;
  street: string;
  zipCode: string;
  city: string;
}
