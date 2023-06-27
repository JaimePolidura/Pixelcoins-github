import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {delay, Observable, of} from "rxjs";

const urlApi: string = 'http://localhost:8080';

@Injectable({
  providedIn: 'root'
})
export class WebActionApiService {
  constructor(
    private httpClient: HttpClient
  ) { }

  public sendData(token: string, datos: any): Observable<void> {
    return this.httpClient.post<void>(`${urlApi}/webaction?token=${token}`, datos);
  }

  public getWebActionFormData(token: string): Observable<WebActionFormResponse> {
    return this.httpClient.get<WebActionFormResponse>(`${urlApi}/webaction?token=${token}`);
  }
}

export interface WebActionFormResponse {
  parametros: WebActionFormCampo[];
  nombre: string;
}

export interface WebActionFormCampo {
  nombre: string;
  tipo: 'TEXT' | 'NUMERO';
}
