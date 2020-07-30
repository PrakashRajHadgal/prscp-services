CREATE TABLE public.egpr_event_guestlist
(
    event_guest_uuid character varying(64) COLLATE pg_catalog."default" NOT NULL,
    event_detail_uuid character varying(64) COLLATE pg_catalog."default",
    event_guest_type character varying(64) COLLATE pg_catalog."default",
    department_name character varying(255) COLLATE pg_catalog."default",
    user_uuid character varying(64) COLLATE pg_catalog."default",
    guest_name character varying(255) COLLATE pg_catalog."default",
    guest_email character varying(255) COLLATE pg_catalog."default",
    guest_mobile character varying(50) COLLATE pg_catalog."default",
    notification_template_uuid character varying(64) COLLATE pg_catalog."default",
    sent_flag boolean,
    is_active boolean,
    created_by character varying(64) COLLATE pg_catalog."default",
    created_time bigint,
    last_modified_by character varying COLLATE pg_catalog."default",
    last_modified_time bigint,
    tenant_id character varying(256) COLLATE pg_catalog."default",
    department_uuid character varying(64) COLLATE pg_catalog."default",
    module_code character varying(10) COLLATE pg_catalog."default",
    source_uuid character varying(64) COLLATE pg_catalog."default",
    CONSTRAINT egpr_event_guestlist_pkey PRIMARY KEY (event_guest_uuid)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;