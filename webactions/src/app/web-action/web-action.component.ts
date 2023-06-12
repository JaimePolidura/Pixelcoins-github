import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Params, Router} from "@angular/router";
import {
  WebActionApiService,
  WebActionFormCampo,
  WebActionFormResponse
} from "../_shared/web-action-api.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {BehaviorSubject} from "rxjs";

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

  camposByNombre: Map<string, WebActionFormCampo> = new Map<string, WebActionFormCampo>();

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

      this.webActionBackend.getWebActionFormData(this.token).subscribe((formulario: WebActionFormResponse): void => {
        this.setupFormulario(formulario);
      });
    });
  }

  onSubmit($event: any): void {
    const formData: any = JSON.stringify(this.formulario.value);
    this.$loading.next(true);

    this.webActionBackend.sendData(this.token, formData).subscribe(() => {
      this.router.navigateByUrl("/success");
      this.$loading.next(false);
    }, err => {
      this.$error.next(err.error.error ?? "Error hablar con Jaime");
      console.error(err);
      this.$loading.next(false);
    });
  }

  setupFormulario(formulario: WebActionFormResponse): void {
    this.nombre = formulario.nombre;

    formulario.campos.forEach((campo: WebActionFormCampo): void => {
      this.camposByNombre.set(campo.nombre, campo);
      this.formulario.addControl(campo.nombre, this.formBuilder.control("", Validators.required));
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
