import { Routes } from '@angular/router';
import {WebActionComponent} from "./web-action/web-action.component";
import {tieneTokenGuard} from "./_shared/tiene-token.guard";
import {WebActionSuccessComponent} from "./web-action-success/web-action-success.component";
import {WebActionErrorComponent} from "./web-action-error/web-action-error.component";

export const routes: Routes = [
  {path: "webaction", component: WebActionComponent, canActivate: [tieneTokenGuard]},
  {path: "success", component: WebActionSuccessComponent},
  {path: "error", component: WebActionErrorComponent}
];
