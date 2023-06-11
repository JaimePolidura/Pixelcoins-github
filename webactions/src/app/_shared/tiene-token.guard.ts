import {ActivatedRouteSnapshot, CanActivateFn, RouterStateSnapshot} from '@angular/router';

export const tieneTokenGuard: CanActivateFn = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean => {
  return true;
};
