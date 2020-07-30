CREATE TABLE public.eg_device_source_detail
(
    source_uuid character varying(64) COLLATE pg_catalog."default" NOT NULL,
    module_type character varying(64) COLLATE pg_catalog."default",
    device_type character varying(64) COLLATE pg_catalog."default",
    module_code character varying(10) COLLATE pg_catalog."default",
    device_details json,
    created_by character varying(64) COLLATE pg_catalog."default",
    created_time bigint,
    tenant_id character varying(256) COLLATE pg_catalog."default",
    CONSTRAINT egpr_device_source_detail_pkey PRIMARY KEY (source_uuid)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;