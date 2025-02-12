export interface IApiResponse<T> {
  timeStamp: string;
  statusCode: number;
  status: string;
  message: string;
  data: T;
}

export interface IAuthResponse {
  token: {
    accessToken: string;
    refreshToken: string;
  };
}
