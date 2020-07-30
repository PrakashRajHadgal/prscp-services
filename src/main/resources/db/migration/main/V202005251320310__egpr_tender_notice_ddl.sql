CREATE TABLE public.egpr_tender_notice
(
    tender_notice_uuid character varying(64) COLLATE pg_catalog."default" NOT NULL,
    tender_subject character varying(255) COLLATE pg_catalog."default",
    tender_date timestamp without time zone,
    file_number character varying(64) COLLATE pg_catalog."default",
    notification_template_uuid character varying(64) COLLATE pg_catalog."default",
    publication_size character varying(50) COLLATE pg_catalog."default",
    tender_notice_status character varying(64) COLLATE pg_catalog."default",
    is_active boolean,
    created_by character varying(64) COLLATE pg_catalog."default",
    created_time bigint,
    last_modified_by character varying(64) COLLATE pg_catalog."default",
    last_modified_time bigint,
    tenant_id character varying(256) COLLATE pg_catalog."default",
    note_content text COLLATE pg_catalog."default",
    module_code character varying(10) COLLATE pg_catalog."default",
    tender_notice_id character varying(64) COLLATE pg_catalog."default",
    tender_document json,
    source_uuid character varying(64) COLLATE pg_catalog."default",
    CONSTRAINT egpr_tender_notice_pkey PRIMARY KEY (tender_notice_uuid),
    CONSTRAINT fk_notification_template_uuid FOREIGN KEY (notification_template_uuid)
        REFERENCES public.egpr_notification_template (notification_template_uuid) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;
