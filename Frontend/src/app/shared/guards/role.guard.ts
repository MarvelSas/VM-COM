import { inject } from '@angular/core';
import { CanActivateFn } from '@angular/router';
import { RoleService } from '../services/role.service';

export const RoleGuard: CanActivateFn = (route, state) => {
  const roleService = inject(RoleService);
  const requiredRoles = route.data['roles'] as Array<string>;
  return roleService.isAuthorized(requiredRoles);
};
