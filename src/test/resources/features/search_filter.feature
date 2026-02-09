Feature: Búsqueda y filtrado en Sauce Demo
  Scenario: Buscar y filtrar productos por texto y orden
    Given que abro la tienda en "https://www.saucedemo.com"
    And inicio sesión con usuario "standard_user" y contraseña "secret_sauce"
    When busco por texto "bike"
    Then debo ver al menos "1" resultado
    When aplico filtro por orden "Name (A to Z)"
    Then todos los resultados deben contener el texto "bike"