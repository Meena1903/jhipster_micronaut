import { Component, OnInit } from '@angular/core';

import SharedModule from 'app/shared/shared.module';
import { FormsModule } from '@angular/forms';
import { ConfigurationService } from './configuration.service';

import { KeyStartsWith } from './configuration.filter';

@Component({
  standalone: true,
  selector: 'jhi-configuration',
  templateUrl: './configuration.component.html',
  imports: [SharedModule, FormsModule, KeyStartsWith],
})
export default class ConfigurationComponent implements OnInit {
  allConfiguration: any = null;
  filter: string;

  constructor(private configurationService: ConfigurationService) {
    this.filter = '';
  }

  ngOnInit(): void {
    this.configurationService.get().subscribe((configuration: any) => {
      this.allConfiguration = configuration;
    });
  }
}
