CREATE TABLE public.egpr_event_detail
(
    event_detail_uuid character varying(64) COLLATE pg_catalog."default" NOT NULL,
    event_title character varying(255) COLLATE pg_catalog."default",
    event_location character varying(255) COLLATE pg_catalog."default",
    sector character varying(64) COLLATE pg_catalog."default",
    organizer_department_name character varying(255) COLLATE pg_catalog."default",
    organizer_user_uuid character varying(64) COLLATE pg_catalog."default",
    organizer_user_name character varying(255) COLLATE pg_catalog."default",
    facebook_url character varying(255) COLLATE pg_catalog."default",
    twitter_url character varying(255) COLLATE pg_catalog."default",
    instagram_url character varying(255) COLLATE pg_catalog."default",
    start_date timestamp without time zone,
    start_time time without time zone,
    end_date timestamp without time zone,
    end_time time without time zone,
    event_description character varying(1024) COLLATE pg_catalog."default",
    event_type character varying(64) COLLATE pg_catalog."default",
    event_image json,
    event_budget numeric,
    committee_uuid character varying(64) COLLATE pg_catalog."default",
    event_status character varying(64) COLLATE pg_catalog."default",
    is_active boolean,
    created_by character varying(64) COLLATE pg_catalog."default",
    created_time bigint,
    last_modified_by character varying(64) COLLATE pg_catalog."default",
    last_modified_time bigint,
    event_id character varying(64) COLLATE pg_catalog."default",
    tenant_id character varying(256) COLLATE pg_catalog."default",
    locality_name character varying(100) COLLATE pg_catalog."default",
    organizer_department_uuid character varying(64) COLLATE pg_catalog."default",
    module_code character varying(10) COLLATE pg_catalog."default",
    notification_template_uuid character varying(64) COLLATE pg_catalog."default",
    source_uuid character varying(64) COLLATE pg_catalog."default",
    CONSTRAINT egpr_event_detail_pkey PRIMARY KEY (event_detail_uuid),
    CONSTRAINT event_id UNIQUE (event_id)

)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;