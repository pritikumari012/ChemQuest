package com.chemquest.ui.apparatus;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;

import java.awt.Dimension;

/** JavaFX 3D preview assembled from reusable lab-apparatus shapes. */
public class Apparatus3DView extends JFXPanel {
    private static final Color GLASS = Color.rgb(104, 215, 238, 0.46);
    private static final Color GLASS_EDGE = Color.rgb(112, 225, 247, 0.9);
    private static final Color METAL = Color.rgb(174, 193, 205);
    private static final Color DARK_METAL = Color.rgb(65, 91, 109);
    private static final Color LIQUID = Color.rgb(42, 190, 197, 0.8);

    public Apparatus3DView(String apparatusName, int width, int height) {
        setPreferredSize(new Dimension(width, height));
        setMinimumSize(new Dimension(width, height));
        setOpaque(true);
        setBackground(new java.awt.Color(17, 26, 38));
        Platform.runLater(() -> setScene(createScene(apparatusName, width, height)));
    }

    private Scene createScene(String name, int width, int height) {
        Group model = new Group();
        String item = name.toLowerCase();

        if (item.contains("burette")) {
            addTube(model, 10, 260);
            addGraduations(model, 10, 260, 8);
            addBox(model, 34, 8, 8, 15, 58, 0, DARK_METAL);
            addBox(model, 8, 36, 8, 36, 78, 0, METAL);
            addCylinder(model, 5, 28, 36, 101, 0, GLASS_EDGE);
        } else if (item.contains("pipette") || item.contains("dropper")) {
            addTube(model, 9, 205);
            addCylinder(model, 5, 42, 0, 125, 0, GLASS_EDGE);
            addSphere(model, 15, 0, -104, 0, Color.rgb(111, 201, 216));
        } else if (item.contains("conical flask")) {
            addCylinder(model, 19, 38, 0, 91, 0, GLASS);
            addCylinder(model, 48, 16, 0, 61, 0, GLASS);
            addCylinder(model, 66, 24, 0, 35, 0, GLASS);
            addCylinder(model, 68, 7, 0, 19, 0, GLASS_EDGE);
            addCylinder(model, 17, 7, 0, 107, 0, GLASS_EDGE);
            addCylinder(model, 53, 8, 0, 31, 0, LIQUID);
        } else if (item.contains("funnel")) {
            addCylinder(model, 47, 10, 0, 66, 0, GLASS_EDGE);
            addCylinder(model, 37, 14, 0, 53, 0, GLASS);
            addCylinder(model, 25, 14, 0, 39, 0, GLASS);
            addCylinder(model, 13, 14, 0, 25, 0, GLASS);
            addCylinder(model, 5, 76, 0, -20, 0, GLASS_EDGE);
        } else if (item.contains("test tube")) {
            addCylinder(model, 22, 126, 0, 7, 0, GLASS);
            addSphere(model, 21, 0, -56, 0, GLASS);
            addCylinder(model, 23, 6, 0, 71, 0, GLASS_EDGE);
            addCylinder(model, 17, 32, 0, -27, 0, LIQUID);
        } else if (item.contains("beaker") || item.contains("gas jar")
                || item.contains("measuring cylinder") || item.contains("evaporating dish")) {
            boolean tall = item.contains("measuring cylinder") || item.contains("gas jar");
            boolean shallow = item.contains("evaporating dish");
            double vesselHeight = shallow ? 32 : (tall ? 198 : 126);
            double radius = shallow ? 64 : (tall ? 34 : 58);
            addCylinder(model, radius, vesselHeight, 0, 0, 0, GLASS);
            addCylinder(model, radius + 3, 7, 0, vesselHeight / 2, 0, GLASS_EDGE);
            addCylinder(model, radius, 6, 0, -vesselHeight / 2, 0, GLASS_EDGE);
            addCylinder(model, radius - 7, Math.max(8, vesselHeight * 0.24), 0,
                    -vesselHeight / 2 + vesselHeight * 0.15, 0, LIQUID);
            if (item.contains("measuring cylinder")) {
                addGraduations(model, radius, vesselHeight, 10);
                addBox(model, 70, 8, 18, 0, -vesselHeight / 2 - 7, 0, GLASS_EDGE);
            } else if (item.contains("beaker")) {
                addBox(model, 20, 9, 5, radius - 8, vesselHeight / 2 + 5, 0, GLASS_EDGE);
            }
        } else if (item.contains("clamp stand")) {
            addBox(model, 142, 12, 72, 0, -110, 0, DARK_METAL);
            addCylinder(model, 6, 235, 0, 12, 0, METAL);
            addBox(model, 88, 8, 8, 24, 76, 0, DARK_METAL);
            addBox(model, 8, 52, 8, 66, 76, 0, METAL);
        } else if (item.contains("tripod")) {
            addBox(model, 106, 8, 86, 0, 60, 0, DARK_METAL);
            addCylinder(model, 5, 120, -42, -4, 0, METAL);
            addCylinder(model, 5, 120, 42, -4, 0, METAL);
            addCylinder(model, 5, 120, 0, -4, 0, METAL);
        } else if (item.contains("wire gauze")) {
            addBox(model, 120, 7, 100, 0, 0, 0, DARK_METAL);
            for (int i = -4; i <= 4; i++) {
                addBox(model, 2, 2, 100, i * 12, 5, 0, METAL);
                addBox(model, 120, 2, 2, 0, 5, i * 12, METAL);
            }
        } else if (item.contains("burner") || item.contains("lamp")) {
            addCylinder(model, 34, 18, 0, -89, 0, DARK_METAL);
            addCylinder(model, 19, 126, 0, -17, 0, METAL);
            addCylinder(model, 23, 12, 0, 47, 0, DARK_METAL);
            addCylinder(model, 12, 38, 0, 72, 0, Color.rgb(245, 163, 63));
            addSphere(model, 13, 0, 98, 0, Color.rgb(255, 207, 97));
        } else if (item.contains("thermometer")) {
            addCylinder(model, 8, 184, 0, 0, 0, GLASS_EDGE);
            addCylinder(model, 3, 126, 0, -15, 0, Color.rgb(255, 111, 110));
            addSphere(model, 12, 0, -99, 0, Color.rgb(255, 111, 110));
            addGraduations(model, 8, 166, 9);
        } else if (item.contains("spatula") || item.contains("tongs")) {
            addBox(model, 14, 145, 8, 0, 0, 0, METAL);
            if (item.contains("tongs")) {
                addBox(model, 7, 145, 8, -12, 0, 0, DARK_METAL);
                addBox(model, 7, 145, 8, 12, 0, 0, DARK_METAL);
            }
        } else if (item.contains("mortar") || item.contains("pestle")) {
            addCylinder(model, 55, 54, 0, -53, 0, Color.rgb(204, 211, 216));
            addCylinder(model, 49, 8, 0, -25, 0, Color.rgb(145, 159, 169));
            addCylinder(model, 11, 104, 23, 21, 0, METAL);
        } else if (item.contains("balance")) {
            addBox(model, 108, 18, 76, 0, -77, 0, DARK_METAL);
            addBox(model, 86, 72, 10, 0, -28, 0, METAL);
            addBox(model, 74, 10, 56, 0, 16, 0, GLASS_EDGE);
            addBox(model, 78, 18, 68, 0, 69, 0, METAL);
        } else if (item.contains("watch glass")) {
            addSphere(model, 61, 0, 0, 0, GLASS);
            addCylinder(model, 58, 5, 0, -6, 0, GLASS_EDGE);
        } else if (item.contains("filter paper")) {
            addBox(model, 86, 7, 86, 0, 0, 0, Color.rgb(235, 238, 222));
            addBox(model, 4, 9, 86, 0, 7, 0, Color.rgb(178, 191, 181));
        } else if (item.contains("delivery tube")) {
            addCylinder(model, 9, 145, 0, 12, 0, GLASS_EDGE);
            addCylinder(model, 9, 78, 42, -64, 0, GLASS_EDGE);
            addCylinder(model, 9, 62, 42, -107, 0, GLASS_EDGE);
        } else if (item.contains("syringe")) {
            addCylinder(model, 22, 118, 0, 0, 0, GLASS);
            addCylinder(model, 19, 6, 0, 0, 0, GLASS_EDGE);
            addCylinder(model, 7, 86, 0, -89, 0, METAL);
            addBox(model, 68, 9, 12, 0, -137, 0, DARK_METAL);
        } else if (item.contains("goggles")) {
            addCylinder(model, 35, 10, -39, 0, 0, GLASS_EDGE);
            addCylinder(model, 35, 10, 39, 0, 0, GLASS_EDGE);
            addBox(model, 20, 8, 8, 0, 0, 0, METAL);
            addBox(model, 12, 8, 94, 0, 7, 0, DARK_METAL);
        } else {
            addCylinder(model, 43, 106, 0, 0, 0, GLASS);
            addCylinder(model, 46, 7, 0, 55, 0, GLASS_EDGE);
            addCylinder(model, 35, 7, 0, -55, 0, GLASS_EDGE);
        }

        Rotate rotateX = new Rotate(-12, Rotate.X_AXIS);
        Rotate rotateY = new Rotate(-18, Rotate.Y_AXIS);
        model.getTransforms().addAll(rotateX, rotateY);
        model.setScaleX(0.72);
        model.setScaleY(0.72);
        model.setScaleZ(0.72);
        model.getChildren().addAll(new AmbientLight(Color.rgb(190, 211, 228)), createLight());

        SubScene subScene = new SubScene(model, width, height, true, SceneAntialiasing.BALANCED);
        subScene.setFill(Color.rgb(17, 26, 38));
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(1500);
        camera.setTranslateZ(-510);
        subScene.setCamera(camera);

        final double[] drag = new double[2];
        subScene.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            drag[0] = event.getSceneX();
            drag[1] = event.getSceneY();
        });
        subScene.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
            rotateY.setAngle(rotateY.getAngle() + event.getSceneX() - drag[0]);
            rotateX.setAngle(rotateX.getAngle() - event.getSceneY() + drag[1]);
            drag[0] = event.getSceneX();
            drag[1] = event.getSceneY();
        });

        return new Scene(new Group(subScene), width, height, true);
    }

    private PointLight createLight() {
        PointLight light = new PointLight(Color.rgb(168, 230, 255));
        light.setTranslateX(-130);
        light.setTranslateY(-160);
        light.setTranslateZ(-180);
        return light;
    }

    private void addTube(Group group, double radius, double height) {
        addCylinder(group, radius, height, 0, 0, 0, GLASS);
        addCylinder(group, radius + 2, 6, 0, height / 2, 0, GLASS_EDGE);
        addCylinder(group, radius - 2, 5, 0, -height / 2, 0, GLASS_EDGE);
    }

    private void addGraduations(Group group, double radius, double height, int marks) {
        for (int i = 0; i < marks; i++) {
            double y = height / 2 - 15 - i * (height - 30) / Math.max(1, marks - 1);
            addBox(group, radius * 0.72, 2, 3, radius * 0.56, y, 0, GLASS_EDGE);
        }
    }

    private void addCylinder(Group group, double radius, double height, double x, double y, double z, Color color) {
        Cylinder shape = new Cylinder(radius, height, 32);
        shape.setMaterial(new PhongMaterial(color));
        shape.setTranslateX(x);
        shape.setTranslateY(-y);
        shape.setTranslateZ(z);
        group.getChildren().add(shape);
    }

    private void addBox(Group group, double width, double height, double depth,
                        double x, double y, double z, Color color) {
        Box shape = new Box(width, height, depth);
        shape.setMaterial(new PhongMaterial(color));
        shape.setTranslateX(x);
        shape.setTranslateY(-y);
        shape.setTranslateZ(z);
        group.getChildren().add(shape);
    }

    private void addSphere(Group group, double radius, double x, double y, double z, Color color) {
        Sphere shape = new Sphere(radius, 24);
        shape.setMaterial(new PhongMaterial(color));
        shape.setTranslateX(x);
        shape.setTranslateY(-y);
        shape.setTranslateZ(z);
        group.getChildren().add(shape);
    }
}
