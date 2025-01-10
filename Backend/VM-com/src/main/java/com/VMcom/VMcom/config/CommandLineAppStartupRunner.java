package com.VMcom.VMcom.config;

import com.VMcom.VMcom.enums.AppUserRole;
import com.VMcom.VMcom.model.*;
import com.VMcom.VMcom.repository.AppUserRepository;
import com.VMcom.VMcom.repository.ProductCategoryRepository;
import com.VMcom.VMcom.repository.ProductRepository;
import com.VMcom.VMcom.repository.ShopCartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Component
@RequiredArgsConstructor
public class CommandLineAppStartupRunner implements CommandLineRunner {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductRepository productRepository;
    private final ShopCartRepository shopCartRepository;

    @Override
    public void run(String... args) throws Exception {


        //Add admin user
        List<ShopCartLine> shopCartLinesForAdmin = new ArrayList<>();
        ShopCart shopCartForAdmin = new ShopCart();
        shopCartForAdmin.setShopCardLines(shopCartLinesForAdmin);
        shopCartForAdmin = shopCartRepository.save(shopCartForAdmin);

        AppUser admin = new AppUser(
                "Admin",
                "",
                "admin@mail.com",
                passwordEncoder.encode("password"),
                AppUserRole.ROLE_ADMIN,
                false,
                true,
                "+48 223-254-525");
        
        admin = appUserRepository.save(admin);
        
        shopCartForAdmin.setAppUser(admin);
        shopCartRepository.save(shopCartForAdmin);

        

        //Add user
        List<ShopCartLine> shopCartLinesForUser = new ArrayList<>();
        ShopCart shopCartForUser = new ShopCart();
        shopCartForUser.setShopCardLines(shopCartLinesForUser);


        AppUser user = new AppUser(
                "User",
                "",
                "user@mail.com",
                passwordEncoder.encode("password"),
                AppUserRole.ROLE_USER,
                false,
                true,
                "+48 223-254-525");
       
        user = appUserRepository.save(user);
        shopCartForUser.setAppUser(user);
        shopCartRepository.save(shopCartForUser);



//Add test product category
        ProductCategory productCategory1 = productCategoryRepository.save(new ProductCategory("Laptopy"));
        ProductCategory productCategory2 = productCategoryRepository.save(new ProductCategory("Słuchawki"));
        ProductCategory productCategory3 = productCategoryRepository.save(new ProductCategory("Tablety"));
        ProductCategory productCategory4 = productCategoryRepository.save(new ProductCategory("Smartwatche"));
        ProductCategory productCategory5 = productCategoryRepository.save(new ProductCategory("Monitory"));
        ProductCategory productCategory6 = productCategoryRepository.save(new ProductCategory("Smartfony"));
        ProductCategory productCategory7 = productCategoryRepository.save(new ProductCategory("Gaming"));
        ProductCategory productCategory8 = productCategoryRepository.save(new ProductCategory("TV"));
        ProductCategory productCategory9 = productCategoryRepository.save(new ProductCategory("Akcesoria"));

//Add products
        productRepository.save(new Product(
                "LG Ultragear 27GP850P NanoIPS HDR",
                "Zachwycający design oraz niezwykle bogata funkcjonalność – to czyni z monitora LG 27GP850P-B narzędzie, dzięki któremu odkryjesz gaming na nowo.\n" +
                        "Solidna konstrukcja połączona z panelem Nano IPS WQHD oferuje najlepsze doznania z gry w każdym calu.\n" +
                        "Bogate kolory, najdrobniejsze szczegóły i niezwykle szybki czas reakcji to cechy, dzięki którym odniesiesz sukces na wirtualnych polach bitwy.\n" +
                        "Poznaj gamingowy monitor LG 27GP850P-B.",
                1199.00,
                Arrays.asList("lg1.jpg", "lg2.jpg", "lg3.jpg", "lg4.jpg", "lg5.jpg", "lg6.jpg", "lg7.jpg"),
                0,
                2L,
                productCategory5,
                ""
        ));

        productRepository.save(new Product(
                "Nothing Phone (2) - 256 GB + 12 GB",
                "Odkryj rewolucyjny smartfon Nothing Phone (2) - połączenie doskonałej wydajności, wspaniałego wyświetlacza i innowacyjnych funkcji.\n" +
                        "Dzięki układowi Snapdragon® 8+ Gen 1, doświadczysz niezrównanej mocy i szybkości działania.\n" +
                        "Imponujący wyświetlacz OLED o rozmiarze 6,7 cala i jasności pikseli sięgającej 1600 nitów zapewnia niesamowitą jakość obrazu.",
                2499.00,
                Arrays.asList("nothing1.jpg", "nothing2.jpg", "nothing3.jpg", "nothing4.jpg", "nothing5.jpg", "nothing6.jpg", "nothing7.jpg"),
                0,
                2L,
                productCategory6,
                ""
        ));

        productRepository.save(new Product(
                "Apple MacBook Pro 14 M2",
                "Apple MacBook Pro 14 M2 to zaawansowany laptop stworzony z myślą o profesjonalistach poszukujących najwyższej wydajności i doskonałej jakości wykonania.\n" +
                        "Wyposażony w najnowszy procesor Apple M2, 16 GB pamięci RAM oraz 512 GB SSD, oferuje błyskawiczne ładowanie aplikacji oraz płynne działanie nawet podczas najbardziej wymagających zadań.\n" +
                        "Ekran Retina o rozdzielczości 3024x1964 z technologią ProMotion i jasnością do 1000 nitów zapewnia wyjątkową ostrość i bogate kolory, które ożywią Twoje projekty.\n" +
                        "Obudowa z aluminium nie tylko prezentuje się elegancko, ale również zapewnia trwałość i solidność.\n" +
                        "Idealny wybór dla grafików, inżynierów i każdego, kto potrzebuje sprzętu do intensywnej pracy twórczej.",
                10999.00,
                Arrays.asList("apple1.jpg", "apple2.jpg", "apple3.jpg"),
                0,
                5L,
                productCategory1,
                ""
        ));

        productRepository.save(new Product(
                "Samsung Galaxy Tab S9 Ultra",
                "Samsung Galaxy Tab S9 Ultra to tablet, który redefiniuje standardy mobilnej wydajności.\n" +
                        "Wyposażony w procesor Snapdragon 8 Gen 2 i aż 16 GB pamięci RAM, ten potężny sprzęt radzi sobie z każdą aplikacją i grą.\n" +
                        "Gigantyczny wyświetlacz AMOLED o przekątnej 14,6 cala i rozdzielczości 2960x1848 oferuje niesamowite kolory oraz szczegółowość obrazu.\n" +
                        "Dzięki baterii o pojemności 11200 mAh możesz cieszyć się długim czasem pracy na jednym ładowaniu.\n" +
                        "Idealny wybór dla artystów, profesjonalistów i entuzjastów multimediów.",
                5799.00,
                Arrays.asList("samsung1.jpg", "samsung2.jpg", "samsung3.jpg"),
                0,
                5L,
                productCategory2,
                ""
        ));

        productRepository.save(new Product(
                "Sony WH-1000XM5",
                "Sony WH-1000XM5 to bezprzewodowe słuchawki, które oferują niesamowitą jakość dźwięku oraz jedną z najlepszych redukcji szumów na rynku.\n" +
                        "Dzięki przetwornikom o średnicy 30 mm oraz obsłudze Hi-Res Audio, usłyszysz każdy detal swojej ulubionej muzyki.\n" +
                        "Wygodne nauszniki i długi czas pracy na baterii (do 30 godzin) sprawiają, że są one idealnym wyborem na każdą podróż.\n" +
                        "Funkcje takie jak Adaptive Sound Control czy Speak-to-Chat dodatkowo podnoszą komfort użytkowania.",
                1699.00,
                Arrays.asList("sony1.jpg", "sony2.jpg", "sony3.jpg", "sony4.jpg"),
                0,
                2L,
                productCategory3,
                ""
        ));

        productRepository.save(new Product(
                "Dell XPS 15",
                "Dell XPS 15 to laptop stworzony dla osób, które potrzebują narzędzia do pracy i rozrywki w jednym urządzeniu.\n" +
                        "Wyposażony w procesor Intel Core i7-12700H, kartę graficzną NVIDIA RTX 3050 oraz 16 GB RAM, zapewnia wydajność wystarczającą do edycji wideo, projektowania graficznego oraz grania.\n" +
                        "Ekran InfinityEdge o rozdzielczości 3840x2400 zapewnia doskonałą jakość obrazu i szerokie kąty widzenia.\n" +
                        "Stylowy design, wysoka jakość wykonania oraz długi czas pracy na baterii sprawiają, że Dell XPS 15 to laptop premium w każdym calu.",
                8999.00,
                Arrays.asList("dell1.jpg", "dell2.jpg", "dell3.jpg"),
                0,
                5L,
                productCategory4,
                ""
        ));

        productRepository.save(new Product(
                "PlayStation 5 Digital Edition",
                "PlayStation 5 Digital Edition to konsola nowej generacji, która oferuje wyjątkową grafikę, błyskawiczne czasy ładowania oraz bogatą bibliotekę ekskluzywnych gier.\n" +
                        "Wyposażona w procesor AMD Ryzen Zen 2 oraz kartę graficzną RDNA 2, konsola zapewnia rozdzielczość 4K oraz płynność do 120 klatek na sekundę.\n" +
                        "Dysk SSD o pojemności 825 GB gwarantuje szybki dostęp do gier i aplikacji, a minimalistyczny design świetnie komponuje się z każdym wnętrzem.\n" +
                        "Doskonały wybór dla graczy, którzy stawiają na cyfrową dystrybucję gier.",
                2399.00,
                Arrays.asList("ps51.jpg", "ps52.jpg", "ps53.jpg"),
                0,
                2L,
                productCategory5,
                ""
        ));

        productRepository.save(new Product(
                "Kindle Paperwhite Signature Edition",
                "Kindle Paperwhite Signature Edition to idealny czytnik e-booków dla osób, które cenią sobie wygodę czytania w każdej sytuacji.\n" +
                        "Wyposażony w ekran 6,8 cala z regulowanym oświetleniem oraz technologią E-Ink, oferuje komfort czytania zbliżony do papierowej książki.\n" +
                        "Dzięki pojemnej baterii możesz korzystać z urządzenia przez tygodnie bez konieczności ładowania.\n" +
                        "Dodatkowe funkcje, takie jak bezprzewodowe ładowanie oraz 32 GB pamięci, sprawiają, że jest to jeden z najlepszych czytników na rynku.",
                799.00,
                Arrays.asList("kindle1.jpg", "kindle2.jpg"),
                0,
                3L,
                productCategory6,
                ""
        ));

        productRepository.save(new Product(
                "Dell XPS 13",
                "Dell XPS 13 to laptop, który łączy elegancję z potężną wydajnością. Wyposażony w procesor Intel Core i7 11. generacji, 16 GB pamięci RAM i 512 GB dysk SSD, oferuje szybkie działanie i bezproblemową wielozadaniowość. 13.4-calowy wyświetlacz 4K Ultra HD InfinityEdge zapewnia niesamowitą ostrość i głębię kolorów, a smukła obudowa z aluminium czyni go idealnym towarzyszem podróży. Dzięki swojej kompaktowej budowie i długiemu czasowi pracy na baterii, Dell XPS 13 jest doskonałym wyborem dla profesjonalistów w ruchu, którzy potrzebują niezawodnego sprzętu o wysokiej wydajności.",
                8499.00,
                Arrays.asList("dell1.jpg", "dell2.jpg", "dell3.jpg"),
                0,
                6L,
                productCategory1,
                ""
        ));

        productRepository.save(new Product(
                "HP Spectre x360 14",
                "HP Spectre x360 14 to wszechstronny laptop konwertowalny, który łączy elegancję z funkcjonalnością. Wyposażony w procesor Intel Core i7, 16 GB pamięci RAM oraz 1 TB dysk SSD, oferuje ogromną moc obliczeniową i pojemność na wszystkie Twoje pliki. 13.5-calowy ekran OLED z rozdzielczością 3000x2000 zapewnia doskonałą jakość obrazu oraz żywe kolory, które ożywiają każdy projekt. Dzięki funkcji konwertowalności, laptop można łatwo przekształcić w tablet, co czyni go wszechstronnym narzędziem do pracy, nauki i zabawy. Wyposażony w długi czas pracy na baterii i nowoczesny design, HP Spectre x360 14 jest idealnym wyborem dla osób poszukujących elastyczności i stylu.",
                9999.00,
                Arrays.asList("hp1.jpg", "hp2.jpg"),
                0,
                7L,
                productCategory1,
                ""
        ));

        productRepository.save(new Product(
                "Sony WH-1000XM5",
                "Sony WH-1000XM5 to najnowsze słuchawki bezprzewodowe, które oferują niezrównaną jakość dźwięku oraz wyjątkowy komfort noszenia. Wyposażone w zaawansowaną technologię aktywnej redukcji szumów, pozwalają cieszyć się muzyką w każdym środowisku, eliminując zakłócenia z otoczenia. Dzięki funkcji szybkiego ładowania i 30 godzinom pracy na baterii, możesz cieszyć się ulubioną muzyką przez cały dzień. Słuchawki posiadają również funkcję adaptacyjnego dźwięku, która dostosowuje ustawienia w zależności od Twojego otoczenia, oferując optymalne wrażenia dźwiękowe bez względu na miejsce, w którym się znajdujesz.",
                1499.00,
                Arrays.asList("sony1.jpg", "sony2.jpg", "sony3.jpg", "sony4.jpg"),
                0,
                8L,
                productCategory9,
                ""
        ));

        productRepository.save(new Product(
                "Bose QuietComfort 45",
                "Bose QuietComfort 45 to bezprzewodowe słuchawki, które oferują doskonałą jakość dźwięku oraz zaawansowaną redukcję szumów. Dzięki technologii Acoustic Noise Cancelling, możesz zanurzyć się w ulubionej muzyce bez zakłóceń z otoczenia. Słuchawki wyposażone są w regulację poziomu redukcji szumów, co pozwala na dostosowanie intensywności do Twoich potrzeb. Komfort noszenia zapewniają miękkie nauszniki i regulowany pałąk, a długi czas pracy na baterii i szybkie ładowanie sprawiają, że są idealne na długie podróże oraz codzienne użytkowanie.",
                1399.00,
                Arrays.asList("bose1.jpg", "bose2.jpg", "bose3.jpg"),
                0,
                9L,
                productCategory2,
                ""
        ));

        productRepository.save(new Product(
                "JBL Quantum 800",
                "JBL Quantum 800 to profesjonalne słuchawki gamingowe zaprojektowane, aby zapewnić doskonałe wrażenia dźwiękowe podczas gry. Dzięki technologii JBL QuantumSURROUND oraz aktywnej redukcji szumów, możesz cieszyć się immersyjnym dźwiękiem przestrzennym i wyraźnym komunikatem głosowym. Wbudowany mikrofon na wysięgniku zapewnia czystość rozmów, a możliwość dostosowania ustawień dźwięku sprawia, że każda gra staje się jeszcze bardziej emocjonująca. Ergonomiczne nauszniki i regulowany pałąk zapewniają komfort nawet podczas długich sesji gamingowych.",
                999.00,
                Arrays.asList("jbl1.jpg", "jbl2.jpg", "jbl3.jpg"),
                0,
                10L,
                productCategory2,
                ""
        ));

        productRepository.save(new Product(
                "Samsung Galaxy Tab S8+",
                "Samsung Galaxy Tab S8+ to zaawansowany tablet, który łączy potężną wydajność z eleganckim designem. Wyposażony w ekran Super AMOLED o rozdzielczości 2800x1752 pikseli i przekątnej 12.4 cala, oferuje niesamowitą jakość obrazu oraz żywe kolory. Procesor Snapdragon 8 Gen 1 oraz 8 GB pamięci RAM zapewniają płynne działanie aplikacji i gier, a 128 GB pamięci wewnętrznej zapewnia dużo miejsca na pliki i aplikacje. Tablet obsługuje rysik S Pen oraz klawiaturę, co czyni go wszechstronnym narzędziem do pracy i rozrywki. Bateria o pojemności 10,090 mAh gwarantuje długotrwałe użytkowanie bez potrzeby ładowania.",
                4699.00,
                Arrays.asList("galaxy1.jpg", "galaxy2.jpg", "galaxy3.jpg"),
                0,
                11L,
                productCategory3,
                ""
        ));

        productRepository.save(new Product(
                "Apple iPad Pro 11",
                "Apple iPad Pro 11 to niezwykle wszechstronny tablet, który łączy potężną wydajność z eleganckim designem. Wyposażony w chip M1, 8 GB pamięci RAM i 128 GB pamięci wewnętrznej, oferuje płynne działanie nawet podczas intensywnego użytkowania. Ekran Liquid Retina o rozdzielczości 2388x1668 pikseli i obsługa Apple Pencil 2 czynią go idealnym narzędziem do rysowania, pisania i pracy kreatywnej. Tablet jest również kompatybilny z klawiaturą Magic Keyboard, co czyni go wszechstronnym narzędziem do pracy i zabawy. Długotrwała bateria i wsparcie dla szybkiego ładowania zapewniają wygodę użytkowania przez cały dzień.",
                4299.00,
                Arrays.asList("ipad1.jpg", "ipad2.jpg", "ipad3.jpg"),
                0,
                12L,
                productCategory3,
                ""
        ));

        productRepository.save(new Product(
                "Huawei MatePad Pro",
                "Huawei MatePad Pro to tablet z ekranem 10.8 cala o rozdzielczości 2560x1600 pikseli, który oferuje doskonałą jakość obrazu i wygodę użytkowania. Wyposażony w procesor Kirin 990 oraz 6 GB pamięci RAM, zapewnia płynne działanie i szybką reakcję na wszelkie polecenia. Tablet obsługuje rysik M-Pencil oraz klawiaturę, co czyni go idealnym narzędziem do pracy, rysowania i rozrywki. Bateria o pojemności 7,250 mAh gwarantuje długotrwałe użytkowanie, a elegancka obudowa z metalowego materiału zapewnia trwałość i solidność.",
                3599.00,
                Arrays.asList("huawei1.jpg", "huawei2.jpg", "huawei3.jpg"),
                0,
                13L,
                productCategory3,
                ""
        ));

        productRepository.save(new Product(
                "Samsung Galaxy S23 Ultra",
                "Samsung Galaxy S23 Ultra to flagowy smartfon z potężnym aparatem 200 MP, który zapewnia doskonałą jakość zdjęć i wideo. Wyposażony w procesor Snapdragon 8 Gen 2 oraz 12 GB RAM, oferuje niezrównaną wydajność oraz płynne działanie aplikacji i gier. Ekran Dynamic AMOLED 2X o przekątnej 6.8 cala i rozdzielczości 1440x3088 pikseli oferuje niesamowitą jakość obrazu i żywe kolory. Bateria o pojemności 5,000 mAh gwarantuje długotrwałe użytkowanie, a zaawansowane funkcje fotograficzne i wideo czynią ten telefon idealnym narzędziem do rejestrowania najważniejszych chwil.",
                5499.00,
                Arrays.asList("samsung1.jpg", "samsung2.jpg", "samsung3.jpg", "samsung4.jpg"),
                0,
                14L,
                productCategory6,
                ""
        ));

        productRepository.save(new Product(
                "Google Pixel 7 Pro",
                "Google Pixel 7 Pro to zaawansowany smartfon, który oferuje doskonałą jakość zdjęć dzięki systemowi trzech aparatów oraz procesorowi Google Tensor G2. Wyposażony w ekran AMOLED o przekątnej 6.7 cala i rozdzielczości 1440x3120 pikseli, zapewnia niesamowitą jakość obrazu i płynność działania. Telefon obsługuje najnowsze funkcje systemu Android, oferując szybkie aktualizacje i wsparcie. Bateria o pojemności 5,000 mAh gwarantuje długotrwałe użytkowanie, a zaawansowane funkcje aparatu i oprogramowania czynią ten telefon idealnym narzędziem do codziennego użytku oraz profesjonalnych zdjęć.",
                4699.00,
                Arrays.asList("pixel1.jpg", "pixel2.jpg", "pixel3.jpg"),
                0,
                15L,
                productCategory6,
                ""
        ));

        productRepository.save(new Product(
                "Acer Nitro 5",
                "Laptop Acer Nitro 5 to potężne narzędzie zaprojektowane z myślą o graczach, którzy oczekują najwyższej wydajności i jakości. Wyposażony w procesor Intel Core i7 oraz kartę graficzną NVIDIA GeForce RTX 3060, oferuje doskonałą wydajność w każdej grze. 15.6-calowy wyświetlacz Full HD z odświeżaniem 144Hz oraz podświetlana klawiatura RGB zapewniają wyjątkowe wrażenia z gry oraz komfort podczas długich sesji. System chłodzenia i efektywność energetyczna czynią go idealnym wyborem dla entuzjastów gier oraz profesjonalistów potrzebujących mocnego sprzętu gamingowego.",
                6999.00,
                Arrays.asList("acer1.jpg", "acer2.jpg", "acer3.jpg"),
                0,
                16L,
                productCategory7,
                ""
        ));

        productRepository.save(new Product(
                "Asus ROG Zephyrus G15",
                "Asus ROG Zephyrus G15 to laptop zaprojektowany dla najbardziej wymagających graczy, który łączy potężną wydajność z eleganckim designem. Wyposażony w procesor AMD Ryzen 9 oraz kartę graficzną NVIDIA GeForce RTX 3070, oferuje doskonałe osiągi w każdej grze. 15.6-calowy ekran WQHD z odświeżaniem 165Hz zapewnia wyjątkową płynność i dokładność obrazu, a system chłodzenia i podświetlana klawiatura RGB zapewniają komfort i efektywność podczas długich sesji gamingowych. Dzięki smukłej budowie i długiemu czasowi pracy na baterii, laptop jest idealnym wyborem dla graczy i profesjonalistów w ruchu.",
                9999.00,
                Arrays.asList("asus1.jpg", "asus2.jpg", "asus3.jpg"),
                0,
                17L,
                productCategory1,
                ""
        ));

        productRepository.save(new Product(
                "Razer BlackShark V2 Pro",
                "Słuchawki Razer BlackShark V2 Pro oferują doskonałą jakość dźwięku dzięki technologii THX Spatial Audio oraz mikrofonowi z funkcją redukcji szumów. Wyposażone w przełączniki Triforce Titanium i ergonomiczne nauszniki, zapewniają komfort i precyzję podczas długich sesji gamingowych. Technologia THX Spatial Audio umożliwia precyzyjne określenie kierunku dźwięku, co zwiększa immersję i reakcję w grach. Słuchawki są również wyposażone w wygodny system regulacji i długi czas pracy na baterii, co czyni je idealnym wyborem dla zapalonych graczy.",
                1299.00,
                Arrays.asList("razer1.jpg", "razer2.jpg", "razer3.jpg"),
                0,
                18L,
                productCategory2,
                ""
        ));

        productRepository.save(new Product(
                "HyperX Cloud II Wireless",
                "HyperX Cloud II Wireless to bezprzewodowe słuchawki gamingowe zaprojektowane z myślą o wygodzie i doskonałej jakości dźwięku. Wyposażone w technologię HyperX Dual Chamber, oferują czysty i dynamiczny dźwięk, który przenosi Cię w sam środek akcji. Dzięki długiemu czasowi pracy na baterii oraz wygodnym nausznikom, słuchawki są idealne do długich sesji gamingowych. System bezprzewodowy zapewnia swobodę ruchów, a mikrofon z funkcją redukcji szumów zapewnia czystość rozmów z drużyną.",
                899.00,
                Arrays.asList("hyperx1.jpg", "hyperx2.jpg", "hyperx3.jpg"),
                0,
                19L,
                productCategory2,
                ""
        ));

        productRepository.save(new Product(
                "Lenovo IdeaPad 5",
                "Lenovo IdeaPad 5 to wszechstronny laptop zaprojektowany dla osób szukających dobrego stosunku jakości do ceny. Wyposażony w procesor AMD Ryzen 7, 16 GB pamięci RAM i 512 GB SSD, oferuje doskonałą wydajność w codziennym użytkowaniu. 15.6-calowy ekran Full HD zapewnia wyraźny obraz, a długi czas pracy na baterii i ergonomiczna klawiatura sprawiają, że laptop jest wygodny zarówno do pracy, jak i rozrywki. Smukła obudowa i nowoczesny design sprawiają, że IdeaPad 5 jest idealnym wyborem dla studentów i profesjonalistów.",
                3599.00,
                Arrays.asList("lenovo1.jpg", "lenovo2.jpg", "lenovo3.jpg"),
                0,
                20L,
                productCategory1,
                ""
        ));
        productRepository.save(new Product(
                "Telewizor Samsung",
                "Lenovo IdeaPad 5 to wszechstronny laptop zaprojektowany dla osób szukających dobrego stosunku jakości do ceny. Wyposażony w procesor AMD Ryzen 7, 16 GB pamięci RAM i 512 GB SSD, oferuje doskonałą wydajność w codziennym użytkowaniu. 15.6-calowy ekran Full HD zapewnia wyraźny obraz, a długi czas pracy na baterii i ergonomiczna klawiatura sprawiają, że laptop jest wygodny zarówno do pracy, jak i rozrywki. Smukła obudowa i nowoczesny design sprawiają, że IdeaPad 5 jest idealnym wyborem dla studentów i profesjonalistów.",
                3599.00,
                Arrays.asList("lenovo1.jpg", "lenovo2.jpg", "lenovo3.jpg"),
                0,
                20L,
                productCategory8,
                ""
        ));
    }
}
