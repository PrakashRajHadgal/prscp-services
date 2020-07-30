CREATE TABLE public.egpr_library
(
    library_uuid character varying(64) COLLATE pg_catalog."default" NOT NULL,
    event_detail_uuid character varying(64) COLLATE pg_catalog."default",
    document_type character varying(64) COLLATE pg_catalog."default",
    uploaded_document json,
    is_active boolean,
    created_by character varying(64) COLLATE pg_catalog."default",
    created_time bigint,
    last_modified_by character varying(63) COLLATE pg_catalog."default",
    last_modified_time bigint,
    tenant_id character varying(256) COLLATE pg_catalog."default",
    module_code character varying(10) COLLATE pg_catalog."default",
    source_uuid character varying(64) COLLATE pg_catalog."default",
    CONSTRAINT pk_library_uuid PRIMARY KEY (library_uuid)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;
