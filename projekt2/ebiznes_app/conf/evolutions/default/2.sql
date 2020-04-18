# --- !Ups

INSERT INTO "product"("name","description") VALUES("spodnie","lorem ipsum");
INSERT INTO "product"("name","description") VALUES("kurtka","lorem ipsum");
INSERT INTO "product"("name","description") VALUES("skarpety","lorem ipsum");

INSERT INTO "opinion"("description","product") VALUES("spodnie super", 1);
INSERT INTO "opinion"("description","product") VALUES("srednie", 1);

# --- !Downs

DELETE FROM "product" WHERE name="spodnie";
DELETE FROM "product" WHERE name="kurtka";
DELETE FROM "product" WHERE name="skarpety";
