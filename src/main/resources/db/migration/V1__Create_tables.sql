CREATE TABLE public.app_user (
                             id BIGSERIAL PRIMARY KEY,
                             name TEXT NOT NULL
);

CREATE TABLE public.sleep_log (
                                  id BIGSERIAL PRIMARY KEY,
                                  user_id BIGINT NOT NULL REFERENCES public.app_user(id),
                                  sleep_date DATE NOT NULL,
                                  time_in_bed_start TIME NOT NULL,
                                  time_in_bed_end TIME NOT NULL,
                                  total_time_in_bed BIGINT NOT NULL,
                                  sleep_quality VARCHAR(10) NOT NULL
);
