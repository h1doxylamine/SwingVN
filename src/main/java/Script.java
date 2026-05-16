import managers.ScriptManager;
import screens.components.Image;
import screens.components.SoundManager;

import java.awt.*;

public class Script extends ScriptManager {

    @Override
    public void run() {

        /* =========================
           INITIALISATION
        ========================= */

        initButtons();

        Image arriereplan = background("bg");
        arriereplan.setScale(1.9f);

        playSound("liebestraum");

        Image arriereplan2 = background("bg2");
        arriereplan2.setScale(1.9f);

        Image rika = image("rika");
        rika.setPosition(600, 400);
        rika.setScale(1.5f);

        Chara Rika = character("Rika", Color.BLUE);

        fadeShow(arriereplan, 400);

        /* =========================
           DÉBUT
        ========================= */

        label("debut");

        speak(null, "La nuit est tombée depuis longtemps.");
        speak(null, "La ville dort, mais pas Rika.");
        speak(null, "Dans cette chambre silencieuse, une pensée refuse de la laisser tranquille.");

        instantPass();
        fadeShow(rika, 400);

        fadeHide(arriereplan, 400);
        fadeShow(arriereplan2, 400);

        speak(Rika, "Encore ce rêve...");
        speak(Rika, "Toujours le même.");
        speak(Rika, "Cette sensation de chute, juste avant le réveil.");

        jump("doute");

        /* =========================
           DOUTE
        ========================= */

        label("doute");

        updateScale(rika, 1.7f);

        speak(null, "Rika détourne lentement le regard.");
        speak(null, "Elle a l’étrange impression d’être observée.");

        speak(Rika, "Est-ce que je deviens folle ?");
        speak(Rika, "Ou est-ce que je commence enfin à comprendre ?");

        choice("Que décide Rika ?")
                .option("Ignorer cette sensation", "fuite")
                .option("Faire face à ses souvenirs", "revelation")
                .build();

        /* =========================
           FUITE
        ========================= */

        label("fuite");

        updateScale(rika, 1.4f);

        speak(null, "Rika ferme les yeux très fort.");
        speak(null, "Elle tente de chasser cette pensée dérangeante.");

        speak(Rika, "Ce n’est rien...");
        speak(Rika, "Je suis juste fatiguée.");

        speak(null, "Mais le malaise persiste, enfoui quelque part au fond d’elle.");

        jump("fin");

        /* =========================
           RÉVÉLATION
        ========================= */

        label("revelation");

        updateScale(rika, 2.2f);

        speak(null, "Un souvenir refait surface.");
        speak(null, "Clair. Précis. Impossible à ignorer.");

        speak(Rika, "Ce jour-là...");
        speak(Rika, "On m’a dit que tout irait bien.");
        speak(Rika, "Mais personne ne m’a jamais demandé si je voulais connaître la vérité.");

        jump("decision");

        /* =========================
           DÉCISION
        ========================= */

        label("decision");

        updateScale(rika, 2.6f);

        speak(null, "Le silence devient lourd.");
        speak(null, "Rika serre les poings.");

        speak(Rika, "Si je ferme les yeux, tout redeviendra normal.");
        speak(Rika, "Mais si je les ouvre...");
        speak(Rika, "Je ne pourrai plus jamais faire semblant.");

        choice("Ouvre-t-elle les yeux ?")
                .option("Oui", "acceptation")
                .option("Non", "fuite")
                .build();

        /* =========================
           ACCEPTATION
        ========================= */

        label("acceptation");

        updateScale(rika, 3.0f);

        speak(null, "Elle inspire profondément.");
        speak(Rika, "D’accord...");
        speak(Rika, "Je veux savoir.");
        speak(Rika, "Même si ça fait mal.");

        jump("fin");

        /* =========================
           FIN COMMUNE
        ========================= */

        label("fin");

        fadeHide(arriereplan2, 400);

        speak(null, "À cet instant précis, quelque chose change.");
        speak(null, "Pas dans le monde.");
        speak(null, "Mais en elle.");

        speak(Rika, "Quoi qu’il arrive maintenant...");
        speak(Rika, "Je ne reculerai plus.");

        instantPass();
        fadeHide(rika, 400);
        speak(null, "Fin.");
    }
}
