import { Injectable } from '@angular/core';
import { AuthService } from './auth.service';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class RoleService {
  private rolesSubject = new BehaviorSubject<string[]>([]);
  roles$: Observable<string[]> = this.rolesSubject.asObservable();

  setRoles(roles: string[]) {
    this.rolesSubject.next(roles);
  }

  isAuthorized(roles: string[]): boolean {
    const currentRoles = this.rolesSubject.getValue();
    return roles.some((role) => currentRoles.includes(role));
  }
}
