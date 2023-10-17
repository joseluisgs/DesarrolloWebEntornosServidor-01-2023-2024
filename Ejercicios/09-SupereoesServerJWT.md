Vamos a tener en nuestro servidor un repositorio de héroes como en el de ejercicios anteriores.

Por otro lado tendremos un repositorio de usuarios, con username y password en Bcrypt con salt 10 y un rol, que puede ser ADMIN o USER.

Además, tendremos un servicio de token que generará tokens con un tiempo de vida de 20s y una contraseña de "SuperHeroesCodersDAW"

El cliente se conectara por el puerto 3000, mandando su paquete de Login. Si es correcto recibirá un token.

Con ese token podrá realizar la petición saber nombre de super heroes mandando la identidad secreta. Solo si es ADMIN recibirá los datos completos del superhéroe.