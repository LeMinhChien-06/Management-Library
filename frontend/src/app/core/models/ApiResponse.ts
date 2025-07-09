export interface ApiResponse<T> {
  success: boolean;
  messageCode: string;
  message: string;
  data: T;
  errors?: string[];
  error?: string;
  timestamp: string;
  path?: string;
  statusCode: number;
}
