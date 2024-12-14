import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.scss']
})
export class ProductListComponent {
  products = [
    { id: 1, name: 'Proizvod A', price: 10.99 },
    { id: 2, name: 'Proizvod B', price: 15.49 },
    { id: 3, name: 'Proizvod C', price: 7.99 }
  ];

  addToCart(product: any) {
    localStorage.setItem('selectedProduct', JSON.stringify(product));
    window.location.href = '/checkout';
  }  
}