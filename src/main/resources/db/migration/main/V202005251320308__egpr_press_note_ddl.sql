CREATE TABLE public.egpr_press_note
(
    press_note_uuid character varying(64) COLLATE pg_catalog."default" NOT NULL,
    press_note_subject character varying(255) COLLATE pg_catalog."default",
    press_note_date timestamp without time zone,
    file_number character varying(64) COLLATE pg_catalog."default",
    notification_template_uuid character varying(64) COLLATE pg_catalog."default",
    is_active boolean,
    created_by character varying(64) COLLATE pg_catalog."default",
    created_time bigint,
    last_modified_by character varying(64) COLLATE pg_catalog."default",
    last_modified_time bigint,
    tenant_id character varying(256) COLLATE pg_catalog."default",
    note_content text COLLATE pg_catalog."default",
    module_code character varying(10) COLLATE pg_catalog."default",
    source_uuid character varying(64) COLLATE pg_catalog."default",
    CONSTRAINT egpr_press_note_pkey PRIMARY KEY (press_note_uuid),
    CONSTRAINT "FK_notification_template_uuid" FOREIGN KEY (notification_template_uuid)
        REFERENCES public.egpr_notification_template (notification_template_uuid) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;