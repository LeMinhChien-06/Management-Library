import { Component, inject, OnInit, OnDestroy, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet, RouterLink, Router } from '@angular/router';
import { AuthService } from '../../core/services/AuthService';
import { finalize } from 'rxjs';

@Component({
  selector: 'app-cms-layout',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterLink],
  templateUrl: './cms-layout.component.html',
  styleUrl: './cms-layout.component.css'
})
export class CmsLayoutComponent implements OnInit, OnDestroy {
  private router = inject(Router);
  private authService = inject(AuthService);

  isLoading = false;
  isSidebarOpen = false; // Mặc định đóng trên mobile
  isUserMenuOpen = false;

  ngOnInit() {
    // Kiểm tra kích thước màn hình khi khởi tạo
    this.checkScreenSize();
  }

  ngOnDestroy() {
    // Cleanup nếu cần
  }

  @HostListener('window:resize', ['$event'])
  onResize(event: any) {
    this.checkScreenSize();
  }

  private checkScreenSize() {
    // Kiểm tra xem có đang chạy trong browser không
    if (typeof window !== 'undefined') {
      // Tự động mở sidebar trên desktop (>= 1024px)
      if (window.innerWidth >= 1024) {
        this.isSidebarOpen = true;
      } else {
        this.isSidebarOpen = false;
      }
    }
  }

  toggleSidebar() {
    this.isSidebarOpen = !this.isSidebarOpen;
    // Đóng user menu khi toggle sidebar
    this.isUserMenuOpen = false;
  }

  closeSidebar() {
    // Chỉ đóng sidebar trên mobile
    if (typeof window !== 'undefined' && window.innerWidth < 1024) {
      this.isSidebarOpen = false;
    }
    this.isUserMenuOpen = false;
  }

  toggleUserMenu() {
    this.isUserMenuOpen = !this.isUserMenuOpen;
  }

  logout() {
    this.isLoading = true;
    this.isUserMenuOpen = false;

    this.authService.logout()
      .pipe(
        finalize(() => this.isLoading = false)
      )
      .subscribe({
        next: () => {
          this.router.navigate(['/login']);
        },
        error: (error) => {
          console.error('Logout failed:', error);
        }
      });
  }
}
