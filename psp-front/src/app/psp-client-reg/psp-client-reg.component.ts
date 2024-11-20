import { Component,Output,EventEmitter , Input} from '@angular/core';

@Component({
  selector: 'app-psp-client-reg',
  templateUrl: './psp-client-reg.component.html',
  styleUrls: ['./psp-client-reg.component.css']
})
export class PspClientRegComponent {
  @Output() optionsSelected = new EventEmitter<{ name: string ; clientId: string | null }>();

  options: { name: string }[] = []; 
  @Input()
  set optionsP(value: string) {
    this.options = [];

    if (value) {
      const optionsArray = value.split(' ').map(option => option.trim());  

      optionsArray.forEach(option => {
        console.log(option)
        this.options.push({ name: option });  
      });
    }
  }
  
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
