import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FavoritesProductsComponent } from './favorites-products.component';

describe('FavoritesProductsComponent', () => {
  let component: FavoritesProductsComponent;
  let fixture: ComponentFixture<FavoritesProductsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FavoritesProductsComponent]
    });
    fixture = TestBed.createComponent(FavoritesProductsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
