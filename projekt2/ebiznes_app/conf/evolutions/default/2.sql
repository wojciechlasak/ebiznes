# --- !Ups

INSERT INTO "category"("name") VALUES("siatkarskie");
INSERT INTO "category"("name") VALUES("koszykarskie");
INSERT INTO "category"("name") VALUES("biegowe");

INSERT INTO "payment"("name") VALUES("blik");
INSERT INTO "payment"("name") VALUES("przelew");

INSERT INTO "client"("name","address") VALUES("Kamil Nowak", "Zakopane");
INSERT INTO "client"("name","address") VALUES("Jan Nowak", "Zakopane");

INSERT INTO "product"("name","description","category", "photo") VALUES ("R2000","idealne buty do biegania wszedzie. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque pretium massa non varius dictum.","3", "nike.jpg");
INSERT INTO "product"("name","description","category", "photo") VALUES ("Sense Ride","idealne buty do gry w siatke. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque pretium massa non varius dictum.","1", "sample.jpg");
INSERT INTO "product"("name","description","category", "photo") VALUES ("Jordan","idealne buty do kosza. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque pretium massa non varius dictum.","2", "jordan.jpg");
INSERT INTO "product"("name","description","category", "photo") VALUES ("slab","idealne buty do biegania po gorach. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque pretium massa non varius dictum.","3", "slab.jpg");
INSERT INTO "product"("name","description","category", "photo") VALUES ("speedcross","iLorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque pretium massa non varius dictum. Sed interdum elementum consequat. Ut venenatis arcu auctor vestibulum fermentum. Proin sollicitudin est eu egestas imperdiet. Nunc viverra felis non lectus interdum maximus. Class aptent taciti sociosqu ad litora torquent per conubia nostra","3", "speedcross.jpg");
INSERT INTO "product"("name","description","category", "photo") VALUES ("asics","Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque pretium massa non varius dictum. Sed interdum elementum consequat. Ut venenatis arcu auctor vestibulum fermentum. Proin sollicitudin est eu egestas imperdiet. Nunc viverra felis non lectus interdum maximus. Class aptent taciti sociosqu ad litora torquent per conubia nostra","1", "asics.jpg");

INSERT INTO "opinion"("description","product") VALUES ("lorem ipsum dolem", "1");
INSERT INTO "opinion"("description","product") VALUES ("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque pretium massa non varius dictum. ", "1");
INSERT INTO "opinion"("description","product") VALUES ("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque pretium massa non varius dictum. ", "2");
INSERT INTO "opinion"("description","product") VALUES ("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque pretium massa non varius dictum. ", "4");
INSERT INTO "opinion"("description","product") VALUES ("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque pretium massa non varius dictum. ", "4");

# --- !Downs

DELETE FROM "category" WHERE name="siatkarskie";
DELETE FROM "category" WHERE name="koszykarskie";
DELETE FROM "category" WHERE name="biegowe";

DELETE FROM "payment" WHERE name="blik";
DELETE FROM "payment" WHERE name="przelew";

DELETE FROM "client" WHERE name="Kamil Nowak";
DELETE FROM "client" WHERE name="Jan Nowak";

DELETE FROM "product" WHERE name="R2000";
DELETE FROM "product" WHERE name="Sense Ride";
DELETE FROM "product" WHERE name="Jordan";
DELETE FROM "product" WHERE name="slab";
DELETE FROM "product" WHERE name="slab";
DELETE FROM "product" WHERE name="speedcross";
DELETE FROM "product" WHERE name="asics";

DELETE FROM "opinion" WHERE id="1";
DELETE FROM "opinion" WHERE id="2";
DELETE FROM "opinion" WHERE id="3";
DELETE FROM "opinion" WHERE id="4";
DELETE FROM "opinion" WHERE id="5";
