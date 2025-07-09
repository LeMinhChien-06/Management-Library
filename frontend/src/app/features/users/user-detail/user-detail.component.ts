import { Component, Input, Output, EventEmitter, OnInit, PLATFORM_ID, Inject } from '@angular/core';
import { NgIf } from '@angular/common';
import { isPlatformBrowser } from '@angular/common';
import { UserDetail } from '../../../core/models/User';
import { Role } from '../../../core/models/role.enum';

@Component({
  selector: 'app-user-detail',
  standalone: true,
  imports: [NgIf],
  templateUrl: './user-detail.component.html',
  styleUrls: ['./user-detail.component.css']
})
export class UserDetailComponent implements OnInit {
  @Input() user: UserDetail | null = null;
  @Input() isVisible: boolean = false;
  @Input() isLoading: boolean = false;

  @Output() close = new EventEmitter<void>();
  @Output() edit = new EventEmitter<UserDetail>();
  @Output() delete = new EventEmitter<UserDetail>();

  private isBrowser: boolean;

  constructor(@Inject(PLATFORM_ID) private platformId: Object) {
    this.isBrowser = isPlatformBrowser(this.platformId);
  }

  ngOnInit(): void {}

  onClose(): void {
    this.close.emit();
  }

  onEdit(): void {
    if (this.user) {
      this.edit.emit(this.user);
    }
  }

  onDelete(): void {
    if (this.user && this.isBrowser && confirm(`Bạn có chắc chắn muốn xóa người dùng ${this.user.fullName}?`)) {
      this.delete.emit(this.user);
    }
  }

  formatDate(date: Date): string {
    if (!this.isBrowser) return '';
    return new Date(date).toLocaleDateString('vi-VN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    });
  }

  getInitials(fullName: string): string {
    return fullName
      .split(' ')
      .map(word => word.charAt(0))
      .join('')
      .toUpperCase()
      .slice(0, 2);
  }

  getRoleDisplayName(role: string): string {
    const roleMap: Record<string, string> = {
      'ADMIN': 'Quản trị viên',
      'LIBRARIAN': 'Thủ thư',
      'USER': 'Người dùng'
    };
    return roleMap[role] || role;
  }

  getRoleClass(role: string): string {
    const roleClassMap: Record<string, string> = {
      'ADMIN': 'role-admin',
      'LIBRARIAN': 'role-librarian',
      'USER': 'role-user'
    };
    return roleClassMap[role] || 'role-user';
  }

  onOverlayClick(event: Event): void {
    if (event.target === event.currentTarget) {
      this.onClose();
    }
  }
}
