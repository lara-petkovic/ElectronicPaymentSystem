import { Component, Input } from '@angular/core';
import { ImageService } from '../image.service';

@Component({
  selector: 'app-qr-code',
  templateUrl: './qr-code.component.html',
  styleUrls: ['./qr-code.component.css']
})
export class QrCodeComponent {
  qrCodeSource: string = ''

  constructor(private imageService: ImageService) {}

  ngOnInit() {
    this.qrCodeSource = this.imageService.getImage()
  }
}
