// script to add


CREATE TABLE public.tbl_contact_us
(
        id bigint NOT NULL,
 
    version integer,
    creation_date timestamp without time zone,
    last_update timestamp without time zone,
  user_id bigint,
    message_ character varying(3000) COLLATE pg_catalog."default",

    lastupdate timestamp(6) without time zone,
    creationdate timestamp without time zone,
    CONSTRAINT tbl_contact_us_pkey PRIMARY KEY (id),
  CONSTRAINT fk_contact_us_user_id FOREIGN KEY (user_id) 
	
        REFERENCES public.tbl_user (id) MATCH SIMPLE
	)



WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.tbl_contact_us
    OWNER to postgres;

