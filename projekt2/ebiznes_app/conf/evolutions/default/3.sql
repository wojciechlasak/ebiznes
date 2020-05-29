# --- !Ups


INSERT INTO "favorite"("client") VALUES("1");
INSERT INTO "favorite"("client") VALUES("2");

# --- !Downs

DELETE FROM "favorite" WHERE id="1";
DELETE FROM "favorite" WHERE id="2";