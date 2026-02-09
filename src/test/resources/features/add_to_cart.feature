Feature: Agregar producto al carrito y checkout básico (Sauce Demo)

  Scenario: Agregar un producto al carrito y validar nombre, cantidad y precio
    Given que abro la tienda en "https://www.saucedemo.com"
    And inicio sesión con usuario "standard_user" y contraseña "secret_sauce"
    When selecciono el producto "Sauce Labs Backpack"
    And lo agrego al carrito
    And voy al carrito
    Then el carrito debe contener el producto "Sauce Labs Backpack" con cantidad "1"
    And el precio del producto debe ser visible y mayor que "0"
