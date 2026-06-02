# 🎬 Cine El Master
## Sistema de Compra y Pago de Películas

<div align="center">

![Java](https://img.shields.io/badge/Java-17+-orange)
![JavaFX](https://img.shields.io/badge/JavaFX-GUI-blue)
![MySQL](https://img.shields.io/badge/Base%20de%20Datos-MySQL-green)
![Arquitectura](https://img.shields.io/badge/Arquitectura-MVC-purple)
![Estado](https://img.shields.io/badge/Estado-Funcional-success)

</div>

---

# 📖 Descripción

**Cine El Master** es una aplicación de escritorio desarrollada en **JavaFX** que permite gestionar el proceso de compra de películas dentro de un sistema de cine.

La aplicación permite:

- Registrar compradores.
- Consultar un catálogo de películas almacenado en base de datos.
- Agregar películas al carrito de compra.
- Modificar cantidades.
- Eliminar productos.
- Calcular el valor total de la compra.
- Registrar pagos.
- Calcular cambio (vueltas).
- Persistir la compra en base de datos.

La solución fue desarrollada siguiendo una arquitectura por capas basada en:

- Dominio
- Negocio
- Persistencia
- Presentación

---

# 🎯 Objetivo General

Desarrollar una aplicación de escritorio para la gestión de compras de películas que permita registrar clientes, administrar carritos de compra y procesar pagos de forma sencilla e intuitiva.

---

# 🎯 Objetivos Específicos

- Gestionar compradores.
- Consultar el catálogo de películas.
- Agregar productos a una compra.
- Modificar cantidades de productos.
- Eliminar productos del carrito.
- Calcular automáticamente los totales.
- Procesar pagos y calcular vueltas.
- Almacenar la información en una base de datos.
- Implementar interfaces gráficas usando JavaFX.

---

# 🏗 Arquitectura del Sistema

```text
┌─────────────────────────────┐
│        JavaFX GUI           │
│  (FXML + Controller)        │
└──────────────┬──────────────┘
               │
               ▼
┌─────────────────────────────┐
│      Capa de Negocio        │
│      NegocioComprar         │
└──────────────┬──────────────┘
               │
               ▼
┌─────────────────────────────┐
│    IntegradorFileSystem     │
│    Persistencia JDBC        │
└──────────────┬──────────────┘
               │
               ▼
┌─────────────────────────────┐
│         Base Datos          │
│      Compras / Películas    │
└─────────────────────────────┘
