# Chat-app
Chat based in java

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

## Known issues
Sometimes connection to server doesn't stop looping (reestart server to solve this issue).  
Register panel returns a Frame with previous information (instead of new one).  
Lost connection message repeats itself.
Session frames don't close if changed between them (login to register || resgister to login)
## Design

## Next Steps
User/Group info 
Show Only saved friends/groups  
Block user  
User/Group photo  
Clean own chat  
Search user/group/text  
Edit message
Remember user
