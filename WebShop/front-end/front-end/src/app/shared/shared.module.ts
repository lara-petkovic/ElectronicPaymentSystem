import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatDialogModule } from '@angular/material/dialog';
import {MatCardModule} from '@angular/material/card'; 

@NgModule({
    declarations: [
    ],
    imports: [
        CommonModule,
        MatDialogModule,
        MatCardModule
    ],
    exports: [
    ]
})
export class SharedModule { }
