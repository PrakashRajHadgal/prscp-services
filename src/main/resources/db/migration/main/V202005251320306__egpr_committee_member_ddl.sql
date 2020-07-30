CREATE TABLE public.egpr_committee_member
(
    committee_member_uuid character varying(64) COLLATE pg_catalog."default" NOT NULL,
    committee_uuid character varying(64) COLLATE pg_catalog."default",
    department_name character varying(255) COLLATE pg_catalog."default",
    user_uuid character varying(64) COLLATE pg_catalog."default",
    is_active boolean,
    created_by character varying(64) COLLATE pg_catalog."default",
    created_time bigint,
    last_modified_by character varying(64) COLLATE pg_catalog."default",
    last_modified_time bigint,
    tenant_id character varying(256) COLLATE pg_catalog."default",
    department_uuid character varying(64) COLLATE pg_catalog."default",
    module_code character varying(10) COLLATE pg_catalog."default",
    source_uuid character varying(64) COLLATE pg_catalog."default",
    CONSTRAINT egpr_committee_member_pkey PRIMARY KEY (committee_member_uuid),
    CONSTRAINT fk_committe_uuid FOREIGN KEY (committee_uuid)
        REFERENCES public.egpr_committee_detail (committee_uuid) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

CREATE INDEX fki_fk_committe_uuid
    ON public.egpr_committee_member USING btree
    (committee_uuid COLLATE pg_catalog."default")
    TABLESPACE pg_default;
	