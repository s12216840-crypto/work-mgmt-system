import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const token = authService.getToken();

  console.log('🔥 INTERCEPTOR RUNNING');
  console.log('🔥 TOKEN:', token);

  if (!token) {
    console.log('❌ NO TOKEN');
    return next(req);
  }

  const authReq = req.clone({
    setHeaders: {
      Authorization: `Bearer ${token}`,
    },
  });

  console.log('✅ AUTH HEADER:', authReq.headers.get('Authorization'));

  return next(authReq);
};
