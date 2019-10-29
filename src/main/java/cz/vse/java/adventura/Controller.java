package cz.vse.java.adventura;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Controller {

    public static final int SIRKA_IKONY = 45;
    public static final int VYSKA_IKONY = 30;

    @FXML
    private VBox seznamVychodu;
    @FXML
    private VBox seznamPredmetuVMistnosti;
    @FXML
    private VBox seznamPredmetuVBatohu;
    private IHra hra;

    public ImageView obrazekLokace;
    @FXML
    private Label popisLokace;
    @FXML
    private Label jmenoLokace;

    public void setHra(IHra hra) {
        this.hra = hra;
        HerniPlan herniPlan = hra.getHerniPlan();
        Prostor aktualniProstor = herniPlan.getAktualniProstor();
        zmenProstor(aktualniProstor);
    }

    private void zmenProstor(Prostor prostor) {
        hra.zpracujPrikaz("jdi " + prostor.getNazev());
        System.out.println(hra.getHerniPlan().getAktualniProstor().getNazev());

        jmenoLokace.setText(prostor.getNazev());
        popisLokace.setText(prostor.getPopis());

        String nazevObrazku = "/" + prostor.getNazev() + ".jpg";
        Image image = new Image(getClass().getResourceAsStream(nazevObrazku));
        obrazekLokace.setImage(image);

        pridejVychody(prostor);
        pridejPredmety(prostor);
    }

    private void pridejVychody(Prostor prostor) {
        seznamVychodu.getChildren().clear();
        for (Prostor p : prostor.getVychody()) {
            HBox vychod = new HBox();
            vychod.setSpacing(10);
            Label nazevProstoru = new Label(p.getNazev());

            ImageView vychodImageView = new ImageView();
            Image vychodImage = new Image(getClass().getClassLoader().getResourceAsStream("\\" + p.getNazev() + ".jpg"));
            vychodImageView.setFitHeight(VYSKA_IKONY);
            vychodImageView.setFitWidth(SIRKA_IKONY);
            vychodImageView.setImage(vychodImage);


            vychod.getChildren().addAll(vychodImageView, nazevProstoru);

            seznamVychodu.getChildren().add(vychod);
            vychod.setOnMouseClicked(event -> {
                zmenProstor(p);
            });
        }
    }

    private void pridejPredmety(Prostor prostor) {
        seznamPredmetuVMistnosti.getChildren().clear();

        for (Vec vec : prostor.getSeznamVeci()) {
           pridejPredmetDoMistnosti(vec);
        }
    }

    private void pridejPredmetDoMistnosti(Vec vec) {
        HBox predmet = vytvorHBoxPredmet(vec);

        seznamPredmetuVMistnosti.getChildren().add(predmet);

        predmet.setOnMouseClicked(event -> {
            if (vec.jePrenositelna()) {
                hra.zpracujPrikaz("seber " + vec.getJmeno());
                HBox vecVBatohu = vytvorHBoxPredmet(vec);
                seznamPredmetuVBatohu.getChildren().add(vecVBatohu);
                pridejPredmety(hra.getHerniPlan().getAktualniProstor());


                vecVBatohu.setOnMouseClicked(event1 -> {
                    hra.zpracujPrikaz("poloz "+vec.getJmeno());
                    seznamPredmetuVBatohu.getChildren().remove(vecVBatohu);
                    pridejPredmetDoMistnosti(vec);
                });
            }
        });
    }

    private HBox vytvorHBoxPredmet(Vec vec) {
        HBox predmet = new HBox();
        predmet.setSpacing(10);
        Label nazevVeci = new Label(vec.getJmeno());

        ImageView predmetImageView = new ImageView();
        Image predmetImage = new Image(getClass().getClassLoader().getResourceAsStream("\\" + vec.getJmeno() + ".jpg"));
        predmetImageView.setFitHeight(VYSKA_IKONY);
        predmetImageView.setFitWidth(SIRKA_IKONY);
        predmetImageView.setImage(predmetImage);


        predmet.getChildren().addAll(predmetImageView, nazevVeci);

        return predmet;
    }

}
