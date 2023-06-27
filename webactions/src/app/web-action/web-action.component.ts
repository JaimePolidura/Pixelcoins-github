import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Params, Router} from "@angular/router";
import {
  WebActionApiService,
  WebActionFormParam,
  WebActionFormResponse
} from "../_shared/web-action-api.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {BehaviorSubject, map, tap} from "rxjs";

@Component({
  selector: 'app-web-action',
  templateUrl: './web-action.component.html',
  styleUrls: ['./web-action.component.scss']
})
export class WebActionComponent implements OnInit {
  nombre: string = "";
  formulario: FormGroup;
  token: string = "";

  $loading: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
  $error: BehaviorSubject<string> = new BehaviorSubject<string>("");
  numeroErroresConsecutivos: number = 0;

  camposByNombre: Map<string, WebActionFormParam> = new Map<string, WebActionFormParam>();
  paramsFormulario: string[] = [];

  constructor(
    private webActionBackend: WebActionApiService,
    private activeRoute: ActivatedRoute,
    private formBuilder: FormBuilder,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.formulario = this.formBuilder.group({});

    this.activeRoute.queryParams.subscribe((params: Params): void => {
      this.token = params["token"];

      this.webActionBackend.getWebActionFormData(this.token)
          .pipe(map((res: WebActionFormResponse): WebActionFormResponse => {
            return {
              ...res,
              params: res.params.sort((a: WebActionFormParam, b: WebActionFormParam) => b.showPriority - a.showPriority)
            };
          }))
          .pipe(tap((res: WebActionFormResponse): void => {
            res.params.forEach((param: WebActionFormParam) => this.paramsFormulario.push(param.name))
          }))
          .subscribe((formulario: WebActionFormResponse): void => {
            this.setupFormulario(formulario);
          }, err => {
            this.router.navigateByUrl("/error")
          });
    });
  }

  onSubmit($event: any): void {
    this.$error.next("");

    const formData: any = JSON.stringify(this.formulario.value);
    this.$loading.next(true);

    this.webActionBackend.sendData(this.token, formData).subscribe(() => {
      this.router.navigateByUrl("/success");
      this.$loading.next(false);
    }, err => {
      this.numeroErroresConsecutivos++;
      this.$loading.next(false);

      this.mostrarError(err);
    });
  }

  private mostrarError(err: any): void {
    const muchosErroresMensaje: string = this.numeroErroresConsecutivos > 2 ? 'Si crees que no deberia ser un error, genera de nuevo el link en el servidor de minecraft' : '';

    if(err != undefined){
      this.$error.next(`${err.error} ${muchosErroresMensaje}`);
    }else{
      this.$error.next(`Error hablar con Jaime ${muchosErroresMensaje}`);
    }
  }

  setupFormulario(formulario: WebActionFormResponse): void {
    this.nombre = formulario.name;

    formulario.params.forEach((campo: WebActionFormParam): void => {
      this.camposByNombre.set(campo.name, campo);
      this.formulario.addControl(campo.name, this.formBuilder.control("", Validators.required));
    });
  }

  getNombreBoton(nombre: string): string {
    return this.primmeraLetraMayusculas(nombre.split(" ")[0]);
  }

  splitNombreCamelCaseEnEspacios(nombre: string): string {
    let transformedString: string = nombre.replace(/([A-Z])/g, ' $1'); // Insert a space before each capital letter
    return this.primmeraLetraMayusculas(transformedString) // Capitalize the first letter;
  }

  private primmeraLetraMayusculas(text: string): string {
    const formattedText = text.replace(/([a-z])([A-Z])/g, '$1 $2');
    const words = formattedText.split(' ');
    const capitalizedWords = words.map((word, index) => {
      if (index === 0 || word.length === 1) {
        return word.charAt(0).toUpperCase() + word.slice(1).toLowerCase();
      } else {
        return word.toLowerCase();
      }
    });

    return capitalizedWords.join(' ');
  }
}
