CREATE TABLE public.egpr_press_master
(
    press_master_uuid character varying(64) COLLATE pg_catalog."default" NOT NULL,
    personnel_name character varying(255) COLLATE pg_catalog."default",
    press_type character varying(64) COLLATE pg_catalog."default",
    publication_name character varying(255) COLLATE pg_catalog."default",
    email character varying(255) COLLATE pg_catalog."default",
    mobile character varying(50) COLLATE pg_catalog."default",
    is_active boolean,
    created_by character varying(64) COLLATE pg_catalog."default",
    created_time bigint,
    last_modified_by character varying(64) COLLATE pg_catalog."default",
    last_modified_time bigint,
    tenant_id character varying(256) COLLATE pg_catalog."default",
    module_code character varying(10) COLLATE pg_catalog."default",
    source_uuid character varying(64) COLLATE pg_catalog."default",
    CONSTRAINT egpr_press_master_pkey PRIMARY KEY (press_master_uuid)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;
