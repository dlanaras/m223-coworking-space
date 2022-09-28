INSERT INTO public."User" (
"foreName", "lastName", "email", "isAdmin", "password") VALUES (
'Lynn'::character varying, 'Fredi'::character varying, 'lfredi0@eepurl.com'::character varying, true::boolean, '1QWrfIEgoq'::character varying)
 returning user_id;

INSERT INTO public."User" (
"foreName", "lastName", "email", "isAdmin", "password") VALUES (
'Trumaine'::character varying, 'Toke'::character varying, 'ttoke1@paginegialle.it'::character varying, false::boolean, 'SSwpzJkGJ'::character varying)
 returning user_id;


INSERT INTO public."Booking" (
"dayPrice", "endDate", "startDate", "needsLaptop", user_id) VALUES (
'71.86'::real, '2022-09-26'::date, '5/11/2022'::date, false::boolean, '2'::bigint)
 returning booking_id;

INSERT INTO public."Booking" (
"dayPrice", "endDate", "startDate", "needsLaptop", user_id) VALUES (
'66.31'::real, '9/29/2021'::date, '10/26/2021'::date, false::boolean, '2'::bigint)
 returning booking_id;