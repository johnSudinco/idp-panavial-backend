# Funcionamiento de Facturas y Saldos

Este documento describe el flujo de autenticación, autorización y consumo de los sistemas de **Facturas** y **Saldos**, así como las consideraciones de seguridad y rendimiento aplicadas en la arquitectura.

---

## 1. Autenticación y Registro de Usuarios

**Base de datos de autenticación**  
postgresql://localhost:5433/idp-panavial

El sistema cuenta con un **servicio de autenticación independiente**, el cual se conecta a una base de datos PostgreSQL fuera del entorno del Data Warehouse (DWH).

### Flujo de autenticación
1. El usuario accede al sistema de **Login**.
2. El sistema valida:
   - **Usuario registrado**: se autentica correctamente.
   - **Usuario no registrado**: debe completar el proceso de registro.
3. Una vez autenticado, el sistema genera un **JWT (JSON Web Token)** que contiene:
   - Identificador único del usuario (`userId`).
   - Número de identificación del usuario (documento / RUC).
   - Rol asignado (`USER` o `ADMIN`).
4. El token permite al usuario mantener una sesión válida y es utilizado para autorizar el acceso a los sistemas de Facturas y Saldos.

---

## 2. Consumo del Sistema de Facturas

El sistema de **Facturas** se encuentra desacoplado del sistema de autenticación y valida el acceso **exclusivamente mediante el JWT**.

**Base de datos (DWH – PostgreSQL)**  
postgresql://192.168.89.34:5432/sudinco

### Comportamiento según rol

#### Rol ADMIN
- Puede consultar **todas las facturas** del sistema.
- Las consultas deben realizarse **obligatoriamente con filtros de fecha**.
- Las respuestas están **limitadas y/o paginadas**, debido al alto volumen de información almacenada en el DWH.

#### Rol USER
- Solo puede consultar las facturas **asociadas a su propio número de identificación**.
- El RUC/documento se obtiene directamente desde el **token JWT**.
- El frontend **no envía el RUC**, evitando manipulación de datos.
- Puede aplicar filtros de fecha de forma opcional.

---

## 3. Consumo del Sistema de Saldos

El sistema de **Saldos** accede a información proveniente del **Data Warehouse (DWH)** y opera bajo las mismas reglas de seguridad y autorización.

**Base de datos (DWH – MySQL)**  
mysql://192.168.61.9:3306/consultaWeb

### Comportamiento según rol

#### Rol ADMIN
- Puede consultar **todos los saldos** dentro de un **rango de fechas obligatorio y controlado**.
- Las consultas están optimizadas mediante:
  - Límites estrictos de resultados.
  - **Paginación por cursor (keyset pagination)**, evitando el uso de `OFFSET`.
- Este enfoque garantiza un acceso controlado sin afectar el rendimiento del DWH.

#### Rol USER
- Solo puede consultar los saldos **relacionados a su propio documento/RUC**.
- El documento se obtiene desde el **token JWT**, garantizando seguridad y consistencia.
- Los filtros de fecha son opcionales.

---

## 4. Consideraciones de Seguridad y Rendimiento

- El **RUC / número de identificación nunca es enviado desde el frontend**.
- Toda la autorización se basa en los **claims del JWT**.
- Las consultas de tipo **ADMIN** están:
  - Limitadas por rango de fechas.
  - Limitadas en cantidad de registros.
  - Diseñadas para evitar consultas abiertas sobre el DWH.
- Los sistemas de **Login, Facturas y Saldos están desacoplados**, permitiendo:
  - Escalabilidad.
  - Mantenimiento independiente.
  - Mejor control de seguridad y rendimiento.

---

## 5. Beneficios del Enfoque

- Seguridad reforzada (no exposición de identificadores sensibles).
- Separación clara de responsabilidades entre sistemas.
- Alto rendimiento incluso con grandes volúmenes de datos.
- Arquitectura preparada para crecimiento, auditoría y monitoreo.
- Cumplimiento de buenas prácticas para sistemas basados en Data Warehouse (DWH).

---
