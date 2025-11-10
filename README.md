# Biblioteca - Sistema de Gestión de Biblioteca

Sistema de gestión de biblioteca desarrollado en Java con interfaz gráfica Swing y base de datos MySQL.

## 📋 Descripción

Aplicación de escritorio para administrar una biblioteca que permite gestionar:
- **Usuarios**: Registro e inicio de sesión con roles (Administrador, Desarrollador, Usuario)
- **Libros**: CRUD completo de libros con título, autor, categoría, año y estado
- **Autores**: CRUD Gestión de autores con nombre y nacionalidad
- **Categorías**: CRUD Organización de libros por categorías

## 🚀 Tecnologías

- **Java 21** - Lenguaje de programación
- **Swing** - Interfaz gráfica de usuario
- **MySQL** - Base de datos relacional
- **BCrypt** - Encriptación de contraseñas
- **JDBC** - Conexión con base de datos

## 📦 Dependencias

Las siguientes librerías están incluidas en `lib/`:
- `mysql-connector-j-8.0.33.jar` - Conector MySQL
- `jbcrypt-0.4.jar` - Encriptación de contraseñas

## ⚙️ Configuración

### 1. Base de Datos

Ejecutar los scripts SQL en orden:

```bash
# Crear base de datos y tablas
mysql -u root -p < mysql/mysql.txt

# Insertar datos iniciales (opcional)
mysql -u root -p < mysql/Inserts.txt
```

### 2. Configuración de Conexión

Editar la clase `mysqlConexiones` con tus credenciales:

```java
private static final String URL = "jdbc:mysql://localhost:3306/Biblioteca";
private static final String USER = "tu_usuario";
private static final String PASSWORD = "tu_contraseña";
```

## 🔧 Configuración de Credenciales de Base de Datos

Antes de ejecutar la aplicación, debes configurar las credenciales de tu base de datos MySQL:

1. Abre el archivo [`src/Recursos/mysqlConexiones.java`](src/Recursos/mysqlConexiones.java)
2. Reemplaza `USUARIO` y `CLAVE` con tus credenciales de MySQL:

```java
private static final String user = "USUARIO";
private static final String pass = "CLAVE";
```

**Ejemplo:**
```java
private static final String user = "root";
private static final String pass = "micontraseña123";
```

## 🏃 Ejecución

### Desde línea de comandos

```bash
# Ir a la carpeta dist
cd dist

# Ejecutar la aplicación
java -jar "Biblioteca.jar"
```

### Desde NetBeans
1. Abrir el proyecto en NetBeans
2. Click derecho en el proyecto → Run

## 👤 Usuario por Defecto

Después de ejecutar los scripts SQL, puedes usar:
- **Usuario**: USER
- **Contraseña**: (definida en el script de inserts)

## 📁 Estructura del Proyecto

```
Biblioteca/
├── src/
│   ├── Login/          # Módulo de autenticación
│   ├── MenuPrincipal/  # Menú principal y gestión
│   ├── Recursos/       # Utilidades y conexión DB
│   └── Start/          # Punto de entrada
├── librerias/          # Dependencias JAR
├── mysql/              # Scripts de base de datos
└── build.xml           # Configuración Ant
```

## 🔑 Características Principales

- Sistema de autenticación con hash de contraseñas
- CRUD completo para libros, autores y categorías
- Gestión de perfiles de usuario con foto
- Interfaz gráfica intuitiva con GridBagLayout
- Roles de usuario con diferentes permisos

