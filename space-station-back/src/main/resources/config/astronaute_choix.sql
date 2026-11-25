INSERT INTO astronautes_catalogue (id, nom, genre, role)
VALUES
    (1, 'Neil ARMSTRONG', 'homme', NULL),
    (2, 'Buzz ALDRIN', 'homme', NULL),
    (3, 'Yuri GAGARIN', 'homme', NULL),
    (4, 'Valentina TERESHKOVA', 'femme', NULL),
    (5, 'Thomas PESQUET', 'NB', NULL),
    (6, 'Valeri POLYAKOV', 'homme', NULL),
    (7, 'Samantha CRISTOFORETTI', 'femme', NULL),
    (8, 'Luca PARMITANO', 'NB', NULL),
    (9, 'Eileen COLLINS', 'femme', NULL),
    (10, 'Yang LIU', 'femme', NULL),
    (11, 'Andreas MOGENSEN', 'homme', NULL),
    (12, 'Jordan ABID', 'homme', NULL),

    -- AGRICULTEURS
    (13, 'Alan SHEPARD', 'homme', 'Agriculteur'),
    (14, 'John GLENN', 'homme', 'Agriculteur'),
    (15, 'Sally RIDE', 'femme', 'Agriculteur'),
    (16, 'Mae JEMISON', 'femme', 'Agriculteur'),
    (17, 'Tim PEAKE', 'homme', 'Agriculteur'),
    (18, 'Lucas PARMITANO', 'homme', 'Agriculteur'),

    -- CHERCHEURS
    (19, 'Michael COLLINS', 'homme', 'Chercheur'),
    (20, 'Jim LOVELL', 'NB', 'Chercheur'),
    (21, 'Peggy WHITSON', 'femme', 'Chercheur'),
    (22, 'Sunita WILLIAMS', 'femme', 'Chercheur'),
    (23, 'Chris HADFIELD', 'homme', 'Chercheur'),
    (24, 'Claudie HAIGNERÉ', 'femme', 'Chercheur'),

    -- MECANICIENS
    (25, 'Alexei LEONOV', 'homme', 'Mecanicien'),
    (26, 'Sergei KRIKALEV', 'homme', 'Mecanicien'),
    (27, 'Scott KELLY', 'homme', 'Mecanicien'),
    (28, 'Mark KELLY', 'homme', 'Mecanicien'),
    (29, 'Koichi WAKATA', 'NB', 'Mecanicien'),
    (30, 'Akihiko HOSHIDE', 'homme', 'Mecanicien'),

    -- PLOMBIERS
    (31, 'Liwei YANG', 'homme', 'Plombier'),
    (32, 'Junlong FEI', 'homme', 'Plombier'),
    (33, 'Haisheng NIE', 'NB', 'Plombier'),
    (34, 'Soichi NOGUCHI', 'homme', 'Plombier'),
    (35, 'Svetlana SAVITSKAYA', 'femme', 'Plombier'),
    (36, 'Frederick HAISE', 'homme', 'Plombier')
ON DUPLICATE KEY UPDATE id = id;