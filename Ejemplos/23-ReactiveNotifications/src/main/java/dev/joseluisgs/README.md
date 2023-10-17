Ambas opciones, Flux con share() y ConnectableFlux, son adecuadas para un sistema de notificaciones que te avise de
cambios. La elección entre las dos depende de tus necesidades específicas.

Si necesitas que las notificaciones comiencen a emitirse inmediatamente después de que se produzcan cambios,
independientemente de si hay suscriptores o no, puedes utilizar ConnectableFlux. Sin embargo, ten en cuenta que si no
hay suscriptores en el momento en que se emite una notificación, esa notificación se perderá.

Por otro lado, si prefieres que las notificaciones solo se emitan cuando hay al menos un suscriptor, puedes utilizar
Flux con share(). Esto puede ser útil si quieres evitar el procesamiento innecesario de notificaciones cuando no hay
suscriptores.

En general, Flux con share() es probablemente la opción más segura, ya que garantiza que las notificaciones no se
pierdan si no hay suscriptores en el momento en que se producen los cambios. Sin embargo, si tienes requisitos
específicos que hacen que ConnectableFlux sea más adecuado, entonces deberías usar ConnectableFlux.

Además, si necesitas que las notificaciones se almacenen hasta que un suscriptor esté listo para procesarlas, puedes
considerar el uso de Flux con replay(). Esto creará un ConnectableFlux que almacena y retransmite las notificaciones a
los suscriptores, incluso si las notificaciones se emitieron antes de que el suscriptor se conectara.