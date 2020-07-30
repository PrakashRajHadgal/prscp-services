CREATE TABLE public.egpr_map_tender_press
(
    map_tender_press_uuid character varying(64) COLLATE pg_catalog."default" NOT NULL,
    press_master_uuid character varying(64) COLLATE pg_catalog."default",
    tender_notice_uuid character varying(64) COLLATE pg_catalog."default",
    notify_status boolean,
    is_active boolean,
    created_by character varying(64) COLLATE pg_catalog."default",
    created_time bigint,
    last_modified_by character varying(64) COLLATE pg_catalog."default",
    last_modified_time bigint,
    tenant_id character varying(256) COLLATE pg_catalog."default",
    module_code character varying(10) COLLATE pg_catalog."default",
    source_uuid character varying(64) COLLATE pg_catalog."default",
    CONSTRAINT egpr_map_tender_press_pkey PRIMARY KEY (map_tender_press_uuid),
    CONSTRAINT "FK_press_master_uuid" FOREIGN KEY (press_master_uuid)
        REFERENCES public.egpr_press_master (press_master_uuid) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT "FK_tender_notice_uuid" FOREIGN KEY (tender_notice_uuid)
        REFERENCES public.egpr_tender_notice (tender_notice_uuid) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;
