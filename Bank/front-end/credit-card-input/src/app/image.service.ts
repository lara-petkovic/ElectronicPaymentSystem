import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class ImageService {
  private base64Image: string = '';

  setImage(base64: string) {
    this.base64Image = base64;
  }

  getImage(): string {
    return this.base64Image;
  }
}
