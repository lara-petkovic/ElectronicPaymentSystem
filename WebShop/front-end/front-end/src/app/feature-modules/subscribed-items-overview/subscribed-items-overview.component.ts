import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/infrastructure/auth/auth.service';
import { ItemDto } from 'src/app/infrastructure/auth/model/itemDto.model';
import { FeatureModulesServicesService } from '../feature-modules-services.service';

@Component({
  selector: 'xp-subscribed-items-overview',
  templateUrl: './subscribed-items-overview.component.html',
  styleUrls: ['./subscribed-items-overview.component.css']
})
export class SubscribedItemsOverviewComponent implements OnInit {
  userId: number;
  itemDtos: ItemDto[] = [];

  constructor(private service: FeatureModulesServicesService, private authService: AuthService) {}

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
  cancelSubscription(itemName: string, itemPrice: number): void {
    const encodedItemName = encodeURIComponent(itemName);
    this.service.cancelSubscription(this.userId, encodedItemName, itemPrice).subscribe({
      next: () => {
        alert(`Subscription for "${itemName}" has been successfully canceled.`);
        this.loadItemDtos();
      },
      error: (err) => {
        this.loadItemDtos();
        console.error('Error canceling subscription:', err);
      }
    });
    this.loadItemDtos();
    console.log(`Canceling subscription for item:`, itemName);
  }
  

  extendSubscription(itemName: string, itemPrice: number, event: Event): void {
    const target = event.target as HTMLSelectElement; // Type assertion for event.target
    const years = target?.value;
  
    if (!years) {
      alert('Please select a valid duration.');
      return;
    }
  
    const parsedYears = parseInt(years, 10);
    if (isNaN(parsedYears)) {
      alert('Invalid duration selected.');
      return;
    }
  
    console.log(`Extending membership for "${itemName}" priced at ${itemPrice} by ${parsedYears} year(s).`);
    alert(`Membership for "${itemName}" has been extended by ${parsedYears} year(s).`);
  }
  

  private findItem(name: string, price: number): ItemDto | undefined {
    return this.itemDtos.find(item => item.name === name && item.price === price);
  }
}
