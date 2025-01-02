import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-success',
  templateUrl: './success.component.html',
  styleUrls: ['./success.component.scss']
})
export class SuccessComponent implements OnInit {
  transactionDetails: any;

  constructor(private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.transactionDetails = {
        orderId: params['orderId'],
        merchantId: params['merchantId'],
        amount: params['amount'],
        timestamp: params['timestamp']
      };
    });
  }
}