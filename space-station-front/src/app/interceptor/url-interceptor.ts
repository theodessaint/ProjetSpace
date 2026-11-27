import { HttpInterceptorFn } from '@angular/common/http';

const API_URL = 'http://localhost:7002';

export const apiUrlInterceptor: HttpInterceptorFn = (req, next) => {
  const apiReq = req.clone({
    url: API_URL + req.url
  });

  return next(apiReq);
};
