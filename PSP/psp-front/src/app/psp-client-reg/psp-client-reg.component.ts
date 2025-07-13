import { Component, Output, EventEmitter, Input } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
  selector: 'app-psp-client-reg',
  templateUrl: './psp-client-reg.component.html',
  styleUrls: ['./psp-client-reg.component.css']
})
export class PspClientRegComponent {
  @Output() optionsSelected = new EventEmitter<{ name: string; clientId: string | null; walletAddress: string | null, paypalClientId: string | null }>();

  options: { name: string }[] = [];
  @Input()
  set optionsP(value: string) {
    this.options = [];

    if (value) {
      const optionsArray = value.split(' ').map(option => option.trim());
      optionsArray.forEach(option => {
        this.options.push({ name: option });
      });
    }
  }

  displayedColumns: string[] = ['name'];
  selectedOptions: string[] = [];
  hoverOption: string = '';
  walletAddress: string | null = null;
  paypalString: string | null = null;

  constructor(private sanitizer: DomSanitizer) {}

  sanitizeInput(input: string): string {
    const sanitized = this.sanitizer.sanitize(1, input);
    return sanitized ?? ''; 
  }

  toggleSelection(option: string) {
    const index = this.selectedOptions.indexOf(option);
    if (index > -1) {
      this.selectedOptions.splice(index, 1);
      if (option === 'Crypto') {
        this.walletAddress = null; 
      }
      if (option === 'PayPal') {
        this.paypalString = null;
      }
    } else {
      this.selectedOptions.push(option);
    }
  }

  confirmSelection() {
    if (this.selectedOptions.includes('Crypto') && !this.walletAddress) {
      alert('Please enter a wallet address for Crypto.');
      return;
    }
    if (this.selectedOptions.includes('PayPal') && !this.paypalString) {
      alert('Please enter a PayPal string.');
      return;
    }
    const sanitizedWalletAddress = this.walletAddress 
      ? this.sanitizeInput(this.walletAddress) 
      : null;
    const sanitizedPaypalString = this.paypalString
      ? this.sanitizeInput(this.paypalString)
      : null;
    this.optionsSelected.emit({
      name: this.selectedOptions.join(','),
      clientId: null,
      walletAddress: this.selectedOptions.includes('Crypto') ? sanitizedWalletAddress : null,
      paypalClientId: this.selectedOptions.includes('PayPal') ? sanitizedPaypalString : null
    });
    alert(`Selected options: ${this.selectedOptions.join(', ')}\nWallet Address: ${sanitizedWalletAddress || 'N/A'}\nPayPal String: ${sanitizedPaypalString || 'N/A'}`);
    this.selectedOptions=[];
    this.walletAddress=null;
    this.paypalString=null;
  }
}
