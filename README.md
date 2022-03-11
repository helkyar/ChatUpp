# Chat-app
Chat based in java
Nuestra apliación se basa en la interacción con otros usuarios mediante un chat.
Las personas que se conecten al chat podrán enviar mensajes de texto a otras 
personas conectadas. Pueden entrar en el chat aquellas personas que se han registrado
e iniciado sesión.

### Functionality
- Sistema de login y registro  
>  ->Autentificación  
>  ->Autorización (administrador/usuario)  
>  ->Cifrado de contraseña  
    
- Operaciones CRUD con base de datos  
>  ->Visualización de usuarios, incidencias y comentarios  
>  ->Crear usuarios, incidencias y comentarios  
>  ->Actualizar el estado de la incidencia y los comentarios  
>  ->Eliminar usuarios, incidencias y comentarios  
  
-Chat
> ->Conexión por Socket
> ->Historial de conversación (infinite scroll)
> ->Chat room
> ->Cam showcase

### Run
Has de tener instalado en el ordenador una versión de java superior a la 8.
Para poder conectarte a la aplicación uno de los usuarios ha de tener activado el servidor. 
Solo debe haber un servidor y cada cleinte solo puede ejecutar una aplicación de chat. 
Quien ejecute el servidor ha de tener instalado un gestor de base de datos como MySQL. No es 
necesario tener el programa de netbeans ya que se ejecutará a través de un archivo server.jar. 
Una vez el servidor está activo, los usuarios se pueden conectar al chat, ejecutando
la aplicación de chat con el archivo chat.jar. Cuando ejecutas el chat, puedes entrar si 
tienes un usuario registrado o, en caso contrario, registrarte. Puedes hablar con todos los
usuarios que estén conectados. Si creas un grupo de usuarios, puedes enviar mensajes
a todos esos usuarios. Si alguno no está conectado en el momento, cuando se conecte
podrá ver el mensaje. 
 
## Known issues
Sometimes connection to server doesn't stop looping -> (reestart server to solve this issue).  
Register panel returns a Frame with previous information (instead of new one).  
Lost connection message repeats itself.  
Session frames don't close if changed between them (login to register || resgister to login)  
2 CHATS AT THE SAME TIME BREAKS THINGS -> Use just one  
TOGGLE TOGGLE-BUTTONS  
CHATS REPEAT IF THERE IS POST-LOGIN  
BOTHS USER CHATS (FROM MEMORY AND ONLINE) PERSIST  

## Design
La aplicación chat al ser ejecutada una ventana de información con el proceso de busqueda de sefvidor. 
Una vez encontrado, muestra las opciones de inicio. Hay un botón de 
login, de registro o de entrar como invitado en el chat. Al darle al botón de login
se habre un panel de login. Hay un campo en el que ingresas el usuario y otro en 
el que ingresas la contraseña. Una vez rellenados los campos, hay un botón de login que te 
lleva a acceder al chat si los datos son correctos.Si tienes que registrarte, hay un botón 
en este panel al que le das y te muestra un panel de registro. Los campos de registro
son: usuario, contraseña, confirmar contraseña, nombre real, apellidos, nickname,
género e imagen. Con el botón de registro, te registras una vez rellenados los datos correctamente.
Una vez registrado, puedes hacer click en "iniciar sesión".

Una vez te logueas, en la ventana principal se muestran los usuarios que están conectados.
Puedes seleccionar al usuario con quien deseas hablar o crear un grupo y añadir en él 
los usuarios que te interesen. Importante, al crear un grupo, debes añadirte a ti mismo.
 Una vez creado, al seleccionar el grupo, puedes añadir 
más miembros o eliminarlos. Hay dos botones superiores, una almoadilla y una x. Al escribir 
puedes borrar con la almoadilla y con la x puedes salir de la aplicación.


## Next Steps
User/Group info 
Show Only saved friends/groups    
Add usera as a friend  
Set user visibility (public/private)  
User configuration JPanel  
Show if user online  
Put updated chat at the top  
Block user  
User/Group photo  
Search user/group/text  
Edit message  
Remember user  
