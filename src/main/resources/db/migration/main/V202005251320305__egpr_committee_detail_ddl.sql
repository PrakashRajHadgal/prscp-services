CREATE TABLE public.egpr_committee_detail
(
    committee_uuid character varying(64) COLLATE pg_catalog."default" NOT NULL,
    committee_name character varying(255) COLLATE pg_catalog."default",
    committee_description character varying(255) COLLATE pg_catalog."default",
    is_active boolean,
    created_by character varying(64) COLLATE pg_catalog."default",
    created_time bigint,
    last_modified_by character varying(64) COLLATE pg_catalog."default",
    last_modified_time bigint,
    tenant_id character varying(256) COLLATE pg_catalog."default",
    module_code character varying(10) COLLATE pg_catalog."default",
    source_uuid character varying(64) COLLATE pg_catalog."default",
    CONSTRAINT pk_committee_uuid PRIMARY KEY (committee_uuid)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;