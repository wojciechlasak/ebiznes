# --- !Ups

INSERT INTO "basket"("client") VALUES("1");
INSERT INTO "basket"("client") VALUES("2");

INSERT INTO "favorite"("client") VALUES("1");
INSERT INTO "favorite"("client") VALUES("2");

# --- !Downs

DELETE FROM "basket" WHERE id="1";
DELETE FROM "basket" WHERE id="2";

DELETE FROM "favorite" WHERE id="1";
DELETE FROM "favorite" WHERE id="2";