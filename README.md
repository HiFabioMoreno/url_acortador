# Acortador URL ![Java](https://img.shields.io/badge/Java-22-orange?logo=openjdk) ![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.x-6DB33F?logo=springboot) ![Status](https://img.shields.io/badge/status-active-brightgreen)



Este proyecto expone una API que recibe una URL larga y devuelve un enlace corto basado en un slug único. Al acceder al enlace corto, el usuario es redirigido automáticamente a la URL original. Ideal para simplificar el compartir de enlaces en redes sociales, correos electrónicos o cualquier medio donde el espacio o la legibilidad importa.

---

### ✨ Características

- ✅ Acortar cualquier URL larga en un enlace corto y memorable
- ✅ Generación automática de slug
- ✅ Redirección automática al acceder al slug
- ✅ Validación de URLs mal formadas
- ✅ Respuestas en formato JSON

---

### 💡 Ejemplos
```
curl -X POST http://localhost:8080/api/shorten \
  -H "Content-Type: application/json" \
  -d '{"url": "https://www.google.com/search?q=spring+boot+url+shortener"}'
 Respuesta
{ "shortUrl": "http://localhost:8080/aB3xY", "slug": "aB3xY", ... }

Usar el enlace corto (redirige automáticamente)
curl -L http://localhost:8080/aB3xY 
