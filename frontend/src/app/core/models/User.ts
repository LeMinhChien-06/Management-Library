import { Role } from "./role.enum";

export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  token: string;
}

export interface LogoutRequest {
  token: string;
}

export interface UserList{
  id: number;
  username: string;
  email: string;
  fullName: string;
  phone?: string;
  active: boolean;
}

export interface UserDetail {
  id: number;
  username: string;
  email: string;
  fullName: string;
  phone?: string;
  roles: string;
  active: boolean;
  createdAt: Date;
  updatedAt: Date;
}

export interface User {
  id: number;
  username: string;
  password: string;
  email: string;
  fullName: string;
  phone?: string;
  role: Role;
  active: boolean;
  borrowings?: any[];
  userLogs?: any[];
  createdAt: Date;
  updatedAt: Date;
}
