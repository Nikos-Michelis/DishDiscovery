DROP DATABASE IF EXISTS cookingdb;
CREATE DATABASE cookingdb;
USE cookingdb;

CREATE TABLE `user` (
  `user_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
   user_uuid VARCHAR(36) DEFAULT NULL,
  `userName` varchar(45) NOT NULL,
  `email` varchar(50) DEFAULT NULL,
  `password` varchar(200) NOT NULL,
  `firstName` varchar(200) DEFAULT NULL,
  `lastName` varchar(200) DEFAULT NULL,
  `tel` varchar(50) DEFAULT NULL,
  `enable` BOOLEAN DEFAULT FALSE,
  `accountLocked` BOOLEAN DEFAULT FALSE,
   total_blocks INT DEFAULT NULL,
   total_attempts INT DEFAULT NULL,
   lockedAt DATETIME DEFAULT NULL,
   lock_expiresAt DATETIME DEFAULT NULL,
   created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
   last_update DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,  PRIMARY KEY (`user_id`),
  UNIQUE(`userName`),
  UNIQUE(`user_uuid`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE otp_resend (
    resend_id INT UNSIGNED AUTO_INCREMENT,
    user_id INT UNSIGNED NOT NULL,
    otp_type VARCHAR(36) NOT NULL,
    otp_resend_count INT DEFAULT 0 NOT NULL,
    last_otp_sent_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(resend_id),
    CONSTRAINT otp_resend_fk_user FOREIGN KEY (user_id) REFERENCES user(user_id)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE otp(
	otp_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
	user_id INT UNSIGNED NOT NULL,
    otp_uuid VARCHAR(256) UNIQUE DEFAULT NULL,
	otp_code VARCHAR(6) DEFAULT NULL,
	otp_type VARCHAR(36) DEFAULT NULL,
	createdAt DATETIME DEFAULT NULL,
	expiresAt DATETIME DEFAULT NULL,
	validatedAt DATETIME DEFAULT NULL,
	redeemed BOOLEAN DEFAULT FALSE,
    otp_resend_count INT DEFAULT 0 NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_update DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (otp_id),
    UNIQUE(user_id, otp_type),
	CONSTRAINT activation_otp_fk_user FOREIGN KEY (user_id) REFERENCES user(user_id)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE token (
	token_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
	user_id INT UNSIGNED NOT NULL,
	jti VARCHAR(36) UNIQUE DEFAULT NULL,
	token TEXT NOT NULL,
	token_type VARCHAR(45) NOT NULL,
	token_scope VARCHAR(45) DEFAULT NULL,
	expired BOOLEAN DEFAULT FALSE,
	revoked BOOLEAN DEFAULT FALSE,
	token_expires DATETIME DEFAULT NULL,
	ipAddress VARCHAR(39),
	created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_update DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (token_id),
	CONSTRAINT token_fk_user FOREIGN KEY (user_id) REFERENCES user(user_id)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `role` (
  `role_id` int UNSIGNED NOT NULL AUTO_INCREMENT,
  `description` varchar(45) DEFAULT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  last_update DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `user_role` (
  `user_id` INT UNSIGNED NOT NULL,
  `role_id` INT UNSIGNED NOT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  last_update DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  KEY `user_role_fk_user` (`user_id`),
  KEY `user_role_fk_role` (`role_id`),
  CONSTRAINT `user_role_fk_role` FOREIGN KEY (`role_id`) REFERENCES `role` (`role_id`) ON DELETE CASCADE,
  CONSTRAINT `user_role_fk_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE category(
	category_id INT NOT NULL AUTO_INCREMENT,
    category_name VARCHAR(50) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_update DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (category_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE wishlist (
    wishlist_id INT NOT NULL AUTO_INCREMENT,
    user_id INT UNSIGNED NOT NULL,
    last_update DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (wishlist_id),
    UNIQUE KEY unique_user (user_id),
    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE recipes (
    recipe_id INT AUTO_INCREMENT,
	user_id INT UNSIGNED NOT NULL,
    recipe_uuid VARCHAR(255) DEFAULT NULL,
    recipe_name VARCHAR(255) NOT NULL,
    category_id INT DEFAULT NULL,
    difficulty ENUM('Hard', 'Normal', 'Easy') NOT NULL,
    execution_time INT NOT NULL,
    image VARCHAR(255) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_update DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (recipe_id),
	/*UNIQUE KEY unique_recipe_name (recipe_name),*/
    FOREIGN KEY (category_id) REFERENCES category(category_id) ON DELETE CASCADE,
	FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE recipe_list (
    wishlist_id  INT NOT NULL,
    recipe_id INT NOT NULL,
	created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_update DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (wishlist_id) REFERENCES wishlist(wishlist_id) ON DELETE CASCADE,
    FOREIGN KEY (recipe_id) REFERENCES recipes(recipe_id) ON DELETE CASCADE,
    PRIMARY KEY(wishlist_id, recipe_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE measurements (
    unit_id INT NOT NULL AUTO_INCREMENT,
    unit_name VARCHAR(5) NOT NULL,
	created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_update DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,   
    PRIMARY KEY(unit_id),
	UNIQUE KEY unique_unit_name (unit_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE ingredients_category(
	ingredient_category_id INT NOT NULL AUTO_INCREMENT,
	ingredient_category VARCHAR(50) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_update DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (ingredient_category_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE ingredients (
    ingredient_id INT NOT NULL AUTO_INCREMENT,
    ingredient_category_id INT DEFAULT NULL,
    ingredient_name VARCHAR(50) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_update DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (ingredient_id),
	UNIQUE KEY ingredient_name_in (ingredient_name),
	FOREIGN KEY (ingredient_category_id) REFERENCES ingredients_category(ingredient_category_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE recipe_ingredients (
    recipe_ingredient_id INT NOT NULL AUTO_INCREMENT,
    recipe_id INT NOT NULL,
	ingredient_id INT NOT NULL,
	unit_id INT NOT NULL,
	ingredient_amount TINYTEXT NOT NULL,
	created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_update DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,    
    PRIMARY KEY (recipe_ingredient_id),
    FOREIGN KEY (recipe_id) REFERENCES recipes(recipe_id) ON DELETE CASCADE,
    FOREIGN KEY (ingredient_id) REFERENCES ingredients(ingredient_id) ON DELETE CASCADE,
	FOREIGN KEY (unit_id) REFERENCES measurements(unit_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE recipe_steps (
	step_order INT NOT NULL,
	step_instructions TEXT NOT NULL,
    recipe_id INT NOT NULL,
	created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_update DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,    
    PRIMARY KEY (recipe_id, step_order),
    FOREIGN KEY (recipe_id) REFERENCES recipes(recipe_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


INSERT INTO `role`
(`role_id`,`description`)VALUES(1,'ADMIN'), (2,'USER');

INSERT INTO user (user_id,user_uuid, userName, email, password) 
VALUES (1, 'ed063570-1v63-4037-49hf-15659e8cve85', 'user1', 'example@gmail.com' ,'pass1');

INSERT INTO user (user_id,user_uuid, userName, email, password, enable) 
VALUES ('7', '093a2bd9-40f9-4e87-a89a-7ab54ac04549', 'user3', 'example23@gmail.com', 'pass2', '1');

UPDATE user_role SET role_id = 1 WHERE user_id = 7;

INSERT INTO user_role (user_id, role_id) 
VALUES (7,1);

INSERT INTO category (category_name) 
VALUES 
('Appetizers'),
('Main Courses'),
('Side Dishes'),
('Soups and Stews'),
('Salads'),
('Desserts'),
('Beverages'),
('Breakfast and Brunch'),
('Breads and Pastries'),
('Grilling and BBQ'),
('Vegetarian and Vegan'),
('International Cuisine'),
('Healthy and Low-Calorie'),
('Comfort Food'),
('One-Pot Meals');
INSERT INTO measurements (unit_name) VALUES
('ml'), ('l'), ('g'), ('kg'),
('tsp'), ('tbsp'), ('bunch'), ('dash');
INSERT INTO ingredients_category (ingredient_category)
VALUES 
( 'Vegetables'),
( 'Spices and Herbs'),
( 'Cereals and Pulses'),
( 'Meat'),
( 'Dairy Products'),
( 'Fruits'),
( 'Seafood'),
( 'Sugar and Sugar Products'),
( 'Nuts and Oilseeds'),
( 'Other Ingredients');

INSERT INTO wishlist(wishlist_id, user_id) values (1, 7);

INSERT INTO recipes (user_id, recipe_name, category_id, difficulty, execution_time, image)
VALUES 
(1, 'Mushroom Pie', 11, 'Normal', 30, 'http://localhost:8080/RecipeRestApi-1.0-SNAPSHOT/api/images/1707944301997_manitaropita-nhstisimh.jpg'),
(1, 'Cheese and Ham Toast', 8, 'Easy', 9, 'http://localhost:8080/RecipeRestApi-1.0-SNAPSHOT/api/images/1707944340388_toast.jpg'),
(1, 'New York-Style Cheesecake', 6, 'Normal', 20, 'http://localhost:8080/RecipeRestApi-1.0-SNAPSHOT/api/images/1708160986513_cheesecake.jpg'),
(1, 'Pumkin Pie', 11, 'Hard', 50, 'http://localhost:8080/RecipeRestApi-1.0-SNAPSHOT/api/images/1708161202053_pumkin_pie.jpg'),
(1, 'Vanillia Cake', 6, 'Normal', 30, 'http://localhost:8080/RecipeRestApi-1.0-SNAPSHOT/api/images/1708161438711_vanilla-cake.jpg'),
(1, 'Spinach Pie', 9, 'Normal', 50, 'http://localhost:8080/RecipeRestApi-1.0-SNAPSHOT/api/images/1708176737276_Spinach_pie.jpg'),
(1, 'Vanillia Icecream', 6, 'Normal', 30, 'http://localhost:8080/RecipeRestApi-1.0-SNAPSHOT/api/images/1708162088141_Vanilla-Ice-Cream.jpg'),
(1, 'Spaghetti With Tomato Sauce', 8, 'Normal', 40, 'http://localhost:8080/RecipeRestApi-1.0-SNAPSHOT/api/images/1708162593473_pasta-sauce-recipe-cover.jpg');

INSERT INTO recipe_list (wishlist_id, recipe_id)
VALUES (1, 3),(1, 5),(1, 7);


INSERT INTO ingredients (ingredient_name, ingredient_category_id)
VALUES 
('Mushrooms1', 1),
('Onions2', 1),
('Garlic3 cloves', 1),
('Thyme4', 10),
('Salt5', 2),
('Black pepper6', 2),
('Flour7', 3),
('Vegetable or mushroom broth8' , 1),
('Heavy cream9', 5),
('Puff pastry10', 10),
('Egg2', 10);
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, unit_id, ingredient_amount)
VALUES
(1, 1, 3, '200'),
(1, 2, 1, '1'),
(1, 3, 1, '2'),
(1, 4, 5, '1'),
(1, 5, 5, '1/2'),
(1, 6, 5, '1/4'),
(1, 7, 6, '2'),
(1, 8, 1, '2'),
(1, 9, 2, '1/2'),
(1, 10, 6, '1'),
(1, 11, 5, '1');
INSERT INTO recipe_steps (step_order, step_instructions, recipe_id) 
VALUES 
(1,'Preheat your oven to 375°F (190°C).\n\n',1),
(2,'In a large skillet, heat olive oil over medium heat. Add chopped onions and minced garlic, sauté until softened. Add sliced mushrooms, thyme, salt, and pepper. Cook until the mushrooms release their moisture and become golden brown.',1),
(3,'Sprinkle flour over the mushroom mixture and stir well to combine. Cook for an additional 2 minutes. Pour in the vegetable or mushroom broth and heavy cream. Stir continuously until the mixture thickens. Remove from heat and let it cool slightly.',1),
(4,'Roll out the puff pastry on a floured surface. If needed, cut it to fit your pie dish or individual tart pans.',1),
(5,'Transfer the mushroom mixture into the pie dish or tart pans, spreading it evenly. Place the rolled-out puff pastry over the filling, trimming any excess. Press the edges to seal, and use a fork to create a decorative border.',1),
(6,'Brush the top of the pastry with the beaten egg to give it a golden finish. Cut a few slits in the center to allow steam to escape during baking. Place the pie in the preheated oven and bake for 25-30 minutes, or until the pastry is golden brown and crispy.',1),
(7,'Allow the mushroom pie to cool for a few minutes before slicing. Serve warm and enjoy your delicious Mushroom Pie!',1),
(1,'Preheat your oven broiler or toaster oven.\n\n',2),
(2,'Lightly toast the bread slices in a toaster or on a griddle.',2),
(3,'Spread a thin layer of butter on one side of each toasted slice.',2),
(4,'Place 2 slices of ham on one of the bread slices.',2),
(5,'Sprinkle half of the shredded cheese over the ham.',2),
(6,'If you like, spread a thin layer of Dijon mustard on the other slice of bread before placing it on top, butter side up.',2),
(7,'Place the assembled sandwich on a baking sheet.',2),
(8,'Put the baking sheet under the broiler or in the toaster oven until the cheese is melted and bubbly, and the bread is golden brown. Keep a close eye on it to prevent burning, as this can happen quickly.',2),
(9,'Carefully remove the toast from the oven and let it cool for a minute.',2),
(10,'Optionally, sprinkle freshly ground black pepper over the top and garnish with chopped parsley or chives.',2),
(11,'Cut the toast diagonally and serve hot.',2),
(1,'Preheat your oven to 325°F (163°C).\n\n',3),
(2,'In a bowl, combine graham cracker crumbs and melted butter. Press the mixture into the bottom of a springform pan to create the crust. Bake it for about 10 minutes and then let it cool.',3),
(3,'a. In a large mixing bowl, beat the cream cheese until smooth.\nb. Add sugar and vanilla extract, and continue to beat until well combined.\nc. Add eggs one at a time, mixing well after each addition.\nd. Stir in sour cream.\ne. Gradually add flour and a pinch of salt, mixing until just combined. Be careful not to overmix.',3),
(4,'Pour the cream cheese mixture over the cooled crust in the springform pan. Smooth the top with a spatula.',3),
(5,'Place the springform pan in a larger baking pan. Fill the larger pan with hot water, creating a water bath. This helps prevent cracks on the surface of the cheesecake.',3),
(6,'Bake for about 1 hour or until the center is set and the top is lightly browned. Keep an eye on it to avoid overbaking.',3),
(7,'Once done, turn off the oven and let the cheesecake cool in the oven for about an hour. Then, refrigerate it for several hours or overnight.',3),
(8,'Slice and serve chilled. You can top it with fruit compote, whipped cream, or your favorite topping.',3),
(1,'Prepare the Pie Crust:\nIn a food processor, combine the flour, butter, and salt. Pulse until the mixture resembles coarse crumbs.\nAdd ice water, one tablespoon at a time, pulsing until the dough just comes together.\nForm the dough into a disk, wrap it in plastic wrap, and refrigerate for at least 30 minutes.',4),
(2,'Roll out the Pie Crust:\nPreheat your oven to 425°F (220°C).\nOn a floured surface, roll out the pie crust to fit a 9-inch pie dish. Transfer the crust to the pie dish and trim any excess edges.',4),
(3,'Prepare the Pumpkin Filling:\nIn a large bowl, whisk together the pumpkin puree, sugar, cinnamon, ginger, cloves, and salt.\nAdd the eggs and beat until well combined.\nGradually stir in the evaporated milk until the mixture is smooth.',4),
(4,'Assemble the Pie:\nPour the pumpkin filling into the prepared pie crust.\nSmooth the top with a spatula and place the pie on a baking sheet.',4),
(5,'Bake:\nBake the pie in the preheated oven for 15 minutes. Then reduce the oven temperature to 350°F (175°C) and continue baking for 40-50 minutes or until a knife inserted into the center comes out clean.',4),
(6,'Cool and Serve:\nAllow the pie to cool completely on a wire rack before serving. You can refrigerate it for a few hours or overnight for best results.\nServe with a dollop of whipped cream if desired.',4),
(1,'For the Cake:\n1. Preheat your oven to 350°F (175°C). Grease and flour two 9-inch round cake pans.\n2. In a medium bowl, whisk together the flour, baking powder, and salt. Set aside.\n3. In a large mixing bowl, cream together the softened butter and sugar until light and fluffy.\n4. Add the eggs one at a time, beating well after each addition. Stir in the vanilla extract.\n5. Gradually add the dry ingredients to the wet ingredients, alternating with the milk. Begin and end with the dry ingredients, mixing just until combined. Do not overmix.\n6. Divide the batter evenly between the prepared cake pans and smooth the tops.\n7. Bake in the preheated oven for 25-30 minutes or until a toothpick inserted into the center comes out clean.\n8. Allow the cakes to cool in the pans for 10 minutes, then transfer them to a wire rack to cool completely.',5),
(2,'For the Vanilla Buttercream Frosting:\n1. In a large bowl, beat the softened butter until creamy.\n2. Gradually add the confectioners\' sugar, one cup at a time, beating well after each addition.\n3. Add the milk and vanilla extract, and continue beating until smooth and fluffy.',5),
(3,'Assembling the Cake:\n1. Once the cakes are completely cooled, spread a layer of buttercream on top of one cake layer.\n2. Place the second cake layer on top and frost the top and sides of the entire cake with the remaining buttercream.\n3. Decorate as desired, and enjoy your delicious homemade vanilla cake!',5),
(1,'For the Filling:\nPreheat your oven to 375°F (190°C).',6),
(2,'In a large skillet, heat olive oil over medium heat. Add chopped onion and garlic, sauté until softened.',6),
(3,'Add the chopped spinach to the skillet and cook until wilted. If using frozen spinach, make sure to squeeze out excess water after thawing.',6),
(4,'In a large mixing bowl, combine the cooked spinach mixture with feta, ricotta, Parmesan, beaten eggs, salt, pepper, and nutmeg. Mix well until all ingredients are evenly combined.',6),
(5,'Unroll the phyllo dough and cover it with a damp kitchen towel to prevent it from drying out.\n\n',6),
(6,'Brush a 9x13-inch baking dish with melted butter.\n\n',6),
(7,'Place one sheet of phyllo dough in the baking dish and brush it with melted butter. Repeat with several more layers.\n\n',6),
(8,'Spread the spinach and cheese mixture evenly over the phyllo layers.\n\n',6),
(9,'Continue layering the remaining phyllo sheets on top of the spinach mixture, brushing each layer with melted butter.',6),
(10,'Once all the layers are assembled, use a sharp knife to cut the phyllo layers into squares or diamonds.',6),
(11,'Bake in the preheated oven for about 45-50 minutes, or until the top is golden brown and crispy.\n\n',6),
(12,'Allow the spinach pie to cool for a few minutes before serving. It can be served warm or at room temperature.',6),
(1,'In a mixing bowl, whisk together the heavy cream, whole milk, sugar, vanilla extract, and a pinch of salt until the sugar is completely dissolved.',7),
(2,'Pour the mixture into an ice cream maker.',7),
(3,'Follow the manufacturer\'s instructions for your specific ice cream maker to churn the mixture. This usually takes about 20-25 minutes.',7),
(4,'Once the ice cream reaches a soft-serve consistency, transfer it to a lidded container, and freeze for at least 4 hours or until firm.',7),
(5,'If you prefer a creamier texture, you can take the ice cream out of the freezer after about 2 hours, and give it a good stir to break up any ice crystals. Then return it to the freezer to finish firming up.',7),
(6,'When ready to serve, scoop the vanilla ice cream into bowls or cones, and enjoy!',7),
(1,'Bring a large pot of salted water to a boil. Cook the spaghetti according to the package instructions until al dente. Drain and set aside.',8),
(2,'In a large skillet, heat the olive oil over medium heat. Add the chopped onion and cook until softened, about 3-4 minutes.',8),
(3,'Add the minced garlic to the skillet and sauté for about 1 minute until fragrant.',8),
(4,'Pour in the crushed tomatoes, dried oregano, dried basil, red pepper flakes (if using), salt, and pepper. Stir to combine.',8),
(5,'Bring the sauce to a simmer, then reduce the heat to low and let it simmer for about 15-20 minutes, stirring occasionally. If the sauce is too thick, you can add a splash of water to achieve your desired consistency.\n\n',8),
(6,'Taste the sauce and adjust the seasonings as needed. If you prefer a smoother sauce, you can use an immersion blender to blend it until smooth.',8),
(7,'Once the sauce is ready, add the cooked spaghetti to the skillet. Toss the spaghetti in the sauce until it\'s well-coated and heated through.',8),
(8,'Serve the spaghetti in bowls, topped with grated Parmesan cheese. Garnish with fresh basil or parsley if desired.',8);




