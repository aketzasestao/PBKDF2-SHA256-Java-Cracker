# PBKDF2 SHA256 Cracker 🔐

Herramienta concurrente en Java para crackear contraseñas hasheadas con PBKDF2-SHA256.

## ¿Qué es PBKDF2-SHA256?

PBKDF2 (Password-Based Key Derivation Function 2) es un estándar de derivación de claves definido en el RFC 2898. Aplica una función pseudoaleatoria (en este caso HMAC-SHA256) repetida miles de veces para encarecer los ataques de fuerza bruta. Es ampliamente utilizado en:

- **Django** (por defecto usa `PBKDF2-HMAC-SHA256`)
- **Bibliotecas de contraseñas** utilizadas en Flask y otros frameworks
- **Servicios cloud** como AWS Cognito o GCP en ciertos flujos internos
- **Almacenamientos de credenciales** y herramientas modernas de gestión de secretos

Se considera **seguro y moderno** (RFC 2898) porque, a diferencia de MD5 o SHA1, el número de iteraciones lo hace computacionalmente costoso.

## Compilación

```bash
javac cracker.java WordlistReader.java PBKDF2Verifier.java
```

## Uso

```bash
java cracker <wordlist> <iterations> <salt> <targethash>
```

## Ejemplo

```bash
java cracker prueba.txt 600000 AMtzteQIG7yAbZIa 0673ad90a0b4afb19d662336f0fce3a9edd0b7b19193717be28ce4d66c887133
```

## Desglose del hash PBKDF2

Formato: `pbkdf2:sha256:<iterations>$<salt>$<hash>`

```
pbkdf2:sha256:600000$AMtzteQIG7yAbZIa$0673ad90a0b4afb19d662336f0fce3a9edd0b7b19193717be28ce4d66c887133
                 ↑              ↑                    ↑
           iterations        salt              targethash
```

- **Iterations**: 600000 (número de veces que se aplica la función)
- **Salt**: AMtzteQIG7yAbZIa (valor aleatorio para evitar rainbow tables)
- **Hash**: Valor resultante en hexadecimal
