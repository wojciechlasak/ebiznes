# --- !Ups

CREATE TABLE "category" (
 "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
 "name" VARCHAR NOT NULL,
 UNIQUE(name)
);

CREATE TABLE "client" (
 "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
 "name" VARCHAR NOT NULL,
 "address" VARCHAR NOT NULL,
 UNIQUE(name, address)
);

CREATE TABLE "payment" (
 "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
 "name" VARCHAR NOT NULL
);

CREATE TABLE "product" (
 "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
 "name" VARCHAR NOT NULL,
 "description" VARCHAR NOT NULL,
 "category" INT NOT NULL,
 "photo" VARCHAR NOT NULL,
 FOREIGN KEY(category) references category(id)
);

CREATE TABLE "opinion" (
 "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
 "description" VARCHAR NOT NULL,
 "product" INTEGER NOT NULL,
 FOREIGN KEY(product) references product(id)
);

CREATE TABLE "basket" (
 "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
 "user" VARCHAR NOT NULL,
 FOREIGN KEY(user) references user(id)
);

CREATE TABLE "basket_products" (
  "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
  "quantity" INTEGER NOT NULL,
  "product" INTEGER NOT NULL,
  "basket" INTEGER NOT NULL,
  FOREIGN KEY(product) references product(id),
  FOREIGN KEY(basket) references basket(id),
  UNIQUE(product, basket)
);

CREATE TABLE "favorite" (
 "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
 "user" VARCHAR NOT NULL,
 FOREIGN KEY(user) references user(id)
);

CREATE  TABLE "favorite_products" (
 "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
 "product" INTEGER NOT NULL,
 "favorite" INTEGER NOT NULL,
 FOREIGN KEY(product) references product(id),
 FOREIGN KEY(favorite) references favorite(id),
 UNIQUE(product, favorite)
);

CREATE TABLE "order" (
 "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
 "basket" INTEGER NOT NULL,
 "payment" INTEGER NOT NULL,
 FOREIGN KEY(payment) references payment(id),
 FOREIGN KEY(basket) references basket(id),
 UNIQUE(basket, payment)
);

# --- !Downs

DROP TABLE "order"
DROP TABLE "favorite_products"
DROP TABLE "favorite"
DROP TABLE "basket_products"
DROP TABLE "basket"
DROP TABLE "opinion"
DROP TABLE "category"
DROP TABLE "client"
DROP TABLE "payment"
DROP TABLE "product"
