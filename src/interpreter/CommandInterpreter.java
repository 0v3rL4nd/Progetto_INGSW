package interpreter;

import command.CreateObjectCommand;
import command.HistoryCommandHandler;
import shapes.controller.GraphicObjectController;
import shapes.model.AbstractGraphicObject;
import shapes.model.CircleObject;
import shapes.model.ImageObject;
import shapes.model.RectangleObject;

import javax.swing.*;
import java.awt.Point;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class CommandInterpreter {
    private final HistoryCommandHandler handler;
    private final GraphicObjectController controller;
    private final Map<String, AbstractGraphicObject> objects;
    private int idCounter;

    public CommandInterpreter(HistoryCommandHandler handler, GraphicObjectController controller) {
        this.handler = handler;
        this.controller = controller;
        this.objects = new HashMap<>();
        this.idCounter = 0;
    }

    public String executeCommand(String command) {
        String[] parts = command.split("\\s+");
        String action = parts[0];

        switch (action) {
            case "new":
                return createObject(parts);
            case "del":
                return deleteObject(parts[1]);
            case "mv":
                return moveObject(parts[1], parsePoint(parts[2]));
            case "mvoff":
                return moveObjectOffset(parts[1], parsePoint(parts[2]));
            case "scale":
                return scaleObject(parts[1], Float.parseFloat(parts[2]));
            case "ls":
                return listObjects(parts[1]);
            case "grp":
                return groupObjects(parts[1]);
            case "ungrp":
                return ungroupObjects(parts[1]);
            case "area":
                return calculateArea(parts[1]);
            case "perimeter":
                return calculatePerimeter(parts[1]);
            default:
                return "Unknown command: " + command;
        }
    }

    private String createObject(String[] parts) {
        String type = parts[1];
        AbstractGraphicObject obj;
        switch (type) {
            case "circle":
                float radius = Float.parseFloat(parts[2]);
                Point position = parsePoint(parts[3]);
                obj = new CircleObject(position, radius);
                break;
            case "rectangle":
                Point topLeft = parsePoint(parts[2]);
                Point bottomRight = parsePoint(parts[3]);
                obj = new RectangleObject(topLeft, bottomRight.x - topLeft.x, bottomRight.y - topLeft.y);
                break;
            case "img":
                File file = new File(parts[2]);
                Point pos = parsePoint(parts[3]);
                obj = new ImageObject(new ImageIcon(file.getPath()), pos);
                break;
            default:
                return "Unknown type: " + type;
        }

        String id = "id" + (idCounter++);
        objects.put(id, obj);
        handler.addCommand(new CreateObjectCommand(obj, controller));
        return "Created " + type + " with ID " + id;
    }

    private String deleteObject(String id) {
        AbstractGraphicObject obj = objects.remove(id);
        if (obj == null) {
            return "Object with ID " + id + " not found";
        }
        handler.addCommand(new DeleteObjectCommand(obj, controller));
        return "Deleted object with ID " + id;
    }

    private String moveObject(String id, Point newPosition) {
        AbstractGraphicObject obj = objects.get(id);
        if (obj == null) {
            return "Object with ID " + id + " not found";
        }
        handler.addCommand(new MoveObjectCommand(obj, newPosition));
        return "Moved object with ID " + id;
    }

    private String moveObjectOffset(String id, Point offset) {
        AbstractGraphicObject obj = objects.get(id);
        if (obj == null) {
            return "Object with ID " + id + " not found";
        }
        Point newPosition = new Point(obj.getPosition().x + offset.x, obj.getPosition().y + offset.y);
        handler.addCommand(new MoveObjectCommand(obj, newPosition));
        return "Moved object with ID " + id;
    }

    private String scaleObject(String id, float scaleFactor) {
        AbstractGraphicObject obj = objects.get(id);
        if (obj == null) {
            return "Object with ID " + id + " not found";
        }
        handler.addCommand(new ScaleObjectCommand(obj, scaleFactor));
        return "Scaled object with ID " + id + " by factor " + scaleFactor;
    }

    private String listObjects(String criterion) {
        // Implementare il metodo per restituire una lista di oggetti in base al criterio
        return "Listing objects based on " + criterion;
    }

    private String groupObjects(String ids) {
        // Implementare il metodo per creare un gruppo di oggetti
        return "Grouped objects: " + ids;
    }

    private String ungroupObjects(String id) {
        // Implementare il metodo per rimuovere un gruppo di oggetti
        return "Ungrouped object group with ID " + id;
    }

    private String calculateArea(String criterion) {
        // Implementare il metodo per calcolare l'area degli oggetti in base al criterio
        return "Calculated area based on " + criterion;
    }

    private String calculatePerimeter(String criterion) {
        // Implementare il metodo per calcolare il perimetro degli oggetti in base al criterio
        return "Calculated perimeter based on " + criterion;
    }

    private Point parsePoint(String str) {
        str = str.replace("(", "").replace(")", "");
        String[] coords = str.split(",");
        return new Point(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]));
    }

    }

