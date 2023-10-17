# Prueba Java

Imaginad que tenemos que implementar una cola de procesos por prioridad circular, es decir un Round Robin.

Tenemos 9 niveles de prioridad. El nivel 0 es el más prioritario y el 8 el menos prioritario.

Un proceso tiene:

- PID: Es un uuid
- Nombre: PX (donde x es el número del proceso)
- Tiempo de Creación
- Tiempo de última modificación
- Quantum: Tiempo que puede estar ejecutándose el proceso en la CPU entre 1 y 100.
- Prioridad: Entre 0 y 8.

Ademas,

- Crear una cola de 50 procesos aleatorios y echarla a andar hasta que se termine todos.
- Testear todas las operaciones que hagas.
