export interface IApiResponse<T> {
  timeStamp: string;
  statusCode: number;
  status: string;
  message: string;
  data: T;
}

export interface IAuthResponseData {
  token: {
    accessToken: string;
    refreshToken: string;
  };
}
