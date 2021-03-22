[33mcommit 00ab1410620c761176822b26d18bb3c974dfd96f[m[33m ([m[1;36mHEAD -> [m[1;32mmain[m[33m, [m[1;31morigin/main[m[33m, [m[1;31morigin/HEAD[m[33m)[m
Author: = <jaime.polidura@gmail.com>
Date:   Tue Jan 26 12:31:52 2021 +0100

    Añadido los enums tipoactivo y tipoposicion a los objetos de la base de datos. Se ha mejorado la clase Scoreboard manager

[33mcommit e11dd95c59aea76724796b7d2d9d47a08d907f5d[m
Author: = <jaime.polidura@gmail.com>
Date:   Mon Jan 25 20:12:16 2021 +0100

    Improved mysql table classes. Added lombok to the project

[33mcommit a80249ee714a5e212fe54d77cdda65c3c5843060[m
Author: = <jaime.polidura@gmail.com>
Date:   Sun Jan 24 21:41:42 2021 +0100

    Se ha hecho mas limpio el codigo de los menus. Se ha añadido la clases de utilizades ItemBuilder

[33mcommit e61cd8734eb5cd17c5c9093ad0000ba71c8a73df[m
Author: = <jaime.polidura@gmail.com>
Date:   Sat Jan 23 23:01:40 2021 +0100

    Mejorado el commandmanager

[33mcommit a87706eefa6345068ea63fdc4864c4f6b5a0b21d[m
Author: = <jaime.polidura@gmail.com>
Date:   Sat Jan 16 22:59:06 2021 +0100

    Acabado la implementacion de la libreria de las validaciones. Acabado con los subcomandos de empresas

[33mcommit 3696da6ffb69207ef3a93a6b3dc46445f7a924ae[m
Author: = <jaime.polidura@gmail.com>
Date:   Fri Jan 15 23:31:11 2021 +0100

    Añadido las validaciones en los comandos de deudas y empleados

[33mcommit 208d5f09ce25dd6f18b5a4b0819f5f2ce23df766[m
Author: = <jaime.polidura@gmail.com>
Date:   Mon Jan 11 21:28:15 2021 +0100

    Implementada la libreria de validacines en comandos y bolsa

[33mcommit f3187b2a55ef48498a34634c1dd5b74264cc6d28[m
Author: = <jaime.polidura@gmail.com>
Date:   Thu Dec 31 14:03:02 2020 +0100

    Every request have now a class

[33mcommit 661be5e977033b4b2058f0a5061cfc8301c8af9c[m
Author: = <jaime.polidura@gmail.com>
Date:   Wed Dec 30 12:55:07 2020 +0100

    Now the disconexion of the player and the user of the website are handled. The whole socket project need to be sorted up

[33mcommit 496226c6c1291e353b9e6ceef6ee4f2c9ee6936d[m
Author: = <jaime.polidura@gmail.com>
Date:   Tue Dec 29 22:23:35 2020 +0100

    Ahora el jugador puede enviar mensajes al usuraio de la web. Falta de enviar un mensaje al frontend cuando el jugador del servidor cierre el chat (desconectarse, le habla otra persona)

[33mcommit 3417baa79571bff1169443dd8cc12794cf171f10[m
Author: = <jaime.polidura@gmail.com>
Date:   Mon Dec 28 18:52:54 2020 +0100

    Añadido el consumidor de rabbitmq ahora hay que hacer que el jugador pueda hablar con el frontend

[33mcommit 3dcbb77be6ceb3c217b6d878f719db7a85bc2cb3[m
Author: = <jaime.polidura@gmail.com>
Date:   Thu Dec 24 13:31:14 2020 +0100

    Se ha implementado el mensaje del socket de isOnline. Hay que testarlo mas veces

[33mcommit 12b1e3b7bde496d4234a2c10c475998ec40f02d4[m
Author: = <jaime.polidura@gmail.com>
Date:   Wed Dec 23 23:41:33 2020 +0100

    He añadido el socket cliente/servidor. Solo testa implemetnado el mensaje de sendnumber

[33mcommit 4490fa354b338c6b2cf168954a4ac2a0327e6552[m[33m ([m[1;32mchangeDataBase[m[33m)[m
Author: = <jaime.polidura@gmail.com>
Date:   Sat Dec 19 12:00:53 2020 +0100

    Se ha modificado el plugin acorde a las relaciones de las tablas de las bases de datos

[33mcommit 7dbb56856363bd0911cb9e52eecde77364987f90[m[33m ([m[1;31morigin/changeDataBase[m[33m)[m
Author: = <jaime.polidura@gmail.com>
Date:   Sat Dec 19 10:54:09 2020 +0100

    Ahora ya funciona el plugin con el nuevo formato de la base de datos

[33mcommit 8ac4c92230da43296d48afd01c95b9556ec35835[m
Author: = <jaime.polidura@gmail.com>
Date:   Sat Dec 19 00:01:10 2020 +0100

    Todos los value objects de la base de datos se han cambiado. Se ha cambiado tambien las clases mysql

[33mcommit a632ea5144d735bc0073fd7e003a564c81106a90[m
Author: = <jaime.polidura@gmail.com>
Date:   Sat Dec 19 00:00:55 2020 +0100

    Todos los value objects de la base de datos se han cambiado. Se ha cambiado tambien las clases mysql

[33mcommit 5e48a1a6cb76e0ca62b88a82f446b6de39ab702e[m
Author: = <jaime.polidura@gmail.com>
Date:   Thu Dec 17 13:58:13 2020 +0100

    Añadido la parte de la base de datos VerificacionCuentas

[33mcommit 5cf6005e288670587f9493c6b3eed2811243cc56[m
Author: = <jaime.polidura@gmail.com>
Date:   Fri Dec 11 11:19:25 2020 +0100

    Added eventsBDListener. It needs to be tested

[33mcommit 8c4f1ce5bc8616dcfb1f748824da6600e4d0c375[m
Merge: bc65b53 6335de7
Author: = <jaime.polidura@gmail.com>
Date:   Thu Dec 3 18:40:13 2020 +0100

    Improved getPosicionesAbiertasJugador in PosicionesAbiertas.java. Also improved bolsa cartera

[33mcommit bc65b5390d2f65276394b16c78a662770322b59a[m
Author: = <jaime.polidura@gmail.com>
Date:   Thu Dec 3 18:03:30 2020 +0100

    remove MySQL.conectart() from crearMapaTopPatrimonioPlayers in Funciones

[33mcommit 6335de77bf0f026ad9547f894a51eddb4c7f5b7b[m[33m ([m[1;32mlib[m[33m)[m
Author: = <jaime.polidura@gmail.com>
Date:   Thu Dec 3 17:56:12 2020 +0100

    ignore this commit

[33mcommit a7c988f83b1384c7873fb65cbd1fb55a676c5b1b[m[33m ([m[1;31morigin/lib[m[33m)[m
Author: = <jaime.polidura@gmail.com>
Date:   Sat Nov 28 19:32:40 2020 +0100

    Fixed getTransaccionesPagaEmpresa in Transacciones.java

[33mcommit 769b589abefacba2efb1258e05566cc2ba32507b[m
Author: = <jaime.polidura@gmail.com>
Date:   Sat Nov 28 19:00:44 2020 +0100

    Added getTransaccionesPagaEmpresa in Transacciones.java

[33mcommit b9eb5ef40457f690005ff6c0b61cc1e89aecdb5a[m
Author: = <jaime.polidura@gmail.com>
Date:   Wed Nov 25 17:48:14 2020 +0100

    Added getAllEmpleadosEmpresas method in Empleados

[33mcommit 82eeb74bb6fffff8cbd3d45c63a2eee72fa40136[m
Author: = <jaime.polidura@gmail.com>
Date:   Sun Nov 22 16:37:38 2020 +0100

    Improvemetn of the method getAllPosicionesAbiertasMap in PosicionesAbiertas.ajva

[33mcommit 2e3cbf7bc0a5df580762f82d56587f37d09e543a[m
Author: = <jaime.polidura@gmail.com>
Date:   Tue Nov 24 13:24:43 2020 +0100

    Improvmetn of the method getAllPosicionesAbiertas in PosicionesAbiertas.java

[33mcommit 21e9a9201cbdeb80a97f48e970be98036dda2707[m
Author: = <jaime.polidura@gmail.com>
Date:   Sun Nov 22 16:37:38 2020 +0100

    En la clase mensajes he hecho publico el metodo de borrarmensajes. Antes era publico pero con el problema de las ramas que he tenido se ha borrado ese cambio

[33mcommit 260dc52e6d6d1b85e410f443d94be482cc8ab433[m
Author: = <jaime.polidura@gmail.com>
Date:   Sun Nov 22 16:24:49 2020 +0100

    Añadido el MySQL.conectar() en el metodo crear mapPatrimonioPlayers en Funciones

[33mcommit 207af0214c7ba0351a10b4bf5f50db93bbd22569[m
Author: = <jaime.polidura@gmail.com>
Date:   Sun Nov 22 16:23:36 2020 +0100

    Añadido el MySQL.conectar() en el metodo crear mapPatrimonioPlayers en Funciones

[33mcommit 0a0647b381589812650c0ca83f9aa443e614c043[m
Author: = <jaime.polidura@gmail.com>
Date:   Sun Nov 22 16:19:59 2020 +0100

    Añadido una nueva rama para utilzarla en la web ya q en la otra que tenia no me funcionaba

[33mcommit a2cef61058acc6f5d21b6d4adda1cfe4a82be588[m
Author: = <jaime.polidura@gmail.com>
Date:   Sun Nov 22 16:07:46 2020 +0100

    Mejorado el metodo para sacar un mapa de todos los jugadores con su patrimonio. Ahora hace menos peticionea a la base de datos

[33mcommit 7027e7b0ee8649f4585809d87c683403881d434b[m
Author: = <jaime.polidura@gmail.com>
Date:   Sun Nov 15 18:39:14 2020 +0100

    Modificacion del enviador añadido

[33mcommit 9109609014ad20d27107b284ba6481a5153fd28a[m
Author: Jaime <jaime.polidura@gmail.com>
Date:   Mon Nov 2 20:30:17 2020 +0100

    Añadido el proyecto

[33mcommit ac43d29f1feb52bfd87f07b81de2be5f034bd477[m
Author: JaimeTruman <73758994+JaimeTruman@users.noreply.github.com>
Date:   Mon Nov 2 20:26:56 2020 +0100

    Initial commit
