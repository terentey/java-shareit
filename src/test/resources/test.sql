insert into users (name, email)
values ('owner', 'owner@mail.com');

insert into users (name, email)
values ('booker', 'booker@mail.com');

insert into requests (user_id, description, created)
values (2, 'request', parsedatetime('01-01-2025', 'dd-MM-yyyy'));

insert into items (user_id, name, description, available)
values (1, 'item', 'item', true);

insert into bookings (start, end_time, status, item_id, user_id)
values (parsedatetime('01-02-2025', 'dd-MM-yyyy'), parsedatetime('01-03-2025', 'dd-MM-yyyy'), 'APPROVED', 1, 2);

insert into comments (text, item_id, user_id, created)
values ('comment', 1, 2, parsedatetime('01-04-2025', 'dd-MM-yyyy'));