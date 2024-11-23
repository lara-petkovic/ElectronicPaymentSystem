import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/infrastructure/auth/auth.service';
import { ItemDto } from 'src/app/infrastructure/auth/model/itemDto.model';
import { FeatureModulesServicesService } from '../feature-modules-services.service';

@Component({
  selector: 'xp-subscribed-items-overview',
  templateUrl: './subscribed-items-overview.component.html',
  styleUrls: ['./subscribed-items-overview.component.css']
})
export class SubscribedItemsOverviewComponent implements OnInit{
  userId: number;
  itemDtos: ItemDto[] = [];

  constructor(private service: FeatureModulesServicesService , private authService: AuthService) {

  }

  ngOnInit(): void {
    this.userId = this.authService.user$.value.id;
    this.loadItemDtos();
  }

  loadItemDtos(): void {
    this.service.getItemDtos(this.userId).subscribe({
      next: (result) => {
        this.itemDtos = result;
      },
      error: (err) => {
        console.error('Error fetching itemDtos:', err);
        alert('Error fetching itemDtos. Please try again.');
      }
    });
  }
}
