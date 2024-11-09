import { Component,Output,EventEmitter } from '@angular/core';

@Component({
  selector: 'app-psp-client-reg',
  templateUrl: './psp-client-reg.component.html',
  styleUrls: ['./psp-client-reg.component.css']
})
export class PspClientRegComponent {
  @Output() optionsSelected = new EventEmitter<{ name: string ; clientId: string | null }>();

  options = [
    { name: 'Card' },
    { name: 'QR Code' },
    { name: 'PayPal' },
    { name: 'Bitcoin' }
  ];
  
  displayedColumns: string[] = ['name'];
  selectedOptions: string[] = [];
  hoverOption: string = '';


  toggleSelection(option: string) {
    const index = this.selectedOptions.indexOf(option);
    if (index > -1) {
      this.selectedOptions.splice(index, 1);
    } else {
      this.selectedOptions.push(option);
    }
  }

  confirmSelection() {
    alert(`Selected options: ${this.selectedOptions.join(', ')}`);
    if (this.selectedOptions) {
      this.optionsSelected.emit({ name: this.selectedOptions.join(','), clientId:null}); 
      console.log("b")

    }
  }
}
