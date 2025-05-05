package edu.vanier.template.helpers;

import edu.vanier.template.controllers.SimulationMainPageController;
import edu.vanier.template.sim.Body;
import edu.vanier.template.sim.BodyHandler;
import edu.vanier.template.sim.SolarSystemAssets;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.shape.Shape3D;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class GarbageCollector {
    private Group groupRootNode;
    private BodyHandler bodyHandler;

    public  GarbageCollector(Group group, BodyHandler bodyHandler){
        this.groupRootNode = group;
        this.bodyHandler = bodyHandler;
    }

    public void clearAll(){
        SimulationMainPageController.getLastInstance().pauseSimulation();
        ListViewBodies.listView.getItems().clear();
        SolarSystemAssets.solarSystemBodies.clear();
        List<Shape3D> allBodies = new ArrayList<>(bodyHandler.getBodies());
        for (Shape3D shape3D : allBodies){
            if(shape3D instanceof Body){
                removeBodyCompletely((Body) shape3D);
            }
        }
        groupRootNode.getChildren().clear();
    }
    public  void clearSpecificBodies(Collection<Shape3D> bodiesToRemove){
        bodyHandler.getBodies().removeAll(bodiesToRemove);
        groupRootNode.getChildren().removeAll(bodiesToRemove);
    }
    public void removeBodyCompletely(Body body){
        body.removeBody();
        if(body == null) return;

        bodyHandler.getBodies().remove(body);

                if(body.getTrail() != null){
                    body.getTrail().setActive(false);
                    Platform.runLater(()->{
                        if(body.getParent() != null){
                            ((Group) body.getParent()).getChildren().removeAll(body.getTrail().getTrailParticles());
                        }

                    });
                }
        Platform.runLater(()->{
            groupRootNode.getChildren().remove(body);

            if ((body.nameLabel != null)){
                groupRootNode.getChildren().remove(body.nameLabel);
            }
        });
    }

    public void clearBody(Shape3D shape3D){
        bodyHandler.getBodies().remove(shape3D);
        groupRootNode.getChildren().remove(shape3D);
    }
}
