import {Injectable} from '@angular/core';
import {environment} from "../../environments/environment";
import {HttpClient, HttpParams} from '@angular/common/http';
import {User, UserDetail, UserList} from '../models/User';
import {Observable} from 'rxjs';
import {ApiResponse} from '../models/ApiResponse';
import {PageResponse} from '../models/PageResponse';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = `${environment.hostBackend}`;

  constructor(private http: HttpClient) {
  }

  // getUsers(page: number, size: number, search?: string): Observable<ApiResponse<PageResponse<UserList>>> {
  //   const params: any = {page, size, ...(search && {search})};
  //   return this.http.get<ApiResponse<PageResponse<UserList>>>(`${this.apiUrl}/user/all`, {params});
  // }

  getUsers(page: number, size: number, search?: string): Observable<ApiResponse<PageResponse<UserList>>> {
    const params: any = {page, size};

    if (search && search.trim() !== '') {
      const trimmed = search.trim();

      if (/^\d+$/.test(trimmed)) {
        // Toàn số → phone
        params.phone = trimmed;
      } else if (trimmed.includes('@')) {
        // Có @ → email
        params.email = trimmed;
      } else {
        // Mặc định → name
        params.name = trimmed;
      }
    }

    return this.http.get<ApiResponse<PageResponse<UserList>>>(`${this.apiUrl}/user/all`, {params});
  }


  getUserById(id: number): Observable<ApiResponse<UserDetail>> {
    return this.http.get<ApiResponse<UserDetail>>(`${this.apiUrl}/user/${id}`);
  }


}
