CREATE TABLE public.egpr_notification_template
(
    notification_template_uuid character varying(64) COLLATE pg_catalog."default" NOT NULL,
    email_content json,
    sms_content character varying(255) COLLATE pg_catalog."default",
    created_by character varying(64) COLLATE pg_catalog."default",
    created_time bigint,
    last_modified_by character varying(64) COLLATE pg_catalog."default",
    last_modified_time bigint,
    tenant_id character varying(256) COLLATE pg_catalog."default",
    template_type character varying(64) COLLATE pg_catalog."default",
    document_attachment json,
    template_mapped_uuid character varying(64) COLLATE pg_catalog."default",
    module_code character varying(10) COLLATE pg_catalog."default",
    source_uuid character varying(64) COLLATE pg_catalog."default",
    CONSTRAINT egpr_notification_template_pkey PRIMARY KEY (notification_template_uuid)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;