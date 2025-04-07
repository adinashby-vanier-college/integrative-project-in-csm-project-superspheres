package edu.vanier.template.helpers;

import edu.vanier.template.sim.BodyHandler;
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
        List<Shape3D> allBodies = new ArrayList<>(bodyHandler.getBodies());
        clearSpecificBodies(allBodies);
    }
    public  void clearSpecificBodies(Collection<Shape3D> bodiesToRemove){
        bodyHandler.getBodies().removeAll(bodiesToRemove);
        groupRootNode.getChildren().removeAll(bodiesToRemove);
    }

    public void clearBody(Shape3D shape3D){
        bodyHandler.getBodies().remove(shape3D);
        groupRootNode.getChildren().remove(shape3D);
    }
}
