import {
  Directive,
  Input,
  OnDestroy,
  TemplateRef,
  ViewContainerRef,
} from '@angular/core';
import { RoleService } from '../services/role.service';
import { Subscription } from 'rxjs';

@Directive({
  selector: '[hasRole]',
})
export class HasRoleDirective implements OnDestroy {
  private currentRoles: string[] = [];
  private roleSubscription: Subscription;

  constructor(
    private templateRef: TemplateRef<any>,
    private viewContainer: ViewContainerRef,
    private roleService: RoleService
  ) {
    this.roleSubscription = this.roleService.roles$.subscribe(() => {
      this.updateView();
    });
  }

  @Input()
  set hasRole(roles: string | string[]) {
    // Zawsze zamieniamy na tablicę
    this.currentRoles = Array.isArray(roles) ? roles : [roles];
    this.updateView();
  }

  private updateView(): void {
    if (this.roleService.isAuthorized(this.currentRoles)) {
      console.log('tak');
      this.viewContainer.createEmbeddedView(this.templateRef);
    } else {
      console.log('nie');
      this.viewContainer.clear();
    }
  }

  ngOnDestroy(): void {
    this.roleSubscription.unsubscribe();
  }
}
