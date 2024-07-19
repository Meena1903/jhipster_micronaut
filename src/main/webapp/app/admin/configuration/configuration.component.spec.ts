import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import ConfigurationComponent from './configuration.component';
import { ConfigurationService } from './configuration.service';

describe('ConfigurationComponent', () => {
  let comp: ConfigurationComponent;
  let fixture: ComponentFixture<ConfigurationComponent>;
  let service: ConfigurationService;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, ConfigurationComponent],
      providers: [ConfigurationService],
    })
      .overrideTemplate(ConfigurationComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConfigurationComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ConfigurationService);
  });

  describe('OnInit', () => {
    it('should set all default values correctly', () => {
      expect(comp.filter).toBe('');
    });
    it('Should call load all on init', () => {
      // GIVEN
      const body = [{ config: 'test', properties: 'test' }, { config: 'test2' }];
      jest.spyOn(service, 'get').mockReturnValue(of(body));

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.get).toHaveBeenCalled();
      expect(comp.allConfiguration).toEqual(body);
    });
  });
});
