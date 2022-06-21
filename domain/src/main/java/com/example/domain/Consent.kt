package com.example.domain

class Consents(val consentimiento_captura: Boolean = false,
              val consentimiento_voluntario: Boolean = false,
              val consentimiento_publicacion: Boolean = false,
              val consentimiento_administracion: Boolean = false,
              val consentimiento_acceso_correcion_eliminacion: Boolean = false) {
}