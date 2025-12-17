import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../core/services/api.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
  standalone: false
})
export class HomeComponent implements OnInit {
  apiStatus: any = null;
  loading = true;
  error: string | null = null;

  constructor(private apiService: ApiService) {}

  ngOnInit(): void {
    this.checkApiStatus();
  }

  checkApiStatus(): void {
    this.loading = true;
    this.error = null;

    this.apiService.get<any>('').subscribe({
      next: (response) => {
        this.apiStatus = response;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Backend non disponible';
        this.loading = false;
      }
    });
  }
}
