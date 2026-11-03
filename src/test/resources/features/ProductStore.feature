# language: es
Característica: Product - Store
  Como usuario de la tienda
  Quiero autenticarme y consultar productos
  Para confirmar que la tienda, autenticación y catálogo están disponibles

  @storeRegression @authentication
  Esquema del escenario: Validación del servicio de autenticación
    Dado estoy en la página de la tienda
    Y me logueo con mi usuario "<usuario>" y clave "<clave>"
    Entonces valido el resultado de autenticación "<resultado>"

    @requiresValidCredentials
    Ejemplos: acceso válido
      | usuario             | clave                 | resultado |
      | JMFLORESQUISPE2025@gmail.com | Dios@789456123/* | success   |
    @negative
    Ejemplos: acceso rechazado
      | usuario          | clave         | resultado |
      | JMFLORESQUISPE20@gmail.com | 123456 | rejected  |

  @storeRegression @purchase
  Escenario: Validación del precio de un producto
    Dado estoy en la página de la tienda
    Y me logueo con mi usuario "JMFLORESQUISPE2025@gmail.com" y clave "Dios@789456123/*"
    Cuando navego a la categoria "Clothes" y subcategoria "Men"
    Y agrego 2 unidades del primer producto al carrito
    Entonces valido en el popup la confirmación del producto agregado
    Y valido en el popup que el monto total sea calculado correctamente
    Cuando finalizo la compra
    Entonces valido el titulo de la pagina del carrito
    Y vuelvo a validar el calculo de precios en el carrito

  @storeRegression @category
  Escenario: Detener el flujo al buscar una categoría inexistente
    Dado estoy en la página de la tienda
    Y me logueo con mi usuario "JMFLORESQUISPE2025@gmail.com" y clave "Dios@789456123/*"
    Cuando navego a la categoria "Autos" y subcategoria ""
    Entonces valido que la categoria no existe y que la navegación se detuvo
