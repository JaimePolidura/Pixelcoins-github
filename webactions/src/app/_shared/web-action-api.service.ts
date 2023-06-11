import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable, of} from "rxjs";

const urlApi: string = 'http://localhost:8080';

@Injectable({
  providedIn: 'root'
})
export class WebActionApiService {
  constructor(
    private httpClient: HttpClient
  ) { }

  public sendData(token: string, datos: any): Observable<void> {
    // return this.httpClient.post<void>(`${urlApi}/webaction?token=${token}`, datos);
    return new Observable<void>(subscriber => {
      subscriber.next();
    });
  }

  public getWebActionFormData(token: string): Observable<WebActionFormResponse> {
    // return this.httpClient.get<WebActionFormResponse>(`${urlApi}/webaction?token=${token}`);
    return of({
      nombre: "Proponer director",
      campos: [
        {nombre: "nombreDeLaEmpresa", tipo: 'TEXTO'},
        {nombre: "nombreDelNuevoDirector", tipo: 'TEXTO'},
        {nombre: "descripccion", tipo: 'TEXTO'},
        {nombre: "sueldo", tipo: 'NUMERO'},
        {nombre: "periodoPagoEnSegundos", tipo: 'NUMERO'}
      ]
    });
  }
}

export interface WebActionFormResponse {
  nombre: string;
  campos: WebActionFormCamposResponse[];
}

export interface WebActionFormCamposResponse {
  nombre: string;
  tipo: 'TEXTO' | 'NUMERO';
}
