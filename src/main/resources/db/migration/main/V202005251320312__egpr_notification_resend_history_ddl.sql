CREATE TABLE public.egpr_notification_resend_history
(
    notification_resend_uuid character varying(64) COLLATE pg_catalog."default" NOT NULL,
    module_code character varying(10) COLLATE pg_catalog."default",
    notification_mapped_uuid character varying(64) COLLATE pg_catalog."default",
    module_name character varying(64) COLLATE pg_catalog."default",
    receiver_uuid character varying(64) COLLATE pg_catalog."default",
    receiver_name character varying(255) COLLATE pg_catalog."default",
    receiver_email character varying(255) COLLATE pg_catalog."default",
    receiver_mobile character varying(50) COLLATE pg_catalog."default",
    tenant_id character varying(256) COLLATE pg_catalog."default",
    resent_by character varying(64) COLLATE pg_catalog."default",
    resent_time bigint,
    CONSTRAINT egpr_notification_resend_history_pkey PRIMARY KEY (notification_resend_uuid)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;