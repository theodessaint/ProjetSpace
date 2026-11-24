import { Component, inject, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { MusiqueService } from './service/musique-service';

@Component({
  imports: [RouterOutlet],
  selector: 'app-root',
  styleUrl: './app.css',
  templateUrl: './app.html',
})
export class App {
  protected readonly title = signal('space-station-front');

  musique = inject(MusiqueService);
}
