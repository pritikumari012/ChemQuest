package com.chemquest.ui.apparatus;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;

import java.awt.Dimension;
import java.util.List;

/** Rotatable JavaFX beaker and illustrative particle model for the virtual lab. */
public class VirtualReactionView extends JFXPanel {
    private final int viewWidth;
    private final int viewHeight;
    private Group particles;
    private Group liquid;
    private Rotate rotateX;
    private Rotate rotateY;

    public VirtualReactionView(int width, int height) {
        viewWidth = width;
        viewHeight = height;
        setPreferredSize(new Dimension(width, height));
        setMinimumSize(new Dimension(300, 250));
        setOpaque(true);
        setBackground(new java.awt.Color(17, 26, 38));
        Platform.runLater(this::createScene);
    }

    public void showReaction(List<String> reactants, List<String> products, double progress) {
        showReaction(reactants, products, progress, 7, 35, false);
    }

    public void showReaction(List<String> reactants, List<String> products, double progress,
                             int pH, int liquidLevel, boolean heating) {
        List<String> reactantCopy = List.copyOf(reactants);
        List<String> productCopy = List.copyOf(products);
        Platform.runLater(() -> updateModel(reactantCopy, productCopy, progress, pH, liquidLevel, heating));
    }

    private void createScene() {
        Group root = new Group();
        Group vessel = new Group();
        particles = new Group();
        liquid = new Group();
        rotateX = new Rotate(-12, Rotate.X_AXIS);
        rotateY = new Rotate(-22, Rotate.Y_AXIS);
        Group model = new Group(vessel, particles);
        model.getTransforms().addAll(rotateX, rotateY);

        Color glass = Color.rgb(122, 224, 242, 0.24);
        Color rim = Color.rgb(130, 230, 250, 0.8);
        Cylinder body = cylinder(130, 270, 0, 12, 0, glass);
        Cylinder top = cylinder(135, 9, 0, 148, 0, rim);
        Cylinder base = cylinder(126, 8, 0, -126, 0, rim);
        vessel.getChildren().addAll(new AmbientLight(Color.rgb(205, 225, 238)),
                body, top, base, liquid);
        addGraduations(vessel);
        root.getChildren().add(model);

        SubScene subScene = new SubScene(root, viewWidth, viewHeight, true, SceneAntialiasing.BALANCED);
        subScene.setFill(Color.rgb(17, 26, 38));
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(1800);
        camera.setTranslateZ(-680);
        subScene.setCamera(camera);
        subScene.widthProperty().addListener((observable, oldValue, newValue) ->
                subScene.setWidth(Math.max(1, newValue.doubleValue())));
        subScene.heightProperty().addListener((observable, oldValue, newValue) ->
                subScene.setHeight(Math.max(1, newValue.doubleValue())));

        double[] previous = new double[2];
        subScene.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            previous[0] = event.getSceneX();
            previous[1] = event.getSceneY();
        });
        subScene.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
            rotateY.setAngle(rotateY.getAngle() + event.getSceneX() - previous[0]);
            rotateX.setAngle(rotateX.getAngle() - event.getSceneY() + previous[1]);
            previous[0] = event.getSceneX();
            previous[1] = event.getSceneY();
        });

        setScene(new Scene(new Group(subScene), viewWidth, viewHeight, true));
    }

    private void updateModel(List<String> reactants, List<String> products, double progress,
                             int pH, int liquidLevel, boolean heating) {
        if (particles == null) {
            return;
        }
        particles.getChildren().clear();
        liquid.getChildren().clear();
        if (progress <= 0 || reactants.isEmpty()) {
            return;
        }
        double height = 220 * Math.max(10, Math.min(90, liquidLevel)) / 100.0;
        double centerY = -13 - height / 2;
        Color liquidColor = phColor(pH);
        liquid.getChildren().addAll(
                cylinder(115, height, 0, centerY, 0, liquidColor),
                cylinder(114, 5, 0, centerY - height / 2, 0, liquidColor.brighter()));

        List<String> visible = progress >= 1 && !products.isEmpty() ? products : reactants;
        List<Color> colors = visible.stream().map(VirtualReactionView::colorFor).toList();
        int particleCount = Math.max(visible.size() * 2, 4);
        boolean gas = visible.stream().anyMatch(formula -> formula.equals("CO2")
                || formula.equals("H2") || formula.equals("O2"));
        for (int i = 0; i < particleCount; i++) {
            Color color = colors.get(i % colors.size());
            double x = ((i % 3) - 1) * 48 + (i % 2 == 0 ? -8 : 8);
            double y = gas ? -70 - (i / 3) * 36 : centerY + (i / 3) * 24;
            double z = ((i % 4) - 1.5) * 20;
            Sphere particle = new Sphere(gas ? 11 : 10, 20);
            particle.setMaterial(new PhongMaterial(color));
            particle.setTranslateX(x);
            particle.setTranslateY(y);
            particle.setTranslateZ(z);
            particles.getChildren().add(particle);
        }
        if (heating) {
            for (int i = -1; i <= 1; i++) {
                Cylinder flame = new Cylinder(11, 42, 18);
                flame.setMaterial(new PhongMaterial(Color.rgb(255, 139, 55, 0.9)));
                flame.setTranslateX(i * 35);
                flame.setTranslateY(157);
                particles.getChildren().add(flame);
            }
        }
    }

    private Color phColor(int pH) {
        float hue = (float) Math.max(0, Math.min(1, 0.98 - pH * 0.065));
        return Color.hsb(hue * 360, 0.78, 0.9, 0.78);
    }

    private void addGraduations(Group vessel) {
        for (int i = 0; i < 7; i++) {
            Cylinder mark = cylinder(2, 20, 111, 95 - i * 27, 0, Color.rgb(206, 232, 238, 0.8));
            vessel.getChildren().add(mark);
        }
    }

    private Cylinder cylinder(double radius, double height, double x, double y, double z, Color color) {
        Cylinder shape = new Cylinder(radius, height, 48);
        shape.setMaterial(new PhongMaterial(color));
        shape.setTranslateX(x);
        shape.setTranslateY(-y);
        shape.setTranslateZ(z);
        return shape;
    }

    private static Color colorFor(String formula) {
        return switch (formula) {
            case "CaCO3", "CaO", "NaCl", "AgCl" -> Color.rgb(239, 230, 202);
            case "CO2", "H2", "O2" -> Color.rgb(225, 245, 255);
            case "HCl" -> Color.rgb(242, 185, 90);
            case "NaOH" -> Color.rgb(90, 202, 180);
            case "CuSO4" -> Color.rgb(60, 130, 238);
            case "Fe", "Mg" -> Color.rgb(180, 193, 205);
            case "FeSO4" -> Color.rgb(126, 190, 130);
            case "AgNO3" -> Color.rgb(225, 225, 240);
            case "H2O2" -> Color.rgb(154, 223, 239);
            default -> Color.rgb(65, 190, 200);
        };
    }
}
