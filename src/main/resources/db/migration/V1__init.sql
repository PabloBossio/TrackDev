-- ============================
-- Tablas base (sin dependencias)
-- ============================

CREATE TABLE perfil_desarrollador (
    id          BIGSERIAL PRIMARY KEY,
    nombre      VARCHAR(80)   NOT NULL UNIQUE,
    costo_hora  NUMERIC(10,2) NOT NULL CHECK (costo_hora > 0),
    activo      BOOLEAN       NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMP     NOT NULL DEFAULT now(),
    updated_at  TIMESTAMP     NOT NULL DEFAULT now()
);

CREATE TABLE cliente (
    id                BIGSERIAL PRIMARY KEY,
    razon_social      VARCHAR(150) NOT NULL,
    cuit              VARCHAR(20)  UNIQUE,
    contacto_nombre   VARCHAR(120),
    contacto_email    VARCHAR(150),
    contacto_telefono VARCHAR(30),
    activo            BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at        TIMESTAMP    NOT NULL DEFAULT now(),
    updated_at        TIMESTAMP    NOT NULL DEFAULT now()
);

-- ============================
-- Usuario (depende de perfil_desarrollador)
-- ============================

CREATE TABLE usuario (
    id             BIGSERIAL PRIMARY KEY,
    nombre         VARCHAR(80)  NOT NULL,
    apellido       VARCHAR(80)  NOT NULL,
    email          VARCHAR(150) NOT NULL UNIQUE,
    password_hash  VARCHAR(255) NOT NULL,
    rol            VARCHAR(20)  NOT NULL CHECK (rol IN ('ADMIN','OPERADOR')),
    perfil_id      BIGINT       REFERENCES perfil_desarrollador(id),
    activo         BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at     TIMESTAMP    NOT NULL DEFAULT now(),
    updated_at     TIMESTAMP    NOT NULL DEFAULT now(),
    CONSTRAINT chk_operador_requiere_perfil
        CHECK (rol <> 'OPERADOR' OR perfil_id IS NOT NULL)
);

CREATE INDEX idx_usuario_perfil_id ON usuario(perfil_id);

-- ============================
-- Proyecto (depende de cliente)
-- ============================

CREATE TABLE proyecto (
    id                  BIGSERIAL PRIMARY KEY,
    nombre              VARCHAR(150) NOT NULL,
    descripcion         TEXT,
    cliente_id          BIGINT       NOT NULL REFERENCES cliente(id),
    fecha_inicio        DATE         NOT NULL,
    fecha_fin_estimada  DATE,
    estado              VARCHAR(20)  NOT NULL DEFAULT 'PLANIFICADO'
                          CHECK (estado IN ('PLANIFICADO','EN_CURSO','FINALIZADO','CANCELADO')),
    presupuesto_venta   NUMERIC(12,2) CHECK (presupuesto_venta IS NULL OR presupuesto_venta > 0),
    created_at          TIMESTAMP    NOT NULL DEFAULT now(),
    updated_at          TIMESTAMP    NOT NULL DEFAULT now(),
    CONSTRAINT chk_fechas_proyecto
        CHECK (fecha_fin_estimada IS NULL OR fecha_fin_estimada >= fecha_inicio)
);

CREATE INDEX idx_proyecto_cliente_id ON proyecto(cliente_id);

-- ============================
-- Modulo (depende de proyecto)
-- ============================

CREATE TABLE modulo (
    id           BIGSERIAL PRIMARY KEY,
    proyecto_id  BIGINT       NOT NULL REFERENCES proyecto(id),
    nombre       VARCHAR(120) NOT NULL,
    descripcion  TEXT,
    created_at   TIMESTAMP    NOT NULL DEFAULT now(),
    updated_at   TIMESTAMP    NOT NULL DEFAULT now(),
    UNIQUE (proyecto_id, nombre)
);

CREATE INDEX idx_modulo_proyecto_id ON modulo(proyecto_id);

-- ============================
-- Tarea (depende de modulo)
-- ============================

CREATE TABLE tarea (
    id           BIGSERIAL PRIMARY KEY,
    modulo_id    BIGINT       NOT NULL REFERENCES modulo(id),
    nombre       VARCHAR(150) NOT NULL,
    descripcion  TEXT,
    estado       VARCHAR(20)  NOT NULL DEFAULT 'PENDIENTE'
                   CHECK (estado IN ('PENDIENTE','EN_CURSO','FINALIZADA')),
    created_at   TIMESTAMP    NOT NULL DEFAULT now(),
    updated_at   TIMESTAMP    NOT NULL DEFAULT now()
);

CREATE INDEX idx_tarea_modulo_id ON tarea(modulo_id);

-- ============================
-- Estimacion (depende de tarea, perfil_desarrollador, usuario)
-- ============================

CREATE TABLE estimacion (
    id                   BIGSERIAL PRIMARY KEY,
    tarea_id             BIGINT        NOT NULL UNIQUE REFERENCES tarea(id),
    perfil_estimado_id   BIGINT        NOT NULL REFERENCES perfil_desarrollador(id),
    horas_estimadas      NUMERIC(8,2)  NOT NULL CHECK (horas_estimadas > 0),
    costo_estimado       NUMERIC(12,2) NOT NULL CHECK (costo_estimado > 0),
    creado_por           BIGINT        NOT NULL REFERENCES usuario(id),
    created_at           TIMESTAMP     NOT NULL DEFAULT now(),
    updated_at           TIMESTAMP     NOT NULL DEFAULT now()
);

-- ============================
-- Registro de tiempo (depende de tarea, usuario)
-- ============================

CREATE TABLE registro_tiempo (
    id                    BIGSERIAL PRIMARY KEY,
    tarea_id              BIGINT        NOT NULL REFERENCES tarea(id),
    usuario_id            BIGINT        NOT NULL REFERENCES usuario(id),
    fecha                 DATE          NOT NULL,
    horas_trabajadas      NUMERIC(5,2)  NOT NULL CHECK (horas_trabajadas > 0 AND horas_trabajadas <= 24),
    costo_hora_aplicado   NUMERIC(10,2) NOT NULL CHECK (costo_hora_aplicado > 0),
    costo_real            NUMERIC(12,2) NOT NULL CHECK (costo_real > 0),
    descripcion           VARCHAR(300),
    created_at            TIMESTAMP     NOT NULL DEFAULT now(),
    updated_at            TIMESTAMP     NOT NULL DEFAULT now()
);

CREATE INDEX idx_registro_tiempo_tarea_id   ON registro_tiempo(tarea_id);
CREATE INDEX idx_registro_tiempo_usuario_id ON registro_tiempo(usuario_id);
CREATE INDEX idx_registro_tiempo_fecha      ON registro_tiempo(fecha);