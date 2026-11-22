import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { Auth } from '../services/auth';

export const roleGuard: CanActivateFn = (route, state) => {
  const auth = inject(Auth); const router = inject(Router); const roles = route.data['roles'] as string[] | undefined;
  return !!auth.user() && (!roles || roles.includes(auth.user()!.role)) || router.createUrlTree(['/']);
};
