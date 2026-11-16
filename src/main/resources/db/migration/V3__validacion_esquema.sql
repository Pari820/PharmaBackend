-- Sesion 14 - V3: Validacion no destructiva del esquema existente.
-- La V1 se registro como baseline y la V2 ya esta aplicada.
-- Comprueba las cuatro tablas de negocio antes de la migracion V4.
DECLARE
    v_tablas NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_tablas
      FROM user_tables
     WHERE table_name IN ('CATEGORIAS', 'PRODUCTOS', 'CLIENTES', 'VENTAS');

    IF v_tablas <> 4 THEN
        RAISE_APPLICATION_ERROR(-20014,
            'V3: Faltan tablas de negocio (CATEGORIAS, PRODUCTOS, CLIENTES, VENTAS). Revisar esquema antes de V4.');
    END IF;
END;
/
