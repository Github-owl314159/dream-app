-- DB preparation

-- deep_dreams_by_dream table alias 'dbd'

CREATE TABLE dbd(
    id INTEGER PRIMARY KEY,
    dream_id INTEGER,
    ddream_id INTEGER,
    FOREIGN KEY (dream_id)
    REFERENCES dream (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    FOREIGN KEY (ddream_id)
    REFERENCES ddream (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
    );

-- deep_dream table alias 'dd'
CREATE TABLE ddream(
    id INTEGER PRIMARY KEY,
    thumbnail BLOB
    );

-- symbols_by_dream table alias 'sbd'
CREATE TABLE sbd(
    id INTEGER PRIMARY KEY,
    dream_id INTEGER,
    symbol_id INTEGER,
    FOREIGN KEY (dream_id)
    REFERENCES dream (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    FOREIGN KEY (symbol_id)
    REFERENCES symbol (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
    );

SELECT DISTINCT name from symbol, sbd WHERE sbd.dream_id == 1 AND sbd.symbol_id == symbol.id;
INSERT INTO sbd(dream_id, symbol_id) VALUES (1, 1);
INSERT INTO sbd(dream_id, symbol_id) VALUES (1, 2);
INSERT INTO sbd(dream_id, symbol_id) VALUES (1, 3);
INSERT INTO sbd(dream_id, symbol_id) VALUES (1, 8);
INSERT INTO sbd(dream_id, symbol_id) VALUES (2, 69);
INSERT INTO sbd(dream_id, symbol_id) VALUES (2, 8);

-- dream table
CREATE TABLE dream(
    id INTEGER PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    text TEXT NOT NULL,
    date VARCHAR(255) NOT NULL,
    notes TEXT,
    mood VARCHAR(255)
    );

-- insert some values into dream table

INSERT INTO dream VALUES (1, "some title", "some content", "11 August", "some notes", "some mood");
INSERT INTO dream VALUES (2, "some other title", "some other content", "12 Sept", "some other notes", "some other mood");

-- symbol table
CREATE TABLE symbol(id INTEGER PRIMARY KEY, name VARCHAR(255) NOT NULL, description TEXT NOT NULL);

-- insert some values for demo
INSERT INTO symbol (name, description) VALUES ("Abacus", "To see or use an abacus in your dream refers to your outdated views. You have an old fashion perspective on certain issues.");
INSERT INTO symbol (name, description) VALUES ("Abandonment", "To dream that you are abandoned suggests that it is time to leave behind past feelings and characteristics that are hindering your growth. Let go of your old attitudes. A more direct and literal interpretation of this dream indicates you have a fear of being deserted, abandoned, or even betrayed. It may stem from a  recent loss or a fear of losing a loved one. The fear of abandonment may manifest itself into your dream as part of the healing process and dealing with losing a loved one. It may also stem from unresolved feelings or problems from childhood. Alternatively, the dream indicates that you are feeling neglected or that your feelings are being overlooked. Perhaps the dream is a metaphor that you need to approach life with \"reckless abandon\" and live more freely.");
INSERT INTO symbol (name, description) VALUES ("Abyss", "To dream that you are falling into an abyss symbolizes the depths of your subconscious.  You are afraid and/or uncertain as to what you will discover about yourself and about your hidden feelings and fears. The abyss may also represent your primal fears and feelings of \"falling into a pit of despair\". Perhaps you are in a state of depression or wallowing in your negative feelings. Alternatively, the dream could denote your lack of spirituality.");
INSERT INTO symbol (name, description) VALUES ("Backdoor", "To see a backdoor in your dream suggests that you need to search a little harder to find an answer to your problem. Sometimes the solution may not be obvious.  Alternatively, the dream indicates that you are trying to find short cuts to do things.");
INSERT INTO symbol (name, description) VALUES ("Bald", "To dream that you are going bald suggests a lack of self-esteem or worries about getting older. Alternatively, baldness symbolizes humility, purity, and personal sacrifice. You are at a stage in your life where you are confident in fully exposing yourself.");
INSERT INTO symbol (name, description) VALUES ("Barcode", "To see a barcode in your dream symbolizes automation, simplification and ease. Alternatively, the dream represents an impersonal relationship in your waking  life. You are feeling alienated.");
INSERT INTO symbol (name, description) VALUES ("Cabbage", "To see or eat cabbage in your dream suggests that you should not waste time with petty things in your life. You live and thrive on life's challenges. Alternatively, the dream refers to your unwise choices or decisions. You need to rethink some of your decisions.");
INSERT INTO symbol (name, description) VALUES ("Cactus", "To see a cactus in your dream suggests that you are feeling invaded, that your space is being crowded into and that you are being suffocated. The prickly spines of the cactus represent the boundary you are trying to establish between your personal and private. Or you feel the need to defend yourself in some way. Alternatively, the dream implies that you have found yourself in a sticky situation. Perhaps you need to adapt to your existing circumstances instead of trying to change them.");
INSERT INTO symbol (name, description) VALUES ("Castle", "To see a castle in your dream signifies reward, honor, recognition, and praise for your achievements. You are destined to a position of power, wealth, and prestige. Alternatively, the dream indicates your desire to escape from life's daily problems.");
INSERT INTO symbol (name, description) VALUES ("Dachshund", "To see a Dachshund in your dream highlights your loyalty and devotion to others. You are well grounded and rational in your thinking.");
INSERT INTO symbol (name, description) VALUES ("Dead", "To see or talk to the dead in your dream forewarns that you are being influenced by negative people and are hanging around the wrong crowd. This dream may also be a way for you to resolve your feelings with those who have passed on. Alternatively, the dream symbolizes material loss.");
INSERT INTO symbol (name, description) VALUES ("Depression", "To feel depressed in your dream refers to your inability to make connections. You are unable to see the causes of your problems and consequences of your decisions. People who are depressed in their waking life often have dreams about being depressed. Pay attention to what is depressing you in your dream and see how it relates to your waking life.");
INSERT INTO symbol (name, description) VALUES ("Eagles", "To see an eagle in your dream symbolizes nobility, pride, fierceness, freedom, superiority, courage, and powerful intellectual ability. It also represents self-renewal and your connection with your spirituality. You will struggle fiercely and courageously to realize your highest ambitions and greatest desires. Alternatively, if you live in the United States, then the national bird could represent your patriotism and devotion to country.");
INSERT INTO symbol (name, description) VALUES ("Egypt", "To dream of Egypt indicates the roots and core of your own emotions and spirituality. It suggests of a time in life where things may have been simpler.");
INSERT INTO symbol (name, description) VALUES ("Emerald", "To see an emerald in your dream represents strength, longevity, immortality, faithfulness, durability, and fertility. You may be entering the healing stages of some situation.");
INSERT INTO symbol (name, description) VALUES ("Factory", "To dream that you are at a factory represents repetitious thinking and an old way of doing things. It is symbolic of predictability and unchanging habits. Alternatively, it signifies business, productivity, energy and bustling activity. You are a person that can get things done.");
INSERT INTO symbol (name, description) VALUES ("Fear", "To dream that you feel fear indicates that your achievements will not be as successful as you had anticipated. You are experiencing anxieties in various aspects of your life. The key to overcoming your fear is to discuss them and deal with them openly.");
INSERT INTO symbol (name, description) VALUES ("Fever", "To dream that you have a fever suggests that feelings of anger or hatred are threatening to come to your consciousness. You need to find a safe way to express these feelings. Alternatively, the dream may be analogous to blushing and thus represent some embarrassing situation.");
INSERT INTO symbol (name, description) VALUES ("Galaxy", "To see the galaxy in your dream represents your creativity. It also means that you are looking at the broader picture and are more aware of your surroundings.");
INSERT INTO symbol (name, description) VALUES ("Genocide", "To dream about a genocide points to your fear of other people's differences. You are having difficulties accepting others and their differences. Alternatively, the dream is a wake up call about the hostilities in the world.");
INSERT INTO symbol (name, description) VALUES ("Ghost", "To see a ghost in your dream represents something that is no longer obtainable or within reach. It indicates that you are feeling disconnected from life and society. Try to figure out what the ghost wants or what it is looking for. The dream may also be a calling for you to move on and abandon your outdated modes of thinking and behavior.");
INSERT INTO symbol (name, description) VALUES ("Halloween", "To dream of Halloween signifies death and the underworld. Halloween also represents the temporary adoption of a new persona where you feel less inhibited and more comfortable to freely express yourself. You may also be trying to hide your true self. Alternatively, dreaming of Halloween reflects your childhood and the corresponding feelings that you associated with the holiday.");
INSERT INTO symbol (name, description) VALUES ("Haunted House", "To dream of a haunted house signifies unfinished emotional business related to your childhood family, dead relatives, or repressed memories and feelings.");
INSERT INTO symbol (name, description) VALUES ("Headstone", "To see a headstone in your dream represents a forgotten or buried aspect of yourself which you need to acknowledge. Consider also the message on the headstone. It may indicate a statement about your life and its condition.");
INSERT INTO symbol (name, description) VALUES ("Ice", "To see ice in your dream suggests that you are lacking a flow of ideas and thoughts. You are not seeing any progress in your life. Alternatively, the dream implies that you are taking risks that you shouldn't be taking.");
INSERT INTO symbol (name, description) VALUES ("Idols", "To dream that you are worshipping an idol signifies little progress in attaining your goals. You are worshipping false values and ideas.");
INSERT INTO symbol (name, description) VALUES ("Infinity", "To see the infinity symbol in your dream represents time and longevity. Alternatively, it symbolizes wealth or the number 8.");
INSERT INTO symbol (name, description) VALUES ("Jackal", "To see a jackal in your dream refers to manipulation. You have a tendency to feed off of others.");
INSERT INTO symbol (name, description) VALUES ("Jaguar", "To see a jaguar in your dream represents speed, agility, and power.");
INSERT INTO symbol (name, description) VALUES ("Japan", "To dream about Japan or that you are in Japan depends on your own personal associations with the country. If you have never been to Japan, then the dream may represent some sort of realization enlightenment. As the land of the rising sun, Japan symbolizes peace of mind and tranquility.");
INSERT INTO symbol (name, description) VALUES ("Kraken", "To see the Kraken in your dream indicates that all that anger you have suppressed is coming to the surface in a violent way. You are emotionally overwhelmed.");
INSERT INTO symbol (name, description) VALUES ("Knight", "To see a knight in your dream signifies honor, loyalty, protection and security. The knight can be seen as a savior or someone who sweeps you off your feet, as in the \"knight in shining armor\".");
INSERT INTO symbol (name, description) VALUES ("Knife", "To dream that you are carrying a knife signifies anger, aggression and/or separation. There may be something in your life that you need to cut out and get rid of. Perhaps you need to cut ties or sever some relationship. Be more divisive. Alternatively, a knife refers to some sexual tension or sexual confrontation.");
INSERT INTO symbol (name, description) VALUES ("Leaving", "To dream that someone is leaving you refers to feelings of rejection or of not being able to keep up. You are questioning your abilities. Perhaps you are not utilizing your full potential. Alternatively, the dream indicates an end to something. You are ready to let go of the past and move forward.");
INSERT INTO symbol (name, description) VALUES ("Leaves", "To see leaves in your dream signify new found happiness and improvements in various aspects of your life. It is symbolic of fertility, growth and openness. Alternatively, leaves represent a passage of time. Depending on the color and type of leaf, the dream could be highlighting a certain period of time. The leaves may also be a metaphor to \"leave\" you alone.");
INSERT INTO symbol (name, description) VALUES ("Leak", "To see a leak in your dream symbolizes loss, disappointments, frustrations and distress. You are wasting your energy on fruitless endeavors. Alternatively, the dream indicates some repressed feelings emerging from your subconscious or from your past. Metaphorically, the dream may suggest that some secret information has \"leaked\" out.");
INSERT INTO symbol (name, description) VALUES ("Lake", "To see a lake in your dream signifies your emotional state of mind. You feel restricted and that you can't express your emotions freely. Alternatively, the lake may provide you with solace, security, and peace of mind. If the lake is clear and calm, then it symbolizes your inner peace. If the lake is disturbed, then you may be going through some emotional turmoil.");
INSERT INTO symbol (name, description) VALUES ("Mars", "To see Mars in your dream symbolizes energy, drive, passion, fearlessness and ambition. It also represents war, violence and masculine power.");
INSERT INTO symbol (name, description) VALUES ("Marijuana", "To dream that you are using marijuana implies that you are trying to escape reality. Perhaps you are trying to numb some emotional or psychological pain. If someone else is using marijuana or trying to get your to use it, then it indicates a negative influence in your life. You are on the verge of losing control. Perhaps you feel that your identity and sense of self is being compromised or disrespected. The dream may also be a reflection of waking drug use.");
INSERT INTO symbol (name, description) VALUES ("Mango", "To see or eat a mango in your dream symbolizes fertility, sexual desires, and lust. Alternatively, the mango may also be a pun to mean \"man go\" in reference to a relationship in which you should let go and move on.");
INSERT INTO symbol (name, description) VALUES ("New York", "If you do not live in New York, but dream that you are in New York, then it symbolizes your fast paced lifestyle. Perhaps things are moving too fast and you are unable to keep up with the demands of everyday life. Alternatively, the dream represents your desires for more excitement in your life. Or you are striving for success in your professional career.  Known as the big apple, dreaming of New York City could mean that you need to eat a more healthy diet.");
INSERT INTO symbol (name, description) VALUES ("Nebula", "To see a nebula in your dream symbolizes an outburst of creative energy and possibilities.");
INSERT INTO symbol (name, description) VALUES ("Nautilus", "To see a nautilus in your dream reflects your tenacity. You don't let go of what's yours and don't give up. Alternatively, dreaming of a nautilus indicates that you have a good handle of a situation.");
INSERT INTO symbol (name, description) VALUES ("Operation", "To dream that you are having an operation suggests that you need to get something out of your system or cut it out of your life. Perhaps you need to let go of something or change your habits.");
INSERT INTO symbol (name, description) VALUES ("Omen", "To dream about an omen reflects your concerns, fears and/or anxieties about the future. You are worried about some outcome in your waking life and want to control what is happening around you.");
INSERT INTO symbol (name, description) VALUES ("Ocean", "To see an ocean in your dream represents the state of your emotions and feelings. It is indicative of spiritual refreshment, tranquility and renewal. Alternatively, the dream means that you are feeling empowered and unhindered. You have a positive outlook in life and are not limited by anything. If you are sailing across the ocean, then it signifies new found freedom and independence. You are showing great courage. If the ocean is rough, then the dream represents some emotional turmoil. You are doing your best to handle life's ups and downs.");
INSERT INTO symbol (name, description) VALUES ("Package", "To see a package in your dream represents hidden creative energy, skills, and/or feelings. If you receive a package, then it indicates that you are acknowledging certain feelings or acquiring new resources. If you are giving or sending a package, then it suggests that you are projecting your feelings onto another instead of dealing with them.");
INSERT INTO symbol (name, description) VALUES ("Pain", "To dream that you are in pain suggests that you are being too hard on yourself, especially if a situation was out of your control. The dream may also be a true reflection of actual pain that exists somewhere in your body. Dreams can reveal and warn about health problems. Consider where the pain is for additional significance. If the pain is in your neck, then the dream may be a metaphor that you are literally being a \"pain in the neck\".");
INSERT INTO symbol (name, description) VALUES ("Paint", "To see paint in your dream symbolizes expression of your inner emotions. Consider the color of the paint and how the color makes your feel. It is this feeling that you need to express more in your waking life.");
INSERT INTO symbol (name, description) VALUES ("Queen", "To see a queen in your dream symbolizes intuition, personal growth, power and influence. The queen is also a symbol for your mother.");
INSERT INTO symbol (name, description) VALUES ("Quicksand", "To dream that you are sinking in quicksand indicates feelings of insecurity. You have misjudged the solid foundation that you are on. Perhaps you have mistakenly gotten too comfortable in some situation. You need to pay attention to what you are doing and where you are going. Alternatively, the dream is analogous to regressing into your subconscious.");
INSERT INTO symbol (name, description) VALUES ("Quarantine", "To dream that you are placed in quarantine suggests that you need to distance yourself from others or from a situation. You need to alter your actions before you or someone gets hurt.");
INSERT INTO symbol (name, description) VALUES ("Rabbit", "To see a rabbit in your dream signifies luck, magical power, and success. You have a positive outlook on life. Alternatively, rabbits symbolize abundance, warmth, fertility and sexual activity. Perhaps your sex life needs to be kept in check. The dream can also be associated with Easter time and your own personal memories of Easter.");
INSERT INTO symbol (name, description) VALUES ("Racoon", "To see a raccoon in your dream signifies deceit and thievery. You are not being completely honest in some situation. Alternatively, the dream suggests that you are hiding something. You are keeping a secret.");
INSERT INTO symbol (name, description) VALUES ("Referee", "To see a referee in your dream signifies an inner battle between your own ideals and values and between the ideals and values of others. You are looking for a resolution to some conflict in your daily life.");
INSERT INTO symbol (name, description) VALUES ("Seashells", "To see seashells in your dream represent security and protection. You are not showing your true self or real feelings. In protecting yourself from getting hurt, you are also becoming reclusive and emotionally closed off.");
INSERT INTO symbol (name, description) VALUES ("Seagulls", "To see seagulls in your dream indicate a desire to get away from your problems or the demands of your walking life. You may be wasting away your potential and unused skills. Alternatively, a seagull symbolizes your strengths. You are able to cope with life's changes with grace and understanding. The dream may also be a pun on \"see go\" and thus indicate something that you need to let go or see go.");
INSERT INTO symbol (name, description) VALUES ("Sea", "To see the sea in your dream represents your subconscious and the transition between your subconscious and conscious. As with all water symbol, it also represents your emotions. The dream may also be a pun on your understanding and perception of a situation. \"I see\" or perhaps there is something you need to \"see\" more clearly. Alternatively, the dream indicates a need to reassure yourself or to offer reassurance to someone. It brings about hope, a new perspective and a positive outlook on life no matter how difficult your current problems may be.");
INSERT INTO symbol (name, description) VALUES ("Tennis", "To dream that you are playing tennis represents changes or challenges in your life. You need to actively assert yourself and prove yourself time and time again. Alternatively, playing or watching tennis indicates that you are unable to commit to a situation or decision. You are literally going back and forth between two choices. Perhaps the dream is trying to tell you that \"the ball is in your court.\" It is your turn to make the next move.");
INSERT INTO symbol (name, description) VALUES ("Temple", "To see a temple in your dream represents inspiration, spiritual thinking, meditation and growth. It is also symbolic of your physical body and the attention you give it. Perhaps you need to pamper yourself. Alternatively, the dream suggests that you are looking for a place of refuge and a place to keep things that are dear to you.");
INSERT INTO symbol (name, description) VALUES ("Television", "To dream that you are watching television represents your mind and its flowing thoughts. The dream reflects how you are receiving, integrating, and expressing your ideas and thoughts. The programs you dream of watching are an objective view of the things that are in your mind.");
INSERT INTO symbol (name, description) VALUES ("Universe", "To see the universe in your dream signifies the endless possibilities. You need to look at the overall big picture. Alternatively, the dream brings to your attention that we are all interconnected in some way.");
INSERT INTO symbol (name, description) VALUES ("Unicorn", "To see a unicorn in your dream symbolizes high ideals, hope and insight in a current situation. It also symbolizes power, gentleness, and purity. Alternatively, it may represent your one-sided views.");
INSERT INTO symbol (name, description) VALUES ("Undead", "To see the undead in your dream represents your fears and the rejected aspects of yourself. You are refusing to acknowledge those negative parts. If you are being chased or are surrounded by the undead, then it symbolizes unresolved issues that you are not confronting. Things that you thought or assume were put to rest is coming back to haunt you.");
INSERT INTO symbol (name, description) VALUES ("Vomiting", "To dream that you are vomiting indicates that you need to reject or discard an aspect of your life that is revolting. There are some emotions or concepts that you need to confront and then let go.");
INSERT INTO symbol (name, description) VALUES ("Volcano", "To see a volcano in your dream indicates that you are unable to control your emotions, particularly if the volcano is erupting. You are ready to burst. The outcome may be damaging and hurtful, especially to those around you. If the volcano is dormant, then it represents past issues that have been resolved and put to the rest.");
INSERT INTO symbol (name, description) VALUES ("Virus", "To dream that a computer has a virus or has crashed suggests that something in your life that is out of control. It may parallel something in your life that has come to a crashing end. If you dream that your computer has a sentient computer virus that would create random or strange artwork, then it depicts your subconscious desires. The artwork may also be a reflection of a situation in your life that you are ignoring. In particular, if the artwork is of a little boy smearing X's with his poop on a calendar, then it indicates past regrets or remorse.");
INSERT INTO symbol (name, description) VALUES ("Water", "To see water in your dream symbolizes your subconscious and your emotional state of mind. Water is the living essence of the psyche and the flow of life energy. It is also symbolic of spirituality, knowledge, healing and refreshment. To dream that water is boiling suggests that you are expressing some emotional turmoil. Feelings from your subconscious are surfacing and ready to be acknowledged. You need to let out some steam.");
INSERT INTO symbol (name, description) VALUES ("Web", "To see a web in your dream represents your desire to control everything around you. Alternatively, it suggests that you are being held back from fully expressing yourself. You feel trapped and do not know what to do or where to go. The dream may also be symbolic of your social network of acquaintances and associates or it may represent the world wide web.");
INSERT INTO symbol (name, description) VALUES ("Werewolf", "To see a werewolf in your dream indicates that something in your life is not what it seems. It is symbolic of fear, repressed anger, and uncontrollable violence.");
INSERT INTO symbol (name, description) VALUES ("X-Ray", "To dream that you are being x-rayed denotes that you are being deceived by a person or situation. You need to look beneath the surface of the person or situation. On the other hand, the dream means that you are working through a problem or issue that has been troubling you.");
INSERT INTO symbol (name, description) VALUES ("Xylophone", "To see or play a xylophone in your dream indicates concerns for the environment. You need to be more environmentally conscious. Alternatively, the dream represents your ambition and your drive. It also provides motivation and inspirational insight into the future.");
INSERT INTO symbol (name, description) VALUES ("Yeti", "To see a yeti in your dream suggests that you need to learn to find balance between your reasonable, rational side and your emotional, instinctual nature.");
INSERT INTO symbol (name, description) VALUES ("Yin Yang", "To see the yin yang in your dream symbolizes a balance of opposites. It represents the feminine and the masculine, the spiritual and the physical, and the emotional and the rational.");
INSERT INTO symbol (name, description) VALUES ("Yoga", "To dream that you are performing yoga symbolizes unity, balance, harmony, calmness, and self-discipline. You have complete control of your mind and body. Alternatively, the dream indicates that you are stressed and overwhelmed. You need to take a breather.");
INSERT INTO symbol (name, description) VALUES ("Zebra", "To see a zebra in your dream represents perfect balance, unity and harmony. It also indicates that opposites attract. Alternatively, a zebra suggests that you are spending too much time in trivial matters. You need to establish a clear timeline and lay your groundwork for success.");
INSERT INTO symbol (name, description) VALUES ("Zeus", "To see or dream that you are Zeus suggests that you have total control over your life. You are looking for better control.");
INSERT INTO symbol (name, description) VALUES ("Zombie", "To see or dream that you are a zombie suggests that you are physically and/or emotionally detached from people and situations that are currently surrounding you. You are feeling out of touch. Alternatively, a zombie means that you are feeling dead inside. You are just going through the motions of daily living.");