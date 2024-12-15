export interface IAddressResponse {
  timeStamp: string;
  statusCode: number;
  status: string;
  message: string;
  data: IAddressData;
}

interface IAddressData {
  data: IAddressData2;
}

interface IAddressData2 {
  id: number;
  firstName: string;
  lastName: string;
  phoneNumber: string;
  street: string;
  zipCode: string;
  city: string;
}
