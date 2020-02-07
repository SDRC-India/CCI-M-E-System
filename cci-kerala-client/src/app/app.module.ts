import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {
  MatButtonModule, MatCardModule, MatDialogModule, MatInputModule, MatTableModule,
  MatToolbarModule, MatMenuModule, MatIconModule, MatProgressSpinnerModule, MatFormFieldModule, MatTooltipModule
} from '@angular/material';
import { LoginComponent } from './pages/login/login.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FooterComponent } from './pages/fragments/footer/footer.component';
import { HeaderComponent } from './pages/fragments/header/header.component';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { XhrInterceptorService } from './services/xhr-interceptor.service';
import { ToastrModule } from 'ngx-toastr';
import { LoadingBarHttpClientModule } from '@ngx-loading-bar/http-client';
import { LoadingBarRouterModule } from '@ngx-loading-bar/router';
import { LoadingBarModule } from '@ngx-loading-bar/core';
import { NgxSpinnerModule } from 'ngx-spinner';
import { TableModule } from 'lib-table/public_api';
import { DashboardModule } from './pages/dashboard/dashboard.module';
import { ImageViewerModule } from 'ng2-image-viewer';
import {MatBadgeModule} from '@angular/material/badge';
import { CmsModule } from './pages/cms/cms.module';
import { RecourcesComponent } from './pages/recources/recources.component';
@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    FooterComponent,
    HeaderComponent,
    RecourcesComponent
  ],
  imports: [
    FormsModule,
    ReactiveFormsModule,
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatFormFieldModule,
    MatToolbarModule,
    MatButtonModule,
    MatCardModule,
    MatInputModule,
    MatDialogModule,
    MatTableModule,
    MatMenuModule,
    MatIconModule,
    MatProgressSpinnerModule,
    HttpClientModule,
    ToastrModule.forRoot({positionClass: 'toast-top-full-width'}),
    LoadingBarHttpClientModule,
    LoadingBarRouterModule,
    LoadingBarModule,
    NgxSpinnerModule,
    TableModule,
    DashboardModule,
    ImageViewerModule,
    MatBadgeModule,
    MatTooltipModule,
    CmsModule
  ],
  providers: [{ provide: HTTP_INTERCEPTORS, useClass: XhrInterceptorService, multi: true }],
  bootstrap: [AppComponent]
})
export class AppModule { }
