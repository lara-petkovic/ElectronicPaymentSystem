import { Component, Output, EventEmitter, Input } from '@angular/core';

@Component({
  selector: 'app-psp-client-reg',
  templateUrl: './psp-client-reg.component.html',
  styleUrls: ['./psp-client-reg.component.css']
})
export class PspClientRegComponent {
  @Output() optionsSelected = new EventEmitter<{ name: string; clientId: string | null; walletAddress: string | null }>();

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

  toggleSelection(option: string) {
    const index = this.selectedOptions.indexOf(option);
    if (index > -1) {
      this.selectedOptions.splice(index, 1);
      if (option === 'Crypto') {
        this.walletAddress = null; 
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

    this.optionsSelected.emit({
      name: this.selectedOptions.join(','),
      clientId: null,
      walletAddress: this.selectedOptions.includes('Crypto') ? this.walletAddress : null
    });

    alert(`Selected options: ${this.selectedOptions.join(', ')}\nWallet Address: ${this.walletAddress || 'N/A'}`);
  }
}
