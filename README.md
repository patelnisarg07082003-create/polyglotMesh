# PolyglotMesh – Multi-Language Code Execution Engine

PolyglotMesh is a Java-based code execution engine that provides a unified way to execute code written in multiple programming languages.

Currently supported languages:

* Java
* JavaScript
* Python

## 🚀 Features

* Multi-language code execution
* Common `LanguageExecutor` interface
* Language-specific executors
* `ExecutorFactory` for selecting the appropriate executor
* REST API support for code execution
* Standardized execution result
* Error handling
* Execution timeout handling
* Unit/integration test classes

## 🏗️ Architecture

The project follows a modular executor-based architecture:

```text
Client
  |
  v
ExecutionController
  |
  v
ExecutionService
  |
  v
ExecutorFactory
  |
  +----> JavaExecutor
  |
  +----> JavaScriptExecutor
  |
  +----> PythonExecutor
```

Each language executor implements the common:

```text
LanguageExecutor
```

interface.

The execution response is standardized through:

```text
ExecutionResult
```

## 📁 Project Structure

```text
src/main/java/
├── HelloPolyglot.java
└── com/polyglotmesh/engine/
    ├── ExecutionController.java
    ├── ExecutionRequest.java
    ├── ExecutionResult.java
    ├── ExecutionService.java
    ├── ExecutorFactory.java
    ├── LanguageExecutor.java
    ├── JavaExecutor.java
    ├── JavaScriptExecutor.java
    ├── PythonExecutor.java
    ├── PolyglotMeshApplication.java
    └── test classes
```

## 🛠️ Technologies

* Java
* Spring Boot
* Maven
* REST API
* JavaScript
* Python
* Git & GitHub

## ▶️ Running the Project

### 1. Clone the repository

```bash
git clone <repository-url>
```

### 2. Navigate to the project

```bash
cd PolyglotMesh-Execution
```

### 3. Build the project

```bash
mvn clean install
```

### 4. Run the application

```bash
mvn spring-boot:run
```

## 🧪 Testing

The project contains test classes for validating execution functionality.

Examples include:

* Java execution testing
* Multi-language execution testing
* Execution result validation

## 🎯 Project Objective

The main objective of PolyglotMesh is to provide a common execution layer for different programming languages through a clean and extensible architecture.

New languages can be added by implementing the `LanguageExecutor` interface and registering the executor through the factory.

## 🔮 Future Improvements

* Docker-based isolated execution
* Additional programming languages
* Better sandboxing and security
* Resource and memory limits
* Authentication and authorization
* Execution history
* Improved API documentation
* Containerized deployment

## 👨‍💻 Project

**PolyglotMesh – Multi-Language Code Execution Engine**

