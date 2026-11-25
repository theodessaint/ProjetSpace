INSERT INTO astronautes_catalogue (id, nom, genre, role)
VALUES
    (1, 'Neil ARMSTRONG', 'Homme', NULL),
    (2, 'Buzz ALDRIN', 'Homme', NULL),
    (3, 'Yuri GAGARIN', 'Homme', NULL),
    (4, 'Valentina TERESHKOVA', 'Femme', NULL),
    (5, 'Thomas PESQUET', 'NB', NULL),
    (6, 'Valeri POLYAKOV', 'Homme', NULL),
    (7, 'Samantha CRISTOFORETTI', 'Femme', NULL),
    (8, 'Luca PARMITANO', 'NB', NULL),
    (9, 'Eileen COLLINS', 'Femme', NULL),
    (10, 'Yang LIU', 'Femme', NULL),
    (11, 'Andreas MOGENSEN', 'Homme', NULL),
    (12, 'Jordan ABID', 'Homme', NULL),

    -- AGRICULTEURS
    (13, 'Alan SHEPARD', 'Homme', 'Agriculteur'),
    (14, 'John GLENN', 'Homme', 'Agriculteur'),
    (15, 'Sally RIDE', 'Femme', 'Agriculteur'),
    (16, 'Mae JEMISON', 'Femme', 'Agriculteur'),
    (17, 'Tim PEAKE', 'Homme', 'Agriculteur'),
    (18, 'Lucas PARMITANO', 'Homme', 'Agriculteur'),

    -- CHERCHEURS
    (19, 'Michael COLLINS', 'Homme', NULL),
    (20, 'Jim LOVELL', 'NB', NULL),
    (21, 'Peggy WHITSON', 'Femme', NULL),
    (22, 'Sunita WILLIAMS', 'Femme', NULL),
    (23, 'Chris HADFIELD', 'Homme', NULL),
    (24, 'Claudie HAIGNERÉ', 'Femme', NULL),

    -- MECANICIENS
    (25, 'Alexei LEONOV', 'Homme', 'Mecanicien'),
    (26, 'Sergei KRIKALEV', 'Homme', 'Mecanicien'),
    (27, 'Scott KELLY', 'Homme', 'Mecanicien'),
    (28, 'Mark KELLY', 'Homme', 'Mecanicien'),
    (29, 'Koichi WAKATA', 'NB', 'Mecanicien'),
    (30, 'Akihiko HOSHIDE', 'Homme', 'Mecanicien'),

    -- PLOMBIERS
    (31, 'Liwei YANG', 'Homme', 'Plombier'),
    (32, 'Junlong FEI', 'Homme', 'Plombier'),
    (33, 'Haisheng NIE', 'NB', 'Plombier'),
    (34, 'Soichi NOGUCHI', 'Homme', 'Plombier'),
    (35, 'Svetlana SAVITSKAYA', 'Femme', 'Plombier'),
    (36, 'Frederick HAISE', 'Homme', 'Plombier')
ON DUPLICATE KEY UPDATE nom = VALUES(nom), genre = VALUES(genre), role = VALUES(role);