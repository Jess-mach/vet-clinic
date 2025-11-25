-- Migration: Insert base data for animals and consultations
-- Date: 2025-11-25
-- Description: Inserts 120 animals (Dog, Cat, Bird, Rabbit) and 600 consultations
--              with consultation dates after 2025-01-01 for realistic history data.

-- ================================
-- Insert 120 animals
-- ================================
-- Distribution by species:
--  - 60 Dogs
--  - 40 Cats
--  - 15 Birds
--  - 5 Rabbits

WITH base_animals AS (
    SELECT
        gs AS id,
        CASE
            WHEN gs <= 60 THEN 'Dog'
            WHEN gs <= 100 THEN 'Cat'
            WHEN gs <= 115 THEN 'Bird'
            ELSE 'Rabbit'
        END AS species
    FROM generate_series(1, 120) AS gs
),
animal_data AS (
    SELECT
        -- Pet names (simple e compostos) baseados na lista fornecida
        ARRAY[
            'Abel', 'Abelardo', 'Açaí', 'Adalberto', 'Afonso', 'Agnes', 'Aipim', 'Akira', 'Aladdin', 'Alecrim',
            'Alfredo', 'Alice', 'Amora', 'Ana Banana', 'Ana Clara', 'Ana Júlia', 'Ana Maria', 'Angel', 'Angelina', 'Anita',
            'Apolo', 'Aquiles', 'Aragorn', 'Ariel', 'Arthur', 'Arya', 'Astro', 'Atena', 'Athos', 'Aurora',
            'Babalu', 'Bacon', 'Baguera', 'Baloo', 'Balu', 'Bambam', 'Bambi', 'Bandido', 'Barbie', 'Barão Vermelho',
            'Bart', 'Batata Doce', 'Batman', 'Beethoven', 'Bela', 'Belinha', 'Bella', 'Bento', 'Bernardo', 'Biscoito',
            'Bisteca', 'Bob Esponja', 'Bob Marley', 'Boby', 'Bolacha', 'Bolinha', 'Bolt', 'Bombom', 'Branca de Neve', 'Brigadeiro',
            'Brisa', 'Brownie', 'Bruce', 'Brutus', 'Bubaloo', 'Bubu', 'Buddy', 'Cacau', 'Cachaça', 'Café',
            'Caju', 'Canela', 'Capitão Gancho', 'Caramelo', 'Carmen Miranda', 'Catatau', 'Chanel', 'Charlie', 'Chewbacca', 'Chiclete',
            'Chico Bento', 'Chiquinha', 'Chocolate', 'Chokito', 'Cindy', 'Clarabela', 'Clark Kent', 'Cléo', 'Cocada', 'Cookie',
            'Costelinha', 'Coxinha', 'Cupcake', 'Daisy', 'Dandara', 'Darth Vader', 'Dobby', 'Dom Casmurro', 'Dom Pedro', 'Dona Benta',
            'Dona Florinda', 'Duque', 'Duquesa', 'Einstein', 'Elvis Presley', 'Estrela', 'Farofa', 'Faísca', 'Feijão', 'Fiona',
            'Floquinho', 'Flora', 'Fofo', 'Fofão', 'Foguinho', 'Frida', 'Frida Kahlo', 'Fumaça', 'Fuscão', 'Gael',
            'Galileu', 'Gandhi', 'Garfield', 'Gato de Botas', 'Gengibre', 'Goku', 'Gold', 'Golias', 'Gordo', 'Grazi',
            'Gucci', 'Guga', 'Hachi', 'Hanna', 'Happy', 'Haroldo', 'Harry Potter', 'Hércules', 'Hermione', 'Hobbit',
            'Homer', 'Honey', 'Hulk', 'Indiana Jones', 'Iogurte', 'Iolanda', 'Ivy', 'Jack Sparrow', 'Jambo', 'Janis',
            'Jaspion', 'Jazz', 'Jedi', 'Jerry', 'Jiji', 'Joey', 'John Lennon', 'Jojo', 'Joker', 'Jolie',
            'Juju', 'Jujuba', 'Julieta', 'Jumanji', 'Junior', 'Juno', 'Kaiser', 'Kiara', 'Kinder', 'King',
            'Kira', 'Klaus', 'Koda', 'Krypto', 'Kung Fu', 'Kyara', 'Lady Gaga', 'Laika', 'Lampião', 'Lancelot',
            'Lara Croft', 'Lassie', 'Leão', 'Led', 'Legolas', 'Leia', 'Lennon', 'Leo', 'Lilo', 'Lilo Stitch',
            'Lilica', 'Linda', 'Link', 'Lobo Mau', 'Loki', 'Lola', 'Lolita', 'Lord', 'Lorde', 'Luca',
            'Lucky', 'Luigi', 'Luke Skywalker', 'Luna', 'Lupita', 'Luther', 'Mabel', 'Macarrão', 'Madalena', 'Madonna',
            'Mafalda', 'Magali', 'Maggie', 'Maia', 'Malu', 'Mané', 'Manolo', 'Manu', 'Maracujá', 'Maria Bonita',
            'Maria Cecília', 'Maria Clara', 'Maria Eduarda', 'Maria Flor', 'Maria Joaquina', 'Maria Mole', 'Marley', 'Marshmallow', 'Martin', 'Matilda',
            'Max', 'Maya', 'Mel', 'Melancia', 'Melissa', 'Merida', 'Mickey Mouse', 'Milka', 'Milkshake', 'Mimosa',
            'Mingau', 'Minie', 'Minnie', 'Moana', 'Mogli', 'Monalisa', 'Monica', 'Monkey', 'Moon', 'Morgana',
            'Morpheu', 'Morticia', 'Mozart', 'Mufasa', 'Mulher Maravilha', 'Mulan', 'Murphy', 'Mustafá', 'Nala', 'Napoleão',
            'Narizinho', 'Negresco', 'Neve', 'Newton', 'Nick', 'Nhoque', 'Nutella', 'Obi-Wan', 'Odin', 'Olaf',
            'Oliver', 'Olivia Palito', 'Onix', 'Oreo', 'Oscar', 'Ozzy Osbourne', 'Paçoca', 'Pac-Man', 'Panda', 'Pandora',
            'Pantera Cor de Rosa', 'Papa Léguas', 'Pato Donald', 'Paul McCartney', 'Pé de Pano', 'Penélope Charmosa', 'Phoebe', 'Phoenix', 'Picles', 'Pimenta',
            'Pingo', 'Pink', 'Pipoca', 'Pirata', 'Piu-Piu', 'Pixel', 'Pizza', 'Pluto', 'Pocahontas', 'Polenta',
            'Polly', 'Pompom', 'Pongo', 'Popeye', 'Potter', 'Preta', 'Pretinho', 'Princesa Leia', 'Pudim', 'Pumba',
            'Quico', 'Quindim', 'Rabito', 'Rambo', 'Rapunzel', 'Ratinho', 'Rei Arthur', 'Rei Leão', 'Rex', 'Rihanna',
            'Ringo Starr', 'Rita Lee', 'Robin Hood', 'Rocky Balboa', 'Romeu', 'Rosinha', 'Ross', 'Roxy', 'Ruffles', 'Safira',
            'Salsicha', 'Samba', 'Sansão', 'Scooby Doo', 'Scott', 'Shakira', 'Shazam', 'She-Ra', 'Sherlock Holmes', 'Shrek',
            'Simba', 'Sininho', 'Skate', 'Snoopy', 'Snow', 'Sofia', 'Sol', 'Sombra', 'Sonic', 'Soneca',
            'Sophie', 'Spock', 'Sr. Gato', 'Stark', 'Stella', 'Stuart Little', 'Suflair', 'Sushi', 'Super Homem', 'T-Rex',
            'Tico e Teco', 'Taz', 'Ted', 'Tequila', 'Teodoro', 'Tiana', 'Tigrão', 'Timão', 'Tinker Bell', 'Tio Patinhas',
            'Titan', 'Toddy', 'Tom Jobim', 'Tommy', 'Tony Stark', 'Topázio', 'Totó', 'Trovão', 'Trufa', 'Tuca',
            'Tulipa', 'Tupã', 'Tutti-Frutti', 'Twister', 'Tyson', 'Ulisses', 'Ursinho Pooh', 'Vader', 'Valentina', 'Valente',
            'Vanilla', 'Vênus', 'Vingador', 'Vinícius', 'Violeta', 'Vitório', 'Vodka', 'Waffle', 'Wally', 'Walter',
            'Whisky', 'Whoopi', 'Willow', 'Winnie', 'Wolf', 'Wolverine', 'Woody', 'Xena', 'Xico', 'Xuxa',
            'Yasmin', 'Yellow', 'Yoda', 'Yoshi', 'Yuki', 'Yumi', 'Zeca', 'Zelda', 'Zen', 'Zeus',
            'Zé Carioca', 'Zé Colmeia', 'Zé Pequeno', 'Zé Gatinho', 'Zica', 'Ziggy', 'Zoe', 'Zorro', 'Zuleica', 'Zulu'
        ] AS pet_names,

        -- Nomes de tutores brasileiros
        ARRAY[
            'João Silva', 'Maria Oliveira', 'Carlos Santos', 'Ana Paula Souza', 'Pedro Almeida',
            'Mariana Costa', 'Lucas Pereira', 'Fernanda Rodrigues', 'Rafael Lima', 'Camila Ribeiro',
            'Bruno Carvalho', 'Patrícia Fernandes', 'Gustavo Gomes', 'Juliana Araújo', 'Felipe Barros',
            'Larissa Melo', 'André Castro', 'Beatriz Martins', 'Diego Teixeira', 'Aline Duarte',
            'Rodrigo Rocha', 'Carolina Nogueira', 'Eduardo Monteiro', 'Letícia Freitas', 'Marcelo Cardoso',
            'Vanessa Correia', 'Tiago Moraes', 'Natália Batista', 'Fábio Moreira', 'Priscila Antunes',
            'Henrique Campos', 'Luciana Pires', 'Daniel Souza', 'Renata Farias', 'Sérgio Tavares',
            'Tatiane Cunha', 'Igor Vieira', 'Vivian Barros', 'César Almeida', 'Roberta Mello',
            'Vinícius Prado', 'Bianca Rezende', 'Alexandre Coutinho', 'Daniele Siqueira', 'Hugo Peixoto',
            'Simone Brito', 'Ricardo Sales', 'Érika Cavalcante', 'Rogério Braga', 'Cláudia Silveira',
            'Márcio Queiroz', 'Adriana Magalhães', 'Leandro Moraes', 'Carol Souza', 'Talita Lopes',
            'Joana Campos', 'Paulo Henrique', 'Rafaela Dias', 'Caio Gonçalves', 'Sabrina Alves'
        ] AS owner_names,

        -- Breeds by species
        ARRAY[
            'Golden Retriever', 'Labrador Retriever', 'German Shepherd', 'Bulldog', 'Beagle',
            'Rottweiler', 'Poodle', 'Doberman', 'Shih Tzu', 'Siberian Husky',
            'Chihuahua', 'Boxer', 'Cocker Spaniel', 'Border Collie', 'Yorkshire Terrier',
            'Schnauzer', 'French Bulldog', 'Dalmatian', 'Basset Hound', 'Maltese',
            'Australian Shepherd', 'Great Dane', 'Pomeranian', 'Saint Bernard', 'Shiba Inu',
            'Weimaraner', 'Cavalier King Charles Spaniel', 'Bernese Mountain Dog',
            'West Highland White Terrier', 'Jack Russell Terrier'
        ] AS dog_breeds,
        ARRAY[
            'Persian', 'Siamese', 'Maine Coon', 'British Shorthair', 'Bengal',
            'Ragdoll', 'Scottish Fold', 'Russian Blue', 'American Shorthair', 'Norwegian Forest',
            'Sphynx', 'Abyssinian', 'Turkish Angora', 'Exotic Shorthair', 'Oriental Shorthair',
            'Cornish Rex', 'Devon Rex', 'Himalayan', 'Manx', 'Birman'
        ] AS cat_breeds,
        ARRAY[
            'Cockatiel', 'Parrot', 'Canary', 'Budgerigar', 'Lovebird',
            'Macaw', 'Cockatoo', 'Finch', 'Parakeet'
        ] AS bird_breeds,
        ARRAY[
            'Holland Lop', 'Flemish Giant', 'Rex', 'Dutch', 'Angora'
        ] AS rabbit_breeds,

        -- Generic colors
        ARRAY[
            'Brown', 'Black', 'White', 'Golden', 'Gray',
            'Tricolor', 'Orange and White', 'Black and White', 'Brindle', 'Cream and Brown'
        ] AS colors
)
INSERT INTO animals (
    id,
    name,
    species,
    breed,
    gender,
    birth_date,
    color,
    weight,
    microchip_number,
    owner_name,
    owner_phone,
    owner_email,
    created_at,
    updated_at,
    is_active,
    inactivated_at
)
SELECT
    b.id,
    ad.pet_names[((b.id - 1) % array_length(ad.pet_names, 1)) + 1] AS name,
    b.species,
    CASE b.species
        WHEN 'Dog' THEN ad.dog_breeds[((b.id - 1) % array_length(ad.dog_breeds, 1)) + 1]
        WHEN 'Cat' THEN ad.cat_breeds[((b.id - 1) % array_length(ad.cat_breeds, 1)) + 1]
        WHEN 'Bird' THEN ad.bird_breeds[((b.id - 1) % array_length(ad.bird_breeds, 1)) + 1]
        ELSE ad.rabbit_breeds[((b.id - 1) % array_length(ad.rabbit_breeds, 1)) + 1]
    END AS breed,
    CASE WHEN (b.id % 2) = 0 THEN 'Male' ELSE 'Female' END AS gender,
    DATE '2020-01-01' + (b.id % 1460) AS birth_date, -- spread between 2020 and ~2023
    ad.colors[((b.id - 1) % array_length(ad.colors, 1)) + 1] AS color,
    CASE b.species
        WHEN 'Dog' THEN (5.0 + (b.id % 40))::DECIMAL(5,2)
        WHEN 'Cat' THEN (3.0 + (b.id % 5))::DECIMAL(5,2)
        WHEN 'Bird' THEN (0.10 + (b.id % 3) * 0.05)::DECIMAL(5,2)
        ELSE (1.50 + (b.id % 4) * 0.50)::DECIMAL(5,2)
    END AS weight,
    format('CHIP%06s', b.id::text) AS microchip_number,
    ad.owner_names[((b.id - 1) % array_length(ad.owner_names, 1)) + 1] AS owner_name,
    format('(11) 9%07s', lpad(b.id::text, 7, '0')) AS owner_phone,
    lower(
        replace(
            ad.owner_names[((b.id - 1) % array_length(ad.owner_names, 1)) + 1],
            ' ',
            '.'
        )
    ) || '@email.com' AS owner_email,
    TIMESTAMP '2024-12-01 10:00:00' + (b.id % 30) * INTERVAL '1 day' AS created_at,
    TIMESTAMP '2024-12-01 10:00:00' + (b.id % 30) * INTERVAL '1 day' AS updated_at,
    TRUE AS is_active,
    NULL::TIMESTAMP AS inactivated_at
FROM base_animals b
CROSS JOIN animal_data ad;

-- Ensure animals sequence is aligned with inserted IDs
SELECT setval('animals_id_seq', 500, true);


-- ================================
-- Insert 600 consultations
-- ================================
-- All consultation_date values are strictly greater than 2025-01-01.
-- Uses existing veterinarians (IDs 1..15) created in previous migration.

WITH consultation_base AS (
    SELECT
        gs AS seq,
        ((gs - 1) % 120) + 1 AS animal_id,
        ((gs - 1) % 15) + 1 AS veterinarian_id,
        TIMESTAMP '2025-01-02 09:00:00' + (gs - 1) * INTERVAL '8 hours' AS consultation_date,
        CASE
            WHEN (gs % 10) IN (1, 2, 3, 4, 5) THEN 1   -- GENERAL_CHECKUP (majority)
            WHEN (gs % 10) IN (6, 7) THEN 8           -- VACCINATION
            WHEN (gs % 10) = 8 THEN 9                 -- SURGERY-related
            WHEN (gs % 10) = 9 THEN 10                -- FOLLOW_UP
            ELSE 6                                    -- EXAMS
        END AS reason_code
    FROM generate_series(1, 600) AS gs
)
INSERT INTO consultations (
    animal_id,
    veterinarian_id,
    consultation_date,
    reason_code,
    description,
    diagnosis,
    treatment_prescribed,
    observations,
    next_appointment_date,
    status,
    created_at,
    updated_at
)
SELECT
    cb.animal_id,
    cb.veterinarian_id,
    cb.consultation_date,
    cb.reason_code,
    CASE cb.reason_code
        WHEN 1 THEN 'Consulta de rotina / check-up geral.'
        WHEN 6 THEN 'Consulta para realização de exames laboratoriais.'
        WHEN 8 THEN 'Consulta para vacinação e revisão clínica rápida.'
        WHEN 9 THEN 'Consulta relacionada a avaliação pré ou pós-cirúrgica.'
        WHEN 10 THEN 'Consulta de retorno para acompanhamento de tratamento.'
        ELSE 'Consulta clínica geral com avaliação completa.'
    END AS description,
    CASE cb.reason_code
        WHEN 1 THEN 'Exame clínico dentro da normalidade, sem alterações significativas.'
        WHEN 6 THEN 'Solicitados exames laboratoriais básicos para monitoramento.'
        WHEN 8 THEN 'Paciente em boas condições gerais para receber vacina.'
        WHEN 9 THEN 'Quadro compatível com necessidade de acompanhamento cirúrgico.'
        WHEN 10 THEN 'Evolução satisfatória em relação ao quadro anterior.'
        ELSE 'Avaliação clínica compatível com quadro leve, sem riscos imediatos.'
    END AS diagnosis,
    CASE cb.reason_code
        -- GENERAL_CHECKUP
        WHEN 1 THEN 'Prescrito suplemento polivitamínico VetMulti Pet 500 UI, 1 comprimido ao dia por 30 dias, associado a dieta balanceada SuperPet Diet Light.'
        -- EXAMS
        WHEN 6 THEN 'Solicitado uso de contraste oral GastroView Pet 20 mL único para exames de imagem, manter hidratação com SoluVet Hidra 100 mL via oral ao dia.'
        -- VACCINATION
        WHEN 8 THEN 'Aplicada vacina fictícia ImunoPet V10 1 dose SC, com prescrição de analgésico leve AnalgoPet 25 mg se houver dor, a cada 12h por até 2 dias.'
        -- SURGERY
        WHEN 9 THEN 'Pós-operatório: Antibiótico AmoxiVet Plus 150 mg a cada 12h por 10 dias, anti-inflamatório FlogVet 20 mg a cada 24h por 5 dias e protetor gástrico GastroPet 5 mg a cada 24h.'
        -- FOLLOW_UP
        WHEN 10 THEN 'Manter uso de CondroFlex Pet 500 mg ao dia por 60 dias, podendo associar AnalgoPet 25 mg se houver dor, máximo 2 vezes ao dia.'
        -- DEFAULT / OUTROS
        ELSE 'Prescrita solução otológica OtoClean Pet 5 gotas em cada orelha 2x ao dia por 7 dias e limpeza com Dermasept Pet Spray 2x ao dia na região afetada.'
    END AS treatment_prescribed,
    'Registro gerado para massa de dados de histórico (uso interno SysCecilia).' AS observations,
    CASE
        WHEN (cb.seq % 4) = 0 THEN cb.consultation_date + INTERVAL '30 days'
        ELSE NULL
    END AS next_appointment_date,
    'COMPLETED' AS status,
    cb.consultation_date AS created_at,
    cb.consultation_date AS updated_at
FROM consultation_base cb;

-- Align consultations sequence with current max ID
SELECT setval(
    'consultations_id_seq',
    COALESCE((SELECT MAX(id) FROM consultations), 0),
    TRUE
);

