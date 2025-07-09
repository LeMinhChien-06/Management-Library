import {Component, OnDestroy, OnInit} from '@angular/core';
import {User, UserDetail, UserList} from '../../../core/models/User';
import {UserService} from '../../../core/services/UserService';
import {NgForOf, NgIf} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {UserDetailComponent} from '../user-detail/user-detail.component';
import {debounceTime, distinctUntilChanged, Subject} from 'rxjs';

@Component({
  selector: 'app-user',
  imports: [
    NgForOf,
    NgIf,
    FormsModule,
    UserDetailComponent // Import component mới
  ],
  templateUrl: './user.component.html',
  styleUrl: './user.component.css'
})
export class UserComponent implements OnInit, OnDestroy {

  private searchSubject = new Subject<string>();

  users: UserList[] = [];
  selectedUser: UserDetail | null = null;
  showUserDetail = false;
  currentPage = 0;
  pageSize = 10;
  totalPages = 0;
  searchKeyword = '';
  isLoading = false;

  constructor(private userService: UserService) {
  }


  ngOnInit(): void {
    // Setup debounce search
    this.searchSubject.pipe(
      debounceTime(300), // Đợi 300ms sau khi user ngừng gõ
      distinctUntilChanged() // Chỉ search khi keyword thay đổi
    ).subscribe(keyword => {
      this.performSearch(keyword);
    });

    this.loadUsers();
  }

  ngOnDestroy(): void {
    this.searchSubject.complete();
  }

  getUserDetail(userId: number) {
    this.isLoading = true;
    this.showUserDetail = true;
    this.userService.getUserById(userId)
      .subscribe({
        next: response => {
          this.selectedUser = response.data;
          this.isLoading = false;
          console.log('User detail:', this.selectedUser);
        },
        error: (err) => {
          console.error('Lỗi khi tải chi tiết người dùng:', err);
          this.isLoading = false;
          this.showUserDetail = false;
        }
      });
  }

  onCloseUserDetail() {
    this.showUserDetail = false;
    this.selectedUser = null;
  }

  onEditUser(user: UserDetail) {
    console.log('Edit user:', user);
    // Implement edit functionality
    this.showUserDetail = false;
  }

  onDeleteUser(user: UserDetail) {
    console.log('Delete user:', user);
    // Implement delete functionality
    this.showUserDetail = false;
    // Reload users after delete
    this.loadUsers();
  }

  loadUsers(): void {
    this.userService.getUsers(this.currentPage, this.pageSize, this.searchKeyword)
      .subscribe({
        next: (response) => {
          this.users = response.data.content;
          this.totalPages = response.data.totalPages;
        },
        error: (err) => {
          console.error('Lỗi khi tải người dùng:', err);
        }
      });
  }

  onPageChange(page: number): void {
    this.currentPage = page;
    this.loadUsers();
  }

  onSearch(keyword: string): void {
    this.searchKeyword = keyword.trim();
    this.searchSubject.next(keyword);
  }

  private performSearch(keyword: string): void {
    this.currentPage = 0;
    this.loadUsers();
  }

  getCurrentDate(): string {
    return new Date().toLocaleDateString('vi-VN');
  }

  getPaginationNumbers(): number[] {
    const totalNumbers = Math.min(5, this.totalPages);
    const half = Math.floor(totalNumbers / 2);
    let start = Math.max(0, this.currentPage - half);
    let end = Math.min(this.totalPages - 1, start + totalNumbers + 1);

    if (end - start + 1 < totalNumbers) {
      start = Math.max(0, end - totalNumbers + 1);
    }

    const numbers: number[] = [];
    for (let i = start; i <= end; i++) {
      numbers.push(i + 1);
    }
    return numbers;
  }

  get Math() {
    return Math;
  }
}
