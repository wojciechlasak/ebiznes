# --- !Ups

CREATE TABLE "product" (
 "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
 "name" VARCHAR NOT NULL,
 "description" VARCHAR NOT NULL
);

CREATE TABLE "opinion" (
 "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
 "description" VARCHAR NOT NULL,
 "product" INTEGER NOT NULL,
 FOREIGN KEY(product) references product(id)
);

CREATE TABLE "basket" (
 "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT
);

CREATE TABLE "basket_products" (
  "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
  "product" INTEGER NOT NULL,
  "basket" INTEGER NOT NULL,
  FOREIGN KEY(product) references product(id),
  FOREIGN KEY(basket) references basket(id)
);

# --- !Downs

DROP TABLE "basket_products"
DROP TABLE "basket"
DROP TABLE "opinion"
DROP TABLE "product"
