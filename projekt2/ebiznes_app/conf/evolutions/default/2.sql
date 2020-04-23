# --- !Ups

INSERT INTO "category"("name") VALUES("siatkarskie");
INSERT INTO "category"("name") VALUES("koszykarskie");
INSERT INTO "category"("name") VALUES("biegowe");

INSERT INTO "payment"("name") VALUES("blik");
INSERT INTO "payment"("name") VALUES("przelew");

INSERT INTO "client"("name","address") VALUES("Kamil Nowak", "Zakopane");
INSERT INTO "client"("name","address") VALUES("Jan Nowak", "Zakopane");

INSERT INTO "product"("name","description","category") VALUES ("R2000","idealne buty do biegania wszedzie","3");

# --- !Downs

DELETE FROM "category" WHERE name="siatkarskie";
DELETE FROM "category" WHERE name="koszykarskie";
DELETE FROM "category" WHERE name="biegowe";

DELETE FROM "payment" WHERE name="blik";
DELETE FROM "payment" WHERE name="przelew";

DELETE FROM "client" WHERE name="Kamil Nowak";
DELETE FROM "client" WHERE name="Jan Nowak";

DELETE FROM "product" WHERE name="R2000";
