CREATE TABLE public.egpr_map_press_note
(
    map_press_note_uuid character varying(64) COLLATE pg_catalog."default" NOT NULL,
    press_master_uuid character varying(64) COLLATE pg_catalog."default",
    press_note_uuid character varying(64) COLLATE pg_catalog."default",
    notify_status boolean,
    module_code character varying(10) COLLATE pg_catalog."default",
    tenant_id character varying(256) COLLATE pg_catalog."default",
    is_active boolean,
    created_by character varying(64) COLLATE pg_catalog."default",
    created_time bigint,
    last_modified_by character varying(64) COLLATE pg_catalog."default",
    last_modified_time bigint,
    source_uuid character varying(64) COLLATE pg_catalog."default",
    CONSTRAINT egpr_map_press_note_pkey PRIMARY KEY (map_press_note_uuid)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;